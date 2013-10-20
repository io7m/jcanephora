/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
 * Typed, readable cursor addressing areas consisting of elements of type int
 * (values are assumed to be in the range <code>[0, 0xff]</code>).
 */

public interface SpatialCursorReadable1i extends SpatialCursor
{
  /**
   * Get the value at the current cursor location and seek the cursor to the
   * next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to read from the cursor would read outside of
   *           the valid area for the cursor.
   */

  int get1i()
    throws ConstraintError;
}
