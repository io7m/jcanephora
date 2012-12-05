/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * Generic byte buffer cursor pointing to elements containing four floats.
 */

final class ByteBufferCursorWritable4f extends BufferCursor implements
  CursorWritable4f
{
  private final @Nonnull ByteBuffer target_data;

  ByteBufferCursorWritable4f(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull RangeInclusive range,
    final long attribute_offset,
    final long element_size)
  {
    super(range, attribute_offset, element_size);
    this.target_data = target_data;
  }

  /**
   * Put the values <code>x, y, z, w</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  @Override public void put4f(
    final float x,
    final float y,
    final float z,
    final float w)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.canWrite(), "Cursor is within range");

    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, x);
    this.target_data.putFloat(byte_current + 4, y);
    this.target_data.putFloat(byte_current + 8, z);
    this.target_data.putFloat(byte_current + 12, w);
    this.next();
  }
}