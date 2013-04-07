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
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;

/**
 * A JOGL-based implementation of the <code>jcanephora</code> API.
 */

public final class GLImplementationJOGL implements GLImplementation
{
  private final @Nonnull Log              log;
  private final @Nonnull GLContext        context;
  private final @Nonnull GLInterfaceGLES2 gl_es2;
  private final @Nonnull GLInterfaceGL3   gl_3;

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws GLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

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

    if (context.isGLES2()) {
      log.debug("Context is GLES2 - creating GLES2 interface");
      this.gl_es2 = new GLInterfaceGLES2_JOGL_ES2(context, log);
      this.gl_3 = null;
      return;
    }

    if (context.isGL2()) {
      log.debug("Context is GL2, creating OpenGL 2.1-3.0 interface");
      this.gl_3 = new GLInterfaceGL3_JOGL_GL21(context, log);
      this.gl_es2 = null;
      return;
    }

    if (context.isGL3()) {
      log.debug("Context is GL3, creating OpenGL >= 3.1 interface");
      this.gl_3 = new GLInterfaceGL3_JOGL_GL3(context, log);
      this.gl_es2 = null;
      return;
    }

    throw new GLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public @Nonnull Option<GLInterfaceGL3> getGL3()
  {
    if (this.gl_3 != null) {
      return new Option.Some<GLInterfaceGL3>(this.gl_3);
    }
    return new Option.None<GLInterfaceGL3>();
  }

  @Override public @Nonnull GLInterfaceCommon getGLCommon()
  {
    if (this.gl_es2 != null) {
      return this.gl_es2;
    }
    if (this.gl_3 != null) {
      return this.gl_3;
    }

    throw new UnreachableCodeException();
  }

  @Override public @Nonnull Option<GLInterfaceGLES2> getGLES2()
  {
    if (this.gl_es2 != null) {
      return new Option.Some<GLInterfaceGLES2>(this.gl_es2);
    }
    return new Option.None<GLInterfaceGLES2>();
  }
}