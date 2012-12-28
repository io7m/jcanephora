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
      Assert.assertEquals(GLTypeConversions
        .depthFunctionFromGL(GLTypeConversions.depthFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLTypeConversions.depthFunctionFromGL(-1);
  }
}
