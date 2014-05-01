/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

final class ByteBufferTextureCursorReadable_1_16_I extends AreaCursor implements
  SpatialCursorReadable1f,
  SpatialCursorReadable1d,
  SpatialCursorReadable1i
{
  private final byte[]     buffer = new byte[2];
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable_1_16_I(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 2);
    this.target_data = in_target_data;
  }

  @Override public double get1d()
  {
    return this.get1f();
  }

  @Override public float get1f()
  {
    final int x = this.get1i();
    return FixedPoint.signedNormalizedFixedToFloat(16, x);
  }

  @Override public int get1i()
  {
    final int i = (int) this.getByteOffset();
    final byte[] b = this.buffer;
    final int x =
      (short) ByteBufferCursor.unpackInteger16(this.target_data, b, i);
    this.next();
    return x;
  }
}
