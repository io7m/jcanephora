package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class JOGL30LogicOperationTest
{
  /**
   * ∀o. logicOpFromGL(logicOpToGL(o)) == o.
   */

  @SuppressWarnings("static-method") @Test public void testLogicBijection()
  {
    for (final LogicOperation o : LogicOperation.values()) {
      Assert.assertEquals(
        GLInterfaceJOGL30.logicOpFromGL(GLInterfaceJOGL30.logicOpToGL(o)),
        o);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testLogicFailure()
  {
    GLInterfaceJOGL30.logicOpFromGL(-1);
  }
}
