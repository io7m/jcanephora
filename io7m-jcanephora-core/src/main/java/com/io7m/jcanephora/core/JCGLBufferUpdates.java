/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility functions to allocate buffer updates.
 */

public final class JCGLBufferUpdates
{
  private JCGLBufferUpdates()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Construct an update that will replace all of the data in {@code buffer}.
   *
   * @param buffer The buffer
   * @param <T>    The precise type of buffer
   *
   * @return An update
   */

  public static <T extends JCGLBufferWritableType> JCGLBufferUpdateType<T>
  newUpdateReplacingAll(final T buffer)
  {
    NullCheck.notNull(buffer);
    return JCGLBufferUpdates.newUpdateReplacingRange(buffer, buffer.getRange());
  }

  /**
   * Construct an update that will replace the range of bytes given by {@code
   * range} in {@code buffer}.
   *
   * @param buffer The buffer
   * @param range  The range
   * @param <T>    The precise type of buffer
   *
   * @return An update
   */

  public static <T extends JCGLBufferWritableType> JCGLBufferUpdateType<T>
  newUpdateReplacingRange(
    final T buffer,
    final RangeInclusiveL range)
  {
    NullCheck.notNull(buffer);
    NullCheck.notNull(range);

    final RangeInclusiveL buffer_range = buffer.getRange();
    RangeCheck.checkRangeIncludedInLong(
      range, "Update range", buffer_range, "Buffer range");

    final ByteBuffer data =
      ByteBuffer.allocateDirect((int) range.getInterval());
    data.order(ByteOrder.nativeOrder());
    return new Update<>(buffer, data, range);
  }

  private static final class Update<T extends JCGLBufferWritableType>
    implements JCGLBufferUpdateType<T>
  {
    private final T               buffer;
    private final ByteBuffer      data;
    private final RangeInclusiveL range;

    Update(
      final T in_buffer,
      final ByteBuffer in_data,
      final RangeInclusiveL in_range)
    {
      this.buffer = NullCheck.notNull(in_buffer);
      this.data = NullCheck.notNull(in_data);
      this.range = NullCheck.notNull(in_range);
    }

    @Override public T getBuffer()
    {
      return this.buffer;
    }

    @Override public ByteBuffer getData()
    {
      return this.data;
    }

    @Override public RangeInclusiveL getBufferUpdateRange()
    {
      return this.range;
    }
  }
}
