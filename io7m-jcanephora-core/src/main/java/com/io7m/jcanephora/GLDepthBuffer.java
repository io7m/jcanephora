package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the depth buffer.
 */

public interface GLDepthBuffer
{
  /**
   * Clear the depth buffer with the specified value <code>depth</code>.
   * 
   * @param depth
   *          The depth value.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff no depth buffer is available.
   */

  void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError;

  /**
   * Disable depth testing.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferDisable()
    throws GLException;

  /**
   * Enable depth testing with the given <code>function</code>. The OpenGL
   * default for <code>function</code> is <code>LESS_THAN</code>.
   * 
   * @param function
   *          The depth function.
   * @throws ConstraintError
   *           Iff <code>function == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferEnable(
    final DepthFunction function)
    throws ConstraintError,
      GLException;

  /**
   * Retrieve the number of bits available in the depth buffer for the current
   * framebuffer configuration.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  int depthBufferGetBits()
    throws GLException;

  /**
   * Return <code>true</code> iff depth testing is enabled.
   */

  boolean depthBufferIsEnabled()
    throws GLException;
}
