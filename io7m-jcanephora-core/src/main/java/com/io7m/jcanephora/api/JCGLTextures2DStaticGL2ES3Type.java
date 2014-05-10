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

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;

/**
 * <p>
 * Type-safe interface to the common set of 2D texture types guaranteed to be
 * supported by OpenGL 2.1, 3.* and OpenGL ES 3.*.
 * </p>
 */

public interface JCGLTextures2DStaticGL2ES3Type extends
  JCGLTextures2DStaticCommonType
{
  /**
   * <p>
   * Allocate an RGB texture of width <code>width</code> and height
   * <code>height</code>. See
   * {@link com.io7m.jcanephora.TextureFormat#TEXTURE_FORMAT_RGB_8_3BPP} for
   * the precise format of the texture.
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
   * @see com.io7m.jcanephora.TextureFormat#TEXTURE_FORMAT_RGB_8_3BPP
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
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL error occurs.
   */

  Texture2DStaticType texture2DStaticAllocateRGB8(
    final String name,
    int width,
    int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Allocate an RGBA texture of width <code>width</code> and height
   * <code>height</code>. See
   * {@link com.io7m.jcanephora.TextureFormat#TEXTURE_FORMAT_RGBA_8_4BPP} for
   * the precise format of the texture.
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
   * @param min_filter
   *          The minification filter.
   * @param mag_filter
   *          The magnification filter.
   * @return An allocated texture.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL error occurs.
   */

  Texture2DStaticType texture2DStaticAllocateRGBA8(
    final String name,
    int width,
    int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime;
}
