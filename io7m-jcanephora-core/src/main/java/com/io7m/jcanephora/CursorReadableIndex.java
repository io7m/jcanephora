package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, readable cursor addressing elements of index buffers.
 */

public interface CursorReadableIndex extends Cursor
{
  /**
   * Retrieve the value at the current cursor location and seek the cursor to
   * the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff the cursor is pointing past the end of the buffer.
   */

  public int getIndex()
    throws ConstraintError;
}
