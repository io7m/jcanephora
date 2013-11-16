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

package com.io7m.jcanephora.contracts.checkedexec;

import java.io.IOException;
import java.util.List;

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
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLShadersCommon;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramReferenceUsable;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jcanephora.checkedexec.JCCEExecutionAPI;
import com.io7m.jcanephora.checkedexec.JCCEExecutionAbstract;
import com.io7m.jcanephora.contracts.TestContract;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class JCCEExecutionAbstractContract implements TestContract
{
  private static final class ExecCalled extends JCCEExecutionAbstract
  {
    public boolean called;

    public ExecCalled(
      final ProgramReferenceUsable p)
      throws ConstraintError
    {
      super(p);
      this.called = false;
    }

    @Override protected void execRunActual()
      throws Exception
    {
      this.called = true;
    }
  }

  private static @Nonnull ArrayBuffer makeArrayBuffer(
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final ArrayBufferAttributeDescriptor[] as =
        new ArrayBufferAttributeDescriptor[4];

      as[0] =
        new ArrayBufferAttributeDescriptor(
          "a_vf2",
          JCGLScalarType.TYPE_FLOAT,
          2);
      as[1] =
        new ArrayBufferAttributeDescriptor(
          "a_vf3",
          JCGLScalarType.TYPE_FLOAT,
          3);
      as[2] =
        new ArrayBufferAttributeDescriptor(
          "a_vf4",
          JCGLScalarType.TYPE_FLOAT,
          4);
      as[3] =
        new ArrayBufferAttributeDescriptor(
          "a_f",
          JCGLScalarType.TYPE_FLOAT,
          1);

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

  private static @Nonnull ProgramReference makeProgram(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final PathVirtual vsp = tc.getShaderPath().appendName("complex.v");
      final PathVirtual fsp = tc.getShaderPath().appendName("simple.f");

      final List<String> vs_lines =
        JCCEExecutionAbstractContract.readLines(tc.getFilesystem(), vsp);
      final List<String> fs_lines =
        JCCEExecutionAbstractContract.readLines(tc.getFilesystem(), fsp);

      final VertexShader v = gl.vertexShaderCompile("v", vs_lines);
      final FragmentShader f = gl.fragmentShaderCompile("f", fs_lines);
      return gl.programCreateCommon("p", v, f);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final FilesystemError e) {
      throw new AssertionError(e);
    } catch (final IOException e) {
      throw new AssertionError(e);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull ProgramReference makeProgramWithFragmentUniforms(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final PathVirtual vsp = tc.getShaderPath().appendName("complex.v");
      final PathVirtual fsp = tc.getShaderPath().appendName("complex.f");

      final List<String> vs_lines =
        JCCEExecutionAbstractContract.readLines(tc.getFilesystem(), vsp);
      final List<String> fs_lines =
        JCCEExecutionAbstractContract.readLines(tc.getFilesystem(), fsp);

      final VertexShader v = gl.vertexShaderCompile("v", vs_lines);
      final FragmentShader f = gl.fragmentShaderCompile("f", fs_lines);
      final ProgramReference p = gl.programCreateCommon("p", v, f);
      gl.vertexShaderDelete(v);
      gl.fragmentShaderDelete(f);
      return p;
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final FilesystemError e) {
      throw new AssertionError(e);
    } catch (final IOException e) {
      throw new AssertionError(e);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull List<String> readLines(
    final @Nonnull FSCapabilityRead filesystem,
    final @Nonnull PathVirtual path)
    throws FilesystemError,
      ConstraintError,
      IOException
  {
    return ShaderUtilities.readLines(filesystem.openFile(path));
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Trying to bind an array attribute if the array isn't bound, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeArrayNotBound()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    ProgramReferenceUsable p = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;

    try {
      p = JCCEExecutionAbstractContract.makeProgram(tc, gl);
      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      a_vf2 = a.getAttribute("a_vf2");
      e = new ExecCalled(p);
      Assert.assertFalse(e.called);
      e.execPrepare(gl);
      gl.arrayBufferUnbind();
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    e.execAttributeBind(gl, "a_vf2", a_vf2);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      a0 = a.getAttribute("a_vf2");

      e = new ExecCalled(p);
      e.execPrepare(gl);
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

  /**
   * Assigning a nonexistent attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeNonexistentFloat()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execAttributePutFloat(gl, "nonexistent", 23.0f);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeNonexistentVector2F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execAttributePutVector2F(gl, "nonexistent", new VectorI2F(23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeNonexistentVector3F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e
        .execAttributePutVector3F(
          gl,
          "nonexistent",
          new VectorI3F(23, 23, 23));
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning a nonexistent attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testAttributeNonexistentVector4F()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execAttributePutVector4F(gl, "nonexistent", new VectorI4F(
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
   * Assigning attributes of all types works.
   */

  @Test public final void testAttributes()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;

    try {
      units = gl.textureGetUnits();

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributePutVector2F(gl, "a_vf2", new VectorI2F(0.0f, 1.0f));
      e
        .execAttributePutVector3F(
          gl,
          "a_vf3",
          new VectorI3F(0.0f, 1.0f, 2.0f));
      e.execAttributePutVector4F(gl, "a_vf4", new VectorI4F(
        0.0f,
        1.0f,
        2.0f,
        3.0f));
      e.execAttributePutFloat(gl, "a_f", 1.0f);

      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }
  }

  /**
   * Cancelling when not prepared, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testCancelAfterRun()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
      e.execRun(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    Assert.assertTrue(e.called);
    e.execCancel();
  }

  /**
   * Cancelling execution cancels preparation.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testCancelBeforeRun()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);

      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
      e.execCancel();
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    Assert.assertFalse(e.called);
    e.execRun(gl);
  }

  /**
   * Cancelling when already cancelled, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testCancelCancelled()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
      e.execCancel();
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    Assert.assertFalse(e.called);
    e.execCancel();
  }

  /**
   * Passing a deleted program to the constructor, fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public final
    void
    testMakeDeletedProgram()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ProgramReference p =
      JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);

    gl.programDelete(p);
    new JCCEExecutionAbstract(null) {
      @Override protected void execRunActual()
        throws Exception
      {
        throw new UnreachableCodeException();
      }
    };
  }

  /**
   * Passing <tt>null</tt> to the constructor instead of a program, fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public final
    void
    testMakeNullProgram()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    new JCCEExecutionAbstract(null) {
      @Override protected void execRunActual()
        throws Exception
      {
        throw new UnreachableCodeException();
      }
    };
  }

  /**
   * Trying to assign uniforms or attributes before preparation, fails.
   */

  @Test public final void testPreparation()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;
    ProgramReferenceUsable p = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert units != null;
    assert e != null;
    boolean failed = false;

    /**
     * Trying to assign each type of uniform or attribute fails, because the
     * execution is not in the preparation stage.
     */

    try {
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutFloat(gl, "u_f", 23.0f);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf2", a_vf2);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf3", a_vf3);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf4", a_vf4);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_f", a_f);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;
  }

  /**
   * Trying to assign uniforms or attributes before preparation (after running
   * has cancelled preparation), fails.
   * 
   * This ensures that the constraint errors raised aren't due to nonexistent
   * or ill-typed uniforms.
   */

  @Test public final void testPreparationAfterRun()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;
    ProgramReferenceUsable p = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);

      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert units != null;
    assert e != null;
    boolean failed = false;

    /**
     * Trying to assign each type of uniform or attribute fails, because the
     * execution is not in the preparation stage.
     */

    try {
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutFloat(gl, "u_f", 23.0f);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf2", a_vf2);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf3", a_vf3);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_vf4", a_vf4);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;

    try {
      e.execAttributeBind(gl, "a_f", a_f);
    } catch (final ConstraintError x) {
      failed = true;
    }
    Assert.assertTrue(failed);
    failed = false;
  }

  /**
   * Preparing after cancelling works.
   */

  @Test public final void testPrepareCancelPrepare()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
      e.execCancel();
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    Assert.assertFalse(e.called);
    e.execPrepare(gl);
  }

  /**
   * Passing <tt>null</tt> to
   * {@link JCCEExecutionAbstract#execPrepare(JCGLShadersCommon)} fails.
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
    final ProgramReferenceUsable p =
      JCCEExecutionAbstractContract.makeProgram(tc, gl);

    final JCCEExecutionAPI e = new JCCEExecutionAbstract(p) {
      @Override protected void execRunActual()
        throws JCGLException
      {
        throw new UnreachableCodeException();
      }
    };

    e.execPrepare(null);
  }

  /**
   * Preparing twice without cancelling or running, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPrepareTwice()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    assert e != null;
    Assert.assertFalse(e.called);
    e.execPrepare(gl);
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
    ProgramReferenceUsable p = null;

    try {
      p = JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      Assert.assertFalse(e.called);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(1, 1));
      e.execUniformPutVector3F(gl, "u_vf3", new VectorI3F(1, 1, 1));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(1, 1, 1, 1));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(1, 1));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(1, 1, 1));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());
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
    ProgramReferenceUsable p = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      p = JCCEExecutionAbstractContract.makeProgram(tc, gl);
      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);

      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");
      e = new ExecCalled(p);
      Assert.assertFalse(e.called);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(1, 1));
      e.execUniformPutVector3F(gl, "u_vf3", new VectorI3F(1, 1, 1));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(1, 1, 1, 1));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(1, 1));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(1, 1, 1));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());
      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);
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
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    ProgramReferenceUsable p = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a0 = null;

    try {
      p = JCCEExecutionAbstractContract.makeProgram(tc, gl);
      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      a0 = a.getAttribute("a_f");

      e = new ExecCalled(p);
      Assert.assertFalse(e.called);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(1, 1));
      e.execUniformPutVector3F(gl, "u_vf3", new VectorI3F(1, 1, 1));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(1, 1, 1, 1));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(1, 1));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(1, 1, 1));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(1, 1, 1, 1));
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    try {
      e.execAttributeBind(gl, "a0", a0);
      e.execRun(gl);
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
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

    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ProgramReferenceUsable p =
      JCCEExecutionAbstractContract.makeProgram(tc, gl);

    ExecCalled ex = null;

    try {
      ex = new ExecCalled(p);
      Assert.assertFalse(ex.called);
      ex.execPrepare(gl);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    try {
      ex.execRun(gl);
    } catch (final ConstraintError e) {
      System.out.println(e);
      Assert.assertFalse(ex.called);
      throw e;
    }
  }

  /**
   * Calling {@link JCCEExecutionAbstract#execRun(JCGLShadersCommon)} without
   * calling {@link JCCEExecutionAbstract#execPrepare(JCGLShadersCommon)}
   * first fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testRunNotPrepared()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final ProgramReference p =
      JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
    final JCCEExecutionAPI e = new JCCEExecutionAbstract(p) {
      @Override protected void execRunActual()
        throws Exception
      {
        throw new UnreachableCodeException();
      }
    };

    e.execRun(gl);
  }

  /**
   * Calling {@link JCCEExecutionAbstract#execRun(JCGLShadersCommon)} with
   * <tt>null</tt> fails.
   */

  @Test(expected = ConstraintError.class) public final void testRunNull()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ProgramReferenceUsable p =
      JCCEExecutionAbstractContract.makeProgram(tc, gl);

    final JCCEExecutionAPI e = new JCCEExecutionAbstract(p) {
      @Override protected void execRunActual()
        throws Exception
      {
        throw new UnreachableCodeException();
      }
    };

    e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
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
   * Reusing a uniform value that has been assigned, works.
   */

  @Test public final void testUniformReuseAssigned()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;
    ProgramReferenceUsable p = null;

    try {
      p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);

      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);

      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;

      e.execPrepare(gl);
      e.execUniformUseExisting("u_vf2");
      e.execUniformUseExisting("u_vf3");
      e.execUniformUseExisting("u_vf4");
      e.execUniformUseExisting("u_vi2");
      e.execUniformUseExisting("u_vi3");
      e.execUniformUseExisting("u_vi4");
      e.execUniformUseExisting("u_f");
      e.execUniformUseExisting("u_t");
      e.execUniformUseExisting("u_m3");
      e.execUniformUseExisting("u_m4");

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);

      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }
  }

  /**
   * Reusing a uniform value that has not ever been assigned, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformReuseNeverAssigned()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformUseExisting("u_f");
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Reusing a nonexistent uniform value, fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUniformReuseNonexistent()
      throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;

    try {
      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgram(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }

    try {
      assert e != null;
      e.execUniformUseExisting("nonexistent");
    } catch (final ConstraintError x) {
      System.out.println(x);
      throw x;
    }
  }

  /**
   * Assigning uniforms of all types works.
   */

  @Test public final void testUniforms()
    throws Throwable
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ExecCalled e = null;
    TextureUnit[] units = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute a_vf2 = null;
    ArrayBufferAttribute a_vf3 = null;
    ArrayBufferAttribute a_vf4 = null;
    ArrayBufferAttribute a_f = null;

    try {
      units = gl.textureGetUnits();

      a = JCCEExecutionAbstractContract.makeArrayBuffer(gl);
      gl.arrayBufferBind(a);
      a_vf2 = a.getAttribute("a_vf2");
      a_vf3 = a.getAttribute("a_vf3");
      a_vf4 = a.getAttribute("a_vf4");
      a_f = a.getAttribute("a_f");

      final ProgramReferenceUsable p =
        JCCEExecutionAbstractContract.makeProgramWithFragmentUniforms(tc, gl);
      e = new ExecCalled(p);
      e.execPrepare(gl);
      e.execUniformPutVector2F(gl, "u_vf2", new VectorI2F(23.0f, 23.0f));
      e.execUniformPutVector3F(
        gl,
        "u_vf3",
        new VectorI3F(23.0f, 23.0f, 23.0f));
      e.execUniformPutVector4F(gl, "u_vf4", new VectorI4F(
        23.0f,
        23.0f,
        23.0f,
        23.0f));
      e.execUniformPutVector2I(gl, "u_vi2", new VectorI2I(23, 23));
      e.execUniformPutVector3I(gl, "u_vi3", new VectorI3I(23, 23, 23));
      e.execUniformPutVector4I(gl, "u_vi4", new VectorI4I(23, 23, 23, 23));
      e.execUniformPutFloat(gl, "u_f", 23.0f);
      e.execUniformPutTextureUnit(gl, "u_t", units[0]);
      e.execUniformPutMatrix3x3F(gl, "u_m3", new MatrixM3x3F());
      e.execUniformPutMatrix4x4F(gl, "u_m4", new MatrixM4x4F());

      e.execAttributeBind(gl, "a_vf2", a_vf2);
      e.execAttributeBind(gl, "a_vf3", a_vf3);
      e.execAttributeBind(gl, "a_vf4", a_vf4);
      e.execAttributeBind(gl, "a_f", a_f);

      e.execRun(gl);
      Assert.assertTrue(e.called);
    } catch (final Throwable x) {
      Assert.fail(x.getMessage());
    }
  }
}
