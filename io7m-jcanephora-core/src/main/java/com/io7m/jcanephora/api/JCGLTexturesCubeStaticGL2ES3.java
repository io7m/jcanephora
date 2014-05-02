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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;

/**
 * <p>
 * Type-safe interface to the common set of cube texture types guaranteed to
 * be supported by OpenGL 2.1, 3.* and OpenGL ES 3.*.
 * </p>
 */

public interface JCGLTexturesCubeStaticGL2ES3 extends
  JCGLTexturesCubeStaticCommon
{
  /**
   * <p>
   * Allocate an RGB texture of width/height <code>size</code>. See
   * {@link TextureFormat#TEXTURE_FORMAT_RGB_8_3BPP} for the precise format of the
   * texture.
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
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL error occurs.
   */

  public TextureCubeStatic textureCubeStaticAllocateRGB8(
    final  String name,
    int size,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLExceptionRuntime;

  /**
   * <p>
   * Allocate an RGBA texture of width/height <code>size</code>. See
   * {@link TextureFormat#TEXTURE_FORMAT_RGBA_8_4BPP} for the precise format of
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
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL error occurs.
   */

  public TextureCubeStatic textureCubeStaticAllocateRGBA8(
    final  String name,
    int size,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLExceptionRuntime;

}
