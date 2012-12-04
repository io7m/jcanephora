package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30FaceWindingOrderTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.faceWindingOrderFromGL(-1);
  }

  /**
   * ∀f. faceWindingOrderFromGL(faceWindingOrderToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testOrderBijection()
  {
    for (final FaceWindingOrder f : FaceWindingOrder.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_LWJGL_ES2_Actual
        .faceWindingOrderFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .faceWindingOrderToGL(f)), f);
    }
  }
}
