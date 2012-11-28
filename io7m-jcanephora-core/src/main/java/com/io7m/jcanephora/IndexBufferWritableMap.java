package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Abstraction over mapped index buffers.
 * 
 * Because the type of elements in index buffers are dependent on the range of
 * indices in the buffer, it is difficult to write code that works with index
 * buffers generically. The IndexBufferWritableMap class provides a simple
 * checked "put" API that allows code to avoid knowing about the specific
 * types of the elements but also asserts that index values placed in the
 * buffer are representable by the types of the elements.
 */

public final class IndexBufferWritableMap
{
  private final @Nonnull IndexBuffer buffer;
  private final @Nonnull ByteBuffer  map;

  IndexBufferWritableMap(
    final @Nonnull IndexBuffer id,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.map = Constraints.constrainNotNull(map, "Index buffer map");
    this.buffer = Constraints.constrainNotNull(id, "Index buffer");
  }

  /**
   * Retrieve the writable <code>ByteBuffer</code> that backs the index
   * buffer.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  /**
   * Retrieve the mapped IndexBuffer.
   */

  public @Nonnull IndexBuffer getIndexBuffer()
  {
    return this.buffer;
  }

  public @Nonnull CursorWritableIndex getCursor()
  {
    return new ByteBufferCursorWritableIndex(
      this.map,
      0,
      this.buffer.getElements() - 1,
      this.buffer.getType());
  }
}
