package com.io7m.jcanephora;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;

public class ArrayBufferLWJGL30Test
{
  private Pbuffer buffer;

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
  }

  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @SuppressWarnings("static-method") @Test public void testArrayBufferAllocate()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferAllocateZeroElements()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    gl.arrayBufferAllocate(0, d);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferDeleteDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferDeleteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.arrayBufferDelete(null);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferElementOffset()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test public void testArrayBufferMap()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test(expected = IndexOutOfBoundsException.class) public
    void
    testArrayBufferMapReadBounds()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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
   * Mapping a buffer twice fails.
   */

  @SuppressWarnings("static-method") @Test(expected = GLException.class) public
    void
    testArrayBufferMapReadDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
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

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferMapReadNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test(expected = IndexOutOfBoundsException.class) public
    void
    testArrayBufferMapWriteBounds()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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
   * Mapping a null buffer fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayBufferMapWriteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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
   * Unmapping a buffer twice fails.
   */

  @SuppressWarnings("static-method") @Test(expected = GLException.class) public
    void
    testArrayBufferUnmapDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
      throw e;
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }
}
