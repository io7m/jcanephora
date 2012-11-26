package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30BlendEquationEmbeddedTest
{
  /**
   * ∀f. blendEquationFromGL(blendEquationToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquationEmbedded f : BlendEquationEmbedded.values()) {
      Assert
        .assertEquals(
          GLInterfaceEmbedded_JOGL_ES2_Actual.blendEquationEmbeddedFromGL(GLInterfaceEmbedded_JOGL_ES2_Actual
            .blendEquationEmbeddedToGL(f)),
          f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_JOGL_ES2_Actual.blendEquationEmbeddedFromGL(-1);
  }
}
