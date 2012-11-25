package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30PrimitivesTest
{
  /**
   * ∀m. primitiveFromGL(primitiveToGL(p)) == p.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final Primitives p : Primitives.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .primitiveFromGL(GLInterfaceLWJGL30.primitiveToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.polygonModeFromGL(-1);
  }
}
