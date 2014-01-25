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

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM4F;

/**
 * An implementation of the {@link TextureLoader} interface using Java's
 * ImageIO interface.
 */

public final class TextureLoaderImageIO implements TextureLoader
{
  private static @Nonnull Texture2DStatic allocateTexture2D_ES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_TYPE_RGBA_4444_2BPP:
        return gl.texture2DStaticAllocateRGBA4444(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGBA_5551_2BPP:
        return gl.texture2DStaticAllocateRGBA5551(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_565_2BPP:
        return gl.texture2DStaticAllocateRGB565(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
        throw new UnreachableCodeException(
          new AssertionError(type.toString()));
    }

    throw new UnreachableCodeException();
  }

  private static @Nonnull Texture2DStatic allocateTexture2D_GL2ES3(
    final @Nonnull JCGLTextures2DStaticGL2ES3 gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_RGBA_8_4BPP:
        return gl.texture2DStaticAllocateRGBA8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_8_3BPP:
        return gl.texture2DStaticAllocateRGB8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
        throw new UnreachableCodeException();
    }

    throw new UnreachableCodeException();
  }

  private static @Nonnull Texture2DStatic allocateTexture2D_GL3ES3(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_TYPE_DEPTH_16_2BPP:
        return gl.texture2DStaticAllocateDepth16(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_DEPTH_24_4BPP:
        return gl.texture2DStaticAllocateDepth24(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
        return gl.texture2DStaticAllocateDepth32f(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGBA_8_4BPP:
        return gl.texture2DStaticAllocateRGBA8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_8_3BPP:
        return gl.texture2DStaticAllocateRGB8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RG_8_2BPP:
        return gl.texture2DStaticAllocateRG8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_R_8_1BPP:
        return gl.texture2DStaticAllocateR8(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGBA_32F_16BPP:
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
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
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
      case TEXTURE_TYPE_RGBA_16F_8BPP:
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
      case TEXTURE_TYPE_RGBA_16I_8BPP:
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
      case TEXTURE_TYPE_RGBA_16U_8BPP:
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
      case TEXTURE_TYPE_RGBA_16_8BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
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
      case TEXTURE_TYPE_RGBA_32U_16BPP:
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
      case TEXTURE_TYPE_RGBA_8I_4BPP:
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
      case TEXTURE_TYPE_RGBA_8U_4BPP:
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
      case TEXTURE_TYPE_RGB_16F_6BPP:
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
      case TEXTURE_TYPE_RGB_16I_6BPP:
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
      case TEXTURE_TYPE_RGB_16U_6BPP:
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
      case TEXTURE_TYPE_RGB_16_6BPP:
      {
        throw new UnreachableCodeException();
      }

      case TEXTURE_TYPE_RGB_32F_12BPP:
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
      case TEXTURE_TYPE_RGB_32I_12BPP:
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
      case TEXTURE_TYPE_RGB_32U_12BPP:
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
      case TEXTURE_TYPE_RGB_8I_3BPP:
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
      case TEXTURE_TYPE_RGB_8U_3BPP:
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
      case TEXTURE_TYPE_RG_16F_4BPP:
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
      case TEXTURE_TYPE_RG_16I_4BPP:
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
      case TEXTURE_TYPE_RG_16U_4BPP:
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
      case TEXTURE_TYPE_RG_16_4BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RG_32F_8BPP:
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
      case TEXTURE_TYPE_RG_32I_8BPP:
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
      case TEXTURE_TYPE_RG_32U_8BPP:
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
      case TEXTURE_TYPE_RG_8I_2BPP:
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
      case TEXTURE_TYPE_RG_8U_2BPP:
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
      case TEXTURE_TYPE_R_16F_2BPP:
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
      case TEXTURE_TYPE_R_16I_2BPP:
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
      case TEXTURE_TYPE_R_16U_2BPP:
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
      case TEXTURE_TYPE_R_16_2BPP:
      {
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_R_32F_4BPP:
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
      case TEXTURE_TYPE_R_32I_4BPP:
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
      case TEXTURE_TYPE_R_32U_4BPP:
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
      case TEXTURE_TYPE_R_8I_1BPP:
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
      case TEXTURE_TYPE_R_8U_1BPP:
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

  private static @Nonnull TextureCubeStatic allocateTextureCube_GL2ES3(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureType type,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_TYPE_RGBA_8_4BPP:
        return gl.textureCubeStaticAllocateRGBA8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_8_3BPP:
        return gl.textureCubeStaticAllocateRGB8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static @Nonnull TextureCubeStatic allocateTextureCube_GL3ES3(
    final @Nonnull JCGLTexturesCubeStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_TYPE_RGBA_8_4BPP:
        return gl.textureCubeStaticAllocateRGBA8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_8_3BPP:
        return gl.textureCubeStaticAllocateRGB8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_R_8_1BPP:
        return gl.textureCubeStaticAllocateR8(
          name,
          size,
          wrap_r,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);

      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        throw new UnreachableCodeException(new AssertionError(type));
      }
    }

    throw new UnreachableCodeException();
  }

  private static @Nonnull TextureCubeStatic allocateTextureCube_GLES2(
    final @Nonnull JCGLTexturesCubeStaticGLES2 gl,
    final @Nonnull TextureType type,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_RGBA_4444_2BPP:
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
      case TEXTURE_TYPE_RGBA_5551_2BPP:
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
      case TEXTURE_TYPE_RGB_565_2BPP:
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

      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:

      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static void check2DConstraintsGL2ES3(
    final @Nonnull JCGLTextures2DStaticGL2ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    TextureLoaderImageIO.check2DConstraintsMain(
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
  }

  private static void check2DConstraintsGLES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    TextureLoaderImageIO.check2DConstraintsMain(
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
  }

  private static void check2DConstraintsGLI(
    final @Nonnull JCGLImplementation gi,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gi, "OpenGL interface");
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
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");
  }

  private static void checkCubeConstraintsGI(
    final @Nonnull JCGLImplementation gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
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
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
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
    final CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(wrap_r, "Wrap R mode");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");
    Constraints.constrainNotNull(positive_z, "Positive Z stream");
    Constraints.constrainNotNull(negative_z, "Negative Z stream");
    Constraints.constrainNotNull(positive_y, "Positive Y stream");
    Constraints.constrainNotNull(negative_y, "Negative Y stream");
    Constraints.constrainNotNull(positive_x, "Positive X stream");
    Constraints.constrainNotNull(negative_x, "Negative X stream");
  }

  private static void checkCubeImageSizes(
    final BufferedImage positive_z,
    final BufferedImage negative_z,
    final BufferedImage positive_y,
    final BufferedImage negative_y,
    final BufferedImage positive_x,
    final BufferedImage negative_x)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      positive_z.getWidth() == positive_z.getHeight(),
      "Positive Z width equals height");

    TextureLoaderImageIO.checkSizeMatch(positive_z, negative_z, "negative Z");
    TextureLoaderImageIO.checkSizeMatch(positive_z, positive_y, "positive Y");
    TextureLoaderImageIO.checkSizeMatch(positive_z, negative_y, "negative Y");
    TextureLoaderImageIO.checkSizeMatch(positive_z, positive_x, "positive X");
    TextureLoaderImageIO.checkSizeMatch(positive_z, negative_x, "negative X");
  }

  private static void checkSizeMatch(
    final @Nonnull BufferedImage x,
    final @Nonnull BufferedImage y,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(x.getWidth() == y.getWidth(), "Width of "
      + name
      + " matches positive Z");
    Constraints.constrainArbitrary(
      x.getHeight() == y.getHeight(),
      "Height of " + name + " matches positive Z");
  }

  /**
   * Convert any image to RGBA using ImageIO's conversion functions.
   */

  private static @Nonnull BufferedImage customToRGBA(
    final @Nonnull BufferedImage image)
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

  private static @Nonnull BufferedImage getBufferedImage(
    final @Nonnull InputStream stream)
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
    final @Nonnull BufferedImage image,
    final int x,
    final int y)
  {
    final int upper = image.getHeight() - 1;
    return image.getRGB(x, upper - y);
  }

  private static @Nonnull TextureType inferTextureTypeES2(
    final @Nonnull BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureType.TEXTURE_TYPE_RGBA_4444_2BPP;
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
        return TextureType.TEXTURE_TYPE_RGB_565_2BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
      }
    }
  }

  private static @Nonnull TextureType inferTextureTypeGL2(
    final @Nonnull BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
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
        return TextureType.TEXTURE_TYPE_RGB_8_3BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
      }
    }
  }

  private static @Nonnull TextureType inferTextureTypeGLES3(
    final @Nonnull BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
      }
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      {
        return TextureType.TEXTURE_TYPE_RGB_565_2BPP;
      }
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      {
        return TextureType.TEXTURE_TYPE_RGB_8_3BPP;
      }
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        return TextureType.TEXTURE_TYPE_R_8_1BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
      }
    }
  }

  /**
   * Create a new texture loader.
   * 
   * @throws ConstraintError
   *           Iff <code>log == null</code>.
   */

  public static @Nonnull TextureLoaderImageIO newTextureLoader(
    final @Nonnull Log log)
    throws ConstraintError
  {
    return new TextureLoaderImageIO(log, false);
  }

  /**
   * Create a new texture loader that will convert all textures to
   * alpha-premultiplied form upon loading.
   * 
   * @throws ConstraintError
   *           Iff <code>log == null</code>.
   */

  public static @Nonnull
    TextureLoaderImageIO
    newTextureLoaderWithAlphaPremultiplication(
      final @Nonnull Log log)
      throws ConstraintError
  {
    return new TextureLoaderImageIO(log, true);
  }

  private static final @Nonnull TextureType textureTypeES2Map(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        throw new UnreachableCodeException();

      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return TextureType.TEXTURE_TYPE_RGB_8_3BPP;
      }
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8_4BPP;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return type;
      }
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
    }

    throw new UnreachableCodeException();
  }

  private static void write1f(
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data)
    throws ConstraintError
  {
    final SpatialCursorWritable1f cursor = data.getCursor1f();
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
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data)
    throws ConstraintError
  {
    final SpatialCursorWritable2f cursor = data.getCursor2f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM2F pixel = new VectorM2F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);
        pixel.x = ((argb >> 16) & 0xFF) * recip;
        pixel.y = ((argb >> 8) & 0xFF) * recip;
        cursor.put2f(pixel);
      }
    }
  }

  private static void write3f(
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data)
    throws ConstraintError
  {
    final SpatialCursorWritable3f cursor = data.getCursor3f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM3F pixel = new VectorM3F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);
        pixel.x = ((argb >> 16) & 0xFF) * recip;
        pixel.y = ((argb >> 8) & 0xFF) * recip;
        pixel.z = (argb & 0xFF) * recip;
        cursor.put3f(pixel);
      }
    }
  }

  private static void write4f(
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data)
    throws ConstraintError
  {
    final SpatialCursorWritable4f cursor = data.getCursor4f();
    final int height = image.getHeight();
    final int width = image.getWidth();
    final float recip = 1.0f / 256.0f;
    final VectorM4F pixel = new VectorM4F();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb =
          TextureLoaderImageIO.getImageARGBAsOpenGL(image, x, y);
        pixel.w = ((argb >> 24) & 0xFF) * recip;
        pixel.x = ((argb >> 16) & 0xFF) * recip;
        pixel.y = ((argb >> 8) & 0xFF) * recip;
        pixel.z = (argb & 0xFF) * recip;
        cursor.put4f(pixel);
      }
    }
  }

  private static void write4fPremultiplied(
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data)
    throws ConstraintError
  {
    final SpatialCursorWritable4f cursor = data.getCursor4f();
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

        pixel.w = fa;
        pixel.x = fpr;
        pixel.y = fpg;
        pixel.z = fpb;
        cursor.put4f(pixel);
      }
    }
  }

  private final @Nonnull Log log;
  private final @Nonnull Log log_alpha;

  private final boolean      premultiply_alpha;

  private TextureLoaderImageIO(
    final @Nonnull Log log,
    final boolean premultiply_alpha)
    throws ConstraintError
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "Log"), "texture-loader");
    this.log_alpha = new Log(this.log, "alpha");

    this.premultiply_alpha = premultiply_alpha;
  }

  private void cubeUpdateFacesLH(
    final @Nonnull JCGLTexturesCubeStaticCommon gl,
    final @Nonnull TextureType type,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    try {

      final TextureCubeWritableData data =
        new TextureCubeWritableData(texture);

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

    } catch (final JCGLRuntimeException e) {
      gl.textureCubeStaticDelete(texture);
      throw e;
    }
  }

  private void cubeUpdateFacesRH(
    final @Nonnull JCGLTexturesCubeStaticCommon gl,
    final @Nonnull TextureType type,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    try {

      final TextureCubeWritableData data =
        new TextureCubeWritableData(texture);

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

    } catch (final JCGLRuntimeException e) {
      gl.textureCubeStaticDelete(texture);
      throw e;
    }
  }

  @Override public @Nonnull Texture2DStatic load2DStaticDepth16(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_DEPTH_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticDepth24(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_DEPTH_24_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticDepth32f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_DEPTH_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStatic load2DStaticInferred(
    final @Nonnull JCGLImplementation gi,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      IOException,
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
      .implementationAccept(new JCGLImplementationVisitor<Texture2DStatic, IOException>() {
        @SuppressWarnings("synthetic-access") @Override public @Nonnull
          Texture2DStatic
          implementationIsGL2(
            final @Nonnull JCGLInterfaceGL2 gl)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public @Nonnull
          Texture2DStatic
          implementationIsGL3(
            final @Nonnull JCGLInterfaceGL3 gl)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public @Nonnull
          Texture2DStatic
          implementationIsGLES2(
            final @Nonnull JCGLInterfaceGLES2 gl)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public @Nonnull
          Texture2DStatic
          implementationIsGLES3(
            final @Nonnull JCGLInterfaceGLES3 gl)
            throws JCGLException,
              ConstraintError,
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

  @Override public @Nonnull Texture2DStatic load2DStaticR16(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR16f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_16F_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR16I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_16I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR16U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_16U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR32f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR32I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_32I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR32U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_32U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR8(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_8_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR8I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_8I_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticR8U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_R_8U_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG16(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_16_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG16f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_16F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG16I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_16I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG16U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_16U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG32f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_32F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG32I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_32I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG32U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_32U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG8(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_8_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG8I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_8I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG8U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RG_8U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB16(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_16_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB16f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_16F_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB16I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_16I_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB16U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_16U_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB32f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_32F_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB32I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_32I_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB32U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_32U_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB565(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_565_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStatic load2DStaticRGB8(
    final @Nonnull JCGLTextures2DStaticGL2ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_8_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB8I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_8I_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB8U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_8U_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public Texture2DStatic load2DStaticRGBA1010102(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_1010102_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA16(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_16_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA16f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_16F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA16I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_16I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA16U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_16U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA32f(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_32F_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA32I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_32I_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA32U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_32U_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA4444(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_4444_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA5551(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_5551_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA8(
    final @Nonnull JCGLTextures2DStaticGL2ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA8I(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_8I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA8U(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_8U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  private @Nonnull Texture2DStatic load2DStaticSpecificImage_ES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final Texture2DStatic texture =
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
      final Texture2DWritableData data = new Texture2DWritableData(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLRuntimeException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private @Nonnull Texture2DStatic load2DStaticSpecificImage_GL2ES3(
    final @Nonnull JCGLTextures2DStaticGL2ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    final Texture2DStatic texture =
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
      final Texture2DWritableData data = new Texture2DWritableData(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLRuntimeException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private @Nonnull Texture2DStatic load2DStaticSpecificImage_GL3ES3(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    final Texture2DStatic texture =
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
      final Texture2DWritableData data = new Texture2DWritableData(texture);
      this.writeImageDataWithConversionToTexture(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLRuntimeException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  @Override public TextureCubeStatic loadCubeLHStaticInferred(
    final @Nonnull JCGLImplementation gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
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
      .implementationAccept(new JCGLImplementationVisitor<TextureCubeStatic, JCGLException>() {

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGL2(
            final @Nonnull JCGLInterfaceGL2 gl2)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGL3(
            final @Nonnull JCGLInterfaceGL3 gl3)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGLES2(
            final @Nonnull JCGLInterfaceGLES2 gles2)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGLES3(
            final @Nonnull JCGLInterfaceGLES3 gles3)
            throws JCGLException,
              ConstraintError,
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

  @Override public @Nonnull TextureCubeStatic loadCubeLHStaticRGB8(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_8_3BPP,
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

  @Override public @Nonnull TextureCubeStatic loadCubeLHStaticRGBA8(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
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

  private @Nonnull TextureCubeStatic loadCubeLHStaticSpecificImageGL2ES3(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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

  private @Nonnull TextureCubeStatic loadCubeLHStaticSpecificImageGL3ES3(
    final @Nonnull JCGLTexturesCubeStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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

  private @Nonnull TextureCubeStatic loadCubeLHStaticSpecificImageGLES2(
    final @Nonnull JCGLTexturesCubeStaticGLES2 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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

  @Override public TextureCubeStatic loadCubeRHStaticInferred(
    final @Nonnull JCGLImplementation gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      IOException,
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
      .implementationAccept(new JCGLImplementationVisitor<TextureCubeStatic, JCGLException>() {

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGL2(
            final @Nonnull JCGLInterfaceGL2 gl2)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGL3(
            final @Nonnull JCGLInterfaceGL3 gl3)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGLES2(
            final @Nonnull JCGLInterfaceGLES2 gles2)
            throws JCGLException,
              ConstraintError,
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

        @SuppressWarnings("synthetic-access") @Override public
          TextureCubeStatic
          implementationIsGLES3(
            final @Nonnull JCGLInterfaceGLES3 gles3)
            throws JCGLException,
              ConstraintError,
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

  @Override public @Nonnull TextureCubeStatic loadCubeRHStaticRGB8(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGB_8_3BPP,
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

  @Override public @Nonnull TextureCubeStatic loadCubeRHStaticRGBA8(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveZ> positive_z,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeZ> negative_z,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveY> positive_y,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeY> negative_y,
    final @Nonnull CubeMapFaceInputStream<CMFKPositiveX> positive_x,
    final @Nonnull CubeMapFaceInputStream<CMFKNegativeX> negative_x,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLRuntimeException,
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
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
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

  private @Nonnull TextureCubeStatic loadCubeRHStaticSpecificImageGL2ES3(
    final @Nonnull JCGLTexturesCubeStaticGL2ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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

  private @Nonnull TextureCubeStatic loadCubeRHStaticSpecificImageGL3ES3(
    final @Nonnull JCGLTexturesCubeStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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

  private @Nonnull TextureCubeStatic loadCubeRHStaticSpecificImageGLES2(
    final @Nonnull JCGLTexturesCubeStaticGLES2 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage positive_z,
    final @Nonnull BufferedImage negative_z,
    final @Nonnull BufferedImage positive_y,
    final @Nonnull BufferedImage negative_y,
    final @Nonnull BufferedImage positive_x,
    final @Nonnull BufferedImage negative_x,
    final @Nonnull String name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    TextureLoaderImageIO.checkCubeImageSizes(
      positive_z,
      negative_z,
      positive_y,
      negative_y,
      positive_x,
      negative_x);

    final TextureCubeStatic texture =
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
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWritableData data,
    final @Nonnull TextureType type)
    throws ConstraintError
  {
    if (type != data.getType()) {
      Constraints.constrainArbitrary(
        type == data.getType(),
        "Texture type matches");
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
          case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
            throw new UnreachableCodeException();

          case TEXTURE_TYPE_RGBA_16I_8BPP:
          case TEXTURE_TYPE_RGBA_16U_8BPP:
          case TEXTURE_TYPE_RGBA_32I_16BPP:
          case TEXTURE_TYPE_RGBA_32U_16BPP:
          case TEXTURE_TYPE_RGBA_8I_4BPP:
          case TEXTURE_TYPE_RGBA_8U_4BPP:
          case TEXTURE_TYPE_RGBA_16_8BPP:
          case TEXTURE_TYPE_RGBA_1010102_4BPP:
          case TEXTURE_TYPE_RGBA_16F_8BPP:
          case TEXTURE_TYPE_RGBA_32F_16BPP:
          case TEXTURE_TYPE_RGBA_4444_2BPP:
          case TEXTURE_TYPE_RGBA_5551_2BPP:
          case TEXTURE_TYPE_RGBA_8_4BPP:
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
          case TEXTURE_TYPE_RGB_16I_6BPP:
          case TEXTURE_TYPE_RGB_16U_6BPP:
          case TEXTURE_TYPE_RGB_32I_12BPP:
          case TEXTURE_TYPE_RGB_32U_12BPP:
          case TEXTURE_TYPE_RGB_8I_3BPP:
          case TEXTURE_TYPE_RGB_8U_3BPP:
          case TEXTURE_TYPE_RGB_16_6BPP:
          case TEXTURE_TYPE_RGB_32F_12BPP:
          case TEXTURE_TYPE_RGB_16F_6BPP:
          case TEXTURE_TYPE_RGB_565_2BPP:
          case TEXTURE_TYPE_RGB_8_3BPP:
          {
            TextureLoaderImageIO.write3f(image, data);
            return;
          }
          case TEXTURE_TYPE_RG_16I_4BPP:
          case TEXTURE_TYPE_RG_16U_4BPP:
          case TEXTURE_TYPE_RG_32I_8BPP:
          case TEXTURE_TYPE_RG_32U_8BPP:
          case TEXTURE_TYPE_RG_8I_2BPP:
          case TEXTURE_TYPE_RG_8U_2BPP:
          case TEXTURE_TYPE_RG_16F_4BPP:
          case TEXTURE_TYPE_RG_8_2BPP:
          case TEXTURE_TYPE_RG_16_4BPP:
          case TEXTURE_TYPE_RG_32F_8BPP:
          {
            TextureLoaderImageIO.write2f(image, data);
            return;
          }
          case TEXTURE_TYPE_R_16I_2BPP:
          case TEXTURE_TYPE_R_16U_2BPP:
          case TEXTURE_TYPE_R_16_2BPP:
          case TEXTURE_TYPE_R_32F_4BPP:
          case TEXTURE_TYPE_R_32I_4BPP:
          case TEXTURE_TYPE_R_32U_4BPP:
          case TEXTURE_TYPE_R_8I_1BPP:
          case TEXTURE_TYPE_R_8U_1BPP:
          case TEXTURE_TYPE_R_16F_2BPP:
          case TEXTURE_TYPE_R_8_1BPP:
          case TEXTURE_TYPE_DEPTH_16_2BPP:
          case TEXTURE_TYPE_DEPTH_24_4BPP:
          case TEXTURE_TYPE_DEPTH_32F_4BPP:
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
