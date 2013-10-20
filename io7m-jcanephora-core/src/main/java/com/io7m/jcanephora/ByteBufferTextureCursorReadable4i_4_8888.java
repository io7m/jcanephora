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
 * Texture cursor addressing textures with four 8 bit RGBA elements.
 */

final class ByteBufferTextureCursorReadable4i_4_8888 extends AreaCursor implements
  SpatialCursorReadable4i
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable4i_4_8888(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4);
    this.target_data = target_data;
  }

  @Override public void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");

    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.get(byte_current + 0);
    v.y = this.target_data.get(byte_current + 1);
    v.z = this.target_data.get(byte_current + 2);
    v.w = this.target_data.get(byte_current + 3);
    this.next();
  }
}
