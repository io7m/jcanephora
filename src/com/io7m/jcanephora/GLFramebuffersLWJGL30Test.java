package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;

public class GLFramebuffersLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("GLFramebuffersLWJGL30", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

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
      g = GLInterfaceLWJGL30Util.getGL();
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
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
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

  @Test public void testCheckDepthBits()
    throws GLException
  {
    GLInterface g = null;
    Framebuffer fb = null;
    RenderbufferD24S8 rb = null;
    Texture2DRGBAStatic cb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
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
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    Assert.assertTrue(g.depthBufferGetBits() == 24);
    Assert.assertTrue(g.stencilBufferGetBits() == 8);
  }

  @Test(expected = ConstraintError.class) public void testDepthEnableNone()
    throws GLException,
      ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
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
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    g.depthBufferEnable(DepthFunction.DEPTH_LESS_THAN_OR_EQUAL);
  }
}
