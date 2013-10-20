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
import com.io7m.jtensors.VectorM4I;

public class ByteBufferSpatialCursorReadable4i_2_4444Test
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
          4 * 4 * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());

    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        final int index =
          (y * 4 * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL)
            + (x * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL);

        final int q0 = (y * 50) + x;
        final int q1 = (y * 51) + x;
        final int q2 = (y * 52) + x;
        final int q3 = (y * 53) + x;
        final char k = TexturePixelPack.pack2_4444(q0, q1, q2, q3);

        buffer.putChar(index, k);
      }
    }

    for (int index = 0; index < 16; ++index) {
      System.out.println((int) buffer.getChar(index));
    }

    final ByteBufferTextureCursorReadable4i_2_4444 c =
      new ByteBufferTextureCursorReadable4i_2_4444(
        buffer,
        area_outer,
        area_outer);

    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        final int q0 = (y * 50) + x;
        final int q1 = (y * 51) + x;
        final int q2 = (y * 52) + x;
        final int q3 = (y * 53) + x;
        final char k = TexturePixelPack.pack2_4444(q0, q1, q2, q3);

        final int[] u = new int[4];
        TexturePixelPack.unpack2_4444(k, u);

        Assert.assertTrue(c.isValid());
        final VectorM4I e = new VectorM4I();
        e.x = u[0];
        e.y = u[1];
        e.z = u[2];
        e.w = u[3];

        final VectorM4I v = new VectorM4I();
        c.get4i(v);
        Assert.assertEquals(e, v);
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
          12 * 12 * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());

    for (int y = 0; y < 12; ++y) {
      for (int x = 0; x < 12; ++x) {
        final int index =
          (y * 12 * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL)
            + (x * ByteBufferSpatialCursorReadable4i_2_4444Test.BYTES_PER_PIXEL);

        final int q0 = (y * 50) + x;
        final int q1 = (y * 51) + x;
        final int q2 = (y * 52) + x;
        final int q3 = (y * 53) + x;
        final char k = TexturePixelPack.pack2_4444(q0, q1, q2, q3);

        buffer.putChar(index, k);
      }
    }

    for (int index = 0; index < (12 * 12); ++index) {
      System.out.println((int) buffer.getChar(index));
    }

    final ByteBufferTextureCursorReadable4i_2_4444 c =
      new ByteBufferTextureCursorReadable4i_2_4444(
        buffer,
        area_outer,
        area_inner);

    for (int y = 4; y <= 7; ++y) {
      for (int x = 4; x <= 7; ++x) {
        final int q0 = (y * 50) + x;
        final int q1 = (y * 51) + x;
        final int q2 = (y * 52) + x;
        final int q3 = (y * 53) + x;
        final char k = TexturePixelPack.pack2_4444(q0, q1, q2, q3);

        final int[] u = new int[4];
        TexturePixelPack.unpack2_4444(k, u);

        Assert.assertTrue(c.isValid());
        final VectorM4I e = new VectorM4I();
        e.x = u[0];
        e.y = u[1];
        e.z = u[2];
        e.w = u[3];

        final VectorM4I v = new VectorM4I();
        c.get4i(v);
        Assert.assertEquals(e, v);
      }
    }

    Assert.assertFalse(c.isValid());
    Assert.assertEquals(0, buffer.position());
  }
}
