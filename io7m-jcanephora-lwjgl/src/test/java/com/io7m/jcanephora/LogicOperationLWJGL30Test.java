package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class LogicOperationLWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testLogicBijection()
  {
    for (final LogicOperation o : LogicOperation.values()) {
      Assert.assertEquals(
        GLInterfaceLWJGL30.logicOpFromGL(GLInterfaceLWJGL30.logicOpToGL(o)),
        o);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testLogicFailure()
  {
    GLInterfaceLWJGL30.logicOpFromGL(-1);
  }
}
