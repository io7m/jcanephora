package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.PixelUnpackBuffer;
import com.io7m.jcanephora.PixelUnpackBufferCursorWritable4b;
import com.io7m.jcanephora.PixelUnpackBufferWritableMap;
import com.io7m.jcanephora.UsageHint;

public abstract class PixelUnpackBufferMapContract implements GLTestContract
{
  /**
   * Retrieving a cursor of the correct type works.
   */

  @Test public final void testPixelUnpackBufferMapCursor4bByteTypeCorrect0()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  /**
   * Retrieving a cursor of the correct type works.
   */

  @Test public final void testPixelUnpackBufferMapCursor4bByteTypeCorrect1()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  /**
   * Writing with a 4b cursor works.
   */

  @Test public final void testPixelUnpackBufferMapCursor4bCorrect()
    throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          3,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
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

  /**
   * Seeking before the start of a map fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPixelUnpackBufferMapCursor4bSeekLowerBad()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
      mc = pm.getCursor4b();
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert mc != null;
    mc.seekTo(-1);
  }

  /**
   * Seeking past the end of a map fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPixelUnpackBufferMapCursor4bSeekUpperBad()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;
    PixelUnpackBufferCursorWritable4b mc = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_UNSIGNED_BYTE,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
      mc = pm.getCursor4b();
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert mc != null;
    mc.seekTo(10);
  }

  /**
   * Retrieving a cursor of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPixelUnpackBufferMapCursor4bWrongSize()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_BYTE,
          3,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }

  /**
   * Retrieving a cursor of the wrong type fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testPixelUnpackBufferMapCursor4bWrongType()
      throws ConstraintError
  {
    GLInterface gl = null;
    PixelUnpackBuffer pb = null;
    PixelUnpackBufferWritableMap pm = null;

    try {
      gl = this.makeNewGL();
      pb =
        gl.pixelUnpackBufferAllocate(
          10,
          GLScalarType.TYPE_INT,
          4,
          UsageHint.USAGE_STATIC_DRAW);
      pm = gl.pixelUnpackBufferMapWrite(pb);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert pm != null;
    pm.getCursor4b();
  }
}
