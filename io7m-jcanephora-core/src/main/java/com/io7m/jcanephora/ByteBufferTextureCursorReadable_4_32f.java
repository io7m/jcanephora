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
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;

final class ByteBufferTextureCursorReadable_4_32f extends AreaCursor implements
  SpatialCursorReadable4f,
  SpatialCursorReadable4d
{
  private final @Nonnull ByteBuffer target_data;

  protected ByteBufferTextureCursorReadable_4_32f(
    final @Nonnull ByteBuffer target_data1,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4 * 4);
    this.target_data = target_data1;
  }

  @Override public void get4d(
    final @Nonnull VectorM4D v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getFloat(byte_current);
    v.y = this.target_data.getFloat(byte_current + 4);
    v.z = this.target_data.getFloat(byte_current + 8);
    v.w = this.target_data.getFloat(byte_current + 12);
    this.next();
  }

  @Override public void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v, "Vector");
    final int byte_current = (int) this.getByteOffset();
    v.x = this.target_data.getFloat(byte_current);
    v.y = this.target_data.getFloat(byte_current + 4);
    v.z = this.target_data.getFloat(byte_current + 8);
    v.w = this.target_data.getFloat(byte_current + 12);
    this.next();
  }
}
