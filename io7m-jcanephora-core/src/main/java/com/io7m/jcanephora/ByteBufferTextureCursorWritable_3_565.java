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
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorReadable3D;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;

final class ByteBufferTextureCursorWritable_3_565 extends AreaCursor implements
  SpatialCursorWritable3f,
  SpatialCursorWritable3d,
  SpatialCursorWritable3i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM3I  vector = new VectorM3I();

  protected ByteBufferTextureCursorWritable_3_565(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data;
  }

  @Override public void put3d(
    final @Nonnull VectorReadable3D v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    this.vector.x = FixedPoint.doubleToUnsignedNormalized(v.getXD(), 5);
    this.vector.y = FixedPoint.doubleToUnsignedNormalized(v.getYD(), 6);
    this.vector.z = FixedPoint.doubleToUnsignedNormalized(v.getZD(), 5);
    this.put3i(this.vector);
  }

  @Override public void put3f(
    final @Nonnull VectorReadable3F v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    this.vector.x = FixedPoint.floatToUnsignedNormalized(v.getXF(), 5);
    this.vector.y = FixedPoint.floatToUnsignedNormalized(v.getYF(), 6);
    this.vector.z = FixedPoint.floatToUnsignedNormalized(v.getZF(), 5);
    this.put3i(this.vector);
  }

  @Override public void put3i(
    final @Nonnull VectorReadable3I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final int k = TexturePixelPack.pack2_565(v.getXI(), v.getYI(), v.getZI());
    final ByteBuffer b = this.target_data;
    ByteBufferCursor.packInteger16(b, i, k);
    this.next();
  }
}