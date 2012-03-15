package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints.ConstraintError;

public class IndexBufferLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("IndexBuffer", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public void testIndexBufferAllocateByte()
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
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.allocateArrayBuffer(255, d);
      i = gl.allocateIndexBuffer(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_BYTE, i.getType());
      Assert.assertEquals(1, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getElements());
      Assert.assertEquals(4, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public void testIndexBufferAllocateInt()
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
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.allocateArrayBuffer(65536, d);
      i = gl.allocateIndexBuffer(a, 2);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_INT, i.getType());
      Assert.assertEquals(4, i.getElementSizeBytes());
      Assert.assertEquals(2, i.getElements());
      Assert.assertEquals(8, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * Passing null to allocateIndexBuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferAllocateNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.allocateIndexBuffer(null, 1);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public void testIndexBufferAllocateShort()
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
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.allocateArrayBuffer(256, d);
      i = gl.allocateIndexBuffer(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_SHORT, i.getType());
      Assert.assertEquals(2, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getElements());
      Assert.assertEquals(8, i.getSizeBytes());
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferAllocateZeroElements()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(1, d);
    gl.allocateIndexBuffer(a, 0);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferDeleteDouble()
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
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = gl.allocateArrayBuffer(4, d);
      i = gl.allocateIndexBuffer(a, 10);
      gl.deleteIndexBuffer(i);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.deleteIndexBuffer(i);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferDeleteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.deleteIndexBuffer(null);
  }

  /**
   * Mapping a buffer works.
   */

  @Test public void testIndexBufferMap()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 10);

    try {
      try {
        final IndexBufferWritableMap b = gl.mapIndexBufferWrite(i);
        for (int index = 0; index < 10; ++index) {
          b.put(index, index);
        }
      } finally {
        gl.unmapIndexBuffer(i);
      }

      try {
        final IndexBufferReadableMap b = gl.mapIndexBufferRead(i);
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, b.get(index));
        }
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapReadBoundsByte()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 10);

    try {
      try {
        final IndexBufferReadableMap b = gl.mapIndexBufferRead(i);
        b.get(10);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapReadBoundsInt()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 65536);

    try {
      try {
        final IndexBufferReadableMap b = gl.mapIndexBufferRead(i);
        b.get(65536);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapReadBoundsShort()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 256);

    try {
      try {
        final IndexBufferReadableMap b = gl.mapIndexBufferRead(i);
        b.get(256);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * Mapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public
    void
    testIndexBufferMapReadDouble()
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
    IndexBuffer i = null;

    try {
      a = gl.allocateArrayBuffer(4, d);
      i = gl.allocateIndexBuffer(a, 10);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.mapIndexBufferRead(i);
      gl.mapIndexBufferRead(i);
    } catch (final GLException e) {
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
      throw e;
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferMapReadNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.mapIndexBufferRead(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapWriteBoundsByte()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 10);

    try {
      try {
        final IndexBufferWritableMap b = gl.mapIndexBufferWrite(i);
        b.put(10, 0xff);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapWriteBoundsInt()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 65536);

    try {
      try {
        final IndexBufferWritableMap b = gl.mapIndexBufferWrite(i);
        b.put(65536, 0xffffffff);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public
    void
    testIndexBufferMapWriteBoundsShort()
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
    final ArrayBuffer a = gl.allocateArrayBuffer(4, d);
    final IndexBuffer i = gl.allocateIndexBuffer(a, 256);

    try {
      try {
        final IndexBufferWritableMap b = gl.mapIndexBufferWrite(i);
        b.put(256, 0xffff);
      } finally {
        gl.unmapIndexBuffer(i);
      }
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testIndexBufferMapWriteNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.mapIndexBufferWrite(null);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public
    void
    testIndexBufferUnmapDouble()
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
    IndexBuffer i = null;

    try {
      a = gl.allocateArrayBuffer(4, d);
      i = gl.allocateIndexBuffer(a, 10);
      gl.mapIndexBufferWrite(i);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.unmapIndexBuffer(i);
      gl.unmapIndexBuffer(i);
    } catch (final GLException e) {
      Assert.assertEquals(GL11.GL_INVALID_OPERATION, e.getCode());
      throw e;
    } finally {
      if (i != null) {
        i.delete(gl);
      }
    }
  }
}
