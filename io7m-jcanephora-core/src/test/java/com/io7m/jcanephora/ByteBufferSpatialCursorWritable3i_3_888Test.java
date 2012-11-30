package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferSpatialCursorWritable3i_3_888Test
{
  private static int BYTES_PER_PIXEL = 3;

  @SuppressWarnings("static-method") @Test public void testRange()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 3));

    final ByteBuffer buffer =
      ByteBuffer
        .allocate(
          4 * 4 * ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferSpatialCursorWritable3i_3_888 c =
      new ByteBufferSpatialCursorWritable3i_3_888(
        buffer,
        area_outer,
        area_outer);

    int index = 0;
    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        Assert.assertTrue(c.canWrite());
        c.put3i(0x50, 0x40, 0x30);

        Assert.assertEquals(0x50, buffer.get(index + 0));
        Assert.assertEquals(0x40, buffer.get(index + 1));
        Assert.assertEquals(0x30, buffer.get(index + 2));

        index += ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL;
      }
    }

    Assert.assertFalse(c.canWrite());
    Assert.assertEquals(0, buffer.position());
  }

  @SuppressWarnings("static-method") @Test public void testSubRange()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 11), new RangeInclusive(0, 11));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusive(4, 7), new RangeInclusive(4, 7));

    final ByteBuffer buffer =
      ByteBuffer
        .allocate(
          12 * 12 * ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferSpatialCursorWritable3i_3_888 c =
      new ByteBufferSpatialCursorWritable3i_3_888(
        buffer,
        area_outer,
        area_inner);
    final long width = area_outer.getRangeX().getInterval();

    for (int y = 4; y <= 7; ++y) {
      for (int x = 4; x <= 7; ++x) {
        Assert.assertTrue(c.canWrite());
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert
          .assertEquals(
            (y * width * ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL)
              + (x * ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL),
            c.getByteOffset());

        c.put3i(0x50, 0x40, 0x30);
      }
    }

    Assert.assertFalse(c.canWrite());
    Assert.assertEquals(0, buffer.position());

    int index = 0;
    for (int y = 0; y <= 11; ++y) {
      for (int x = 0; x <= 11; ++x) {

        if ((y >= 4) && (y <= 7) && (x >= 4) && (x <= 7)) {
          Assert.assertEquals(0x50, buffer.get(index + 0));
          Assert.assertEquals(0x40, buffer.get(index + 1));
          Assert.assertEquals(0x30, buffer.get(index + 2));
        } else {
          Assert.assertEquals(0x0, buffer.get(index + 0));
          Assert.assertEquals(0x0, buffer.get(index + 1));
          Assert.assertEquals(0x0, buffer.get(index + 2));
        }

        index += ByteBufferSpatialCursorWritable3i_3_888Test.BYTES_PER_PIXEL;
      }
    }
  }
}
