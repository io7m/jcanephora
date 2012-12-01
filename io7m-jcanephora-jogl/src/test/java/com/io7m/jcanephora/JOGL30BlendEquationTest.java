package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30BlendEquationTest
{
  /**
   * ∀f. blendEquationFromGL(blendEquationToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquation f : BlendEquation.values()) {
      Assert.assertEquals(GLInterface_JOGL30
        .blendEquationFromGL(GLInterface_JOGL30.blendEquationToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterface_JOGL30.blendEquationFromGL(-1);
  }
}
