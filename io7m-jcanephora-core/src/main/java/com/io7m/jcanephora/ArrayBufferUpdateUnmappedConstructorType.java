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

import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

/**
 * The type of unmapped array buffer update constructors.
 */

public interface ArrayBufferUpdateUnmappedConstructorType
{
  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>buffer</code> on the GPU.
   *
   * @param array
   *          The array buffer.
   * @return An array update.
   * @throws JCGLExceptionDeleted
   *           If the array is deleted.
   */

  ArrayBufferUpdateUnmappedType newUpdateReplacingAll(
    final ArrayBufferType array)
    throws JCGLExceptionDeleted;

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * range <code>range</code> of the data in <code>buffer</code> on the GPU.
   *
   * @return An array update.
   * @param array
   *          The array buffer.
   * @param range
   *          The range of elements to replace.
   * @throws RangeCheckException
   *           If the given range is not included in the buffer's range.
   * @throws JCGLExceptionDeleted
   *           If the array is deleted.
   */

  ArrayBufferUpdateUnmappedType newUpdateReplacingRange(
    final ArrayBufferType array,
    final RangeInclusiveL range)
    throws RangeCheckException,
      JCGLExceptionDeleted;
}
