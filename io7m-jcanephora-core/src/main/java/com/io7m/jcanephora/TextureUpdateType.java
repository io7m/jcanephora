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
 * The interface supported by textures that have writable data that can be
 * modified with cursors.
 */

public interface TextureUpdateType
{
  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1.
   */

  SpatialCursorWritable1dType getCursor1d()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1.
   */

  SpatialCursorWritable1fType getCursor1f()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1, or the
   *           texture is floating-point.
   */

  SpatialCursorWritable1iType getCursor1i()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2.
   */

  SpatialCursorWritable2dType getCursor2d()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2.
   */

  SpatialCursorWritable2fType getCursor2f()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2, or the
   *           texture is floating-point.
   */

  SpatialCursorWritable2iType getCursor2i()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3.
   */

  SpatialCursorWritable3dType getCursor3d()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3.
   */

  SpatialCursorWritable3fType getCursor3f()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3, or the
   *           texture is floating-point.
   */

  SpatialCursorWritable3iType getCursor3i()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 4.
   */

  SpatialCursorWritable4dType getCursor4d()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 4.
   */

  SpatialCursorWritable4fType getCursor4f()
    throws JCGLExceptionTypeError;

  /**
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   *
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 4, or the
   *           texture is floating-point.
   */

  SpatialCursorWritable4iType getCursor4i()
    throws JCGLExceptionTypeError;

  /**
   * @return The current mipmap generation behaviour.
   * @see #setMipMapGeneration(MipmapGeneration)
   */

  MipmapGeneration getMipmapGeneration();

  /**
   * @return The type of the underlying texture.
   */

  TextureFormat getType();

  /**
   * Indicate whether or not the texture update should update mipmaps.
   * Defaults to
   * <code>{@link MipmapGeneration#MIPMAP_GENERATE_BY_MINIFICATION}</code>.
   *
   * @param u
   *          The mipmap generation behaviour
   */

  void setMipMapGeneration(
    final MipmapGeneration u);

}
