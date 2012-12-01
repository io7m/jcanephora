package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, writable cursor addressing areas consisting of elements of type
 * Vector2B.
 * 
 * Due to the lack of 8-bit unsigned types in Java, the interface uses
 * ordinary integers and simply assumes that values passed are in the range
 * <code>[0 .. 0xFF]</code>.
 */

public interface SpatialCursorWritable2i extends SpatialCursor
{
  /**
   * Put the values <code>x, y</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to write to the cursor would write outside of
   *           the valid area for the cursor.
   */

  void put2i(
    final int x,
    final int y)
    throws ConstraintError;
}
