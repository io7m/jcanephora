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
import com.io7m.jtensors.VectorM2L;
import com.io7m.jtensors.VectorReadable2D;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable2L;

final class ByteBufferTextureCursorWritable_2_32_U extends AreaCursor implements
  SpatialCursorWritable2f,
  SpatialCursorWritable2d,
  SpatialCursorWritable2l,
  SpatialCursorWritable2i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM2L  vector = new VectorM2L();

  protected ByteBufferTextureCursorWritable_2_32_U(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2 * 4);
    this.target_data = target_data1;
  }

  @Override public void put2d(
    final @Nonnull VectorReadable2D v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.doubleToUnsignedNormalizedLong(v.getXD(), 32);
    this.vector.y = FixedPoint.doubleToUnsignedNormalizedLong(v.getYD(), 32);
    this.put2l(this.vector);
  }

  @Override public void put2f(
    final @Nonnull VectorReadable2F v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    this.vector.x = FixedPoint.floatToUnsignedNormalizedLong(v.getXF(), 32);
    this.vector.y = FixedPoint.floatToUnsignedNormalizedLong(v.getYF(), 32);
    this.put2l(this.vector);
  }

  @Override public void put2i(
    final @Nonnull VectorReadable2I v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, v.getXI());
    this.target_data.putInt(i + 4, v.getYI());
    this.next();
  }

  @Override public void put2l(
    final @Nonnull VectorReadable2L v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, (int) v.getXL());
    this.target_data.putInt(i + 4, (int) v.getYL());
    this.next();
  }
}
