package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class LWJGL30UnsignedTypeTest
{
  /**
   * ∀t. unsignedTypeFromGL(unsignedTypeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnsignedBijection()
  {
    for (final GLUnsignedType u : GLUnsignedType.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .unsignedTypeFromGL(GLInterfaceLWJGL30.unsignedTypeToGL(u)), u);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testUnsignedFailure()
  {
    GLInterfaceLWJGL30.unsignedTypeFromGL(-1);
  }
}
