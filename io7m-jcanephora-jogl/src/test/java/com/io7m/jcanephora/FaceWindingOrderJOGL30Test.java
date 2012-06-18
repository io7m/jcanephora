package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class FaceWindingOrderJOGL30Test
{
  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.faceWindingOrderFromGL(-1);
  }

  @Test public void testOrderBijection()
  {
    for (final FaceWindingOrder f : FaceWindingOrder.values()) {
      Assert
        .assertEquals(
          GLInterfaceJOGL30.faceWindingOrderFromGL(GLInterfaceJOGL30
            .faceWindingOrderToGL(f)),
          f);
    }
  }
}
