package com.io7m.jcanephora.contracts_full;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Type;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;

public abstract class RasterizationContract implements GLTestContract
{
  /**
   * All aliased line widths in the range [min .. max] inclusive are valid.
   */

  @Test public final void testLineAliasedWidthOK()
    throws GLException,
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
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
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    Assert.assertTrue(gl.pointGetMinimumWidth() >= 0);
    Assert.assertTrue(gl.pointGetMaximumWidth() >= 1);
  }

  /**
   * Enabling/disabling polygon smoothing works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testPolygonSmoothing()
    throws GLException,
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;

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
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;

    gl.pointProgramSizeControlDisable();
    Assert.assertFalse(gl.pointProgramSizeControlIsEnabled());
    gl.pointProgramSizeControlEnable();
    Assert.assertTrue(gl.pointProgramSizeControlIsEnabled());
  }
}