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

package com.io7m.jcanephora.jogl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.SpatialCursorReadable1dType;
import com.io7m.jcanephora.SpatialCursorReadable1fType;
import com.io7m.jcanephora.SpatialCursorReadable1iType;
import com.io7m.jcanephora.SpatialCursorReadable2dType;
import com.io7m.jcanephora.SpatialCursorReadable2fType;
import com.io7m.jcanephora.SpatialCursorReadable2iType;
import com.io7m.jcanephora.SpatialCursorReadable3dType;
import com.io7m.jcanephora.SpatialCursorReadable3fType;
import com.io7m.jcanephora.SpatialCursorReadable3iType;
import com.io7m.jcanephora.SpatialCursorReadable4dType;
import com.io7m.jcanephora.SpatialCursorReadable4fType;
import com.io7m.jcanephora.SpatialCursorReadable4iType;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
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
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

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

final class JOGLTextureCubeReadableData implements
  TextureCubeStaticReadableType
{
  private final AreaInclusive area;
  private final ByteBuffer    data;
  private final TextureFormat type;

  JOGLTextureCubeReadableData(
    final TextureFormat type1,
    final AreaInclusive area1)
  {
    this.type = NullCheck.notNull(type1, "Texture type");
    this.area = NullCheck.notNull(area1, "Area");

    final long width = this.area.getRangeX().getInterval();
    final long height = this.area.getRangeY().getInterval();
    final int bpp = type1.getBytesPerPixel();

    final ByteBuffer bb = ByteBuffer.allocate((int) (height * width * bpp));
    bb.order(ByteOrder.nativeOrder());
    this.data = bb;
  }

  private void checkComponents(
    final int c)
    throws JCGLExceptionTypeError
  {
    if (this.type.getComponentCount() != c) {
      final StringBuilder m = new StringBuilder();
      m
        .append("The required number of texture components for this cursor type is ");
      m.append(c);
      m.append(", but this texture has ");
      m.append(this.type.getComponentCount());
      final String r = m.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  private void checkNotFloatingPoint()
    throws JCGLExceptionTypeError
  {
    if (TextureFormatMeta.isFloatingPoint(this.type) == true) {
      final StringBuilder m = new StringBuilder();
      m
        .append("Integer cursors cannot be used to address textures with components of type ");
      m.append(this.type);
      final String r = m.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  private void checkNotPackedDepthStencil()
    throws JCGLExceptionTypeError
  {
    if (this.type == TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP) {
      final StringBuilder m = new StringBuilder();
      m
        .append("Cursors of this type cannot be used to address textures of type ");
      m.append(this.type);
      final String r = m.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  @Override public SpatialCursorReadable1dType getCursor1d()
    throws JCGLExceptionTypeError
  {
    return (SpatialCursorReadable1dType) this.getCursor1f();
  }

  @Override public SpatialCursorReadable1fType getCursor1f()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(1);

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_R_16I_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_16U_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_24_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_32f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8I_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_32I_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_32U_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_32_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_16F_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16f.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable1iType getCursor1i()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(1);
    this.checkNotFloatingPoint();

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_R_16I_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_16U_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      {
        return ByteBufferTextureCursorReadable_1_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_24_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8I_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return ByteBufferTextureCursorReadable_1_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_32I_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_R_32U_4BPP:
      {
        return ByteBufferTextureCursorReadable_1_32_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable2dType getCursor2d()
    throws JCGLExceptionTypeError
  {
    return (SpatialCursorReadable2dType) this.getCursor2f();
  }

  @Override public SpatialCursorReadable2fType getCursor2f()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(2);
    this.checkNotPackedDepthStencil();

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RG_16I_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_16U_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_16_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_32F_8BPP:
      {
        return ByteBufferTextureCursorReadable_2_32f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_32I_8BPP:
      {
        return ByteBufferTextureCursorReadable_2_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_32U_8BPP:
      {
        return ByteBufferTextureCursorReadable_2_32_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8I_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8U_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_16F_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16f.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable2iType getCursor2i()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(2);
    this.checkNotFloatingPoint();
    this.checkNotPackedDepthStencil();

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RG_16I_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_16U_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_16_4BPP:
      {
        return ByteBufferTextureCursorReadable_2_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_32I_8BPP:
      {
        return ByteBufferTextureCursorReadable_2_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_32U_8BPP:
      {
        return ByteBufferTextureCursorReadable_2_32_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8I_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8U_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RG_8_2BPP:
      {
        return ByteBufferTextureCursorReadable_2_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable3dType getCursor3d()
    throws JCGLExceptionTypeError
  {
    return (SpatialCursorReadable3dType) this.getCursor3f();
  }

  @Override public SpatialCursorReadable3fType getCursor3f()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(3);

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_16_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      {
        return ByteBufferTextureCursorReadable_3_32f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      {
        return ByteBufferTextureCursorReadable_3_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      {
        return ByteBufferTextureCursorReadable_3_32_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_565_2BPP:
      {
        return ByteBufferTextureCursorReadable_3_565.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable3iType getCursor3i()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(3);
    this.checkNotFloatingPoint();

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_16_6BPP:
      {
        return ByteBufferTextureCursorReadable_3_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      {
        return ByteBufferTextureCursorReadable_3_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      {
        return ByteBufferTextureCursorReadable_3_32_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_565_2BPP:
      {
        return ByteBufferTextureCursorReadable_3_565.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGB_8_3BPP:
      {
        return ByteBufferTextureCursorReadable_3_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable4dType getCursor4d()
    throws JCGLExceptionTypeError
  {
    return (SpatialCursorReadable4dType) this.getCursor4f();
  }

  @Override public SpatialCursorReadable4fType getCursor4f()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(4);

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_1010102.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      {
        return ByteBufferTextureCursorReadable_4_4444.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      {
        return ByteBufferTextureCursorReadable_4_5551.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      {
        return ByteBufferTextureCursorReadable_4_32f.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      {
        return ByteBufferTextureCursorReadable_4_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      {
        return ByteBufferTextureCursorReadable_4_32_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  @Override public SpatialCursorReadable4iType getCursor4i()
    throws JCGLExceptionTypeError
  {
    this.checkComponents(4);
    this.checkNotFloatingPoint();

    switch (this.type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_1010102.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      {
        return ByteBufferTextureCursorReadable_4_4444.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      {
        return ByteBufferTextureCursorReadable_4_5551.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      {
        return ByteBufferTextureCursorReadable_4_16_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      {
        return ByteBufferTextureCursorReadable_4_32_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      {
        return ByteBufferTextureCursorReadable_4_32_UNFP.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_I.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_U.newCursor(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      {
        return ByteBufferTextureCursorReadable_4_8_UNFP.newCursor(
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
