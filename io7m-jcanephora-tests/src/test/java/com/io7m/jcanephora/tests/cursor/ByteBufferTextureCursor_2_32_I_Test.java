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
import com.io7m.jcanephora.SpatialCursorReadable2Type;
import com.io7m.jcanephora.SpatialCursorWritable2Type;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_32_I;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.VectorI4D;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jtensors.VectorI4L;
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM2L;

@SuppressWarnings({ "static-method", "boxing", "null" }) public final class ByteBufferTextureCursor_2_32_I_Test
{
  private final int    ELEMENT_COMPONENT_BYTES = 4;
  private final int    ELEMENT_COUNT           = 3;
  private final double EPSILON                 = 0.000000001;
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
        final int bx = b.getInt(i + 0);
        final int by = b.getInt(i + 4);
        final int bz = b.getInt(i + 8);
        System.out.print(String.format("[%08x %08x %08x]", bx, by, bz));
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

  private SpatialCursorReadable2Type getReadableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorReadable_2_32_I.newCursor(
      buffer,
      area,
      area);
  }

  private SpatialCursorWritable2Type getWritableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorWritable_2_32_I.newCursor(
      buffer,
      area,
      area);
  }

  private SpatialCursorWritable2Type getWritableCursorSub(
    final ByteBuffer buffer,
    final AreaInclusive area_whole,
    final AreaInclusive area_update)
  {
    return ByteBufferTextureCursorWritable_2_32_I.newCursor(
      buffer,
      area_whole,
      area_update);
  }

  @Test public void testReadWriteD()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM2F read_f = new VectorM2F();
    final VectorM2D read_d = new VectorM2D();
    final VectorM2L read_l = new VectorM2L();
    final VectorM2I read_i = new VectorM2I();

    final SpatialCursorReadable2Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable2Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get2f(read_f);
        Assert.assertEquals(0.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getYF(), 0.0f);

        w.put2d(new VectorI4D(-1.0, 1.0, -0.5, 0.5));

        r.seekTo(x, y);
        r.get2f(read_f);
        Assert.assertEquals(-1.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(1.0f, read_f.getYF(), 0.0f);

        r.seekTo(x, y);
        r.get2d(read_d);
        Assert.assertEquals(-1.0, read_d.getXD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getYD(), this.EPSILON);

        r.seekTo(x, y);
        r.get2i(read_i);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_i.getXI());
        Assert.assertEquals(Integer.MAX_VALUE, read_i.getYI());

        r.seekTo(x, y);
        r.get2l(read_l);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_l.getXL());
        Assert.assertEquals(Integer.MAX_VALUE, read_l.getYL());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteF()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM2F read_f = new VectorM2F();
    final VectorM2D read_d = new VectorM2D();
    final VectorM2L read_l = new VectorM2L();
    final VectorM2I read_i = new VectorM2I();

    final SpatialCursorReadable2Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable2Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get2f(read_f);
        Assert.assertEquals(0.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getYF(), 0.0f);

        w.put2f(new VectorI4F(-1.0f, 1.0f, -0.5f, 0.5f));

        r.seekTo(x, y);
        r.get2f(read_f);
        Assert.assertEquals(-1.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(1.0f, read_f.getYF(), 0.0f);

        r.seekTo(x, y);
        r.get2d(read_d);
        Assert.assertEquals(-1.0, read_d.getXD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getYD(), this.EPSILON);

        r.seekTo(x, y);
        r.get2i(read_i);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_i.getXI());
        Assert.assertEquals(Integer.MAX_VALUE, read_i.getYI());

        r.seekTo(x, y);
        r.get2l(read_l);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_l.getXL());
        Assert.assertEquals(Integer.MAX_VALUE, read_l.getYL());
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

    final SpatialCursorWritable2Type w =
      this.getWritableCursorSub(b, a_all, a_up);

    final RangeInclusiveL aury = a_up.getRangeY();
    final RangeInclusiveL aurx = a_up.getRangeX();
    for (long y = aury.getLower(); y <= aury.getUpper(); ++y) {
      for (long x = aurx.getLower(); x <= aurx.getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());
        w.put2f(new VectorI4F(0.25f, 0.5f, 0.75f, 1.0f));
      }
    }

    this.dumpBuffer(b);

    final VectorM2F read = new VectorM2F();
    final SpatialCursorReadable2Type r = this.getReadableCursor(b, a_all);

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

        r.get2f(read);
        if (in_center) {
          Assert.assertEquals(0.25f, read.getXF(), 0.0f);
          Assert.assertEquals(0.5f, read.getYF(), 0.0f);
        } else {
          Assert.assertEquals(0.0f, read.getXF(), 0.0f);
          Assert.assertEquals(0.0f, read.getYF(), 0.0f);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteI()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM2I read_i = new VectorM2I();

    final SpatialCursorReadable2Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable2Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        w.put2i(new VectorI4I(0, 0xFFFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF));

        r.seekTo(x, y);
        r.get2i(read_i);
        Assert.assertEquals(0, read_i.getXI());
        Assert.assertEquals(0xFFFFFFFF, read_i.getYI());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteL()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM2I read_i = new VectorM2I();

    final SpatialCursorReadable2Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable2Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        w.put2l(new VectorI4L(0, 0xFFFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF));

        r.seekTo(x, y);
        r.get2i(read_i);
        Assert.assertEquals(0, read_i.getXI());
        Assert.assertEquals(0xFFFFFFFF, read_i.getYI());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test(expected = RangeCheckException.class) public void testTooSmallR_0()
  {
    final ByteBuffer b = ByteBuffer.allocate(3);
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 0), new RangeInclusiveL(0, 0));
    this.getReadableCursor(b, area);
  }

  @Test(expected = RangeCheckException.class) public void testTooSmallW_0()
  {
    final ByteBuffer b = ByteBuffer.allocate(3);
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 0), new RangeInclusiveL(0, 0));
    this.getWritableCursor(b, area);
  }
}
