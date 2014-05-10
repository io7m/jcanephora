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

import com.io7m.jcanephora.cursors.ByteBufferCursorWritableIndex;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An allocated region of data, to replace or update an index buffer.
 */

public final class IndexBufferUpdateUnmapped implements
  IndexBufferUpdateUnmappedType
{
  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>b</code> on the GPU.
   * 
   * @return An index buffer update.
   * @param b
   *          The index buffer.
   */

  public static IndexBufferUpdateUnmappedType newReplacing(
    final IndexBufferType b)
  {
    NullCheck.notNull(b, "Buffer");
    return new IndexBufferUpdateUnmapped(b, b.bufferGetRange());
  }

  /**
   * Construct a buffer of data that will be used to replace the range of
   * elements given by <code>r</code> in <code>b</code> on the GPU.
   * 
   * @return An index buffer update.
   * @param b
   *          The index buffer.
   * @param r
   *          The range of elements to replace.
   * @throws RangeCheckException
   *           If the given range is not included in the buffer's range.
   */

  public static IndexBufferUpdateUnmappedType newUpdating(
    final IndexBufferType b,
    final RangeInclusiveL r)
    throws RangeCheckException
  {
    return new IndexBufferUpdateUnmapped(b, r);
  }

  private final IndexBufferUsableType buffer;
  private final RangeInclusiveL       range;
  private final ByteBuffer            target_data;
  private final long                  target_data_offset;
  private final long                  target_data_size;
  private final RangeInclusiveL       target_range;

  private IndexBufferUpdateUnmapped(
    final IndexBufferType in_buffer,
    final RangeInclusiveL in_range)
    throws RangeCheckException
  {
    this.buffer = NullCheck.notNull(in_buffer, "Array buffer");
    this.range = NullCheck.notNull(in_range, "Range");

    RangeCheck.checkRangeIncludedIn(
      in_range,
      "Target range",
      in_buffer.bufferGetRange(),
      "Buffer range");

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

  @Override public CursorWritableIndexType getCursor()
  {
    return ByteBufferCursorWritableIndex.newCursor(
      this.target_data,
      this.target_range,
      this.buffer.indexGetType());
  }

  @Override public IndexBufferUsableType getIndexBuffer()
  {
    return this.buffer;
  }

  @Override public ByteBuffer getTargetData()
  {
    return this.target_data;
  }

  @Override public long getTargetDataOffset()
  {
    return this.target_data_offset;
  }

  @Override public long getTargetDataSize()
  {
    return this.target_data_size;
  }
}
