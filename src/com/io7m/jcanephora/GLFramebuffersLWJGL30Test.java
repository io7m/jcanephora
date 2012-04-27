package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

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

  @Test(expected = ConstraintError.class) public void testDepthEnableNone()
    throws GLException,
      ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
      fb = g.allocateFramebuffer();
      final Texture2DRGBAStatic cb =
        g.allocateTextureRGBAStatic(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);

      g
        .attachFramebufferStorage(
          fb,
          new FramebufferAttachment[] { new FramebufferAttachment.FramebufferColorAttachment(
            cb,
            0) });
      g.bindFramebuffer(fb);
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    g.enableDepthTest(DepthFunction.DEPTH_LESS_THAN_OR_EQUAL);
  }

  @Test(expected = ConstraintError.class) public
    void
    testAttachDepthMultiple()
      throws GLException,
        ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;
    RenderbufferDepth rb = null;
    Texture2DRGBAStatic cb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
      fb = g.allocateFramebuffer();
      rb = g.allocateRenderbufferDepth(128, 128);
      cb =
        g.allocateTextureRGBAStatic(
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

    g.attachFramebufferStorage(fb, new FramebufferAttachment[] {
      new FramebufferAttachment.FramebufferColorAttachment(cb, 0),
      new FramebufferAttachment.FramebufferDepthAttachment(rb),
      new FramebufferAttachment.FramebufferDepthAttachment(rb) });
  }

  @Test public void testCheckDepthBits()
    throws GLException
  {
    GLInterface g = null;
    Framebuffer fb = null;
    RenderbufferDepth rb = null;
    Texture2DRGBAStatic cb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
      fb = g.allocateFramebuffer();
      rb = g.allocateRenderbufferDepth(128, 128);
      cb =
        g.allocateTextureRGBAStatic(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);
      g.attachFramebufferStorage(fb, new FramebufferAttachment[] {
        new FramebufferAttachment.FramebufferColorAttachment(cb, 0),
        new FramebufferAttachment.FramebufferDepthAttachment(rb) });
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    Assert.assertTrue(g.getDepthBits() >= 1);
  }
}
