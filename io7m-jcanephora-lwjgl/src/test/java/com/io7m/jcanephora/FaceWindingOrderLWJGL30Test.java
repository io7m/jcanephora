package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class FaceWindingOrderLWJGL30Test
{
  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.faceWindingOrderFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test public void testOrderBijection()
  {
    for (final FaceWindingOrder f : FaceWindingOrder.values()) {
      Assert.assertEquals(
        GLInterfaceLWJGL30.faceWindingOrderFromGL(GLInterfaceLWJGL30
          .faceWindingOrderToGL(f)),
        f);
    }
  }
}
