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
import com.io7m.jcanephora.SpatialCursorReadable4Type;
import com.io7m.jintegers.Unsigned8;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;
import com.io7m.jtensors.VectorM4L;

/**
 * A texture cursor for <code>4_8_U</code> components.
 */

public final class ByteBufferTextureCursorReadable_4_8_U extends
  ByteBufferAreaCursor implements SpatialCursorReadable4Type
{
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

  public static SpatialCursorReadable4Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorReadable_4_8_U(
      in_target_data,
      target_area,
      update_area);
  }

  private final VectorM4I vector = new VectorM4I();

  private ByteBufferTextureCursorReadable_4_8_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 4 * 1);
  }

  @Override public void get4d(
    final VectorM4D v)
  {
    this.get4i(this.vector);
    v.set4D(
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getYI()),
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getZI()),
      FixedPoint.unsignedNormalizedFixedToDouble(8, this.vector.getWI()));
  }

  @Override public void get4f(
    final VectorM4F v)
  {
    this.get4i(this.vector);
    v.set4F(
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getYI()),
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getZI()),
      FixedPoint.unsignedNormalizedFixedToFloat(8, this.vector.getWI()));
  }

  @Override public void get4i(
    final VectorM4I v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set4I(
      Unsigned8.unpackFromBuffer(b, i + 0),
      Unsigned8.unpackFromBuffer(b, i + 1),
      Unsigned8.unpackFromBuffer(b, i + 2),
      Unsigned8.unpackFromBuffer(b, i + 3));
    this.next();
  }

  @Override public void get4l(
    final VectorM4L v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set4L(
      Unsigned8.unpackFromBuffer(b, i + 0),
      Unsigned8.unpackFromBuffer(b, i + 1),
      Unsigned8.unpackFromBuffer(b, i + 2),
      Unsigned8.unpackFromBuffer(b, i + 3));
    this.next();
  }
}
