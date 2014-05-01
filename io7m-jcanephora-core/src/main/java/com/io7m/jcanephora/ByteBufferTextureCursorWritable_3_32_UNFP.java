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
import com.io7m.jtensors.VectorM3L;
import com.io7m.jtensors.VectorReadable3D;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable3L;

final class ByteBufferTextureCursorWritable_3_32_UNFP extends AreaCursor implements
  SpatialCursorWritable3f,
  SpatialCursorWritable3d,
  SpatialCursorWritable3l,
  SpatialCursorWritable3i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM3L  vector = new VectorM3L();

  protected ByteBufferTextureCursorWritable_3_32_UNFP(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3 * 4);
    this.target_data = target_data1;
  }

  @Override public void put3d(
    final @Nonnull VectorReadable3D v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.doubleToUnsignedNormalizedLong(v.getXD(), 32);
    this.vector.y = FixedPoint.doubleToUnsignedNormalizedLong(v.getYD(), 32);
    this.vector.z = FixedPoint.doubleToUnsignedNormalizedLong(v.getZD(), 32);
    this.put3l(this.vector);
  }

  @Override public void put3f(
    final @Nonnull VectorReadable3F v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.floatToUnsignedNormalizedLong(v.getXF(), 32);
    this.vector.y = FixedPoint.floatToUnsignedNormalizedLong(v.getYF(), 32);
    this.vector.z = FixedPoint.floatToUnsignedNormalizedLong(v.getZF(), 32);
    this.put3l(this.vector);
  }

  @Override public void put3i(
    final @Nonnull VectorReadable3I v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, v.getXI());
    this.target_data.putInt(i + 4, v.getYI());
    this.target_data.putInt(i + 8, v.getZI());
    this.next();
  }

  @Override public void put3l(
    final @Nonnull VectorReadable3L v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, (int) v.getXL());
    this.target_data.putInt(i + 4, (int) v.getYL());
    this.target_data.putInt(i + 8, (int) v.getZL());
    this.next();
  }
}
