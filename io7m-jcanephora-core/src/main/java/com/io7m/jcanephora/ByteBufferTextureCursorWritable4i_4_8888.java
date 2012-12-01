package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with four 8 bit RGBA elements.
 */

final class ByteBufferTextureCursorWritable4i_4_8888 extends AreaCursor implements
  SpatialCursorWritable4i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable4i_4_8888(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4);
    this.target_data = target_data;
  }

  @Override public void put4i(
    final int r,
    final int g,
    final int b,
    final int a)
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    this.target_data.put(byte_current + 0, (byte) (r & 0xff));
    this.target_data.put(byte_current + 1, (byte) (g & 0xff));
    this.target_data.put(byte_current + 2, (byte) (b & 0xff));
    this.target_data.put(byte_current + 3, (byte) (a & 0xff));
    this.next();
  }
}
