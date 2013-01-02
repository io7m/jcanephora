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
   * All aliased line widths in the range [min .. max] inclusive are valid.
   */

  @Test public final void testLineAliasedWidthOK()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int min = 0;
    int max = 0;

    try {
      gl.lineSmoothingDisable();
      min = gl.lineAliasedGetMinimumWidth();
      max = gl.lineAliasedGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    for (int index = min; index <= max; ++index) {
      gl.lineSetWidth(index);
    }
  }

  /**
   * Attempting to set too large a line width fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testLineAliasedWidthTooLarge()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int max = 99999;

    try {
      gl.lineSmoothingDisable();
      max = gl.lineAliasedGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(max + 1);
  }

  /**
   * Attempting to set too small a line width fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testLineAliasedWidthTooSmall()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int min = 0;

    try {
      gl.lineSmoothingDisable();
      min = gl.lineAliasedGetMinimumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(min - 1);
  }

  /**
   * All smoothed line widths in the range [min .. max] inclusive are valid.
   */

  @Test public final void testLineSmoothWidthOK()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int min = 0;
    int max = 0;

    try {
      gl.lineSmoothingEnable();
      min = gl.lineSmoothGetMinimumWidth();
      max = gl.lineSmoothGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    for (int index = min; index <= max; ++index) {
      gl.lineSetWidth(index);
    }
  }

  /**
   * Attempting to set too large a line width fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testLineSmoothWidthTooLarge()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int max = 99999;

    try {
      gl.lineSmoothingEnable();
      max = gl.lineSmoothGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(max + 1);
  }

  /**
   * Attempting to set too small a line width fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testLineSmoothWidthTooSmall()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    int min = 0;

    try {
      gl.lineSmoothingEnable();
      min = gl.lineSmoothGetMinimumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(min - 1);
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
