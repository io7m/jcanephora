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

import javax.annotation.Nonnull;

import com.io7m.jaux.RangeInclusive;

/**
 * A read-only interface to the {@link ArrayBuffer} type that allows use of
 * the type but not mutation and/or deletion of the contents.
 */

public interface IndexBufferUsable
{
  /**
   * Retrieve the size in bytes of each element.
   */

  long getElementSizeBytes();

  /**
   * Retrieve the valid range of elements.
   */

  @Nonnull RangeInclusive getRange();

  /**
   * Retrieve the total size in bytes of the allocated buffer.
   */

  long getSizeBytes();

  /**
   * Retrieve the type of the elements in the buffer.
   */

  @Nonnull GLUnsignedType getType();

}
