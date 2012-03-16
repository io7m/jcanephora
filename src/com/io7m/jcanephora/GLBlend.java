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
   * respectively. The given blend functions are applied to both the RGB and
   * alpha elements of the colors in question (as opposed to
   * {@link GLBlend#enableBlendingSeparate}, which allows for different
   * functions for the RGB and alpha components).
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

  /**
   * Enable blending with the blending functions <code>source_factor</code>
   * and <code>destination_factor</code>. The OpenGL defaults are
   * <code>BLEND_ONE</code> for <code>source_rgb_factor</code> and
   * <code>source_alpha_factor</code>, and <code>BLEND_ZERO</code>, for and
   * <code>destination_rgb_factor</code> and
   * <code>destination_alpha_factor</code>, respectively.
   * 
   * The given blend functions are applied to the RGB and alpha elements of
   * the colors in question separately (as opposed to
   * {@link GLBlend#enableBlending}, which applies the given functions to the
   * RGB and alpha components simultaneously).
   * 
   * @param source_rgb_factor
   *          The function used to blend the RGB components of incoming
   *          (source) color values.
   * @param source_alpha_factor
   *          The function used to blend the alpha components of incoming
   *          (source) color values.
   * @param destination_rgb_factor
   *          The function used to blend the RGB components of the existing
   *          (destination) color values.
   * @param destination_alpha_factor
   *          The function used to blend the alpha components of the existing
   *          (destination) color values.
   * @throws ConstraintError
   *           Iff <code>source_rgb_factor == null ||
   *               source_alpha_factor == null ||
   *               destination_rgb_factor == null ||
   *               destination_alpha_factor == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlendingSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException;
}
