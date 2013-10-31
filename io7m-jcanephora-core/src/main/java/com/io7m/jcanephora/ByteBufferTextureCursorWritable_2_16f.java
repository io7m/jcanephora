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
import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2D;
import com.io7m.jtensors.VectorReadable2F;

final class ByteBufferTextureCursorWritable_2_16f extends AreaCursor implements
  SpatialCursorWritable2f,
  SpatialCursorWritable2d
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorWritable_2_16f(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 2 * 2);
    this.target_data = target_data;
  }

  @Override public void put2d(
    final @Nonnull VectorReadable2D v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    ByteBufferCursor.packInteger16(b, i + 0, Binary16.packDouble(v.getXD()));
    ByteBufferCursor.packInteger16(b, i + 2, Binary16.packDouble(v.getYD()));
    this.next();
  }

  @Override public void put2f(
    final @Nonnull VectorReadable2F v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int i = (int) this.getByteOffset();
    final ByteBuffer b = this.target_data;
    ByteBufferCursor.packInteger16(b, i + 0, Binary16.packFloat(v.getXF()));
    ByteBufferCursor.packInteger16(b, i + 2, Binary16.packFloat(v.getYF()));
    this.next();
  }
}
