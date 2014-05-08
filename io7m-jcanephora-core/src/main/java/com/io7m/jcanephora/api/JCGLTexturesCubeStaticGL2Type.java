/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;

/**
 * <p>
 * Type-safe interface to the set of cube texture types guaranteed to be
 * supported by OpenGL 2.1.
 * </p>
 */

public interface JCGLTexturesCubeStaticGL2Type extends
  JCGLTexturesCubeStaticGL2ES3Type
{
  /**
   * <p>
   * Allocate a depth texture of width/height <code>size</code>.
   * </p>
   * <p>
   * See
   * {@link com.io7m.jcanephora.TextureFormat#TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP}
   * for the precise format of the texture.
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
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  TextureCubeStaticType textureCubeStaticAllocateDepth24Stencil8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException;

  /**
   * @return The texture image data associated with the face <code>face</code>
   *         of the left-handed cube texture <code>texture</code>.
   * 
   * @param texture
   *          The texture.
   * @param face
   *          The cube map face, assuming a left-handed coordinate system.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  TextureCubeStaticReadableType textureCubeStaticGetImageLH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException;

  /**
   * @return the texture image data associated with the face <code>face</code>
   *         of the right-handed cube texture <code>texture</code>.
   * 
   * @param texture
   *          The texture.
   * @param face
   *          The cube map face, assuming a right-handed coordinate system.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  TextureCubeStaticReadableType textureCubeStaticGetImageRH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceRH face)
    throws JCGLException;
}
