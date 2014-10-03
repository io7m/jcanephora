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

package com.io7m.jcanephora;

/**
 * The type of {@link ClearSpecification} builders.
 */

public interface ClearSpecificationBuilderType extends
  ClearSpecificationBuilderReadableType
{
  /**
   * @return A clear specification based on all of the parameters given so far
   */

  ClearSpecification build();

  /**
   * <p>
   * Disable clearing of the color buffer(s).
   * </p>
   */

  void disableColorBufferClear();

  /**
   * <p>
   * Disable clearing of the depth buffer.
   * </p>
   */

  void disableDepthBufferClear();

  /**
   * <p>
   * Disable clearing of the stencil buffer.
   * </p>
   */

  void disableStencilBufferClear();

  /**
   * <p>
   * Enable clearing of all color buffers.
   * </p>
   *
   * @param r
   *          The red value
   * @param g
   *          The green value
   * @param b
   *          The blue value
   */

  void enableColorBufferClear3f(
    final float r,
    final float g,
    final float b);

  /**
   * <p>
   * Enable clearing of all color buffers.
   * </p>
   *
   * @param r
   *          The red value
   * @param g
   *          The green value
   * @param b
   *          The blue value
   * @param a
   *          The alpha value
   */

  void enableColorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a);

  /**
   * <p>
   * Enable clearing of the depth buffer.
   * </p>
   *
   * @param d
   *          The depth value to which the buffer will be cleared
   */

  void enableDepthBufferClear(
    final float d);

  /**
   * <p>
   * Enable clearing of the stencil buffer.
   * </p>
   *
   * @param s
   *          The stencil value to which the buffer will be cleared
   */

  void enableStencilBufferClear(
    final int s);

  /**
   * <p>
   * Enable/disable strict checking.
   * </p>
   * <p>
   * If checking is enabled, checks will be performed prior to clearing to
   * ensure that buffers really do exist. For example, if the specification
   * implies depth buffer clearing, but the currently bound framebuffer does
   * not have a depth buffer, then an exception will be raised on clearing.
   * </p>
   *
   * @param enabled
   *          <code>true</code> if checking should be enabled
   */

  void setStrictChecking(
    boolean enabled);
}
