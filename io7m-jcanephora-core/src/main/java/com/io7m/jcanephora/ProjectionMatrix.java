/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;

/**
 * Functions for producing projection matrices.
 */

public final class ProjectionMatrix
{
  /**
   * Calculate a projection matrix that produces an orthographic projection
   * based on the given clipping plane coordinates.
   * 
   * @param x_min
   *          The left clipping plane coordinate.
   * @param x_max
   *          The right clipping plane coordinate.
   * @param y_min
   *          The bottom clipping plane coordinate.
   * @param y_max
   *          The top clipping plane coordinate.
   * @param z_near
   *          The near clipping plane coordinate.
   * @param z_far
   *          The far clipping plane coordinate.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>m == null</code></li>
   *           </ul>
   */

  public static void makeOrthographicProjection(
    final @Nonnull MatrixM4x4F matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "Matrix");

    final double rml = x_max - x_min;
    final double rpl = x_max + x_min;
    final double tmb = y_max - y_min;
    final double tpb = y_max + y_min;
    final double fmn = z_far - z_near;
    final double fpn = z_far + z_near;

    final float r0c0 = (float) (2 / rml);
    final float r0c3 = (float) -(rpl / rml);
    final float r1c1 = (float) (2 / tmb);
    final float r1c3 = (float) -(tpb / tmb);
    final float r2c2 = (float) (-2 / fmn);
    final float r2c3 = (float) -(fpn / fmn);

    matrix.set(0, 0, r0c0);
    matrix.set(0, 1, 0.0f);
    matrix.set(0, 2, 0.0f);
    matrix.set(0, 3, r0c3);

    matrix.set(1, 0, 0.0f);
    matrix.set(1, 1, r1c1);
    matrix.set(1, 2, 0.0f);
    matrix.set(1, 3, r1c3);

    matrix.set(2, 0, 0.0f);
    matrix.set(2, 1, 0.0f);
    matrix.set(2, 2, r2c2);
    matrix.set(2, 3, r2c3);

    matrix.set(3, 0, 0.0f);
    matrix.set(3, 1, 0.0f);
    matrix.set(3, 2, 0.0f);
    matrix.set(3, 3, 1.0f);
  }

  /**
   * <p>
   * Calculate a matrix that produces a perspective projection. The
   * <code>(x_min, y_min, z_near)</code> and
   * <code>(x_max, y_max, z_near)</code> parameters specify the points on the
   * near clipping plane that are mapped to the lower-left and upper-right
   * corners of the window, respectively, assuming that the eye is located at
   * <code>(0, 0, 0)</code>. The <code>z_far</code> parameter specifies the
   * location of the far clipping plane.
   * </p>
   * <p>
   * Note that iff <code>z_far >= Double.POSITIVE_INFINITY</code>, the
   * function produces an "infinite projection matrix", suitable for use in
   * code that deals with shadow volumes.
   * <p>
   * 
   * @link http://http.developer.nvidia.com/GPUGems/gpugems_ch09.html
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>m == null</code></li> <li><code>0 &lt=; z_near &lt;
   *           z_far == false</code></li>
   *           </ul>
   */

  public static void makeFrustumProjection(
    final @Nonnull MatrixM4x4F matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainRange(z_near, 0.0, Double.MAX_VALUE);
    Constraints.constrainLessThan(z_near, z_far);

    final double r0c0 = (2 * z_near) / (x_max - x_min);
    final double r0c2 = (x_max + x_min) / (x_max - x_min);
    final double r1c1 = (2 * z_near) / (y_max - y_min);
    final double r1c2 = (y_max + y_min) / (y_max - y_min);

    final double r2c2;
    final double r2c3;

    if (z_far >= Double.POSITIVE_INFINITY) {
      r2c2 = -1.0;
      r2c3 = -2.0 * z_near;
    } else {
      r2c2 = -((z_far + z_near) / (z_far - z_near));
      r2c3 = -((2 * z_far * z_near) / (z_far - z_near));
    }

    matrix.set(0, 0, (float) r0c0);
    matrix.set(0, 1, 0.0f);
    matrix.set(0, 2, (float) r0c2);
    matrix.set(0, 3, 0.0f);

    matrix.set(1, 0, 0.0f);
    matrix.set(1, 1, (float) r1c1);
    matrix.set(1, 2, (float) r1c2);
    matrix.set(1, 3, 0.0f);

    matrix.set(2, 0, 0.0f);
    matrix.set(2, 1, 0.0f);
    matrix.set(2, 2, (float) r2c2);
    matrix.set(2, 3, (float) r2c3);

    matrix.set(3, 0, 0.0f);
    matrix.set(3, 1, 0.0f);
    matrix.set(3, 2, -1.0f);
    matrix.set(3, 3, 0.0f);
  }

  /**
   * <p>
   * Calculate a matrix that will produce a perspective projection based on
   * the given view frustum parameters, the aspect ratio of the viewport and a
   * given horizontal field of view in radians. Note that
   * <code>fov_radians</code> represents the full horizontal field of view:
   * the angle at the base of the triangle formed by the frustum on the
   * <code>x/z</code> plane.
   * </p>
   * <p>
   * Note that iff <code>z_far >= Double.POSITIVE_INFINITY</code>, the
   * function produces an "infinite projection matrix", suitable for use in
   * code that deals with shadow volumes.
   * <p>
   * 
   * @link http://http.developer.nvidia.com/GPUGems/gpugems_ch09.html
   * 
   * @param z_near
   *          The near clipping plane coordinate.
   * @param z_far
   *          The far clipping plane coordinate.
   * @param aspect
   *          The aspect ratio of the viewport; the width divided by the
   *          height. For example, an aspect ratio of 2.0 indicates a viewport
   *          twice as wide as it is high.
   * @param horizontal_fov
   *          The horizontal field of view in radians.
   */

  public static void makePerspectiveProjection(
    final @Nonnull MatrixM4x4F matrix,
    final double z_near,
    final double z_far,
    final double aspect,
    final double horizontal_fov)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "matrix");

    final double x_max = z_near * Math.tan(horizontal_fov / 2.0);
    final double x_min = -x_max;
    final double y_max = x_max / aspect;
    final double y_min = -y_max;

    ProjectionMatrix.makeFrustumProjection(
      matrix,
      x_min,
      x_max,
      y_min,
      y_max,
      z_near,
      z_far);
  }
}
