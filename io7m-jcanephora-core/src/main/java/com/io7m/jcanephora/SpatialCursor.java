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
 * <p>
 * A cursor that addresses elements of a 2D area.
 * </p>
 * <p>
 * The cursor advances along rows in an area, visiting each column in series.
 * The cursor moves to the next row when it has passed the last column.
 * </p>
 */

public interface SpatialCursor
{
  /**
   * Return <code>true</code> iff the cursor is currently pointing to a valid
   * location.
   */

  public boolean canWrite();

  /**
   * Retrieve the current position of the cursor on the X axis.
   * 
   * @throws ConstraintError
   *           Iff the cursor is not currently pointing to a writable element.
   */

  public long getElementX()
    throws ConstraintError;

  /**
   * Retrieve the current position of the cursor on the Y axis.
   * 
   * @throws ConstraintError
   *           Iff the cursor is not currently pointing to a writable element.
   */

  public long getElementY()
    throws ConstraintError;

  /**
   * Seek the cursor to the next element.
   */

  public void next();

  /**
   * Seek to the specific element at <code>(x, y)</code>.
   */

  public void seekTo(
    long x,
    long y);
}
