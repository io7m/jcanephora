package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class JOGL30PrimitivesTest
{
  /**
   * ∀m. primitiveFromGL(primitiveToGL(p)) == p.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final Primitives p : Primitives.values()) {
      Assert
        .assertEquals(GLInterfaceJOGL30.primitiveFromGL(GLInterfaceJOGL30
          .primitiveToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.polygonModeFromGL(-1);
  }
}
