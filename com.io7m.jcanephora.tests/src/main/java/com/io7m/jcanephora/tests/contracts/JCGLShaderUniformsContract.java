/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jtensors.VectorI4F;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Optional;

/**
 * Extended shaders contract to evaluate uniform values.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLShaderUniformsContract extends JCGLContract
{
  protected abstract JCGLInterfaceGL33Type getGL33();

  protected abstract List<String> getShaderLines(String name);

  @Test
  public final void testSumFloatArray()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_float", this.getShaderLines("uniform_eval_float.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_float", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put(index, 1.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(8.0f, r.getXF(), 0.0f);
  }

  @Test
  public final void testSumVector2fArray()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_vec2", this.getShaderLines("uniform_eval_vec2.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_vec2", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * (2 * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * 2) + 0, 1.0f);
      fb.put((index * 2) + 1, 2.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(8.0f, r.getXF(), 0.0f);
    Assert.assertEquals(16.0f, r.getYF(), 0.0f);
  }

  @Test
  public final void testSumVector3fArray()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_vec3", this.getShaderLines("uniform_eval_vec3.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_vec3", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * (3 * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * 3) + 0, 1.0f);
      fb.put((index * 3) + 1, 2.0f);
      fb.put((index * 3) + 2, 3.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(8.0f, r.getXF(), 0.0f);
    Assert.assertEquals(16.0f, r.getYF(), 0.0f);
    Assert.assertEquals(24.0f, r.getZF(), 0.0f);
  }

  @Test
  public final void testSumVector4fArray()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_vec4", this.getShaderLines("uniform_eval_vec4.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_vec4", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * (4 * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * 4) + 0, 1.0f);
      fb.put((index * 4) + 1, 2.0f);
      fb.put((index * 4) + 2, 3.0f);
      fb.put((index * 4) + 3, 4.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(8.0f, r.getXF(), 0.0f);
    Assert.assertEquals(16.0f, r.getYF(), 0.0f);
    Assert.assertEquals(24.0f, r.getZF(), 0.0f);
    Assert.assertEquals(32.0f, r.getWF(), 0.0f);
  }

  @Test
  public final void testSumFloatMatrix2x2()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_mat2x2", this.getShaderLines("uniform_eval_mat2x2.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_mat2x2", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * ((2 * 2) * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * (2 * 2)) + 0, 1.0f);
      fb.put((index * (2 * 2)) + 1, 2.0f);
      fb.put((index * (2 * 2)) + 2, 3.0f);
      fb.put((index * (2 * 2)) + 3, 4.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(80.0f, r.getXF(), 0.0f);
  }

  @Test
  public final void testSumFloatMatrix3x3()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_mat3x3", this.getShaderLines("uniform_eval_mat3x3.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_mat3x3", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * ((3 * 3) * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * (3 * 3)) + 0, 1.0f);
      fb.put((index * (3 * 3)) + 1, 2.0f);
      fb.put((index * (3 * 3)) + 2, 3.0f);
      fb.put((index * (3 * 3)) + 3, 4.0f);
      fb.put((index * (3 * 3)) + 4, 5.0f);
      fb.put((index * (3 * 3)) + 5, 6.0f);
      fb.put((index * (3 * 3)) + 6, 7.0f);
      fb.put((index * (3 * 3)) + 7, 8.0f);
      fb.put((index * (3 * 3)) + 8, 9.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(360.0f, r.getXF(), 0.0f);
  }

  @Test
  public final void testSumFloatMatrix4x4()
    throws Exception
  {
    final JCGLInterfaceGL33Type g3 = this.getGL33();
    final JCGLShadersType s = g3.getShaders();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex(
        "uniform_eval", this.getShaderLines("uniform_eval.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "uniform_eval_mat4x4", this.getShaderLines("uniform_eval_mat4x4.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram(
        "uniform_eval_mat4x4", v, Optional.empty(), f);

    final JCGLShaderTestFunctionEvaluator eval =
      new JCGLShaderTestFunctionEvaluator(g3, p);

    final FloatBuffer fb =
      ByteBuffer.allocateDirect(8 * ((4 * 4) * 4))
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    for (int index = 0; index < 8; ++index) {
      fb.put((index * (4 * 4)) + 0, 1.0f);
      fb.put((index * (4 * 4)) + 1, 2.0f);
      fb.put((index * (4 * 4)) + 2, 3.0f);
      fb.put((index * (4 * 4)) + 3, 4.0f);
      fb.put((index * (4 * 4)) + 4, 5.0f);
      fb.put((index * (4 * 4)) + 5, 6.0f);
      fb.put((index * (4 * 4)) + 6, 7.0f);
      fb.put((index * (4 * 4)) + 7, 8.0f);
      fb.put((index * (4 * 4)) + 8, 9.0f);
      fb.put((index * (4 * 4)) + 9, 10.0f);
      fb.put((index * (4 * 4)) + 10, 11.0f);
      fb.put((index * (4 * 4)) + 11, 12.0f);
      fb.put((index * (4 * 4)) + 12, 13.0f);
      fb.put((index * (4 * 4)) + 13, 14.0f);
      fb.put((index * (4 * 4)) + 14, 15.0f);
      fb.put((index * (4 * 4)) + 15, 16.0f);
    }

    final VectorI4F r = eval.evaluateArrayF(fb);

    System.out.println(r);
    Assert.assertEquals(1088.0f, r.getXF(), 0.0f);
  }
}
