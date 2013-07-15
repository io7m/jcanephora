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

public class ByteBufferSpatialCursorWritable1i_1_24Test
{
  /**
   * Yes, four bytes per pixel for 24 bit integers.
   */

  private static int BYTES_PER_PIXEL = 4;

  @SuppressWarnings("static-method") @Test public void testRange()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 3));

    final ByteBuffer buffer =
      ByteBuffer
        .allocate(
          4 * 4 * ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable1i_1_24 c =
      new ByteBufferTextureCursorWritable1i_1_24(
        buffer,
        area_outer,
        area_outer);

    int index = 0;
    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        Assert.assertTrue(c.canWrite());
        c.put1i(0x50);

        Assert.assertEquals(0x50, buffer.get(index + 0));

        index += ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL;
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
          12 * 12 * ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());
    final ByteBufferTextureCursorWritable1i_1_24 c =
      new ByteBufferTextureCursorWritable1i_1_24(
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
            (y * width * ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL)
              + (x * ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL),
            c.getByteOffset());

        c.put1i(0x50);
      }
    }

    Assert.assertFalse(c.canWrite());
    Assert.assertEquals(0, buffer.position());

    int index = 0;
    for (int y = 0; y <= 11; ++y) {
      for (int x = 0; x <= 11; ++x) {

        if ((y >= 4) && (y <= 7) && (x >= 4) && (x <= 7)) {
          Assert.assertEquals(0x50, buffer.get(index + 0));
        } else {
          Assert.assertEquals(0x0, buffer.get(index + 0));
        }

        index += ByteBufferSpatialCursorWritable1i_1_24Test.BYTES_PER_PIXEL;
      }
    }
  }
}
