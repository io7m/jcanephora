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

import com.io7m.jcanephora.CursorWritable3fType;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Generic byte buffer cursor pointing to elements containing three floats.
 */

public final class ByteBufferCursorWritable3f extends ByteBufferCursor implements
  CursorWritable3fType
{
  /**
   * Construct a new cursor.
   * 
   * @param in_target_data
   *          The data.
   * @param range
   *          The range to be modified.
   * @param attribute_offset
   *          The attribute offset.
   * @param element_size
   *          The element size.
   * @return A new cursor.
   */

  public static CursorWritable3fType newCursor(
    final ByteBuffer in_target_data,
    final RangeInclusiveL range,
    final long attribute_offset,
    final long element_size)
  {
    return new ByteBufferCursorWritable3f(
      in_target_data,
      range,
      attribute_offset,
      element_size);
  }

  private ByteBufferCursorWritable3f(
    final ByteBuffer in_target_data,
    final RangeInclusiveL range,
    final long attribute_offset,
    final long element_size)
  {
    super(in_target_data, range, attribute_offset, element_size);
  }

  @Override public void put3f(
    final float x,
    final float y,
    final float z)
  {
    this.checkValid();
    final ByteBuffer b = this.getBuffer();
    final int byte_current = (int) this.getByteOffset();
    b.putFloat(byte_current + 0, x);
    b.putFloat(byte_current + 4, y);
    b.putFloat(byte_current + 8, z);
    this.next();
  }
}
