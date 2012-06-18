package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class DepthFunctionJOGL30Test
{
  @Test public void testDepthBijection()
  {
    for (final DepthFunction f : DepthFunction.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .depthFunctionFromGL(GLInterfaceJOGL30.depthFunctionToGL(f)), f);
    }
  }

  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.depthFunctionFromGL(-1);
  }
}
