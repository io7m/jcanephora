package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A cursor that address elements of a 2D area.
 * 
 * The cursor advances along rows in an area, visiting each column in series.
 * The cursor moves to the next row when it has passed the last column.
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
