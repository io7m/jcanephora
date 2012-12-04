package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30DepthFunctionTest
{
  /**
   * ∀f. depthFunctionFromGL(depthFunctionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testDepthBijection()
  {
    for (final DepthFunction f : DepthFunction.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_LWJGL_ES2_Actual
        .depthFunctionFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .depthFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.depthFunctionFromGL(-1);
  }
}
