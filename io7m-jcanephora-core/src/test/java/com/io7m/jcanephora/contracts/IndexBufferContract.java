package com.io7m.jcanephora.contracts;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.IndexBuffer;

public abstract class IndexBufferContract implements GLTestContract
{
  /**
   * Allocating an index buffer from a deleted array buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateArrayDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(1, d);
    a.resourceDelete(gl);
    gl.indexBufferAllocate(a, 0);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateByte()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(255, d);
      i = gl.indexBufferAllocate(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_BYTE, i.getType());
      Assert.assertEquals(1, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
      Assert.assertEquals(4, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateInt()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(65536, d);
      i = gl.indexBufferAllocate(a, 2);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_INT, i.getType());
      Assert.assertEquals(4, i.getElementSizeBytes());
      Assert.assertEquals(2, i.getRange().getInterval());
      Assert.assertEquals(8, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * Passing null to allocateIndexBuffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.indexBufferAllocate(null, 1);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateShort()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(256, d);
      i = gl.indexBufferAllocate(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_SHORT, i.getType());
      Assert.assertEquals(2, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
      Assert.assertEquals(8, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * Allocating with a null type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateTypeNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.indexBufferAllocateType(null, 1);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateTypeZeroElements()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 0);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateZeroElements()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(1, d);
    gl.indexBufferAllocate(a, 0);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferDeleteDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(4, d);
      i = gl.indexBufferAllocate(a, 10);
      gl.indexBufferDelete(i);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.indexBufferDelete(i);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferDeleteNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.indexBufferDelete(null);
  }
}
