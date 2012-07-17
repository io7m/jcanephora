package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class JOGL30FaceWindingOrderTest
{
  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.faceWindingOrderFromGL(-1);
  }

  /**
   * ∀f. faceWindingOrderFromGL(faceWindingOrderToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testOrderBijection()
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
