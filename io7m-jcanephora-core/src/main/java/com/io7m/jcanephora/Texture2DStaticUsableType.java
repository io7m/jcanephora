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

import com.io7m.jranges.RangeInclusiveL;

/**
 * A read-only interface to the {@link Texture2DStaticType} type that allows
 * use of the type but not mutation and/or deletion of the contents.
 */

public interface Texture2DStaticUsableType extends
  JCGLNameType,
  JCGLResourceUsableType,
  JCGLResourceSizedType,
  TextureUsableType
{
  /**
   * @return The inclusive area of this texture.
   */

  AreaInclusive textureGetArea();

  /**
   * @return The format of the texture.
   */

  TextureFormat textureGetFormat();

  /**
   * @return The height in pixels of the texture.
   */

  int textureGetHeight();

  /**
   * @return The magnification filter used for the texture.
   */

  TextureFilterMagnification textureGetMagnificationFilter();

  /**
   * @return The minification filter used for the texture.
   */

  TextureFilterMinification textureGetMinificationFilter();

  /**
   * @return The name of the texture.
   */

  String textureGetName();

  /**
   * @return The range of valid indices on the X axis.
   */

  RangeInclusiveL textureGetRangeX();

  /**
   * @return The range of valid indices on the Y axis.
   */

  RangeInclusiveL textureGetRangeY();

  /**
   * @return The width in pixels of the texture.
   */

  int textureGetWidth();

  /**
   * @return The wrapping mode used on the S axis of the texture.
   */

  TextureWrapS textureGetWrapS();

  /**
   * @return The wrapping mode used on the T axis of the texture.
   */

  TextureWrapT textureGetWrapT();
}
