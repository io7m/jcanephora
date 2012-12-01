package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type-safe interface to 2D OpenGL textures.
 */

public interface GLTextures2DStatic
{
  /**
   * Allocate a texture of width <code>width</code> and height
   * <code>height</code>, of type <code>type</code>.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>. The texture is wrapped around the
   * <code>t</code> axis using the wrapping mode <code>wrap_t</code>, with the
   * OpenGL default being <code>TEXTURE_WRAP_REPEAT</code>. The texture is
   * scaled up using the magnification filter <code>mag_filter</code>, with
   * the OpenGL default being <code>TEXURE_FILTER_LINEAR</code>. The texture
   * is scaled down using the minification filter <code>min_filter</code>,
   * with the OpenGL default being <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param width
   *          The width in pixels.
   * @param height
   *          The height in pixels.
   * @param type
   *          The type of texture.
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
   *           <li><code>type == null</code></li>
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

  public @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureType type,
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
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>unit == null</code>.</li>
   *           <li><code>texture == null</code>.</li>
   *           <li><code>texture</code> does not refer to a valid texture
   *           (possible if the texture has already been deleted).</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
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

  void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff the texture <code>texture</code> is bound to
   * the texture unit <code>unit</code>.
   * 
   * @param unit
   *          The texture unit.
   * @param texture
   *          The texture. Iff any of the following hold:
   *          <ul>
   *          <li><code>unit == null</code>.</li>
   *          <li><code>texture == null</code>.</li>
   *          <li><code>texture</code> does not refer to a valid texture
   *          (possible if the texture has already been deleted).</li>
   *          </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException;

  /**
   * Replace the contents (or part of the contents) of the texture
   * <code>data.getTexture()</code> with <code>data</code>.
   * 
   * @param data
   *          The data to upload.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>data == null</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException;
}