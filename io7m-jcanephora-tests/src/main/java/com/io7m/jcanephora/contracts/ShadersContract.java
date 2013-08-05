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

package com.io7m.jcanephora.contracts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLShadersCommon;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ShadersContract implements TestContract
{
  private static @Nonnull ProgramUniform getUniform(
    final @Nonnull JCGLInterfaceCommon gl,
    final @Nonnull ProgramReference p,
    final @Nonnull String name)
    throws AssertionError
  {
    try {
      final ProgramUniform u = p.getUniforms().get(name);
      if (u == null) {
        throw new AssertionError("missing uniform " + name);
      }
      return u;
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }
  }

  private static @Nonnull ProgramReference makeComplexProgram(
    final @Nonnull JCGLInterfaceCommon gl,
    final @Nonnull FSCapabilityAll fs,
    final @Nonnull PathVirtual sp)

  {
    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("texture.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("large.v"))));

      final ProgramReference p = gl.programCreateCommon("p", v, f);
      gl.programActivate(p);
      return p;
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull ProgramReference makeComplexProgramWithAttributes(
    final @Nonnull JCGLInterfaceCommon gl,
    final @Nonnull FSCapabilityAll fs,
    final @Nonnull PathVirtual sp)

  {
    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("complex.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("complex.v"))));

      final ProgramReference p = gl.programCreateCommon("p", v, f);
      gl.programActivate(p);
      return p;
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull ProgramReference makeStandardPositionProgram(
    final @Nonnull JCGLInterfaceCommon gl,
    final @Nonnull FSCapabilityAll fs,
    final @Nonnull PathVirtual sp)
  {
    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
      v =
        gl
          .vertexShaderCompile("v", ShaderUtilities.readLines(fs.openFile(sp
            .appendName("position.v"))));

      final ProgramReference p = gl.programCreateCommon("p", v, f);
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
   * Unbinding a vertex attribute for a deleted array fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa = a.getAttribute("position");

      ga.arrayBufferBind(a);
      gp.programAttributeArrayBind(pa, aa);
      ga.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gp.programAttributeArrayUnbind(aa, pa);
  }

  /**
   * Unbinding a vertex attribute with a null attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      final ArrayBufferAttribute aa = a.getAttribute("position");
      gp.programAttributeArrayBind(pa, aa);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gp.programAttributeArrayUnbind(null, pa);
  }

  /**
   * Unbinding a vertex attribute with a null program attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullProgram()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      final ProgramAttribute pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      aa = a.getAttribute("position");
      gp.programAttributeArrayBind(pa, aa);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gp.programAttributeArrayUnbind(aa, null);
  }

  /**
   * Unbinding a bound vertex attribute works.
   */

  @Test public final void testArrayBufferUnbindVertexAttributeOK()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      aa = a.getAttribute("position");
      gp.programAttributeArrayBind(pa, aa);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gp.programAttributeArrayUnbind(aa, pa);
  }

  /**
   * Unbinding a vertex attribute with an unbound array fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeUnbound()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      ga.arrayBufferBind(a);
      aa = a.getAttribute("position");
      gp.programAttributeArrayBind(pa, aa);
      ga.arrayBufferUnbind();
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gp.programAttributeArrayUnbind(aa, pa);
  }

  /**
   * Check that the number of available vertex attributes is sane.
   */

  @Test public final void testAttributesMax()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    Assert.assertTrue(gl.programGetMaximumActiveAttributes() >= 8);
  }

  /**
   * Empty fragment shaders are rejected.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderEmpty()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(" ");
    lines.add("\n");
    gl.fragmentShaderCompile("name", lines);
  }

  /**
   * Compiling an invalid fragment shader fails.
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testFragmentShaderInvalid()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    gl.fragmentShaderCompile(
      "name",
      ShaderUtilities.readLines(fs.openFile(sp.appendName("invalid.f"))));
  }

  /**
   * Passing null instead of fragment shader name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderNameNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.fragmentShaderCompile(null, new ArrayList<String>());
  }

  /**
   * Fragment shader source containing null is rejected.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderSourceContainsNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(null);
    gl.fragmentShaderCompile("name", lines);
  }

  /**
   * Passing null instead of fragment shader source fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderSourceNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.fragmentShaderCompile("name", null);
  }

  /**
   * Compiling an valid fragment shader works.
   */

  @Test public final void testFragmentShaderValid()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    gl.fragmentShaderCompile(
      "name",
      ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
  }

  /**
   * Attempting to bind a vertex attribute with a deleted array fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeDeletedArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    ProgramAttribute pa = null;
    ArrayBufferAttribute aa = null;

    try {
      final ProgramReference p =
        ShadersContract.makeComplexProgramWithAttributes(
          gl,
          tc.getFilesystem(),
          tc.getShaderPath());

      pa = p.getAttributes().get("a_vf4");

      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            4) });
      final ArrayBuffer a =
        gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.getAttribute("position");
      gl.arrayBufferDelete(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    assert aa != null;
    gl.programAttributeArrayBind(pa, aa);
  }

  /**
   * Attempting to bind a vertex attribute with a null array attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeNullArrayAttribute()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    ProgramAttribute pa = null;

    try {
      final ProgramReference p =
        ShadersContract.makeComplexProgramWithAttributes(
          gl,
          tc.getFilesystem(),
          tc.getShaderPath());

      pa = p.getAttributes().get("a_vf4");
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert pa != null;
    gl.programAttributeArrayBind(pa, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null program attribute
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeNullProgramAttribute()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            4) });
      final ArrayBuffer a =
        gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.getAttribute("position");
      gl.arrayBufferDelete(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    assert aa != null;
    gl.programAttributeArrayBind(null, aa);
  }

  /**
   * Binding a vertex attribute works.
   */

  @Test public final void testProgramAttributeBindArrayAttributeOK()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      gp.programActivate(pr);

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      aa = a.getAttribute("position");

      ga.arrayBufferBind(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayBind(pa, aa);
  }

  /**
   * Binding a vertex attribute that doesn't belong to the given array buffer
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongArrayBound()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa1 = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d0 =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      final ArrayBuffer a0 =
        ga.arrayBufferAllocate(10, d0, UsageHint.USAGE_STATIC_DRAW);
      final ArrayBuffer a1 =
        ga.arrayBufferAllocate(10, d0, UsageHint.USAGE_STATIC_DRAW);

      aa1 = a1.getAttribute("position");
      ga.arrayBufferBind(a0);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayBind(pa, aa1);
  }

  /**
   * Trying to bind an array attribute to a program attribute when the program
   * that owns the attribute is not active, is an error.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongProgram()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr0 =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());
      final ProgramReference pr1 =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      gp.programActivate(pr0);

      pa = pr0.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa = a.getAttribute("position");
      gp.programActivate(pr1);
      ga.arrayBufferBind(a);

    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayBind(pa, aa);
  }

  /**
   * Binding a vertex attribute that doesn't have the same type as the program
   * attribute fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramAttributeBindArrayAttributeWrongType()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = tc.getGLImplementation().getGLCommon();
    final JCGLShadersCommon gp = tc.getGLImplementation().getGLCommon();

    ArrayBufferAttribute aa = null;
    ProgramAttribute pa = null;

    try {
      final ProgramReference pr =
        ShadersContract.makeStandardPositionProgram(tc
          .getGLImplementation()
          .getGLCommon(), tc.getFilesystem(), tc.getShaderPath());

      pa = pr.getAttributes().get("position");
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_INT,
            3) });

      final ArrayBuffer a =
        ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

      aa = a.getAttribute("position");

      ga.arrayBufferBind(a);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gp.programAttributeArrayBind(pa, aa);
  }

  /**
   * Fetching attributes works.
   */

  @Test public final void testProgramAttributes()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    final ProgramReference p =
      ShadersContract.makeComplexProgramWithAttributes(gl, fs, sp);
    final Map<String, ProgramAttribute> as = p.getAttributes();

    Assert.assertTrue(as.containsKey("a_f"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT, as.get("a_f").getType());
    Assert.assertTrue(as.containsKey("a_vf2"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, as
      .get("a_vf2")
      .getType());
    Assert.assertTrue(as.containsKey("a_vf3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, as
      .get("a_vf3")
      .getType());
    Assert.assertTrue(as.containsKey("a_vf4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, as
      .get("a_vf4")
      .getType());
  }

  /**
   * Deleting a program twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteTwice()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    try {
      gl.programDelete(p);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    gl.programDelete(p);
  }

  /**
   * Passing a deleted fragment shader fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramFragmentShaderDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
      gl.fragmentShaderDelete(f);
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateCommon("p", v, f);
  }

  /**
   * Passing null instead of a fragment shader fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramFragmentShaderNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    VertexShader v = null;

    try {
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateCommon("name", v, null);
  }

  /**
   * Linking a program with inconsistent varying parameters fails.
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramLinkInconsistentVarying()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    VertexShader v = null;
    FragmentShader f = null;

    try {
      v =
        gl.vertexShaderCompile("vertex", ShaderUtilities.readLines(fs
          .openFile(path.appendName("varying0.v"))));
      f =
        gl.fragmentShaderCompile("frag", ShaderUtilities.readLines(fs
          .openFile(path.appendName("varying1.f"))));
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    gl.programCreateCommon("invalid", v, f);
  }

  /**
   * Passing null instead of a program name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramNameNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateCommon(null, v, f);
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedFloat()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programDelete(p);
    gl.programPutUniformFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedMatrix3x3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat3");
    gl.programDelete(p);
    gl.programPutUniformMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedMatrix4x4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat4");
    gl.programDelete(p);
    gl.programPutUniformMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector2F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec2");
    gl.programDelete(p);
    gl.programPutUniformVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector2I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec2");
    gl.programDelete(p);
    gl.programPutUniformVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec3");
    gl.programDelete(p);
    gl.programPutUniformVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector3I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec3");
    gl.programDelete(p);
    gl.programPutUniformVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec4");
    gl.programDelete(p);
    gl.programPutUniformVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformDeletedVector4I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec4");
    gl.programDelete(p);
    gl.programPutUniformVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveFloat()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programDeactivate();
    gl.programPutUniformFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveMatrix3x3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat3");
    gl.programDeactivate();
    gl.programPutUniformMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveMatrix4x4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat4");
    gl.programDeactivate();
    gl.programPutUniformMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector2F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec2");
    gl.programDeactivate();
    gl.programPutUniformVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector2I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec2");
    gl.programDeactivate();
    gl.programPutUniformVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec3");
    gl.programDeactivate();
    gl.programPutUniformVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector3I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec3");
    gl.programDeactivate();
    gl.programPutUniformVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec4");
    gl.programDeactivate();
    gl.programPutUniformVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of an inactive program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformInactiveVector4I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec4");
    gl.programDeactivate();
    gl.programPutUniformVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullFloat()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformFloat(null, 23.0f);
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullMatrix3x3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformMatrix3x3f(null, new MatrixM3x3F());
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullMatrix4x4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformMatrix4x4f(null, new MatrixM4x4F());
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullTextureUnit()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final TextureUnit[] units = gl.textureGetUnits();
    gl.programPutUniformTextureUnit(null, units[0]);
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector2F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector2f(null, new VectorI2F(1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector2I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector2i(null, new VectorI2I(1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector3f(null, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector3I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector3i(null, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector4f(null, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a null uniform fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformNullVector4I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programPutUniformVector4i(null, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeFloat()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeMatrix3x3F()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat3");
    gl.programPutUniformMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeMatrix4x4F()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat4");
    gl.programPutUniformMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeTextureUnit()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final TextureUnit[] units = gl.textureGetUnits();
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "sampler");
    gl.programPutUniformTextureUnit(pu, units[0]);
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector2F()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec2");
    gl.programPutUniformVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector2I()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec2");
    gl.programPutUniformVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector3F()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec3");
    gl.programPutUniformVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector3I()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec3");
    gl.programPutUniformVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector4F()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_vec4");
    gl.programPutUniformVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the right type works.
   */

  @Test public final void testProgramUniformRightTypeVector4I()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_ivec4");
    gl.programPutUniformVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Fetching uniforms works.
   */

  @Test public final void testProgramUniforms()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final Map<String, ProgramUniform> us = p.getUniforms();

    Assert.assertTrue(us.containsKey("u_float"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT, us.get("u_float").getType());
    Assert.assertTrue(us.containsKey("u_vec2"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, us
      .get("u_vec2")
      .getType());
    Assert.assertTrue(us.containsKey("u_vec3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, us
      .get("u_vec3")
      .getType());
    Assert.assertTrue(us.containsKey("u_vec4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, us
      .get("u_vec4")
      .getType());
    Assert.assertTrue(us.containsKey("u_ivec2"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_2, us
      .get("u_ivec2")
      .getType());
    Assert.assertTrue(us.containsKey("u_ivec3"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_3, us
      .get("u_ivec3")
      .getType());
    Assert.assertTrue(us.containsKey("u_ivec4"));
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_4, us
      .get("u_ivec4")
      .getType());
    Assert.assertTrue(us.containsKey("u_mat3"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, us
      .get("u_mat3")
      .getType());
    Assert.assertTrue(us.containsKey("u_mat4"));
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, us
      .get("u_mat4")
      .getType());
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeFloat()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat3");
    gl.programPutUniformFloat(pu, 23.0f);
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeMatrix3x3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformMatrix3x3f(pu, new MatrixM3x3F());
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeMatrix4x4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformMatrix4x4f(pu, new MatrixM4x4F());
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeTextureUnit()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final TextureUnit[] units = gl.textureGetUnits();
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_mat3");
    gl.programPutUniformTextureUnit(pu, units[0]);
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector2F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector2f(pu, new VectorI2F(1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector2I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector2i(pu, new VectorI2I(1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector3F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector3f(pu, new VectorI3F(1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector3I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector3i(pu, new VectorI3I(1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector4F()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector4f(pu, new VectorI4F(1, 1, 1, 1));
  }

  /**
   * Setting a uniform of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformWrongTypeVector4I()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final ProgramReference p = ShadersContract.makeComplexProgram(gl, fs, sp);
    final ProgramUniform pu = ShadersContract.getUniform(gl, p, "u_float");
    gl.programPutUniformVector4i(pu, new VectorI4I(1, 1, 1, 1));
  }

  /**
   * Constructing a valid program works.
   */

  @Test public final void testProgramValid()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    ShadersContract.makeComplexProgram(gl, fs, sp);
  }

  /**
   * Passing a deleted vertex shader fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramVertexShaderDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      gl.vertexShaderDelete(v);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateCommon("p", v, f);
  }

  /**
   * Passing null instead of a vertex shader fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramVertexShaderNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    FragmentShader f = null;

    try {
      f =
        gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.f"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateCommon("name", null, f);
  }

  /**
   * Empty vertex shaders are rejected.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderEmpty()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(" ");
    lines.add("\n");
    gl.vertexShaderCompile("name", lines);
  }

  /**
   * Compiling an invalid vertex shader fails.
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testVertexShaderInvalid()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    gl.vertexShaderCompile(
      "name",
      ShaderUtilities.readLines(fs.openFile(sp.appendName("invalid.v"))));
  }

  /**
   * Passing null instead of vertex shader name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderNameNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.vertexShaderCompile(null, new ArrayList<String>());
  }

  /**
   * Vertex shader source containing null is rejected.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderSourceContainsNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ArrayList<String> lines = new ArrayList<String>();
    lines.add(null);
    gl.vertexShaderCompile("name", lines);
  }

  /**
   * Passing null instead of vertex shader source fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderSourceNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.vertexShaderCompile("name", null);
  }

  /**
   * Compiling an valid vertex shader works.
   */

  @Test public final void testVertexShaderValid()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();

    gl.vertexShaderCompile(
      "name",
      ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
  }
}
