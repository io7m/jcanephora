package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGLES2UnsignedTypeTest
{
  /**
   * ∀t. unsignedTypeFromGL(unsignedTypeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnsignedBijection()
  {
    for (final GLUnsignedType u : GLUnsignedType.values()) {
      Assert.assertEquals(GLTypeConversions
        .unsignedTypeFromGL(GLTypeConversions.unsignedTypeToGL(u)), u);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public
    void
    testUnsignedFailure()
  {
    GLTypeConversions.unsignedTypeFromGL(-1);
  }
}