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
import com.io7m.jcanephora.SpatialCursorReadable2dType;
import com.io7m.jcanephora.SpatialCursorReadable2fType;
import com.io7m.jcanephora.SpatialCursorReadable2iType;
import com.io7m.jintegers.Unsigned8;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;

final class ByteBufferTextureCursorReadable_2_8_UNFP extends AreaCursor implements
  SpatialCursorReadable2fType,
  SpatialCursorReadable2dType,
  SpatialCursorReadable2iType
{
  private final ByteBuffer target_data;
  private final VectorM2I  vector = new VectorM2I();

  protected ByteBufferTextureCursorReadable_2_8_UNFP(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 2 * 1);
    this.target_data = in_target_data;
  }

  @Override public void get2d(
    final VectorM2D v)
  {
    this.get2i(this.vector);
    v.set2D(
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getYI()));
  }

  @Override public void get2f(
    final VectorM2F v)
  {
    this.get2i(this.vector);
    v.set2F(
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getYI()));
  }

  @Override public void get2i(
    final VectorM2I v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.set2I(
      Unsigned8.unpackFromBuffer(this.target_data, i + 0),
      Unsigned8.unpackFromBuffer(this.target_data, i + 1));
    this.next();
  }
}
