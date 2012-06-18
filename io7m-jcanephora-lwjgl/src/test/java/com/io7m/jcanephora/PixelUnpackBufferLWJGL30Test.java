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

public class PixelUnpackBufferLWJGL30Test
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
   * An allocated buffer has the correct number of elements, element type,
   * element size, and element value count.
   */

  @Test public void testPixelUnpackBufferAllocate()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    PixelUnpackBuffer a = null;

    try {
      a =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          2,
          UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(2, a.getElementSizeBytes());
      Assert.assertEquals(10, a.getElements());
      Assert.assertEquals(20, a.getSizeBytes());
      Assert.assertEquals(2, a.getElementValues());
      Assert
        .assertEquals(GLScalarType.TYPE_UNSIGNED_BYTE, a.getElementType());
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferAllocateZeroElements()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.pixelUnpackBufferAllocate(
      0,
      GLScalarType.TYPE_UNSIGNED_BYTE,
      2,
      UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Allocating a buffer of zero-size elements fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferAllocateZeroElementsSize()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.pixelUnpackBufferAllocate(
      10,
      GLScalarType.TYPE_UNSIGNED_BYTE,
      0,
      UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferDeleteDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    PixelUnpackBuffer a = null;

    try {
      a =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          2,
          UsageHint.USAGE_STATIC_DRAW);
      gl.pixelUnpackBufferDelete(a);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.pixelUnpackBufferDelete(a);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferDeleteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.pixelUnpackBufferDelete(null);
  }

  /**
   * Mapping a buffer works.
   */

  @Test public void testPixelUnpackBufferMap()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final PixelUnpackBuffer a =
      gl.pixelUnpackBufferAllocate(
        10,
        GLScalarType.TYPE_UNSIGNED_BYTE,
        2,
        UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final PixelUnpackBufferWritableMap m =
          gl.pixelUnpackBufferMapWrite(a);
        Assert.assertEquals(a, m.getPixelUnpackBuffer());

        final ByteBuffer b = m.getByteBuffer();
        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          s.put(index, (short) index);
        }
      } finally {
        gl.pixelUnpackBufferUnmap(a);
      }

      try {
        final ByteBuffer b = gl.pixelUnpackBufferMapRead(a);

        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, s.get(index));
        }
      } finally {
        gl.pixelUnpackBufferUnmap(a);
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

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testPixelUnpackBufferMapReadBounds()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final PixelUnpackBuffer a =
      gl.pixelUnpackBufferAllocate(
        10,
        GLScalarType.TYPE_UNSIGNED_BYTE,
        2,
        UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ByteBuffer b = gl.pixelUnpackBufferMapRead(a);
        b.get(20);
      } finally {
        gl.pixelUnpackBufferUnmap(a);
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

  @Test(expected = GLException.class) public
    void
    testPixelUnpackBufferMapReadDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    PixelUnpackBuffer a = null;

    try {
      a =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          2,
          UsageHint.USAGE_STATIC_DRAW);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.pixelUnpackBufferMapRead(a);
      gl.pixelUnpackBufferMapRead(a);
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

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapReadNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.pixelUnpackBufferAllocate(
      10,
      GLScalarType.TYPE_UNSIGNED_BYTE,
      1,
      UsageHint.USAGE_STATIC_DRAW);
    gl.pixelUnpackBufferMapRead(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testPixelUnpackBufferMapWriteBounds()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final PixelUnpackBuffer a =
      gl.pixelUnpackBufferAllocate(
        10,
        GLScalarType.TYPE_UNSIGNED_BYTE,
        2,
        UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ByteBuffer b = gl.pixelUnpackBufferMapWrite(a).getByteBuffer();
        b.put(20, (byte) 0xff);
      } finally {
        gl.pixelUnpackBufferUnmap(a);
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

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapWriteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.pixelUnpackBufferAllocate(
      10,
      GLScalarType.TYPE_UNSIGNED_BYTE,
      1,
      UsageHint.USAGE_STATIC_DRAW);
    gl.pixelUnpackBufferMapWrite(null);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public
    void
    testPixelUnpackBufferUnmapDouble()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    PixelUnpackBuffer a = null;

    try {
      a =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          2,
          UsageHint.USAGE_STATIC_DRAW);
      gl.pixelUnpackBufferMapWrite(a);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.pixelUnpackBufferUnmap(a);
      gl.pixelUnpackBufferUnmap(a);
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
