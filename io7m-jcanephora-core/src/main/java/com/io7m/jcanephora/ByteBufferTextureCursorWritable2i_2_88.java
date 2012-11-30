package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with two elements, both 8 bits.
 */

final class ByteBufferSpatialCursorWritable2i_2_88 extends AreaCursor implements
  SpatialCursorWritable2i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferSpatialCursorWritable2i_2_88(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data;
  }

  @Override public void put2i(
    final int x,
    final int y)
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    this.target_data.put(byte_current + 0, (byte) (x & 0xFF));
    this.target_data.put(byte_current + 1, (byte) (y & 0xFF));
    this.next();
  }
}
