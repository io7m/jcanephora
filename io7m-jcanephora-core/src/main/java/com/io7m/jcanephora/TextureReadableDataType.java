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
 * The interface supported by types that have readable data that can be
 * accessed with cursors.
 */

public interface TextureReadableDataType
{
  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1.
   */

  SpatialCursorReadable1dType getCursor1d()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1.
   */

  SpatialCursorReadable1fType getCursor1f()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 1, or the
   *           components are floating point values.
   */

  SpatialCursorReadable1iType getCursor1i()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2.
   */

  SpatialCursorReadable2dType getCursor2d()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2.
   */

  SpatialCursorReadable2fType getCursor2f()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 2, or the
   *           texture has floating-point components.
   */

  SpatialCursorReadable2iType getCursor2i()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3.
   */

  SpatialCursorReadable3dType getCursor3d()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3.
   */

  SpatialCursorReadable3fType getCursor3f()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3, or the
   *           texture has floating-point components.
   */

  SpatialCursorReadable3iType getCursor3i()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 3.
   */

  SpatialCursorReadable4dType getCursor4d()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 4.
   */

  SpatialCursorReadable4fType getCursor4f()
    throws JCGLExceptionTypeError;

  /**
   * 
   * @return A cursor that points to elements of the texture. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   * 
   * 
   * @throws JCGLExceptionTypeError
   *           If the number of components in the texture is not 4, or the
   *           texture has floating-point components.
   */

  SpatialCursorReadable4iType getCursor4i()
    throws JCGLExceptionTypeError;

}
