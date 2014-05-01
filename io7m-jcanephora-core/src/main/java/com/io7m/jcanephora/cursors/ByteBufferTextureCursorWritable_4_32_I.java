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
import com.io7m.jcanephora.SpatialCursorWritable4dType;
import com.io7m.jcanephora.SpatialCursorWritable4fType;
import com.io7m.jcanephora.SpatialCursorWritable4iType;
import com.io7m.jcanephora.SpatialCursorWritable4lType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM4L;
import com.io7m.jtensors.VectorReadable4DType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.io7m.jtensors.VectorReadable4LType;

final class ByteBufferTextureCursorWritable_4_32_I extends AreaCursor implements
  SpatialCursorWritable4fType,
  SpatialCursorWritable4dType,
  SpatialCursorWritable4lType,
  SpatialCursorWritable4iType
{
  private final ByteBuffer target_data;
  private final VectorM4L  vector = new VectorM4L();

  protected ByteBufferTextureCursorWritable_4_32_I(
    final ByteBuffer in_target_data,
    final AreaInclusive target_area,
    final AreaInclusive update_area)
  {
    super(target_area, update_area, 4 * 4);
    this.target_data = in_target_data;
  }

  @Override public void put4d(
    final VectorReadable4DType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4L(
      FixedPoint.doubleToSignedNormalized(v.getXD(), 32),
      FixedPoint.doubleToSignedNormalized(v.getYD(), 32),
      FixedPoint.doubleToSignedNormalized(v.getZD(), 32),
      FixedPoint.doubleToSignedNormalized(v.getWD(), 32));
    this.put4l(this.vector);
  }

  @Override public void put4f(
    final VectorReadable4FType v)
  {
    NullCheck.notNull(v, "Vector");
    this.vector.set4L(
      FixedPoint.floatToSignedNormalized(v.getXF(), 32),
      FixedPoint.floatToSignedNormalized(v.getYF(), 32),
      FixedPoint.floatToSignedNormalized(v.getZF(), 32),
      FixedPoint.floatToSignedNormalized(v.getWF(), 32));
    this.put4l(this.vector);
  }

  @Override public void put4i(
    final VectorReadable4IType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, v.getXI());
    this.target_data.putInt(i + 4, v.getYI());
    this.target_data.putInt(i + 8, v.getZI());
    this.target_data.putInt(i + 12, v.getWI());
    this.next();
  }

  @Override public void put4l(
    final VectorReadable4LType v)
  {
    NullCheck.notNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    this.target_data.putInt(i + 0, (int) v.getXL());
    this.target_data.putInt(i + 4, (int) v.getYL());
    this.target_data.putInt(i + 8, (int) v.getZL());
    this.target_data.putInt(i + 12, (int) v.getWL());
    this.next();
  }
}
