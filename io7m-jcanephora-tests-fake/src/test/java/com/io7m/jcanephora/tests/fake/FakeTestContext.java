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

package com.io7m.jcanephora.tests.fake;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLVersionNumber;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jcanephora.fake.FakeContext;
import com.io7m.jcanephora.fake.FakeDefaultFramebuffer;
import com.io7m.jcanephora.fake.FakeShaderControlType;
import com.io7m.jcanephora.fake.JCGLImplementationFake;
import com.io7m.jcanephora.fake.JCGLImplementationFakeBuilderType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jfunctional.Option;
import com.io7m.jlog.Log;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogPolicyAllOn;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

public final class FakeTestContext
{
  static final String LOG_DESTINATION_OPENGL_2_1;
  static final String LOG_DESTINATION_OPENGL_3_0;
  static final String LOG_DESTINATION_OPENGL_ES_2_0;
  static final String LOG_DESTINATION_OPENGL_ES_3_0;

  static {
    LOG_DESTINATION_OPENGL_ES_3_0 = "fake_es_3_0-test";
    LOG_DESTINATION_OPENGL_ES_2_0 = "fake_es_2_0-test";
    LOG_DESTINATION_OPENGL_3_0 = "fake_3_0-test";
    LOG_DESTINATION_OPENGL_2_1 = "fake_2_1-test";
  }

  private static FilesystemType getFS(
    final LogUsableType log)
  {
    try {
      final FilesystemType fs = Filesystem.makeWithoutArchiveDirectory(log);
      fs.mountClasspathArchive(TestContext.class, PathVirtual.ROOT);
      return fs;
    } catch (final FilesystemError e) {
      throw new java.lang.RuntimeException(e);
    }
  }

  private static LogType getLog(
    final String destination)
  {
    return Log.newLog(LogPolicyAllOn.newPolicy(LogLevel.LOG_DEBUG), "tests");
  }

  public static TestContext makeContextWithOpenGL_ES2(
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL_ES2_Actual(
      null,
      shader_control);
  }

  private static TestContext makeContextWithOpenGL_ES2_Actual(
    final @Nullable JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    try {
      final LogType log =
        FakeTestContext.getLog(FakeTestContext.LOG_DESTINATION_OPENGL_ES_2_0);
      final FilesystemType fs = FakeTestContext.getFS(log);

      final AreaInclusive area =
        new AreaInclusive(new RangeInclusiveL(0, 639), new RangeInclusiveL(
          0,
          479));
      final FakeDefaultFramebuffer in_framebuffer =
        FakeDefaultFramebuffer.newFramebuffer(area, 8, 8, 8, 24, 8);
      final JCGLVersionNumber in_number = new JCGLVersionNumber(2, 0, 0);
      final JCGLApi in_api = JCGLApi.JCGL_ES;
      final JCGLSLVersionNumber in_glsl_version =
        new JCGLSLVersionNumber(1, 0);
      final FakeContext ctx =
        FakeContext.newContext(
          in_framebuffer,
          in_number,
          in_api,
          in_glsl_version);

      final JCGLImplementationType gi =
        FakeTestContext.makeImplementation(r, shader_control, log, ctx);

      return new TestContext(fs, gi, log);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static TestContext makeContextWithOpenGL_ES2_WithRestrictions(
    final JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext
      .makeContextWithOpenGL_ES2_Actual(r, shader_control);
  }

  public static TestContext makeContextWithOpenGL_ES3(
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL_ES3_Actual(
      null,
      shader_control);
  }

  private static TestContext makeContextWithOpenGL_ES3_Actual(
    final @Nullable JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    try {
      final LogType log =
        FakeTestContext.getLog(FakeTestContext.LOG_DESTINATION_OPENGL_ES_3_0);
      final FilesystemType fs = FakeTestContext.getFS(log);

      final AreaInclusive area =
        new AreaInclusive(new RangeInclusiveL(0, 639), new RangeInclusiveL(
          0,
          479));
      final FakeDefaultFramebuffer in_framebuffer =
        FakeDefaultFramebuffer.newFramebuffer(area, 8, 8, 8, 24, 8);
      final JCGLVersionNumber in_number = new JCGLVersionNumber(3, 0, 0);
      final JCGLApi in_api = JCGLApi.JCGL_ES;
      final JCGLSLVersionNumber in_glsl_version =
        new JCGLSLVersionNumber(3, 0);
      final FakeContext ctx =
        FakeContext.newContext(
          in_framebuffer,
          in_number,
          in_api,
          in_glsl_version);

      final JCGLImplementationType gi =
        FakeTestContext.makeImplementation(r, shader_control, log, ctx);

      return new TestContext(fs, gi, log);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static TestContext makeContextWithOpenGL_ES3_WithRestrictions(
    final JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext
      .makeContextWithOpenGL_ES3_Actual(r, shader_control);
  }

  public static TestContext makeContextWithOpenGL2_1(
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL2_1_Actual(
      null,
      shader_control);
  }

  private static TestContext makeContextWithOpenGL2_1_Actual(
    final @Nullable JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    try {
      final LogType log =
        FakeTestContext.getLog(FakeTestContext.LOG_DESTINATION_OPENGL_2_1);
      final FilesystemType fs = FakeTestContext.getFS(log);

      final AreaInclusive area =
        new AreaInclusive(new RangeInclusiveL(0, 639), new RangeInclusiveL(
          0,
          479));
      final FakeDefaultFramebuffer in_framebuffer =
        FakeDefaultFramebuffer.newFramebuffer(area, 8, 8, 8, 24, 8);
      final JCGLVersionNumber in_number = new JCGLVersionNumber(2, 1, 0);
      final JCGLApi in_api = JCGLApi.JCGL_FULL;
      final JCGLSLVersionNumber in_glsl_version =
        new JCGLSLVersionNumber(1, 20);
      final FakeContext ctx =
        FakeContext.newContext(
          in_framebuffer,
          in_number,
          in_api,
          in_glsl_version);

      final JCGLImplementationType gi =
        FakeTestContext.makeImplementation(r, shader_control, log, ctx);

      return new TestContext(fs, gi, log);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static TestContext makeContextWithOpenGL2_1_WithRestrictions(
    final JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL2_1_Actual(r, shader_control);
  }

  public static TestContext makeContextWithOpenGL3_0(
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL3_0_Actual(
      null,
      shader_control);
  }

  private static TestContext makeContextWithOpenGL3_0_Actual(
    final @Nullable JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    try {
      final LogType log =
        FakeTestContext.getLog(FakeTestContext.LOG_DESTINATION_OPENGL_3_0);
      final FilesystemType fs = FakeTestContext.getFS(log);

      final AreaInclusive area =
        new AreaInclusive(new RangeInclusiveL(0, 639), new RangeInclusiveL(
          0,
          479));
      final FakeDefaultFramebuffer in_framebuffer =
        FakeDefaultFramebuffer.newFramebuffer(area, 8, 8, 8, 24, 8);
      final JCGLVersionNumber in_number = new JCGLVersionNumber(3, 0, 0);
      final JCGLApi in_api = JCGLApi.JCGL_FULL;
      final JCGLSLVersionNumber in_glsl_version =
        new JCGLSLVersionNumber(1, 50);
      final FakeContext ctx =
        FakeContext.newContext(
          in_framebuffer,
          in_number,
          in_api,
          in_glsl_version);

      final JCGLImplementationType gi =
        FakeTestContext.makeImplementation(r, shader_control, log, ctx);

      return new TestContext(fs, gi, log);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static TestContext makeContextWithOpenGL3_0_WithRestrictions(
    final JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control)
  {
    return FakeTestContext.makeContextWithOpenGL3_0_Actual(r, shader_control);
  }

  private static JCGLImplementationType makeImplementation(
    final JCGLSoftRestrictionsType r,
    final FakeShaderControlType shader_control,
    final LogUsableType log,
    final FakeContext ctx)
    throws JCGLException
  {
    final JCGLImplementationFakeBuilderType b =
      JCGLImplementationFake.newBuilder();
    b.setRestrictions(Option.of(r));
    return b.build(ctx, shader_control, log);
  }

  private FakeTestContext()
  {
    throw new UnreachableCodeException();
  }
}
