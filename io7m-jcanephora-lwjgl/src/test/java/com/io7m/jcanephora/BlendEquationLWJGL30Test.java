package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class BlendEquationLWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquation f : BlendEquation.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .blendEquationFromGL(GLInterfaceLWJGL30.blendEquationToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.blendEquationFromGL(-1);
  }
}
