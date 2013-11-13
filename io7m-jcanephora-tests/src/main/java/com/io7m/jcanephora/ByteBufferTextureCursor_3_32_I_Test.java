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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jtensors.VectorI4D;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jtensors.VectorI4L;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM3L;

public final class ByteBufferTextureCursor_3_32_I_Test
{
  private final double EPSILON                 = 0.000000001;
  private final int    ELEMENT_COMPONENT_BYTES = 4;
  private final int    ELEMENT_COUNT           = 3;
  private final int    WIDTH                   = 4;
  private final int    HEIGHT                  = 4;

  @SuppressWarnings("boxing") private void dumpBuffer(
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
    try {
      return new AreaInclusive(
        new RangeInclusive(0, this.WIDTH - 1),
        new RangeInclusive(0, this.HEIGHT - 1));
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  @SuppressWarnings("static-method") private
    AreaInclusive
    getAreaSmallerCentered()
  {
    try {
      final int lo_x = 1;
      final int hi_x = 2;
      final int lo_y = 1;
      final int hi_y = 2;
      return new AreaInclusive(
        new RangeInclusive(lo_x, hi_x),
        new RangeInclusive(lo_y, hi_y));
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  private ByteBuffer getNewAreaBuffer()
  {
    return ByteBuffer.allocate(this.HEIGHT * (this.WIDTH * (4 * 4)));
  }

  private @SuppressWarnings("static-method")
    ByteBufferTextureCursorReadable_3_32_I
    getReadableCursor(
      final ByteBuffer buffer,
      final AreaInclusive area)
  {
    try {
      return new ByteBufferTextureCursorReadable_3_32_I(buffer, area, area);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  private @SuppressWarnings("static-method")
    ByteBufferTextureCursorWritable_3_32_I
    getWritableCursor(
      final ByteBuffer buffer,
      final AreaInclusive area)
  {
    try {
      return new ByteBufferTextureCursorWritable_3_32_I(buffer, area, area);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  private @SuppressWarnings("static-method")
    ByteBufferTextureCursorWritable_3_32_I
    getWritableCursorSub(
      final ByteBuffer buffer,
      final AreaInclusive area_whole,
      final AreaInclusive area_update)
  {
    try {
      return new ByteBufferTextureCursorWritable_3_32_I(
        buffer,
        area_whole,
        area_update);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Test public void testReadWriteD()
    throws ConstraintError
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3F read_f = new VectorM3F();
    final VectorM3D read_d = new VectorM3D();
    final VectorM3L read_l = new VectorM3L();
    final VectorM3I read_i = new VectorM3I();

    final ByteBufferTextureCursorReadable_3_32_I r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_3_32_I w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get3f(read_f);
        Assert.assertEquals(0.0f, read_f.x, 0.0f);
        Assert.assertEquals(0.0f, read_f.y, 0.0f);
        Assert.assertEquals(0.0f, read_f.z, 0.0f);

        w.put3d(new VectorI4D(-1.0, 1.0, -0.5, 0.5));

        r.seekTo(x, y);
        r.get3f(read_f);
        Assert.assertEquals(-1.0f, read_f.x, 0.0f);
        Assert.assertEquals(1.0f, read_f.y, 0.0f);
        Assert.assertEquals(-0.5f, read_f.z, 0.0f);

        r.seekTo(x, y);
        r.get3d(read_d);
        Assert.assertEquals(-1.0, read_d.x, this.EPSILON);
        Assert.assertEquals(1.0, read_d.y, this.EPSILON);
        Assert.assertEquals(-0.5, read_d.z, this.EPSILON);

        r.seekTo(x, y);
        r.get3i(read_i);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_i.x);
        Assert.assertEquals(Integer.MAX_VALUE, read_i.y);

        r.seekTo(x, y);
        r.get3l(read_l);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_l.x);
        Assert.assertEquals(Integer.MAX_VALUE, read_l.y);
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteF()
    throws ConstraintError
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3F read_f = new VectorM3F();
    final VectorM3D read_d = new VectorM3D();
    final VectorM3L read_l = new VectorM3L();
    final VectorM3I read_i = new VectorM3I();

    final ByteBufferTextureCursorReadable_3_32_I r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_3_32_I w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        r.get3f(read_f);
        Assert.assertEquals(0.0f, read_f.x, 0.0f);
        Assert.assertEquals(0.0f, read_f.y, 0.0f);
        Assert.assertEquals(0.0f, read_f.z, 0.0f);

        w.put3f(new VectorI4F(-1.0f, 1.0f, -0.5f, 0.5f));

        r.seekTo(x, y);
        r.get3f(read_f);
        Assert.assertEquals(-1.0f, read_f.x, 0.0f);
        Assert.assertEquals(1.0f, read_f.y, 0.0f);
        Assert.assertEquals(-0.5f, read_f.z, 0.0f);

        r.seekTo(x, y);
        r.get3d(read_d);
        Assert.assertEquals(-1.0, read_d.x, this.EPSILON);
        Assert.assertEquals(1.0, read_d.y, this.EPSILON);
        Assert.assertEquals(-0.5, read_d.z, this.EPSILON);

        r.seekTo(x, y);
        r.get3i(read_i);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_i.x);
        Assert.assertEquals(Integer.MAX_VALUE, read_i.y);

        r.seekTo(x, y);
        r.get3l(read_l);
        Assert.assertEquals(Integer.MIN_VALUE + 1, read_l.x);
        Assert.assertEquals(Integer.MAX_VALUE, read_l.y);
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteFSub()
    throws ConstraintError
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a_all = this.getArea();
    final AreaInclusive a_up = this.getAreaSmallerCentered();

    final ByteBufferTextureCursorWritable_3_32_I w =
      this.getWritableCursorSub(b, a_all, a_up);

    final RangeInclusive aury = a_up.getRangeY();
    final RangeInclusive aurx = a_up.getRangeX();
    for (long y = aury.getLower(); y <= aury.getUpper(); ++y) {
      for (long x = aurx.getLower(); x <= aurx.getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());
        w.put3f(new VectorI4F(0.25f, 0.5f, 0.75f, 1.0f));
      }
    }

    this.dumpBuffer(b);

    final VectorM3F read = new VectorM3F();
    final ByteBufferTextureCursorReadable_3_32_I r =
      this.getReadableCursor(b, a_all);

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
          Assert.assertEquals(0.25f, read.x, 0.0f);
          Assert.assertEquals(0.5f, read.y, 0.0f);
          Assert.assertEquals(0.75f, read.z, 0.0f);
        } else {
          Assert.assertEquals(0.0f, read.x, 0.0f);
          Assert.assertEquals(0.0f, read.y, 0.0f);
          Assert.assertEquals(0.0f, read.z, 0.0f);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteI()
    throws ConstraintError
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3I read_i = new VectorM3I();

    final ByteBufferTextureCursorReadable_3_32_I r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_3_32_I w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        w.put3i(new VectorI4I(0, 0xFFFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF));

        r.seekTo(x, y);
        r.get3i(read_i);
        Assert.assertEquals(0, read_i.x);
        Assert.assertEquals(0xFFFFFFFF, read_i.y);
        Assert.assertEquals(0x7FFFFFFF, read_i.z);
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }

  @Test public void testReadWriteL()
    throws ConstraintError
  {
    final ByteBuffer b = this.getNewAreaBuffer();
    final AreaInclusive a = this.getArea();

    final VectorM3I read_i = new VectorM3I();

    final ByteBufferTextureCursorReadable_3_32_I r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_3_32_I w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        w.put3l(new VectorI4L(0, 0xFFFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF));

        r.seekTo(x, y);
        r.get3i(read_i);
        Assert.assertEquals(0, read_i.x);
        Assert.assertEquals(0xFFFFFFFF, read_i.y);
        Assert.assertEquals(0x7FFFFFFF, read_i.z);
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }
}