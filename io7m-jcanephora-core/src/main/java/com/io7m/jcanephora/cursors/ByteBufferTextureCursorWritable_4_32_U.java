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
import com.io7m.jcanephora.SpatialCursorWritable4Type;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM4L;
import com.io7m.jtensors.VectorReadable4DType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.io7m.jtensors.VectorReadable4LType;

/**
 * A texture cursor for <code>4_32_U</code> components.
 */

public final class ByteBufferTextureCursorWritable_4_32_U extends
  ByteBufferAreaCursor implements SpatialCursorWritable4Type
{
  private final VectorM4L vector = new VectorM4L();

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

  public static SpatialCursorWritable4Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_4_32_U(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorWritable_4_32_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 4 * 4);
  }

  @Override public void put4d(
    final VectorReadable4DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4L(
      FixedPoint.doubleToUnsignedNormalizedLong(v.getXD(), 32),
      FixedPoint.doubleToUnsignedNormalizedLong(v.getYD(), 32),
      FixedPoint.doubleToUnsignedNormalizedLong(v.getZD(), 32),
      FixedPoint.doubleToUnsignedNormalizedLong(v.getWD(), 32));
    this.put4l(this.vector);
  }

  @Override public void put4f(
    final VectorReadable4FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4L(
      FixedPoint.floatToUnsignedNormalizedLong(v.getXF(), 32),
      FixedPoint.floatToUnsignedNormalizedLong(v.getYF(), 32),
      FixedPoint.floatToUnsignedNormalizedLong(v.getZF(), 32),
      FixedPoint.floatToUnsignedNormalizedLong(v.getWF(), 32));
    this.put4l(this.vector);
  }

  @Override public void put4i(
    final VectorReadable4IType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    b.putInt(i + 0, v.getXI());
    b.putInt(i + 4, v.getYI());
    b.putInt(i + 8, v.getZI());
    b.putInt(i + 12, v.getWI());
    this.next();
  }

  @Override public void put4l(
    final VectorReadable4LType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    b.putInt(i + 0, (int) v.getXL());
    b.putInt(i + 4, (int) v.getYL());
    b.putInt(i + 8, (int) v.getZL());
    b.putInt(i + 12, (int) v.getWL());
    this.next();
  }
}
