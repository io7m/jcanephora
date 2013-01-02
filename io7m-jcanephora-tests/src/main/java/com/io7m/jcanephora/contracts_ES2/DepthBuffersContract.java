package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;

public abstract class DepthBuffersContract implements GLES2TestContract
{
  private static FramebufferReference makeFramebuffer(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer db = g.renderbufferAllocateDepth16(128, 128);
    final Renderbuffer cb = g.renderbufferAllocateRGB565(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);
    g.framebufferDrawAttachDepthRenderbuffer(fb, db);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  private static FramebufferReference makeFramebufferNoDepth(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer cb = g.renderbufferAllocateRGB565(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testDepthBufferClearWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);

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
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    gl.framebufferDrawUnbind();
    Assert.assertTrue(gl.depthBufferGetBits() >= 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthClearFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb =
      DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferDrawBind(fb);
    gl.depthBufferClear(1.0f);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthEnableFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb =
      DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferDrawBind(fb);
    gl.depthBufferEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb =
      DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferDrawBind(fb);
    Assert.assertEquals(0, gl.depthBufferGetBits());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteDisableWithoutDepthFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb =
      DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferDrawBind(fb);
    gl.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);

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
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final FramebufferReference fb =
      DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferDrawBind(fb);
    gl.depthBufferWriteEnable();
  }
}
