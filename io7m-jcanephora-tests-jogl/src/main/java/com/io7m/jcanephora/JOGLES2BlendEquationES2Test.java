package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGLES2BlendEquationES2Test
{
  /**
   * ∀f. blendEquationFromGL(blendEquationToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquationES2 f : BlendEquationES2.values()) {
      Assert
        .assertEquals(
          GLTypeConversions.blendEquationES2FromGL(GLTypeConversions
            .blendEquationES2ToGL(f)),
          f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLTypeConversions.blendEquationES2FromGL(-1);
  }
}
