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
import com.io7m.jcanephora.SpatialCursorReadable3Type;
import com.io7m.jintegers.Unsigned32;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM3L;

/**
 * A texture cursor for <code>3_32_U</code> components.
 */

public final class ByteBufferTextureCursorReadable_3_32_U extends
  ByteBufferAreaCursor implements SpatialCursorReadable3Type
{
  private final VectorM3L vector = new VectorM3L();

  /**
   * Construct a new cursor.
   * 
   * @param in_target_data
   *          The byte buffer.
   * @param target_area
   *          The outer area of the buffer.
   * @param update_area
   *          The area of the buffer that will be read.
   * @return A new cursor.
   */

  public static SpatialCursorReadable3Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorReadable_3_32_U(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorReadable_3_32_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 3 * 4);
  }

  @Override public void get3d(
    final VectorM3D v)
  {
    this.get3l(this.vector);
    v
      .set3D(
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getXL()),
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getYL()),
        FixedPoint.unsignedNormalizedFixedToDoubleLong(
          32,
          this.vector.getZL()));
  }

  @Override public void get3f(
    final VectorM3F v)
  {
    this.get3l(this.vector);
    v.set3F(
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getXL()),
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getYL()),
      FixedPoint.unsignedNormalizedFixedToFloatLong(32, this.vector.getZL()));
  }

  @Override public void get3i(
    final VectorM3I v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set3I(b.getInt(i + 0), b.getInt(i + 4), b.getInt(i + 8));
    this.next();
  }

  @Override public void get3l(
    final VectorM3L v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set3L(
      Unsigned32.unpackFromBuffer(b, i + 0),
      Unsigned32.unpackFromBuffer(b, i + 4),
      Unsigned32.unpackFromBuffer(b, i + 8));
    this.next();
  }
}
