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

import com.io7m.jcanephora.core.JCGLProjectionMatricesType;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jtensors.Matrix4x4FType;
import com.io7m.jtensors.MatrixHeapArrayM4x4F;
import org.junit.Assert;
import org.junit.Test;

/**
 * Projection matrix contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLProjectionMatricesContract
{
  protected abstract JCGLProjectionMatricesType newProjectionMatrices();

  @Test(expected = RangeCheckException.class)
  public final void testFrustumFarLessThanNear()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makeFrustumProjection(m, -1.0, 1.0, -1.0, 1.0, 1.0, -1.0);
  }

  @Test(expected = RangeCheckException.class)
  public final void testFrustumNearFarSame()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makeFrustumProjection(m, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0);
  }

  @Test(expected = RangeCheckException.class)
  public final void testFrustumNearNegative()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makeFrustumProjection(m, -1.0, 1.0, -1.0, 1.0, -0.001, 100.0);
  }

  @Test
  public final void testFrustumSimple()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makeFrustumProjection(
      m,
      -1.0,
      1.0,
      -1.0,
      1.0,
      5.0,
      100.0);

    System.out.println(m);

    Assert.assertEquals(5.0, (double) m.getRowColumnF(0, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 0), 0.0);
    Assert.assertEquals(5.0, (double) m.getRowColumnF(1, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 1), 0.0);
    Assert.assertEquals(
      -1.105263113975525,
      (double) m.getRowColumnF(2, 2),
      0.0);
    Assert.assertEquals(
      -10.526315689086914,
      (double) m.getRowColumnF(2, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 1), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(3, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 3), 0.0);
  }


  @Test
  public final void testOrthographic()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makeOrthographicProjection(
      m,
      0.0,
      10.0,
      0.0,
      10.0,
      1.0,
      10.0);

    Assert.assertEquals(0.2, (double) m.getRowColumnF(0, 0), 0.00000001);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 2), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(0, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 0), 0.0);
    Assert.assertEquals(0.2, (double) m.getRowColumnF(1, 1), 0.00000001);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 2), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(1, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 1), 0.0);
    Assert.assertEquals(
      -0.22222222,
      (double) m.getRowColumnF(2, 2),
      0.00000001);
    Assert.assertEquals(-1.2222222, (double) m.getRowColumnF(2, 3), 0.00000001);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 2), 0.0);
    Assert.assertEquals(1.0, (double) m.getRowColumnF(3, 3), 0.0);
  }


  @Test
  public final void testPerspective()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makePerspectiveProjection(
      m,
      1.0,
      1000.0,
      1.0,
      Math.toRadians(90.0));

    System.out.println(m);

    Assert.assertEquals(1.0, (double) m.getRowColumnF(0, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 0), 0.0);
    Assert.assertEquals(1.0, (double) m.getRowColumnF(1, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 1), 0.0);
    Assert.assertEquals(
      -1.002002000808716,
      (double) m.getRowColumnF(2, 2),
      0.00000000001);
    Assert.assertEquals(
      -2.002002000808716,
      (double) m.getRowColumnF(2, 3),
      0.00000000001);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 1), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(3, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 3), 0.0);
  }

  @Test
  public final void testPerspectiveInfinite()
  {
    final JCGLProjectionMatricesType pm = this.newProjectionMatrices();
    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    pm.makePerspectiveProjection(
      m,
      1.0,
      Double.POSITIVE_INFINITY,
      1.3,
      Math.PI / 4.0);

    System.out.println(m);

    Assert.assertEquals(
      2.414213657379150,
      (double) m.getRowColumnF(0, 0),
      0.00000000001);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 0), 0.0);
    Assert.assertEquals(
      3.138477563858032,
      (double) m.getRowColumnF(1, 1),
      0.00000000001);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 1), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(2, 2), 0.0);
    Assert.assertEquals(-2.0, (double) m.getRowColumnF(2, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 1), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(3, 2), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 3), 0.0);
  }

}
