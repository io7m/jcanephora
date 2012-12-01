package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with single 8 bit elements.
 */

final class ByteBufferTextureCursorWritable1i_1_8 extends AreaCursor implements
  SpatialCursorWritable1i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable1i_1_8(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 1);
    this.target_data = target_data;
  }

  @Override public void put1i(
    final int x)
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    this.target_data.put(byte_current + 0, (byte) (x & 0xFF));
    this.next();
  }
}
