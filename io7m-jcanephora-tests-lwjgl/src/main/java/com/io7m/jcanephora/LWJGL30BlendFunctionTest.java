package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30BlendFunctionTest
{
  /**
   * ∀f. blendFunctionFromGL(blendFunctionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendFunction f : BlendFunction.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendFunctionFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .blendFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.blendFunctionFromGL(-1);
  }
}
