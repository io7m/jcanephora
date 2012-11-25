package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30BlendEquationTest
{
  /**
   * ∀f. blendEquationFromGL(blendEquationToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquation f : BlendEquation.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .blendEquationFromGL(GLInterfaceLWJGL30.blendEquationToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.blendEquationFromGL(-1);
  }
}
