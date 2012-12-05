/*
 * Copyright © 2012 http://io7m.com
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
