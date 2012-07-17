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
import com.io7m.jcanephora.IndexBufferReadableMap;
import com.io7m.jcanephora.IndexBufferWritableMap;

public abstract class IndexBufferContract implements GLTestContract
{
  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateByte()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
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
      Assert.assertEquals(4, i.getElements());
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
    final GLInterface gl = this.getGL();
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
      Assert.assertEquals(2, i.getElements());
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
    final GLInterface gl = this.getGL();
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
    final GLInterface gl = this.getGL();
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
      Assert.assertEquals(4, i.getElements());
      Assert.assertEquals(8, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
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
    final GLInterface gl = this.getGL();
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
    final GLInterface gl = this.getGL();
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
    final GLInterface gl = this.getGL();
    gl.indexBufferDelete(null);
  }

  /**
   * Mapping a buffer works.
   */

  @Test public final void testIndexBufferMap()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 10);

    try {
      try {
        final IndexBufferWritableMap b = gl.indexBufferMapWrite(i);
        for (int index = 0; index < 10; ++index) {
          b.put(index, index);
        }
      } finally {
        gl.indexBufferUnmap(i);
      }

      try {
        final IndexBufferReadableMap b = gl.indexBufferMapRead(i);
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, b.get(index));
        }
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapReadBoundsByte()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 10);

    try {
      try {
        final IndexBufferReadableMap b = gl.indexBufferMapRead(i);
        b.get(10);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapReadBoundsInt()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 65536);

    try {
      try {
        final IndexBufferReadableMap b = gl.indexBufferMapRead(i);
        b.get(65536);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapReadBoundsShort()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 256);

    try {
      try {
        final IndexBufferReadableMap b = gl.indexBufferMapRead(i);
        b.get(256);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testIndexBufferMapReadDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;
    IndexBuffer i = null;

    try {
      a = gl.arrayBufferAllocate(4, d);
      i = gl.indexBufferAllocate(a, 10);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.indexBufferMapRead(i);
      gl.indexBufferMapRead(i);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferMapReadNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.indexBufferMapRead(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapWriteBoundsByte()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 10);

    try {
      try {
        final IndexBufferWritableMap b = gl.indexBufferMapWrite(i);
        b.put(10, 0xff);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapWriteBoundsInt()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 65536);

    try {
      try {
        final IndexBufferWritableMap b = gl.indexBufferMapWrite(i);
        b.put(65536, 0xffffffff);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testIndexBufferMapWriteBoundsShort()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(4, d);
    final IndexBuffer i = gl.indexBufferAllocate(a, 256);

    try {
      try {
        final IndexBufferWritableMap b = gl.indexBufferMapWrite(i);
        b.put(256, 0xffff);
      } finally {
        gl.indexBufferUnmap(i);
      }
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferMapWriteNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.indexBufferMapWrite(null);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testIndexBufferUnmapDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;
    IndexBuffer i = null;

    try {
      a = gl.arrayBufferAllocate(4, d);
      i = gl.indexBufferAllocate(a, 10);
      gl.indexBufferMapWrite(i);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.indexBufferUnmap(i);
      gl.indexBufferUnmap(i);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (i != null) {
        i.resourceDelete(gl);
      }
    }
  }
}
