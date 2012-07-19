package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.LogicOpContract;
import com.io7m.jlog.Log;

public final class JOGL30LogicOperationTest extends LogicOpContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGL();
  }

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
