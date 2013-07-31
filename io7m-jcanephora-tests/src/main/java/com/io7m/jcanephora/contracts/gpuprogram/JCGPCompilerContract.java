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

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.TestContract;
import com.io7m.jcanephora.gpuprogram.JCGPCompiler;
import com.io7m.jcanephora.gpuprogram.JCGPFragmentShaderInput;
import com.io7m.jcanephora.gpuprogram.JCGPFragmentShaderOutput;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratedSource;
import com.io7m.jcanephora.gpuprogram.JCGPGenerator;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratorAPI;
import com.io7m.jcanephora.gpuprogram.JCGPGeneratorContext;
import com.io7m.jcanephora.gpuprogram.JCGPSource;
import com.io7m.jcanephora.gpuprogram.JCGPStringSource;
import com.io7m.jcanephora.gpuprogram.JCGPUniform;
import com.io7m.jcanephora.gpuprogram.JCGPUnit;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitFragmentShader;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitVertexShader;
import com.io7m.jcanephora.gpuprogram.JCGPVersionRange;
import com.io7m.jcanephora.gpuprogram.JCGPVertexShaderInput;
import com.io7m.jcanephora.gpuprogram.JCGPVertexShaderOutput;

public abstract class JCGPCompilerContract implements TestContract
{
  private static @Nonnull
    JCGPGeneratorAPI
    makeProgramWithAttributesAndUniforms(
      final @Nonnull TestContext tc,
      final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final JCGLSLVersion want_version = gl.metaGetSLVersion();

      final StringBuilder s = new StringBuilder();
      s.append("void main (void)\n");
      s.append("{\n");
      s.append("  vec4 xyz = vec4(0, 0, 0, 0);\n");
      s.append("  vec4 v0  = vec4(u_integer4_0);\n");
      s.append("  vec4 v1  = vec4(u_integer4_1);\n");
      s.append("  vec4 v2  = vec4(u_integer4_2);\n");
      s.append("  f_v0     = v0;\n");
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

      v_unit.declareOutput(JCGPVertexShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "f_v0"));

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
                output.add("  gl_FragColor = vec4(1, 1, 1, 1) + f_v0;\n");
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
                output.add("  gl_FragColor = vec4(1, 1, 1, 1) + f_v0;\n");
                output.add("}\n");
                return;
              }
              break;
            }
          }

          output.add("void main (void)\n");
          output.add("{\n");
          output.add("  out_frag = vec4(1, 1, 1, 1) + f_v0;\n");
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
      f_unit.declareInput(JCGPFragmentShaderInput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "f_v0"));

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

      return gen;
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

  @Test public void testCompileGeneral()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final JCGPCompiler comp = JCGPCompiler.newCompiler();
    final JCGPGeneratorAPI gen =
      JCGPCompilerContract.makeProgramWithAttributesAndUniforms(tc, gl);

    final JCGLSLVersion ver = gl.metaGetSLVersion();
    final JCGPGeneratedSource<JCGLShaderKindVertex> vs =
      gen.generatorGenerateVertexShader(ver.getNumber(), ver.getAPI());
    final JCGPGeneratedSource<JCGLShaderKindFragment> fs =
      gen.generatorGenerateFragmentShader(ver.getNumber(), ver.getAPI());

    comp.compileProgram(gl, "general", vs, fs);
  }
}
