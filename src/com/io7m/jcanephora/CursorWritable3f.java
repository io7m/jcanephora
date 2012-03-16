package com.io7m.jcanephora;

/**
 * Typed, writable cursor addressing elements of type Vector3F.
 */

public interface CursorWritable3f extends Cursor
{
  /**
   * Put the values <code>x, y, z</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  void put3f(
    final float x,
    final float y,
    final float z);
}
