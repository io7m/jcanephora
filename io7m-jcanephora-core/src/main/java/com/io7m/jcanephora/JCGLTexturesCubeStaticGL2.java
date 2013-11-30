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
 * Type-safe interface to the set of cube texture types guaranteed to be
 * supported by OpenGL 2.1.
 * </p>
 */

public interface JCGLTexturesCubeStaticGL2 extends
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
   * Retrieve the texture image data associated with the face
   * <code>face</code> of the left-handed cube texture <code>texture</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null || face == null</code></li>
   *           <li><code>texture</code> has been deleted</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeReadableData textureCubeStaticGetImageLH(
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws ConstraintError,
      JCGLException;

  /**
   * Retrieve the texture image data associated with the face
   * <code>face</code> of the right-handed cube texture <code>texture</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null || face == null</code></li>
   *           <li><code>texture</code> has been deleted</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeReadableData textureCubeStaticGetImageRH(
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceRH face)
    throws ConstraintError,
      JCGLException;
}
