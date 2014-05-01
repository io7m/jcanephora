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
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_24_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_32_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_1_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_32_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_2_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_32_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_32_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_565;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_3_8_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_1010102;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_16_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_16_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_16_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_16f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_32_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_32_UNFP;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_32f;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_4444;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_5551;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_8_I;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_8_U;
import com.io7m.jcanephora.cursors.ByteBufferTextureCursorReadable_4_8_UNFP;

/**
 * <p>
 * An allocated read-only region of data, typically created by downloading a
 * texture from the GPU.
 * </p>
 * <p>
 * The cursors exposed by this interface treat <tt>(0, 0)</tt> as the bottom
 * left corner of the image, which is consistent with OpenGL's conventions.
 * </p>
 */

public final class TextureCubeReadableData implements TextureReadableDataType
{
  private final  AreaInclusive area;
  private final  ByteBuffer    data;
  private final  TextureType   type;

  TextureCubeReadableData(
    final  TextureType type1,
    final  AreaInclusive area1)
    throws ConstraintError
  {
    this.type = NullCheck.notNull(type1, "Texture type");
    this.area = NullCheck.notNull(area1, "Area");

    final long width = this.area.getRangeX().getInterval();
    final long height = this.area.getRangeY().getInterval();
    final int bpp = type1.getBytesPerPixel();
    this.data =
      ByteBuffer.allocate((int) (height * width * bpp)).order(
        ByteOrder.nativeOrder());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor1d()
   */

  @Override public  SpatialCursorReadable1dType getCursor1d()
    throws ConstraintError
  {
    return (SpatialCursorReadable1dType) this.getCursor1f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor1f()
   */

  @Override public  SpatialCursorReadable1fType getCursor1f()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 1,
      "Number of components in the texture is 1");

    switch (this.type) {
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
      case TEXTURE_TYPE_R_16I_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_16U_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_24_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_32f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8I_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_32I_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_32U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_32_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_16F_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16f(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor1i()
   */

  @Override public  SpatialCursorReadable1iType getCursor1i()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.type.getComponentCount() == 1,
      "Number of components in the texture is 1");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isFloatingPoint(this.type) == false,
      "Texture is not floating point");

    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_R_16I_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_16U_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorReadable_1_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_24_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8I_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorReadable_1_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_32I_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_R_32U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_1_32_U(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor2d()
   */

  @Override public  SpatialCursorReadable2dType getCursor2d()
    throws ConstraintError
  {
    return (SpatialCursorReadable2dType) this.getCursor2f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor2f()
   */

  @Override public  SpatialCursorReadable2fType getCursor2f()
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
        return new ByteBufferTextureCursorReadable_2_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_16U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_2_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_16_4BPP:
      {
        return new ByteBufferTextureCursorReadable_2_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_32F_8BPP:
      {
        return new ByteBufferTextureCursorReadable_2_32f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_32I_8BPP:
      {
        return new ByteBufferTextureCursorReadable_2_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_32U_8BPP:
      {
        return new ByteBufferTextureCursorReadable_2_32_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8I_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8U_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_16F_4BPP:
      {
        return new ByteBufferTextureCursorReadable_2_16f(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor2i()
   */

  @Override public  SpatialCursorReadable2iType getCursor2i()
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
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
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
        return new ByteBufferTextureCursorReadable_2_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_16U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_2_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_16_4BPP:
      {
        return new ByteBufferTextureCursorReadable_2_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_32I_8BPP:
      {
        return new ByteBufferTextureCursorReadable_2_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_32U_8BPP:
      {
        return new ByteBufferTextureCursorReadable_2_32_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8I_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8U_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RG_8_2BPP:
      {
        return new ByteBufferTextureCursorReadable_2_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor3d()
   */

  @Override public  SpatialCursorReadable3dType getCursor3d()
    throws ConstraintError
  {
    return (SpatialCursorReadable3dType) this.getCursor3f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor3f()
   */

  @Override public  SpatialCursorReadable3fType getCursor3f()
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
        return new ByteBufferTextureCursorReadable_3_16f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_16I_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_16U_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_16_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_32F_12BPP:
      {
        return new ByteBufferTextureCursorReadable_3_32f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_32I_12BPP:
      {
        return new ByteBufferTextureCursorReadable_3_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_32U_12BPP:
      {
        return new ByteBufferTextureCursorReadable_3_32_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorReadable_3_565(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8I_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8U_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor3i()
   */

  @Override public  SpatialCursorReadable3iType getCursor3i()
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
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGB_16I_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_16U_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_16_6BPP:
      {
        return new ByteBufferTextureCursorReadable_3_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_32I_12BPP:
      {
        return new ByteBufferTextureCursorReadable_3_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_32U_12BPP:
      {
        return new ByteBufferTextureCursorReadable_3_32_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorReadable_3_565(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8I_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8U_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return new ByteBufferTextureCursorReadable_3_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor4d()
   */

  @Override public  SpatialCursorReadable4dType getCursor4d()
    throws ConstraintError
  {
    return (SpatialCursorReadable4dType) this.getCursor4f();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor4f()
   */

  @Override public  SpatialCursorReadable4fType getCursor4f()
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
        return new ByteBufferTextureCursorReadable_4_1010102(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorReadable_4_4444(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorReadable_4_5551(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      {
        return new ByteBufferTextureCursorReadable_4_32f(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      {
        return new ByteBufferTextureCursorReadable_4_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      {
        return new ByteBufferTextureCursorReadable_4_32_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.io7m.jcanephora.TextureReadableData#getCursor4i()
   */

  @Override public  SpatialCursorReadable4iType getCursor4i()
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
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
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
        return new ByteBufferTextureCursorReadable_4_1010102(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorReadable_4_4444(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorReadable_4_5551(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_16_8BPP:
      {
        return new ByteBufferTextureCursorReadable_4_16_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      {
        return new ByteBufferTextureCursorReadable_4_32_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      {
        return new ByteBufferTextureCursorReadable_4_32_UNFP(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_I(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_U(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8_4BPP:
      {
        return new ByteBufferTextureCursorReadable_4_8_UNFP(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

   AreaInclusive targetArea()
  {
    return this.area;
  }

   ByteBuffer targetData()
  {
    return this.data;
  }
}
