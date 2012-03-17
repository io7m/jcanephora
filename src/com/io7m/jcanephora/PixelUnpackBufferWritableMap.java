package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Abstraction over mapped pixel unpack buffers.
 */

public final class PixelUnpackBufferWritableMap
{
  private final @Nonnull ByteBuffer   map;
  private final @Nonnull GLScalarType type;
  private final int                   element_size;

  PixelUnpackBufferWritableMap(
    final @Nonnull GLScalarType type,
    final int element_size,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.map = Constraints.constrainNotNull(map, "Byte buffer map");
    this.type = Constraints.constrainNotNull(type, "Element type");
    this.element_size =
      Constraints.constrainRange(element_size, 1, 4, "Element size");

    assert this.type != null;
    assert this.element_size > 0;
  }

  /**
   * Retrieve the writable <code>ByteBuffer</code> that backs the index
   * buffer.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }
}
