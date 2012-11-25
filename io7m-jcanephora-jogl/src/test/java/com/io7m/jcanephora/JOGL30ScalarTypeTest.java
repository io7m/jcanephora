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
      Assert.assertEquals(GLInterfaceJOGL30
        .scalarTypeFromGL(GLInterfaceJOGL30.scalarTypeToGL(t)), t);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public
    void
    testScalarFailure()
  {
    GLInterfaceJOGL30.scalarTypeFromGL(-1);
  }
}