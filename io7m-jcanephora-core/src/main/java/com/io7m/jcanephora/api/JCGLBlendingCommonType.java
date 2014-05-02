/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.JCGLRuntimeException;

/**
 * Simplified and type-safe interface to the common subset of blending
 * features supported by OpenGL ES2 and OpenGL 3.*.
 */

public interface JCGLBlendingCommonType
{
  /**
   * Disable blending.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingDisable()
    throws JCGLRuntimeException;

  /**
   * Enable blending with the given blending functions. The function is
   * equivalent to: <code>
   * {@link JCGLBlendingCommonType#blendingEnableSeparateWithEquationSeparateES2}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   BlendEquation.BLEND_EQUATION_ADD,
   *   BlendEquation.BLEND_EQUATION_ADD);
   * </code>
   * 
   * @param source_factor
   *          The source factor.
   * @param destination_factor
   *          The destination factor.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingEnable(
    final BlendFunction source_factor,
    final BlendFunction destination_factor)
    throws JCGLRuntimeException;

  /**
   * Enable blending with the given blending functions. The function is
   * equivalent to: <code>
   * {@link JCGLBlendingCommonType#blendingEnableSeparateWithEquationSeparateES2}(
   *   source_rgb_factor,
   *   source_alpha_factor,
   *   destination_rgb_factor,
   *   destination_alpha_factor,
   *   BlendEquation.BLEND_EQUATION_ADD,
   *   BlendEquation.BLEND_EQUATION_ADD);
   * </code>
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
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingEnableSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor)
    throws JCGLRuntimeException;

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
   * {@link JCGLBlendingCommonType#blendingEnable}, which applies the given
   * functions to the RGB and alpha components simultaneously).
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
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingEnableSeparateWithEquationSeparateES2(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLRuntimeException;

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: <code>
   * {@link JCGLBlendingCommonType#blendingEnableSeparateWithEquationSeparateES2}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   equation,
   *   equation);
   * </code>
   * 
   * @param source_factor
   *          The source factor.
   * @param destination_factor
   *          The destination factor.
   * @param equation
   *          The blend equation.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingEnableWithEquationES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation)
    throws JCGLRuntimeException;

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: <code>
   * {@link JCGLBlendingCommonType#blendingEnableSeparateWithEquationSeparateES2}(
   *   source_factor,
   *   source_factor,
   *   destination_factor,
   *   destination_factor,
   *   equation_rgb,
   *   equation_alpha);
   * </code>
   * 
   * @param source_factor
   *          The source factor.
   * @param destination_factor
   *          The destination factor.
   * @param equation_rgb
   *          The blend equation for the RGB components.
   * @param equation_alpha
   *          The blend equation for the alpha components.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void blendingEnableWithEquationSeparateES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLRuntimeException;

  /**
   * @return <code>true</code> iff blending is enabled.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean blendingIsEnabled()
    throws JCGLRuntimeException;
}
