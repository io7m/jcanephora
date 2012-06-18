package com.io7m.jcanephora;

/**
 * Typed, writable cursor addressing elements of type Vector4B.
 */

public interface CursorWritable4b extends Cursor
{
  /**
   * Put the values <code>s, t, u, v</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  void put4b(
    final byte s,
    final byte t,
    final byte u,
    final byte v);
}
