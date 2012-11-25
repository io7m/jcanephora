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
      Assert.assertEquals(GLInterfaceLWJGL30
        .blendFunctionFromGL(GLInterfaceLWJGL30.blendFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.blendFunctionFromGL(-1);
  }
}
