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

import com.io7m.jaux.Constraints.ConstraintError;

final class ByteBufferTextureCursorReadable_1_32_I extends AreaCursor implements
  SpatialCursorReadable1f,
  SpatialCursorReadable1d,
  SpatialCursorReadable1i,
  SpatialCursorReadable1l
{
  private final @Nonnull ByteBuffer target_data;
  private final @Nonnull byte[]     buffer = new byte[3];

  protected ByteBufferTextureCursorReadable_1_32_I(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull AreaInclusive target_area,
    final @Nonnull AreaInclusive update_area)
    throws ConstraintError
  {
    super(target_area, update_area, 4);
    this.target_data = target_data;
  }

  @Override public double get1d()
    throws ConstraintError
  {
    final long z = this.get1l();
    return FixedPoint.signedNormalizedFixedToDoubleLong(32, z);
  }

  @Override public float get1f()
    throws ConstraintError
  {
    final long z = this.get1l();
    return FixedPoint.signedNormalizedFixedToFloatLong(32, z);
  }

  @Override public int get1i()
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    final int z = this.target_data.getInt(byte_current);
    this.next();
    return z;
  }

  @Override public long get1l()
    throws ConstraintError
  {
    final int byte_current = (int) this.getByteOffset();
    final int z = this.target_data.getInt(byte_current);
    this.next();
    return z;
  }
}