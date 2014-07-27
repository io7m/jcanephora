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

import java.util.Map;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.fake.FakeProgram;
import com.io7m.jcanephora.fake.FakeProgramAttribute;
import com.io7m.jcanephora.fake.FakeProgramUniform;
import com.io7m.jcanephora.fake.FakeShaderControlType;

public final class FakeShaderControl implements FakeShaderControlType
{
  @Override public void onFragmentShaderCompile(
    final String name,
    final FragmentShaderType v)
    throws JCGLException
  {
    System.out.println(FakeShaderControl.class.getCanonicalName()
      + ": onFragmentShaderCompile: "
      + name);

    if ("invalid".equals(name)) {
      throw new JCGLExceptionProgramCompileError(name, "Failed!");
    }
  }

  @Override public void onProgramCreate(
    final String name,
    final ProgramUsableType program,
    final Map<String, ProgramUniformType> uniforms,
    final Map<String, ProgramAttributeType> attributes)
    throws JCGLException
  {
    System.out.println(FakeShaderControl.class.getCanonicalName()
      + ": onProgramCreate: "
      + name);

    final FakeProgram p = (FakeProgram) program;

    if ("everything".equals(name)) {
      final FakeProgramUniform u_vf2 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vf2",
          JCGLType.TYPE_FLOAT_VECTOR_2);
      uniforms.put("u_vf2", u_vf2);

      final FakeProgramUniform u_vf3 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vf3",
          JCGLType.TYPE_FLOAT_VECTOR_3);
      uniforms.put("u_vf3", u_vf3);

      final FakeProgramUniform u_vf4 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vf4",
          JCGLType.TYPE_FLOAT_VECTOR_4);
      uniforms.put("u_vf4", u_vf4);

      final FakeProgramUniform u_float =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_float",
          JCGLType.TYPE_FLOAT);
      uniforms.put("u_float", u_float);

      final FakeProgramUniform u_vi2 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vi2",
          JCGLType.TYPE_INTEGER_VECTOR_2);
      uniforms.put("u_vi2", u_vi2);

      final FakeProgramUniform u_vi3 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vi3",
          JCGLType.TYPE_INTEGER_VECTOR_3);
      uniforms.put("u_vi3", u_vi3);

      final FakeProgramUniform u_vi4 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_vi4",
          JCGLType.TYPE_INTEGER_VECTOR_4);
      uniforms.put("u_vi4", u_vi4);

      final FakeProgramUniform u_int =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_int",
          JCGLType.TYPE_INTEGER);
      uniforms.put("u_int", u_int);

      final FakeProgramUniform u_sampler2d =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_sampler2d",
          JCGLType.TYPE_SAMPLER_2D);
      uniforms.put("u_sampler2d", u_sampler2d);

      final FakeProgramUniform u_sampler_cube =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_sampler_cube",
          JCGLType.TYPE_SAMPLER_CUBE);
      uniforms.put("u_sampler_cube", u_sampler_cube);

      final FakeProgramUniform u_m3 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_m3",
          JCGLType.TYPE_FLOAT_MATRIX_3);
      uniforms.put("u_m3", u_m3);

      final FakeProgramUniform u_m4 =
        FakeProgramUniform.newUniform(
          p.getContext(),
          p,
          0,
          0,
          "u_m4",
          JCGLType.TYPE_FLOAT_MATRIX_4);
      uniforms.put("u_m4", u_m4);

      final FakeProgramAttribute a_vf2 =
        FakeProgramAttribute.newAttribute(
          p.getContext(),
          p,
          0,
          0,
          "a_vf2",
          JCGLType.TYPE_FLOAT_VECTOR_2);
      attributes.put("a_vf2", a_vf2);

      final FakeProgramAttribute a_vf3 =
        FakeProgramAttribute.newAttribute(
          p.getContext(),
          p,
          0,
          0,
          "a_vf3",
          JCGLType.TYPE_FLOAT_VECTOR_3);
      attributes.put("a_vf3", a_vf3);

      final FakeProgramAttribute a_vf4 =
        FakeProgramAttribute.newAttribute(
          p.getContext(),
          p,
          0,
          0,
          "a_vf4",
          JCGLType.TYPE_FLOAT_VECTOR_4);
      attributes.put("a_vf4", a_vf4);

      final FakeProgramAttribute a_f =
        FakeProgramAttribute.newAttribute(
          p.getContext(),
          p,
          0,
          0,
          "a_f",
          JCGLType.TYPE_FLOAT);
      attributes.put("a_f", a_f);
    }

    if ("simple".equals(name)) {
      final FakeProgramAttribute a_position =
        FakeProgramAttribute.newAttribute(
          p.getContext(),
          p,
          0,
          0,
          "v_position",
          JCGLType.TYPE_FLOAT_VECTOR_3);
      attributes.put("v_position", a_position);
    }
  }

  @Override public void onVertexShaderCompile(
    final String name,
    final VertexShaderType v)
    throws JCGLException
  {
    System.out.println(FakeShaderControl.class.getCanonicalName()
      + ": onVertexShaderCompile: "
      + name);

    if ("invalid".equals(name)) {
      throw new JCGLExceptionProgramCompileError(name, "Failed!");
    }
  }
}
