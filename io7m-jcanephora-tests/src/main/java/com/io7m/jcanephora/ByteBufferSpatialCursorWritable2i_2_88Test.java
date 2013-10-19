/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferSpatialCursorWritable2i_2_88Test
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
          4 * 4 * ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable2i_2_88 c =
      new ByteBufferTextureCursorWritable2i_2_88(
        buffer,
        area_outer,
        area_outer);

    int index = 0;
    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        Assert.assertTrue(c.isValid());
        c.put2i(0x50, 0x40);

        Assert.assertEquals(0x50, buffer.get(index + 0));
        Assert.assertEquals(0x40, buffer.get(index + 1));

        index += ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL;
      }
    }

    Assert.assertFalse(c.isValid());
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
          12 * 12 * ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable2i_2_88 c =
      new ByteBufferTextureCursorWritable2i_2_88(
        buffer,
        area_outer,
        area_inner);
    final long width = area_outer.getRangeX().getInterval();

    for (int y = 4; y <= 7; ++y) {
      for (int x = 4; x <= 7; ++x) {
        Assert.assertTrue(c.isValid());
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert
          .assertEquals(
            (y * width * ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL)
              + (x * ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL),
            c.getByteOffset());

        c.put2i(0x50, 0x40);
      }
    }

    Assert.assertFalse(c.isValid());
    Assert.assertEquals(0, buffer.position());

    int index = 0;
    for (int y = 0; y <= 11; ++y) {
      for (int x = 0; x <= 11; ++x) {

        if ((y >= 4) && (y <= 7) && (x >= 4) && (x <= 7)) {
          Assert.assertEquals(0x50, buffer.get(index + 0));
          Assert.assertEquals(0x40, buffer.get(index + 1));
        } else {
          Assert.assertEquals(0x0, buffer.get(index + 0));
          Assert.assertEquals(0x0, buffer.get(index + 1));
        }

        index += ByteBufferSpatialCursorWritable2i_2_88Test.BYTES_PER_PIXEL;
      }
    }
  }
}
