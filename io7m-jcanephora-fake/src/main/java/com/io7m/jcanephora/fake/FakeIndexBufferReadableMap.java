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

package com.io7m.jcanephora.fake;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.CursorReadableIndexType;
import com.io7m.jcanephora.IndexBufferReadMappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.cursors.ByteBufferCursorReadableIndex;
import com.io7m.jnull.NullCheck;

/**
 * Abstraction over mapped index buffers.
 * 
 * Because the type of elements in index buffers are dependent on the range of
 * indices in the buffer, it is difficult to write code that works with index
 * buffers generically. The IndexBufferReadableMap class provides a simple
 * checked "get" API that allows code to avoid knowing about the specific
 * types of the elements and transparently promotes values retrieved from the
 * index buffers to <code>int</code>.
 */

final class FakeIndexBufferReadableMap implements IndexBufferReadMappedType
{
  private final IndexBufferUsableType buffer;
  private final ByteBuffer            map;

  FakeIndexBufferReadableMap(
    final IndexBufferUsableType id,
    final ByteBuffer in_map)
  {
    this.map = NullCheck.notNull(in_map, "Index buffer map");
    this.buffer = NullCheck.notNull(id, "Index buffer");
  }

  @Override public ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  @Override public CursorReadableIndexType getCursor()
  {
    return ByteBufferCursorReadableIndex.newCursor(
      this.map,
      this.buffer.bufferGetRange(),
      this.buffer.indexGetType());
  }

  @Override public IndexBufferUsableType getIndexBuffer()
  {
    return this.buffer;
  }
}
