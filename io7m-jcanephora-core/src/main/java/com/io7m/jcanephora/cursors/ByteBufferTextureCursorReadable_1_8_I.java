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

package com.io7m.jcanephora.cursors;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FixedPoint;
import com.io7m.jcanephora.SpatialCursorReadable1dType;
import com.io7m.jcanephora.SpatialCursorReadable1fType;
import com.io7m.jcanephora.SpatialCursorReadable1iType;

final class ByteBufferTextureCursorReadable_1_8_I extends AreaCursor implements
  SpatialCursorReadable1fType,
  SpatialCursorReadable1dType,
  SpatialCursorReadable1iType
{
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable_1_8_I(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 1);
    this.target_data = in_target_data;
  }

  @Override public double get1d()
  {
    return this.get1f();
  }

  @Override public float get1f()
  {
    final int z = this.get1i();
    return FixedPoint.signedNormalizedFixedToFloat(8, z);
  }

  @Override public int get1i()
  {
    final int byte_current = (int) this.getByteOffset();
    final int x = this.target_data.get(byte_current);
    this.next();
    return x;
  }
}
