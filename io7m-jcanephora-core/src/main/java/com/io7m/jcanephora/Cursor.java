package com.io7m.jcanephora;

/**
 * Seekable cursor type.
 */

public interface Cursor
{
  /**
   * Return <code>true</code> iff the cursor is pointing within the valid
   * range of elements.
   */

  boolean canWrite();

  /**
   * Return <code>true</code> iff there are more elements available.
   */

  boolean hasNext();

  /**
   * Seek the cursor to the next element.
   */

  void next();

  /**
   * Seek the cursor to the index <code>index</code>.
   * 
   * @param index
   *          The element.
   */

  void seekTo(
    long index);
}
