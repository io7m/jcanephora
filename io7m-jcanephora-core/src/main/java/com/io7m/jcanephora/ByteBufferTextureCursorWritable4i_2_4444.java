/*
 * Copyright © 2012 http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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
