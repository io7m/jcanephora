package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class FaceWindingOrderLWJGL30Test
{
  @Test public void testOrderBijection()
  {
    for (final FaceWindingOrder f : FaceWindingOrder.values()) {
      Assert.assertEquals(
        GLInterfaceLWJGL30.faceWindingOrderFromGL(GLInterfaceLWJGL30
          .faceWindingOrderToGL(f)),
        f);
    }
  }
}
