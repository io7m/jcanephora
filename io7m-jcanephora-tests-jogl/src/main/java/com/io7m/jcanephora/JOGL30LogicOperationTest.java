package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gl3.LogicOpContract;

public final class JOGL30LogicOperationTest extends LogicOpContract
{
  @Override public GLLogic getGLLogic(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL3Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL3_X();
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
