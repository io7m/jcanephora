package com.io7m.jcanephora;

/**
 * A cursor that address elements of a 2D area.
 * 
 * The cursor advances along rows in an area, visiting each column in series.
 * The cursor moves to the next row when it has passed the last column.
 */

interface SpatialCursor
{
  /**
   * Return <code>true</code> iff the cursor is currently pointing to a valid
   * location.
   */

  public boolean canWrite();

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
