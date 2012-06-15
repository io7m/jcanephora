package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;

public class IndexBufferMapLWJGL30Test
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
   * An index buffer of less than 256 elements has type 'unsigned byte' and
   * reading/writing is correct.
   */

  @Test public void testReadWriteByte()
    throws GLException,
      ConstraintError,
      IOException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(255, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());

      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);

      for (int index = 0; index < ib.getElements(); ++index) {
        final int value = map.get(index);
        Assert.assertEquals(index, value);
      }
    }
  }

  /**
   * An index buffer of less than 4294967296 elements has type 'unsigned
   * short' and reading/writing is correct.
   */

  @Test public void testReadWriteInt()
    throws GLException,
      ConstraintError,
      IOException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(70000, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_INT);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        final int value = map.get(index);
        Assert.assertEquals(index, value);
      }
    }
  }

  /**
   * An index buffer of less than 65536 elements has type 'unsigned short' and
   * reading/writing is correct.
   */

  @Test public void testReadWriteShort()
    throws GLException,
      ConstraintError,
      IOException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(65535, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_SHORT);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        final int value = map.get(index);
        Assert.assertEquals(index, value);
      }
    }
  }
}
