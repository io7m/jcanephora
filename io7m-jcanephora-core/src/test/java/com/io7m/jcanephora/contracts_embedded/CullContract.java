package com.io7m.jcanephora.contracts_embedded;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;

public abstract class CullContract implements GLEmbeddedTestContract
{
  /**
   * Enabling culling works.
   */

  @Test public void testCullingEnable()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();

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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    gl.cullingEnable(null, FaceWindingOrder.FRONT_FACE_CLOCKWISE);
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = ConstraintError.class) public void testCullingNullOrder()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    gl.cullingEnable(FaceSelection.FACE_BACK, null);
  }
}
