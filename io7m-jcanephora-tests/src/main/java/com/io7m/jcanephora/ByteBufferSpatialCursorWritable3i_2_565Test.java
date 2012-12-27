package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferSpatialCursorWritable3i_2_565Test
{
  private static int BYTES_PER_PIXEL = 2;

  @SuppressWarnings("static-method") @Test public void testRange()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 3));

    final ByteBuffer buffer =
      ByteBuffer
        .allocate(
          4 * 4 * ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable3i_2_565 c =
      new ByteBufferTextureCursorWritable3i_2_565(
        buffer,
        area_outer,
        area_outer);

    int index = 0;
    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        Assert.assertTrue(c.canWrite());
        c.put3i(0xFF - x, 0xFF - y, 0);

        final char exp = TexturePixelPack.pack2_565(0xFF, 0xFF, 0);
        final char got = buffer.getChar(index);
        Assert.assertEquals(exp, got);

        index += ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL;
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
          12 * 12 * ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable3i_2_565 c =
      new ByteBufferTextureCursorWritable3i_2_565(
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
            (y * width * ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL)
              + (x * ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL),
            c.getByteOffset());
        c.put3i(0xFF, 0xFF, 0);
      }
    }

    Assert.assertFalse(c.canWrite());
    Assert.assertEquals(0, buffer.position());

    int index = 0;
    for (int y = 0; y <= 11; ++y) {
      for (int x = 0; x <= 11; ++x) {

        final int got = buffer.getChar(index);
        int exp = 0;
        if ((y >= 4) && (y <= 7)) {
          if ((x >= 4) && (x <= 7)) {
            exp = TexturePixelPack.pack2_565(0xFF, 0xFF, 0);
          }
        }

        Assert.assertEquals(exp, got);
        index += ByteBufferSpatialCursorWritable3i_2_565Test.BYTES_PER_PIXEL;
      }
    }
  }
}
