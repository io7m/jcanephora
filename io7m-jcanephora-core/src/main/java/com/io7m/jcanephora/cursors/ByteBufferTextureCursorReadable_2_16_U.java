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
import com.io7m.jcanephora.SpatialCursorReadable2Type;
import com.io7m.jintegers.Unsigned16;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM2L;

/**
 * A texture cursor for <code>2_16_U</code> components.
 */

public final class ByteBufferTextureCursorReadable_2_16_U extends
  ByteBufferAreaCursor implements SpatialCursorReadable2Type
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

  public static SpatialCursorReadable2Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorReadable_2_16_U(
      in_target_data,
      target_area,
      update_area);
  }

  private final VectorM2I vector = new VectorM2I();

  private ByteBufferTextureCursorReadable_2_16_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 2 * 2);
  }

  @Override public void get2d(
    final VectorM2D v)
  {
    this.get2i(this.vector);
    v.set2D(
      FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToDouble(16, this.vector.getYI()));
  }

  @Override public void get2f(
    final VectorM2F v)

  {
    this.get2i(this.vector);
    v.set2F(
      FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.getXI()),
      FixedPoint.unsignedNormalizedFixedToFloat(16, this.vector.getYI()));
  }

  @Override public void get2i(
    final VectorM2I v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set2I(
      Unsigned16.unpackFromBuffer(b, i + 0),
      Unsigned16.unpackFromBuffer(b, i + 2));
    this.next();
  }

  @Override public void get2l(
    final VectorM2L v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    v.set2L(
      Unsigned16.unpackFromBuffer(b, i + 0),
      Unsigned16.unpackFromBuffer(b, i + 2));
    this.next();
  }
}
