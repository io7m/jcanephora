package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.contracts_full.LogicOpContract;
import com.io7m.jlog.Log;

public final class LWJGL30LogicOperationTest extends LogicOpContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGL3Supported();
  }

  @Override public GLImplementation makeNewGLImplementation()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      GLUnsupportedException
  {
    return LWJGLTestDisplay.makeImplementationWithOpenGL3();
  }

  /**
   * ∀o. logicOpFromGL(logicOpToGL(o)) == o.
   */

  @SuppressWarnings("static-method") @Test public void testLogicBijection()
  {
    for (final LogicOperation o : LogicOperation.values()) {
      Assert.assertEquals(
        GLTypeConversions.logicOpFromGL(GLTypeConversions.logicOpToGL(o)),
        o);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testLogicFailure()
  {
    GLTypeConversions.logicOpFromGL(-1);
  }
}
