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
import com.io7m.jcanephora.SpatialCursorReadable1Type;

/**
 * A texture cursor for <code>1_24_UNFP</code> components.
 */

public final class ByteBufferTextureCursorReadable_1_24_UNFP extends
  ByteBufferAreaCursor implements SpatialCursorReadable1Type
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

  public static SpatialCursorReadable1Type newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorReadable_1_24_UNFP(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorReadable_1_24_UNFP(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    // Note 4-byte alignment
    super(in_target_data, target_area, update_area, 4);
  }

  @Override public double get1d()
  {
    final int x = this.get1i();
    return FixedPoint.unsignedNormalizedFixedToDouble(24, x);
  }

  @Override public float get1f()
  {
    final int x = this.get1i();
    return FixedPoint.unsignedNormalizedFixedToFloat(24, x);
  }

  @Override public int get1i()
  {
    final int byte_current = (int) this.getByteOffset();
    final int x = this.getBuffer().getInt(byte_current);
    this.next();
    return x;
  }

  @Override public long get1l()
  {
    return this.get1i();
  }
}
