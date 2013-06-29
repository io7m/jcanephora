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
package com.io7m.jcanephora.contracts.common;

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
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.GLArrayBuffers;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLShaders;
import com.io7m.jcanephora.GLUnsupportedException;
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
    final GLShaders gl,
    final FSCapabilityAll filesystem,
    final PathVirtual vertex_shader,
    final PathVirtual fragment_shader)
    throws GLException,
      ConstraintError,
      FilesystemError,
      GLCompileException,
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
    final @Nonnull GLShaders shaders)
    throws ConstraintError,
      GLException,
      FilesystemError,
      GLCompileException,
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

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull GLArrayBuffers getGLArrayBuffers(
    final @Nonnull TestContext context);

  public abstract @Nonnull GLShaders getGLPrograms(
    final @Nonnull TestContext context);

  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @Test public final void testArrayBufferAllocate()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
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
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeDeletedArray()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferDelete(a);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null array fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullArray()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferBindVertexAttribute(null, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null attribute fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullAttribute()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null program attribute
   * fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullProgramAttribute()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), null);
  }

  /**
   * Binding a vertex attribute works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test public final void testArrayBufferBindVertexAttributeOK()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      GLCompileException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    ga.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Binding a vertex attribute that doesn't belong to the given array buffer
   * fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongArray()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongType()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_INT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferDelete(null);
  }

  /**
   * Array buffer offsets are correct.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferElementOffset()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("position", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("normal", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("color", GLScalarType.TYPE_SHORT, 3), });
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
   * Checking if a null buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferNullIsBound()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferIsBound(null);
  }

  /**
   * Unbinding a vertex attribute for a deleted array fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws GLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullArray()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullProgram()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws FilesystemError
   * @throws GLCompileException
   * @throws IOException
   */

  @Test public final void testArrayBufferUnbindVertexAttributeOK()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      GLCompileException,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   * @throws IOException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeUnbound()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
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
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws ConstraintError
   * @throws GLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeWrongArray()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLShaders gp = this.getGLPrograms(tc);

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
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers gl = this.getGLArrayBuffers(tc);

    ArrayBuffer a = null;
    ArrayBuffer b = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
