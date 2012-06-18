package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualFloat;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;

public class ProjectionMatrixTest
{
  @Test public void testOrthographic()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makeOrthographic(m, 0.0, 10.0, 0.0, 10.0, 1.0, 10.0);

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.2f,
      m.get(0, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(0, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(0, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      -1.0f,
      m.get(0, 3)));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(1, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.2f,
      m.get(1, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(1, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      -1.0f,
      m.get(1, 3)));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(2, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(2, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqualExplicit(
      -0.2f,
      m.get(2, 2),
      0.1f));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqualExplicit(
      -1.2f,
      m.get(2, 3),
      0.1f));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(3, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(3, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(3, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      1.0f,
      m.get(3, 3)));
  }

  @Test(expected = ConstraintError.class) public void testOrthographicNull()
    throws ConstraintError
  {
    ProjectionMatrix.makeOrthographic(null, 0.0, 1.0, 0.0, 1.0, 1.0, 100.0);
  }

  @Test public void testPerspective()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makePerspective(m, 1.0, 1000.0, 1.3, Math.PI / 4);

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      1.0f,
      m.get(0, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(0, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(0, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(0, 3)));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(1, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqualExplicit(
      1.29999f,
      m.get(1, 1),
      0.0001f));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(1, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(1, 3)));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(2, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(2, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqualExplicit(
      -1.0f,
      m.get(2, 2),
      0.1f));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqualExplicit(
      -2.0f,
      m.get(2, 3),
      0.1f));

    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(3, 0)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      0.0f,
      m.get(3, 1)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      -1.0f,
      m.get(3, 2)));
    Assert.assertTrue(ApproximatelyEqualFloat.approximatelyEqual(
      1.0f,
      m.get(3, 3)));
  }

  @Test(expected = ConstraintError.class) public void testPerspectiveNull()
    throws ConstraintError
  {
    ProjectionMatrix.makePerspective(null, 1.0, 100.0, 1.0, Math.PI / 2);
  }
}
