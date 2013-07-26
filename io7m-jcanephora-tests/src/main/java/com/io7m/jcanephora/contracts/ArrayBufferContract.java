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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable3f;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLShaders;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ArrayBufferContract implements TestContract
{
  @SuppressWarnings("resource") static ProgramReference makeProgram(
    final JCGLShaders gl,
    final FSCapabilityAll filesystem,
    final PathVirtual vertex_shader,
    final PathVirtual fragment_shader)
    throws JCGLException,
      ConstraintError,
      FilesystemError,
      JCGLCompileException,
      IOException
  {
    final ProgramReference pr = gl.programCreate("program");

    if (vertex_shader != null) {
      final InputStream vss = filesystem.openFile(vertex_shader);
      final VertexShader vs = gl.vertexShaderCompile("vertex", vss);
      vss.close();
      gl.vertexShaderAttach(pr, vs);
    }

    if (fragment_shader != null) {
      final InputStream fss = filesystem.openFile(fragment_shader);
      final FragmentShader fs = gl.fragmentShaderCompile("fragment", fss);
      fss.close();
      gl.fragmentShaderAttach(pr, fs);
    }

    gl.programLink(pr);
    return pr;
  }

  private static @Nonnull ProgramReference makeStandardPositionProgram(
    final @Nonnull TestContext context,
    final @Nonnull JCGLShaders shaders)
    throws ConstraintError,
      JCGLException,
      FilesystemError,
      JCGLCompileException,
      IOException
  {
    final FSCapabilityAll fs = context.getFilesystem();
    final PathVirtual path = context.getShaderPath();

    final PathVirtual vss = PathVirtual.ofString(path + "/position.v");
    final PathVirtual fss = PathVirtual.ofString(path + "/simple.f");
    final ProgramReference pr =
      ArrayBufferContract.makeProgram(shaders, fs, vss, fss);
    return pr;
  }

  private static long testArrayBufferGridElementsRequired(
    final int x,
    final int z)
  {
    final long x_pos_points = x * 2;
    final long x_neg_points = x * 2;
    final long x_0_points = 2;
    final long x_points = x_0_points + x_neg_points + x_pos_points;

    final long z_pos_points = z * 2;
    final long z_neg_points = z * 2;
    final long z_0_points = 2;
    final long z_points = z_0_points + z_neg_points + z_pos_points;

    return x_points + z_points;
  }

  private static long testArrayBufferGridElementsRequiredActual(
    final int x,
    final int z)
  {
    long c = 0;

    for (int cx = -x; cx <= x; ++cx) {
      ++c;
      ++c;
    }
    for (int cz = -z; cz <= z; ++cz) {
      ++c;
      ++c;
    }

    return c;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull JCGLArrayBuffers getGLArrayBuffers(
    final @Nonnull TestContext context);

  public abstract @Nonnull JCGLShaders getGLPrograms(
    final @Nonnull TestContext context);

  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @Test public final void testArrayBufferAllocate()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(12, a.getElementSizeBytes());
      Assert.assertEquals(10, a.getRange().getInterval());
      Assert.assertEquals(120, a.getSizeBytes());
      Assert.assertEquals(d, a.getDescriptor());
    } finally {
      if (a != null) {
        gl.arrayBufferDelete(a);
      }
    }
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferAllocateZeroElements()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });

    gl.arrayBufferAllocate(0, d, UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Binding a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
    gl.arrayBufferDelete(a);
    gl.arrayBufferBind(a);
  }

  /**
   * Buffer binding/unbinding works.
   */

  @Test public final void testArrayBufferBinding()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
  }

  /**
   * Attempting to bind a vertex attribute with a deleted array fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeDeletedArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferDelete(a);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null array fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferBindVertexAttribute(null, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null attribute fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullAttribute()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null program attribute
   * fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullProgramAttribute()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), null);
  }

  /**
   * Binding a vertex attribute works.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws JCGLCompileException
   */

  @Test public final void testArrayBufferBindVertexAttributeOK()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Binding a vertex attribute that doesn't belong to the given array buffer
   * fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d1, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Binding a vertex attribute that doesn't have the same type as the program
   * attribute fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws JCGLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongType()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_INT,
          3) });

    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d0, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Checking if a deleted buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeletedIsBound()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferDelete(a);
    ga.arrayBufferIsBound(a);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeleteDouble()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      gl.arrayBufferDelete(a);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.arrayBufferDelete(a);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeleteNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferDelete(null);
  }

  /**
   * Array buffer offsets are correct.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   *           , GLUnsupportedException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferElementOffset()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("position", JCGLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("normal", JCGLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("color", JCGLScalarType.TYPE_SHORT, 3), });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(0, a.getElementOffset(0));
      Assert.assertEquals(18, a.getElementOffset(1));
      Assert.assertEquals(36, a.getElementOffset(2));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert a != null;
    a.getElementOffset(3);
  }

  /**
   * Ad-hoc test to ensure that a suspected problem was not actually a bug in
   * the buffer implementation.
   */

  @Test public final void testArrayBufferGridTest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    try {
      final int x = 8;
      final int z = 8;
      final int y = 0;

      final long expected0 =
        ArrayBufferContract.testArrayBufferGridElementsRequired(x, z);
      final long expected1 =
        ArrayBufferContract.testArrayBufferGridElementsRequiredActual(x, z);

      System.out.println("expected0 : " + expected0);
      System.out.println("expected1 : " + expected1);

      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      final ArrayBuffer a =
        gl.arrayBufferAllocate(expected1, d, UsageHint.USAGE_STATIC_READ);

      final ArrayBufferWritableData array_map =
        new ArrayBufferWritableData(a);

      final CursorWritable3f pc = array_map.getCursor3f("position");
      for (int cx = -x; cx <= x; ++cx) {
        pc.put3f(cx, y, -z);
        pc.put3f(cx, y, z);
      }
      for (int cz = -z; cz <= z; ++cz) {
        pc.put3f(-z, y, cz);
        pc.put3f(x, y, cz);
      }

      gl.arrayBufferUpdate(a, array_map);

    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Checking if a null buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferNullIsBound()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferIsBound(null);
  }

  /**
   * Unbinding a vertex attribute for a deleted array fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws JCGLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d0, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
    ga.arrayBufferDelete(a);
    ga.arrayBufferUnbindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with a null attribute fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws JCGLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    ga.arrayBufferUnbindVertexAttribute(a, null, pa);
  }

  /**
   * Unbinding a vertex attribute with a null array fails.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws JCGLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    ga.arrayBufferUnbindVertexAttribute(null, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with a null program attribute fails.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws JCGLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullProgram()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    ga.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), null);
  }

  /**
   * Unbinding a bound vertex attribute works.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws FilesystemError
   * @throws JCGLCompileException
   * @throws IOException
   */

  @Test public final void testArrayBufferUnbindVertexAttributeOK()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      FilesystemError,
      JCGLCompileException,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    ga.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with an unbound array fails.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws JCGLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeUnbound()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    ga.arrayBufferUnbind();
    ga.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute that does not belong to the given array
   * fails.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   * @throws JCGLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeWrongArray()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLShaders gp = this.getGLPrograms(tc);

    final ProgramReference pr =
      ArrayBufferContract.makeStandardPositionProgram(tc, gp);

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    gp.programGetAttributes(pr, attributes);

    final ProgramAttribute pa = attributes.get("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d0, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
    ga.arrayBufferUnbindVertexAttribute(a, d1.getAttribute("position"), pa);
  }

  /**
   * Array buffer complete updates work.
   */

  @Test public final void testArrayBufferUpdateComplete()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates with a deleted buffer bound fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateDeletedFails()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(a);
    gl.arrayBufferDelete(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer partial updates work.
   */

  @Test public final void testArrayBufferUpdatePartial()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data =
      new ArrayBufferWritableData(a, new RangeInclusive(2, 8));
    gl.arrayBufferBind(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates without a bound buffer fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateUnboundFails()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferUnbind();
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates with the wrong buffer bound fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateWrongBindingFails()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;
    ArrayBuffer b = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      b = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(b);
    gl.arrayBufferUpdate(a, data);
  }
}
