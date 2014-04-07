/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
import com.io7m.jtensors.VectorM3F;

/**
 * Generic byte buffer cursor pointing to elements containing three floats.
 */

final class ByteBufferCursorReadable3f extends BufferCursor implements
  CursorReadable3f
{
  private final @Nonnull ByteBuffer target_data;

  ByteBufferCursorReadable3f(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull RangeInclusive range,
    final long attribute_offset,
    final long element_size)
  {
    super(range, attribute_offset, element_size);
    this.target_data = target_data1;
  }

  /**
   * Retrieve the values at the current cursor location and seek the cursor to
   * the next element iff there is one.
   */

  @Override public void get3f(
    final @Nonnull VectorM3F v)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is within range");
    Constraints.constrainNotNull(v, "Vector");

    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getFloat(byte_current + 0);
    v.y = this.target_data.getFloat(byte_current + 4);
    v.z = this.target_data.getFloat(byte_current + 8);
    this.next();
  }
}
