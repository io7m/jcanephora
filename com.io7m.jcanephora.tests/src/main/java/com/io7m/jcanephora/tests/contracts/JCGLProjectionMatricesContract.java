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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jranges.RangeCheckException;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import org.junit.Test;

import static com.io7m.jcanephora.tests.contracts.JCGLTestUtilities.checkAlmostEquals;

/**
 * Projection matrix contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLProjectionMatricesContract
{


  protected abstract Matrix4x4D frustumProjectionRH(
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far);

  protected abstract Matrix4x4D orthographicProjectionRH(
    final double x_min,
    final double x_max,
    final double y_min,
    final double y_max,
    final double z_near,
    final double z_far);

  protected abstract Matrix4x4D perspectiveProjectionRH(
    final double z_near,
    final double z_far,
    final double aspect,
    final double horizontal_fov);

  @Test(expected = RangeCheckException.class)
  public final void testFrustumFarLessThanNear()
  {
    this.frustumProjectionRH(-1.0, 1.0, -1.0, 1.0, 1.0, -1.0);
  }

  @Test(expected = RangeCheckException.class)
  public final void testFrustumNearFarSame()
  {
    this.frustumProjectionRH(-1.0, 1.0, -1.0, 1.0, 1.0, 1.0);
  }

  @Test(expected = RangeCheckException.class)
  public final void testFrustumNearNegative()
  {
    this.frustumProjectionRH(-1.0, 1.0, -1.0, 1.0, -0.001, 100.0);
  }

  @Test
  public final void testFrustumSimple()
  {
    final Matrix4x4D m =
      this.frustumProjectionRH(
        -1.0,
        1.0,
        -1.0,
        1.0,
        5.0,
        100.0);

    System.out.println(m);

    checkAlmostEquals(5.0, m.rowColumn(0, 0));
    checkAlmostEquals(0.0, m.rowColumn(0, 1));
    checkAlmostEquals(0.0, m.rowColumn(0, 2));
    checkAlmostEquals(0.0, m.rowColumn(0, 3));

    checkAlmostEquals(0.0, m.rowColumn(1, 0));
    checkAlmostEquals(5.0, m.rowColumn(1, 1));
    checkAlmostEquals(0.0, m.rowColumn(1, 2));
    checkAlmostEquals(0.0, m.rowColumn(1, 3));

    checkAlmostEquals(0.0, m.rowColumn(2, 0));
    checkAlmostEquals(0.0, m.rowColumn(2, 1));
    checkAlmostEquals(-1.105263113975525, m.rowColumn(2, 2));
    checkAlmostEquals(-10.526315689086914, m.rowColumn(2, 3));

    checkAlmostEquals(0.0, m.rowColumn(3, 0));
    checkAlmostEquals(0.0, m.rowColumn(3, 1));
    checkAlmostEquals(-1.0, m.rowColumn(3, 2));
    checkAlmostEquals(0.0, m.rowColumn(3, 3));
  }


  @Test
  public final void testOrthographic()
  {
    final Matrix4x4D m =
      this.orthographicProjectionRH(
        0.0,
        10.0,
        0.0,
        10.0,
        1.0,
        10.0);

    checkAlmostEquals(0.2, m.rowColumn(0, 0));
    checkAlmostEquals(0.0, m.rowColumn(0, 1));
    checkAlmostEquals(0.0, m.rowColumn(0, 2));
    checkAlmostEquals(-1.0, m.rowColumn(0, 3));

    checkAlmostEquals(0.0, m.rowColumn(1, 0));
    checkAlmostEquals(0.2, m.rowColumn(1, 1));
    checkAlmostEquals(0.0, m.rowColumn(1, 2));
    checkAlmostEquals(-1.0, m.rowColumn(1, 3));

    checkAlmostEquals(0.0, m.rowColumn(2, 0));
    checkAlmostEquals(0.0, m.rowColumn(2, 1));
    checkAlmostEquals(-0.22222222, m.rowColumn(2, 2));
    checkAlmostEquals(-1.2222222, m.rowColumn(2, 3));

    checkAlmostEquals(0.0, m.rowColumn(3, 0));
    checkAlmostEquals(0.0, m.rowColumn(3, 1));
    checkAlmostEquals(0.0, m.rowColumn(3, 2));
    checkAlmostEquals(1.0, m.rowColumn(3, 3));
  }


  @Test
  public final void testPerspective()
  {
    final Matrix4x4D m =
      this.perspectiveProjectionRH(
        1.0,
        1000.0,
        1.0,
        Math.toRadians(90.0));

    System.out.println(m);

    checkAlmostEquals(1.0, m.rowColumn(0, 0));
    checkAlmostEquals(0.0, m.rowColumn(0, 1));
    checkAlmostEquals(0.0, m.rowColumn(0, 2));
    checkAlmostEquals(0.0, m.rowColumn(0, 3));

    checkAlmostEquals(0.0, m.rowColumn(1, 0));
    checkAlmostEquals(1.0, m.rowColumn(1, 1));
    checkAlmostEquals(0.0, m.rowColumn(1, 2));
    checkAlmostEquals(0.0, m.rowColumn(1, 3));

    checkAlmostEquals(0.0, m.rowColumn(2, 0));
    checkAlmostEquals(0.0, m.rowColumn(2, 1));
    checkAlmostEquals(-1.002002000808716, m.rowColumn(2, 2));
    checkAlmostEquals(-2.002002000808716, m.rowColumn(2, 3));

    checkAlmostEquals(0.0, m.rowColumn(3, 0));
    checkAlmostEquals(0.0, m.rowColumn(3, 1));
    checkAlmostEquals(-1.0, m.rowColumn(3, 2));
    checkAlmostEquals(0.0, m.rowColumn(3, 3));
  }

  @Test
  public final void testPerspectiveInfinite()
  {
    final Matrix4x4D m =
      this.perspectiveProjectionRH(
        1.0,
        Double.POSITIVE_INFINITY,
        1.3,
        Math.PI / 4.0);

    System.out.println(m);

    checkAlmostEquals(2.414213657379150, m.rowColumn(0, 0));
    checkAlmostEquals(0.0, m.rowColumn(0, 1));
    checkAlmostEquals(0.0, m.rowColumn(0, 2));
    checkAlmostEquals(0.0, m.rowColumn(0, 3));

    checkAlmostEquals(0.0, m.rowColumn(1, 0));
    checkAlmostEquals(3.138477563858032, m.rowColumn(1, 1));
    checkAlmostEquals(0.0, m.rowColumn(1, 2));
    checkAlmostEquals(0.0, m.rowColumn(1, 3));

    checkAlmostEquals(0.0, m.rowColumn(2, 0));
    checkAlmostEquals(0.0, m.rowColumn(2, 1));
    checkAlmostEquals(-1.0, m.rowColumn(2, 2));
    checkAlmostEquals(-2.0, m.rowColumn(2, 3));

    checkAlmostEquals(0.0, m.rowColumn(3, 0));
    checkAlmostEquals(0.0, m.rowColumn(3, 1));
    checkAlmostEquals(-1.0, m.rowColumn(3, 2));
    checkAlmostEquals(0.0, m.rowColumn(3, 3));
  }

}
