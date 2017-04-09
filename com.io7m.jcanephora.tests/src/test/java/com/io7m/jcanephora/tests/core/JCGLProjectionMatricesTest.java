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

package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLProjectionMatrices;
import com.io7m.jcanephora.tests.contracts.JCGLProjectionMatricesContract;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;

/**
 * Projection matrix test.
 */

// CHECKSTYLE_JAVADOC:OFF

public final class JCGLProjectionMatricesTest extends
  JCGLProjectionMatricesContract
{
  public JCGLProjectionMatricesTest()
  {

  }

  @Override
  protected Matrix4x4D frustumProjectionRH(
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
  {
    return JCGLProjectionMatrices.frustumProjectionRH(
      x_min, x_max, y_min, y_max, z_near, z_far);
  }

  @Override
  protected Matrix4x4D orthographicProjectionRH(
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far)
  {
    return JCGLProjectionMatrices.orthographicProjectionRH(
      x_min, x_max, y_min, y_max, z_near, z_far);
  }

  @Override
  protected Matrix4x4D perspectiveProjectionRH(
    final double z_near,
    final double z_far,
    final double aspect,
    final double horizontal_fov)
  {
    return JCGLProjectionMatrices.perspectiveProjectionRH(
      z_near, z_far, aspect, horizontal_fov);
  }
}
