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
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;

final class ByteBufferTextureCursorReadable_4_8_UNFP extends AreaCursor implements
  SpatialCursorReadable4f,
  SpatialCursorReadable4d,
  SpatialCursorReadable4i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM4I  vector = new VectorM4I();

  protected ByteBufferTextureCursorReadable_4_8_UNFP(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 1);
    this.target_data = target_data;
  }

  @Override public void get4d(
    final @Nonnull VectorM4D v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.z);
    v.w = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.w);
  }

  @Override public void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.z);
    v.w = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.w);
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.x = ByteBufferCursor.unpackUnsigned8(this.target_data, i);
    v.y = ByteBufferCursor.unpackUnsigned8(this.target_data, i + 1);
    v.z = ByteBufferCursor.unpackUnsigned8(this.target_data, i + 2);
    v.w = ByteBufferCursor.unpackUnsigned8(this.target_data, i + 3);
    this.next();
  }
}
