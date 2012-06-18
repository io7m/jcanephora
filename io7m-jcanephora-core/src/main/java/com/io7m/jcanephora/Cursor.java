package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Seekable cursor type.
 */

public interface Cursor
{
  /**
   * Return <code>true</code> iff there are more elements available.
   */

  boolean hasNext();

  /**
   * Seek the cursor to the next element.
   * 
   * @throws ConstraintError
   *           Iff there are no more elements.
   */

  void next()
    throws ConstraintError;

  /**
   * Seek the cursor to the index <code>index</code>.
   * 
   * @param index
   *          The element.
   * @throws ConstraintError
   *           Iff the index is negative or out of range for the given
   *           structure.
   */

  void seekTo(
    long index)
    throws ConstraintError;
}
