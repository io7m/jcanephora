/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * The interface supported by types that have writable data that can be
 * modified with cursors.
 */

public interface TextureReadableData
{

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public SpatialCursorReadable1d getCursor1d()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public SpatialCursorReadable1f getCursor1f()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1, or the
   *           components are floating point values.
   */

  public SpatialCursorReadable1i getCursor1i()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 2.
   */

  public SpatialCursorReadable2d getCursor2d()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 2.
   */

  public SpatialCursorReadable2f getCursor2f()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 2, or the
   *           texture has floating-point components.
   */

  public SpatialCursorReadable2i getCursor2i()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3.
   */

  public SpatialCursorReadable3d getCursor3d()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3.
   */

  public SpatialCursorReadable3f getCursor3f()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3, or the
   *           texture has floating-point components.
   */

  public SpatialCursorReadable3i getCursor3i()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3.
   */

  public SpatialCursorReadable4d getCursor4d()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 4.
   */

  public SpatialCursorReadable4f getCursor4f()
    throws ConstraintError;

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 4, or the
   *           texture has floating-point components.
   */

  public SpatialCursorReadable4i getCursor4i()
    throws ConstraintError;

}
