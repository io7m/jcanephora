package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class DepthFunctionLWJGL30Test
{
  @Test public void testDepthBijection()
  {
    for (final DepthFunction f : DepthFunction.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .depthFunctionFromGL(GLInterfaceLWJGL30.depthFunctionToGL(f)), f);
    }
  }

  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.depthFunctionFromGL(-1);
  }
}
