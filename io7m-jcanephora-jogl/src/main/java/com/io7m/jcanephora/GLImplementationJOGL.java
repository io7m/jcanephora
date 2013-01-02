/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class GLImplementationJOGL implements GLImplementation
{
  private static boolean isGLES2(
    final @Nonnull GLContext context)
  {
    return context.isGLES() && (context.getGLVersionMajor() == 2);
  }

  private static boolean isGLES3OrNewer(
    final @Nonnull GLContext context)
  {
    return context.isGLES() && (context.getGLVersionMajor() >= 3);
  }

  private static boolean isOpenGL21OrNewer(
    final @Nonnull GLContext context)
  {
    if (context.getGLVersionMajor() == 2) {
      return context.getGLVersionMinor() == 1;
    }
    return context.getGLVersionMajor() >= 3;
  }

  private final @Nonnull Log            log;

  private final @Nonnull GLContext      context;

  private final @Nonnull GLInterfaceES2 gl_es2;

  private final @Nonnull GLInterface3   gl_3;

  public GLImplementationJOGL(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");
    this.context = Constraints.constrainNotNull(context, "GL context");

    log.debug("Context is " + context.getGLVersion());

    if (GLImplementationJOGL.isGLES2(context)) {
      log.debug("Creating GLES2 interface");
      this.gl_es2 = new GLInterfaceES2_JOGLES2(context, log);
      this.gl_3 = null;
      return;
    }

    if (GLImplementationJOGL.isGLES3OrNewer(context)
      || GLImplementationJOGL.isOpenGL21OrNewer(context)) {
      log.debug("Creating OpenGL 3 interface");
      this.gl_3 = new GLInterface_JOGL30(context, log);
      this.gl_es2 = this.gl_3;
      return;
    }

    throw new GLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public @Nonnull GLInterface3 implementationGetGL3()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.implementationProvidesGL3(),
      "Implementation supports OpenGL 3");
    return this.gl_3;
  }

  @Override public @Nonnull GLInterfaceES2 implementationGetGLES2()
  {
    return this.gl_es2;
  }

  @Override public boolean implementationProvidesGL3()
  {
    return this.gl_3 != null;
  }
}
