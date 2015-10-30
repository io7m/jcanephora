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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGL;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGLType;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public final class JOGLTestContexts
{
  private static final JCGLImplementationJOGLType           IMPLEMENTATION;
  private static final Map<String, GLOffscreenAutoDrawable> CACHED_CONTEXTS;
  private static final Logger                               LOG;
  private static final Function<GLContext, GL3>
                                                            GL_CONTEXT_GL3_SUPPLIER;

  static {
    IMPLEMENTATION = JCGLImplementationJOGL.getInstance();
    CACHED_CONTEXTS = new HashMap<>(32);
    LOG = LoggerFactory.getLogger(JOGLTestContexts.class);
    GL_CONTEXT_GL3_SUPPLIER = c -> {
      final DebugGL3 g = new DebugGL3(c.getGL().getGL3());
      JOGLTestContexts.LOG.trace("supplying GL3: {}", g);
      return g;
    };
  }

  private JOGLTestContexts()
  {

  }

  public static JCGLContextType newGL33Context(final String name)
  {
    final Function<GLContext, GL3> supplier =
      JOGLTestContexts.GL_CONTEXT_GL3_SUPPLIER;
    return JOGLTestContexts.newGL33ContextWithSupplier(name, supplier);
  }

  public static JCGLContextType newGL33ContextWithSupplier(
    final String name,
    final Function<GLContext, GL3> supplier)
  {
    try {
      return JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        name, supplier);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static JCGLContextType newGL33ContextWithSupplierAndErrors(
    final String name,
    final Function<GLContext, GL3> supplier)
    throws JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    final GLContext c = JOGLTestContexts.newGL33Drawable(name);
    return JOGLTestContexts.IMPLEMENTATION.newContextFromWithSupplier(
      c, supplier, name);
  }

  public static GLContext newGL33Drawable(final String name)
  {
    JOGLTestContexts.LOG.debug("creating drawable {}", name);
    JOGLTestContexts.destroyCachedDrawableAndRemove(name);

    final GLProfile profile = GLProfile.get(GLProfile.GL3);
    final GLCapabilities cap = new GLCapabilities(profile);
    cap.setFBO(true);
    final GLDrawableFactory f = GLDrawableFactory.getFactory(profile);
    final GLOffscreenAutoDrawable drawable =
      f.createOffscreenAutoDrawable(null, cap, null, 640, 480);
    drawable.display();

    JOGLTestContexts.CACHED_CONTEXTS.put(name, drawable);

    final GLContext c = drawable.getContext();
    final int r = c.makeCurrent();
    if (r == GLContext.CONTEXT_NOT_CURRENT) {
      throw new AssertionError("Could not make context current");
    }
    return c;
  }

  public static JCGLSharedContextPair<JCGLContextType> newGL33ContextSharedWith(
    final String name,
    final String shared)
  {
    JOGLTestContexts.LOG.debug(
      "creating context {} shared with {}", name, shared);

    JOGLTestContexts.destroyCachedDrawableAndRemove(name);
    JOGLTestContexts.destroyCachedDrawableAndRemove(shared);

    final GLProfile profile = GLProfile.get(GLProfile.GL3);
    final GLCapabilities cap = new GLCapabilities(profile);
    cap.setFBO(true);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(profile);
    final GLOffscreenAutoDrawable master =
      f.createOffscreenAutoDrawable(null, cap, null, 640, 480);
    master.display();

    final GLOffscreenAutoDrawable slave =
      f.createOffscreenAutoDrawable(null, cap, null, 640, 480);
    slave.setSharedAutoDrawable(master);
    slave.display();

    final GLContext master_ctx = master.getContext();
    final GLContext slave_ctx = slave.getContext();
    Assertive.ensure(master_ctx.getCreatedShares().contains(slave_ctx));
    Assertive.ensure(slave_ctx.getCreatedShares().contains(master_ctx));

    try {
      master_ctx.makeCurrent();
      final JCGLContextType jmaster =
        JOGLTestContexts.IMPLEMENTATION.newContextFromWithSupplier(
          master_ctx, JOGLTestContexts.GL_CONTEXT_GL3_SUPPLIER, name);
      slave_ctx.makeCurrent();
      final JCGLContextType jslave =
        JOGLTestContexts.IMPLEMENTATION.newContextFromWithSupplier(
          slave_ctx, JOGLTestContexts.GL_CONTEXT_GL3_SUPPLIER, shared);
      master_ctx.makeCurrent();

      Assertive.require(!JOGLTestContexts.CACHED_CONTEXTS.containsKey(name));
      Assertive.require(!JOGLTestContexts.CACHED_CONTEXTS.containsKey(shared));
      JOGLTestContexts.CACHED_CONTEXTS.put(name, master);
      JOGLTestContexts.CACHED_CONTEXTS.put(shared, slave);

      return new JCGLSharedContextPair<>(jmaster, jmaster, jslave, jslave);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  private static void destroyCachedDrawableAndRemove(final String name)
  {
    if (JOGLTestContexts.CACHED_CONTEXTS.containsKey(name)) {
      JOGLTestContexts.LOG.debug("destroying existing drawable {}", name);
      final GLOffscreenAutoDrawable cached =
        JOGLTestContexts.CACHED_CONTEXTS.get(name);
      cached.destroy();
      JOGLTestContexts.CACHED_CONTEXTS.remove(name);
    }
  }

  public static void closeAllContexts()
  {
    JOGLTestContexts.LOG.debug("cleaning up contexts");

    final Iterator<String> iter =
      JOGLTestContexts.CACHED_CONTEXTS.keySet().iterator();
    while (iter.hasNext()) {
      final String name = iter.next();
      JOGLTestContexts.LOG.debug("destroying drawable {}", name);
      Assertive.require(JOGLTestContexts.CACHED_CONTEXTS.containsKey(name));
      final GLOffscreenAutoDrawable drawable =
        JOGLTestContexts.CACHED_CONTEXTS.get(name);
      drawable.destroy();
      iter.remove();
    }

    JOGLTestContexts.LOG.debug("cleaned up contexts");
  }
}
