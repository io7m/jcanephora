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

final class ByteBufferTextureCursorReadable_4_16_U extends AreaCursor implements
  SpatialCursorReadable4f,
  SpatialCursorReadable4d,
  SpatialCursorReadable4i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull byte[]     buffer = new byte[2];
  private final @Nonnull VectorM4I  vector = new VectorM4I();

  protected ByteBufferTextureCursorReadable_4_16_U(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 2);
    this.target_data = target_data;
  }

  @Override public void get4d(
    final @Nonnull VectorM4D v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.z);
    v.w = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.w);
  }

  @Override public void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.z);
    v.w = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.w);
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");

    final int byte_current = (int) this.getByteOffset();
    v.x =
      ByteBufferCursor.unpackInteger16(
        this.target_data,
        this.buffer,
        byte_current);
    v.y =
      ByteBufferCursor.unpackInteger16(
        this.target_data,
        this.buffer,
        byte_current + 2);
    v.z =
      ByteBufferCursor.unpackInteger16(
        this.target_data,
        this.buffer,
        byte_current + 4);
    v.w =
      ByteBufferCursor.unpackInteger16(
        this.target_data,
        this.buffer,
        byte_current + 6);

    this.next();
  }
}