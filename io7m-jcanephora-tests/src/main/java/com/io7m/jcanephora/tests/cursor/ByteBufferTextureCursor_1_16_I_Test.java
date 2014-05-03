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

package com.io7m.jcanephora.tests.cursor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.SpatialCursorReadable1Type;
import com.io7m.jcanephora.SpatialCursorWritable1Type;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_16_I;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings({ "boxing", "null", "static-method" }) public final class ByteBufferTextureCursor_1_16_I_Test
{
  @Test public void testReadWriteFL()
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();
    final SpatialCursorReadable1Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable1Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        w.seekTo(x, y);
        w.put1l(0x7fff);
        r.seekTo(x, y);
        Assert.assertEquals(1.0, r.get1d(), this.EPSILON);
        r.seekTo(x, y);
        Assert.assertEquals(0x7fff, r.get1l());

        w.seekTo(x, y);
        w.put1l(-0x7fff);
        r.seekTo(x, y);
        Assert.assertEquals(-1.0, r.get1d(), this.EPSILON);
        r.seekTo(x, y);
        Assert.assertEquals(-0x7fff, r.get1l());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  private final int    ELEMENT_COMPONENT_BYTES = 2;
  private final int    ELEMENT_COUNT           = 1;
  private final double EPSILON                 = 0.0001f;
  private final int    HEIGHT                  = 4;
  private final int    WIDTH                   = 4;

  private void dumpBuffer(
    final ByteBuffer b)
  {
    final int element_bytes =
      this.ELEMENT_COUNT * this.ELEMENT_COMPONENT_BYTES;

    final int row_bytes = this.WIDTH * element_bytes;
    final int all_bytes = this.HEIGHT * row_bytes;

    for (int y = 0; y < all_bytes; y += row_bytes) {
      for (int x = 0; x < row_bytes; x += element_bytes) {
        final int i = y + x;
        final int bx = b.getChar(i + 0);
        System.out.print(String.format("[%04x]", bx));
      }
      System.out.println();
    }
  }

  private AreaInclusive getArea()
  {
    return new AreaInclusive(
      new RangeInclusiveL(0, this.WIDTH - 1),
      new RangeInclusiveL(0, this.HEIGHT - 1));
  }

  private AreaInclusive getAreaSmallerCentered()
  {
    final int lo_x = 1;
    final int hi_x = 2;
    final int lo_y = 1;
    final int hi_y = 2;
    return new AreaInclusive(
      new RangeInclusiveL(lo_x, hi_x),
      new RangeInclusiveL(lo_y, hi_y));
  }

  private ByteBuffer getNewAreaBuffer()
  {
    return ByteBuffer.allocate(this.HEIGHT * (this.WIDTH * (4 * 4)));
  }

  private SpatialCursorReadable1Type getReadableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorReadable_1_16_I.newCursor(
      buffer,
      area,
      area);
  }

  private SpatialCursorWritable1Type getWritableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorWritable_1_16_I.newCursor(
      buffer,
      area,
      area);
  }

  private SpatialCursorWritable1Type getWritableCursorSub(
    final ByteBuffer buffer,
    final AreaInclusive area_whole,
    final AreaInclusive area_update)
  {
    return ByteBufferTextureCursorWritable_1_16_I.newCursor(
      buffer,
      area_whole,
      area_update);
  }

  @Test(expected = RangeCheckException.class) public void testTooSmallW_0()
  {
    final ByteBuffer b = ByteBuffer.allocate(1);
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 0), new RangeInclusiveL(0, 0));
    this.getWritableCursor(b, area);
  }

  @Test(expected = RangeCheckException.class) public void testTooSmallR_0()
  {
    final ByteBuffer b = ByteBuffer.allocate(1);
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 0), new RangeInclusiveL(0, 0));
    this.getReadableCursor(b, area);
  }

  @Test public void testReadWriteD()
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final SpatialCursorReadable1Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable1Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        {
          final float k = r.get1f();
          Assert.assertEquals(0.0f, k, 0.0f);
        }

        w.put1d(-1.0);

        {
          r.seekTo(x, y);
          final float k = r.get1f();
          Assert.assertEquals(-1.0f, k, 0.0f);
        }

        {
          r.seekTo(x, y);
          final double k = r.get1d();
          Assert.assertEquals(-1.0, k, 0.0f);
        }

        {
          r.seekTo(x, y);
          final int k = r.get1i();
          Assert.assertEquals(-32767, k);
        }

        {
          r.seekTo(x, y);
          final long k = r.get1l();
          Assert.assertEquals(-32767, k);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteF()
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final SpatialCursorReadable1Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable1Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        {
          final float k = r.get1f();
          Assert.assertEquals(0.0f, k, 0.0f);
        }

        w.put1f(-1.0f);

        {
          r.seekTo(x, y);
          final float k = r.get1f();
          Assert.assertEquals(-1.0f, k, 0.0f);
        }

        {
          r.seekTo(x, y);
          final double k = r.get1d();
          Assert.assertEquals(-1.0, k, 0.0f);
        }

        {
          r.seekTo(x, y);
          final int k = r.get1i();
          Assert.assertEquals(-32767, k);
        }

        {
          r.seekTo(x, y);
          final long k = r.get1l();
          Assert.assertEquals(-32767, k);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteFSub()
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a_all = this.getArea();
    final AreaInclusive a_up = this.getAreaSmallerCentered();

    final SpatialCursorWritable1Type w =
      this.getWritableCursorSub(b, a_all, a_up);

    final RangeInclusiveL aury = a_up.getRangeY();
    final RangeInclusiveL aurx = a_up.getRangeX();
    for (long y = aury.getLower(); y <= aury.getUpper(); ++y) {
      for (long x = aurx.getLower(); x <= aurx.getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());
        w.put1f(-1.0f);
      }
    }

    this.dumpBuffer(b);

    final SpatialCursorReadable1Type r = this.getReadableCursor(b, a_all);

    for (int y = 0; y <= a_all.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a_all.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());

        boolean in_center = true;
        in_center = in_center && (x >= aurx.getLower());
        in_center = in_center && (x <= aurx.getUpper());
        in_center = in_center && (y >= aury.getLower());
        in_center = in_center && (y <= aury.getUpper());

        final float k = r.get1f();
        if (in_center) {
          Assert.assertEquals(-1.0f, k, this.EPSILON);
        } else {
          Assert.assertEquals(0.0f, k, this.EPSILON);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }
}
