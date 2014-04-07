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
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;

final class ByteBufferTextureCursorReadable_3_8_UNFP extends AreaCursor implements
  SpatialCursorReadable3f,
  SpatialCursorReadable3d,
  SpatialCursorReadable3i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM3I  vector = new VectorM3I();

  protected ByteBufferTextureCursorReadable_3_8_UNFP(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3 * 1);
    this.target_data = target_data1;
  }

  @Override public void get3d(
    final @Nonnull VectorM3D v)
    throws ConstraintError
  {
    this.get3i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.z);
  }

  @Override public void get3f(
    final @Nonnull VectorM3F v)
    throws ConstraintError
  {
    this.get3i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.z);
  }

  @Override public void get3i(
    final @Nonnull VectorM3I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    v.x = ByteBufferCursor.unpackUnsigned8(b, i + 0);
    v.y = ByteBufferCursor.unpackUnsigned8(b, i + 1);
    v.z = ByteBufferCursor.unpackUnsigned8(b, i + 2);
    this.next();
  }
}
