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

package com.io7m.jcanephora.texture.loader.core;

import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;

/**
 * The type of texture update providers.
 */

public interface JCGLTLTextureUpdateProviderType
{
  /**
   * Populate and return a texture update for {@code t} using the contents of
   * {@code data}. The width and height of {@code data} must be less than or
   * equal to the width and height of {@code t}.
   *
   * @param t    The texture
   * @param data The update data
   *
   * @return A texture update
   */

  JCGLTexture2DUpdateType getTextureUpdate2D(
    JCGLTexture2DUsableType t,
    JCGLTLTextureDataType data);

  /**
   * Populate and return a texture update for {@code t} using the contents of
   * {@code data}. The width and height of {@code data} must be less than or
   * equal to the width and height of {@code t}.
   *
   * @param t    The texture
   * @param data The update data
   *
   * @return A texture update
   */

  JCGLTextureCubeUpdateType getTextureUpdateCube(
    JCGLTextureCubeUsableType t,
    JCGLTLTextureDataType data);
}
