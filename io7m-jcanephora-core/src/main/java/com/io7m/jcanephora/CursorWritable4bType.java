/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora;


/**
 * Typed, writable cursor addressing elements of type Vector4B.
 */

public interface CursorWritable4bType extends CursorType
{
  /**
   * Put the values <code>s, t, u, v</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   * 
   * @param s
   *          The <code>s</code> value
   * @param t
   *          The <code>t</code> value
   * @param u
   *          The <code>u</code> value
   * @param v
   *          The <code>v</code> value
   */

  void put4b(
    final byte s,
    final byte t,
    final byte u,
    final byte v);
}
