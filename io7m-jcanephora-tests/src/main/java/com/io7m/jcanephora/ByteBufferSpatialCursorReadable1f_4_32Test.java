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
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferSpatialCursorReadable1f_4_32Test
{
  private static int BYTES_PER_PIXEL = 4;

  @SuppressWarnings("static-method") @Test public void testRange()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 3));

    final ByteBuffer buffer =
      ByteBuffer
        .allocate(
          4 * 4 * ByteBufferSpatialCursorReadable1f_4_32Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());

    final FloatBuffer fb = buffer.asFloatBuffer();
    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        final int index = (y * 4) + x;
        fb.put(index, (y * 1000.f) + x);
      }
    }

    for (int index = 0; index < 16; ++index) {
      System.out.println(fb.get(index));
    }

    final ByteBufferTextureCursorReadable1f_4_32 c =
      new ByteBufferTextureCursorReadable1f_4_32(
        buffer,
        area_outer,
        area_outer);

    for (int y = 0; y <= 3; ++y) {
      for (int x = 0; x <= 3; ++x) {
        Assert.assertTrue(c.isValid());
        final float expected = (y * 1000.f) + x;
        final float got = c.get1f();
        Assert.assertEquals(expected, got, 0.0f);
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
          12 * 12 * ByteBufferSpatialCursorReadable1f_4_32Test.BYTES_PER_PIXEL)
        .order(ByteOrder.nativeOrder());

    final FloatBuffer fb = buffer.asFloatBuffer();
    for (int y = 0; y < 12; ++y) {
      for (int x = 0; x < 12; ++x) {
        final int index = (y * 12) + x;
        fb.put(index, (y * 1000.f) + x);
      }
    }

    for (int index = 0; index < (12 * 12); ++index) {
      System.out.println(fb.get(index));
    }

    final ByteBufferTextureCursorReadable1f_4_32 c =
      new ByteBufferTextureCursorReadable1f_4_32(
        buffer,
        area_outer,
        area_inner);

    for (int y = 4; y <= 7; ++y) {
      for (int x = 4; x <= 7; ++x) {
        Assert.assertTrue(c.isValid());

        Assert.assertTrue(c.isValid());
        final float expected = (y * 1000.f) + x;
        final float got = c.get1f();
        Assert.assertEquals(expected, got, 0.0f);
      }
    }

    Assert.assertFalse(c.isValid());
    Assert.assertEquals(0, buffer.position());
  }
}