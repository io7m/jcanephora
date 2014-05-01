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
import com.io7m.jcanephora.SpatialCursorReadable4dType;
import com.io7m.jcanephora.SpatialCursorReadable4fType;
import com.io7m.jcanephora.SpatialCursorReadable4iType;
import com.io7m.jintegers.Integer16;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;

final class ByteBufferTextureCursorReadable_4_16_I extends AreaCursor implements
  SpatialCursorReadable4fType,
  SpatialCursorReadable4dType,
  SpatialCursorReadable4iType
{
  private final ByteBuffer target_data;
  private final VectorM4I  vector = new VectorM4I();

  protected ByteBufferTextureCursorReadable_4_16_I(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)

  {
    super(target_area, update_area, 4 * 2);
    this.target_data = in_target_data;
  }

  @Override public void get4d(
    final VectorM4D v)
  {
    this.get4i(this.vector);
    v.set4D(
      FixedPoint.signedNormalizedFixedToDouble(16, this.vector.getXI()),
      FixedPoint.signedNormalizedFixedToDouble(16, this.vector.getYI()),
      FixedPoint.signedNormalizedFixedToDouble(16, this.vector.getZI()),
      FixedPoint.signedNormalizedFixedToDouble(16, this.vector.getWI()));
  }

  @Override public void get4f(
    final VectorM4F v)
  {
    this.get4i(this.vector);
    v.set4F(
      FixedPoint.signedNormalizedFixedToFloat(16, this.vector.getXI()),
      FixedPoint.signedNormalizedFixedToFloat(16, this.vector.getYI()),
      FixedPoint.signedNormalizedFixedToFloat(16, this.vector.getZI()),
      FixedPoint.signedNormalizedFixedToFloat(16, this.vector.getWI()));
  }

  @Override public void get4i(
    final VectorM4I v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.set4I(
      Integer16.unpackFromBuffer(this.target_data, i + 0),
      Integer16.unpackFromBuffer(this.target_data, i + 2),
      Integer16.unpackFromBuffer(this.target_data, i + 4),
      Integer16.unpackFromBuffer(this.target_data, i + 6));
    this.next();
  }
}
