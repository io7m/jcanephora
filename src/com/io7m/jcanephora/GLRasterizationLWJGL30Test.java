package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLRasterizationLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("Meta", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void testLineAliasedWidthOK()
    throws GLException,
      IOException,
      ConstraintError
  {
    GLInterface gl = null;
    int min = 0;
    int max = 0;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
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

  @Test(expected = ConstraintError.class) public
    void
    testLineAliasedWidthTooLarge()
      throws GLException,
        IOException,
        ConstraintError
  {
    GLInterface gl = null;
    int max = 99999;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      gl.lineSmoothingDisable();
      max = gl.lineAliasedGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(max + 1);
  }

  @Test(expected = ConstraintError.class) public
    void
    testLineAliasedWidthTooSmall()
      throws GLException,
        IOException,
        ConstraintError
  {
    GLInterface gl = null;
    int min = 0;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      gl.lineSmoothingDisable();
      min = gl.lineAliasedGetMinimumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(min - 1);
  }

  @Test public void testLineSmoothWidthOK()
    throws GLException,
      IOException,
      ConstraintError
  {
    GLInterface gl = null;
    int min = 0;
    int max = 0;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
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

  @Test(expected = ConstraintError.class) public
    void
    testLineSmoothWidthTooLarge()
      throws GLException,
        IOException,
        ConstraintError
  {
    GLInterface gl = null;
    int max = 99999;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      gl.lineSmoothingEnable();
      max = gl.lineSmoothGetMaximumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(max + 1);
  }

  @Test(expected = ConstraintError.class) public
    void
    testLineSmoothWidthTooSmall()
      throws GLException,
        IOException,
        ConstraintError
  {
    GLInterface gl = null;
    int min = 0;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      gl.lineSmoothingEnable();
      min = gl.lineSmoothGetMinimumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(min - 1);
  }
}
