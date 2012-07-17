package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public final class JOGL30BlendEquationTest
{
  /**
   * ∀f. blendEquationFromGL(blendEquationToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquation f : BlendEquation.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .blendEquationFromGL(GLInterfaceJOGL30.blendEquationToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.blendEquationFromGL(-1);
  }
}
