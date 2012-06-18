package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Simplified interface to the color buffer.
 */

public interface GLColorBuffer
{
  /**
   * Clear the color buffer with the color specified by
   * <code>(r,g,b,1.0)</code> .
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException;

  /**
   * Clear the color buffer with the color specified by <code>(r,g,b,a)</code>
   * .
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException;

  /**
   * Clear the color buffer with the color specified by <code>color</code>.
   * The alpha channel of the buffer is set to 1.0.
   * 
   * @param color
   *          The color to use.
   * @throws ConstraintError
   *           Iff <code>color == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClearV3f(
    final VectorReadable3F color)
    throws ConstraintError,
      GLException;

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

  void colorBufferClearV4f(
    final VectorReadable4F color)
    throws ConstraintError,
      GLException;
}
