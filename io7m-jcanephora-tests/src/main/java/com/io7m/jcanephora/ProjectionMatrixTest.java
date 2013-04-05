package com.io7m.jcanephora;

import junit.framework.Assert;

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
    ProjectionMatrix.makeOrthographic(m, 0.0, 10.0, 0.0, 10.0, 1.0, 10.0);
    final ContextRelative context = new AlmostEqualFloat.ContextRelative();

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.2f, m.get(0, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(0, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.2f, m.get(1, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(1, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 1)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -0.22222222f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.2222222f,
      m.get(2, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testOrthographicNull()
      throws ConstraintError
  {
    ProjectionMatrix.makeOrthographic(null, 0.0, 1.0, 0.0, 1.0, 1.0, 100.0);
  }

  @SuppressWarnings("static-method") @Test public void testPerspective()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makePerspective(m, 1.0, 1000.0, 1.3, Math.PI / 4);
    final ContextRelative context = new AlmostEqualFloat.ContextRelative();

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.0f, m.get(0, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.3f, m.get(1, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -2.002002f,
      m.get(2, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(3, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testPerspectiveInfinite()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    ProjectionMatrix.makePerspective(
      m,
      1.0,
      Double.POSITIVE_INFINITY,
      1.3,
      Math.PI / 4);

    final ContextRelative context = new AlmostEqualFloat.ContextRelative();

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.0f, m.get(0, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(0, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.3f, m.get(1, 1)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(1, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -2.0f,
      m.get(2, 3)));

    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 0)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(
      context,
      -1.0f,
      m.get(3, 2)));
    Assert
      .assertTrue(AlmostEqualFloat.almostEqual(context, 1.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testPerspectiveNull()
      throws ConstraintError
  {
    ProjectionMatrix.makePerspective(null, 1.0, 100.0, 1.0, Math.PI / 2);
  }
}
