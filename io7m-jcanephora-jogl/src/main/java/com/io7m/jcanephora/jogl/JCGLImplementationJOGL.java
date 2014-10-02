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
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * A JOGL-based implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationJOGL implements JCGLImplementationType
{
  @SuppressWarnings("synthetic-access") private static final class Builder implements
    JCGLImplementationJOGLBuilderType
  {
    private boolean                  caching;
    private boolean                  debugging;
    private JCGLSoftRestrictionsType restrictions;
    private OptionType<PrintStream>  tracing;

    public Builder()
    {
      this.restrictions = JCGLImplementationJOGL.DEFAULT_RESTRICTIONS;
      this.debugging = false;
      this.caching = false;
      this.tracing = Option.none();
    }

    @Override public JCGLImplementationType build(
      final GLContext c,
      final LogUsableType log)
    {
      NullCheck.notNull(c, "Context");
      NullCheck.notNull(log, "Log");

      log.debug("Context is " + c.getGLVersion());

      if (c.isGLES3()) {
        log.debug("Context is GLES3 - creating GLES3 interface");
        final JCGLInterfaceGLES3Type in_gl_es3 =
          new JCGLInterfaceGLES3_JOGL_ES3(
            c,
            log,
            this.debugging,
            this.tracing,
            this.caching,
            this.restrictions);
        final JCGLInterfaceGLES2Type in_gl_es2 = null;
        final JCGLInterfaceGL2Type in_gl_2 = null;
        final JCGLInterfaceGL3Type in_gl_3 = null;

        return new JCGLImplementationJOGL(
          in_gl_2,
          in_gl_3,
          in_gl_es2,
          in_gl_es3);
      }

      if (c.isGLES2()) {
        log.debug("Context is GLES2 - creating GLES2 interface");
        final JCGLInterfaceGLES2Type in_gl_es2 =
          new JCGLInterfaceGLES2_JOGL_ES2(
            c,
            log,
            this.debugging,
            this.tracing,
            this.caching,
            this.restrictions);
        final JCGLInterfaceGLES3Type in_gl_es3 = null;
        final JCGLInterfaceGL2Type in_gl_2 = null;
        final JCGLInterfaceGL3Type in_gl_3 = null;

        return new JCGLImplementationJOGL(
          in_gl_2,
          in_gl_3,
          in_gl_es2,
          in_gl_es3);
      }

      /**
       * Context is OpenGL 3.n, where n >= 1?
       */

      if (c.isGL3()) {
        log.debug("Context is GL3, creating OpenGL >= 3.1 interface");
        final JCGLInterfaceGL3Type in_gl_3 =
          new JCGLInterfaceGL3_JOGL_GL2GL3(
            c,
            log,
            this.debugging,
            this.tracing,
            this.caching,
            this.restrictions);
        final JCGLInterfaceGL2Type in_gl_2 = null;
        final JCGLInterfaceGLES2Type in_gl_es2 = null;
        final JCGLInterfaceGLES3Type in_gl_es3 = null;

        return new JCGLImplementationJOGL(
          in_gl_2,
          in_gl_3,
          in_gl_es2,
          in_gl_es3);
      }

      /**
       * Context is either 2.1 or 3.0.
       */

      if (c.isGL2()) {
        JCGLInterfaceGL3_JOGL_GL2GL3 in_gl_3;
        JCGLInterfaceGLES2Type in_gl_es2;
        JCGLInterfaceGL2Type in_gl_2;
        JCGLInterfaceGLES3Type in_gl_es3;

        if (c.getGLVersionNumber().getMajor() == 3) {
          log
            .debug("Context is GL2 but version is 3.0, creating OpenGL >= 3.1 interface");
          in_gl_3 =
            new JCGLInterfaceGL3_JOGL_GL2GL3(
              c,
              log,
              this.debugging,
              this.tracing,
              this.caching,
              this.restrictions);
          in_gl_2 = null;
          in_gl_es2 = null;
          in_gl_es3 = null;

          return new JCGLImplementationJOGL(
            in_gl_2,
            in_gl_3,
            in_gl_es2,
            in_gl_es3);
        }

        if (c.hasFullFBOSupport() == false) {
          JCGLImplementationJOGL.announceLackingFBO(c);
          throw new UnreachableCodeException();
        }

        log.debug("Context is GL2, creating OpenGL 2.1 interface");
        in_gl_2 =
          new JCGLInterfaceGL2_JOGL_GL21(
            c,
            log,
            this.debugging,
            this.tracing,
            this.caching,
            this.restrictions);
        in_gl_3 = null;
        in_gl_es2 = null;
        in_gl_es3 = null;

        return new JCGLImplementationJOGL(
          in_gl_2,
          in_gl_3,
          in_gl_es2,
          in_gl_es3);
      }

      throw new JCGLExceptionUnsupported(
        "At least OpenGL 2.1 or OpenGL ES2 is required");
    }

    @Override public void setDebugging(
      final boolean enabled)
    {
      this.debugging = enabled;
    }

    @Override public void setRestrictions(
      final OptionType<JCGLSoftRestrictionsType> r)
    {
      NullCheck.notNull(r, "Restrictions");
      if (r.isNone()) {
        this.restrictions = JCGLImplementationJOGL.DEFAULT_RESTRICTIONS;
      } else {
        final Some<JCGLSoftRestrictionsType> s =
          (Some<JCGLSoftRestrictionsType>) r;
        this.restrictions = s.get();
      }
    }

    @Override public void setStateCaching(
      final boolean enabled)
    {
      this.caching = enabled;
    }

    @Override public void setTracing(
      final OptionType<PrintStream> stream)
    {
      this.tracing = NullCheck.notNull(stream, "Tracing");
    }
  }

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
   * @return A new JCGL implementation builder
   */

  public static JCGLImplementationJOGLBuilderType newBuilder()
  {
    return new Builder();
  }

  private final @Nullable JCGLInterfaceGL2Type   gl_2;
  private final @Nullable JCGLInterfaceGL3Type   gl_3;
  private final @Nullable JCGLInterfaceGLES2Type gl_es2;
  private final @Nullable JCGLInterfaceGLES3Type gl_es3;

  private JCGLImplementationJOGL(
    final @Nullable JCGLInterfaceGL2Type in_gl_2,
    final @Nullable JCGLInterfaceGL3Type in_gl_3,
    final @Nullable JCGLInterfaceGLES2Type in_gl_es2,
    final @Nullable JCGLInterfaceGLES3Type in_gl_es3)
  {
    this.gl_2 = in_gl_2;
    this.gl_3 = in_gl_3;
    this.gl_es2 = in_gl_es2;
    this.gl_es3 = in_gl_es3;

    assert (this.gl_2 != null)
      || (this.gl_3 != null)
      || (this.gl_es2 != null)
      || (this.gl_es3 != null);
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
