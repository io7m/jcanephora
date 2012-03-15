package com.io7m.jcanephora;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jlog.Log;

public class IndexBufferMapLWJGL30Test
{
  private static GLInterface getGL()
    throws IOException,
      ConstraintError
  {
    final Properties properties =
      PropertyUtils.loadFromFile("tests.properties");
    final Log log = new Log(properties, "com.io7m", "example");
    return new GLInterfaceLWJGL30(log);
  }

  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("IndexBufferMap", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
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
    final GLInterface gl = IndexBufferMapLWJGL30Test.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.allocateArrayBuffer(255, d);
    final IndexBuffer ib = gl.allocateIndexBuffer(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE);

    {
      final IndexBufferWritableMap map = gl.mapIndexBufferWrite(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.unmapIndexBuffer(ib);

    {
      final IndexBufferReadableMap map = gl.mapIndexBufferRead(ib);
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
    final GLInterface gl = IndexBufferMapLWJGL30Test.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.allocateArrayBuffer(70000, d);
    final IndexBuffer ib = gl.allocateIndexBuffer(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_INT);

    {
      final IndexBufferWritableMap map = gl.mapIndexBufferWrite(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.unmapIndexBuffer(ib);

    {
      final IndexBufferReadableMap map = gl.mapIndexBufferRead(ib);
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
    final GLInterface gl = IndexBufferMapLWJGL30Test.getGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.allocateArrayBuffer(65535, d);
    final IndexBuffer ib = gl.allocateIndexBuffer(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_SHORT);

    {
      final IndexBufferWritableMap map = gl.mapIndexBufferWrite(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        map.put(index, index);
      }
    }

    gl.unmapIndexBuffer(ib);

    {
      final IndexBufferReadableMap map = gl.mapIndexBufferRead(ib);
      for (int index = 0; index < ib.getElements(); ++index) {
        final int value = map.get(index);
        Assert.assertEquals(index, value);
      }
    }
  }
}
