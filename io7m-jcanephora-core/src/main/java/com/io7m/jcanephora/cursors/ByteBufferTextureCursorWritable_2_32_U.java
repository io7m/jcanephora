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
import com.io7m.jcanephora.SpatialCursorWritable2Type;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2L;
import com.io7m.jtensors.VectorReadable2DType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable2LType;

/**
 * A texture cursor for <code>2_32_U</code> components.
 */

public final class ByteBufferTextureCursorWritable_2_32_U extends
  ByteBufferAreaCursor implements SpatialCursorWritable2Type
{
  /**
   * Construct a new cursor.
   * 
   * @param in_target_data
   *          The byte buffer.
   * @param target_area
   *          The outer area of the buffer.
   * @param update_area
   *          The area of the buffer that will be updated.
   * @return A new cursor.
   */

  public static SpatialCursorWritable2Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_2_32_U(
      in_target_data,
      target_area,
      update_area);
  }

  private final VectorM2L vector = new VectorM2L();

  private ByteBufferTextureCursorWritable_2_32_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 2 * 4);
  }

  @Override public void put2d(
    final VectorReadable2DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set2L(
      FixedPoint.doubleToUnsignedNormalizedLong(v.getXD(), 32),
      FixedPoint.doubleToUnsignedNormalizedLong(v.getYD(), 32));
    this.put2l(this.vector);
  }

  @Override public void put2f(
    final VectorReadable2FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set2L(
      FixedPoint.floatToUnsignedNormalizedLong(v.getXF(), 32),
      FixedPoint.floatToUnsignedNormalizedLong(v.getYF(), 32));
    this.put2l(this.vector);
  }

  @Override public void put2i(
    final VectorReadable2IType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.getBuffer();
    b.putInt(i + 0, v.getXI());
    b.putInt(i + 4, v.getYI());
    this.next();
  }

  @Override public void put2l(
    final VectorReadable2LType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.getBuffer();
    b.putInt(i + 0, (int) v.getXL());
    b.putInt(i + 4, (int) v.getYL());
    this.next();
  }
}