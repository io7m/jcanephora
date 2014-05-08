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

package com.io7m.jcanephora.jogl;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.IndexBufferUpdateMappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritableIndex;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Abstraction over mapped index buffers.
 */

final class JOGLIndexBufferUpdateMapped implements
  IndexBufferUpdateMappedType
{
  private final IndexBufferUsableType buffer;
  private final ByteBuffer            map;
  private final RangeInclusiveL       target_range;

  JOGLIndexBufferUpdateMapped(
    final IndexBufferUsableType id,
    final ByteBuffer in_map)
  {
    this.map = NullCheck.notNull(in_map, "Index buffer map");
    this.buffer = NullCheck.notNull(id, "Index buffer");
    this.target_range =
      new RangeInclusiveL(0, this.buffer.bufferGetRange().getInterval() - 1);
  }

  @Override public ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  @Override public CursorWritableIndexType getCursor()
  {
    return ByteBufferCursorWritableIndex.newCursor(
      this.map,
      this.target_range,
      this.buffer.indexGetType());
  }

  @Override public IndexBufferUsableType getIndexBuffer()
  {
    return this.buffer;
  }
}
