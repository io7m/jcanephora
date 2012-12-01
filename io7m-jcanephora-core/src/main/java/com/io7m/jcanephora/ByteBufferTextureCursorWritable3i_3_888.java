package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with three 8 bit RGBA elements.
 */

final class ByteBufferTextureCursorWritable3i_3_888 extends AreaCursor implements
  SpatialCursorWritable3i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable3i_3_888(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3);
    this.target_data = target_data;
  }

  @Override public void put3i(
    final int r,
    final int g,
    final int b)
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    this.target_data.put(byte_current + 0, (byte) (r & 0xff));
    this.target_data.put(byte_current + 1, (byte) (g & 0xff));
    this.target_data.put(byte_current + 2, (byte) (b & 0xff));
    this.next();
  }
}
