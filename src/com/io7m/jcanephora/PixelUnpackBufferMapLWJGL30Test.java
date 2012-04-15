package com.io7m.jcanephora;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class PixelUnpackBufferMapLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("PixelUnpackBufferMapTest", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void testPixelUnpackBufferMapCursor4bByteTypeCorrect0()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  @Test public void testPixelUnpackBufferMapCursor4bByteTypeCorrect1()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  @Test public void testPixelUnpackBufferMapCursor4bCorrect()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          3,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
      mc = pm.getCursor4b();
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert mc != null;

    Assert.assertTrue(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);
    Assert.assertTrue(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);
    Assert.assertFalse(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);

    mc.seekTo(0);

    Assert.assertTrue(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);
    Assert.assertTrue(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);
    Assert.assertFalse(mc.hasNext());
    mc.put4b((byte) 0x10, (byte) 0x20, (byte) 0x30, (byte) 0x40);
  }

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapCursor4bSeekLowerBad()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
      mc = pm.getCursor4b();
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert mc != null;
    mc.seekTo(-1);
  }

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapCursor4bSeekUpperBad()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
      mc = pm.getCursor4b();
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert mc != null;
    mc.seekTo(10);
  }

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapCursor4bWrongSize()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_BYTE,
          3,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  @Test(expected = ConstraintError.class) public
    void
    testPixelUnpackBufferMapCursor4bWrongType()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      pb =
        gl.allocatePixelUnpackBuffer(
          10,
          GLScalarType.TYPE_INT,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.mapPixelUnpackBufferWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }
}
