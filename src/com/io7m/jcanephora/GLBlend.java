package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified and type-safe interface to OpenGL blending.
 */

public interface GLBlend
{
  /**
   * Disable blending.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void disableBlending()
    throws GLException;

  /**
   * Enable blending with the blending functions <code>source_factor</code>
   * and <code>destination_factor</code>. The OpenGL defaults are
   * <code>BLEND_ONE</code> and <code>BLEND_ZERO</code>, for
   * <code>source_factor</code> and <code>destination_factor</code>,
   * respectively.
   * 
   * @param source_factor
   *          The function used to blend the incoming (source) color values.
   * @param destination_factor
   *          The function used to blend the existing (destination) color
   *          values.
   * @throws ConstraintError
   *           Iff
   *           <code>source_factor == null || destination_factor == null</code>
   *           .
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlending(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException;
}
