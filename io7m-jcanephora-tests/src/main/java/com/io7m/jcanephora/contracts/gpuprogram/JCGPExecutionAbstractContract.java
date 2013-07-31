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

package com.io7m.jcanephora.contracts.gpuprogram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLShaders;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.contracts.TestContract;
import com.io7m.jcanephora.gpuprogram.JCGPCompiler;
import com.io7m.jcanephora.gpuprogram.JCGPExecutionAPI;
import com.io7m.jcanephora.gpuprogram.JCGPExecutionAbstract;
import com.io7m.jcanephora.gpuprogram.JCGPFragmentShaderOutput;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratedSource;
import com.io7m.jcanephora.gpuprogram.JCGPGenerator;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratorAPI;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratorContext;
import com.io7m.jcanephora.gpuprogram.JCGPProgram;
import com.io7m.jcanephora.gpuprogram.JCGPSource;
import com.io7m.jcanephora.gpuprogram.JCGPStringSource;
import com.io7m.jcanephora.gpuprogram.JCGPUniform;
import com.io7m.jcanephora.gpuprogram.JCGPUnit;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitFragmentShader;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitVertexShader;
import com.io7m.jcanephora.gpuprogram.JCGPVersionRange;
import com.io7m.jcanephora.gpuprogram.JCGPVertexShaderInput;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;

public abstract class JCGPExecutionAbstractContract implements TestContract
{
  private static final class ExecCalled extends
    JCGPExecutionAbstract<Throwable>
  {
    public boolean called;

    public ExecCalled()
    {
      this.called = false;
    }

    @Override protected void execRunActual()
      throws JCGLException,
        Throwable
    {
      this.called = true;
    }
  }

  private static @Nonnull ArrayBuffer makeArrayBuffer(
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final ArrayBufferAttributeDescriptor[] as =
        new ArrayBufferAttributeDescriptor[3];
      as[0] =
        new ArrayBufferAttributeDescriptor("a0", JCGLScalarType.TYPE_FLOAT, 4);
      as[1] =
        new ArrayBufferAttributeDescriptor("a1", JCGLScalarType.TYPE_FLOAT, 4);
      as[2] =
        new ArrayBufferAttributeDescriptor("a2", JCGLScalarType.TYPE_FLOAT, 4);

      final ArrayBufferTypeDescriptor descriptor =
        new ArrayBufferTypeDescriptor(as);
      return gl.arrayBufferAllocate(
        3,
        descriptor,
        UsageHint.USAGE_STATIC_DRAW);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull JCGPProgram makeComplicatedProgram(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final JCGLSLVersion want_version = gl.metaGetSLVersion();

      final StringBuilder s = new StringBuilder();
      s.append("void main (void)\n");
      s.append("{\n");
      s.append("  vec4  vf0 = vec4 (ufv2, 1, 1) + vec4 (ufv3, 1) + ufv4;\n");
      s
        .append("  ivec4 vi0 = ivec4 (uiv2, 1, 1) + ivec4 (uiv3, 1) + uiv4;\n");
      s.append("  vec4  vf1 = vec4 (vi0);\n");
      s.append("  vec4  vf2 = vec4 (uf, uf, uf, uf);\n");
      s.append("  vec4  vf3 = vec4 (ufv3 * um3, 1);\n");
      s.append("  vec4  vf4 = vec4 (ufv4 * um4);\n");
      s.append("  gl_Position = a + vf0 + vf1 + vf2 + vf3 + vf4;\n");
      s.append("}\n");

      final JCGPSource vs_source = new JCGPStringSource(s.toString());

      final JCGPVersionRange<JCGLApiKindES> versions_es =
        new JCGPVersionRange<JCGLApiKindES>(
          JCGLSLVersion.GLSL_ES_100.getNumber(),
          JCGLSLVersion.GLSL_ES_30.getNumber());

      final JCGPVersionRange<JCGLApiKindFull> versions_full =
        new JCGPVersionRange<JCGLApiKindFull>(
          JCGLSLVersion.GLSL_110.getNumber(),
          JCGLSLVersion.GLSL_440.getNumber());

      final JCGPUnitVertexShader v_unit =
        JCGPUnit.makeVertexShader(
          "v",
          vs_source,
          new LinkedList<String>(),
          versions_es,
          versions_full);
      v_unit.declareInput(JCGPVertexShaderInput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "a"));

      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_FLOAT_VECTOR_2,
        "ufv2"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_FLOAT_VECTOR_3,
        "ufv3"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "ufv4"));
      v_unit.declareUniformInput(JCGPUniform.make(JCGLType.TYPE_FLOAT, "uf"));

      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_2,
        "uiv2"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_3,
        "uiv3"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_4,
        "uiv4"));

      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_FLOAT_MATRIX_3,
        "um3"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_FLOAT_MATRIX_4,
        "um4"));

      final JCGPSource source = new JCGPSource() {
        @Override public boolean sourceChangedSince(
          final @Nonnull Calendar since)
          throws Exception,
            ConstraintError
        {
          throw new UnreachableCodeException();
        }

        @Override public void sourceGet(
          final @Nonnull JCGPGeneratorContext context,
          final @Nonnull ArrayList<String> output)
          throws Exception,
            ConstraintError
        {
          final JCGLSLVersionNumber version = context.getVersion();
          switch (context.getApi()) {
            case JCGL_ES:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_ES_100.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = texture2D(ut, vec2(0, 0));\n");
                output.add("}\n");
                return;
              }
              break;
            }
            case JCGL_FULL:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_120.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = texture2D(ut, vec2(0, 0));\n");
                output.add("}\n");
                return;
              }
              break;
            }
          }

          output.add("void main (void)\n");
          output.add("{\n");
          output.add("  out_frag = texture2D(ut, vec2(0, 0));\n");
          output.add("}\n");
        }
      };

      final JCGPUnitFragmentShader f_unit =
        JCGPUnit.makeFragmentShader(
          "f",
          source,
          new LinkedList<String>(),
          versions_es,
          versions_full);
      f_unit.declareOutput(JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "out_frag",
        0));
      f_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_SAMPLER_2D,
        "ut"));

      final JCGPGeneratorAPI gen =
        JCGPGenerator.newProgramFullAndES(
          tc.getLog(),
          "p",
          versions_full,
          versions_es);

      gen.generatorDebuggingEnable(true);
      gen.generatorUnitAdd(v_unit);
      gen.generatorUnitAdd(f_unit);

      final JCGPGeneratedSource<JCGLShaderKindVertex> v_source =
        gen.generatorGenerateVertexShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : v_source.getLines()) {
        System.out.print(line);
      }

      final JCGPGeneratedSource<JCGLShaderKindFragment> f_source =
        gen.generatorGenerateFragmentShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : f_source.getLines()) {
        System.out.print(line);
      }

      final JCGPCompiler comp = JCGPCompiler.newCompiler();
      return comp.compileProgram(gl, "p", v_source, f_source);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLUnsupportedException e) {
      throw new AssertionError(e);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull JCGPProgram makeEmptyProgram(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final JCGLSLVersion want_version = gl.metaGetSLVersion();

      final StringBuilder s = new StringBuilder();
      s.append("void main (void)\n");
      s.append("{\n");
      s.append("  gl_Position = vec4(1, 2, 3, 1);\n");
      s.append("}\n");

      final JCGPSource vs_source = new JCGPStringSource(s.toString());

      final JCGPVersionRange<JCGLApiKindES> versions_es =
        new JCGPVersionRange<JCGLApiKindES>(
          JCGLSLVersion.GLSL_ES_100.getNumber(),
          JCGLSLVersion.GLSL_ES_30.getNumber());

      final JCGPVersionRange<JCGLApiKindFull> versions_full =
        new JCGPVersionRange<JCGLApiKindFull>(
          JCGLSLVersion.GLSL_110.getNumber(),
          JCGLSLVersion.GLSL_440.getNumber());

      final JCGPUnitVertexShader v_unit =
        JCGPUnit.makeVertexShader(
          "v",
          vs_source,
          new LinkedList<String>(),
          versions_es,
          versions_full);

      final JCGPSource source = new JCGPSource() {
        @Override public boolean sourceChangedSince(
          final @Nonnull Calendar since)
          throws Exception,
            ConstraintError
        {
          return false;
        }

        @Override public void sourceGet(
          final @Nonnull JCGPGeneratorContext context,
          final @Nonnull ArrayList<String> output)
          throws Exception,
            ConstraintError
        {
          final JCGLSLVersionNumber version = context.getVersion();
          switch (context.getApi()) {
            case JCGL_ES:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_ES_100.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = vec4(1, 1, 1, 1);\n");
                output.add("}\n");
                return;
              }
              break;
            }
            case JCGL_FULL:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_120.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = vec4(1, 1, 1, 1);\n");
                output.add("}\n");
                return;
              }
              break;
            }
          }

          output.add("void main (void)\n");
          output.add("{\n");
          output.add("  out_frag = vec4(1, 1, 1, 1);\n");
          output.add("}\n");
        }
      };

      final JCGPUnitFragmentShader f_unit =
        JCGPUnit.makeFragmentShader(
          "f",
          source,
          new LinkedList<String>(),
          versions_es,
          versions_full);
      f_unit.declareOutput(JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "out_frag",
        0));

      final JCGPGeneratorAPI gen =
        JCGPGenerator.newProgramFullAndES(
          tc.getLog(),
          "p",
          versions_full,
          versions_es);

      gen.generatorDebuggingEnable(true);
      gen.generatorUnitAdd(v_unit);
      gen.generatorUnitAdd(f_unit);

      final JCGPGeneratedSource<JCGLShaderKindVertex> v_source =
        gen.generatorGenerateVertexShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : v_source.getLines()) {
        System.out.print(line);
      }

      final JCGPGeneratedSource<JCGLShaderKindFragment> f_source =
        gen.generatorGenerateFragmentShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : f_source.getLines()) {
        System.out.print(line);
      }

      final JCGPCompiler comp = JCGPCompiler.newCompiler();
      return comp.compileProgram(gl, "p", v_source, f_source);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLUnsupportedException e) {
      throw new AssertionError(e);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull JCGPProgram makeProgramWithAttributesAndUniforms(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final JCGLSLVersion want_version = gl.metaGetSLVersion();

      final StringBuilder s = new StringBuilder();
      s.append("void main (void)\n");
      s.append("{\n");
      s.append("  vec4 xyz = vec4(0, 0, 0, 0);");
      s.append("  vec4 v0  = vec4(u_integer4_0);");
      s.append("  vec4 v1  = vec4(u_integer4_1);");
      s.append("  vec4 v2  = vec4(u_integer4_2);");
      s.append("  gl_Position = a0 + a1 + a2 + v0 + v1 + v2;\n");
      s.append("}\n");

      final JCGPSource vs_source = new JCGPStringSource(s.toString());

      final JCGPVersionRange<JCGLApiKindES> versions_es =
        new JCGPVersionRange<JCGLApiKindES>(
          JCGLSLVersion.GLSL_ES_100.getNumber(),
          JCGLSLVersion.GLSL_ES_30.getNumber());

      final JCGPVersionRange<JCGLApiKindFull> versions_full =
        new JCGPVersionRange<JCGLApiKindFull>(
          JCGLSLVersion.GLSL_110.getNumber(),
          JCGLSLVersion.GLSL_440.getNumber());

      final JCGPUnitVertexShader v_unit =
        JCGPUnit.makeVertexShader(
          "v",
          vs_source,
          new LinkedList<String>(),
          versions_es,
          versions_full);
      v_unit.declareInput(JCGPVertexShaderInput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "a0"));
      v_unit.declareInput(JCGPVertexShaderInput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "a1"));
      v_unit.declareInput(JCGPVertexShaderInput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "a2"));

      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_4,
        "u_integer4_0"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_4,
        "u_integer4_1"));
      v_unit.declareUniformInput(JCGPUniform.make(
        JCGLType.TYPE_INTEGER_VECTOR_4,
        "u_integer4_2"));

      final JCGPSource source = new JCGPSource() {
        @Override public boolean sourceChangedSince(
          final @Nonnull Calendar since)
          throws Exception,
            ConstraintError
        {
          throw new UnreachableCodeException();
        }

        @Override public void sourceGet(
          final @Nonnull JCGPGeneratorContext context,
          final @Nonnull ArrayList<String> output)
          throws Exception,
            ConstraintError
        {
          final JCGLSLVersionNumber version = context.getVersion();
          switch (context.getApi()) {
            case JCGL_ES:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_ES_100.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = vec4(1, 1, 1, 1);\n");
                output.add("}\n");
                return;
              }
              break;
            }
            case JCGL_FULL:
            {
              if (version.compareTo(JCGLSLVersion.GLSL_120.getNumber()) <= 0) {
                output.add("void main (void)\n");
                output.add("{\n");
                output.add("  gl_FragColor = vec4(1, 1, 1, 1);\n");
                output.add("}\n");
                return;
              }
              break;
            }
          }

          output.add("void main (void)\n");
          output.add("{\n");
          output.add("  out_frag = vec4(1, 1, 1, 1);\n");
          output.add("}\n");
        }
      };

      final JCGPUnitFragmentShader f_unit =
        JCGPUnit.makeFragmentShader(
          "f",
          source,
          new LinkedList<String>(),
          versions_es,
          versions_full);
      f_unit.declareOutput(JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "out_frag",
        0));

      final JCGPGeneratorAPI gen =
        JCGPGenerator.newProgramFullAndES(
          tc.getLog(),
          "p",
          versions_full,
          versions_es);

      gen.generatorDebuggingEnable(true);
      gen.generatorUnitAdd(v_unit);
      gen.generatorUnitAdd(f_unit);

      final JCGPGeneratedSource<JCGLShaderKindVertex> v_source =
        gen.generatorGenerateVertexShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : v_source.getLines()) {
        System.out.print(line);
      }

      final JCGPGeneratedSource<JCGLShaderKindFragment> f_source =
        gen.generatorGenerateFragmentShader(
          want_version.getNumber(),
          want_version.getAPI());

      for (final String line : f_source.getLines()) {
        System.out.print(line);
      }

      final JCGPCompiler comp = JCGPCompiler.newCompiler();
      return comp.compileProgram(gl, "p", v_source, f_source);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLUnsupportedException e) {
      throw new AssertionError(e);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Trying to bind a nonexistent attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeNonexistent()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a0 = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);

      a = JCGPExecutionAbstractContract.makeArrayBuffer(gl);
      a0 = a.getAttribute("a0");

      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execAttributeBind(gl, "nonexistent", a0);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  @Test public final void testExecution()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final JCGPProgram p =
      JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);

    final ExecCalled e = new ExecCalled();
    Assert.assertFalse(e.called);
    e.execPrepare(gl, p);
    e.execRun(gl);
    Assert.assertTrue(e.called);
  }

  /**
   * Passing <tt>null</tt> to
   * {@link JCGPExecutionAbstract#execPrepare(JCGLShaders, JCGPProgram)}
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPrepareNullGL()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final JCGPProgram p =
      JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);

    final JCGPExecutionAPI<Throwable> e =
      new JCGPExecutionAbstract<Throwable>() {
        @Override protected void execRunActual()
          throws JCGLException,
            Throwable
        {
          throw new UnreachableCodeException();
        }
      };

    e.execPrepare(null, p);
  }

  /**
   * Passing <tt>null</tt> to
   * {@link JCGPExecutionAbstract#execPrepare(JCGLShaders, JCGPProgram)}
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPrepareNullProgram()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final JCGPExecutionAPI<Throwable> e =
      new JCGPExecutionAbstract<Throwable>() {
        @Override protected void execRunActual()
          throws JCGLException,
            Throwable
        {
          throw new UnreachableCodeException();
        }
      };

    e.execPrepare(gl, null);
  }

  /**
   * Running a program without binding all of the attributes fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testRunMissedAttributes()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    JCGPProgram p = null;

    try {
      p =
        JCGPExecutionAbstractContract.makeProgramWithAttributesAndUniforms(
          tc,
          gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
      e.execUniformPutVector4I(gl, "u_integer4_0", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_1", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_2", new VectorI4I(1, 1, 1, 1));
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      Assert.assertFalse(e.called);
      e.execRun(gl);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Running a program with all of the attributes bound, and all of the
   * uniforms assigned, works.
   */

  @Test public final void testRunMissedNone()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    JCGPProgram p = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a0 = null;
    ArrayBufferAttribute a1 = null;
    ArrayBufferAttribute a2 = null;

    try {
      p =
        JCGPExecutionAbstractContract.makeProgramWithAttributesAndUniforms(
          tc,
          gl);
      a = JCGPExecutionAbstractContract.makeArrayBuffer(gl);
      a0 = a.getAttribute("a0");
      a1 = a.getAttribute("a1");
      a2 = a.getAttribute("a2");
      e = new ExecCalled();
      Assert.assertFalse(e.called);
      e.execPrepare(gl, p);
      e.execUniformPutVector4I(gl, "u_integer4_0", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_1", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_2", new VectorI4I(1, 1, 1, 1));
      e.execAttributeBind(gl, "a0", a0);
      e.execAttributeBind(gl, "a1", a1);
      e.execAttributeBind(gl, "a2", a2);
      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }
  }

  /**
   * Running a program without binding all of the attributes fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testRunMissedSomeAttributes()
      throws Throwable
  {
    try {
      final TestContext tc = this.newTestContext();
      final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeProgramWithAttributesAndUniforms(
          tc,
          gl);

      final ArrayBuffer a = JCGPExecutionAbstractContract.makeArrayBuffer(gl);
      final ArrayBufferAttribute a0 = a.getAttribute("a0");

      final ExecCalled e = new ExecCalled();
      Assert.assertFalse(e.called);
      e.execPrepare(gl, p);
      e.execUniformPutVector4I(gl, "u_integer4_0", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_1", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutVector4I(gl, "u_integer4_2", new VectorI4I(1, 1, 1, 1));
      e.execAttributeBind(gl, "a0", a0);
      e.execRun(gl);
    } catch (final ConstraintError e) {
      System.out.println(e);
      throw e;
    }
  }

  /**
   * Running a program without binding all of the uniforms fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testRunMissedUniforms()
      throws Throwable
  {
    final ExecCalled ex = new ExecCalled();
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final JCGPProgram p =
      JCGPExecutionAbstractContract.makeProgramWithAttributesAndUniforms(
        tc,
        gl);

    try {
      Assert.assertFalse(ex.called);
      ex.execPrepare(gl, p);
      ex.execRun(gl);
    } catch (final ConstraintError e) {
      System.out.println(e);
      Assert.assertFalse(ex.called);
      throw e;
    }
  }

  /**
   * Calling {@link JCGPExecutionAbstract#execRun(JCGLShaders)} without
   * calling
   * {@link JCGPExecutionAbstract#execPrepare(JCGLShaders, JCGPProgram)} first
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testRunNotPrepared()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final JCGPExecutionAPI<Throwable> e =
      new JCGPExecutionAbstract<Throwable>() {
        @Override protected void execRunActual()
          throws JCGLException,
            Throwable
        {
          throw new UnreachableCodeException();
        }
      };

    e.execRun(gl);
  }

  /**
   * Calling {@link JCGPExecutionAbstract#execRun(JCGLShaders)} with
   * <tt>null</tt> fails.
   */

  @Test(expected = ConstraintError.class) public final void testRunNull()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final JCGPProgram p =
      JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);

    final JCGPExecutionAPI<Throwable> e =
      new JCGPExecutionAbstract<Throwable>() {
        @Override protected void execRunActual()
          throws JCGLException,
            Throwable
        {
          throw new UnreachableCodeException();
        }
      };

    e.execPrepare(gl, p);
    e.execRun(null);
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentFloat()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutFloat(gl, "nonexistent", 23.0f);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentTextureUnit()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
      units = gl.textureGetUnits();
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert units != null;
      assert e != null;
      e.execUniformPutTextureUnit(gl, "nonexistent", units[0]);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector2F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector2F(gl, "nonexistent", new VectorI2F(23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector2I()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector2I(gl, "nonexistent", new VectorI2I(23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector3F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector3F(gl, "nonexistent", new VectorI3F(23, 23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector3I()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector3I(gl, "nonexistent", new VectorI3I(23, 23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector4F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector4F(gl, "nonexistent", new VectorI4F(
        23,
        23,
        23,
        23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformNonexistentVector4I()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeEmptyProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformPutVector4I(gl, "nonexistent", new VectorI4I(
        23,
        23,
        23,
        23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a uniforms of all types works.
   */

  @Test public final void testUniforms()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a0 = null;
    TextureUnit[] units = null;

    try {
      a = JCGPExecutionAbstractContract.makeArrayBuffer(gl);
      a0 = a.getAttribute("a0");
      units = gl.textureGetUnits();

      final JCGPProgram p =
        JCGPExecutionAbstractContract.makeComplicatedProgram(tc, gl);
      e = new ExecCalled();
      e.execPrepare(gl, p);
      e.execUniformPutVector2F(gl, "ufv2", new VectorI2F(23.0f, 23.0f));
      e
        .execUniformPutVector3F(
          gl,
          "ufv3",
          new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "ufv4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "uiv2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "uiv3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "uiv4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "uf", 23.0f);
      e.execUniformPutTextureUnit(gl, "ut", units[0]);
      e.execUniformPutMatrix3x3F(gl, "um3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "um4", new MatrixM4x4F());
      e.execAttributeBind(gl, "a", a0);
      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }
  }
}
