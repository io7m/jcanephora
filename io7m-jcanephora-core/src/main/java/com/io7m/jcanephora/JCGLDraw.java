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

/**
 * Commands to render primitives from array data.
 */

public interface JCGLDraw
{
  /**
   * Draw a set of polygons using the currently bound array buffers and the
   * current shading program. Elements are picked from the currently bound
   * array buffer using indices from the element buffer <code>indices</code>.
   * Polygons are drawn using mode <code>mode</code>.
   * 
   * @param mode
   *          The drawing mode.
   * @param indices
   *          The vertex indices.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>mode == null</code></li>
   *           <li><code>indices == null</code></li>
   *           <li>The buffer <code>indices</code> does not correspond to a
   *           valid buffer (perhaps because it has been deleted).</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLException;
}
