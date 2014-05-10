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
import com.io7m.jcanephora.SpatialCursorReadable3FloatType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;

/**
 * A texture cursor for <code>3_16f</code> components.
 */

public final class ByteBufferTextureCursorReadable_3_16f extends
  ByteBufferAreaCursor implements SpatialCursorReadable3FloatType
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

  public static SpatialCursorReadable3FloatType newCursor(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    return new ByteBufferTextureCursorReadable_3_16f(
      in_target_data,
      target_area,
      update_area);
  }

  private ByteBufferTextureCursorReadable_3_16f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(in_target_data, target_area, update_area, 3 * 2);
  }

  @Override public void get3d(
    final VectorM3D v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    final char x = b.getChar(i + 0);
    final char y = b.getChar(i + 2);
    final char z = b.getChar(i + 4);
    v.set3D(
      Binary16.unpackDouble(x),
      Binary16.unpackDouble(y),
      Binary16.unpackDouble(z));
    this.next();
  }

  @Override public void get3f(
    final VectorM3F v)
  {
    NullCheck.notNull(v, "Vector");
    final ByteBuffer b = this.getBuffer();
    final int i = (int) this.getByteOffset();
    final char x = b.getChar(i + 0);
    final char y = b.getChar(i + 2);
    final char z = b.getChar(i + 4);
    v.set3F(
      Binary16.unpackFloat(x),
      Binary16.unpackFloat(y),
      Binary16.unpackFloat(z));
    this.next();
  }
}