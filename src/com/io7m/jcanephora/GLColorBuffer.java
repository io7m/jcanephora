package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Simplified interface to the color buffer.
 */

public interface GLColorBuffer
{
  /**
   * Clear the color buffer with the color specified by <code>color</code>.
   * 
   * @param color
   *          The color to use.
   * @throws ConstraintError
   *           Iff <code>color == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClear(
    final VectorReadable4F color)
    throws ConstraintError,
      GLException;
}
