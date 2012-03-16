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
   * Enable blending with the given blending functions. The function is
   * equivalent to: <code>
   * {@link GLBlend#enableBlendingSeparateWithEquationSeparate}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   BlendEquation.BLEND_EQUATION_ADD,
   *   BlendEquation.BLEND_EQUATION_ADD);
   * </code>
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlending(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException;

  /**
   * Enable blending with the given blending functions. The function is
   * equivalent to: <code>
   * {@link GLBlend#enableBlendingSeparateWithEquationSeparate}(
   *   source_rgb_factor,
   *   source_alpha_factor,
   *   destination_rgb_factor,
   *   destination_alpha_factor,
   *   BlendEquation.BLEND_EQUATION_ADD,
   *   BlendEquation.BLEND_EQUATION_ADD);
   * </code>
   * 
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

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: <code>
   * {@link GLBlend#enableBlendingSeparateWithEquationSeparate}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   equation,
   *   equation);
   * </code>
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlendingWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException;

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: <code>
   * {@link GLBlend#enableBlendingSeparateWithEquationSeparate}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   equation_rgb,
   *   equation_alpha);
   * </code>
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlendingWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException;

  /**
   * <p>
   * Enable blending with the blending functions
   * <code>source_rgb_factor</code>, <code>source_alpha_factor</code>,
   * <code>destination_rgb_factor</code>, and
   * <code>destination_alphafactor</code>. The OpenGL defaults are
   * <code>BLEND_ONE</code> for <code>source_rgb_factor</code> and
   * <code>source_alpha_factor</code>, and <code>BLEND_ZERO</code>, for and
   * <code>destination_rgb_factor</code> and
   * <code>destination_alpha_factor</code>, respectively.
   * </p>
   * <p>
   * The given blend functions are applied to the RGB and alpha elements of
   * the colors in question separately (as opposed to
   * {@link GLBlend#enableBlending}, which applies the given functions to the
   * RGB and alpha components simultaneously).
   * </p>
   * <p>
   * The given blend equations <code>equation_rgb</code> and
   * <code>equation_alpha</code> determine how an incoming pixel is blended
   * with the corresponding pixel in the framebuffer. The OpenGL default is
   * <code>BLEND_EQUATION_ADD</code> for both equations.
   * </p>
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
   * @param equation_rgb
   *          The equation used to blend the RGB components.
   * @param equation_alpha
   *          The equation used to blend the alpha components.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>source_rgb_factor == null || source_alpha_factor == null ||
   *               destination_rgb_factor == null || destination_alpha_factor == null ||
   *               equation_rgb == null || equation_alpha == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableBlendingSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException;
}
