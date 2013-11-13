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
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;

final class ByteBufferTextureCursorReadable_2_16_UNFP extends AreaCursor implements
  SpatialCursorReadable2f,
  SpatialCursorReadable2d,
  SpatialCursorReadable2i
{
  private final @Nonnull byte[]     buffer = new byte[2];
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM2I  vector = new VectorM2I();

  protected ByteBufferTextureCursorReadable_2_16_UNFP(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2 * 2);
    this.target_data = target_data;
  }

  @Override public void get2d(
    final @Nonnull VectorM2D v)
    throws ConstraintError
  {
    this.get2i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.y);
  }

  @Override public void get2f(
    final @Nonnull VectorM2F v)
    throws ConstraintError
  {
    this.get2i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.y);
  }

  @Override public void get2i(
    final @Nonnull VectorM2I v)
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

    this.next();
  }
}
