package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Typed, writable cursor addressing elements of index buffers.
 */

public interface CursorWritableIndex extends Cursor
{
  /**
   * Put the value <code>index</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   * 
   * The value <code>index</code> is silently truncated to fit the size of the
   * elements in the index buffer.
   * 
   * @throws ConstraintError
   *           Iff the cursor is pointing past the end of the buffer.
   */

  void putIndex(
    final int index)
    throws ConstraintError;
}
