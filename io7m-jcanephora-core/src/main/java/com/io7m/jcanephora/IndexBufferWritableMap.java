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
   * Retrieve the writable <code>ByteBuffer</code> that backs the index
   * buffer.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }

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
