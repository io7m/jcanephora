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

package com.io7m.jcanephora.gpuprogram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLShaderType;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitVertexShader;

public class JCGPGenerationTest
{
  private static final JCGPSource TEST_STRING_SOURCE;

  static {
    try {
      TEST_STRING_SOURCE = new JCGPStringSource("struct t { };");
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException();
    }
  }

  /**
   * Generating fragment shaders works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGenerateFragmentShaderSourceImports()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 10, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 10, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final JCGPUnit u1 =
      JCGPUnit.makeGeneric(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    final JCGPUnit u2 =
      JCGPUnit.makeGeneric(
        "unit2",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");
    unit0_imports.add("unit2");

    final JCGPUnit.JCGPUnitFragmentShader u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit0_imports,
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
    cp.generatorUnitAdd(u2);

    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);

    cp.generatorSetDebugging(true);
    final JCGPGeneratedSource<JCGLShaderKindFragment> r =
      cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);

    Assert.assertEquals(version, r.getVersion());
    Assert.assertEquals(JCGLApi.JCGL_ES, r.getApi());
    Assert.assertEquals(JCGLShaderType.JCGL_FRAGMENT_SHADER, r.getType());

    final List<String> output = r.getLines();
    for (final String line : output) {
      System.out.println(line);
    }
  }

  /**
   * Generating fragment shaders with cyclic dependencies fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateFragmentShaderSourceImportsCyclic()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");
    final LinkedList<String> unit1_imports = new LinkedList<String>();
    unit1_imports.add("unit0");

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit0_imports,
        version_es,
        version_full);
    final JCGPUnit u1 =
      JCGPUnit.makeGeneric(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit1_imports,
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating fragment shaders with missing dependencies fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateFragmentShaderSourceImportsMissing()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> dependencies = new LinkedList<String>();
    dependencies.add("a");
    dependencies.add("b");
    dependencies.add("c");

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        dependencies,
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating fragment shaders when units import non-generic units fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateFragmentShaderSourceImportsNonGeneric()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit0_imports,
        version_es,
        version_full);
    final JCGPUnit u1 =
      JCGPUnit.makeVertexShader(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating fragment shaders without adding a main function fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateFragmentShaderSourceNoMain()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating fragment shaders with a null API fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testGenerateFragmentShaderSourceNullAPI()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    cp
      .generatorGenerateFragmentShader(new JCGLSLVersionNumber(0, 0, 0), null);
  }

  /**
   * Generating fragment shaders with a null version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testGenerateFragmentShaderSourceNullVersion()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    cp.generatorGenerateFragmentShader(null, JCGLApi.JCGL_ES);
  }

  /**
   * Generating fragment shaders for an unsupported version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testGenerateFragmentShaderSourceUnsupportedFull()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(3, 0, 0);
    cp.generatorGenerateFragmentShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGenerateVertexShaderSourceImports()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(4, 30, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final JCGPUnit u1 =
      JCGPUnit.makeGeneric(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    final JCGPUnit u2 =
      JCGPUnit.makeGeneric(
        "unit2",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");
    unit0_imports.add("unit2");

    final JCGPUnitVertexShader u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        new JCGPStringSource("void main() { gl_Position = vec4(0,0,0,1); }"),
        unit0_imports,
        version_es,
        version_full);

    u0.declareInput(JCGPVertexShaderInput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "input0"));
    u0.declareInput(JCGPVertexShaderInput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "input1"));
    u0.declareInput(JCGPVertexShaderInput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "input2"));

    u0.declareOutput(JCGPVertexShaderOutput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "output0"));
    u0.declareOutput(JCGPVertexShaderOutput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "output1"));
    u0.declareOutput(JCGPVertexShaderOutput.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "output2"));

    u0.declareUniformInput(JCGPUniform.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "uniform0"));
    u0.declareUniformInput(JCGPUniform.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "uniform1"));
    u0.declareUniformInput(JCGPUniform.make(
      JCGLType.TYPE_FLOAT_VECTOR_4,
      "uniform2"));

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
    cp.generatorUnitAdd(u2);

    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(3, 30, 0);

    cp.generatorSetDebugging(true);
    final JCGPGeneratedSource<JCGLShaderKindVertex> r =
      cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_FULL);

    Assert.assertEquals(version, r.getVersion());
    Assert.assertEquals(JCGLApi.JCGL_FULL, r.getApi());
    Assert.assertEquals(JCGLShaderType.JCGL_VERTEX_SHADER, r.getType());

    final List<String> output = r.getLines();
    for (final String line : output) {
      System.out.println(line);
    }
  }

  /**
   * Generating vertex shaders with cyclic dependencies fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateVertexShaderSourceImportsCyclic()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");
    final LinkedList<String> unit1_imports = new LinkedList<String>();
    unit1_imports.add("unit0");

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit0_imports,
        version_es,
        version_full);
    final JCGPUnit u1 =
      JCGPUnit.makeGeneric(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit1_imports,
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders with missing dependencies fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateVertexShaderSourceImportsMissing()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> dependencies = new LinkedList<String>();
    dependencies.add("a");
    dependencies.add("b");
    dependencies.add("c");

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        dependencies,
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders when units import non-generic units fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateVertexShaderSourceImportsNonGeneric()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final LinkedList<String> unit0_imports = new LinkedList<String>();
    unit0_imports.add("unit1");

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        unit0_imports,
        version_es,
        version_full);
    final JCGPUnit u1 =
      JCGPUnit.makeFragmentShader(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders without adding a main function fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLCompileException.class) public
    void
    testGenerateVertexShaderSourceNoMain()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(1, 0, 0);
    cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders with a null API fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testGenerateVertexShaderSourceNullAPI()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    cp.generatorGenerateVertexShader(new JCGLSLVersionNumber(0, 0, 0), null);
  }

  /**
   * Generating vertex shaders with a null version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testGenerateVertexShaderSourceNullVersion()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final ArrayList<String> output = new ArrayList<String>();
    cp.generatorGenerateVertexShader(null, JCGLApi.JCGL_ES);
  }

  /**
   * Generating vertex shaders for an unsupported version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testGenerateVertexShaderSourceUnsupportedFull()
      throws ConstraintError,
        IOException,
        JCGLCompileException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGenerator cp =
      JCGPGenerator.newProgramFullAndES(
        TestData.getLog(),
        "name",
        version_full,
        version_es);

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version_es,
        version_full);

    cp.generatorUnitAdd(u0);

    final ArrayList<String> output = new ArrayList<String>();
    final JCGLSLVersionNumber version = new JCGLSLVersionNumber(3, 0, 0);
    cp.generatorGenerateVertexShader(version, JCGLApi.JCGL_ES);
  }

  /**
   * Creating a new program with a null log fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramESNullLog()
      throws ConstraintError
  {
    final JCGPVersionRange<JCGLApiKindES> version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramES(null, "name", version);
  }

  /**
   * Creating a new program with a null name fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramESNullName()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindES> version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramES(TestData.getLog(), null, version);
  }

  /**
   * Creating a new program with a null version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramESNullVersion()
      throws ConstraintError,
        IOException
  {
    JCGPGenerator.newProgramES(TestData.getLog(), "name", null);
  }

  /**
   * Creating a new program works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testNewProgramFullAndES()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFullAndES(
      TestData.getLog(),
      "name",
      version_full,
      version_es);
  }

  /**
   * Creating a new program with a null log fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullAndESAndESNullLog()
      throws ConstraintError
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFullAndES(null, "name", version_full, version_es);
  }

  /**
   * Creating a new program with a null ES version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullAndESNullESVersion()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFullAndES(
      TestData.getLog(),
      "name",
      version_full,
      null);
  }

  /**
   * Creating a new program with a null name fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullAndESNullName()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> version_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFullAndES(
      TestData.getLog(),
      null,
      version_full,
      version_es);
  }

  /**
   * Creating a new program with a null full version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullAndESNullVersion()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindES> version_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFullAndES(
      TestData.getLog(),
      "name",
      null,
      version_es);
  }

  /**
   * Creating a new program with a null log fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullNullLog()
      throws ConstraintError
  {
    final JCGPVersionRange<JCGLApiKindFull> version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFull(null, "name", version);
  }

  /**
   * Creating a new program with a null name fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullNullName()
      throws ConstraintError,
        IOException
  {
    final JCGPVersionRange<JCGLApiKindFull> version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    JCGPGenerator.newProgramFull(TestData.getLog(), null, version);
  }

  /**
   * Creating a new program with a null version fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNewProgramFullNullVersion()
      throws ConstraintError,
        IOException
  {
    JCGPGenerator.newProgramFull(TestData.getLog(), "name", null);
  }

  /**
   * Adding two main fragment shaders fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testUnitAddFragmentShaderNotUnique()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    final JCGPUnit u1 =
      JCGPUnit.makeFragmentShader(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
  }

  /**
   * Adding/removing/adding a fragment shaders works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnitAddFragmentShaderRemoveAdd()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u0 =
      JCGPUnit.makeFragmentShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitRemove(u0.getName());
    cp.generatorUnitAdd(u0);
  }

  /**
   * Trying to add a null unit fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testUnitAddNull()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator.newProgramES(TestData.getLog(), "name", version);
    cp.generatorUnitAdd(null);
  }

  /**
   * Adding a (generic) unit with the correct version works.
   */

  @SuppressWarnings("static-method") @Test public void testUnitAddOK()
    throws ConstraintError,
      IOException,
      JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(1, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator.newProgramES(TestData.getLog(), "name", version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        version,
        null);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding/removing/adding a generic units works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnitAddUnitRemoveAdd()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u0 =
      JCGPUnit.makeGeneric(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    final JCGPUnit u1 =
      JCGPUnit.makeGeneric(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    final JCGPUnit u2 =
      JCGPUnit.makeGeneric(
        "unit2",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
    cp.generatorUnitAdd(u2);
    cp.generatorUnitRemove(u0.getName());
    cp.generatorUnitRemove(u1.getName());
    cp.generatorUnitRemove(u2.getName());
    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
    cp.generatorUnitAdd(u2);
    cp.generatorUnitRemove(u0.getName());
    cp.generatorUnitRemove(u1.getName());
    cp.generatorUnitRemove(u2.getName());
  }

  /**
   * Adding a unit that doesn't support the right full version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionBadLacking()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindES> unit_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(3, 0, 0),
        new JCGLSLVersionNumber(4, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        unit_version,
        null);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit that doesn't support the right full version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionBadLower()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(0, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit that doesn't support the right full version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionBadUpper()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(3, 0, 0),
        new JCGLSLVersionNumber(4, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit that doesn't support the right ES version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionESBadLacking()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> program_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(3, 0, 0),
        new JCGLSLVersionNumber(4, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator.newProgramES(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit that doesn't support the right ES version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionESBadLower()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> program_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindES> unit_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(0, 0, 0),
        new JCGLSLVersionNumber(0, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator.newProgramES(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        unit_version,
        null);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit that doesn't support the right ES version fails.
   */

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testUnitAddVersionESBadUpper()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindES> program_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindES> unit_version =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLSLVersionNumber(3, 0, 0),
        new JCGLSLVersionNumber(4, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator.newProgramES(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        unit_version,
        null);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding a unit with the same name as another already-added unit fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testUnitAddVersionNameNotUnique()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u =
      JCGPUnit.makeGeneric(
        "unit",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);
    cp.generatorUnitAdd(u);
    cp.generatorUnitAdd(u);
  }

  /**
   * Adding two main vertex shaders fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testUnitAddVertexShaderNotUnique()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    final JCGPUnit u1 =
      JCGPUnit.makeVertexShader(
        "unit1",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitAdd(u1);
  }

  /**
   * Adding/removing/adding a vertex shaders works.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnitAddVertexShaderRemoveAdd()
      throws ConstraintError,
        IOException,
        JCGLUnsupportedException
  {
    final JCGPVersionRange<JCGLApiKindFull> program_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPVersionRange<JCGLApiKindFull> unit_version =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLSLVersionNumber(1, 0, 0),
        new JCGLSLVersionNumber(2, 0, 0));

    final JCGPGeneratorAPI cp =
      JCGPGenerator
        .newProgramFull(TestData.getLog(), "name", program_version);

    final JCGPUnit u0 =
      JCGPUnit.makeVertexShader(
        "unit0",
        JCGPGenerationTest.TEST_STRING_SOURCE,
        new LinkedList<String>(),
        null,
        unit_version);

    cp.generatorUnitAdd(u0);
    cp.generatorUnitRemove(u0.getName());
    cp.generatorUnitAdd(u0);
  }

}
