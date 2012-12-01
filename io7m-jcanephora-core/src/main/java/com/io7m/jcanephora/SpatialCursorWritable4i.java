package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, writable cursor addressing areas consisting of elements of type
 * Vector4B.
 * 
 * Due to the lack of 8-bit unsigned types in Java, the interface uses
 * ordinary integers and simply assumes that values passed are in the range
 * <code>[0 .. 0xFF]</code>.
 */

public interface SpatialCursorWritable4i extends SpatialCursor
{
  /**
   * Put the values <code>r, g, b, a</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to write to the cursor would write past the end
   *           of the array.
   */

  void put4i(
    final int r,
    final int g,
    final int b,
    final int a)
    throws ConstraintError;
}