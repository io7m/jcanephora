package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL21ScalarTypeTest
{
  /**
   * ∀t. scalarTypeFromGL(scalarTypeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public void testScalarBijection()
  {
    for (final GLScalarType t : GLScalarType.values()) {
      Assert.assertEquals(GLTypeConversions
        .scalarTypeFromGL(GLTypeConversions.scalarTypeToGL(t)), t);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public
    void
    testScalarFailure()
  {
    GLTypeConversions.scalarTypeFromGL(-1);
  }
}
