package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with three RGBA elements, with 5 bits
 * for each color channel and 1 bit for alpha.
 */

final class ByteBufferSpatialCursorWritable4i_2_5551 extends AreaCursor implements
  SpatialCursorWritable4i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferSpatialCursorWritable4i_2_5551(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data;
  }

  @Override public void put4i(
    final int r,
    final int g,
    final int b,
    final int a)
    throws ConstraintError
  {
    final char data =
      TexturePixelPack.pack2_5551((byte) r, (byte) g, (byte) b, (byte) a);
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putChar(byte_current, data);
    this.next();
  }
}
