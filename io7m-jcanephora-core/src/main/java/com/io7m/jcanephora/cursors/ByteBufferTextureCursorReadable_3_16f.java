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

import com.io7m.ieee754b16.Binary16;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.SpatialCursorReadable3dType;
import com.io7m.jcanephora.SpatialCursorReadable3fType;
import com.io7m.jintegers.Integer16;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;

final class ByteBufferTextureCursorReadable_3_16f extends AreaCursor implements
  SpatialCursorReadable3fType,
  SpatialCursorReadable3dType
{
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable_3_16f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 3 * 2);
    this.target_data = in_target_data;
  }

  @Override public void get3d(
    final VectorM3D v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final int x = Integer16.unpackFromBuffer(this.target_data, i + 0);
    final int y = Integer16.unpackFromBuffer(this.target_data, i + 2);
    final int z = Integer16.unpackFromBuffer(this.target_data, i + 4);

    v.set3D(
      Binary16.unpackDouble((char) x),
      Binary16.unpackDouble((char) y),
      Binary16.unpackDouble((char) z));
    this.next();
  }

  @Override public void get3f(
    final VectorM3F v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final int x = Integer16.unpackFromBuffer(this.target_data, i + 0);
    final int y = Integer16.unpackFromBuffer(this.target_data, i + 2);
    final int z = Integer16.unpackFromBuffer(this.target_data, i + 4);

    v.set3F(
      Binary16.unpackFloat((char) x),
      Binary16.unpackFloat((char) y),
      Binary16.unpackFloat((char) z));
    this.next();
  }
}
