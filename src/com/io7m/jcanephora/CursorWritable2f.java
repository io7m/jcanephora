package com.io7m.jcanephora;

public interface CursorWritable2f extends Cursor
{
  /**
   * Put the values <code>x, y</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   */

  void put2f(
    final float x,
    final float y);
}
