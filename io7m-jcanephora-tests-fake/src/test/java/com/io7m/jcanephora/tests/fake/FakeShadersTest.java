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

package com.io7m.jcanephora.tests.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.fake.FakeContext;
import com.io7m.jcanephora.fake.FakeProgramAttribute;
import com.io7m.jcanephora.fake.FakeProgramUniform;
import com.io7m.jcanephora.fake.JCGLImplementationFake;
import com.io7m.jcanephora.fake.JCGLImplementationFakeType;
import com.io7m.jcanephora.tests.contracts.JCGLShadersContract;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.jcanephora.tests.contracts.JCGLUnsharedContextPair;
import com.io7m.junreachable.UnreachableCodeException;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class FakeShadersTest extends JCGLShadersContract
{
  private static void onLinkMatrices0(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "m2", new FakeProgramAttribute(
        context, p, location++, "m2", JCGLType.TYPE_FLOAT_MATRIX_2));
    attributes.put(
      "m3", new FakeProgramAttribute(
        context, p, location++, "m3", JCGLType.TYPE_FLOAT_MATRIX_3));
    attributes.put(
      "m4", new FakeProgramAttribute(
        context, p, location++, "m4", JCGLType.TYPE_FLOAT_MATRIX_4));
  }

  private static void onLinkMatrices1(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "m4", new FakeProgramAttribute(
        context, p, location++, "m4", JCGLType.TYPE_FLOAT_MATRIX_4));
    attributes.put(
      "m3", new FakeProgramAttribute(
        context, p, location++, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3));
    attributes.put(
      "m2", new FakeProgramAttribute(
        context, p, location++, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2));
  }

  private static void onLinkMatrices2(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "m4", new FakeProgramAttribute(
        context, p, location++, "m4", JCGLType.TYPE_FLOAT_MATRIX_4));
    attributes.put(
      "m3", new FakeProgramAttribute(
        context, p, location++, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3));
    attributes.put(
      "m2", new FakeProgramAttribute(
        context, p, location++, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2));
  }

  private static void onLinkMatrices3(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "m4", new FakeProgramAttribute(
        context, p, location++, "m4", JCGLType.TYPE_FLOAT_MATRIX_3x4));
    attributes.put(
      "m3", new FakeProgramAttribute(
        context, p, location++, "m3", JCGLType.TYPE_FLOAT_MATRIX_3));
    attributes.put(
      "m2", new FakeProgramAttribute(
        context, p, location++, "m2", JCGLType.TYPE_FLOAT_MATRIX_3x2));
  }

  private static void onLinkMatrices4(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "m4", new FakeProgramAttribute(
        context, p, location++, "m4", JCGLType.TYPE_FLOAT_MATRIX_2x4));
    attributes.put(
      "m3", new FakeProgramAttribute(
        context, p, location++, "m3", JCGLType.TYPE_FLOAT_MATRIX_2x3));
    attributes.put(
      "m2", new FakeProgramAttribute(
        context, p, location++, "m2", JCGLType.TYPE_FLOAT_MATRIX_2));
  }

  private static void onLinkValid0(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "f", new FakeProgramAttribute(
        context, p, location++, "f", JCGLType.TYPE_FLOAT));
  }

  private static void onLinkAttributes0(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    attributes.put(
      "f", new FakeProgramAttribute(
        context, p, location++, "f", JCGLType.TYPE_FLOAT));
    attributes.put(
      "fv2", new FakeProgramAttribute(
        context, p, location++, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2));
    attributes.put(
      "fv3", new FakeProgramAttribute(
        context, p, location++, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3));
    attributes.put(
      "fv4", new FakeProgramAttribute(
        context, p, location++, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4));

    attributes.put(
      "i", new FakeProgramAttribute(
        context, p, location++, "i", JCGLType.TYPE_INTEGER));
    attributes.put(
      "iv2", new FakeProgramAttribute(
        context, p, location++, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2));
    attributes.put(
      "iv3", new FakeProgramAttribute(
        context, p, location++, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3));
    attributes.put(
      "iv4", new FakeProgramAttribute(
        context, p, location++, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4));

    attributes.put(
      "u", new FakeProgramAttribute(
        context, p, location++, "u", JCGLType.TYPE_UNSIGNED_INTEGER));
    attributes.put(
      "uv2", new FakeProgramAttribute(
        context,
        p,
        location++,
        "uv2",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2));
    attributes.put(
      "uv3", new FakeProgramAttribute(
        context,
        p,
        location++,
        "uv3",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3));
    attributes.put(
      "uv4", new FakeProgramAttribute(
        context,
        p,
        location++,
        "uv4",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4));
  }

  private static void onLinkUniforms0(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    uniforms.put(
      "f", new FakeProgramUniform(
        context, p, location++, "f", JCGLType.TYPE_FLOAT));
    uniforms.put(
      "fv2", new FakeProgramUniform(
        context, p, location++, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2));
    uniforms.put(
      "fv3", new FakeProgramUniform(
        context, p, location++, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3));
    uniforms.put(
      "fv4", new FakeProgramUniform(
        context, p, location++, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4));

    uniforms.put(
      "i", new FakeProgramUniform(
        context, p, location++, "i", JCGLType.TYPE_INTEGER));
    uniforms.put(
      "iv2", new FakeProgramUniform(
        context, p, location++, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2));
    uniforms.put(
      "iv3", new FakeProgramUniform(
        context, p, location++, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3));
    uniforms.put(
      "iv4", new FakeProgramUniform(
        context, p, location++, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4));

    uniforms.put(
      "u", new FakeProgramUniform(
        context, p, location++, "u", JCGLType.TYPE_UNSIGNED_INTEGER));
    uniforms.put(
      "uv2", new FakeProgramUniform(
        context,
        p,
        location++,
        "uv2",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2));
    uniforms.put(
      "uv3", new FakeProgramUniform(
        context,
        p,
        location++,
        "uv3",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3));
    uniforms.put(
      "uv4", new FakeProgramUniform(
        context,
        p,
        location++,
        "uv4",
        JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4));

    uniforms.put(
      "b", new FakeProgramUniform(
        context, p, location++, "b", JCGLType.TYPE_BOOLEAN));
    uniforms.put(
      "bv2", new FakeProgramUniform(
        context, p, location++, "bv2", JCGLType.TYPE_BOOLEAN_VECTOR_2));
    uniforms.put(
      "bv3", new FakeProgramUniform(
        context, p, location++, "bv3", JCGLType.TYPE_BOOLEAN_VECTOR_3));
    uniforms.put(
      "bv4", new FakeProgramUniform(
        context, p, location++, "bv4", JCGLType.TYPE_BOOLEAN_VECTOR_4));
  }

  private static void onLinkUniforms1(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    uniforms.put(
      "fm4", new FakeProgramUniform(
        context, p, location++, "fm4", JCGLType.TYPE_FLOAT_MATRIX_4));
    uniforms.put(
      "fm4x4", new FakeProgramUniform(
        context, p, location++, "fm4x4", JCGLType.TYPE_FLOAT_MATRIX_4));
    uniforms.put(
      "fm4x3", new FakeProgramUniform(
        context, p, location++, "fm4x3", JCGLType.TYPE_FLOAT_MATRIX_4x3));
    uniforms.put(
      "fm4x2", new FakeProgramUniform(
        context, p, location++, "fm4x2", JCGLType.TYPE_FLOAT_MATRIX_4x2));

    uniforms.put(
      "fm3", new FakeProgramUniform(
        context, p, location++, "fm3", JCGLType.TYPE_FLOAT_MATRIX_3));
    uniforms.put(
      "fm3x4", new FakeProgramUniform(
        context, p, location++, "fm3x4", JCGLType.TYPE_FLOAT_MATRIX_3x4));
    uniforms.put(
      "fm3x3", new FakeProgramUniform(
        context, p, location++, "fm3x3", JCGLType.TYPE_FLOAT_MATRIX_3));
    uniforms.put(
      "fm3x2", new FakeProgramUniform(
        context, p, location++, "fm3x2", JCGLType.TYPE_FLOAT_MATRIX_3x2));

    uniforms.put(
      "fm2", new FakeProgramUniform(
        context, p, location++, "fm2", JCGLType.TYPE_FLOAT_MATRIX_2));
    uniforms.put(
      "fm2x4", new FakeProgramUniform(
        context, p, location++, "fm2x4", JCGLType.TYPE_FLOAT_MATRIX_2x4));
    uniforms.put(
      "fm2x3", new FakeProgramUniform(
        context, p, location++, "fm2x3", JCGLType.TYPE_FLOAT_MATRIX_2x3));
    uniforms.put(
      "fm2x2", new FakeProgramUniform(
        context, p, location++, "fm2x2", JCGLType.TYPE_FLOAT_MATRIX_2));
  }

  private static void onLinkUniforms2(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    int location = 0;

    uniforms.put(
      "s2", new FakeProgramUniform(
        context, p, location++, "s2", JCGLType.TYPE_SAMPLER_2D));
    uniforms.put(
      "s3", new FakeProgramUniform(
        context, p, location++, "s3", JCGLType.TYPE_SAMPLER_3D));
    uniforms.put(
      "sc", new FakeProgramUniform(
        context, p, location++, "sc", JCGLType.TYPE_SAMPLER_CUBE));
  }

  @Override protected JCGLShadersType getShaders(final String name)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c =
        i.newContext(name, new FakeShadersTestListener());
      final JCGLInterfaceGL33Type g33 = c.contextGetGL33();
      return g33.getShaders();
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override
  protected JCGLUnsharedContextPair<JCGLShadersType> getShadersUnshared(
    final String main,
    final String alt)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c0 = i.newContext(
        main, new FakeDefaultShaderListener());
      final JCGLContextType c1 =
        i.newContext(alt, new FakeShadersTestListener());
      final JCGLInterfaceGL33Type g33_0 = c0.contextGetGL33();
      final JCGLInterfaceGL33Type g33_1 = c1.contextGetGL33();
      return new JCGLUnsharedContextPair<>(
        g33_0.getShaders(), c0, g33_1.getShaders(), c1);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override
  protected JCGLSharedContextPair<JCGLShadersType> getShadersSharedWith(
    final String name,
    final String shared)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c0 = i.newContext(
        name, new FakeDefaultShaderListener());
      final JCGLContextType c1 = i.newContextSharedWith(
        c0, name, new FakeShadersTestListener());
      final JCGLInterfaceGL33Type g0 = c0.contextGetGL33();
      final JCGLInterfaceGL33Type g1 = c1.contextGetGL33();

      return new JCGLSharedContextPair<>(
        g0.getShaders(), c0, g1.getShaders(), c1);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override protected List<String> getShaderLines(final String name)
  {
    final Class<JCGLShadersContract> c = JCGLShadersContract.class;
    final List<String> lines = new ArrayList<>(32);
    try (final InputStream is = c.getResourceAsStream(name)) {
      try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(is))) {
        while (true) {
          final String line = reader.readLine();
          if (line == null) {
            return lines;
          }
          lines.add(line + "\n");
        }
      }
    } catch (final IOException e) {
      throw new IOError(e);
    }
  }

  @Override public void onTestCompleted()
  {

  }

  private static final class FakeShadersTestListener
    extends FakeDefaultShaderListener
  {
    @Override public void onCompileFragmentShaderStart(
      final FakeContext context,
      final String name,
      final List<String> sources)
      throws JCGLException
    {
      super.onCompileFragmentShaderStart(context, name, sources);

      if ("invalid0".equals(name)) {
        throw new JCGLExceptionProgramCompileError(name, "Failure");
      }
    }

    @Override public void onCompileGeometryShaderStart(
      final FakeContext context,
      final String name,
      final List<String> sources)
      throws JCGLException
    {
      super.onCompileGeometryShaderStart(context, name, sources);

      if ("invalid0".equals(name)) {
        throw new JCGLExceptionProgramCompileError(name, "Failure");
      }
    }

    @Override public void onCompileVertexShaderStart(
      final FakeContext context,
      final String name,
      final List<String> sources)
      throws JCGLException
    {
      super.onCompileVertexShaderStart(context, name, sources);

      if ("invalid0".equals(name)) {
        throw new JCGLExceptionProgramCompileError(name, "Failure");
      }
    }

    @Override public void onLinkProgram(
      final FakeContext context,
      final JCGLProgramShaderUsableType p,
      final String name,
      final JCGLVertexShaderUsableType v,
      final Optional<JCGLGeometryShaderUsableType> g,
      final JCGLFragmentShaderUsableType f,
      final Map<String, JCGLProgramAttributeType> attributes,
      final Map<String, JCGLProgramUniformType> uniforms)
    {
      super.onLinkProgram(context, p, name, v, g, f, attributes, uniforms);

      if ("valid0".equals(name)) {
        FakeShadersTest.onLinkValid0(context, p, attributes, uniforms);
        return;
      }

      if ("attributes0".equals(name)) {
        FakeShadersTest.onLinkAttributes0(context, p, attributes, uniforms);
        return;
      }

      if ("attributes1".equals(name)) {
        FakeShadersTest.onLinkMatrices0(context, p, attributes, uniforms);
        return;
      }

      if ("attributes2".equals(name)) {
        FakeShadersTest.onLinkMatrices2(context, p, attributes, uniforms);
        return;
      }

      if ("attributes3".equals(name)) {
        FakeShadersTest.onLinkMatrices3(context, p, attributes, uniforms);
        return;
      }

      if ("attributes4".equals(name)) {
        FakeShadersTest.onLinkMatrices4(context, p, attributes, uniforms);
        return;
      }

      if ("uniforms0".equals(name)) {
        FakeShadersTest.onLinkUniforms0(context, p, attributes, uniforms);
        return;
      }

      if ("uniforms1".equals(name)) {
        FakeShadersTest.onLinkUniforms1(context, p, attributes, uniforms);
        return;
      }

      if ("uniforms2".equals(name)) {
        FakeShadersTest.onLinkUniforms2(context, p, attributes, uniforms);
        return;
      }

      if ("incompatible0".equals(name)) {
        throw new JCGLExceptionProgramCompileError(name, "Failure");
      }
    }
  }
}
