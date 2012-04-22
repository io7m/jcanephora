package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type-safe interface to OpenGL textures.
 */

public interface GLTextures
{
  /**
   * Allocate an RGBA texture, using four bytes per pixel, of width
   * <code>width</code> and height <code>height</code>. The texture is backed
   * by a pixel unpack buffer. The texture is wrapped around the
   * <code>s</code> axis using the wrapping mode <code>wrap_s</code>, with the
   * OpenGL default being <code>TEXTURE_WRAP_REPEAT</code>. The texture is
   * wrapped around the <code>t</code> axis using the wrapping mode
   * <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>. The texture is scaled up using the
   * magnification filter <code>mag_filter</code>, with the OpenGL default
   * being <code>TEXURE_FILTER_LINEAR</code>. The texture is scaled down using
   * the minification filter <code>min_filter</code>, with the OpenGL default
   * being <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param width
   *          The width in pixels.
   * @param height
   *          The height in pixels.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param mag_filter
   *          The magnification filter.
   * @param min_filter
   *          The minification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>1 &lt; width &lt; Integer.MAX_VALUE</code></li>
   *           <li><code>1 &lt; height &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull Texture2DRGBAStatic allocateTextureRGBAStatic(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull TextureFilter min_filter)
    throws ConstraintError,
      GLException;

  /**
   * Bind the texture <code>texture</code> to the texture unit
   * <code>unit</code>.
   * 
   * @param unit
   *          The texture unit.
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff <code>unit == null || texture == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void bindTexture2DRGBAStatic(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException;

  /**
   * Deletes the texture referenced by <code>texture</code>.
   * 
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null</code>.</li>
   *           <li><code>texture</code> does not refer to a valid texture
   *           (possible if the texture has already been deleted).</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteTexture2DRGBAStatic(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException;

  /**
   * Return the maximum texture size supported by the current implementation.
   * 'Size' refers to the length of a side, so if the implementation returns
   * <code>8192</code>, the largest texture that can be created is
   * <code>8192 * 8192</code>.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  int getMaximumTextureSize()
    throws GLException;

  /**
   * Retrieve the texture data for the given texture.
   * 
   * @param texture
   * @return A ByteBuffer containing 32-bit RGBA data.
   * @throws ConstraintError
   *           Iff <code>unit == null || texture == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull ByteBuffer getTexture2DRGBAStaticImage(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException;

  /**
   * Retrieve all available texture units for the current implementation.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull TextureUnit[] getTextureUnits()
    throws GLException;

  /**
   * Copy new data into the texture from the pixel unpack buffer used by the
   * texture.
   * 
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>texture == null</code>.</li>
   *           <li><code>texture</code> does not refer to a valid texture
   *           (possible if the texture has already been deleted).</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void replaceTexture2DRGBAStatic(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException;
}
