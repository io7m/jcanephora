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
 * Type-safe interface to the cube map texture API exposed by OpenGL 3.*.
 * 
 * The textures are manipulated using the standard <code>glTexImageCube</code>
 * family of functions, internally and the functions are intended for use with
 * textures that are not frequently updated.
 */

public interface GLTexturesCubeStaticGL3 extends GLTexturesCubeStaticCommon
{
  /**
   * Allocate a depth texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_16_2BPP} for the precise format
   * of the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateDepth16(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;

  /**
   * Allocate a depth texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_24_4BPP} for the precise format
   * of the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateDepth24(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;

  /**
   * Allocate a depth texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_32_4BPP} for the precise format
   * of the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateDepth32(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;

  /**
   * Allocate a depth texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_32F_4BPP} for the precise
   * format of the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateDepth32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;

  /**
   * Allocate an R texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_R_8_1BPP} for the precise format of
   * the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;

  /**
   * Allocate an RG texture of width/height <code>size</code>.
   * 
   * See {@link TextureType#TEXTURE_TYPE_RG_88_2BPP} for the precise format of
   * the texture.
   * 
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * 
   * The texture is scaled up using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * 
   * The texture is scaled down using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * 
   * @param name
   *          The name of the texture.
   * @param size
   *          The size in pixels.
   * @param wrap_r
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param wrap_s
   *          The method with which to wrap textures around the <code>s</code>
   *          axis.
   * @param wrap_t
   *          The method with which to wrap textures around the <code>t</code>
   *          axis.
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>wrap_s == null</code></li>
   *           <li><code>wrap_t == null</code></li>
   *           <li><code>wrap_r == null</code></li>
   *           <li><code>min_filter == null</code></li>
   *           <li><code>mag_filter == null</code></li>
   *           <li><code>1 &lt; size &lt; Integer.MAX_VALUE</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG88(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException;
}
