package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGLES2PrimitivesTest
{
  /**
   * ∀m. primitiveFromGL(primitiveToGL(p)) == p.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final Primitives p : Primitives.values()) {
      Assert
        .assertEquals(GLTypeConversions.primitiveFromGL(GLTypeConversions
          .primitiveToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLTypeConversions.polygonModeFromGL(-1);
  }
}
