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
   * Attempt to load an OpenGL texture from the stream <code>stream</code>.
   * The texture will be constructed using the given parameters, and named
   * <code>name</code>. The resulting texture will be of type
   * <code>type</code>, regardless of the original format of the image data.
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param type
   *          The intended texture type.
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

  public @Nonnull Texture2DStatic load2DStaticSpecific(
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
      IOException;

  /**
   * Attempt to load an OpenGL texture from the stream <code>stream</code>.
   * The texture will be constructed using the given parameters, and named
   * <code>name</code>. The resulting texture will be of a type appropriate to
   * the original image data.
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

  public @Nonnull Texture2DStatic load2DStaticInferred(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull InputStream stream,
    final @Nonnull String name)
    throws ConstraintError,
      GLException,
      IOException;
}
