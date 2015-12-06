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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLTextureUnitType;

import java.util.List;

/**
 * The interface to OpenGL textures.
 */

public interface JCGLTexturesType
  extends JCGLTextures2DType, JCGLTexturesCubeType
{
  /**
   * @return The maximum texture size supported by the current implementation.
   * 'Size' refers to the length of a side, so if the implementation returns
   * {@code 8192}, the largest texture that can be created is {@code 8192 *
   * 8192}.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  int textureGetMaximumSize()
    throws JCGLException;

  /**
   * @return All available texture units for the current implementation.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  List<JCGLTextureUnitType> textureGetUnits()
    throws JCGLException;

  /**
   * @param unit The texture unit.
   *
   * @return {@code true} iff the the texture unit {@code unit} has any texture
   * bound to it.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  boolean textureUnitIsBound(
    final JCGLTextureUnitType unit)
    throws JCGLException;

  /**
   * Unbind whatever texture is bound to the texture unit {@code unit} (if
   * any).
   *
   * @param unit The texture unit
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  void textureUnitUnbind(
    JCGLTextureUnitType unit)
    throws JCGLException;
}
