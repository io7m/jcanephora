package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, writable cursor addressing elements of type Vector2F.
 */

public interface CursorWritable2f extends Cursor
{
  /**
   * Put the values <code>x, y</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to write to the cursor would write past the end
   *           of the array.
   */

  void put2f(
    final float x,
    final float y)
    throws ConstraintError;
}
