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

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Abstraction over mapped index buffers.
 */

public final class IndexBufferWritableMap
{
  private final IndexBufferUsableType buffer;
  private final ByteBuffer            map;
  private final RangeInclusiveL       target_range;

  IndexBufferWritableMap(
    final IndexBufferUsableType id,
    final ByteBuffer map1)
  {
    this.map = NullCheck.notNull(map1, "Index buffer map");
    this.buffer = NullCheck.notNull(id, "Index buffer");
    this.target_range =
      new RangeInclusiveL(0, this.buffer.bufferGetRange().getInterval() - 1);
  }

  /**
   * @return <p>
   *         The raw ByteBuffer that backs the array buffer. The memory
   *         backing the buffer is mapped into the application address space
   *         from the GPU. The function is provided for use by developers that
   *         have needs not addressed by the cursor API.
   *         </p>
   *         <p>
   *         Use of this buffer is discouraged for safety reasons.
   *         </p>
   */

  public ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  /**
   * @return A writable cursor that addresses elements of the index buffer.
   */

  public CursorWritableIndexType getCursor()
  {
    return new ByteBufferCursorWritableIndex(
      this.map,
      this.target_range,
      this.buffer.getType());
  }

  /**
   * @return The mapped IndexBuffer.
   */

  public IndexBufferUsableType getIndexBuffer()
  {
    return this.buffer;
  }
}
