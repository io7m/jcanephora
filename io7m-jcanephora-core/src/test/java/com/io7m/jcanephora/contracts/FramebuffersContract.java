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

public abstract class FramebuffersContract implements GLTestContract
{
  /**
   * Attaching multiple depth/stencil buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testAttachDepthMultiple()
      throws GLException,
        ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;
    RenderbufferD24S8 rb = null;
    Texture2DRGBAStatic cb = null;

    try {
      g = this.getGL();
      fb = g.framebufferAllocate();
      rb = g.renderbufferD24S8Allocate(128, 128);
      cb =
        g.texture2DRGBAStaticAllocate(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;

    g.framebufferAttachStorage(fb, new FramebufferAttachment[] {
      new ColorAttachment(cb, 0),
      new RenderbufferD24S8Attachment(rb),
      new RenderbufferD24S8Attachment(rb) });
  }

  /**
   * A framebuffer with a D24S8 buffer attached has 24 bits of depth precision
   * and 8 bits of stencil precision.
   */

  @Test public void testCheckDepthStencilBits()
  {
    GLInterface g = null;
    Framebuffer fb = null;
    RenderbufferD24S8 rb = null;
    Texture2DRGBAStatic cb = null;

    try {
      g = this.getGL();
      fb = g.framebufferAllocate();
      rb = g.renderbufferD24S8Allocate(128, 128);
      cb =
        g.texture2DRGBAStaticAllocate(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);
      g.framebufferAttachStorage(fb, new FramebufferAttachment[] {
        new ColorAttachment(cb, 0),
        new RenderbufferD24S8Attachment(rb) });
      g.framebufferBind(fb);
      Assert.assertTrue(g.depthBufferGetBits() == 24);
      Assert.assertTrue(g.stencilBufferGetBits() == 8);
      g.framebufferUnbind();
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Enabling depth-testing on a framebuffer without a depth buffer fails.
   */

  @Test(expected = ConstraintError.class) public void testDepthEnableNone()
    throws GLException,
      ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;

    try {
      g = this.getGL();
      fb = g.framebufferAllocate();
      final Texture2DRGBAStatic cb =
        g.texture2DRGBAStaticAllocate(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);

      g
        .framebufferAttachStorage(
          fb,
          new FramebufferAttachment[] { new FramebufferAttachment.ColorAttachment(
            cb,
            0) });
      g.framebufferBind(fb);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    g.depthBufferEnable(DepthFunction.DEPTH_LESS_THAN_OR_EQUAL);
  }

  /**
   * Attaching a single color buffer to a framebuffer works.
   */

  @Test public void testFramebufferAttachColorOK()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fbc });
  }

  /**
   * Attaching a single depth/stencil buffer to a framebuffer works.
   */

  @Test public void testFramebufferAttachDepthOK()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda });
  }

  /**
   * Attaching a two depth/stencil buffers to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachDepthTwice()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda, fda });
  }

  /**
   * Attaching nothing to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNone()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] {});
  }

  /**
   * Attaching a null list to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, null);
  }

  /**
   * Attaching a list with a null element fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNullElement()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { null });
  }

  /**
   * Attaching to a null framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.framebufferAttachStorage(null, null);
  }
}
