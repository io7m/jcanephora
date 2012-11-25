package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30BlendFunctionTest
{
  /**
   * ∀f. blendFunctionFromGL(blendFunctionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendFunction f : BlendFunction.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .blendFunctionFromGL(GLInterfaceJOGL30.blendFunctionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceJOGL30.blendFunctionFromGL(-1);
  }
}
