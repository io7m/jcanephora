package com.io7m.jcanephora;

/**
 * Typed, writable cursor addressing elements of index buffers.
 */

public interface IndexBufferCursorWritable extends Cursor
{
  /**
   * Put the value <code>index</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   * 
   * The value <code>index</code> is silently truncated to fit the size of the
   * elements in the index buffer.
   */

  void putIndex(
    final int index);
}
