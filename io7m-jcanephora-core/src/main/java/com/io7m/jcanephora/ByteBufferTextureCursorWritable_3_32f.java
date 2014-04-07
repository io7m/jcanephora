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
import com.io7m.jtensors.VectorReadable3D;
import com.io7m.jtensors.VectorReadable3F;

final class ByteBufferTextureCursorWritable_3_32f extends AreaCursor implements
  SpatialCursorWritable3f,
  SpatialCursorWritable3d
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_3_32f(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 3 * 4);
    this.target_data = target_data1;
  }

  @Override public void put3d(
    final @Nonnull VectorReadable3D v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, (float) v.getXD());
    this.target_data.putFloat(byte_current + 4, (float) v.getYD());
    this.target_data.putFloat(byte_current + 8, (float) v.getZD());
    this.next();
  }

  @Override public void put3f(
    final @Nonnull VectorReadable3F v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, v.getXF());
    this.target_data.putFloat(byte_current + 4, v.getYF());
    this.target_data.putFloat(byte_current + 8, v.getZF());
    this.next();
  }
}
