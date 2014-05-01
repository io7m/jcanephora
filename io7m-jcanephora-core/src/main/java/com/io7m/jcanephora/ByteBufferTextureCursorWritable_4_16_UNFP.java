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

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorM4I;
import com.io7m.jtensors.VectorReadable4D;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

final class ByteBufferTextureCursorWritable_4_16_UNFP extends AreaCursor implements
  SpatialCursorWritable4f,
  SpatialCursorWritable4d,
  SpatialCursorWritable4i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM4I  vector = new VectorM4I();

  protected ByteBufferTextureCursorWritable_4_16_UNFP(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 2);
    this.target_data = target_data1;
  }

  @Override public void put4d(
    final @Nonnull VectorReadable4D v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.doubleToUnsignedNormalized(v.getXD(), 16);
    this.vector.y = FixedPoint.doubleToUnsignedNormalized(v.getYD(), 16);
    this.vector.z = FixedPoint.doubleToUnsignedNormalized(v.getZD(), 16);
    this.vector.w = FixedPoint.doubleToUnsignedNormalized(v.getWD(), 16);
    this.put4i(this.vector);
  }

  @Override public void put4f(
    final @Nonnull VectorReadable4F v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.floatToUnsignedNormalized(v.getXF(), 16);
    this.vector.y = FixedPoint.floatToUnsignedNormalized(v.getYF(), 16);
    this.vector.z = FixedPoint.floatToUnsignedNormalized(v.getZF(), 16);
    this.vector.w = FixedPoint.floatToUnsignedNormalized(v.getWF(), 16);
    this.put4i(this.vector);
  }

  @Override public void put4i(
    final @Nonnull VectorReadable4I v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    ByteBufferCursor.packInteger16(this.target_data, i + 0, v.getXI());
    ByteBufferCursor.packInteger16(this.target_data, i + 2, v.getYI());
    ByteBufferCursor.packInteger16(this.target_data, i + 4, v.getZI());
    ByteBufferCursor.packInteger16(this.target_data, i + 6, v.getWI());
    this.next();
  }
}
