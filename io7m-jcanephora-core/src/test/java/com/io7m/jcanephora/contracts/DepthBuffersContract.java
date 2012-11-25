package com.io7m.jcanephora.contracts;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;

public abstract class DepthBuffersContract implements GLTestContract
{
  private static Framebuffer makeFramebuffer(
    final GLInterface g)
    throws GLException,
      ConstraintError
  {
    final RenderbufferD24S8 rb = g.renderbufferD24S8Allocate(128, 128);
    final Texture2DRGBAStatic cb =
      g.texture2DRGBAStaticAllocate(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g.framebufferAllocate(new FramebufferAttachment[] {
        new ColorAttachment(cb, 0),
        new RenderbufferD24S8Attachment(rb) });
    return fb;
  }

  private static Framebuffer makeFramebufferNoDepth(
    final GLInterface g)
    throws GLException,
      ConstraintError
  {
    final Texture2DRGBAStatic cb =
      g.texture2DRGBAStaticAllocate(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g
        .framebufferAllocate(new FramebufferAttachment[] { new ColorAttachment(
          cb,
          0) });
    return fb;
  }

  @Test public void testDepthBufferClearWorks()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);

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
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.framebufferUnbind();
    Assert.assertTrue(gl.depthBufferGetBits() >= 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthClearFails()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferBind(fb);
    gl.depthBufferClear(1.0f);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthEnableFails()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferBind(fb);
    gl.depthBufferEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferBind(fb);
    Assert.assertEquals(0, gl.depthBufferGetBits());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteDisableWithoutDepthFails()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferBind(fb);
    gl.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);

    gl.depthBufferWriteEnable();
    Assert.assertTrue(gl.depthBufferWriteIsEnabled());
    gl.depthBufferWriteDisable();
    Assert.assertFalse(gl.depthBufferWriteIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteEnableWithoutDepthFails()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Framebuffer fb = DepthBuffersContract.makeFramebufferNoDepth(gl);
    gl.framebufferBind(fb);
    gl.depthBufferWriteEnable();
  }
}
