package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;

public abstract class CullContract implements GLTestContract
{
  /**
   * Enabling culling works.
   */

  @Test public void testCullingEnable()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

    for (final FaceSelection select : FaceSelection.values()) {
      for (final FaceWindingOrder order : FaceWindingOrder.values()) {
        gl.cullingDisable();
        Assert.assertFalse(gl.cullingIsEnabled());
        gl.cullingEnable(select, order);
        Assert.assertTrue(gl.cullingIsEnabled());
      }
    }
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = ConstraintError.class) public void testCullingNullFace()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.cullingEnable(null, FaceWindingOrder.FRONT_FACE_CLOCKWISE);
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = ConstraintError.class) public void testCullingNullOrder()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.cullingEnable(FaceSelection.FACE_BACK, null);
  }
}
