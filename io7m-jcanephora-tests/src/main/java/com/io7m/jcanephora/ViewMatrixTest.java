package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.AlmostEqualFloat;
import com.io7m.jaux.AlmostEqualFloat.ContextRelative;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ViewMatrix.Context;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM3F;

public class ViewMatrixTest
{
  @SuppressWarnings({ "static-method" }) @Test public void testLookAt()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F(10.0f, 10.0f, -10.0f);
    final VectorM3F vt = new VectorM3F(10.0f, 10.0f, 0.0f);
    final VectorM3F vu = new VectorM3F(0.0f, 1.0f, 0.0f);
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    ViewMatrix.lookAt(m, vc, vt, vu);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 10.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(3, 3)));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testLookAtNull0()
      throws ConstraintError
  {
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(null, vc, vt, vu);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testLookAtNull1()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(m, null, vt, vu);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testLookAtNull2()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(m, vc, null, vu);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testLookAtNull3()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();

    ViewMatrix.lookAt(m, vc, vt, null);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testLookAtNull4()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAtWithContext(null, m, vc, vt, vu);
  }

  @SuppressWarnings({ "static-method" }) @Test public
    void
    testLookAtWithContext()
      throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F(10.0f, 10.0f, -10.0f);
    final VectorM3F vt = new VectorM3F(10.0f, 10.0f, 0.0f);
    final VectorM3F vu = new VectorM3F(0.0f, 1.0f, 0.0f);
    final Context context = new Context();
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    ViewMatrix.lookAtWithContext(context, m, vc, vt, vu);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 10.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(3, 3)));

    MatrixM4x4F.setIdentity(m);

    ViewMatrix.lookAtWithContext(context, m, vc, vt, vu);

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(0, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(0, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 10.0f, m.get(0, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(1, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(1, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(1, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(2, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -1.0f, m.get(2, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, -10.0f, m.get(2, 3)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, m.get(3, 2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, m.get(3, 3)));
  }
}
