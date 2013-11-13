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

public final class ByteBufferTextureCursor_1_32f_Test
{
  private final int    ELEMENT_COMPONENT_BYTES = 4;
  private final int    ELEMENT_COUNT           = 1;
  private final double EPSILON                 = 0.000000001;
  private final int    HEIGHT                  = 4;
  private final int    WIDTH                   = 4;

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
        final float bx = b.getFloat(i + 0);
        System.out.print(String.format("[%08f]", bx));
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
    ByteBufferTextureCursorReadable_1_32f
    getReadableCursor(
      final ByteBuffer buffer,
      final AreaInclusive area)
  {
    try {
      return new ByteBufferTextureCursorReadable_1_32f(buffer, area, area);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  private @SuppressWarnings("static-method")
    ByteBufferTextureCursorWritable_1_32f
    getWritableCursor(
      final ByteBuffer buffer,
      final AreaInclusive area)
  {
    try {
      return new ByteBufferTextureCursorWritable_1_32f(buffer, area, area);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  private @SuppressWarnings("static-method")
    ByteBufferTextureCursorWritable_1_32f
    getWritableCursorSub(
      final ByteBuffer buffer,
      final AreaInclusive area_whole,
      final AreaInclusive area_update)
  {
    try {
      return new ByteBufferTextureCursorWritable_1_32f(
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

    final ByteBufferTextureCursorReadable_1_32f r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_1_32f w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        {
          final float z = r.get1f();
          Assert.assertEquals(0.0f, z, 0.0f);
        }

        w.put1d(1370.0f);

        r.seekTo(x, y);
        {
          final float z = r.get1f();
          Assert.assertEquals(1370.0f, z, 0.0f);
        }

        r.seekTo(x, y);
        {
          final double z = r.get1d();
          Assert.assertEquals(1370.0, z, this.EPSILON);
        }
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

    final ByteBufferTextureCursorReadable_1_32f r =
      this.getReadableCursor(b, a);
    final ByteBufferTextureCursorWritable_1_32f w =
      this.getWritableCursor(b, a);

    for (int y = 0; y <= a.getRangeY().getUpper(); ++y) {
      for (int x = 0; x <= a.getRangeX().getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertTrue(r.isValid());
        Assert.assertEquals(x, r.getElementX());
        Assert.assertEquals(y, r.getElementY());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());

        {
          final float z = r.get1f();
          Assert.assertEquals(0.0f, z, 0.0f);
        }

        w.put1f(1370.0f);

        r.seekTo(x, y);
        {
          final float z = r.get1f();
          Assert.assertEquals(1370.0f, z, 0.0f);
        }

        r.seekTo(x, y);
        {
          final double z = r.get1d();
          Assert.assertEquals(1370.0, z, this.EPSILON);
        }
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

    final ByteBufferTextureCursorWritable_1_32f w =
      this.getWritableCursorSub(b, a_all, a_up);

    final RangeInclusive aury = a_up.getRangeY();
    final RangeInclusive aurx = a_up.getRangeX();
    for (long y = aury.getLower(); y <= aury.getUpper(); ++y) {
      for (long x = aurx.getLower(); x <= aurx.getUpper(); ++x) {
        Assert.assertTrue(w.isValid());
        Assert.assertEquals(x, w.getElementX());
        Assert.assertEquals(y, w.getElementY());
        w.put1f(x * 1370f);
      }
    }

    this.dumpBuffer(b);

    final ByteBufferTextureCursorReadable_1_32f r =
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

        final float k = r.get1f();
        if (in_center) {
          Assert.assertEquals(k, x * 1370.0f, 0.0f);
        } else {
          Assert.assertEquals(0.0f, k, 0.0f);
        }
      }
    }

    Assert.assertFalse(w.isValid());
    Assert.assertFalse(r.isValid());
  }
}
