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

package com.io7m.jcanephora;

/**
 * OpenGL texture minification filter specification.
 */

public enum TextureFilterMinification
{
  /**
   * Bilinear interpolation.
   */

  TEXTURE_FILTER_LINEAR,

  /**
   * Nearest-neighbour interpolation.
   */

  TEXTURE_FILTER_NEAREST,

  /**
   * Chooses the mipmap that most closely matches the size of the pixel being
   * textured and uses the {@link #TEXTURE_FILTER_NEAREST} criterion (the
   * texture element closest to the specified texture coordinates) to produce
   * a texture value.
   */

  TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST,

  /**
   * Chooses the mipmap that most closely matches the size of the pixel being
   * textured and uses the {@link #TEXTURE_FILTER_LINEAR} criterion (a
   * weighted average of the four texture elements that are closest to the
   * specified texture coordinates) to produce a texture value.
   */

  TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST,

  /**
   * Chooses the two mipmaps that most closely match the size of the pixel
   * being textured and uses the {@link #TEXTURE_FILTER_NEAREST} criterion
   * (the texture element closest to the specified texture coordinates ) to
   * produce a texture value from each mipmap. The final texture value is a
   * weighted average of those two values.
   */

  TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR,

  /**
   * Chooses the two mipmaps that most closely match the size of the pixel
   * being textured and uses the {@link #TEXTURE_FILTER_LINEAR} criterion (a
   * weighted average of the texture elements that are closest to the
   * specified texture coordinates) to produce a texture value from each
   * mipmap. The final texture value is a weighted average of those two
   * values.
   */

  TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR
}
