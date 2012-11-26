package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30ScalarTypeTest
{
  /**
   * ∀t. scalarTypeFromGL(scalarTypeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public void testScalarBijection()
  {
    for (final GLScalarType t : GLScalarType.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_JOGL_ES2_Actual
        .scalarTypeFromGL(GLInterfaceEmbedded_JOGL_ES2_Actual.scalarTypeToGL(t)), t);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public
    void
    testScalarFailure()
  {
    GLInterfaceEmbedded_JOGL_ES2_Actual.scalarTypeFromGL(-1);
  }
}
