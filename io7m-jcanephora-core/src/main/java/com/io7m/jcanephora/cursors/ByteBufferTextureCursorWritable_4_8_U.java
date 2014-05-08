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
import com.io7m.jtensors.VectorM4I;
import com.io7m.jtensors.VectorReadable4DType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.io7m.jtensors.VectorReadable4LType;

/**
 * A texture cursor for <code>4_8_U</code> components.
 */

public final class ByteBufferTextureCursorWritable_4_8_U extends
  ByteBufferAreaCursor implements SpatialCursorWritable4Type
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

  public static SpatialCursorWritable4Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_4_8_U(
      in_target_data,
      target_area,
      update_area);
  }

  private final VectorM4I vector = new VectorM4I();

  private ByteBufferTextureCursorWritable_4_8_U(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 4 * 1);
  }

  @Override public void put4d(
    final VectorReadable4DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4I(
      FixedPoint.doubleToUnsignedNormalized(v.getXD(), 8),
      FixedPoint.doubleToUnsignedNormalized(v.getYD(), 8),
      FixedPoint.doubleToUnsignedNormalized(v.getZD(), 8),
      FixedPoint.doubleToUnsignedNormalized(v.getWD(), 8));
    this.put4i(this.vector);
  }

  @Override public void put4f(
    final VectorReadable4FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4I(
      FixedPoint.floatToUnsignedNormalized(v.getXF(), 8),
      FixedPoint.floatToUnsignedNormalized(v.getYF(), 8),
      FixedPoint.floatToUnsignedNormalized(v.getZF(), 8),
      FixedPoint.floatToUnsignedNormalized(v.getWF(), 8));
    this.put4i(this.vector);
  }

  @Override public void put4i(
    final VectorReadable4IType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    b.put(i + 0, (byte) v.getXI());
    b.put(i + 1, (byte) v.getYI());
    b.put(i + 2, (byte) v.getZI());
    b.put(i + 3, (byte) v.getWI());
    this.next();
  }

  @Override public void put4l(
    final VectorReadable4LType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    b.put(i + 0, (byte) v.getXL());
    b.put(i + 1, (byte) v.getYL());
    b.put(i + 2, (byte) v.getZL());
    b.put(i + 3, (byte) v.getWL());
    this.next();
  }
}
