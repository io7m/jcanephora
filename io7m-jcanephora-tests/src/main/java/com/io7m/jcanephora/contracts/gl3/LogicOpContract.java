package com.io7m.jcanephora.contracts.gl3;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLLogic;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class LogicOpContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLLogic getGLLogic(
    TestContext tc);

  /**
   * Enabling/disabling logic operations works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public void testLogicOpsEnable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLLogic gl = this.getGLLogic(tc);

    for (final LogicOperation op : LogicOperation.values()) {
      gl.logicOperationsDisable();
      Assert.assertFalse(gl.logicOperationsEnabled());
      gl.logicOperationsEnable(op);
      Assert.assertTrue(gl.logicOperationsEnabled());
    }
  }

  /**
   * Trying to enable a null logic operation fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public void testLogicOpsNull()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLLogic gl = this.getGLLogic(tc);

    gl.logicOperationsEnable(null);
  }
}
