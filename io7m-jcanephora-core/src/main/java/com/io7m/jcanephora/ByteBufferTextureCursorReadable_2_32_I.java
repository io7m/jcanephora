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
import com.io7m.jtensors.VectorM2L;

final class ByteBufferTextureCursorReadable_2_32_I extends AreaCursor implements
  SpatialCursorReadable2f,
  SpatialCursorReadable2d,
  SpatialCursorReadable2i,
  SpatialCursorReadable2l
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM2I  vector = new VectorM2I();

  protected ByteBufferTextureCursorReadable_2_32_I(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 2);
    this.target_data = target_data1;
  }

  @Override public void get2d(
    final @Nonnull VectorM2D v)
    throws ConstraintError
  {
    this.get2i(this.vector);
    v.x = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.x);
    v.y = FixedPoint.signedNormalizedFixedToDoubleLong(32, this.vector.y);
  }

  @Override public void get2f(
    final @Nonnull VectorM2F v)
    throws ConstraintError
  {
    this.get2i(this.vector);
    v.x = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.x);
    v.y = FixedPoint.signedNormalizedFixedToFloatLong(32, this.vector.y);
  }

  @Override public void get2i(
    final @Nonnull VectorM2I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getInt(byte_current);
    v.y = this.target_data.getInt(byte_current + 4);
    this.next();
  }

  @Override public void get2l(
    final @Nonnull VectorM2L v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getInt(byte_current);
    v.y = this.target_data.getInt(byte_current + 4);
    this.next();
  }
}
