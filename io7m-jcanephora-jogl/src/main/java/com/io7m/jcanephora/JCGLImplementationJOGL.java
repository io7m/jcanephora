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

import java.io.PrintStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;

/**
 * A JOGL-based implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationJOGL implements JCGLImplementation
{
  private final static class DefaultRestrictions implements
    JCGLSoftRestrictions
  {
    public DefaultRestrictions()
    {
      // Nothing
    }

    @Override public boolean restrictExtensionVisibility(
      final @Nonnull String name)
    {
      return true;
    }

    @Override public int restrictTextureUnitCount(
      final int count)
    {
      return count;
    }
  }

  private static final @Nonnull JCGLSoftRestrictions DEFAULT_RESTRICTIONS;

  static {
    DEFAULT_RESTRICTIONS = new DefaultRestrictions();
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull JCGLImplementationJOGL newImplementation(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_NONE,
      null,
      JCGLImplementationJOGL.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithDebugging(
      final @Nonnull GLContext context,
      final @Nonnull Log log)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_DEBUGGING,
      null,
      JCGLImplementationJOGL.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled and restrictions
   * <code>r</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null || r == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndRestrictions(
      final @Nonnull GLContext context,
      final @Nonnull Log log,
      final @Nonnull JCGLSoftRestrictions r)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_DEBUGGING,
      null,
      r);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled, and tracing enabled on
   * <code>stream</code>.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>context == null || log == null || stream == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndTracing(
      final @Nonnull GLContext context,
      final @Nonnull Log log,
      final @Nonnull PrintStream trace)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_TRACING_AND_DEBUGGING,
      trace,
      JCGLImplementationJOGL.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled, and tracing enabled on
   * <code>stream</code>, and restrictions <code>r</code>.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>context == null || log == null || stream == null || r == null</code>
   *           .
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndTracingAndRestrictions(
      final @Nonnull GLContext context,
      final @Nonnull Log log,
      final @Nonnull PrintStream trace,
      final @Nonnull JCGLSoftRestrictions r)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_TRACING_AND_DEBUGGING,
      trace,
      r);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with restrictions <code>r</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>context == null || log == null || r == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithRestrictions(
      final @Nonnull GLContext context,
      final @Nonnull Log log,
      final @Nonnull JCGLSoftRestrictions r)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_NONE,
      null,
      r);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with tracing enabled on <code>stream</code>.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>context == null || log == null || stream == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull JCGLImplementationJOGL newImplementationWithTracing(
    final @Nonnull GLContext context,
    final @Nonnull Log log,
    final @Nonnull PrintStream trace)
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_TRACING,
      trace,
      JCGLImplementationJOGL.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with tracing enabled to stream <code>trace</code>
   * and restrictions <code>r</code>.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>context == null || log == null || trace == null || r == null</code>
   *           .
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationJOGL
    newImplementationWithTracingAndRestrictions(
      final @Nonnull GLContext context,
      final @Nonnull Log log,
      final @Nonnull PrintStream trace,
      final @Nonnull JCGLSoftRestrictions r)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_TRACING,
      trace,
      r);
  }

  private final @Nonnull GLContext          context;
  private final @Nonnull JCGLInterfaceGL2   gl_2;
  private final @Nonnull JCGLInterfaceGL3   gl_3;
  private final @Nonnull JCGLInterfaceGLES2 gl_es2;
  private final @Nonnull JCGLInterfaceGLES3 gl_es3;
  private final @Nonnull Log                log;

  private JCGLImplementationJOGL(
    final @Nonnull GLContext context1,
    final @Nonnull Log log1,
    final @Nonnull JCGLDebugging debug,
    final @CheckForNull PrintStream trace,
    final @Nonnull JCGLSoftRestrictions r)
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log1, "log output"), "jogl30");
    this.context = Constraints.constrainNotNull(context1, "GL context");

    log1.debug("Context is " + context1.getGLVersion());

    if (context1.isGLES3()) {
      log1.debug("Context is GLES3 - creating GLES3 interface");
      this.gl_es3 =
        new JCGLInterfaceGLES3_JOGL_ES3(context1, log1, debug, trace, r);
      this.gl_es2 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    if (context1.isGLES2()) {
      log1.debug("Context is GLES2 - creating GLES2 interface");
      this.gl_es2 =
        new JCGLInterfaceGLES2_JOGL_ES2(context1, log1, debug, trace, r);
      this.gl_es3 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    /**
     * Context is OpenGL 3.n, where n >= 1?
     */

    if (context1.isGL3()) {
      log1.debug("Context is GL3, creating OpenGL >= 3.1 interface");
      this.gl_3 =
        new JCGLInterfaceGL3_JOGL_GL2GL3(context1, log1, debug, trace, r);
      this.gl_2 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    /**
     * Context is either 2.1 or 3.0.
     */

    if (context1.isGL2()) {
      if (context1.getGLVersionNumber().getMajor() == 3) {
        log1
          .debug("Context is GL2 but version is 3.0, creating OpenGL >= 3.1 interface");
        this.gl_3 =
          new JCGLInterfaceGL3_JOGL_GL2GL3(context1, log1, debug, trace, r);
        this.gl_2 = null;
        this.gl_es2 = null;
        this.gl_es3 = null;
        return;
      }

      if (context1.hasFullFBOSupport() == false) {

        /**
         * Full FBO support is not available, raise an exception to explain
         * what was missing.
         */

        if (context1
          .isExtensionAvailable(JCGLExtensionNames.GL_ARB_FRAMEBUFFER_OBJECT) == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_ARB_framebuffer_object extension");
        }
        if (context1
          .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_OBJECT) == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_object extension");
        }
        if (context1
          .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_MULTISAMPLE) == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_multisample extension");
        }
        if (context1
          .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_BLIT) == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_blit extension");
        }
        if (context1
          .isExtensionAvailable(JCGLExtensionNames.GL_EXT_PACKED_DEPTH_STENCIL) == false) {
          throw new JCGLUnsupportedException(
            "Context supports OpenGL 2.1 but does not support the required GL_EXT_packed_depth_stencil extension");
        }

        throw new UnreachableCodeException();
      }

      log1.debug("Context is GL2, creating OpenGL 2.1 interface");
      this.gl_2 =
        new JCGLInterfaceGL2_JOGL_GL21(context1, log1, debug, trace, r);
      this.gl_3 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    throw new JCGLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
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

  @Override public <A, E extends Throwable> A implementationAccept(
    final @Nonnull JCGLImplementationVisitor<A, E> v)
    throws JCGLException,
      ConstraintError,
      E
  {
    Constraints.constrainNotNull(v, "Visitor");

    if (this.gl_es3 != null) {
      return v.implementationIsGLES3(this.gl_es3);
    }
    if (this.gl_es2 != null) {
      return v.implementationIsGLES2(this.gl_es2);
    }
    if (this.gl_3 != null) {
      return v.implementationIsGL3(this.gl_3);
    }
    if (this.gl_2 != null) {
      return v.implementationIsGL2(this.gl_2);
    }

    throw new UnreachableCodeException();
  }
}
