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

package com.io7m.jcanephora.tests.contracts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestShaders;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jnull.NullCheckException;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

@SuppressWarnings({ "null", "unchecked" }) public abstract class ShadersContract implements
  TestContract
{
  private static ProgramUniformType getUniform(
    final ProgramType p,
    final String name)
    throws AssertionError
  {
    try {
      final ProgramUniformType u = p.programGetUniforms().get(name);
      if (u == null) {
        throw new AssertionError("missing uniform " + name);
      }
      return u;
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  private static ProgramType makeComplexProgram(
    final JCGLInterfaceCommonType gl,
    final FilesystemType fs,
    final PathVirtual sp)

  {
    FragmentShaderType f = null;
    VertexShaderType v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("texture.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("large.v"))));

      final ProgramType p = gl.programCreateCommon("p", v, f);
      gl.programActivate(p);
      return p;
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }
  }

  private static ProgramType makeComplexProgramWithAttributes(
    final JCGLInterfaceCommonType gl,
    final FilesystemType fs,
    final PathVirtual sp)

  {
    FragmentShaderType f = null;
    VertexShaderType v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("complex.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("complex.v"))));

      final ProgramType p = gl.programCreateCommon("p", v, f);
      gl.programActivate(p);
      return p;
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Unbinding a vertex attribute with a null program attribute fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferTypeUnbindVertexAttributeNullProgram()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;

    try {
      final ProgramType pr =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      final ProgramAttributeType pa =
        pr.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      aa = a.arrayGetAttribute("position");
      gp.programActivate(pr);
      gp.programAttributeArrayAssociate(pa, aa);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gp.programAttributeArrayDisassociate((ProgramAttributeType) TestUtilities
      .actuallyNull());
  }

  /**
   * Unbinding a bound vertex attribute works.
   */

  @Test public final void testArrayBufferTypeUnbindVertexAttributeOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;
    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      pa = pr.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      aa = a.arrayGetAttribute("position");
      gp.programActivate(pr);
      gp.programAttributeArrayAssociate(pa, aa);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gp.programAttributeArrayDisassociate(pa);
  }

  /**
   * Check that the number of available vertex attributes is sane.
   */

  @Test public final void testAttributesMax()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    Assert.assertTrue(gl.programGetMaximumActiveAttributes() >= 8);
  }

  /**
   * Empty fragment shaders are rejected.
   */

  @Test(expected = JCGLExceptionProgramCompileError.class) public final
    void
    testFragmentShaderEmpty()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(" ");
    lines.add("\n");
    gl.fragmentShaderCompile("name", lines);
  }

  /**
   * Compiling an invalid fragment shader fails.
   */

  @Test(expected = JCGLExceptionProgramCompileError.class) public final
    void
    testFragmentShaderInvalid()
      throws JCGLException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    TestShaders.getFragmentShaderMayFail(gl, fs, "invalid");
  }

  /**
   * Passing null instead of fragment shader name fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testFragmentShaderNameNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.fragmentShaderCompile(
      (String) TestUtilities.actuallyNull(),
      new ArrayList<String>());
  }

  /**
   * Fragment shader source containing null is rejected.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testFragmentShaderSourceContainsNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(null);
    gl.fragmentShaderCompile("name", lines);
  }

  /**
   * Passing null instead of fragment shader source fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testFragmentShaderSourceNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.fragmentShaderCompile(
      "name",
      (List<String>) TestUtilities.actuallyNull());
  }

  /**
   * Compiling an valid fragment shader works.
   */

  @Test public final void testFragmentShaderValid()
    throws JCGLExceptionRuntime,
      JCGLExceptionProgramCompileError,
      FilesystemError,
      JCGLException,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    TestShaders.getFragmentShaderMayFail(gl, fs, "everything");
  }

  /**
   * Attempting to bind a vertex attribute with a deleted array fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramAttributeBindArrayAttributeDeletedArray()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;
    ArrayAttributeType aa = null;

    try {
      final ProgramType p =
        TestShaders.getEverythingProgram(gl, tc.getFilesystem());

      pa = p.programGetAttributes().get("a_vf4");

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        4));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.arrayGetAttribute("position");
      gl.arrayBufferDelete(a);
      gl.programActivate(p);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    assert aa != null;

    gl.programAttributeArrayAssociate(pa, aa);
  }

  /**
   * Attempting to bind a vertex attribute with a null array attribute fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeBindArrayAttributeNullArrayAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType p =
        TestShaders.getEverythingProgram(gl, tc.getFilesystem());

      pa = p.programGetAttributes().get("a_vf4");
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gl.programAttributeArrayAssociate(
      pa,
      (ArrayAttributeType) TestUtilities.actuallyNull());
  }

  /**
   * Attempting to bind a vertex attribute with a null program attribute
   * fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeBindArrayAttributeNullProgramAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.arrayGetAttribute("position");
      gl.arrayBufferDelete(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert aa != null;
    gl.programAttributeArrayAssociate(
      (ProgramAttributeType) TestUtilities.actuallyNull(),
      aa);
  }

  /**
   * Binding a vertex attribute works.
   */

  @Test public final void testProgramAttributeBindArrayAttributeOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;
    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      gp.programActivate(pr);

      pa = pr.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.arrayGetAttribute("position");

      ga.arrayBufferBind(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayAssociate(pa, aa);
  }

  /**
   * Binding a vertex attribute that doesn't belong to the given array buffer
   * fails.
   */

  @Test(expected = JCGLExceptionBufferNotBound.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongArrayBound()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa1 = null;
    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      pa = pr.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a0 =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      final ArrayBufferType a1 =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa1 = a1.arrayGetAttribute("position");
      ga.arrayBufferBind(a0);
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayAssociate(pa, aa1);
  }

  /**
   * Trying to bind an array attribute to a program attribute when the program
   * that owns the attribute is not active, is an error.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongProgram()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;
    ProgramAttributeType pa = null;

    try {
      final ProgramType pr0 =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());
      final ProgramType pr1 =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      gp.programActivate(pr0);

      pa = pr0.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa = a.arrayGetAttribute("position");
      gp.programActivate(pr1);
      ga.arrayBufferBind(a);

    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayAssociate(pa, aa);
  }

  /**
   * Binding a vertex attribute that doesn't have the same type as the program
   * attribute fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = tc.getGLImplementation().getGLCommon();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ArrayAttributeType aa = null;
    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getPositionProgram(
          tc.getGLImplementation().getGLCommon(),
          tc.getFilesystem());

      pa = pr.programGetAttributes().get("v_position");
      assert pa != null;

      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_INT,
        3));
      final ArrayDescriptor d = b.build();

      final ArrayBufferType a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa = a.arrayGetAttribute("position");

      ga.arrayBufferBind(a);
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayAssociate(pa, aa);
  }

  /**
   * Putting a value into an attribute of a deleted program, fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramAttributeFloatDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programActivate(pr);
      gp.programDelete(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutFloat(pa, 1.0f);
  }

  /**
   * Putting a value into an attribute of an inactive program, fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramAttributeFloatInactive()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programDeactivate();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutFloat(pa, 1.0f);
  }

  /**
   * Putting a value into a null attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeFloatNullAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributePutFloat(
      (ProgramAttributeType) TestUtilities.actuallyNull(),
      1.0f);
  }

  /**
   * Putting a value into an attribute of the right type, works.
   */

  @Test public final void testProgramAttributeFloatOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutFloat(pa, 1.0f);
  }

  /**
   * Putting a value into an attribute of the wrong type, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramAttributeFloatWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf2");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutFloat(pa, 1.0f);
  }

  /**
   * Fetching attributes works.
   */

  @Test public final void testProgramAttributes()
    throws JCGLExceptionProgramCompileError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final Map<String, ProgramAttributeType> as = p.programGetAttributes();

    Assert.assertTrue(as.containsKey("a_f"));
    Assert
      .assertEquals(JCGLType.TYPE_FLOAT, as.get("a_f").attributeGetType());
    Assert.assertTrue(as.containsKey("a_vf2"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, as
      .get("a_vf2")
      .attributeGetType());
    Assert.assertTrue(as.containsKey("a_vf3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, as
      .get("a_vf3")
      .attributeGetType());
    Assert.assertTrue(as.containsKey("a_vf4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, as
      .get("a_vf4")
      .attributeGetType());
  }

  /**
   * Putting a value into an attribute of a deleted program, fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramAttributeVector2FDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf2");
      gp.programActivate(pr);
      gp.programDelete(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector2f(pa, new VectorI2F(0.0f, 1.0f));
  }

  /**
   * Putting a value into an attribute of an inactive program, fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramAttributeVector2FInactive()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf2");
      gp.programDeactivate();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector2f(pa, new VectorI2F(0.0f, 1.0f));
  }

  /**
   * Putting a value into a null attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector2FNullAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributePutVector2f(
      (ProgramAttributeType) TestUtilities.actuallyNull(),
      new VectorI2F(0.0f, 1.0f));
  }

  /**
   * Putting a null value into an attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector2FNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf2");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector2f(
      pa,
      (VectorReadable2FType) TestUtilities.actuallyNull());
  }

  /**
   * Putting a value into an attribute of the right type, works.
   */

  @Test public final void testProgramAttributeVector2FOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf2");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector2f(pa, new VectorI2F(0.0f, 1.0f));
  }

  /**
   * Putting a value into an attribute of the wrong type, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramAttributeVector2FWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector2f(pa, new VectorI2F(0.0f, 1.0f));
  }

  /**
   * Putting a value into an attribute of a deleted program, fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramAttributeVector3FDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf3");
      gp.programActivate(pr);
      gp.programDelete(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector3f(pa, new VectorI3F(0.0f, 1.0f, 2.0f));
  }

  /**
   * Putting a value into an attribute of an inactive program, fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramAttributeVector3FInactive()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf3");
      gp.programDeactivate();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector3f(pa, new VectorI3F(0.0f, 1.0f, 2.0f));
  }

  /**
   * Putting a value into a null attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector3FNullAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributePutVector3f(
      (ProgramAttributeType) TestUtilities.actuallyNull(),
      new VectorI3F(0.0f, 1.0f, 2.0f));
  }

  /**
   * Putting a null value into an attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector3FNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf3");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector3f(
      pa,
      (VectorReadable3FType) TestUtilities.actuallyNull());
  }

  /**
   * Putting a value into an attribute of the right type, works.
   */

  @Test public final void testProgramAttributeVector3FOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf3");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector3f(pa, new VectorI3F(0.0f, 1.0f, 2.0f));
  }

  /**
   * Putting a value into an attribute of the wrong type, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramAttributeVector3FWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector3f(pa, new VectorI3F(0.0f, 1.0f, 2.0f));
  }

  /**
   * Putting a value into an attribute of a deleted program, fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramAttributeVector4FDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf4");
      gp.programActivate(pr);
      gp.programDelete(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector4f(pa, new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
  }

  /**
   * Putting a value into an attribute of an inactive program, fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramAttributeVector4FInactive()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf4");
      gp.programDeactivate();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector4f(pa, new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
  }

  /**
   * Putting a value into a null attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector4FNullAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributePutVector4f(
      (ProgramAttributeType) TestUtilities.actuallyNull(),
      new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
  }

  /**
   * Putting a null value into an attribute, fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramAttributeVector4FNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf4");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector4f(
      pa,
      (VectorReadable4FType) TestUtilities.actuallyNull());
  }

  /**
   * Putting a value into an attribute of the right type, works.
   */

  @Test public final void testProgramAttributeVector4FOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_vf4");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector4f(pa, new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
  }

  /**
   * Putting a value into an attribute of the wrong type, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramAttributeVector4FWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gp = tc.getGLImplementation().getGLCommon();

    ProgramAttributeType pa = null;

    try {
      final ProgramType pr =
        TestShaders.getEverythingProgram(gp, tc.getFilesystem());

      pa = pr.programGetAttributes().get("a_f");
      gp.programActivate(pr);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gp.programAttributePutVector4f(pa, new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
  }

  /**
   * Deleting a program twice fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramDeleteTwice()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    try {
      gl.programDelete(p);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.programDelete(p);
  }

  /**
   * Passing a deleted fragment shader fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramFragmentShaderDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final VertexShaderType v =
      TestShaders.getVertexShader(gl, fs, "everything");
    final FragmentShaderType f =
      TestShaders.getFragmentShader(gl, fs, "everything");

    gl.fragmentShaderDelete(f);
    gl.programCreateCommon("p", v, f);
  }

  /**
   * Passing null instead of a fragment shader fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramFragmentShaderNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final VertexShaderType v =
      TestShaders.getVertexShader(gl, fs, "everything");

    gl.programCreateCommon(
      "name",
      v,
      (FragmentShaderType) TestUtilities.actuallyNull());
  }

  /**
   * Passing null instead of a program name fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramNameNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final FragmentShaderType f =
      TestShaders.getFragmentShader(gl, fs, "everything");
    final VertexShaderType v =
      TestShaders.getVertexShader(gl, fs, "everything");

    gl.programCreateCommon((String) TestUtilities.actuallyNull(), v, f);
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedFloat()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");
    gl.programDelete(p);
    gl.programUniformPutFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedInteger()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_int");
    gl.programDelete(p);
    gl.programUniformPutInteger(pu, 23);
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedMatrix3x3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");
    gl.programDelete(p);
    gl.programUniformPutMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedMatrix4x4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m4");
    gl.programDelete(p);
    gl.programUniformPutMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector2F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf2");
    gl.programDelete(p);
    gl.programUniformPutVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector2I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi2");
    gl.programDelete(p);
    gl.programUniformPutVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf3");
    gl.programDelete(p);
    gl.programUniformPutVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector3I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi3");
    gl.programDelete(p);
    gl.programUniformPutVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf4");
    gl.programDelete(p);
    gl.programUniformPutVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramUniformDeletedVector4I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi4");
    gl.programDelete(p);
    gl.programUniformPutVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveFloat()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");
    gl.programDeactivate();
    gl.programUniformPutFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveMatrix3x3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");

    gl.programDeactivate();
    gl.programUniformPutMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveMatrix4x4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m4");

    gl.programDeactivate();
    gl.programUniformPutMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector2F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf2");
    gl.programDeactivate();
    gl.programUniformPutVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector2I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi2");
    gl.programDeactivate();
    gl.programUniformPutVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf3");
    gl.programDeactivate();
    gl.programUniformPutVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector3I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi3");
    gl.programDeactivate();
    gl.programUniformPutVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf4");
    gl.programDeactivate();
    gl.programUniformPutVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = JCGLExceptionProgramNotActive.class) public final
    void
    testProgramUniformInactiveVector4I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi4");
    gl.programDeactivate();
    gl.programUniformPutVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullFloat()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutFloat(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      23.0f);
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullInteger()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutInteger(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      23);
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullMatrix3x3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutMatrix3x3f(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new MatrixM3x3F());
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullMatrix4x4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutMatrix4x4f(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new MatrixM4x4F());
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullTextureUnit()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final List<TextureUnitType> units = gl.textureGetUnits();
    gl.programUniformPutTextureUnit(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      units.get(0));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector2F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector2f(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI2F(1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector2I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector2i(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI2I(1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector3f(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector3I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector3i(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector4f(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramUniformNullVector4I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.programUniformPutVector4i(
      (ProgramUniformType) TestUtilities.actuallyNull(),
      new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeFloat()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeInteger()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_int");

    gl.programActivate(p);
    gl.programUniformPutInteger(pu, 23);
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeMatrix3x3F()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");

    gl.programActivate(p);
    gl.programUniformPutMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeMatrix4x4F()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m4");

    gl.programActivate(p);
    gl.programUniformPutMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeTextureUnit()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final List<TextureUnitType> units = gl.textureGetUnits();
    final ProgramUniformType pu =
      ShadersContract.getUniform(p, "u_sampler2d");

    gl.programActivate(p);
    gl.programUniformPutTextureUnit(pu, units.get(0));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector2F()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf2");

    gl.programActivate(p);
    gl.programUniformPutVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector2I()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi2");

    gl.programActivate(p);
    gl.programUniformPutVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector3F()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf3");

    gl.programActivate(p);
    gl.programUniformPutVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector3I()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi3");

    gl.programActivate(p);
    gl.programUniformPutVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector4F()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vf4");

    gl.programActivate(p);
    gl.programUniformPutVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector4I()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_vi4");

    gl.programActivate(p);
    gl.programUniformPutVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Fetching uniforms works.
   */

  @Test public final void testProgramUniforms()
    throws JCGLExceptionProgramCompileError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final Map<String, ProgramUniformType> us = p.programGetUniforms();

    Assert.assertTrue(us.containsKey("u_float"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT, us
      .get("u_float")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vf2"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, us
      .get("u_vf2")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vf3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, us
      .get("u_vf3")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vf4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, us
      .get("u_vf4")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vi2"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_2, us
      .get("u_vi2")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vi3"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_3, us
      .get("u_vi3")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_vi4"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_4, us
      .get("u_vi4")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_m3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, us
      .get("u_m3")
      .uniformGetType());
    Assert.assertTrue(us.containsKey("u_m4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, us
      .get("u_m4")
      .uniformGetType());
  }

  /**
   * Setting a uniform of either sampler type works.
   */

  @Test public final void testProgramUniformSamplers()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pus2d =
      ShadersContract.getUniform(p, "u_sampler2d");
    final ProgramUniformType pusc =
      ShadersContract.getUniform(p, "u_sampler_cube");

    final List<TextureUnitType> units = gl.textureGetUnits();
    gl.programActivate(p);
    gl.programUniformPutTextureUnit(pus2d, units.get(0));
    gl.programUniformPutTextureUnit(pusc, units.get(0));
  }

  /**
   * Setting a sampler uniform to something that isn't a sampler, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformSamplersWrong0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pus2d =
      ShadersContract.getUniform(p, "u_sampler2d");

    gl.programActivate(p);
    gl.programUniformPutFloat(pus2d, 1.0f);
  }

  /**
   * Setting a sampler uniform to something that isn't a sampler, fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformSamplersWrong1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pusc =
      ShadersContract.getUniform(p, "u_sampler_cube");

    gl.programActivate(p);
    gl.programUniformPutFloat(pusc, 1.0f);
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeFloat()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");

    gl.programActivate(p);
    gl.programUniformPutFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeInteger()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");

    gl.programActivate(p);
    gl.programUniformPutInteger(pu, 23);
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeMatrix3x3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeMatrix4x4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeTextureUnit()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final List<TextureUnitType> units = gl.textureGetUnits();
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_m3");

    gl.programActivate(p);
    gl.programUniformPutTextureUnit(pu, units.get(0));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector2F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector2I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector3F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector3I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector4F()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testProgramUniformWrongTypeVector4I()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final ProgramType p = TestShaders.getEverythingProgram(gl, fs);
    final ProgramUniformType pu = ShadersContract.getUniform(p, "u_float");

    gl.programActivate(p);
    gl.programUniformPutVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Constructing a valid program works.
   */

  @Test public final void testProgramValid()
    throws JCGLExceptionProgramCompileError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    TestShaders.getEverythingProgram(gl, fs);
  }

  /**
   * Passing a deleted vertex shader fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testProgramVertexShaderDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final VertexShaderType v = TestShaders.getVertexShader(gl, fs, "simple");
    final FragmentShaderType f =
      TestShaders.getFragmentShader(gl, fs, "simple");

    gl.vertexShaderDelete(v);
    gl.programCreateCommon("p", v, f);
  }

  /**
   * Passing null instead of a vertex shader fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testProgramVertexShaderNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    final FragmentShaderType f =
      TestShaders.getFragmentShader(gl, fs, "simple");

    gl.programCreateCommon(
      "name",
      (VertexShaderType) TestUtilities.actuallyNull(),
      f);
  }

  /**
   * Empty vertex shaders are rejected.
   */

  @Test(expected = JCGLExceptionProgramCompileError.class) public final
    void
    testVertexShaderEmpty()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(" ");
    lines.add("\n");
    gl.vertexShaderCompile("name", lines);
  }

  /**
   * Compiling an invalid vertex shader fails.
   */

  @Test(expected = JCGLExceptionProgramCompileError.class) public final
    void
    testVertexShaderInvalid()
      throws JCGLException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    TestShaders.getVertexShaderMayFail(gl, fs, "invalid");
  }

  /**
   * Passing null instead of vertex shader name fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testVertexShaderNameNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.vertexShaderCompile(
      (String) TestUtilities.actuallyNull(),
      new ArrayList<String>());
  }

  /**
   * Vertex shader source containing null is rejected.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testVertexShaderSourceContainsNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(null);
    gl.vertexShaderCompile("name", lines);
  }

  /**
   * Passing null instead of vertex shader source fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testVertexShaderSourceNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    gl.vertexShaderCompile(
      "name",
      (List<String>) TestUtilities.actuallyNull());
  }

  /**
   * Compiling an valid vertex shader works.
   */

  @Test public final void testVertexShaderValid()
    throws JCGLExceptionRuntime,
      JCGLExceptionProgramCompileError,
      FilesystemError,
      JCGLException,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();

    TestShaders.getVertexShaderMayFail(gl, fs, "everything");
  }
}
