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

import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGL;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGLType;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;

public final class JOGLTestContexts
{
  private static final     JCGLImplementationJOGLType IMPLEMENTATION;
  private static @Nullable GLOffscreenAutoDrawable    DRAWABLE;

  static {
    IMPLEMENTATION = JCGLImplementationJOGL.getInstance();
  }

  private JOGLTestContexts()
  {

  }

  private static GLOffscreenAutoDrawable createOffscreenDrawable(
    final GLProfile profile,
    final int width,
    final int height)
  {
    final GLCapabilities cap = new GLCapabilities(profile);
    cap.setFBO(true);
    final GLDrawableFactory f = GLDrawableFactory.getFactory(profile);
    final GLOffscreenAutoDrawable k =
      f.createOffscreenAutoDrawable(null, cap, null, width, height);
    k.display();
    return k;
  }

  public static JCGLContextType newGL33Context()
  {
    JOGLTestContexts.destroyCachedContext();

    JOGLTestContexts.DRAWABLE = JOGLTestContexts.createOffscreenDrawable(
      GLProfile.get(GLProfile.GL3), 640, 480);

    final GLContext c = JOGLTestContexts.DRAWABLE.getContext();
    final int r = c.makeCurrent();
    if (r == GLContext.CONTEXT_NOT_CURRENT) {
      throw new AssertionError("Could not make context current");
    }

    try {
      return JOGLTestContexts.IMPLEMENTATION.newContextFrom(c);
    } catch (final JCGLExceptionUnsupported x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static void destroyCachedContext()
  {
    if (JOGLTestContexts.DRAWABLE != null) {
      JOGLTestContexts.DRAWABLE.destroy();
    }
  }
}
