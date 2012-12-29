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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type-safe interface to the 2D texture API exposed by OpenGL ES2.
 * 
 * The textures are manipulated using the standard <code>glTexImage2D</code>
 * family of functions, internally and the functions are intended for use with
 * textures that are not frequently updated. For streaming, frequently updated
 * textures on non-embedded platforms that support pixel-unpack buffers, see
 * the {@link GLTextures2DBuffered} interface.
 * 
 * @see GLTextures2DBuffered
 */

public interface GLTextures2DStaticES2
{
  /**
   * Allocate an RGB texture of width <code>width</code> and height
   * <code>height</code>. See {@link TextureType#TEXTURE_TYPE_RGB_565_2BPP}
   * for the precise format of the texture.
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

  public @Nonnull Texture2DStatic texture2DStaticAllocateRGB565(
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
   * Allocate an RGB texture of width <code>width</code> and height
   * <code>height</code>. See {@link TextureType#TEXTURE_TYPE_RGB_888_3BPP}
   * for the precise format of the texture.
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
   * @see TextureType#TEXTURE_TYPE_RGB_888_3BPP
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

  public @Nonnull Texture2DStatic texture2DStaticAllocateRGB888(
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
   * Allocate an RGBA texture of width <code>width</code> and height
   * <code>height</code>. See {@link TextureType#TEXTURE_TYPE_RGBA_4444_2BPP}
   * for the precise format of the texture.
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

  public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA4444(
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
   * Allocate an RGBA texture of width <code>width</code> and height
   * <code>height</code>. See {@link TextureType#TEXTURE_TYPE_RGBA_5551_2BPP}
   * for the precise format of the texture.
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

  public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA5551(
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
   * Allocate an RGBA texture of width <code>width</code> and height
   * <code>height</code>. See {@link TextureType#TEXTURE_TYPE_RGBA_8888_4BPP}
   * for the precise format of the texture.
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

  public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8888(
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
