package com.io7m.jcanephora.contracts;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;

public abstract class ArrayBufferContract implements GLTestContract
{
  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @Test public final void testArrayBufferAllocate()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
      Assert.assertEquals(12, a.getElementSizeBytes());
      Assert.assertEquals(10, a.getElements());
      Assert.assertEquals(120, a.getSizeBytes());
      Assert.assertEquals(d, a.getDescriptor());
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
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
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    gl.arrayBufferAllocate(0, d);
  }

  /**
   * Binding a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindDeleted()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
    a.resourceDelete(gl);
    gl.arrayBufferBind(a);
  }

  /**
   * Buffer binding/unbinding works.
   */

  @Test public final void testArrayBufferBinding()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
  }

  /**
   * Checking if a deleted buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeletedIsBound()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);
    a.resourceDelete(gl);
    gl.arrayBufferIsBound(a);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeleteDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
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
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.arrayBufferDelete(null);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferElementOffset()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("position", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("normal", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("color", GLScalarType.TYPE_SHORT, 3), });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(3, d);
      gl.arrayBufferMapWrite(a);
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
   * Mapping a buffer works.
   */

  @Test public final void testArrayBufferMap()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ArrayBufferWritableMap b = gl.arrayBufferMapWrite(a);
        final ShortBuffer s = b.getByteBuffer().asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          s.put(index, (short) index);
        }
      } finally {
        gl.arrayBufferUnmap(a);
      }

      try {
        final ByteBuffer b = gl.arrayBufferMapRead(a);
        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, s.get(index));
        }
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapReadBounds()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ByteBuffer b = gl.arrayBufferMapRead(a);
        b.get(20);
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a deleted buffer read-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadDeleted()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    a.resourceDelete(gl);
    gl.arrayBufferMapRead(a);
  }

  /**
   * Mapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testArrayBufferMapReadDouble()
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

    try {
      a = gl.arrayBufferAllocate(10, d);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.arrayBufferMapRead(a);
      gl.arrayBufferMapRead(a);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadNull()
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
    gl.arrayBufferAllocate(10, d);
    gl.arrayBufferMapRead(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapWriteBounds()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ArrayBufferWritableMap map = gl.arrayBufferMapWrite(a);
        final ByteBuffer b = map.getByteBuffer();
        b.put(20, (byte) 0xff);
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a deleted buffer write-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteDeleted()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    a.resourceDelete(gl);
    gl.arrayBufferMapWrite(a);
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteNull()
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
    gl.arrayBufferAllocate(10, d);
    gl.arrayBufferMapWrite(null);
  }

  /**
   * Checking if a null buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferNullIsBound()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.arrayBufferIsBound(null);
  }

  /**
   * Unmapping a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnmapDeleted()
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
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferMapWrite(a);
    a.resourceDelete(gl);
    gl.arrayBufferUnmap(a);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testArrayBufferUnmapDouble()
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

    try {
      a = gl.arrayBufferAllocate(10, d);
      gl.arrayBufferMapWrite(a);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.arrayBufferUnmap(a);
      gl.arrayBufferUnmap(a);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }
}
