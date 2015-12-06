/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBlendingMisconfigured;

/**
 * The interface to OpenGL blending.
 */

public interface JCGLBlendingType
{
  /**
   * Disable blending.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void blendingDisable()
    throws JCGLException;

  /**
   * @return {@code true} iff blending is enabled
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean blendingIsEnabled()
    throws JCGLException;

  /**
   * Enable blending with the given blending functions. The function is
   * equivalent to: {@code
   * {@link JCGLBlendingType#blendingEnableSeparateWithEquationSeparate}(
   * source_factor, source_factor, destination_factor, destination_factor,
   * JCGLBlendEquation.BLEND_EQUATION_ADD, JCGLBlendEquation
   * .BLEND_EQUATION_ADD); }
   *
   * @param source_factor      The source factor
   * @param destination_factor The destination factor
   *
   * @throws JCGLException                      Iff an OpenGL error occurs
   * @throws JCGLExceptionBlendingMisconfigured Iff the combination of blending
   *                                            parameters is invalid
   */

  default void blendingEnable(
    final JCGLBlendFunction source_factor,
    final JCGLBlendFunction destination_factor)
    throws JCGLException,
    JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      JCGLBlendEquation.BLEND_EQUATION_ADD,
      JCGLBlendEquation.BLEND_EQUATION_ADD);
  }

  /**
   * Enable blending with the given blending functions. The function is
   * equivalent to: {@code
   * {@link JCGLBlendingType#blendingEnableSeparateWithEquationSeparate}(
   * source_rgb_factor, source_alpha_factor, destination_rgb_factor,
   * destination_alpha_factor, JCGLBlendEquation.BLEND_EQUATION_ADD,
   * JCGLBlendEquation.BLEND_EQUATION_ADD); }
   *
   * @param source_rgb_factor        The function used to blend the RGB
   *                                 components of incoming (source) color
   *                                 values
   * @param source_alpha_factor      The function used to blend the alpha
   *                                 components of incoming (source) color
   *                                 values
   * @param destination_rgb_factor   The function used to blend the RGB
   *                                 components of the existing (destination)
   *                                 color values
   * @param destination_alpha_factor The function used to blend the alpha
   *                                 components of the existing (destination)
   *                                 color values
   *
   * @throws JCGLException                      Iff an OpenGL error occurs
   * @throws JCGLExceptionBlendingMisconfigured Iff the combination of blending
   *                                            parameters is invalid
   */

  default void blendingEnableSeparate(
    final JCGLBlendFunction source_rgb_factor,
    final JCGLBlendFunction source_alpha_factor,
    final JCGLBlendFunction destination_rgb_factor,
    final JCGLBlendFunction destination_alpha_factor)
    throws JCGLException,
    JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      JCGLBlendEquation.BLEND_EQUATION_ADD,
      JCGLBlendEquation.BLEND_EQUATION_ADD);
  }

  /**
   * <p>Enable blending with the blending functions {@code source_rgb_factor},
   * {@code source_alpha_factor}, {@code destination_rgb_factor}, and {@code
   * destination_alphafactor}. The OpenGL defaults are {@code BLEND_ONE} for
   * {@code source_rgb_factor} and {@code source_alpha_factor}, and {@code
   * BLEND_ZERO}, for and {@code destination_rgb_factor} and {@code
   * destination_alpha_factor}, respectively.</p>
   *
   * <p>The given blend functions are applied to the RGB and alpha elements of
   * the colors in question separately (as opposed to {@link
   * JCGLBlendingType#blendingEnable}, which applies the given functions to the
   * RGB and alpha components simultaneously).</p>
   *
   * <p>The given blend equations {@code equation_rgb} and {@code
   * equation_alpha} determine how an incoming pixel is blended with the
   * corresponding pixel in the framebuffer. The OpenGL default is {@code
   * BLEND_EQUATION_ADD} for both equations.</p>
   *
   * <p>This is the most general form of blending specification, and the other
   * methods in the interface are defined in terms of this one.</p>
   *
   * @param source_rgb_factor        The function used to blend the RGB
   *                                 components of incoming (source) color
   *                                 values
   * @param source_alpha_factor      The function used to blend the alpha
   *                                 components of incoming (source) color
   *                                 values
   * @param destination_rgb_factor   The function used to blend the RGB
   *                                 components of the existing (destination)
   *                                 color values
   * @param destination_alpha_factor The function used to blend the alpha
   *                                 components of the existing (destination)
   *                                 color values
   * @param equation_rgb             The equation used to blend the RGB
   *                                 components
   * @param equation_alpha           The equation used to blend the alpha
   *                                 components
   *
   * @throws JCGLException                      Iff an OpenGL error occurs
   * @throws JCGLExceptionBlendingMisconfigured Iff the combination of blending
   *                                            parameters is invalid
   */

  void blendingEnableSeparateWithEquationSeparate(
    final JCGLBlendFunction source_rgb_factor,
    final JCGLBlendFunction source_alpha_factor,
    final JCGLBlendFunction destination_rgb_factor,
    final JCGLBlendFunction destination_alpha_factor,
    final JCGLBlendEquation equation_rgb,
    final JCGLBlendEquation equation_alpha)
    throws JCGLException,
    JCGLExceptionBlendingMisconfigured;

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: {@code
   * {@link JCGLBlendingType#blendingEnableSeparateWithEquationSeparate}(
   * source_factor, source_factor, destination_factor, destination_factor,
   * equation, equation); }
   *
   * @param source_factor      The source factor
   * @param destination_factor The destination factor
   * @param equation           The blend equation
   *
   * @throws JCGLException                      Iff an OpenGL error occurs
   * @throws JCGLExceptionBlendingMisconfigured Iff the combination of blending
   *                                            parameters is invalid
   */

  default void blendingEnableWithEquation(
    final JCGLBlendFunction source_factor,
    final JCGLBlendFunction destination_factor,
    final JCGLBlendEquation equation)
    throws JCGLException,
    JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  /**
   * Enable blending with the given blending functions and equation. The
   * function is equivalent to: {@code
   * {@link JCGLBlendingType#blendingEnableSeparateWithEquationSeparate}(
   * source_factor, source_factor, destination_factor, destination_factor,
   * equation_rgb, equation_alpha); }
   *
   * @param source_factor      The source factor
   * @param destination_factor The destination factor
   * @param equation_rgb       The blend equation for the RGB components
   * @param equation_alpha     The blend equation for the alpha components
   *
   * @throws JCGLException                      Iff an OpenGL error occurs
   * @throws JCGLExceptionBlendingMisconfigured Iff the combination of blending
   *                                            parameters is invalid
   */

  default void blendingEnableWithEquationSeparate(
    final JCGLBlendFunction source_factor,
    final JCGLBlendFunction destination_factor,
    final JCGLBlendEquation equation_rgb,
    final JCGLBlendEquation equation_alpha)
    throws JCGLException,
    JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }
}
