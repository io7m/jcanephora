/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.cursors;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FixedPoint;
import com.io7m.jcanephora.SpatialCursorReadable4dType;
import com.io7m.jcanephora.SpatialCursorReadable4fType;
import com.io7m.jcanephora.SpatialCursorReadable4iType;
import com.io7m.jcanephora.SpatialCursorReadable4lType;
import com.io7m.jintegers.Unsigned32;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;
import com.io7m.jtensors.VectorM4L;

final class ByteBufferTextureCursorReadable_4_32_U extends AreaCursor implements
  SpatialCursorReadable4fType,
  SpatialCursorReadable4dType,
  SpatialCursorReadable4lType,
  SpatialCursorReadable4iType
{
  private final ByteBuffer target_data;
  private final VectorM4L  vector = new VectorM4L();

  protected ByteBufferTextureCursorReadable_4_32_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 4 * 4);
    this.target_data = in_target_data;
  }

  @Override public void get4d(
    final VectorM4D v)
  {
    this.get4l(this.vector);
    v
      .set4D(
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getXL()),
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getYL()),
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getZL()),
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getWL()));
  }

  @Override public void get4f(
    final VectorM4F v)
  {
    this.get4l(this.vector);
    v.set4F(
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getXL()),
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getYL()),
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getZL()),
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getWL()));
  }

  @Override public void get4i(
    final VectorM4I v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.set4I(
      this.target_data.getInt(i + 0),
      this.target_data.getInt(i + 4),
      this.target_data.getInt(i + 8),
      this.target_data.getInt(i + 12));
    this.next();
  }

  @Override public void get4l(
    final VectorM4L v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    v.set4L(
      Unsigned32.unpackFromBuffer(this.target_data, i + 0),
      Unsigned32.unpackFromBuffer(this.target_data, i + 4),
      Unsigned32.unpackFromBuffer(this.target_data, i + 8),
      Unsigned32.unpackFromBuffer(this.target_data, i + 12));
    this.next();
  }
}
