package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Texture cursor addressing textures with four 4 bit RGBA elements.
 */

final class ByteBufferTextureCursorWritable4i_2_4444 extends AreaCursor implements
  SpatialCursorWritable4i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable4i_2_4444(
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
    final int byte_current = (int) this.getByteOffset();
    final char data = TexturePixelPack.pack2_4444(r, g, b, a);
    this.target_data.putChar(byte_current, data);
    this.next();
  }
}
