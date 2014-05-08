/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

import java.io.PrintStream;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionUnsupported;
import com.io7m.jcanephora.JCGLExtensionNames;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLImplementationVisitorType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES3Type;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * A JOGL-based implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationJOGL implements JCGLImplementationType
{
  private final static class DefaultRestrictions implements
    JCGLSoftRestrictionsType
  {
    public DefaultRestrictions()
    {
      // Nothing
    }

    @Override public boolean restrictExtensionVisibility(
      final String name)
    {
      return true;
    }

    @Override public int restrictTextureUnitCount(
      final int count)
    {
      return count;
    }
  }

  private static final JCGLSoftRestrictionsType DEFAULT_RESTRICTIONS;

  static {
    DEFAULT_RESTRICTIONS = new DefaultRestrictions();
  }

  private static void announceLackingFBO(
    final GLContext in_context)
    throws JCGLExceptionUnsupported
  {
    /**
     * Full FBO support is not available, raise an exception to explain what
     * was missing.
     */

    if (in_context
      .isExtensionAvailable(JCGLExtensionNames.GL_ARB_FRAMEBUFFER_OBJECT) == false) {
      throw new JCGLExceptionUnsupported(
        "Context supports OpenGL 2.1 but does not support the required GL_ARB_framebuffer_object extension");
    }
    if (in_context
      .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_OBJECT) == false) {
      throw new JCGLExceptionUnsupported(
        "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_object extension");
    }
    if (in_context
      .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_MULTISAMPLE) == false) {
      throw new JCGLExceptionUnsupported(
        "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_multisample extension");
    }
    if (in_context
      .isExtensionAvailable(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_BLIT) == false) {
      throw new JCGLExceptionUnsupported(
        "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_blit extension");
    }
    if (in_context
      .isExtensionAvailable(JCGLExtensionNames.GL_EXT_PACKED_DEPTH_STENCIL) == false) {
      throw new JCGLExceptionUnsupported(
        "Context supports OpenGL 2.1 but does not support the required GL_EXT_packed_depth_stencil extension");
    }
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>.
   * 
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementation(
    final GLContext context,
    final LogUsableType log)
    throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithDebugging(
    final GLContext context,
    final LogUsableType log)
    throws JCGLException
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
   * @param r
   *          A set of restrictions.
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndRestrictions(
      final GLContext context,
      final LogUsableType log,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndTracing(
      final GLContext context,
      final LogUsableType log,
      final PrintStream trace)
      throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @param r
   *          A set of restrictions.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationJOGL
    newImplementationWithDebuggingAndTracingAndRestrictions(
      final GLContext context,
      final LogUsableType log,
      final PrintStream trace,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @param r
   *          A set of restrictions.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithRestrictions(
    final GLContext context,
    final LogUsableType log,
    final JCGLSoftRestrictionsType r)
    throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithTracing(
    final GLContext context,
    final LogUsableType log,
    final PrintStream trace)
    throws JCGLException
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
   * @param context
   *          The context.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @param r
   *          A set of restrictions.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationJOGL
    newImplementationWithTracingAndRestrictions(
      final GLContext context,
      final LogUsableType log,
      final PrintStream trace,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
  {
    return new JCGLImplementationJOGL(
      context,
      log,
      JCGLDebugging.JCGL_TRACING,
      trace,
      r);
  }

  private final GLContext                        context;
  private final @Nullable JCGLInterfaceGL2Type   gl_2;
  private final @Nullable JCGLInterfaceGL3Type   gl_3;
  private final @Nullable JCGLInterfaceGLES2Type gl_es2;
  private final @Nullable JCGLInterfaceGLES3Type gl_es3;

  private final LogUsableType                    log;

  private JCGLImplementationJOGL(
    final GLContext in_context,
    final LogUsableType in_log,
    final JCGLDebugging in_debug,
    final @Nullable PrintStream in_trace,
    final JCGLSoftRestrictionsType in_restrictions)
    throws JCGLException
  {
    this.log = NullCheck.notNull(in_log, "Log").with("jogl");
    this.context = NullCheck.notNull(in_context, "GL context");

    in_log.debug("Context is " + in_context.getGLVersion());

    if (in_context.isGLES3()) {
      in_log.debug("Context is GLES3 - creating GLES3 interface");
      this.gl_es3 =
        new JCGLInterfaceGLES3_JOGL_ES3(
          in_context,
          in_log,
          in_debug,
          in_trace,
          in_restrictions);
      this.gl_es2 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    if (in_context.isGLES2()) {
      in_log.debug("Context is GLES2 - creating GLES2 interface");
      this.gl_es2 =
        new JCGLInterfaceGLES2_JOGL_ES2(
          in_context,
          in_log,
          in_debug,
          in_trace,
          in_restrictions);
      this.gl_es3 = null;
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    /**
     * Context is OpenGL 3.n, where n >= 1?
     */

    if (in_context.isGL3()) {
      in_log.debug("Context is GL3, creating OpenGL >= 3.1 interface");
      this.gl_3 =
        new JCGLInterfaceGL3_JOGL_GL2GL3(
          in_context,
          in_log,
          in_debug,
          in_trace,
          in_restrictions);
      this.gl_2 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    /**
     * Context is either 2.1 or 3.0.
     */

    if (in_context.isGL2()) {
      if (in_context.getGLVersionNumber().getMajor() == 3) {
        in_log
          .debug("Context is GL2 but version is 3.0, creating OpenGL >= 3.1 interface");
        this.gl_3 =
          new JCGLInterfaceGL3_JOGL_GL2GL3(
            in_context,
            in_log,
            in_debug,
            in_trace,
            in_restrictions);
        this.gl_2 = null;
        this.gl_es2 = null;
        this.gl_es3 = null;
        return;
      }

      if (in_context.hasFullFBOSupport() == false) {
        JCGLImplementationJOGL.announceLackingFBO(in_context);
        throw new UnreachableCodeException();
      }

      in_log.debug("Context is GL2, creating OpenGL 2.1 interface");
      this.gl_2 =
        new JCGLInterfaceGL2_JOGL_GL21(
          in_context,
          in_log,
          in_debug,
          in_trace,
          in_restrictions);
      this.gl_3 = null;
      this.gl_es2 = null;
      this.gl_es3 = null;
      return;
    }

    throw new JCGLExceptionUnsupported(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public JCGLInterfaceCommonType getGLCommon()
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
    final JCGLImplementationVisitorType<A, E> v)
    throws JCGLException,
      E
  {
    NullCheck.notNull(v, "Visitor");

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
