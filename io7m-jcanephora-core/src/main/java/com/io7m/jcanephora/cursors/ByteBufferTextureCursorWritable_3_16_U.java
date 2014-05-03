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
import com.io7m.jcanephora.SpatialCursorWritable3Type;
import com.io7m.jintegers.Unsigned16;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorReadable3DType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable3LType;

/**
 * A texture cursor for <code>3_16_U</code> components.
 */

public final class ByteBufferTextureCursorWritable_3_16_U extends
  ByteBufferAreaCursor implements SpatialCursorWritable3Type
{
  private final VectorM3I vector = new VectorM3I();

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

  public static SpatialCursorWritable3Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_3_16_U(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorWritable_3_16_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 3 * 2);
  }

  @Override public void put3d(
    final VectorReadable3DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set3I(
      FixedPoint.doubleToUnsignedNormalized(v.getXD(), 16),
      FixedPoint.doubleToUnsignedNormalized(v.getYD(), 16),
      FixedPoint.doubleToUnsignedNormalized(v.getZD(), 16));
    this.put3i(this.vector);
  }

  @Override public void put3f(
    final VectorReadable3FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set3I(
      FixedPoint.floatToUnsignedNormalized(v.getXF(), 16),
      FixedPoint.floatToUnsignedNormalized(v.getYF(), 16),
      FixedPoint.floatToUnsignedNormalized(v.getZF(), 16));
    this.put3i(this.vector);
  }

  @Override public void put3i(
    final VectorReadable3IType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    Unsigned16.packToBuffer(v.getXI(), b, i + 0);
    Unsigned16.packToBuffer(v.getYI(), b, i + 2);
    Unsigned16.packToBuffer(v.getZI(), b, i + 4);
    this.next();
  }

  @Override public void put3l(
    final VectorReadable3LType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    Unsigned16.packToBuffer((int) v.getXL(), b, i + 0);
    Unsigned16.packToBuffer((int) v.getYL(), b, i + 2);
    Unsigned16.packToBuffer((int) v.getZL(), b, i + 4);
    this.next();
  }
}
