/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

public final class JCGLImplementationJOGL implements JCGLImplementation
{
  private final @Nonnull Log              log;
  private final @Nonnull GLContext        context;
  private final @Nonnull JCGLInterfaceGLES2 gl_es2;
  private final @Nonnull JCGLInterfaceGLES3 gl_es3;
  private final @Nonnull JCGLInterfaceGL3   gl_3;
  private final @Nonnull JCGLInterfaceGL2   gl_2;

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null</code>.
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public JCGLImplementationJOGL(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");
    this.context = Constraints.constrainNotNull(context, "GL context");

    log.debug("Context is " + context.getGLVersion());

    if (context.isGLES3()) {
      log.debug("Context is GLES3 - creating GLES3 interface");
      this.gl_es3 = new JCGLInterfaceGLES3_JOGL_ES3(context, log);
      this.gl_es2 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    if (context.isGLES2()) {
      log.debug("Context is GLES2 - creating GLES2 interface");
      this.gl_es2 = new JCGLInterfaceGLES2_JOGL_ES2(context, log);
      this.gl_es3 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    /**
     * Context is OpenGL 3.n, where n >= 1?
     */

    if (context.isGL3()) {
      log.debug("Context is GL3, creating OpenGL >= 3.1 interface");
      this.gl_3 = new JCGLInterfaceGL3_JOGL_GL2GL3(context, log);
      this.gl_2 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    /**
     * Context is either 2.1 or 3.0.
     */

    if (context.isGL2()) {
      if (context.getGLVersionNumber().getMajor() == 3) {
        log
          .debug("Context is GL2 but version is 3.0, creating OpenGL >= 3.1 interface");
        this.gl_3 = new JCGLInterfaceGL3_JOGL_GL2GL3(context, log);
        this.gl_2 = null;
        this.gl_es2 = null;
        this.gl_es3 = null;
        return;
      }

      if (context.hasFullFBOSupport() == false) {

        /**
         * Full FBO support is not available, raise an exception to explain
         * what was missing.
         */

        if (context.isExtensionAvailable("GL_ARB_framebuffer_object") == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_ARB_framebuffer_object extension");
        }
        if (context.isExtensionAvailable("GL_EXT_framebuffer_object") == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_object extension");
        }
        if (context.isExtensionAvailable("GL_EXT_framebuffer_multisample") == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_multisample extension");
        }
        if (context.isExtensionAvailable("GL_EXT_framebuffer_blit") == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_blit extension");
        }
        if (context.isExtensionAvailable("GL_EXT_packed_depth_stencil") == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_packed_depth_stencil extension");
        }

        throw new UnreachableCodeException();
      }

      log.debug("Context is GL2, creating OpenGL 2.1 interface");
      this.gl_2 = new JCGLInterfaceGL2_JOGL_GL21(context, log);
      this.gl_3 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    throw new JCGLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public @Nonnull Option<JCGLInterfaceGL2> getGL2()
  {
    if (this.gl_2 != null) {
      return new Option.Some<JCGLInterfaceGL2>(this.gl_2);
    }
    return new Option.None<JCGLInterfaceGL2>();
  }

  @Override public @Nonnull Option<JCGLInterfaceGL3> getGL3()
  {
    if (this.gl_3 != null) {
      return new Option.Some<JCGLInterfaceGL3>(this.gl_3);
    }
    return new Option.None<JCGLInterfaceGL3>();
  }

  @Override public @Nonnull JCGLInterfaceCommon getGLCommon()
  {
    if (this.gl_es3 != null) {
      return this.gl_es3;
    }
    if (this.gl_es2 != null) {
      return this.gl_es2;
    }
    if (this.gl_2 != null) {
      return this.gl_2;
    }
    if (this.gl_3 != null) {
      return this.gl_3;
    }

    throw new UnreachableCodeException();
  }

  @Override public @Nonnull Option<JCGLInterfaceGLES2> getGLES2()
  {
    if (this.gl_es2 != null) {
      return new Option.Some<JCGLInterfaceGLES2>(this.gl_es2);
    }
    return new Option.None<JCGLInterfaceGLES2>();
  }

  @Override public @Nonnull Option<JCGLInterfaceGLES3> getGLES3()
  {
    if (this.gl_es3 != null) {
      return new Option.Some<JCGLInterfaceGLES3>(this.gl_es3);
    }
    return new Option.None<JCGLInterfaceGLES3>();
  }
}
