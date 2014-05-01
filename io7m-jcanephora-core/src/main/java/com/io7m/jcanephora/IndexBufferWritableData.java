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
import java.nio.ByteOrder;

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An allocated region of data, to replace or update an index buffer.
 */

public final class IndexBufferWritableData
{
  private final IndexBufferUsableType buffer;
  private final RangeInclusiveL       range;
  private final ByteBuffer            target_data;
  private final long                  target_data_offset;
  private final long                  target_data_size;
  private final RangeInclusiveL       target_range;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>buffer</code> on the GPU.
   * 
   * @param in_buffer
   *          The array buffer.
   */

  public IndexBufferWritableData(
    final IndexBuffer in_buffer)
  {
    this(in_buffer, in_buffer.bufferGetRange());
  }

  /**
   * Construct a buffer of data that will be used to replace the range of
   * elements given by <code>range</code> in <code>buffer</code> on the GPU.
   * 
   * @param in_buffer
   *          The index buffer.
   * @param in_range
   *          The range of elements to replace.
   * @throws IllegalArgumentException
   *           If the given range is not included in the buffer's range.
   */

  public IndexBufferWritableData(
    final IndexBuffer in_buffer,
    final RangeInclusiveL in_range)
    throws IllegalArgumentException
  {
    this.buffer = NullCheck.notNull(in_buffer, "Array buffer");
    this.range = NullCheck.notNull(in_range, "Range");

    if (in_range.isIncludedIn(in_buffer.bufferGetRange())) {
      throw new IllegalArgumentException(
        "Given range is not included in the buffer's range");
    }

    this.target_range = new RangeInclusiveL(0, in_range.getInterval() - 1);
    this.target_data_size =
      in_range.getInterval() * in_buffer.bufferGetElementSizeBytes();
    this.target_data_offset =
      in_range.getLower() * in_buffer.bufferGetElementSizeBytes();

    final ByteBuffer b =
      ByteBuffer.allocateDirect((int) this.target_data_size);
    b.order(ByteOrder.nativeOrder());

    this.target_data = b;
  }

  /**
   * @return A cursor that points to elements of the index buffer. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   */

  public CursorWritableIndexType getCursor()
  {
    return new ByteBufferCursorWritableIndex(
      this.target_data,
      this.target_range,
      this.buffer.getType());
  }

  /**
   * @return The index buffer to which this data belongs.
   */

  public IndexBufferUsableType getIndexBuffer()
  {
    return this.buffer;
  }

  /**
   * @return The data that will be used to update the array buffer.
   */

  ByteBuffer getTargetData()
  {
    return this.target_data;
  }

  /**
   * @return The offset in bytes of the area of the array buffer to be
   *         updated.
   */

  long getTargetDataOffset()
  {
    return this.target_data_offset;
  }

  /**
   * @return The size in bytes of the area of the array buffer to be updated.
   */

  long getTargetDataSize()
  {
    return this.target_data_size;
  }
}
