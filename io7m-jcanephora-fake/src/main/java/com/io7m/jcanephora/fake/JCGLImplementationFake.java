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
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * A fake implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationFake implements JCGLImplementationType
{

  @SuppressWarnings("synthetic-access") private static final class Builder implements
    JCGLImplementationFakeBuilderType
  {
    private boolean                  caching;
    private boolean                  debugging;
    private JCGLSoftRestrictionsType restrictions;
    private OptionType<PrintStream>  tracing;

    public Builder()
    {
      this.restrictions = JCGLImplementationFake.DEFAULT_RESTRICTIONS;
      this.debugging = false;
      this.caching = false;
      this.tracing = Option.none();
    }

    @Override public JCGLImplementationType build(
      final FakeContext in_context,
      final FakeShaderControlType in_shader_control,
      final LogUsableType in_log)
    {
      final LogType log = NullCheck.notNull(in_log, "Log").with("fake");
      NullCheck.notNull(in_context, "GL context");
      NullCheck.notNull(in_shader_control, "Shader control");

      final JCGLVersion version = in_context.getVersion();
      log.debug("Context is " + version);

      JCGLInterfaceGLES3_Fake in_gl_es3;
      JCGLInterfaceGLES2_Fake in_gl_es2;
      JCGLInterfaceGL3_Fake in_gl_3;
      JCGLInterfaceGL2_Fake in_gl_2;
      switch (version.getAPI()) {
        case JCGL_ES:
        {
          switch (version.getVersionMajor()) {
            case 3:
            {
              log.debug("Creating GLES3 interface");
              in_gl_es3 =
                new JCGLInterfaceGLES3_Fake(
                  in_context,
                  in_shader_control,
                  log,
                  this.restrictions);
              in_gl_es2 = null;
              in_gl_2 = null;
              in_gl_3 = null;
              return new JCGLImplementationFake(
                in_gl_2,
                in_gl_3,
                in_gl_es2,
                in_gl_es3);
            }
            case 2:
            {
              log.debug("Creating GLES2 interface");
              in_gl_es2 =
                new JCGLInterfaceGLES2_Fake(
                  in_context,
                  in_shader_control,
                  log,
                  this.restrictions);
              in_gl_es3 = null;
              in_gl_2 = null;
              in_gl_3 = null;
              return new JCGLImplementationFake(
                in_gl_2,
                in_gl_3,
                in_gl_es2,
                in_gl_es3);
            }
          }

          throw new UnreachableCodeException();
        }
        case JCGL_FULL:
        {
          if (version.getVersionMajor() >= 3) {
            log.debug("Creating OpenGL >= 3.0 interface");
            in_gl_3 =
              new JCGLInterfaceGL3_Fake(
                in_context,
                in_shader_control,
                log,
                this.restrictions);
            in_gl_2 = null;
            in_gl_es2 = null;
            in_gl_es3 = null;
            return new JCGLImplementationFake(
              in_gl_2,
              in_gl_3,
              in_gl_es2,
              in_gl_es3);
          }

          if (version.getVersionMajor() >= 2) {
            log.debug("Creating OpenGL 2.1 interface");
            in_gl_2 =
              new JCGLInterfaceGL2_Fake(
                in_context,
                in_shader_control,
                log,
                this.restrictions);
            in_gl_3 = null;
            in_gl_es2 = null;
            in_gl_es3 = null;
            return new JCGLImplementationFake(
              in_gl_2,
              in_gl_3,
              in_gl_es2,
              in_gl_es3);
          }

          break;
        }
      }

      throw new JCGLExceptionUnsupported(String.format(
        "At least OpenGL 2.1 or OpenGL ES2 is required (got %s)",
        version));
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
        this.restrictions = JCGLImplementationFake.DEFAULT_RESTRICTIONS;
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

  private static final JCGLSoftRestrictionsType  DEFAULT_RESTRICTIONS;

  static {
    DEFAULT_RESTRICTIONS = new DefaultRestrictions();
  }

  /**
   * @return A new fake implementation builder
   */

  public static JCGLImplementationFakeBuilderType newBuilder()
  {
    return new Builder();
  }
  private final @Nullable JCGLInterfaceGL2Type   gl_2;
  private final @Nullable JCGLInterfaceGL3Type   gl_3;
  private final @Nullable JCGLInterfaceGLES2Type gl_es2;

  private final @Nullable JCGLInterfaceGLES3Type gl_es3;

  private JCGLImplementationFake(
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
