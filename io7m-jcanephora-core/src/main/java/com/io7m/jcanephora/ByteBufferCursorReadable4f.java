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

package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.VectorWritable4FType;

/**
 * Generic byte buffer cursor pointing to elements containing four floats.
 */

final class ByteBufferCursorReadable4f extends BufferCursor implements
  CursorReadable4fType
{
  private final ByteBuffer target_data;

  ByteBufferCursorReadable4f(
    final ByteBuffer target_data1,
    final RangeInclusiveL range,
    final long attribute_offset,
    final long element_size)
  {
    super(range, attribute_offset, element_size);
    this.target_data = target_data1;
  }

  @Override public void get4f(
    final VectorWritable4FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.checkValid();

    final int byte_current = (int) this.getByteOffset();
    v.set4F(
      this.target_data.getFloat(byte_current + 0),
      this.target_data.getFloat(byte_current + 4),
      this.target_data.getFloat(byte_current + 8),
      this.target_data.getFloat(byte_current + 12));
    this.next();
  }
}
