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
import com.io7m.jcanephora.SpatialCursorWritable2dType;
import com.io7m.jcanephora.SpatialCursorWritable2fType;
import com.io7m.jcanephora.SpatialCursorWritable2iType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2DType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;

final class ByteBufferTextureCursorWritable_2_8_U extends AreaCursor implements
  SpatialCursorWritable2fType,
  SpatialCursorWritable2dType,
  SpatialCursorWritable2iType
{
  private final ByteBuffer target_data;
  private final VectorM2I  vector = new VectorM2I();

  protected ByteBufferTextureCursorWritable_2_8_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 2 * 1);
    this.target_data = in_target_data;
  }

  @Override public void put2d(
    final VectorReadable2DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set2I(
      FixedPoint.doubleToUnsignedNormalized(v.getXD(), 8),
      FixedPoint.doubleToUnsignedNormalized(v.getYD(), 8));
    this.put2i(this.vector);
  }

  @Override public void put2f(
    final VectorReadable2FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set2I(
      FixedPoint.floatToUnsignedNormalized(v.getXF(), 8),
      FixedPoint.floatToUnsignedNormalized(v.getYF(), 8));
    this.put2i(this.vector);
  }

  @Override public void put2i(
    final VectorReadable2IType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.put(i + 0, (byte) v.getXI());
    this.target_data.put(i + 1, (byte) v.getYI());
    this.next();
  }
}
