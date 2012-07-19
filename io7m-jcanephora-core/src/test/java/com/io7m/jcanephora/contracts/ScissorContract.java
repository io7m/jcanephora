package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jtensors.VectorI2I;

public abstract class ScissorContract implements GLTestContract
{
  @Test public void testScissorEnableWorks()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();

    gl.scissorDisable();
    Assert.assertFalse(gl.scissorIsEnabled());
    gl.scissorEnable(new VectorI2I(8, 8), new VectorI2I(8, 8));
    Assert.assertTrue(gl.scissorIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullDimensions()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.scissorEnable(new VectorI2I(8, 8), null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullPosition()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.scissorEnable(null, new VectorI2I(8, 8));
  }
}
