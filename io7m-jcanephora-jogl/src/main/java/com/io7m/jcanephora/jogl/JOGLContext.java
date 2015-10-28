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
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JOGLContext implements JCGLContextType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLContext.class);
  }

  private final GLContext              context;
  private final JOGLInterfaceGL33      gl33;
  private final JCGLImplementationJOGL implementation;
  private       boolean                destroyed;

  JOGLContext(
    final JCGLImplementationJOGL i,
    final GLContext c)
  {
    this.context = NullCheck.notNull(c);
    this.gl33 = new JOGLInterfaceGL33(this);
    this.destroyed = false;
    this.implementation = i;
  }

  GLContext getContext()
  {
    return this.context;
  }

  @Override public boolean contextIsCurrent()
  {
    return this.context.isCurrent();
  }

  @Override public void contextMakeCurrent()
  {
    JOGLContext.LOG.trace("make current");
    this.context.makeCurrent();
  }

  @Override public void contextReleaseCurrent()
  {
    JOGLContext.LOG.trace("release current");
    this.context.release();
  }

  @Override public JCGLInterfaceGL33Type contextGetGL33()
  {
    return this.gl33;
  }

  @Override public JCGLImplementationType contextGetImplementation()
  {
    return this.implementation;
  }

  @Override public void contextDestroy()
    throws JCGLExceptionDeleted
  {
    if (this.destroyed) {
      throw new JCGLExceptionDeleted("Context is already destroyed");
    }

    this.context.destroy();
    this.destroyed = true;
  }

  @Override public boolean isDeleted()
  {
    return this.destroyed;
  }
}
