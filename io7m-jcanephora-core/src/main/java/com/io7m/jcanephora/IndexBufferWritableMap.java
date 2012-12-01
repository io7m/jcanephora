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
  private final @Nonnull IndexBuffer    buffer;
  private final @Nonnull ByteBuffer     map;
  private final @Nonnull RangeInclusive target_range;

  IndexBufferWritableMap(
    final @Nonnull IndexBuffer id,
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

  public @Nonnull IndexBuffer getIndexBuffer()
  {
    return this.buffer;
  }
}
