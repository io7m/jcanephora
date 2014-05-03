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
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An extension of the {@link AreaCursor} type that addresses a specific byte
 * buffer and performs extra validity checks on creation.
 */

public abstract class ByteBufferAreaCursor extends AreaCursor
{
  private final ByteBuffer buffer;

  protected ByteBufferAreaCursor(
    final ByteBuffer in_data,
    final AreaInclusive in_area_outer,
    final AreaInclusive in_area_inner,
    final long in_element_bytes)
  {
    super(in_area_outer, in_area_inner, in_element_bytes);
    this.buffer = NullCheck.notNull(in_data, "Buffer");

    final RangeInclusiveL range_x = in_area_outer.getRangeX();
    final RangeInclusiveL range_y = in_area_outer.getRangeY();

    /**
     * Check that the offset in bytes of the highest (x, y) value can actually
     * fit into the buffer.
     */

    final long row_bytes = (range_x.getUpper() + 1) * in_element_bytes;
    final long max = row_bytes * (range_y.getUpper() + 1);
    final long cap = in_data.capacity();

    RangeCheck.checkGreaterEqual(cap, "Capacity", max, "Required area bytes");
  }

  protected final ByteBuffer getBuffer()
  {
    return this.buffer;
  }
}
