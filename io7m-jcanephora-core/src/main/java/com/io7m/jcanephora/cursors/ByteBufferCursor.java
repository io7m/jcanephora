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

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An extension of the {@link BufferCursor} type that addresses a specific
 * byte buffer and performs extra validity checks on creation.
 */

public abstract class ByteBufferCursor extends BufferCursor
{
  private final ByteBuffer buffer;

  protected ByteBufferCursor(
    final ByteBuffer in_buffer,
    final RangeInclusiveL in_range,
    final long in_attribute_offset,
    final long in_element_size)
  {
    super(in_range, in_attribute_offset, in_element_size);
    this.buffer = NullCheck.notNull(in_buffer, "Buffer");

    /**
     * Check that the byte buffer is actually large enough to allow access to
     * the uppermost element specified by the range.
     */

    final long max = in_range.getUpper() * in_element_size;
    final long cap = this.buffer.capacity();
    RangeCheck.checkGreaterEqual(
      cap,
      "Buffer capacity",
      max,
      "Range upper bound");
  }

  protected final ByteBuffer getBuffer()
  {
    return this.buffer;
  }
}
