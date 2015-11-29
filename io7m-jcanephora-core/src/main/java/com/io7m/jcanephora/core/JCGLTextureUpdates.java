/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core;

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility functions to allocate texture updates.
 */

public final class JCGLTextureUpdates
{
  private JCGLTextureUpdates()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Create a new update that will replace the entirety of {@code t}.
   *
   * @param t The texture
   *
   * @return A new update
   */

  public static JCGLTexture2DUpdateType newUpdateReplacingAll(
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t);
    return JCGLTextureUpdates.newUpdateReplacingArea(t, t.textureGetArea());
  }

  /**
   * Create a new update that will replace the given {@code area} of {@code
   * t}. {@code area} must be included within the area of {@code t}.
   *
   * @param t    The texture
   * @param area The area that will be updated
   *
   * @return A new update
   *
   * @throws RangeCheckException Iff {@code area} is not included within {@code
   *                             t}
   */

  public static JCGLTexture2DUpdateType newUpdateReplacingArea(
    final JCGLTexture2DUsableType t,
    final AreaInclusiveUnsignedLType area)
  {
    NullCheck.notNull(t, "Texture");
    NullCheck.notNull(area, "Area");

    if (!area.isIncludedIn(t.textureGetArea())) {
      final String s =
        String.format(
          "Target area %s is not included within the texture's area %s",
          area,
          t.textureGetArea());
      assert s != null;
      throw new RangeCheckException(s);
    }

    final long width = area.getRangeX().getInterval();
    final long height = area.getRangeY().getInterval();
    final int bpp = t.textureGetFormat().getBytesPerPixel();
    final long size = width * height * (long) bpp;

    final ByteBuffer data = ByteBuffer.allocateDirect((int) size);
    data.order(ByteOrder.nativeOrder());
    return new Update(t, area, data);
  }

  private static final class Update implements JCGLTexture2DUpdateType
  {
    private final JCGLTexture2DUsableType    texture;
    private final AreaInclusiveUnsignedLType area;
    private final ByteBuffer                 data;
    private final UnsignedRangeInclusiveL    range;

    Update(
      final JCGLTexture2DUsableType in_t,
      final AreaInclusiveUnsignedLType in_area,
      final ByteBuffer in_data)
    {
      this.texture = NullCheck.notNull(in_t);
      this.area = NullCheck.notNull(in_area);
      this.data = NullCheck.notNull(in_data);
      this.range = new UnsignedRangeInclusiveL(0L, this.data.capacity() - 1L);
    }

    @Override public AreaInclusiveUnsignedLType getArea()
    {
      return this.area;
    }

    @Override public JCGLTexture2DUsableType getTexture()
    {
      return this.texture;
    }

    @Override public ByteBuffer getData()
    {
      return this.data;
    }

    @Override public UnsignedRangeInclusiveL getDataUpdateRange()
    {
      return this.range;
    }
  }
}
