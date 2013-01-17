package com.io7m.jcanephora.contracts_ES2;

import javax.annotation.Nonnull;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public abstract class DepthBuffersContract implements GLES2TestContract
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

  @Test public void testDepthBufferClearWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final DepthFunction f : DepthFunction.values()) {
      gl.depthBufferDisable();
      Assert.assertFalse(gl.depthBufferIsEnabled());
      gl.depthBufferEnable(f);
      Assert.assertTrue(gl.depthBufferIsEnabled());
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
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    gl.framebufferDrawUnbind();
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    final int bits = gl.depthBufferGetBits();
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
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferClear(1.0f);
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
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gl.depthBufferGetBits());
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
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferWriteEnable();
    Assert.assertTrue(gl.depthBufferWriteIsEnabled());
    gl.depthBufferWriteDisable();
    Assert.assertFalse(gl.depthBufferWriteIsEnabled());
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
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gl.depthBufferWriteEnable();
  }
}
