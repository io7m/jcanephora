package com.io7m.jcanephora.contracts.common;

import javax.annotation.Nonnull;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.GLDepthBuffer;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLFramebuffersCommon;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public abstract class DepthBuffersContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull FramebufferReference makeFramebufferWithDepth(
    final @Nonnull GLImplementation gi)
    throws ConstraintError,
      GLException;

  public abstract @Nonnull FramebufferReference makeFramebufferWithoutDepth(
    final @Nonnull GLImplementation gi)
    throws ConstraintError,
      GLException;

  public abstract @Nonnull GLFramebuffersCommon getGLFramebuffers(
    final @Nonnull TestContext tc);

  public abstract @Nonnull GLDepthBuffer getGLDepthBuffer(
    final @Nonnull TestContext tc);

  @Test public void testDepthBufferClearWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final DepthFunction f : DepthFunction.values()) {
      gd.depthBufferDisable();
      Assert.assertFalse(gd.depthBufferIsEnabled());
      gd.depthBufferEnable(f);
      Assert.assertTrue(gd.depthBufferIsEnabled());
    }
  }

  /**
   * Attempting to query an unbound framebuffer still works (the default
   * framebuffer is queried instead).
   */

  @Test public void testDepthBufferWithoutBoundFramebufferWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    gl.framebufferDrawUnbind();
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    final int bits = gd.depthBufferGetBits();
    Assert.assertTrue(bits >= 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthClearFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthEnableFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gd.depthBufferGetBits());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteDisableWithoutDepthFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteEnable();
    Assert.assertTrue(gd.depthBufferWriteIsEnabled());
    gd.depthBufferWriteDisable();
    Assert.assertFalse(gd.depthBufferWriteIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteEnableWithoutDepthFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteEnable();
  }
}
