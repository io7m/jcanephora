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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Functions for loading OpenGL textures from image files/streams.
 */

public interface TextureLoader
{
  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_DEPTH_16_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticDepth16(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_DEPTH_24_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticDepth24(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_DEPTH_32_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticDepth32(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_DEPTH_32F_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticDepth32f(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Attempt to load an OpenGL texture from the stream <code>stream</code>.
   * The texture will be constructed using the given parameters, and named
   * <code>name</code>. The resulting texture will be of a type appropriate to
   * the original image data.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticInferredGL3(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Attempt to load an OpenGL texture from the stream <code>stream</code>.
   * The texture will be constructed using the given parameters, and named
   * <code>name</code>. The resulting texture will be of a type appropriate to
   * the original image data.
   * </p>
   * <p>
   * The function will only attempt to infer texture types supported by the
   * common subset of OpenGL ES2 and OpenGL 3.*. In practice, this means that
   * most resulting textures will be of types
   * {@link TextureType#TEXTURE_TYPE_RGB_888_3BPP} or
   * {@link TextureType#TEXTURE_TYPE_RGBA_8888_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticInferredGLES2(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_R_8_1BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticR8(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RG_88_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRG88(
    final @Nonnull GLTextures2DStaticGL3 gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RGB_565_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRGB565(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RGB_888_3BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRGB888(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RGBA_4444_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRGBA4444(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RGBA_5551_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRGBA5551(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureType#TEXTURE_TYPE_RGBA_8888_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image.
   */

  public @Nonnull Texture2DStatic load2DStaticRGBA8888(
    final @Nonnull GLTextures2DStaticCommon gl,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;
}
