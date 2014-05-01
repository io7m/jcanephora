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
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_24_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_1_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_2_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_565;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_3_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_1010102;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_4444;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_5551;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorWritable_4_8_UNFP;

/**
 * <p>
 * An allocated region of data, to replace or update a 2D texture.
 * </p>
 * <p>
 * The cursors exposed by this interface treat <tt>(0, 0)</tt> as the bottom
 * left corner of the image, which is consistent with OpenGL's conventions.
 * </p>
 */

public final class Texture2DWritableData implements TextureWritableDataType
{
  private final  AreaInclusive   source_area;
  private final  AreaInclusive   target_area;
  private final  ByteBuffer      target_data;
  private final  Texture2DStatic texture;
  private final  TextureType     type;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>texture</code> on the GPU.
   * 
   * @param texture1
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           </ul>
   */

  public Texture2DWritableData(
    final  Texture2DStatic texture1)
    throws ConstraintError
  {
    this(texture1, texture1.getArea());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * area <code>area</code> of the data in <code>texture</code> on the GPU.
   * 
   * @param texture1
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
    final  Texture2DStatic texture1,
    final  AreaInclusive area)
    throws ConstraintError
  {
    NullCheck.notNull(texture1, "Texture");
    NullCheck.notNull(area, "Area");
    Constraints.constrainArbitrary(
      area.isIncludedIn(texture1.getArea()),
      "Area is included within texture");

    this.texture = texture1;
    this.target_area = area;

    final RangeInclusive srx =
      new RangeInclusive(0, area.getRangeX().getInterval() - 1);
    final RangeInclusive sry =
      new RangeInclusive(0, area.getRangeY().getInterval() - 1);
    this.source_area = new AreaInclusive(srx, sry);

    final long width = this.source_area.getRangeX().getInterval();
    final long height = this.source_area.getRangeY().getInterval();
    final int bpp = texture1.getType().getBytesPerPixel();
    this.type = texture1.getType();

    this.target_data =
      ByteBuffer.allocateDirect((int) (height * width * bpp)).order(
        ByteOrder.nativeOrder());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor1d()
   */

  @Override public  SpatialCursorWritable1dType getCursor1d()
    throws ConstraintError
  {
    return (SpatialCursorWritable1dType) this.getCursor1f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor1f()
   */

  @Override public  SpatialCursorWritable1fType getCursor1f()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 1,
      "Number of components in the texture is 1");

    switch (this.type) {
      case TEXTURE_TYPE_R_16I_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_16U_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_24_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_32f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8I_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_32I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_32U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_16F_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor1i()
   */

  @Override public  SpatialCursorWritable1iType getCursor1i()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 1,
      "Number of components in the texture is 1");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isFloatingPoint(this.type) == false,
      "Texture is not floating point");

    switch (this.type) {
      case TEXTURE_TYPE_R_16I_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_16U_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorWritable_1_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_24_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8I_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorWritable_1_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_32I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_R_32U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_1_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor2d()
   */

  @Override public  SpatialCursorWritable2dType getCursor2d()
    throws ConstraintError
  {
    return (SpatialCursorWritable2dType) this.getCursor2f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor2f()
   */

  @Override public  SpatialCursorWritable2fType getCursor2f()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 2,
      "Number of components in the texture is 2");
    Constraints.constrainArbitrary(
      this.type != TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP,
      "Type is not packed depth/stencil");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RG_16I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_16U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_16_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_32F_8BPP:
      {
        return new ByteBufferTextureCursorWritable_2_32f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_32I_8BPP:
      {
        return new ByteBufferTextureCursorWritable_2_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_32U_8BPP:
      {
        return new ByteBufferTextureCursorWritable_2_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8I_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8U_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_16F_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor2i()
   */

  @Override public  SpatialCursorWritable2iType getCursor2i()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 2,
      "Number of components in the texture is 2");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isFloatingPoint(this.type) == false,
      "Texture is not floating point");
    Constraints.constrainArbitrary(
      this.type != TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP,
      "Type is not packed depth/stencil");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RG_16I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_16U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_16_4BPP:
      {
        return new ByteBufferTextureCursorWritable_2_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_32I_8BPP:
      {
        return new ByteBufferTextureCursorWritable_2_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_32U_8BPP:
      {
        return new ByteBufferTextureCursorWritable_2_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8I_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8U_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        return new ByteBufferTextureCursorWritable_2_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor3d()
   */

  @Override public  SpatialCursorWritable3dType getCursor3d()
    throws ConstraintError
  {
    return (SpatialCursorWritable3dType) this.getCursor3f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor3f()
   */

  @Override public  SpatialCursorWritable3fType getCursor3f()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 3,
      "Number of components in the texture is 3");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGB_16F_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_16I_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_16U_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_16_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_32F_12BPP:
      {
        return new ByteBufferTextureCursorWritable_3_32f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_32I_12BPP:
      {
        return new ByteBufferTextureCursorWritable_3_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_32U_12BPP:
      {
        return new ByteBufferTextureCursorWritable_3_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorWritable_3_565(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8I_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8U_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor3i()
   */

  @Override public  SpatialCursorWritable3iType getCursor3i()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 3,
      "Number of components in the texture is 3");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isFloatingPoint(this.type) == false,
      "Texture is not floating point");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGB_16I_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_16U_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_16_6BPP:
      {
        return new ByteBufferTextureCursorWritable_3_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_32I_12BPP:
      {
        return new ByteBufferTextureCursorWritable_3_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_32U_12BPP:
      {
        return new ByteBufferTextureCursorWritable_3_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorWritable_3_565(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8I_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8U_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return new ByteBufferTextureCursorWritable_3_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor4d()
   */

  @Override public  SpatialCursorWritable4dType getCursor4d()
    throws ConstraintError
  {
    return (SpatialCursorWritable4dType) this.getCursor4f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor4f()
   */

  @Override public  SpatialCursorWritable4fType getCursor4f()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 4,
      "Number of components in the texture is 4");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_1010102(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorWritable_4_4444(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorWritable_4_5551(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      {
        return new ByteBufferTextureCursorWritable_4_32f(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      {
        return new ByteBufferTextureCursorWritable_4_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      {
        return new ByteBufferTextureCursorWritable_4_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureWritableData#getCursor4i()
   */

  @Override public  SpatialCursorWritable4iType getCursor4i()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 4,
      "Number of components in the texture is 4");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isFloatingPoint(this.type) == false,
      "Texture is not floating point");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_1010102(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorWritable_4_4444(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorWritable_4_5551(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_16_8BPP:
      {
        return new ByteBufferTextureCursorWritable_4_16_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      {
        return new ByteBufferTextureCursorWritable_4_32_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      {
        return new ByteBufferTextureCursorWritable_4_32_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_I(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_U(
          this.target_data,
          this.source_area,
          this.target_area);
      }
      case TEXTURE_TYPE_RGBA_8_4BPP:
      {
        return new ByteBufferTextureCursorWritable_4_8_UNFP(
          this.target_data,
          this.source_area,
          this.target_area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the texture that will be affected by this update.
   */

  public  Texture2DStatic getTexture()
  {
    return this.texture;
  }

  @Override public  TextureType getType()
  {
    return this.type;
  }

   AreaInclusive targetArea()
  {
    return this.target_area;
  }

   ByteBuffer targetData()
  {
    return this.target_data;
  }
}
