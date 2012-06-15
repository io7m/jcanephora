package com.io7m.jcanephora;

import java.io.IOException;

import javax.media.opengl.GLContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLRasterizationJOGL30Test
{
  private GLContext context;

  @Before public void setUp()
    throws Exception
  {
    this.context = JOGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    JOGL30.destroyDisplay(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
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
      gl = GLInterfaceJOGL30Util.getGL(this.context);
      gl.lineSmoothingEnable();
      min = gl.lineSmoothGetMinimumWidth();
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    gl.lineSetWidth(min - 1);
  }
}
