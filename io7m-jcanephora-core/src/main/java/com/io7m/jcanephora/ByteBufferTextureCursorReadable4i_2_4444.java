/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorM4I;

/**
 * Texture cursor addressing textures with four 4 bit RGBA elements.
 */

final class ByteBufferTextureCursorReadable4i_2_4444 extends AreaCursor implements
  SpatialCursorReadable4i
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull int[]      buffer = new int[4];

  protected ByteBufferTextureCursorReadable4i_2_4444(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2);
    this.target_data = target_data;
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");

    final int byte_current = (int) this.getByteOffset();
    final char r = this.target_data.getChar(byte_current);
    TexturePixelPack.unpack2_4444(r, this.buffer);
    v.x = this.buffer[0];
    v.y = this.buffer[1];
    v.z = this.buffer[2];
    v.w = this.buffer[3];
    this.next();
  }
}
