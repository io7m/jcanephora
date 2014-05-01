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
import com.io7m.jcanephora.SpatialCursorWritable3dType;
import com.io7m.jcanephora.SpatialCursorWritable3fType;
import com.io7m.jcanephora.SpatialCursorWritable3iType;
import com.io7m.jcanephora.SpatialCursorWritable3lType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM3L;
import com.io7m.jtensors.VectorReadable3DType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable3LType;

final class ByteBufferTextureCursorWritable_3_32_UNFP extends AreaCursor implements
  SpatialCursorWritable3fType,
  SpatialCursorWritable3dType,
  SpatialCursorWritable3lType,
  SpatialCursorWritable3iType
{
  private final ByteBuffer target_data;
  private final VectorM3L  vector = new VectorM3L();

  protected ByteBufferTextureCursorWritable_3_32_UNFP(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 3 * 4);
    this.target_data = in_target_data;
  }

  @Override public void put3d(
    final VectorReadable3DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set3L(
      FixedPoint.doubleToUnsignedNormalized(v.getXD(), 32),
      FixedPoint.doubleToUnsignedNormalized(v.getYD(), 32),
      FixedPoint.doubleToUnsignedNormalized(v.getZD(), 32));
    this.put3l(this.vector);
  }

  @Override public void put3f(
    final VectorReadable3FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set3L(
      FixedPoint.floatToUnsignedNormalized(v.getXF(), 32),
      FixedPoint.floatToUnsignedNormalized(v.getYF(), 32),
      FixedPoint.floatToUnsignedNormalized(v.getZF(), 32));
    this.put3l(this.vector);
  }

  @Override public void put3i(
    final VectorReadable3IType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, v.getXI());
    this.target_data.putInt(i + 4, v.getYI());
    this.target_data.putInt(i + 8, v.getZI());
    this.next();
  }

  @Override public void put3l(
    final VectorReadable3LType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, (int) v.getXL());
    this.target_data.putInt(i + 4, (int) v.getYL());
    this.target_data.putInt(i + 8, (int) v.getZL());
    this.next();
  }
}
