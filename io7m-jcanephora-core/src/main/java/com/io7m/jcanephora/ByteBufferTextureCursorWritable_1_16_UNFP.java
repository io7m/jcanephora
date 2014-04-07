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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

final class ByteBufferTextureCursorWritable_1_16_UNFP extends AreaCursor implements
  SpatialCursorWritable1f,
  SpatialCursorWritable1d,
  SpatialCursorWritable1i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_1_16_UNFP(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data1;
  }

  @Override public void put1d(
    final double x)
    throws ConstraintError
  {
    this.put1i(FixedPoint.doubleToUnsignedNormalized(x, 16));
  }

  @Override public void put1f(
    final float x)
    throws ConstraintError
  {
    this.put1i(FixedPoint.floatToUnsignedNormalized(x, 16));
  }

  @Override public void put1i(
    final int x)
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    ByteBufferCursor.packInteger16(this.target_data, byte_current, x);
    this.next();
  }
}
