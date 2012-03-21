package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

public interface GLDraw
{
  /**
   * Draw a set of polygons using the currently bound array buffers and the
   * current shading program. Elements are picked from the currently array
   * buffer using indices from the element buffer <code>indices</code>.
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException;
}
