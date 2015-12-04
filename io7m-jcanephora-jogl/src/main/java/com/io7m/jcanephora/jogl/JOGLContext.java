/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLContextUsableType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

final class JOGLContext implements JCGLContextType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLContext.class);
  }

  private final GLContext              context;
  private final JOGLInterfaceGL33      gl33;
  private final JCGLImplementationJOGL implementation;
  private final GL3                    gl3;
  private final String                 name;
  private       boolean                destroyed;

  JOGLContext(
    final JCGLImplementationJOGL i,
    final GLContext c,
    final Function<GLContext, GL3> gl_supplier,
    final String in_name)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.name = NullCheck.notNull(in_name);
    this.gl3 = NullCheck.notNull(NullCheck.notNull(gl_supplier).apply(c));
    this.gl33 = new JOGLInterfaceGL33(this);
    this.destroyed = false;
    this.implementation = i;
    this.context.attachObject("JCGLContextType", this);
    JOGLContext.LOG.debug("created context {}", this.name);
    JOGLContext.LOG.trace("context: {}", c);
  }

  GLContext getContext()
  {
    return this.context;
  }

  @Override public String contextGetName()
  {
    return this.name;
  }

  @Override public List<JCGLContextUsableType> contextGetShares()
  {
    this.checkNotDestroyed();

    final List<GLContext> s = this.context.getCreatedShares();
    final List<JCGLContextUsableType> xs = new ArrayList<>(s.size());

    for (final GLContext c : s) {
      final Object o = c.getAttachedObject("JCGLContextType");
      if (o != null) {
        if (o instanceof JCGLContextType) {
          xs.add((JCGLContextType) o);
        } else {
          JOGLContext.LOG.warn(
            "object on context {} has key '{}' but is of type '{}'",
            c,
            "JCGLContextType",
            o.getClass());
        }
      }
    }

    return xs;
  }

  @Override public boolean contextIsSharedWith(final JCGLContextUsableType c)
  {
    this.checkNotDestroyed();

    if (this.context.isShared()) {
      if (c instanceof JOGLContext) {
        final JOGLContext jc = (JOGLContext) c;
        jc.checkNotDestroyed();
        return this.context.getCreatedShares().contains(jc.context);
      }
    }

    return false;
  }

  private void checkNotDestroyed()
  {
    if (this.isDeleted()) {
      throw new JCGLExceptionDeleted(
        String.format("Context %s is destroyed", this));
    }
  }

  @Override public boolean contextIsCurrent()
  {
    this.checkNotDestroyed();
    return this.context.isCurrent();
  }

  @Override public void contextMakeCurrent()
  {
    this.checkNotDestroyed();
    JOGLContext.LOG.trace("make current");
    this.context.makeCurrent();
  }

  @Override public void contextReleaseCurrent()
  {
    this.checkNotDestroyed();
    JOGLContext.LOG.trace("release current");
    if (this.context.isCurrent()) {
      this.context.release();
    }
  }

  @Override public JCGLInterfaceGL33Type contextGetGL33()
  {
    this.checkNotDestroyed();
    return this.gl33;
  }

  @Override public JCGLImplementationType contextGetImplementation()
  {
    this.checkNotDestroyed();
    return this.implementation;
  }

  @Override public void contextDestroy()
    throws JCGLExceptionDeleted
  {
    this.checkNotDestroyed();
    this.context.destroy();
    this.destroyed = true;
  }

  @Override public boolean isDeleted()
  {
    return this.destroyed;
  }

  GL3 getGL3()
  {
    return this.gl3;
  }
}
