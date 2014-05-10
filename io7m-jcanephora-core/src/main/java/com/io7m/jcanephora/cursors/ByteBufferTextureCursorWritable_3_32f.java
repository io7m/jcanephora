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
import com.io7m.jcanephora.SpatialCursorWritable3FloatType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable3DType;
import com.io7m.jtensors.VectorReadable3FType;

/**
 * A texture cursor for <code>3_32f</code> components.
 */

public final class ByteBufferTextureCursorWritable_3_32f extends
  ByteBufferAreaCursor implements SpatialCursorWritable3FloatType
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

  public static SpatialCursorWritable3FloatType newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_3_32f(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorWritable_3_32f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 3 * 4);
  }

  @Override public void put3d(
    final VectorReadable3DType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int byte_current = (int) this.getByteOffset();
    b.putFloat(byte_current + 0, (float) v.getXD());
    b.putFloat(byte_current + 4, (float) v.getYD());
    b.putFloat(byte_current + 8, (float) v.getZD());
    this.next();
  }

  @Override public void put3f(
    final VectorReadable3FType v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int byte_current = (int) this.getByteOffset();
    b.putFloat(byte_current + 0, v.getXF());
    b.putFloat(byte_current + 4, v.getYF());
    b.putFloat(byte_current + 8, v.getZF());
    this.next();
  }
}