package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.LogicOpContract;
import com.io7m.jlog.Log;

public final class LWJGL30LogicOperationTest extends LogicOpContract
{
  private Pbuffer buffer;

  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceLWJGL30(this.getLog());
  }

  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
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
