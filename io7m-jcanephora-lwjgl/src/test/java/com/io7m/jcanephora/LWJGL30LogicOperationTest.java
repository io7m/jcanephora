package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.LogicOpContract;
import com.io7m.jlog.Log;

public final class LWJGL30LogicOperationTest extends LogicOpContract
{
  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return LWJGL30TestDisplay.getGL();
  }

  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }

  /**
   * ∀o. logicOpFromGL(logicOpToGL(o)) == o.
   */

  @SuppressWarnings("static-method") @Test public void testLogicBijection()
  {
    for (final LogicOperation o : LogicOperation.values()) {
      Assert.assertEquals(
        GLInterfaceLWJGL30.logicOpFromGL(GLInterfaceLWJGL30.logicOpToGL(o)),
        o);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testLogicFailure()
  {
    GLInterfaceLWJGL30.logicOpFromGL(-1);
  }
}
