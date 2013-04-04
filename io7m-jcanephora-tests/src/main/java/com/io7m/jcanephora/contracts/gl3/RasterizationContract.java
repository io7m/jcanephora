package com.io7m.jcanephora.contracts.gl3;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLPolygonModes;
import com.io7m.jcanephora.GLPolygonSmoothing;
import com.io7m.jcanephora.GLProgramPointSizeControl;
import com.io7m.jcanephora.GLRasterization;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.PolygonMode;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class RasterizationContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLRasterization getGLRasterization(
    TestContext tc);

  public abstract GLProgramPointSizeControl getGLProgramPointSizeControl(
    TestContext tc);

  public abstract GLPolygonSmoothing getGLPolygonSmoothing(
    TestContext tc);

  public abstract GLPolygonModes getGLPolygonModes(
    TestContext tc);

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
    final GLRasterization gl = this.getGLRasterization(tc);

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
    final GLPolygonModes gl = this.getGLPolygonModes(tc);

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
    final GLPolygonSmoothing gl = this.getGLPolygonSmoothing(tc);

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
    final GLProgramPointSizeControl gl =
      this.getGLProgramPointSizeControl(tc);

    gl.pointProgramSizeControlDisable();
    Assert.assertFalse(gl.pointProgramSizeControlIsEnabled());
    gl.pointProgramSizeControlEnable();
    Assert.assertTrue(gl.pointProgramSizeControlIsEnabled());
  }
}
