package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30DepthFunctionTest
{
  /**
   * ∀f. depthFunctionFromGL(depthFunctionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testDepthBijection()
  {
    for (final DepthFunction f : DepthFunction.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .depthFunctionFromGL(GLInterfaceJOGL30.depthFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceJOGL30.depthFunctionFromGL(-1);
  }
}
