/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.cursors;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FixedPoint;
import com.io7m.jcanephora.SpatialCursorWritable1dType;
import com.io7m.jcanephora.SpatialCursorWritable1fType;
import com.io7m.jcanephora.SpatialCursorWritable1iType;
import com.io7m.jcanephora.SpatialCursorWritable1lType;

final class ByteBufferTextureCursorWritable_1_32_UNFP extends AreaCursor implements
  SpatialCursorWritable1fType,
  SpatialCursorWritable1dType,
  SpatialCursorWritable1lType,
  SpatialCursorWritable1iType
{
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_1_32_UNFP(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 4);
    this.target_data = in_target_data;
  }

  @Override public void put1d(
    final double x)
  {
    this.put1l(FixedPoint.doubleToUnsignedNormalizedLong(x, 32));
  }

  @Override public void put1f(
    final float x)
  {
    this.put1l(FixedPoint.floatToUnsignedNormalizedLong(x, 32));
  }

  @Override public void put1i(
    final int x)
  {
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i, x);
    this.next();
  }

  @Override public void put1l(
    final long x)
  {
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putInt(byte_current, (int) x);
    this.next();
  }
}
