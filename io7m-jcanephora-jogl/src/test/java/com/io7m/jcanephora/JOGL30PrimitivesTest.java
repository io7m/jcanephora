package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30PrimitivesTest
{
  /**
   * ∀m. primitiveFromGL(primitiveToGL(p)) == p.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final Primitives p : Primitives.values()) {
      Assert.assertEquals(
        GLInterfaceEmbedded_JOGL_ES2_Actual
          .primitiveFromGL(GLInterfaceEmbedded_JOGL_ES2_Actual
            .primitiveToGL(p)),
        p);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterface_JOGL30.polygonModeFromGL(-1);
  }
}
