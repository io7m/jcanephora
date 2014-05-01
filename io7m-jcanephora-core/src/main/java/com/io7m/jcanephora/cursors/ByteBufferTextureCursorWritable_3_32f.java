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
import com.io7m.jcanephora.SpatialCursorWritable3dType;
import com.io7m.jcanephora.SpatialCursorWritable3fType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable3DType;
import com.io7m.jtensors.VectorReadable3FType;

final class ByteBufferTextureCursorWritable_3_32f extends AreaCursor implements
  SpatialCursorWritable3fType,
  SpatialCursorWritable3dType
{
  private final ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_3_32f(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 3 * 4);
    this.target_data = in_target_data;
  }

  @Override public void put3d(
    final VectorReadable3DType v)
  {
    NullCheck.notNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, (float) v.getXD());
    this.target_data.putFloat(byte_current + 4, (float) v.getYD());
    this.target_data.putFloat(byte_current + 8, (float) v.getZD());
    this.next();
  }

  @Override public void put3f(
    final VectorReadable3FType v)
  {
    NullCheck.notNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, v.getXF());
    this.target_data.putFloat(byte_current + 4, v.getYF());
    this.target_data.putFloat(byte_current + 8, v.getZF());
    this.next();
  }
}
