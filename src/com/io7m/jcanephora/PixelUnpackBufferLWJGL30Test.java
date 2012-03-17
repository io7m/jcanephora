package com.io7m.jcanephora;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints.ConstraintError;

public class PixelUnpackBufferLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("PixelUnpackBuffer", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
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
        gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);
      Assert.assertEquals(2, a.getElementSizeBytes());
      Assert.assertEquals(10, a.getElements());
      Assert.assertEquals(20, a.getSizeBytes());
      Assert.assertEquals(2, a.getElementValues());
      Assert
        .assertEquals(GLScalarType.TYPE_UNSIGNED_BYTE, a.getElementType());
    } finally {
      if (a != null) {
        a.delete(gl);
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
    gl.allocatePixelUnpackBuffer(0, GLScalarType.TYPE_UNSIGNED_BYTE, 2);
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
    gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 0);
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
        gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);
      gl.deletePixelUnpackBuffer(a);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.deletePixelUnpackBuffer(a);
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
    gl.deletePixelUnpackBuffer(null);
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
      gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);

    try {
      try {
        final PixelUnpackBufferWritableMap m =
          gl.mapPixelUnpackBufferWrite(a);
        Assert.assertEquals(a, m.getPixelUnpackBuffer());

        final ByteBuffer b = m.getByteBuffer();
        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          s.put(index, (short) index);
        }
      } finally {
        gl.unmapPixelUnpackBuffer(a);
      }

      try {
        final ByteBuffer b = gl.mapPixelUnpackBufferRead(a);

        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, s.get(index));
        }
      } finally {
        gl.unmapPixelUnpackBuffer(a);
      }
    } finally {
      if (a != null) {
        a.delete(gl);
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
      gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);

    try {
      try {
        final ByteBuffer b = gl.mapPixelUnpackBufferRead(a);
        b.get(20);
      } finally {
        gl.unmapPixelUnpackBuffer(a);
      }
    } finally {
      if (a != null) {
        a.delete(gl);
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
        gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.mapPixelUnpackBufferRead(a);
      gl.mapPixelUnpackBufferRead(a);
    } catch (final GLException e) {
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
      throw e;
    } finally {
      if (a != null) {
        a.delete(gl);
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
    gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 1);
    gl.mapPixelUnpackBufferRead(null);
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
      gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);

    try {
      try {
        final ByteBuffer b = gl.mapPixelUnpackBufferWrite(a).getByteBuffer();
        b.put(20, (byte) 0xff);
      } finally {
        gl.unmapPixelUnpackBuffer(a);
      }
    } finally {
      if (a != null) {
        a.delete(gl);
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
    gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 1);
    gl.mapPixelUnpackBufferWrite(null);
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
        gl.allocatePixelUnpackBuffer(10, GLScalarType.TYPE_UNSIGNED_BYTE, 2);
      gl.mapPixelUnpackBufferWrite(a);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.unmapPixelUnpackBuffer(a);
      gl.unmapPixelUnpackBuffer(a);
    } catch (final GLException e) {
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
      throw e;
    } finally {
      if (a != null) {
        a.delete(gl);
      }
    }
  }
}
