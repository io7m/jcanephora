package com.io7m.jcanephora.contracts_full;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface3;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.PolygonMode;
import com.io7m.jcanephora.TestContext;

public abstract class RasterizationContract implements GLTestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Test point size limits are sane.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testPointSize()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    Assert.assertTrue(gl.pointGetMinimumWidth() >= 0);
    Assert.assertTrue(gl.pointGetMaximumWidth() >= 1);
  }

  /**
   * Polygon mode setting works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testPolygonModeIdentities()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    gl.polygonSetMode(PolygonMode.POLYGON_FILL);
    Assert.assertEquals(PolygonMode.POLYGON_FILL, gl.polygonGetMode());

    for (final PolygonMode pm : PolygonMode.values()) {
      System.err.println("front: " + pm);
      gl.polygonSetMode(pm);
      Assert.assertEquals(pm, gl.polygonGetMode());
    }
  }

  /**
   * Enabling/disabling polygon smoothing works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testPolygonSmoothing()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    gl.polygonSmoothingDisable();
    Assert.assertFalse(gl.polygonSmoothingIsEnabled());
    gl.polygonSmoothingEnable();
    Assert.assertTrue(gl.polygonSmoothingIsEnabled());
  }

  /**
   * Enabling program point size control works.
   */

  @Test public final void testProgramSizeControl()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    gl.pointProgramSizeControlDisable();
    Assert.assertFalse(gl.pointProgramSizeControlIsEnabled());
    gl.pointProgramSizeControlEnable();
    Assert.assertTrue(gl.pointProgramSizeControlIsEnabled());
  }
}
