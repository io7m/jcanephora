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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorM4I;

/**
 * Readable cursor addressing areas consisting of four-component elements.
 * Values are fetched directly and not converted (but may, for example, return
 * 16 bit values in 32 bit integers).
 */

public interface SpatialCursorReadable4i extends SpatialCursorType
{
  /**
   * Get the value at the current cursor location and seek the cursor to the
   * next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to read from the cursor would read outside of
   *           the valid area for the cursor, or <code>v == null</code>.
   */

  void get4i(
    final @Nonnull VectorM4I v)
    throws ConstraintError;
}
