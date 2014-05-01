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

final class ByteBufferTextureCursorReadable_4_5551 extends AreaCursor implements
  SpatialCursorReadable4f,
  SpatialCursorReadable4d,
  SpatialCursorReadable4i
{
  private final @Nonnull byte[]     buffer = new byte[2];
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull VectorM4I  vector = new VectorM4I();

  protected ByteBufferTextureCursorReadable_4_5551(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data1;
  }

  @Override public void get4d(
    final @Nonnull VectorM4D v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToDouble(5, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToDouble(5, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToDouble(5, this.vector.z);
    v.w = (this.vector.w > 0) ? 1.0 : 0.0;
  }

  @Override public void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError
  {
    this.get4i(this.vector);
    v.x = FixedPoint.unsignedNormalizedFixedToFloat(5, this.vector.x);
    v.y = FixedPoint.unsignedNormalizedFixedToFloat(5, this.vector.y);
    v.z = FixedPoint.unsignedNormalizedFixedToFloat(5, this.vector.z);
    v.w = (this.vector.w > 0) ? 1.0f : 0.0f;
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    NullCheck.notNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    final byte[] t = this.buffer;
    final int k = ByteBufferCursor.unpackInteger16(b, t, byte_current);
    TexturePixelPack.unpack2_5551(k, v);
    this.next();
  }
}
