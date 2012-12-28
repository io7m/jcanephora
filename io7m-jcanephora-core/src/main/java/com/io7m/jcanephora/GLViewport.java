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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Functions controlling the OpenGL viewport.
 */

public interface GLViewport
{
  /**
   * Set the OpenGL viewport to the <code>(x, y)</code> coordinates specified
   * by <code>position</code>, of width <code>dimensions.x</code> and height
   * <code>dimensions.y</code>. The dimensions and position are specified in
   * pixels and <code>(0, 0)</code> refers to the bottom left corner of the
   * viewport.
   * 
   * @param position
   *          The position in pixels.
   * @param dimensions
   *          The size in pixels.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>position == null</code>.</li>
   *           <li><code>dimensions == null</code>.</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurred.
   */

  void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException;
}
