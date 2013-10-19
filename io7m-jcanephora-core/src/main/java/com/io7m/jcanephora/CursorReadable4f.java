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
import com.io7m.jtensors.VectorM4F;

/**
 * Typed, readable cursor addressing elements of type Vector4F.
 */

public interface CursorReadable4f extends Cursor
{
  /**
   * Retrieve the four floating point values at the current cursor location,
   * saving them to <code>v.x</code>, <code>v.y</code>, <code>v.z</code>, and
   * <code>v.w</code>, respectively, and seek the cursor to the next element
   * iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to read from the cursor would read past the end
   *           of the array, or <code>v == null</code>.
   */

  void get4f(
    final @Nonnull VectorM4F v)
    throws ConstraintError;
}
