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

package com.io7m.jcanephora.core;

import com.io7m.jtensors.MatrixWritable4x4FType;

/**
 * Functions for creating projection matrices.
 */

public interface JCGLProjectionMatricesType
{
  /**
   * <p>Calculate a matrix that produces a perspective projection. The {@code
   * (x_min, y_min, z_near)} and {@code (x_max, y_max, z_near)} parameters
   * specify the points on the near clipping plane that are mapped to the
   * lower-left and upper-right corners of the window, respectively, assuming
   * that the eye is located at {@code (0, 0, 0)}. The {@code z_far} parameter
   * specifies the location of the far clipping plane.</p>
   *
   * <p>Note that iff {@code z_far &gt;= Double.POSITIVE_INFINITY}, the function
   * produces an "infinite projection matrix", suitable for use in code that
   * deals with shadow volumes.</p>
   *
   * <p>See
   * <a href="http://http.developer.nvidia.com/GPUGems/gpugems_ch09.html">GPU
   * Gems</a></p>
   *
   * @param x_min  The minimum X clip plane.
   * @param x_max  The maximum X clip plane.
   * @param y_min  The minimum Y clip plane.
   * @param y_max  The maximum Y clip plane.
   * @param z_near The near Z clip plane.
   * @param z_far  The far Z clip plane.
   * @param matrix The matrix to which values will be written.
   */

  void makeFrustumProjection(
    final MatrixWritable4x4FType matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far);

  /**
   * Calculate a projection matrix that produces an orthographic projection
   * based on the given clipping plane coordinates.
   *
   * @param x_min  The left clipping plane coordinate.
   * @param x_max  The right clipping plane coordinate.
   * @param y_min  The bottom clipping plane coordinate.
   * @param y_max  The top clipping plane coordinate.
   * @param z_near The near clipping plane coordinate.
   * @param z_far  The far clipping plane coordinate.
   * @param matrix The matrix to which values will be written.
   */

  void makeOrthographicProjection(
    final MatrixWritable4x4FType matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far);

  /**
   * <p>Calculate a matrix that will produce a perspective projection based on
   * the given view frustum parameters, the aspect ratio of the viewport and a
   * given horizontal field of view in radians. Note that {@code fov_radians}
   * represents the full horizontal field of view: the angle at the base of the
   * triangle formed by the frustum on the {@code x/z} plane.</p>
   *
   * <p>Note that iff {@code z_far &gt;= Double.POSITIVE_INFINITY}, the
   * function produces an "infinite projection matrix", suitable for use in code
   * that deals with shadow volumes.</p>
   *
   * <p>See
   * <a href="http://http.developer.nvidia.com/GPUGems/gpugems_ch09.html">GPU
   * Gems</a></p>
   *
   * @param z_near         The near clipping plane coordinate.
   * @param z_far          The far clipping plane coordinate.
   * @param aspect         The aspect ratio of the viewport; the width divided
   *                       by the height. For example, an aspect ratio of 2.0
   *                       indicates a viewport twice as wide as it is high.
   * @param horizontal_fov The horizontal field of view in radians.
   * @param matrix         The matrix to which values will be written.
   */

  void makePerspectiveProjection(
    final MatrixWritable4x4FType matrix,
    final double z_near,
    final double z_far,
    final double aspect,
    final double horizontal_fov);
}
