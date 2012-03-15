package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM3F;

public class ViewMatrixTest
{
  @SuppressWarnings("boxing") @Test public void testLookAt()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F(10.0f, 10.0f, -10.0f);
    final VectorM3F vt = new VectorM3F(10.0f, 10.0f, 0.0f);
    final VectorM3F vu = new VectorM3F(0.0f, 1.0f, 0.0f);

    ViewMatrix.lookAt(m, vc, vt, vu);

    Assert.assertEquals(-1.0f, m.get(0, 0));
    Assert.assertEquals(0.0f, m.get(0, 1));
    Assert.assertEquals(0.0f, m.get(0, 2));
    Assert.assertEquals(10.0f, m.get(0, 3));

    Assert.assertEquals(0.0f, m.get(1, 0));
    Assert.assertEquals(1.0f, m.get(1, 1));
    Assert.assertEquals(0.0f, m.get(1, 2));
    Assert.assertEquals(-10.0f, m.get(1, 3));

    Assert.assertEquals(0.0f, m.get(2, 0));
    Assert.assertEquals(0.0f, m.get(2, 1));
    Assert.assertEquals(-1.0f, m.get(2, 2));
    Assert.assertEquals(-10.0f, m.get(2, 3));

    Assert.assertEquals(0.0f, m.get(3, 0));
    Assert.assertEquals(0.0f, m.get(3, 1));
    Assert.assertEquals(0.0f, m.get(3, 2));
    Assert.assertEquals(1.0f, m.get(3, 3));
  }

  @Test(expected = ConstraintError.class) public void testLookAtNull0()
    throws ConstraintError
  {
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(null, vc, vt, vu);
  }

  @Test(expected = ConstraintError.class) public void testLookAtNull1()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(m, null, vt, vu);
  }

  @Test(expected = ConstraintError.class) public void testLookAtNull2()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(m, vc, null, vu);
  }

  @Test(expected = ConstraintError.class) public void testLookAtNull3()
    throws ConstraintError
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();

    ViewMatrix.lookAt(m, vc, vt, null);
  }
}
