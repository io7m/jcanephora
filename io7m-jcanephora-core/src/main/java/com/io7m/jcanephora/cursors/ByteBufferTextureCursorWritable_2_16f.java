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
import com.io7m.jcanephora.SpatialCursorWritable2dType;
import com.io7m.jcanephora.SpatialCursorWritable2fType;
import com.io7m.jintegers.Integer16;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable2DType;
import com.io7m.jtensors.VectorReadable2FType;

final class ByteBufferTextureCursorWritable_2_16f extends AreaCursor implements
  SpatialCursorWritable2fType,
  SpatialCursorWritable2dType
{
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_2_16f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 2 * 2);
    this.target_data = in_target_data;
  }

  @Override public void put2d(
    final VectorReadable2DType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    Integer16.packToBuffer(Binary16.packDouble(v.getXD()), b, i + 0);
    Integer16.packToBuffer(Binary16.packDouble(v.getYD()), b, i + 2);
    this.next();
  }

  @Override public void put2f(
    final VectorReadable2FType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    Integer16.packToBuffer(Binary16.packFloat(v.getXF()), b, i + 0);
    Integer16.packToBuffer(Binary16.packFloat(v.getYF()), b, i + 2);
    this.next();
  }
}
