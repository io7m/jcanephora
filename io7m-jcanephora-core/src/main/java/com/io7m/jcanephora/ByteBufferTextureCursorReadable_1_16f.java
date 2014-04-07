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

import com.io7m.ieee754b16.Binary16;
import com.io7m.jaux.Constraints.ConstraintError;

final class ByteBufferTextureCursorReadable_1_16f extends AreaCursor implements
  SpatialCursorReadable1f,
  SpatialCursorReadable1d
{
  private final @Nonnull byte[]     buffer = new byte[2];
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable_1_16f(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 1 * 2);
    this.target_data = target_data1;
  }

  @Override public double get1d()
    throws ConstraintError
  {
    final int i = (int) this.getByteOffset();
    final byte[] b = this.buffer;

    final int x =
      ByteBufferCursor.unpackInteger16(this.target_data, b, i + 0);

    this.next();
    return Binary16.unpackDouble((char) x);
  }

  @Override public float get1f()
    throws ConstraintError
  {
    final int i = (int) this.getByteOffset();
    final byte[] b = this.buffer;

    final int x =
      ByteBufferCursor.unpackInteger16(this.target_data, b, i + 0);

    this.next();
    return Binary16.unpackFloat((char) x);
  }
}
