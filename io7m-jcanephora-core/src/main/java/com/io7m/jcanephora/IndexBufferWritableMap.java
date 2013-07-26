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

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * Abstraction over mapped index buffers.
 */

public final class IndexBufferWritableMap
{
  private final @Nonnull IndexBufferUsable buffer;
  private final @Nonnull ByteBuffer        map;
  private final @Nonnull RangeInclusive    target_range;

  IndexBufferWritableMap(
    final @Nonnull IndexBufferUsable id,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.map = Constraints.constrainNotNull(map, "Index buffer map");
    this.buffer = Constraints.constrainNotNull(id, "Index buffer");
    this.target_range =
      new RangeInclusive(0, this.buffer.getRange().getInterval() - 1);
  }

  /**
   * Retrieve the raw ByteBuffer that backs the array buffer. The memory
   * backing the buffer is mapped into the application address space from the
   * GPU. The function is provided for use by developers that have needs not
   * addressed by the cursor API.
   * 
   * Use of this buffer is discouraged for safety reasons.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  /**
   * Retrieve a writable cursor that addresses elements of the index buffer.
   */

  public @Nonnull CursorWritableIndex getCursor()
  {
    return new ByteBufferCursorWritableIndex(
      this.map,
      this.target_range,
      this.buffer.getType());
  }

  /**
   * Retrieve the mapped IndexBuffer.
   */

  public @Nonnull IndexBufferUsable getIndexBuffer()
  {
    return this.buffer;
  }
}
