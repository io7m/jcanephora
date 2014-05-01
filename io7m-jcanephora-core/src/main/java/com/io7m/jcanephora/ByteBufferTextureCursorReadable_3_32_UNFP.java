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
import com.io7m.jtensors.VectorM3L;

final class ByteBufferTextureCursorReadable_3_32_UNFP extends AreaCursor implements
  SpatialCursorReadable3f,
  SpatialCursorReadable3d,
  SpatialCursorReadable3i,
  SpatialCursorReadable3l
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM3L  vector = new VectorM3L();

  protected ByteBufferTextureCursorReadable_3_32_UNFP(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3 * 4);
    this.target_data = target_data1;
  }

  @Override public void get3d(
    final @Nonnull VectorM3D v)
    throws ConstraintError
  {
    this.get3l(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDoubleLong(32, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDoubleLong(32, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToDoubleLong(32, this.vector.z);
  }

  @Override public void get3f(
    final @Nonnull VectorM3F v)
    throws ConstraintError
  {
    this.get3l(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.z);
  }

  @Override public void get3i(
    final @Nonnull VectorM3I v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.x = this.target_data.getInt(i);
    v.y = this.target_data.getInt(i + 4);
    v.z = this.target_data.getInt(i + 8);
    this.next();
  }

  @Override public void get3l(
    final @Nonnull VectorM3L v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.x = ByteBufferCursor.unpackUnsigned32(this.target_data, i);
    v.y = ByteBufferCursor.unpackUnsigned32(this.target_data, i + 4);
    v.z = ByteBufferCursor.unpackUnsigned32(this.target_data, i + 8);
    this.next();
  }
}
