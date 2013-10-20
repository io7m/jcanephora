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
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.Integer24;

/**
 * Texture cursor addressing textures with single 24 bit integer elements.
 */

final class ByteBufferTextureCursorReadable1i_3_24 extends AreaCursor implements
  SpatialCursorReadable1i
{
  private final @Nonnull ByteBuffer target_data;
  private final byte[]              buffer = new byte[3];

  protected ByteBufferTextureCursorReadable1i_3_24(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3);
    this.target_data = target_data;
  }

  @Override public int get1i()
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();

    this.buffer[0] = (byte) (this.target_data.get(byte_current + 0) & 0xff);
    this.buffer[1] = (byte) (this.target_data.get(byte_current + 1) & 0xff);
    this.buffer[2] = (byte) (this.target_data.get(byte_current + 2) & 0xff);

    final int x;
    if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
      x = Integer24.unpackLittleEndian(this.buffer);
    } else {
      x = Integer24.unpackBigEndian(this.buffer);
    }

    this.next();
    return x;
  }
}
