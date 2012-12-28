package com.io7m.jcanephora.contracts_full;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.LogicOperation;

public abstract class LogicOpContract implements GLTestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Enabling/disabling logic operations works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public void testLogicOpsEnable()
    throws GLException,
      ConstraintError
  {

    final GLInterface gl = this.makeNewGL();

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
      ConstraintError
  {

    final GLInterface gl = this.makeNewGL();

    gl.logicOperationsEnable(null);
  }
}
