package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.LogicOpContract;
import com.io7m.jlog.Log;

public final class JOGL30LogicOperationTest extends LogicOpContract
{
  private GLContext context;

  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceJOGL30(this.context, JOGL30TestLog.getLog());
  }

  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Before public void setUp()
    throws Exception
  {
    this.context = JOGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    JOGL30.destroyDisplay(this.context);
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
