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
import com.io7m.jcanephora.SpatialCursorWritable1Type;

/**
 * A texture cursor for <code>1_8_I</code> components.
 */

public final class ByteBufferTextureCursorWritable_1_8_I extends
  ByteBufferAreaCursor implements SpatialCursorWritable1Type
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

  public static SpatialCursorWritable1Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorWritable_1_8_I(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorWritable_1_8_I(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 1);
  }

  @Override public void put1d(
    final double x)
  {
    this.put1i(FixedPoint.doubleToSignedNormalized(x, 8));
  }

  @Override public void put1f(
    final float x)
  {
    this.put1i(FixedPoint.floatToSignedNormalized(x, 8));
  }

  @Override public void put1i(
    final int x)
  {
    final int byte_current = (int) this.getByteOffset();
    this.getBuffer().put(byte_current, (byte) x);
    this.next();
  }

  @Override public void put1l(
    final long x)
  {
    final int byte_current = (int) this.getByteOffset();
    this.getBuffer().put(byte_current, (byte) x);
    this.next();
  }
}
