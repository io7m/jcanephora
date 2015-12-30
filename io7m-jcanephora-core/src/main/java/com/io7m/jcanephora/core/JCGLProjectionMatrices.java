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

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jtensors.MatrixWritable4x4FType;

/**
 * Functions to produce projection matrices.
 *
 * Memory for this implementation is allocated on creation of the class instance
 * and reused.
 */

public final class JCGLProjectionMatrices implements JCGLProjectionMatricesType
{
  private JCGLProjectionMatrices()
  {

  }

  /**
   * @return A {@link JCGLProjectionMatricesType} instance
   */

  public static JCGLProjectionMatricesType newMatrices()
  {
    return new JCGLProjectionMatrices();
  }

  @Override
  public void makeFrustumProjection(
    final MatrixWritable4x4FType matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
  {
    NullCheck.notNull(matrix, "Matrix");
    RangeCheck.checkGreaterEqualDouble(
      z_near,
      "Near Z",
      0.0,
      "Minimum Z distance");
    RangeCheck.checkLessDouble(z_near, "Near Z", z_far, "Far Z");

    final double r0c0 = (2.0 * z_near) / (x_max - x_min);
    final double r0c2 = (x_max + x_min) / (x_max - x_min);
    final double r1c1 = (2.0 * z_near) / (y_max - y_min);
    final double r1c2 = (y_max + y_min) / (y_max - y_min);

    final double r2c2;
    final double r2c3;

    if (z_far >= Double.POSITIVE_INFINITY) {
      r2c2 = -1.0;
      r2c3 = -2.0 * z_near;
    } else {
      r2c2 = -((z_far + z_near) / (z_far - z_near));
      r2c3 = -((2.0 * z_far * z_near) / (z_far - z_near));
    }

    matrix.setRowColumnF(0, 0, (float) r0c0);
    matrix.setRowColumnF(0, 1, 0.0f);
    matrix.setRowColumnF(0, 2, (float) r0c2);
    matrix.setRowColumnF(0, 3, 0.0f);

    matrix.setRowColumnF(1, 0, 0.0f);
    matrix.setRowColumnF(1, 1, (float) r1c1);
    matrix.setRowColumnF(1, 2, (float) r1c2);
    matrix.setRowColumnF(1, 3, 0.0f);

    matrix.setRowColumnF(2, 0, 0.0f);
    matrix.setRowColumnF(2, 1, 0.0f);
    matrix.setRowColumnF(2, 2, (float) r2c2);
    matrix.setRowColumnF(2, 3, (float) r2c3);

    matrix.setRowColumnF(3, 0, 0.0f);
    matrix.setRowColumnF(3, 1, 0.0f);
    matrix.setRowColumnF(3, 2, -1.0f);
    matrix.setRowColumnF(3, 3, 0.0f);
  }

  @Override
  public void makeOrthographicProjection(
    final MatrixWritable4x4FType matrix,
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
  {
    NullCheck.notNull(matrix, "Matrix");

    final double rml = x_max - x_min;
    final double rpl = x_max + x_min;
    final double tmb = y_max - y_min;
    final double tpb = y_max + y_min;
    final double fmn = z_far - z_near;
    final double fpn = z_far + z_near;

    final float r0c0 = (float) (2.0 / rml);
    final float r0c3 = (float) -(rpl / rml);
    final float r1c1 = (float) (2.0 / tmb);
    final float r1c3 = (float) -(tpb / tmb);
    final float r2c2 = (float) (-2.0 / fmn);
    final float r2c3 = (float) -(fpn / fmn);

    matrix.setRowColumnF(0, 0, r0c0);
    matrix.setRowColumnF(0, 1, 0.0f);
    matrix.setRowColumnF(0, 2, 0.0f);
    matrix.setRowColumnF(0, 3, r0c3);

    matrix.setRowColumnF(1, 0, 0.0f);
    matrix.setRowColumnF(1, 1, r1c1);
    matrix.setRowColumnF(1, 2, 0.0f);
    matrix.setRowColumnF(1, 3, r1c3);

    matrix.setRowColumnF(2, 0, 0.0f);
    matrix.setRowColumnF(2, 1, 0.0f);
    matrix.setRowColumnF(2, 2, r2c2);
    matrix.setRowColumnF(2, 3, r2c3);

    matrix.setRowColumnF(3, 0, 0.0f);
    matrix.setRowColumnF(3, 1, 0.0f);
    matrix.setRowColumnF(3, 2, 0.0f);
    matrix.setRowColumnF(3, 3, 1.0f);
  }

  @Override
  public void makePerspectiveProjection(
    final MatrixWritable4x4FType matrix,
    final double z_near,
    final double z_far,
    final double aspect,
    final double horizontal_fov)
  {
    NullCheck.notNull(matrix, "matrix");

    final double x_max = z_near * Math.tan(horizontal_fov / 2.0);
    final double x_min = -x_max;
    final double y_max = x_max / aspect;
    final double y_min = -y_max;

    this.makeFrustumProjection(
      matrix,
      x_min,
      x_max,
      y_min,
      y_max,
      z_near,
      z_far);
  }
}
