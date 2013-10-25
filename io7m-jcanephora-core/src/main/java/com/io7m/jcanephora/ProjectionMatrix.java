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
   * @param left
   *          The left clipping plane coordinate.
   * @param right
   *          The right clipping plane coordinate.
   * @param bottom
   *          The bottom clipping plane coordinate.
   * @param top
   *          The top clipping plane coordinate.
   * @param near
   *          The near clipping plane coordinate.
   * @param far
   *          The far clipping plane coordinate.
   * @throws ConstraintError
   *           Iff an internal contract violation occurs.
   */

  public static void makeOrthographic(
    final @Nonnull MatrixM4x4F matrix,
    final double left,
    final double right,
    final double bottom,
    final double top,
    final double near,
    final double far)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "matrix");

    final double rml = right - left;
    final double rpl = right + left;
    final double tmb = top - bottom;
    final double tpb = top + bottom;
    final double fmn = far - near;
    final double fpn = far + near;

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
   * Calculate a matrix that will produce a perspective projection based on
   * the given view frustum parameters, the aspect ratio of the viewport and a
   * given field of view in radians.
   * 
   * Note that iff <code>z_far >= Double.POSITIVE_INFINITY</code>, the
   * function produces an "infinite projection matrix", suitable for use in
   * code that deals with shadow volumes.
   * 
   * @link http://http.developer.nvidia.com/GPUGems/gpugems_ch09.html
   * 
   * @param z_near
   *          The near clipping plane coordinate.
   * @param z_far
   *          The far clipping plane coordinate.
   * @param aspect
   *          The aspect ratio of the viewport; the width divided by the
   *          height.
   * @param fov_radians
   *          The field of view in radians.
   */

  public static void makePerspective(
    final @Nonnull MatrixM4x4F matrix,
    final double z_near,
    final double z_far,
    final double aspect,
    final double fov_radians)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "matrix");

    final double x_max = z_near * (Math.tan(fov_radians));
    final double x_min = -x_max;
    final double y_min = x_min / aspect;
    final double y_max = x_max / aspect;

    final double r0c0 = (2.0 * z_near) / (x_max - x_min);
    final double r1c1 = (2.0 * z_near) / (y_max - y_min);

    final double r0c2 = (x_max + x_min) / (x_max - x_min);
    final double r1c2 = (y_max + y_min) / (y_max - y_min);

    final double r2c2;
    final double r2c3;

    if (z_far >= Double.POSITIVE_INFINITY) {
      r2c2 = -1.0;
      r2c3 = -2.0 * z_near;
    } else {
      r2c2 = -(z_far + z_near) / (z_far - z_near);
      r2c3 = -(2.0 * z_far * z_near) / (z_far - z_near);
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
    matrix.set(3, 3, 1.0f);
  }
}
