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
import com.io7m.jcanephora.SpatialCursorReadable3Type;
import com.io7m.jcanephora.SpatialCursorWritable3Type;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_565;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_565;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI3L;
import com.io7m.jtensors.VectorI4D;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM3L;

@SuppressWarnings({ "static-method", "boxing", "null" }) public final class ByteBufferTextureCursor_3_565_Test
{
  @Test public void testReadWriteFL()
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();
    final VectorM3D read_d = new VectorM3D();
    final VectorM3L read_l = new VectorM3L();
    final SpatialCursorReadable3Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable3Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        w.seekTo(x, y);
        w.put3l(new VectorI3L(0x1f, 0x3f, 0x1f));

        r.seekTo(x, y);
        r.get3d(read_d);
        Assert.assertEquals(1.0, read_d.getXD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getYD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getZD(), this.EPSILON);

        r.seekTo(x, y);
        r.get3l(read_l);
        Assert.assertEquals(0x1f, read_l.getXL());
        Assert.assertEquals(0x3f, read_l.getYL());
        Assert.assertEquals(0x1f, read_l.getZL());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  private final int    ELEMENT_COMPONENT_BYTES = 2;
  private final int    ELEMENT_COUNT           = 1;
  private final double EPSILON                 = 0.01;
  private final int    HEIGHT                  = 4;
  private final int    WIDTH                   = 4;

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
        System.out.print(String.format("[%08x]", bx));
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

  private SpatialCursorReadable3Type getReadableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorReadable_3_565
      .newCursor(buffer, area, area);
  }

  private SpatialCursorWritable3Type getWritableCursor(
    final ByteBuffer buffer,
    final AreaInclusive area)
  {
    return ByteBufferTextureCursorWritable_3_565
      .newCursor(buffer, area, area);
  }

  private SpatialCursorWritable3Type getWritableCursorSub(
    final ByteBuffer buffer,
    final AreaInclusive area_whole,
    final AreaInclusive area_update)
  {
    return ByteBufferTextureCursorWritable_3_565.newCursor(
      buffer,
      area_whole,
      area_update);
  }

  @Test public void testReadWriteD()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3F read_f = new VectorM3F();
    final VectorM3D read_d = new VectorM3D();

    final SpatialCursorReadable3Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable3Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get3f(read_f);
        Assert.assertEquals(0.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getYF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getZF(), 0.0f);

        w.put3d(new VectorI4D(1.0, 0.5, 1.0, 1.0));

        r.seekTo(x, y);
        r.get3f(read_f);
        Assert.assertEquals(1.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.5f, read_f.getYF(), this.EPSILON);
        Assert.assertEquals(1.0f, read_f.getZF(), 0.0f);

        r.seekTo(x, y);
        r.get3d(read_d);
        Assert.assertEquals(1.0, read_d.getXD(), this.EPSILON);
        Assert.assertEquals(0.5, read_d.getYD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getZD(), this.EPSILON);
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteF()

  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3F read_f = new VectorM3F();
    final VectorM3D read_d = new VectorM3D();

    final SpatialCursorReadable3Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable3Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get3f(read_f);
        Assert.assertEquals(0.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getYF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getZF(), 0.0f);

        w.put3f(new VectorI4F(1.0f, 0.0f, 1.0f, 1.0f));

        r.seekTo(x, y);
        r.get3f(read_f);
        Assert.assertEquals(1.0f, read_f.getXF(), 0.0f);
        Assert.assertEquals(0.0f, read_f.getYF(), 0.0f);
        Assert.assertEquals(1.0f, read_f.getZF(), 0.0f);

        r.seekTo(x, y);
        r.get3d(read_d);
        Assert.assertEquals(1.0, read_d.getXD(), this.EPSILON);
        Assert.assertEquals(0.0, read_d.getYD(), this.EPSILON);
        Assert.assertEquals(1.0, read_d.getZD(), this.EPSILON);
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

    final SpatialCursorWritable3Type w =
      this.getWritableCursorSub(b, a_all, a_up);

    final RangeInclusiveL aury = a_up.getRangeY();
    final RangeInclusiveL aurx = a_up.getRangeX();
    for (long y = aury.getLower(); y <= aury.getUpper(); ++y) {
      for (long x = aurx.getLower(); x <= aurx.getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());
        w.put3f(new VectorI4F(1.0f, 0.5f, 1.0f, 1.0f));
      }
    }

    this.dumpBuffer(b);

    final VectorM3F read = new VectorM3F();
    final SpatialCursorReadable3Type r = this.getReadableCursor(b, a_all);

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

        r.get3f(read);
        if (in_center) {
          Assert.assertEquals(1.0f, read.getXF(), 0.0f);
          Assert.assertEquals(0.5f, read.getYF(), this.EPSILON);
          Assert.assertEquals(1.0f, read.getZF(), 0.0f);
        } else {
          Assert.assertEquals(0.0f, read.getXF(), 0.0f);
          Assert.assertEquals(0.0f, read.getYF(), 0.0f);
          Assert.assertEquals(0.0f, read.getZF(), 0.0f);
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

    final VectorM3I read_i = new VectorM3I();

    final SpatialCursorReadable3Type r = this.getReadableCursor(b, a);
    final SpatialCursorWritable3Type w = this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        w.put3i(new VectorI3I(0x1F, 0x3F, 0));

        r.seekTo(x, y);
        r.get3i(read_i);
        Assert.assertEquals(0x1F, read_i.getXI());
        Assert.assertEquals(0x3F, read_i.getYI());
        Assert.assertEquals(0, read_i.getZI());
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

}
