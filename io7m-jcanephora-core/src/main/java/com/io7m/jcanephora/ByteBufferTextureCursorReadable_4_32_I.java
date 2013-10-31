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
import com.io7m.jtensors.VectorM4L;

final class ByteBufferTextureCursorReadable_4_32_I extends AreaCursor implements
  SpatialCursorReadable4f,
  SpatialCursorReadable4d,
  SpatialCursorReadable4l,
  SpatialCursorReadable4i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM4L  vector = new VectorM4L();

  protected ByteBufferTextureCursorReadable_4_32_I(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 4);
    this.target_data = target_data;
  }

  @Override public void get4d(
    final @Nonnull VectorM4D v)
    throws ConstraintError
  {
    this.get4l(this.vector);
    v.x = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.x);
    v.y = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.y);
    v.z = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.z);
    v.w = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.w);
  }

  @Override public void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError
  {
    this.get4l(this.vector);
    v.x = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.x);
    v.y = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.y);
    v.z = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.z);
    v.w = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.w);
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.x = this.target_data.getInt(i);
    v.y = this.target_data.getInt(i + 4);
    v.z = this.target_data.getInt(i + 8);
    v.w = this.target_data.getInt(i + 12);
    this.next();
  }

  @Override public void get4l(
    final @Nonnull VectorM4L v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getInt(byte_current);
    v.y = this.target_data.getInt(byte_current + 4);
    v.z = this.target_data.getInt(byte_current + 8);
    v.w = this.target_data.getInt(byte_current + 12);
    this.next();
  }
}
