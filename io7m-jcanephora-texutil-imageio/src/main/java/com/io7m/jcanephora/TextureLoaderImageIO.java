/*
 * Copyright © 2012 http://io7m.com
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
   * Convert a greyscale image to an alpha texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToAlpha(
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
   * Convert a greyscale image to a luminance texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToLuminance(
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
        final int g = (argb >> 8) & 0xff;
        cursor.put1i(g);
      }
    }

    return data;
  }

  /**
   * Convert a greyscale image to a luminance/alpha texture.
   */

  private static @Nonnull Texture2DWritableData convertGreyToLuminanceAlpha(
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
        final int g = (argb >> 8) & 0xff;
        cursor.put2i(g, g);
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
   * Convert an RGB image to an alpha texture.
   * 
   * The red channel of the image is used as the source for the resulting
   * alpha channel.
   */

  private static @Nonnull Texture2DWritableData convertRGBToAlpha8_1Generic(
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
   * Convert an RGB image to a luminance texture.
   * 
   * The green channel of the image is used as the source for the resulting
   * luminance channel.
   */

  private static @Nonnull
    Texture2DWritableData
    convertRGBToLuminance8_1Generic(
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
        final int g = (argb >> 8) & 0xff;
        cursor.put1i(g);
      }
    }

    return data;
  }

  /**
   * Convert an RGB image to a two-channel luminance/alpha texture.
   * 
   * The red channel of the image is used as the source for the resulting
   * alpha channel.
   * 
   * The green channel of the image is used as the source for the resulting
   * luminance channel.
   */

  private static @Nonnull
    Texture2DWritableData
    convertRGBToLuminance88_2Generic(
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
        final int g = (argb >> 8) & 0xff;
        cursor.put2i(r, g);
      }
    }

    return data;
  }

  private static BufferedImage getBufferedImage(
    final @Nonnull InputStream stream)
    throws IOException
  {
    return ImageIO.read(stream);
  }

  private static @Nonnull Texture2DStatic load2DStaticImageInferred(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull BufferedImage image,
    final @Nonnull String name)
    throws GLException,
      ConstraintError
  {
    final int width = image.getWidth();
    final int height = image.getHeight();

    TextureType inferred = TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
    BufferedImage actual = image;

    switch (image.getType()) {
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      {
        inferred = TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
        break;
      }
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      {
        inferred = TextureType.TEXTURE_TYPE_RGB_888_3BPP;
        break;
      }
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_BYTE_INDEXED:
      {
        inferred = TextureType.TEXTURE_TYPE_RGB_565_2BPP;
        break;
      }
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      case BufferedImage.TYPE_BYTE_GRAY:
      {
        inferred = TextureType.TEXTURE_TYPE_LUMINANCE_8_1BPP;
        break;
      }
      case BufferedImage.TYPE_CUSTOM:
      {
        final BufferedImage converted =
          new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, converted);
        inferred = TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
        actual = converted;
        break;
      }
    }

    final Texture2DStatic texture =
      gl.texture2DStaticAllocate(
        name,
        image.getWidth(),
        image.getHeight(),
        inferred,
        wrap_s,
        wrap_t,
        mag_filter,
        min_filter);

    return TextureLoaderImageIO
      .load2DStaticImageSpecific(gl, texture, actual);
  }

  private static @Nonnull Texture2DStatic load2DStaticImageSpecific(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull Texture2DStatic texture,
    final @Nonnull BufferedImage image)
    throws ConstraintError,
      GLException
  {
    final Texture2DWritableData data =
      TextureLoaderImageIO.load2DStaticImageSpecificData(texture, image);
    gl.texture2DStaticUpdate(data);
    return data.getTexture();
  }

  static @Nonnull Texture2DWritableData load2DStaticImageSpecificData(
    final @Nonnull Texture2DStatic texture,
    final @Nonnull BufferedImage image)
    throws ConstraintError
  {
    switch (image.getType()) {
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_USHORT_GRAY:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_BYTE_BINARY:
      {
        return TextureLoaderImageIO.load2DStaticImageSpecificDataActual(
          texture,
          image);
      }
      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final BufferedImage converted =
          new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, converted);

        return TextureLoaderImageIO.load2DStaticImageSpecificDataActual(
          texture,
          converted);
      }
    }
  }

  private static @Nonnull
    Texture2DWritableData
    load2DStaticImageSpecificDataActual(
      final @Nonnull Texture2DStatic texture,
      final @Nonnull BufferedImage image)
      throws ConstraintError
  {
    final TextureType type = texture.getType();
    final Texture2DWritableData data = new Texture2DWritableData(texture);

    switch (image.getType()) {

    /**
     * Convert any RGBA -> type
     */

      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      case BufferedImage.TYPE_4BYTE_ABGR:
      {
        switch (type) {
          case TEXTURE_TYPE_ALPHA_8_1BPP:
          {
            return TextureLoaderImageIO.convertRGBToAlpha8_1Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_LUMINANCE_8_1BPP:
          {
            return TextureLoaderImageIO.convertRGBToLuminance8_1Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
          {
            return TextureLoaderImageIO.convertRGBToLuminance88_2Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_RGBA_4444_2BPP:
          case TEXTURE_TYPE_RGBA_5551_2BPP:
          case TEXTURE_TYPE_RGBA_8888_4BPP:
          {
            return TextureLoaderImageIO.convertABGRToRGBAGeneric(image, data);
          }
          case TEXTURE_TYPE_RGB_565_2BPP:
          case TEXTURE_TYPE_RGB_888_3BPP:
          {
            return TextureLoaderImageIO.convertABGRToRGBGeneric(image, data);
          }
        }

        throw new UnreachableCodeException();
      }

      /**
       * Convert any RGB -> type
       */

      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      {
        switch (type) {
          case TEXTURE_TYPE_ALPHA_8_1BPP:
          {
            return TextureLoaderImageIO.convertRGBToAlpha8_1Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_LUMINANCE_8_1BPP:
          {
            return TextureLoaderImageIO.convertRGBToLuminance8_1Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
          {
            return TextureLoaderImageIO.convertRGBToLuminance88_2Generic(
              image,
              data);
          }
          case TEXTURE_TYPE_RGBA_4444_2BPP:
          case TEXTURE_TYPE_RGBA_5551_2BPP:
          case TEXTURE_TYPE_RGBA_8888_4BPP:
          {
            return TextureLoaderImageIO.convertBGRToRGBAGeneric(image, data);
          }
          case TEXTURE_TYPE_RGB_565_2BPP:
          case TEXTURE_TYPE_RGB_888_3BPP:
          {
            return TextureLoaderImageIO.convertBGRToRGBGeneric(image, data);
          }
        }

        throw new UnreachableCodeException();
      }

      /**
       * Convert any grey -> type
       */

      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_USHORT_GRAY:
      {
        switch (type) {
          case TEXTURE_TYPE_ALPHA_8_1BPP:
          {
            return TextureLoaderImageIO.convertGreyToAlpha(image, data);
          }
          case TEXTURE_TYPE_LUMINANCE_8_1BPP:
          {
            return TextureLoaderImageIO.convertGreyToLuminance(image, data);
          }
          case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
          {
            return TextureLoaderImageIO.convertGreyToLuminanceAlpha(
              image,
              data);
          }
          case TEXTURE_TYPE_RGBA_4444_2BPP:
          case TEXTURE_TYPE_RGBA_5551_2BPP:
          case TEXTURE_TYPE_RGBA_8888_4BPP:
          {
            return TextureLoaderImageIO.convertGreyToRGBA(image, data);
          }
          case TEXTURE_TYPE_RGB_565_2BPP:
          case TEXTURE_TYPE_RGB_888_3BPP:
          {
            return TextureLoaderImageIO.convertGreyToRGB(image, data);
          }
        }

        throw new UnreachableCodeException();
      }

      /**
       * These alternatives must have been removed after conversion.
       */

      case BufferedImage.TYPE_CUSTOM:
      default:
      {
        throw new UnreachableCodeException();
      }
    }
  }

  @Override public @Nonnull Texture2DStatic load2DStaticInferred(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
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
    return TextureLoaderImageIO.load2DStaticImageInferred(
      gl,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter,
      image,
      name);
  }

  @Override public @Nonnull Texture2DStatic load2DStaticSpecific(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(stream, "Input stream");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    final BufferedImage image = TextureLoaderImageIO.getBufferedImage(stream);
    final Texture2DStatic texture =
      gl.texture2DStaticAllocate(
        name,
        image.getWidth(),
        image.getHeight(),
        type,
        wrap_s,
        wrap_t,
        mag_filter,
        min_filter);

    return TextureLoaderImageIO.load2DStaticImageSpecific(gl, texture, image);
  }
}
