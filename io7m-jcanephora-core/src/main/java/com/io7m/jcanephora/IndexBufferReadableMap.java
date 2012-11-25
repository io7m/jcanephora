package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

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
   * Retrieve the element at index <code>index</code>.
   */

  public int get(
    final int index)
  {
    switch (this.buffer.getType()) {
      case TYPE_UNSIGNED_BYTE:
      {
        final byte b = this.map.get(index);
        return b & 0xff;
      }
      case TYPE_UNSIGNED_INT:
      {
        return this.map.getInt(index * 4);
      }
      case TYPE_UNSIGNED_SHORT:
      {
        final short s = this.map.getShort(index * 2);
        return s & 0xffff;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the readable <code>ByteBuffer</code> that backs the index
   * buffer.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }
}
