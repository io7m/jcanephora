package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class JOGL30UnsignedTypeTest
{
  /**
   * ∀t. unsignedTypeFromGL(unsignedTypeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testUnsignedBijection()
  {
    for (final GLUnsignedType u : GLUnsignedType.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .unsignedTypeFromGL(GLInterfaceJOGL30.unsignedTypeToGL(u)), u);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testUnsignedFailure()
  {
    GLInterfaceJOGL30.unsignedTypeFromGL(-1);
  }
}
