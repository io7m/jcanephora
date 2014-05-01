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
 * <p>
 * A cursor that addresses elements of a 2D area.
 * </p>
 * <p>
 * The cursor advances along rows in an area, visiting each column in series.
 * The cursor moves to the next row when it has passed the last column.
 * </p>
 */

public interface SpatialCursorType
{
  /**
   * @return The current position of the cursor on the X axis.
   */

  long getElementX();

  /**
   * @return The current position of the cursor on the Y axis.
   */

  long getElementY();

  /**
   * @return <code>true</code> iff the cursor is currently pointing to a valid
   *         location.
   */

  boolean isValid();

  /**
   * Seek the cursor to the next element.
   */

  void next();

  /**
   * Seek to the specific element at <code>(x, y)</code>.
   * 
   * @param x
   *          The position on the X axis
   * @param y
   *          The position on the Y axis
   */

  void seekTo(
    long x,
    long y);
}
