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

/**
 * An implementation of the {@link TextureLoader} interface using Java's
 * ImageIO interface.
 */

public final class TextureLoaderImageIO implements TextureLoader
{
  private static @Nonnull Texture2DStatic allocateTexture_ES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLException,
      ConstraintError
  {
    switch (type) {
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
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return gl.texture2DStaticAllocateRGBA8888(
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
      case TEXTURE_TYPE_RGB_888_3BPP:
        return gl.texture2DStaticAllocateRGB888(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
        throw new UnreachableCodeException();
    }

    throw new UnreachableCodeException();
  }

  private static @Nonnull Texture2DStatic allocateTexture_GL3ES3(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLException,
      ConstraintError
  {
    switch (type) {
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
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return gl.texture2DStaticAllocateRGBA8888(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_888_3BPP:
        return gl.texture2DStaticAllocateRGB888(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RG_88_2BPP:
        return gl.texture2DStaticAllocateRG88(
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
    }

    throw new UnreachableCodeException();
  }

  private static Texture2DStatic allocateTextureCommon(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureType type,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull String name)
    throws JCGLException,
      ConstraintError
  {
    switch (type) {
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return gl.texture2DStaticAllocateRGBA8888(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_RGB_888_3BPP:
        return gl.texture2DStaticAllocateRGB888(
          name,
          width,
          height,
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static void checkConstraints(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");
  }

  /**
   * Convert any RGBA/ABGR image to an RGBA texture.
   */

  private static @Nonnull Texture2DWritableData convertABGRToRGBAGeneric(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable4i cursor = data.getCursor4i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int a = (argb >> 24) & 0xff;
        final int r = (argb >> 16) & 0xff;
        final int g = (argb >> 8) & 0xff;
        final int b = argb & 0xff;
        cursor.put4i(r, g, b, a);
      }
    }

    return data;
  }

  /**
   * Convert any RGBA/ABGR image to an RGB texture.
   */

  private static @Nonnull Texture2DWritableData convertABGRToRGBGeneric(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable3i cursor = data.getCursor3i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        final int g = (argb >> 8) & 0xff;
        final int b = argb & 0xff;
        cursor.put3i(r, g, b);
      }
    }

    return data;
  }

  /**
   * Convert any RGB/BGR image to an RGBA texture.
   */

  private static @Nonnull Texture2DWritableData convertBGRToRGBAGeneric(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable4i cursor = data.getCursor4i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int a = 0xff;
        final int r = (argb >> 16) & 0xff;
        final int g = (argb >> 8) & 0xff;
        final int b = argb & 0xff;
        cursor.put4i(r, g, b, a);
      }
    }

    return data;
  }

  /**
   * Convert any RGB/BGR image to an RGB texture.
   */

  private static @Nonnull Texture2DWritableData convertBGRToRGBGeneric(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable3i cursor = data.getCursor3i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        final int g = (argb >> 8) & 0xff;
        final int b = argb & 0xff;
        cursor.put3i(r, g, b);
      }
    }

    return data;
  }

  /**
   * Convert a greyscale image to a single-channel red texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToR(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable1i cursor = data.getCursor1i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        cursor.put1i(r);
      }
    }

    return data;
  }

  /**
   * Convert a greyscale image to an RG texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToRG(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable2i cursor = data.getCursor2i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        cursor.put2i(r, r);
      }
    }

    return data;
  }

  /**
   * Convert a greyscale image to an RGB texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToRGB(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable3i cursor = data.getCursor3i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int g = (argb >> 8) & 0xff;
        cursor.put3i(g, g, g);
      }
    }

    return data;
  }

  /**
   * Convert a greyscale image to an RGBA texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToRGBA(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable4i cursor = data.getCursor4i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int g = (argb >> 8) & 0xff;
        cursor.put4i(g, g, g, g);
      }
    }

    return data;
  }

  /**
   * Convert an (A)RGB image to single channel floating point texture.
   * 
   * The red channel of the image is used as the source for the resulting
   * channel.
   */

  private static @Nonnull Texture2DWritableData convertRGBToR8_1fGeneric(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable1f cursor = data.getCursor1f();

    final float recip = 1.0f / 255.0f;
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        final float rf = r;
        cursor.put1f(rf * recip);
      }
    }

    return data;
  }

  /**
   * Convert an (A)RGB image to single channel texture.
   * 
   * The red channel of the image is used as the source for the resulting
   * channel.
   */

  private static @Nonnull Texture2DWritableData convertRGBToR8_1Generic(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable1i cursor = data.getCursor1i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        cursor.put1i(r);
      }
    }

    return data;
  }

  /**
   * Convert an (A)RGB image to a two-channel RG texture.
   * 
   * The blue channel is simply discarded.
   * 
   * @throws ConstraintError
   */

  private static Texture2DWritableData convertRGBToRG88_2Generic(
    final BufferedImage image,
    final Texture2DWritableData data)
    throws ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final SpatialCursorWritable2i cursor = data.getCursor2i();

    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        final int argb = image.getRGB(x, y);
        final int r = (argb >> 16) & 0xff;
        final int g = (argb >> 8) & 0xff;
        cursor.put2i(r, g);
      }
    }

    return data;
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

  private static @Nonnull TextureType inferTextureTypeCommon(
    final @Nonnull BufferedImage image)
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
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
        return TextureType.TEXTURE_TYPE_RGB_888_3BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
      }
    }
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
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
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
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        return TextureType.TEXTURE_TYPE_RGB_888_3BPP;
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
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
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
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
        return TextureType.TEXTURE_TYPE_RGB_888_3BPP;
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
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
      }
    }
  }

  private static @Nonnull Texture2DStatic load2DStaticSpecificImage_ES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException
  {
    final Texture2DStatic texture =
      TextureLoaderImageIO.allocateTexture_ES2(
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
      TextureLoaderImageIO.writeImageDataWithConversion(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private static @Nonnull Texture2DStatic load2DStaticSpecificImage_GLES3(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws JCGLException,
      ConstraintError
  {
    final Texture2DStatic texture =
      TextureLoaderImageIO.allocateTexture_GL3ES3(
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
      TextureLoaderImageIO.writeImageDataWithConversion(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  private static @Nonnull Texture2DStatic load2DStaticSpecificImageCommon(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws JCGLException,
      ConstraintError
  {
    final Texture2DStatic texture =
      TextureLoaderImageIO.allocateTextureCommon(
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
      TextureLoaderImageIO.writeImageDataWithConversion(image, data, type);
      gl.texture2DStaticUpdate(data);
      return texture;
    } catch (final JCGLException e) {
      gl.texture2DStaticDelete(texture);
      throw e;
    }
  }

  public static final @Nonnull TextureType textureTypeES2Map(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return TextureType.TEXTURE_TYPE_RGB_888_3BPP;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return type;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Update <code>data</code> with the image data in <code>image</code>, doing
   * an appropriate conversion to the type <code>type</code>.
   */

  private static void writeImageDataWithConversion(
    final @Nonnull BufferedImage image,
    final @Nonnull Texture2DWritableData data,
    final @Nonnull TextureType type)
    throws ConstraintError
  {
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
          case TEXTURE_TYPE_DEPTH_16_2BPP:
          case TEXTURE_TYPE_DEPTH_24_4BPP:
          {
            TextureLoaderImageIO.convertRGBToR8_1Generic(image, data);
            return;
          }
          case TEXTURE_TYPE_DEPTH_32F_4BPP:
          {
            TextureLoaderImageIO.convertRGBToR8_1fGeneric(image, data);
            return;
          }
          case TEXTURE_TYPE_RGBA_4444_2BPP:
          case TEXTURE_TYPE_RGBA_5551_2BPP:
          case TEXTURE_TYPE_RGBA_8888_4BPP:
          {
            TextureLoaderImageIO.convertABGRToRGBAGeneric(image, data);
            return;
          }
          case TEXTURE_TYPE_RGB_565_2BPP:
          case TEXTURE_TYPE_RGB_888_3BPP:
          {
            TextureLoaderImageIO.convertABGRToRGBGeneric(image, data);
            return;
          }
          case TEXTURE_TYPE_RG_88_2BPP:
          {
            TextureLoaderImageIO.convertRGBToRG88_2Generic(image, data);
            return;
          }
          case TEXTURE_TYPE_R_8_1BPP:
          {
            TextureLoaderImageIO.convertRGBToR8_1Generic(image, data);
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
        TextureLoaderImageIO.writeImageDataWithConversion(
          converted,
          data,
          type);
        return;
      }
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
      gl,
      TextureType.TEXTURE_TYPE_DEPTH_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticInferredCommon(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    final BufferedImage image = TextureLoaderImageIO.getBufferedImage(stream);
    final TextureType type =
      TextureLoaderImageIO.inferTextureTypeCommon(image);

    return TextureLoaderImageIO.load2DStaticSpecificImageCommon(
      gl,
      type,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      image,
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticInferredGL3ES3(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    final BufferedImage image = TextureLoaderImageIO.getBufferedImage(stream);
    final TextureType type =
      TextureLoaderImageIO.inferTextureTypeGLES3(image);

    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
      gl,
      type,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      image,
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticInferredGLES2(
    final @Nonnull JCGLTextures2DStaticGLES2 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    final BufferedImage image = TextureLoaderImageIO.getBufferedImage(stream);
    final TextureType type = TextureLoaderImageIO.inferTextureTypeES2(image);

    return TextureLoaderImageIO.load2DStaticSpecificImage_ES2(
      gl,
      type,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      image,
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
      gl,
      TextureType.TEXTURE_TYPE_R_8_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRG88(
    final @Nonnull JCGLTextures2DStaticGL3ES3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_GLES3(
      gl,
      TextureType.TEXTURE_TYPE_RG_88_2BPP,
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_ES2(
      gl,
      TextureType.TEXTURE_TYPE_RGB_565_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGB888(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImageCommon(
      gl,
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_ES2(
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
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);
    return TextureLoaderImageIO.load2DStaticSpecificImage_ES2(
      gl,
      TextureType.TEXTURE_TYPE_RGBA_5551_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticRGBA8888(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException,
      IOException
  {
    TextureLoaderImageIO.checkConstraints(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      stream,
      name);

    return TextureLoaderImageIO.load2DStaticSpecificImageCommon(
      gl,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      TextureLoaderImageIO.getBufferedImage(stream),
      name);
  }
}
