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
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;

/**
 * An allocated region of data, to replace or update a 2D texture.
 */

public final class Texture2DWritableData
{
  private final @Nonnull Texture2DStatic texture;
  private final @Nonnull AreaInclusive   target_area;
  private final @Nonnull AreaInclusive   source_area;
  private final @Nonnull ByteBuffer      target_data;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>texture</code> on the GPU.
   * 
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           </ul>
   */

  public Texture2DWritableData(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError
  {
    this(texture, texture.getArea());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * area <code>area</code> of the data in <code>texture</code> on the GPU.
   * 
   * @param texture
   *          The texture.
   * @param area
   *          The inclusive area defining the area of the texture that will be
   *          modified.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>area == null/code></li>
   *           <li><code>area.isIncludedIn(texture.getArea()) == false</code></li>
   *           </ul>
   */

  public Texture2DWritableData(
    final @Nonnull Texture2DStatic texture,
    final @Nonnull AreaInclusive area)
    throws ConstraintError
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainNotNull(area, "Area");
    Constraints.constrainArbitrary(
      area.isIncludedIn(texture.getArea()),
      "Area is included within texture");

    this.texture = texture;
    this.target_area = area;

    final RangeInclusive srx =
      new RangeInclusive(0, area.getRangeX().getInterval() - 1);
    final RangeInclusive sry =
      new RangeInclusive(0, area.getRangeY().getInterval() - 1);
    this.source_area = new AreaInclusive(srx, sry);

    final long width = this.source_area.getRangeX().getInterval();
    final long height = this.source_area.getRangeY().getInterval();
    final int bpp = texture.getType().bytesPerPixel();

    this.target_data =
      ByteBuffer.allocateDirect((int) (height * width * bpp)).order(
        ByteOrder.nativeOrder());
  }

  /**
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public @Nonnull SpatialCursorWritable1f getCursor1f()
    throws ConstraintError
  {
    switch (this.texture.getType()) {
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      {
        Constraints
          .constrainArbitrary(
            false,
            "Number of texture components is 1 and textures are floating point");
        break;
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return new ByteBufferTextureCursorWritable1f_1_32(
          this.target_data,
          this.source_area,
          this.source_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public @Nonnull SpatialCursorWritable1i getCursor1i()
    throws ConstraintError
  {
    switch (this.texture.getType()) {
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorWritable1i_1_8(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 1 and components are integers");
        break;
      }
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      {
        return new ByteBufferTextureCursorWritable1i_1_32(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorWritable1i_1_16(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorWritable1i_1_24(
          this.target_data,
          this.source_area,
          this.source_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 2.
   */

  public @Nonnull SpatialCursorWritable2i getCursor2i()
    throws ConstraintError
  {
    switch (this.texture.getType()) {
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        return new ByteBufferTextureCursorWritable2i_2_88(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 2 and components are integers");
        break;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3.
   */

  public @Nonnull SpatialCursorWritable3i getCursor3i()
    throws ConstraintError
  {
    switch (this.texture.getType()) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 3 and components are integers");
        break;
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorWritable3i_2_565(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return new ByteBufferTextureCursorWritable3i_3_888(
          this.target_data,
          this.source_area,
          this.source_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 4.
   */

  public @Nonnull SpatialCursorWritable4i getCursor4i()
    throws ConstraintError
  {
    switch (this.texture.getType()) {
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorWritable4i_2_4444(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorWritable4i_2_5551(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return new ByteBufferTextureCursorWritable4i_4_8888(
          this.target_data,
          this.source_area,
          this.source_area);
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 4 and components are integers");
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the texture that will be affected by this update.
   */

  public @Nonnull Texture2DStatic getTexture()
  {
    return this.texture;
  }

  @Nonnull AreaInclusive targetArea()
  {
    return this.target_area;
  }

  @Nonnull ByteBuffer targetData()
  {
    return this.target_data;
  }
}
