package com.io7m.jcanephora.contracts_full;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.contracts_ES2.FilesystemTestContract;

public abstract class ArrayBufferMapContract implements
  GLTestContract,
  FilesystemTestContract
{
  /**
   * Mapping a buffer works.
   */

  @Test public final void testArrayBufferMap()
    throws ConstraintError,
      GLException
  {
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
   * Unmapping a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnmapDeleted()
      throws ConstraintError,
        GLException
  {
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
    Assume.assumeTrue(this.isFullGLSupported());
    final GLInterface gl = this.makeNewGL();

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
