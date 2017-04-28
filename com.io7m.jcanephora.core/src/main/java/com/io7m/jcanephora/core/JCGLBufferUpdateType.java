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

import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.immutables.value.Value;

import java.nio.ByteBuffer;

/**
 * An update that will replace data in a buffer.
 *
 * @param <T> The type of buffer
 *
 * @see JCGLBufferUpdates
 */

@JCGLImmutableStyleType
@Value.Immutable
public interface JCGLBufferUpdateType<T extends JCGLBufferWritableType>
  extends JCGLDataUpdateType<T>
{
  /**
   * @return The buffer that will be updated
   */

  @Value.Parameter(order = 0)
  T buffer();

  @Override
  @Value.Parameter(order = 1)
  ByteBuffer data();

  @Override
  @Value.Parameter(order = 2)
  UnsignedRangeInclusiveL dataUpdateRange();
}
