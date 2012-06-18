package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class BlendFunctionLWJGL30Test
{
  @Test public void testBijection()
  {
    for (final BlendFunction f : BlendFunction.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .blendFunctionFromGL(GLInterfaceLWJGL30.blendFunctionToGL(f)), f);
    }
  }

  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.blendFunctionFromGL(-1);
  }
}
