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

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import com.io7m.jregions.core.unparameterized.areas.AreasL;
import com.io7m.jregions.core.unparameterized.sizes.AreaSizesL;
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
   * Create a new update that will replace the entirety of one face of {@code
   * t}.
   *
   * @param t The texture
   *
   * @return A new update
   */

  public static JCGLTextureCubeUpdateType newUpdateReplacingAllCube(
    final JCGLTextureCubeUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    return newUpdateReplacingAreaCube(t, AreaSizesL.area(t.textureGetSize()));
  }

  /**
   * Create a new update that will replace the given {@code area} of {@code t}.
   * {@code area} must be included within the area of {@code t}.
   *
   * @param t           The texture
   * @param update_area The area that will be updated
   *
   * @return A new update
   *
   * @throws RangeCheckException Iff {@code area} is not included within {@code
   *                             t}
   */

  public static JCGLTextureCubeUpdateType newUpdateReplacingAreaCube(
    final JCGLTextureCubeUsableType t,
    final AreaL update_area)
  {
    NullCheck.notNull(t, "Texture");
    NullCheck.notNull(update_area, "Area");

    final AreaL texture_area = AreaSizesL.area(t.textureGetSize());
    if (!AreasL.contains(texture_area, update_area)) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Target area is not contained within the texture's area.");
      sb.append(System.lineSeparator());
      sb.append("  Texture area: ");
      sb.append(t.textureGetSize());
      sb.append(System.lineSeparator());
      sb.append("  Target area:  ");
      sb.append(update_area);
      sb.append(System.lineSeparator());
      throw new RangeCheckException(sb.toString());
    }

    final long width = update_area.width();
    final long height = update_area.height();
    final int bpp = t.textureGetFormat().getBytesPerPixel();
    final long size = width * height * (long) bpp;

    final ByteBuffer data = ByteBuffer.allocateDirect(Math.toIntExact(size));
    data.order(ByteOrder.nativeOrder());
    return new UpdateCube(t, update_area, data);
  }

  /**
   * Create a new update that will replace the entirety of {@code t}.
   *
   * @param t The texture
   *
   * @return A new update
   */

  public static JCGLTexture2DUpdateType newUpdateReplacingAll2D(
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    return newUpdateReplacingArea2D(t, AreaSizesL.area(t.textureGetSize()));
  }

  /**
   * Create a new update that will replace the given {@code area} of {@code t}.
   * {@code area} must be included within the area of {@code t}.
   *
   * @param t           The texture
   * @param update_area The area that will be updated
   *
   * @return A new update
   *
   * @throws RangeCheckException Iff {@code area} is not included within {@code
   *                             t}
   */

  public static JCGLTexture2DUpdateType newUpdateReplacingArea2D(
    final JCGLTexture2DUsableType t,
    final AreaL update_area)
  {
    NullCheck.notNull(t, "Texture");
    NullCheck.notNull(update_area, "Area");

    final AreaL texture_area = AreaSizesL.area(t.textureGetSize());
    if (!AreasL.contains(texture_area, update_area)) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Target area is not contained within the texture's area.");
      sb.append(System.lineSeparator());
      sb.append("  Texture area: ");
      sb.append(t.textureGetSize());
      sb.append(System.lineSeparator());
      sb.append("  Target area:  ");
      sb.append(update_area);
      sb.append(System.lineSeparator());
      throw new RangeCheckException(sb.toString());
    }

    final long width = update_area.width();
    final long height = update_area.height();
    final int bpp = t.textureGetFormat().getBytesPerPixel();
    final long size = width * height * (long) bpp;

    final ByteBuffer data = ByteBuffer.allocateDirect(Math.toIntExact(size));
    data.order(ByteOrder.nativeOrder());
    return new Update2D(t, update_area, data);
  }

  private static final class Update2D implements JCGLTexture2DUpdateType
  {
    private final JCGLTexture2DUsableType texture;
    private final AreaL area;
    private final ByteBuffer data;
    private final UnsignedRangeInclusiveL range;

    Update2D(
      final JCGLTexture2DUsableType in_t,
      final AreaL in_area,
      final ByteBuffer in_data)
    {
      this.texture = NullCheck.notNull(in_t, "Texture");
      this.area = NullCheck.notNull(in_area, "Area");
      this.data = NullCheck.notNull(in_data, "Data");
      this.range = new UnsignedRangeInclusiveL(
        0L,
        (long) this.data.capacity() - 1L);
    }

    @Override
    public AreaL getArea()
    {
      return this.area;
    }

    @Override
    public JCGLTexture2DUsableType getTexture()
    {
      return this.texture;
    }

    @Override
    public ByteBuffer getData()
    {
      return this.data;
    }

    @Override
    public UnsignedRangeInclusiveL getDataUpdateRange()
    {
      return this.range;
    }
  }

  private static final class UpdateCube implements JCGLTextureCubeUpdateType
  {
    private final JCGLTextureCubeUsableType texture;
    private final AreaL area;
    private final ByteBuffer data;
    private final UnsignedRangeInclusiveL range;

    UpdateCube(
      final JCGLTextureCubeUsableType in_t,
      final AreaL in_area,
      final ByteBuffer in_data)
    {
      this.texture = NullCheck.notNull(in_t, "Texture");
      this.area = NullCheck.notNull(in_area, "Area");
      this.data = NullCheck.notNull(in_data, "Data");
      this.range = new UnsignedRangeInclusiveL(
        0L,
        (long) this.data.capacity() - 1L);
    }

    @Override
    public AreaL getArea()
    {
      return this.area;
    }

    @Override
    public JCGLTextureCubeUsableType getTexture()
    {
      return this.texture;
    }

    @Override
    public ByteBuffer getData()
    {
      return this.data;
    }

    @Override
    public UnsignedRangeInclusiveL getDataUpdateRange()
    {
      return this.range;
    }
  }
}
