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

  /**
   * Enable/disable writing to the given channels of the current color buffer.
   * A value of <code>true</code> for a given channel indicates that data will
   * be written to that channel.
   * 
   * @param r
   *          Enable/disable writing to the red channel of the color buffer.
   * @param g
   *          Enable/disable writing to the green channel of the color buffer.
   * @param b
   *          Enable/disable writing to the blue channel of the color buffer.
   * @param a
   *          Enable/disable writing to the alpha channel of the color buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the alpha channel.
   */

  boolean colorBufferMaskStatusAlpha()
    throws GLException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the blue channel.
   */

  boolean colorBufferMaskStatusBlue()
    throws GLException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the green channel.
   */

  boolean colorBufferMaskStatusGreen()
    throws GLException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the red channel.
   */

  boolean colorBufferMaskStatusRed()
    throws GLException;
}
