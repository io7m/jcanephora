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
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2D;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;

final class ByteBufferTextureCursorWritable_2_16_I extends AreaCursor implements
  SpatialCursorWritable2f,
  SpatialCursorWritable2d,
  SpatialCursorWritable2i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM2I  vector = new VectorM2I();

  protected ByteBufferTextureCursorWritable_2_16_I(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2 * 2);
    this.target_data = target_data;
  }

  @Override public void put2d(
    final @Nonnull VectorReadable2D v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    this.vector.x = FixedPoint.doubleToSignedNormalized(v.getXD(), 16);
    this.vector.y = FixedPoint.doubleToSignedNormalized(v.getYD(), 16);
    this.put2i(this.vector);
  }

  @Override public void put2f(
    final @Nonnull VectorReadable2F v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    this.vector.x = FixedPoint.floatToSignedNormalized(v.getXF(), 16);
    this.vector.y = FixedPoint.floatToSignedNormalized(v.getYF(), 16);
    this.put2i(this.vector);
  }

  @Override public void put2i(
    final @Nonnull VectorReadable2I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    ByteBufferCursor.packInteger16(this.target_data, i + 0, v.getXI());
    ByteBufferCursor.packInteger16(this.target_data, i + 2, v.getYI());
    this.next();
  }
}
