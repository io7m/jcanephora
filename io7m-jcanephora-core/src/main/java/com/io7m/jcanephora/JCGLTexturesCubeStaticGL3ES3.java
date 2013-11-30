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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * Type-safe interface to the common set of cube texture types guaranteed to
 * be supported by OpenGL 3.* and OpenGL ES 3.*.
 * </p>
 */

public interface JCGLTexturesCubeStaticGL3ES3 extends
  JCGLTexturesCubeStaticCommon
{
  /**
   * <p>
   * Allocate a depth texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP} for the
   * precise format of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateDepth24Stencil8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate a depth texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_16_2BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
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
      JCGLException;

  /**
   * <p>
   * Allocate a depth texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_24_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
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
      JCGLException;

  /**
   * <p>
   * Allocate a depth texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_DEPTH_32F_4BPP} for the precise
   * format of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
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
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_16F_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_16I_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_16U_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_32F_4BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_32I_4BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_32U_4BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_8_1BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
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
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_8I_1BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an R texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_R_8U_1BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_16F_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_16I_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_16U_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_32F_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_32I_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_32U_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_8_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_8I_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RG texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RG_8U_2BPP} for the precise format of
   * the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_16F_6BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_16I_6BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_16U_6BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_32F_12BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_32I_12BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_32U_12BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_8I_3BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGB_8U_3BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_16F_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_16I_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_16U_8BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_32F_16BPP} for the precise
   * format of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_32I_16BPP} for the precise
   * format of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_32U_16BPP} for the precise
   * format of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_8I_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See {@link TextureType#TEXTURE_TYPE_RGBA_8U_4BPP} for the precise format
   * of the texture.
   * </p>
   * <p>
   * The texture is wrapped around the <code>r</code> axis using the wrapping
   * mode <code>wrap_r</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>s</code> axis using the wrapping
   * mode <code>wrap_s</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is wrapped around the <code>t</code> axis using the wrapping
   * mode <code>wrap_t</code>, with the OpenGL default being
   * <code>TEXTURE_WRAP_REPEAT</code>.
   * </p>
   * <p>
   * The texture is scaled down using the minification filter
   * <code>min_filter</code>, with the OpenGL default being
   * <code>TEXURE_FILTER_LINEAR</code>.
   * </p>
   * <p>
   * The texture is scaled up using the magnification filter
   * <code>mag_filter</code>, with the OpenGL default being
   * <code>TEXTURE_FILTER_LINEAR</code>.
   * </p>
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException;

}
