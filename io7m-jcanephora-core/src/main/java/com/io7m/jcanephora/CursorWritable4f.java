package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, writable cursor addressing elements of type Vector4F.
 */

public interface CursorWritable4f extends Cursor
{
  /**
   * Put the values <code>x, y, z, w</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to write to the cursor would write past the end
   *           of the array.
   */

  void put4f(
    final float x,
    final float y,
    final float z,
    final float w)
    throws ConstraintError;
}