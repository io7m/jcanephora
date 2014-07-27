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

package com.io7m.jcanephora.fake;

import java.io.PrintStream;

import com.io7m.jcanephora.JCGLDebugging;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionUnsupported;
import com.io7m.jcanephora.JCGLVersion;
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
 * A fake implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationFake implements JCGLImplementationType
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

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>.
   *
   * @param context
   *          The context.
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementation(
    final FakeContext context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType log)
    throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
      log,
      JCGLDebugging.JCGL_NONE,
      null,
      JCGLImplementationFake.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled.
   *
   * @param context
   *          The context.
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithDebugging(
    final FakeContext context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType log)
    throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
      log,
      JCGLDebugging.JCGL_DEBUGGING,
      null,
      JCGLImplementationFake.DEFAULT_RESTRICTIONS);
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
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationType
    newImplementationWithDebuggingAndRestrictions(
      final FakeContext context,
      final FakeShaderControlType in_shader_control,
      final LogUsableType log,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
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
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static
    JCGLImplementationType
    newImplementationWithDebuggingAndTracing(
      final FakeContext context,
      final FakeShaderControlType in_shader_control,
      final LogUsableType log,
      final PrintStream trace)
      throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
      log,
      JCGLDebugging.JCGL_TRACING_AND_DEBUGGING,
      trace,
      JCGLImplementationFake.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with debugging enabled, and tracing enabled on
   * <code>stream</code>, and restrictions <code>r</code>.
   *
   * @param context
   *          The context.
   * @param in_shader_control
   *          A shader control interface.
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
    JCGLImplementationType
    newImplementationWithDebuggingAndTracingAndRestrictions(
      final FakeContext context,
      final FakeShaderControlType in_shader_control,
      final LogUsableType log,
      final PrintStream trace,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
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
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @param r
   *          A set of restrictions.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithRestrictions(
    final FakeContext context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType log,
    final JCGLSoftRestrictionsType r)
    throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
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
   * @param in_shader_control
   *          A shader control interface.
   * @param log
   *          A log interface.
   * @param trace
   *          The output stream for debugging.
   * @return An initialized implementation.
   * @throws JCGLException
   *           If an error occurs.
   */

  public static JCGLImplementationType newImplementationWithTracing(
    final FakeContext context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType log,
    final PrintStream trace)
    throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
      log,
      JCGLDebugging.JCGL_TRACING,
      trace,
      JCGLImplementationFake.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation using the initialized <code>context</code>
   * and <code>log</code>, with tracing enabled to stream <code>trace</code>
   * and restrictions <code>r</code>.
   *
   * @param context
   *          The context.
   * @param in_shader_control
   *          A shader control interface.
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
    JCGLImplementationType
    newImplementationWithTracingAndRestrictions(
      final FakeContext context,
      final FakeShaderControlType in_shader_control,
      final LogUsableType log,
      final PrintStream trace,
      final JCGLSoftRestrictionsType r)
      throws JCGLException
  {
    return new JCGLImplementationFake(
      context,
      in_shader_control,
      log,
      JCGLDebugging.JCGL_TRACING,
      trace,
      r);
  }

  private final FakeContext                      context;
  private final @Nullable JCGLInterfaceGL2Type   gl_2;
  private final @Nullable JCGLInterfaceGL3Type   gl_3;
  private final @Nullable JCGLInterfaceGLES2Type gl_es2;
  private final @Nullable JCGLInterfaceGLES3Type gl_es3;
  private final LogUsableType                    log;
  private final FakeShaderControlType            shader_control;

  private JCGLImplementationFake(
    final FakeContext in_context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType in_log,
    final JCGLDebugging in_debug,
    final @Nullable PrintStream in_trace,
    final JCGLSoftRestrictionsType in_restrictions)
    throws JCGLException
  {
    this.log = NullCheck.notNull(in_log, "Log").with("fake");
    this.context = NullCheck.notNull(in_context, "GL context");
    this.shader_control =
      NullCheck.notNull(in_shader_control, "Shader control");

    final JCGLVersion version = this.context.getVersion();
    in_log.debug("Context is " + version);

    switch (version.getAPI()) {
      case JCGL_ES:
      {
        switch (version.getVersionMajor()) {
          case 3:
          {
            in_log.debug("Creating GLES3 interface");
            this.gl_es3 =
              new JCGLInterfaceGLES3_Fake(
                in_context,
                in_shader_control,
                in_log,
                in_debug,
                in_trace,
                in_restrictions);
            this.gl_es2 = null;
            this.gl_2 = null;
            this.gl_3 = null;
            return;
          }
          case 2:
          {
            in_log.debug("Creating GLES2 interface");
            this.gl_es2 =
              new JCGLInterfaceGLES2_Fake(
                in_context,
                in_shader_control,
                in_log,
                in_debug,
                in_trace,
                in_restrictions);
            this.gl_es3 = null;
            this.gl_2 = null;
            this.gl_3 = null;
            return;
          }
        }

        throw new UnreachableCodeException();
      }
      case JCGL_FULL:
      {
        if (version.getVersionMajor() >= 3) {
          in_log.debug("Creating OpenGL >= 3.0 interface");
          this.gl_3 =
            new JCGLInterfaceGL3_Fake(
              in_context,
              in_shader_control,
              in_log,
              in_debug,
              in_trace,
              in_restrictions);
          this.gl_2 = null;
          this.gl_es2 = null;
          this.gl_es3 = null;
          return;
        }

        if (version.getVersionMajor() >= 2) {
          in_log.debug("Creating OpenGL 2.1 interface");
          this.gl_2 =
            new JCGLInterfaceGL2_Fake(
              in_context,
              in_shader_control,
              in_log,
              in_debug,
              in_trace,
              in_restrictions);
          this.gl_3 = null;
          this.gl_es2 = null;
          this.gl_es3 = null;
          return;
        }

        break;
      }
    }

    throw new JCGLExceptionUnsupported(String.format(
      "At least OpenGL 2.1 or OpenGL ES2 is required (got %s)",
      version));
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
