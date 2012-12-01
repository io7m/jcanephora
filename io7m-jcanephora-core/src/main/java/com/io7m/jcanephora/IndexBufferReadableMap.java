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
 * buffers generically. The IndexBufferReadableMap class provides a simple
 * checked "get" API that allows code to avoid knowing about the specific
 * types of the elements and transparently promotes values retrieved from the
 * index buffers to <code>int</code>.
 */

public final class IndexBufferReadableMap
{
  private final @Nonnull IndexBuffer buffer;
  private final @Nonnull ByteBuffer  map;

  IndexBufferReadableMap(
    final @Nonnull IndexBuffer id,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.map = Constraints.constrainNotNull(map, "Index buffer map");
    this.buffer = Constraints.constrainNotNull(id, "Index buffer");
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
   * Retrieve a cursor that points to elements of the index buffer. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   */

  public @Nonnull CursorReadableIndex getCursor()
  {
    return new ByteBufferCursorReadableIndex(
      this.map,
      this.buffer.getRange(),
      this.buffer.getType());
  }
}
