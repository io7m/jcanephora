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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.AlmostEqualFloat;
import com.io7m.jaux.AlmostEqualFloat.ContextRelative;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;

public class ProjectionMatrixTest
{
  @SuppressWarnings("static-method") @Test public void testOrthographic()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makeOrthographicProjection(
      m,
      0.0,
      10.0,
      0.0,
      10.0,
      1.0,
      10.0);
    final ContextRelative c = new AlmostEqualFloat.ContextRelative();

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.2f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.2f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 1)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -0.22222222f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -1.2222222f,
      m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 1.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testOrthographicNull()
      throws ConstraintError
  {
    ProjectionMatrix.makeOrthographicProjection(
      null,
      0.0,
      1.0,
      0.0,
      1.0,
      1.0,
      100.0);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFrustumNull()
      throws ConstraintError
  {
    ProjectionMatrix.makeFrustumProjection(
      null,
      -1.0,
      1.0,
      -1.0,
      1.0,
      1.0,
      100.0);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFrustumNearNegative()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makeFrustumProjection(
      m,
      -1.0,
      1.0,
      -1.0,
      1.0,
      -0.001,
      100.0);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFrustumNearFarSame()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makeFrustumProjection(m, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFrustumFarLessThanNear()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix
      .makeFrustumProjection(m, -1.0, 1.0, -1.0, 1.0, 1.0, -1.0);
  }

  @SuppressWarnings("static-method") @Test public void testFrustumSimple()
    throws ConstraintError
  {
    final ContextRelative c = new AlmostEqualFloat.ContextRelative();
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makeFrustumProjection(
      m,
      -1.0,
      1.0,
      -1.0,
      1.0,
      5.0,
      100.0);

    System.out.println(m);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 5.0f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 5.0f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -1.105263113975525f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -10.526315689086914f,
      m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test public void testPerspective()
    throws ConstraintError
  {
    final ContextRelative c = new AlmostEqualFloat.ContextRelative();
    final MatrixM4x4F m = new MatrixM4x4F();

    ProjectionMatrix.makePerspectiveProjection(
      m,
      1.0,
      1000.0,
      1.0,
      Math.toRadians(90.0));

    System.out.println(m);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 1.0f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 1.0f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -1.002002000808716f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      -2.002002000808716f,
      m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testPerspectiveInfinite()
      throws ConstraintError
  {
    final ContextRelative c = new AlmostEqualFloat.ContextRelative();
    final MatrixM4x4F m = new MatrixM4x4F();

    ProjectionMatrix.makePerspectiveProjection(
      m,
      1.0,
      Double.POSITIVE_INFINITY,
      1.3,
      Math.PI / 4);

    System.out.println(m);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      2.414213657379150f,
      m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      c,
      3.138477563858032f,
      m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -2.0f, m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, -1.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(c, 0.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPerspectiveNull()
      throws ConstraintError
  {
    ProjectionMatrix.makePerspectiveProjection(
      null,
      1.0,
      100.0,
      1.0,
      Math.PI / 2);
  }
}
