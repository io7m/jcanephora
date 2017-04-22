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
import com.io7m.jranges.RangeCheckException;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.junsigned.ranges.UnsignedRangeCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;

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
    NullCheck.notNull(buffer, "Buffer");
    return newUpdateReplacingRange(buffer, buffer.byteRange());
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
   *
   * @throws RangeCheckException Iff {@code range} is not included in the
   *                             buffer's range
   */

  public static <T extends JCGLBufferWritableType> JCGLBufferUpdateType<T>
  newUpdateReplacingRange(
    final T buffer,
    final UnsignedRangeInclusiveL range)
    throws RangeCheckException
  {
    NullCheck.notNull(buffer, "Buffer");
    NullCheck.notNull(range, "Range");

    final UnsignedRangeInclusiveL buffer_range = buffer.byteRange();
    UnsignedRangeCheck.checkRangeIncludedInLong(
      range, "Update range", buffer_range, "Buffer range");

    final ByteBuffer data =
      ByteBuffer.allocateDirect((int) range.getInterval());
    data.order(ByteOrder.nativeOrder());
    return new Update<>(buffer, data, range);
  }

  private static final class Update<T extends JCGLBufferWritableType>
    implements JCGLBufferUpdateType<T>
  {
    private final T buffer;
    private final ByteBuffer data;
    private final UnsignedRangeInclusiveL range;

    Update(
      final T in_buffer,
      final ByteBuffer in_data,
      final UnsignedRangeInclusiveL in_range)
    {
      this.buffer = NullCheck.notNull(in_buffer, "Buffer");
      this.data = NullCheck.notNull(in_data, "Data");
      this.range = NullCheck.notNull(in_range, "Range");
    }

    @Override
    public T buffer()
    {
      return this.buffer;
    }

    @Override
    public ByteBuffer data()
    {
      return this.data;
    }

    @Override
    public UnsignedRangeInclusiveL dataUpdateRange()
    {
      return this.range;
    }
  }
}