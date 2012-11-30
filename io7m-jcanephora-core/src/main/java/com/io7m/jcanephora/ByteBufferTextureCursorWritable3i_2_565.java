package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with three RGBA elements, with 5 bits
 * for red, 6 bits for green, and 5 bits for blue.
 */

final class ByteBufferSpatialCursorWritable3i_2_565 extends AreaCursor implements
  SpatialCursorWritable3i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferSpatialCursorWritable3i_2_565(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data;
  }

  @Override public void put3i(
    final int r,
    final int g,
    final int b)
    throws ConstraintError
  {
    final char data = TexturePixelPack.pack2_565(r, g, b);
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putChar(byte_current, data);
    this.next();
  }
}
