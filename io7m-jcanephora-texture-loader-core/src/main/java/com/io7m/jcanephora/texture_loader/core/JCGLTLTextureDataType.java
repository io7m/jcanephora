/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.texture_loader.core;

import com.io7m.jtensors.VectorWritable4DType;

/**
 * <p>The type of loaded texture data.</p>
 *
 * <p>Instances of this type present the illusion that all images are RGBA
 * images with unsigned normalized double precision floating point values.
 * Greyscale images are presented as grey RGB values with a fully opaque alpha
 * channel. Greyscale-with-alpha images are presented as grey RGB values with
 * the original alpha value intact. RGB images are presented as RGB values with
 * a fully opaque alpha channel. RGBA images are presented unmodified. Indexed
 * color images are presented as RGB values with a fully opaque alpha
 * channel.</p>
 *
 * <p>The {@code (0, 0)} origin is considered to be the top left corner of the
 * image, which is in contrast to OpenGL's bottom left corner.</p>
 */

public interface JCGLTLTextureDataType
{
  /**
   * @return {@code true} iff the values returned are in premultiplied alpha
   * form
   */

  boolean isPremultipliedAlpha();

  /**
   * @return The width of the texture in pixels
   */

  long getWidth();

  /**
   * @return The height of the texture in pixels
   */

  long getHeight();

  /**
   * Retrieve the pixel at {@code (x, y)}, writing the color data to {@code v}.
   *
   * @param x The X coordinate
   * @param y The Y coordinate
   * @param v The output vector
   */

  void getPixel(
    int x,
    int y,
    VectorWritable4DType v);
}
