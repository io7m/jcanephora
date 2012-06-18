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
  private final @Nonnull PixelUnpackBuffer buffer;
  private final @Nonnull ByteBuffer        map;

  PixelUnpackBufferWritableMap(
    final @Nonnull PixelUnpackBuffer buffer,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Pixel unpack buffer");
    this.map = Constraints.constrainNotNull(map, "Byte buffer map");
  }

  /**
   * Retrieve the writable <code>ByteBuffer</code> that backs the mapped
   * buffer.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  /**
   * Retrieve a cursor that may only point to each Vector4b element in the
   * buffer.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>getElements() != 4</code></li>
   *           <li>
   *           <code>getType() != TYPE_BYTE && getType() != TYPE_UNSIGNED_BYTE</code>
   *           </li>
   *           </ul>
   */

  public @Nonnull PixelUnpackBufferCursorWritable4b getCursor4b()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.buffer.getElementValues() == 4,
      "Element type is Vector4b");
    Constraints.constrainArbitrary(
      (this.buffer.getElementType() == GLScalarType.TYPE_BYTE)
        || (this.buffer.getElementType() == GLScalarType.TYPE_UNSIGNED_BYTE),
      "Element type is Vector4b");

    return new PixelUnpackBufferCursorWritable4b(this);
  }

  /**
   * Retrieve the pixel unpack buffer for this map.
   */

  public @Nonnull PixelUnpackBuffer getPixelUnpackBuffer()
  {
    return this.buffer;
  }
}
