package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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

  /**
   * Write the value <code>value</code> at index <code>index</code>.
   * 
   * The value will be silently truncated to fit into the type of elements in
   * the index buffer.
   */

  public void put(
    final int index,
    final int value)
  {
    switch (this.buffer.getType()) {
      case TYPE_UNSIGNED_BYTE:
      {
        final byte b = (byte) value;
        this.map.put(index, b);
        return;
      }
      case TYPE_UNSIGNED_INT:
      {
        final IntBuffer ib = this.map.asIntBuffer();
        ib.put(index, value);
        return;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        final ShortBuffer sb = this.map.asShortBuffer();
        sb.put(index, (short) value);
        return;
      }
    }

    throw new AssertionError("unreachable code: report this bug!");
  }
}
