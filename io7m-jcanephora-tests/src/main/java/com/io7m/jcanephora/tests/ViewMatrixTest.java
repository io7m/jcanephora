/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.AlmostEqualFloat;
import com.io7m.jaux.AlmostEqualFloat.ContextRelative;
import com.io7m.jcanephora.ViewMatrix;
import com.io7m.jcanephora.ViewMatrix.Context;
import com.io7m.jnull.NullCheckException;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3FType;

@SuppressWarnings({ "static-method" }) public class ViewMatrixTest
{
  @Test public void testLookAt()
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

  @Test(expected = NullCheckException.class) public void testLookAtNull0()
  {
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt((MatrixM4x4F) TestUtilities.actuallyNull(), vc, vt, vu);
  }

  @Test(expected = NullCheckException.class) public void testLookAtNull1()
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(
      m,
      (VectorReadable3FType) TestUtilities.actuallyNull(),
      vt,
      vu);
  }

  @Test(expected = NullCheckException.class) public void testLookAtNull2()
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAt(
      m,
      vc,
      (VectorReadable3FType) TestUtilities.actuallyNull(),
      vu);
  }

  @Test(expected = NullCheckException.class) public void testLookAtNull3()
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();

    ViewMatrix.lookAt(
      m,
      vc,
      vt,
      (VectorReadable3FType) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public void testLookAtNull4()
  {
    final MatrixM4x4F m = new MatrixM4x4F();
    final VectorM3F vc = new VectorM3F();
    final VectorM3F vt = new VectorM3F();
    final VectorM3F vu = new VectorM3F();

    ViewMatrix.lookAtWithContext(
      (Context) TestUtilities.actuallyNull(),
      m,
      vc,
      vt,
      vu);
  }

  @Test public void testLookAtWithContext()
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
