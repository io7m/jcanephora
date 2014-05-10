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

package com.io7m.jcanephora.texload.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.io7m.jcanephora.CMFNegativeXKind;
import com.io7m.jcanephora.CMFNegativeYKind;
import com.io7m.jcanephora.CMFNegativeZKind;
import com.io7m.jcanephora.CMFPositiveXKind;
import com.io7m.jcanephora.CMFPositiveYKind;
import com.io7m.jcanephora.CMFPositiveZKind;
import com.io7m.jcanephora.CubeMapFaceInputStream;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionParameterError;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.SpatialCursorWritable1fType;
import com.io7m.jcanephora.SpatialCursorWritable2fType;
import com.io7m.jcanephora.SpatialCursorWritable3fType;
import com.io7m.jcanephora.SpatialCursorWritable4fType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdate;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUpdate;
import com.io7m.jcanephora.TextureCubeStaticUpdateType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureUpdateType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLImplementationVisitorType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL2ES3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGLES2Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL2ES3Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3ES3Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGLES2Type;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM4F;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * An implementation of the {@link TextureLoaderType} interface using Java's
 * ImageIO interface.
 */

@SuppressWarnings({ "boxing", "synthetic-access" }) public final class TextureLoaderImageIO implements
  TextureLoaderType
{
  private static Texture2DStaticType allocateTexture2D_ES2(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureFormat type,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_FORMAT_RGBA_4444_2BPP:
        return gl.texture2DStaticAllocateRGBA4444(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
        return gl.texture2DStaticAllocateRGBA5551(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGB_565_2BPP:
        return gl.texture2DStaticAllocateRGB565(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
        throw new UnreachableCodeException(
          new AssertionError(type.toString()));
    }

    throw new UnreachableCodeException();
  }

  private static Texture2DStaticType allocateTexture2D_GL2ES3(
    final JCGLTextures2DStaticGL2ES3Type gl,
    final TextureFormat type,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_RGBA_8_4BPP:
        return gl.texture2DStaticAllocateRGBA8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGB_8_3BPP:
        return gl.texture2DStaticAllocateRGB8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
        throw new UnreachableCodeException();
    }

    throw new UnreachableCodeException();
  }

  private static Texture2DStaticType allocateTexture2D_GL3ES3(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureFormat type,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_FORMAT_DEPTH_16_2BPP:
        return gl.texture2DStaticAllocateDepth16(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
        return gl.texture2DStaticAllocateDepth24(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        return gl.texture2DStaticAllocateDepth32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGBA_8_4BPP:
        return gl.texture2DStaticAllocateRGBA8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGB_8_3BPP:
        return gl.texture2DStaticAllocateRGB8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RG_8_2BPP:
        return gl.texture2DStaticAllocateRG8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_R_8_1BPP:
        return gl.texture2DStaticAllocateR8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      {
        return gl.texture2DStaticAllocateRGBA32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      {
        return gl.texture2DStaticAllocateRGBA1010102(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      {
        return gl.texture2DStaticAllocateRGBA16f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      {
        return gl.texture2DStaticAllocateRGBA16I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      {
        return gl.texture2DStaticAllocateRGBA16U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      {
        return gl.texture2DStaticAllocateRGBA32I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      {
        return gl.texture2DStaticAllocateRGBA32U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      {
        return gl.texture2DStaticAllocateRGBA8I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      {
        return gl.texture2DStaticAllocateRGBA8U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      {
        return gl.texture2DStaticAllocateRGB16f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      {
        return gl.texture2DStaticAllocateRGB16I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      {
        return gl.texture2DStaticAllocateRGB16U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_16_6BPP:
      {
        throw new UnreachableCodeException();
      }

      case TEXTURE_FORMAT_RGB_32F_12BPP:
      {
        return gl.texture2DStaticAllocateRGB32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      {
        return gl.texture2DStaticAllocateRGB32I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      {
        return gl.texture2DStaticAllocateRGB32U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      {
        return gl.texture2DStaticAllocateRGB8I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      {
        return gl.texture2DStaticAllocateRGB8U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_16F_4BPP:
      {
        return gl.texture2DStaticAllocateRG16f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_16I_4BPP:
      {
        return gl.texture2DStaticAllocateRG16I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_16U_4BPP:
      {
        return gl.texture2DStaticAllocateRG16U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_16_4BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_RG_32F_8BPP:
      {
        return gl.texture2DStaticAllocateRG32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_32I_8BPP:
      {
        return gl.texture2DStaticAllocateRG32I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_32U_8BPP:
      {
        return gl.texture2DStaticAllocateRG32U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_8I_2BPP:
      {
        return gl.texture2DStaticAllocateRG8I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RG_8U_2BPP:
      {
        return gl.texture2DStaticAllocateRG8U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_16F_2BPP:
      {
        return gl.texture2DStaticAllocateR16f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_16I_2BPP:
      {
        return gl.texture2DStaticAllocateR16I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_16U_2BPP:
      {
        return gl.texture2DStaticAllocateR16U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_16_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_FORMAT_R_32F_4BPP:
      {
        return gl.texture2DStaticAllocateR32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_32I_4BPP:
      {
        return gl.texture2DStaticAllocateR32I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_32U_4BPP:
      {
        return gl.texture2DStaticAllocateR32U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_8I_1BPP:
      {
        return gl.texture2DStaticAllocateR8I(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        return gl.texture2DStaticAllocateR8U(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
    }

    throw new UnreachableCodeException();
  }

  private static TextureCubeStaticType allocateTextureCube_GL2ES3(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureFormat type,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_FORMAT_RGBA_8_4BPP:
        return gl.textureCubeStaticAllocateRGBA8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGB_8_3BPP:
        return gl.textureCubeStaticAllocateRGB8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static TextureCubeStaticType allocateTextureCube_GL3ES3(
    final JCGLTexturesCubeStaticGL3ES3Type gl,
    final TextureFormat type,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_FORMAT_RGBA_8_4BPP:
        return gl.textureCubeStaticAllocateRGBA8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_RGB_8_3BPP:
        return gl.textureCubeStaticAllocateRGB8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_FORMAT_R_8_1BPP:
        return gl.textureCubeStaticAllocateR8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);

      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        throw new UnreachableCodeException(new AssertionError(type));
      }
    }

    throw new UnreachableCodeException();
  }

  private static TextureCubeStaticType allocateTextureCube_GLES2(
    final JCGLTexturesCubeStaticGLES2Type gl,
    final TextureFormat type,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final String name)
    throws JCGLException
  {
    switch (type) {
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      {
        return gl.textureCubeStaticAllocateRGBA4444(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      {
        return gl.textureCubeStaticAllocateRGBA5551(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }
      case TEXTURE_FORMAT_RGB_565_2BPP:
      {
        return gl.textureCubeStaticAllocateRGB565(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      }

      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_8_1BPP:

      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static void check2DConstraintsGL2ES3(
    final JCGLTextures2DStaticGL2ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
  {
    NullCheck.notNull(gl, "OpenGL interface");
    TextureLoaderImageIO.check2DConstraintsMain(
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
  }

  private static void check2DConstraintsGLES2(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
  {
    NullCheck.notNull(gl, "OpenGL interface");
    TextureLoaderImageIO.check2DConstraintsMain(
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
  }

  private static void check2DConstraintsGLI(
    final JCGLImplementationType gi,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
  {
    NullCheck.notNull(gi, "OpenGL interface");
    TextureLoaderImageIO.check2DConstraintsMain(
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
  }

  private static void check2DConstraintsMain(
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(stream, "Input stream");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(mag_filter, "Magnification filter");
    NullCheck.notNull(min_filter, "Minification filter");
  }

  private static void checkCubeConstraintsGI(
    final JCGLImplementationType gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
  {
    NullCheck.notNull(gl, "OpenGL interface");
    TextureLoaderImageIO.checkCubeConstraintsMain(
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);
  }

  private static void checkCubeConstraintsGL2ES3(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
  {
    NullCheck.notNull(gl, "OpenGL interface");
    TextureLoaderImageIO.checkCubeConstraintsMain(
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);
  }

  private static void checkCubeConstraintsMain(
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(wrap_r, "Wrap R mode");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(mag_filter, "Magnification filter");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(positive_z, "Positive Z stream");
    NullCheck.notNull(negative_z, "Negative Z stream");
    NullCheck.notNull(positive_y, "Positive Y stream");
    NullCheck.notNull(negative_y, "Negative Y stream");
    NullCheck.notNull(positive_x, "Positive X stream");
    NullCheck.notNull(negative_x, "Negative X stream");
  }

  private static void checkCubeImageSizes(
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x)
    throws JCGLExceptionParameterError
  {
    final int zw = positive_z.getWidth();
    final int zh = positive_z.getHeight();
    if (zw != zh) {
      final String s =
        String.format(
          "Width %d of positive Z image is not equal to height %d",
          zw,
          zh);
      assert s != null;
      throw new JCGLExceptionParameterError(s);
    }

    TextureLoaderImageIO.checkSizeMatch(
      positive_z,
      "Positive Z",
      negative_z,
      "Negative Z");
    TextureLoaderImageIO.checkSizeMatch(
      positive_z,
      "Positive Z",
      positive_y,
      "Positive Y");
    TextureLoaderImageIO.checkSizeMatch(
      positive_z,
      "Positive Z",
      negative_y,
      "Negative Y");
    TextureLoaderImageIO.checkSizeMatch(
      positive_z,
      "Positive Z",
      positive_x,
      "Positive X");
    TextureLoaderImageIO.checkSizeMatch(
      positive_z,
      "Positive Z",
      negative_x,
      "Negative X");
  }

  private static void checkSizeMatch(
    final BufferedImage x,
    final String x_name,
    final BufferedImage y,
    final String y_name)
    throws JCGLExceptionParameterError
  {
    final int xw = x.getWidth();
    final int yw = y.getWidth();

    if (xw != yw) {
      final StringBuilder b = new StringBuilder();
      b.append("Image dimension mismatch between images ");
      b.append(x_name);
      b.append(" and ");
      b.append(y_name);
      b.append(":\n");
      b.append("  Width of ");
      b.append(x_name);
      b.append(": ");
      b.append(xw);
      b.append("\n");
      b.append("  Width of");
      b.append(y_name);
      b.append(": ");
      b.append(yw);
      b.append("\n");

      final String s = b.toString();
      assert s != null;
      throw new JCGLExceptionParameterError(s);
    }

    final int xh = x.getHeight();
    final int yh = y.getHeight();

    if (xh != yh) {
      final StringBuilder b = new StringBuilder();
      b.append("Image dimension mismatch between images ");
      b.append(x_name);
      b.append(" and ");
      b.append(y_name);
      b.append(":\n");
      b.append("  Height of ");
      b.append(x_name);
      b.append(": ");
      b.append(xh);
      b.append("\n");
      b.append("  Height of");
      b.append(y_name);
      b.append(": ");
      b.append(yh);
      b.append("\n");

      final String s = b.toString();
      assert s != null;
      throw new JCGLExceptionParameterError(s);
    }
  }

  /**
   * Convert any image to RGBA using ImageIO's conversion functions.
   */

  private static BufferedImage customToRGBA(
    final BufferedImage image)
  {
    final BufferedImage converted =
      new BufferedImage(
        image.getWidth(),
        image.getHeight(),
        BufferedImage.TYPE_4BYTE_ABGR);
    final ColorConvertOp op = new ColorConvertOp(null);
    op.filter(image, converted);
    return converted;
  }

  private static BufferedImage getBufferedImage(
    final InputStream stream)
    throws IOException
  {
    final BufferedImage r = ImageIO.read(stream);
    if (r == null) {
      throw new IOException("Unknown image format");
    }
    return r;
  }

  /**
   * As Java2D takes (0, 0) to mean the top-left corner of the image, and
   * OpenGL takes (0, 0) to mean the bottom-left corner, the Y coordinates
   * need to be flipped when accessing data from Java2D images (effectively
   * loading the texture upside-down from Java2D's perspective).
   */

  private static int getImageARGBAsOpenGL(
    final BufferedImage image,
    final int x,
    final int y)
  {
    final int upper = image.getHeight() - 1;
    return image.getRGB(x, upper - y);
  }

  private static TextureFormat inferTextureTypeES2(
    final BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP;
      }
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        return TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
    }
  }

  private static TextureFormat inferTextureTypeGL2(
    final BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        return TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
    }
  }

  private static TextureFormat inferTextureTypeGLES3(
    final BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      {
        return TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP;
      }
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      {
        return TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP;
      }
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        return TextureFormat.TEXTURE_FORMAT_R_8_1BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
    }
  }

  /**
   * Create a new texture loader.
   */

  public static TextureLoaderType newTextureLoader(
    final LogUsableType log)
  {
    return new TextureLoaderImageIO(log, false);
  }

  /**
   * Create a new texture loader that will convert all textures to
   * alpha-premultiplied form upon loading.
   */

  public static TextureLoaderType newTextureLoaderWithAlphaPremultiplication(
    final LogUsableType log)
  {
    return new TextureLoaderImageIO(log, true);
  }

  private static final TextureFormat textureTypeES2Map(
    final TextureFormat type)
  {
    switch (type) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP;
      }
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      {
        return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
      }
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      {
        return type;
      }
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
    }

    throw new UnreachableCodeException();
  }

  private static void write1f(
    final BufferedImage image,
    final TextureUpdateType data)
    throws JCGLExceptionTypeError
  {
    final SpatialCursorWritable1fType cursor = data.getCursor1f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);
        final float r = ((argb >> 16) & 0xFF) * recip;
        cursor.put1f(r);
      }
    }
  }

  private static void write2f(
    final BufferedImage image,
    final TextureUpdateType data)
    throws JCGLExceptionTypeError
  {
    final SpatialCursorWritable2fType cursor = data.getCursor2f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM2F pixel = new VectorM2F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);

        final float fx = ((argb >> 16) & 0xFF) * recip;
        final float fy = ((argb >> 8) & 0xFF) * recip;
        pixel.set2F(fx, fy);
        cursor.put2f(pixel);
      }
    }
  }

  private static void write3f(
    final BufferedImage image,
    final TextureUpdateType data)
    throws JCGLExceptionTypeError
  {
    final SpatialCursorWritable3fType cursor = data.getCursor3f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM3F pixel = new VectorM3F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);

        final float fx = ((argb >> 16) & 0xFF) * recip;
        final float fy = ((argb >> 8) & 0xFF) * recip;
        final float fz = (argb & 0xFF) * recip;

        pixel.set3F(fx, fy, fz);
        cursor.put3f(pixel);
      }
    }
  }

  private static void write4f(
    final BufferedImage image,
    final TextureUpdateType data)
    throws JCGLExceptionTypeError
  {
    final SpatialCursorWritable4fType cursor = data.getCursor4f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM4F pixel = new VectorM4F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);

        final float fw = ((argb >> 24) & 0xFF) * recip;
        final float fx = ((argb >> 16) & 0xFF) * recip;
        final float fy = ((argb >> 8) & 0xFF) * recip;
        final float fz = (argb & 0xFF) * recip;

        pixel.set4F(fx, fy, fz, fw);
        cursor.put4f(pixel);
      }
    }
  }

  private static void write4fPremultiplied(
    final BufferedImage image,
    final TextureUpdateType data)
    throws JCGLExceptionTypeError
  {
    final SpatialCursorWritable4fType cursor = data.getCursor4f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM4F pixel = new VectorM4F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);

        final int a = (argb >> 24) & 0xFF;
        final int r = (argb >> 16) & 0xFF;
        final int g = (argb >> 8) & 0xFF;
        final int b = argb & 0xFF;

        final float fa = a * recip;
        final float fr = r * recip;
        final float fg = g * recip;
        final float fb = b * recip;

        final float fpr = fr * fa;
        final float fpg = fg * fa;
        final float fpb = fb * fa;

        pixel.set4F(fpr, fpg, fpb, fa);
        cursor.put4f(pixel);
      }
    }
  }

  private final LogUsableType log;
  private final LogUsableType log_alpha;
  private final boolean       premultiply_alpha;

  private TextureLoaderImageIO(
    final LogUsableType in_log,
    final boolean in_premultiply)
  {
    this.log = NullCheck.notNull(in_log, "Log").with("texture-loader");
    this.log_alpha = this.log.with("alpha");
    this.premultiply_alpha = in_premultiply;
  }

  private void cubeUpdateFacesLH(
    final JCGLTexturesCubeStaticCommonType gl,
    final TextureFormat type,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final TextureCubeStaticType texture)
    throws JCGLException
  {
    try {
      final TextureCubeStaticUpdateType data =
        TextureCubeStaticUpdate.newReplacingAll(texture);

      {
        this.writeImageDataWithConversionToTexture(positive_z, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Z,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_z, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(positive_y, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Y,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_y, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Y,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(positive_x, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_x, data, type);
        gl.textureCubeStaticUpdateLH(
          CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X,
          data);
      }

    } catch (final JCGLException e) {
      gl.textureCubeStaticDelete(texture);
      throw e;
    }
  }

  private void cubeUpdateFacesRH(
    final JCGLTexturesCubeStaticCommonType gl,
    final TextureFormat type,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final TextureCubeStaticType texture)
    throws JCGLException
  {
    try {
      final TextureCubeStaticUpdateType data =
        TextureCubeStaticUpdate.newReplacingAll(texture);

      {
        this.writeImageDataWithConversionToTexture(positive_z, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Z,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_z, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Z,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(positive_y, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Y,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_y, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Y,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(positive_x, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_POSITIVE_X,
          data);
      }

      {
        this.writeImageDataWithConversionToTexture(negative_x, data, type);
        gl.textureCubeStaticUpdateRH(
          CubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_X,
          data);
      }

    } catch (final JCGLException e) {
      gl.textureCubeStaticDelete(texture);
      throw e;
    }
  }

  @Override public Texture2DStaticType load2DStaticDepth16(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticDepth24(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticDepth32f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_DEPTH_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticInferred(
    final JCGLImplementationType gi,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws IOException,
      JCGLException
  {
    TextureLoaderImageIO.check2DConstraintsGLI(
      gi,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);

    final BufferedImage image = TextureLoaderImageIO.getBufferedImage(stream);

    return gi
      .implementationAccept(new JCGLImplementationVisitorType<Texture2DStaticType, IOException>() {
        @Override public Texture2DStaticType implementationIsGL2(
          final JCGLInterfaceGL2Type gl)
          throws JCGLException,

            IOException
        {
          return TextureLoaderImageIO.this.load2DStaticSpecificImage_GL2ES3(
            gl,
            TextureLoaderImageIO.inferTextureTypeGL2(image),
            wrap_s,
            wrap_t,
            min_filter,
            mag_filter,
            image,
            name);
        }

        @Override public Texture2DStaticType implementationIsGL3(
          final JCGLInterfaceGL3Type gl)
          throws JCGLException,

            IOException
        {
          return TextureLoaderImageIO.this.load2DStaticSpecificImage_GL3ES3(
            gl,
            TextureLoaderImageIO.inferTextureTypeGLES3(image),
            wrap_s,
            wrap_t,
            min_filter,
            mag_filter,
            image,
            name);
        }

        @Override public Texture2DStaticType implementationIsGLES2(
          final JCGLInterfaceGLES2Type gl)
          throws JCGLException,

            IOException
        {
          return TextureLoaderImageIO.this.load2DStaticSpecificImage_ES2(
            gl,
            TextureLoaderImageIO.inferTextureTypeES2(image),
            wrap_s,
            wrap_t,
            min_filter,
            mag_filter,
            image,
            name);
        }

        @Override public Texture2DStaticType implementationIsGLES3(
          final JCGLInterfaceGLES3Type gl)
          throws JCGLException,

            IOException
        {
          return TextureLoaderImageIO.this.load2DStaticSpecificImage_GL3ES3(
            gl,
            TextureLoaderImageIO.inferTextureTypeGLES3(image),
            wrap_s,
            wrap_t,
            min_filter,
            mag_filter,
            image,
            name);
        }
      });
  }

  @Override public Texture2DStaticType load2DStaticR16(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR16f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_16F_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR16I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_16I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR16U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_16U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR32f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR32I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_32I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR32U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_32U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR8(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR8I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_8I_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticR8U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_R_8U_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG16(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_16_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG16f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_16F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG16I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_16I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG16U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_16U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG32f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_32F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG32I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_32I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG32U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_32U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG8(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG8I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_8I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRG8U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RG_8U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB16(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_16_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB16f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_16F_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB16I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_16I_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB16U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_16U_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB32f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_32F_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB32I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_32I_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB32U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_32U_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB565(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGLES2(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_ES2(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB8(
    final JCGLTextures2DStaticGL2ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB8I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_8I_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGB8U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_8U_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA1010102(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_1010102_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA16(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_16_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA16f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_16F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA16I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_16I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA16U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_16U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA32f(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_32F_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA32I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_32I_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA32U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_32U_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA4444(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGLES2(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_ES2(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA5551(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGLES2(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);

    return this.load2DStaticSpecificImage_ES2(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA8(
    final JCGLTextures2DStaticGL2ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);

    return this.load2DStaticSpecificImage_GL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA8I(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_8I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStaticType load2DStaticRGBA8U(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final InputStream stream,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.check2DConstraintsGL2ES3(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return this.load2DStaticSpecificImage_GL3ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_8U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  private Texture2DStaticType load2DStaticSpecificImage_ES2(
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureFormat type,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage image,
    final String name)
    throws JCGLException
  {
    final Texture2DStaticType texture =
      TextureLoaderImageIO.allocateTexture2D_ES2(
        gl,
        type,
        image.getWidth(),
        image.getHeight(),
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    try {
      final Texture2DStaticUpdateType data =
        Texture2DStaticUpdate.newReplacingAll(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private Texture2DStaticType load2DStaticSpecificImage_GL2ES3(
    final JCGLTextures2DStaticGL2ES3Type gl,
    final TextureFormat type,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage image,
    final String name)
    throws JCGLException
  {
    final Texture2DStaticType texture =
      TextureLoaderImageIO.allocateTexture2D_GL2ES3(
        gl,
        type,
        image.getWidth(),
        image.getHeight(),
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    try {
      final Texture2DStaticUpdateType data =
        Texture2DStaticUpdate.newReplacingAll(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private Texture2DStaticType load2DStaticSpecificImage_GL3ES3(
    final JCGLTextures2DStaticGL3ES3Type gl,
    final TextureFormat type,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage image,
    final String name)
    throws JCGLException
  {
    final Texture2DStaticType texture =
      TextureLoaderImageIO.allocateTexture2D_GL3ES3(
        gl,
        type,
        image.getWidth(),
        image.getHeight(),
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    try {
      final Texture2DStaticUpdateType data =
        Texture2DStaticUpdate.newReplacingAll(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  @Override public TextureCubeStaticType loadCubeLHStaticInferred(
    final JCGLImplementationType gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkCubeConstraintsGI(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    final BufferedImage pos_z =
      TextureLoaderImageIO.getBufferedImage(positive_z);
    final BufferedImage neg_z =
      TextureLoaderImageIO.getBufferedImage(negative_z);
    final BufferedImage pos_y =
      TextureLoaderImageIO.getBufferedImage(positive_y);
    final BufferedImage neg_y =
      TextureLoaderImageIO.getBufferedImage(negative_y);
    final BufferedImage pos_x =
      TextureLoaderImageIO.getBufferedImage(positive_x);
    final BufferedImage neg_x =
      TextureLoaderImageIO.getBufferedImage(negative_x);

    return gl
      .implementationAccept(new JCGLImplementationVisitorType<TextureCubeStaticType, JCGLException>() {

        @Override public TextureCubeStaticType implementationIsGL2(
          final JCGLInterfaceGL2Type gl2)
          throws JCGLException,
            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeLHStaticSpecificImageGL2ES3(
              gl2,
              TextureLoaderImageIO.inferTextureTypeGL2(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGL3(
          final JCGLInterfaceGL3Type gl3)
          throws JCGLException,
            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeLHStaticSpecificImageGL3ES3(
              gl3,
              TextureLoaderImageIO.inferTextureTypeGLES3(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGLES2(
          final JCGLInterfaceGLES2Type gles2)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeLHStaticSpecificImageGLES2(
              gles2,
              TextureLoaderImageIO.inferTextureTypeES2(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGLES3(
          final JCGLInterfaceGLES3Type gles3)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeLHStaticSpecificImageGL3ES3(
              gles3,
              TextureLoaderImageIO.inferTextureTypeGLES3(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }
      });
  }

  @Override public TextureCubeStaticType loadCubeLHStaticRGB8(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkCubeConstraintsGL2ES3(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    return this.loadCubeLHStaticSpecificImageGL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(positive_z),
      TextureLoaderImageIO.getBufferedImage(negative_z),
      TextureLoaderImageIO.getBufferedImage(positive_y),
      TextureLoaderImageIO.getBufferedImage(negative_y),
      TextureLoaderImageIO.getBufferedImage(positive_x),
      TextureLoaderImageIO.getBufferedImage(negative_x),
      name);
  }

  @Override public TextureCubeStaticType loadCubeLHStaticRGBA8(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkCubeConstraintsGL2ES3(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    return this.loadCubeLHStaticSpecificImageGL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(positive_z),
      TextureLoaderImageIO.getBufferedImage(negative_z),
      TextureLoaderImageIO.getBufferedImage(positive_y),
      TextureLoaderImageIO.getBufferedImage(negative_y),
      TextureLoaderImageIO.getBufferedImage(positive_x),
      TextureLoaderImageIO.getBufferedImage(negative_x),
      name);
  }

  private TextureCubeStaticType loadCubeLHStaticSpecificImageGL2ES3(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GL2ES3(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesLH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  private TextureCubeStaticType loadCubeLHStaticSpecificImageGL3ES3(
    final JCGLTexturesCubeStaticGL3ES3Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GL3ES3(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesLH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  private TextureCubeStaticType loadCubeLHStaticSpecificImageGLES2(
    final JCGLTexturesCubeStaticGLES2Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GLES2(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesLH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  @Override public TextureCubeStaticType loadCubeRHStaticInferred(
    final JCGLImplementationType gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws IOException,
      JCGLException
  {
    TextureLoaderImageIO.checkCubeConstraintsGI(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    final BufferedImage pos_z =
      TextureLoaderImageIO.getBufferedImage(positive_z);
    final BufferedImage neg_z =
      TextureLoaderImageIO.getBufferedImage(negative_z);
    final BufferedImage pos_y =
      TextureLoaderImageIO.getBufferedImage(positive_y);
    final BufferedImage neg_y =
      TextureLoaderImageIO.getBufferedImage(negative_y);
    final BufferedImage pos_x =
      TextureLoaderImageIO.getBufferedImage(positive_x);
    final BufferedImage neg_x =
      TextureLoaderImageIO.getBufferedImage(negative_x);

    return gl
      .implementationAccept(new JCGLImplementationVisitorType<TextureCubeStaticType, JCGLException>() {

        @Override public TextureCubeStaticType implementationIsGL2(
          final JCGLInterfaceGL2Type gl2)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeRHStaticSpecificImageGL2ES3(
              gl2,
              TextureLoaderImageIO.inferTextureTypeGL2(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGL3(
          final JCGLInterfaceGL3Type gl3)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeRHStaticSpecificImageGL3ES3(
              gl3,
              TextureLoaderImageIO.inferTextureTypeGLES3(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGLES2(
          final JCGLInterfaceGLES2Type gles2)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeRHStaticSpecificImageGLES2(
              gles2,
              TextureLoaderImageIO.inferTextureTypeES2(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }

        @Override public TextureCubeStaticType implementationIsGLES3(
          final JCGLInterfaceGLES3Type gles3)
          throws JCGLException,

            JCGLException
        {
          return TextureLoaderImageIO.this
            .loadCubeRHStaticSpecificImageGL3ES3(
              gles3,
              TextureLoaderImageIO.inferTextureTypeGLES3(pos_z),
              wrap_r,
              wrap_s,
              wrap_t,
              min_filter,
              mag_filter,
              pos_z,
              neg_z,
              pos_y,
              neg_y,
              pos_x,
              neg_x,
              name);
        }
      });
  }

  @Override public TextureCubeStaticType loadCubeRHStaticRGB8(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkCubeConstraintsGL2ES3(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    return this.loadCubeRHStaticSpecificImageGL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(positive_z),
      TextureLoaderImageIO.getBufferedImage(negative_z),
      TextureLoaderImageIO.getBufferedImage(positive_y),
      TextureLoaderImageIO.getBufferedImage(negative_y),
      TextureLoaderImageIO.getBufferedImage(positive_x),
      TextureLoaderImageIO.getBufferedImage(negative_x),
      name);
  }

  @Override public TextureCubeStaticType loadCubeRHStaticRGBA8(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final String name)
    throws JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkCubeConstraintsGL2ES3(
      gl,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      name);

    return this.loadCubeRHStaticSpecificImageGL2ES3(
      gl,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(positive_z),
      TextureLoaderImageIO.getBufferedImage(negative_z),
      TextureLoaderImageIO.getBufferedImage(positive_y),
      TextureLoaderImageIO.getBufferedImage(negative_y),
      TextureLoaderImageIO.getBufferedImage(positive_x),
      TextureLoaderImageIO.getBufferedImage(negative_x),
      name);
  }

  private TextureCubeStaticType loadCubeRHStaticSpecificImageGL2ES3(
    final JCGLTexturesCubeStaticGL2ES3Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GL2ES3(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesRH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  private TextureCubeStaticType loadCubeRHStaticSpecificImageGL3ES3(
    final JCGLTexturesCubeStaticGL3ES3Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GL3ES3(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesRH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  private TextureCubeStaticType loadCubeRHStaticSpecificImageGLES2(
    final JCGLTexturesCubeStaticGLES2Type gl,
    final TextureFormat type,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter,
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x,
    final String name)
    throws JCGLException
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStaticType texture =
      TextureLoaderImageIO.allocateTextureCube_GLES2(
        gl,
        type,
        positive_z.getWidth(),
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter,
        name);

    this.cubeUpdateFacesRH(
      gl,
      type,
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x,
      texture);

    return texture;
  }

  private void writeImageDataWithConversionToTexture(
    final BufferedImage image,
    final TextureUpdateType data,
    final TextureFormat type)
    throws JCGLExceptionTypeError
  {
    if (type != data.getType()) {
      final String s =
        String.format("Expected type %s but got %s", type, data.getType());
      assert s != null;
      throw new JCGLExceptionTypeError(s);
    }

    switch (image.getType()) {
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        switch (type) {
          case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
            throw new UnreachableCodeException();

          case TEXTURE_FORMAT_RGBA_16I_8BPP:
          case TEXTURE_FORMAT_RGBA_16U_8BPP:
          case TEXTURE_FORMAT_RGBA_32I_16BPP:
          case TEXTURE_FORMAT_RGBA_32U_16BPP:
          case TEXTURE_FORMAT_RGBA_8I_4BPP:
          case TEXTURE_FORMAT_RGBA_8U_4BPP:
          case TEXTURE_FORMAT_RGBA_16_8BPP:
          case TEXTURE_FORMAT_RGBA_1010102_4BPP:
          case TEXTURE_FORMAT_RGBA_16F_8BPP:
          case TEXTURE_FORMAT_RGBA_32F_16BPP:
          case TEXTURE_FORMAT_RGBA_4444_2BPP:
          case TEXTURE_FORMAT_RGBA_5551_2BPP:
          case TEXTURE_FORMAT_RGBA_8_4BPP:
          {
            if (this.premultiply_alpha) {
              if (image.isAlphaPremultiplied()) {
                this.log_alpha
                  .debug("Premultiplication requested, but image is already premultiplied");
                TextureLoaderImageIO.write4f(image, data);
              } else {
                this.log_alpha.debug("Premultiplying alpha");
                TextureLoaderImageIO.write4fPremultiplied(image, data);
              }
            } else {
              this.log_alpha.debug("No premultiplication requested");
              TextureLoaderImageIO.write4f(image, data);
            }
            return;
          }
          case TEXTURE_FORMAT_RGB_16I_6BPP:
          case TEXTURE_FORMAT_RGB_16U_6BPP:
          case TEXTURE_FORMAT_RGB_32I_12BPP:
          case TEXTURE_FORMAT_RGB_32U_12BPP:
          case TEXTURE_FORMAT_RGB_8I_3BPP:
          case TEXTURE_FORMAT_RGB_8U_3BPP:
          case TEXTURE_FORMAT_RGB_16_6BPP:
          case TEXTURE_FORMAT_RGB_32F_12BPP:
          case TEXTURE_FORMAT_RGB_16F_6BPP:
          case TEXTURE_FORMAT_RGB_565_2BPP:
          case TEXTURE_FORMAT_RGB_8_3BPP:
          {
            TextureLoaderImageIO.write3f(image, data);
            return;
          }
          case TEXTURE_FORMAT_RG_16I_4BPP:
          case TEXTURE_FORMAT_RG_16U_4BPP:
          case TEXTURE_FORMAT_RG_32I_8BPP:
          case TEXTURE_FORMAT_RG_32U_8BPP:
          case TEXTURE_FORMAT_RG_8I_2BPP:
          case TEXTURE_FORMAT_RG_8U_2BPP:
          case TEXTURE_FORMAT_RG_16F_4BPP:
          case TEXTURE_FORMAT_RG_8_2BPP:
          case TEXTURE_FORMAT_RG_16_4BPP:
          case TEXTURE_FORMAT_RG_32F_8BPP:
          {
            TextureLoaderImageIO.write2f(image, data);
            return;
          }
          case TEXTURE_FORMAT_R_16I_2BPP:
          case TEXTURE_FORMAT_R_16U_2BPP:
          case TEXTURE_FORMAT_R_16_2BPP:
          case TEXTURE_FORMAT_R_32F_4BPP:
          case TEXTURE_FORMAT_R_32I_4BPP:
          case TEXTURE_FORMAT_R_32U_4BPP:
          case TEXTURE_FORMAT_R_8I_1BPP:
          case TEXTURE_FORMAT_R_8U_1BPP:
          case TEXTURE_FORMAT_R_16F_2BPP:
          case TEXTURE_FORMAT_R_8_1BPP:
          case TEXTURE_FORMAT_DEPTH_16_2BPP:
          case TEXTURE_FORMAT_DEPTH_24_4BPP:
          case TEXTURE_FORMAT_DEPTH_32F_4BPP:
          {
            TextureLoaderImageIO.write1f(image, data);
            return;
          }
        }

        throw new UnreachableCodeException();
      }

      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        final BufferedImage converted =
          TextureLoaderImageIO.customToRGBA(image);
        assert converted.getType() != BufferedImage.TYPE_CUSTOM;
        this.writeImageDataWithConversionToTexture(converted, data, type);
        return;
      }
    }
  }

}
