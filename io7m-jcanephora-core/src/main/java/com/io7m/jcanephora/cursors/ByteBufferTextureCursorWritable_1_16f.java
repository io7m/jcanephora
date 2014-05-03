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

import com.io7m.ieee754b16.Binary16;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.SpatialCursorWritable1FloatType;

/**
 * A texture cursor for <code>1_16f</code> components.
 */

public final class ByteBufferTextureCursorWritable_1_16f extends
  ByteBufferAreaCursor implements SpatialCursorWritable1FloatType
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

  public static SpatialCursorWritable1FloatType newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_1_16f(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorWritable_1_16f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 1 * 2);
  }

  @Override public void put1d(
    final double x)
  {
    final int i = (int) this.getByteOffset();
    this.getBuffer().putChar(i, Binary16.packDouble(x));
    this.next();
  }

  @Override public void put1f(
    final float x)
  {
    final int i = (int) this.getByteOffset();
    this.getBuffer().putChar(i, Binary16.packFloat(x));
    this.next();
  }
}
