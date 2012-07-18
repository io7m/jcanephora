package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.VectorReadable2I;

/**
 * Simplified interface to the scissor test.
 */

public interface GLScissor
{
  /**
   * Disable the scissor test in the OpenGL pipeline. The scissor test is
   * initially disabled.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void scissorDisable()
    throws GLException;

  /**
   * Set the OpenGL scissor region to the <code>(x, y)</code> coordinates
   * specified by <code>position</code>, of width <code>dimensions.x</code>
   * and height <code>dimensions.y</code>. The dimensions and position are
   * specified in pixels and <code>(0, 0)</code> refers to the bottom left
   * corner of the viewport.
   * 
   * @param position
   *          The position in pixels.
   * @param dimensions
   *          The size in pixels.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>position == null</code></li>
   *           <li><code>dimensions == null</code></li>
   *           <li><code>dimensions.getXI() &lt; 0</code></li>
   *           <li><code>dimensions.getYI() &lt; 0</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurred.
   */

  void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff scissor testing is enabled.
   */

  boolean scissorIsEnabled()
    throws GLException;
}