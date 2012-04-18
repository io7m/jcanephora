package com.io7m.jcanephora;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.FramebufferColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.FramebufferDepthAttachment;

public class FramebufferAttachmentLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("FramebufferAttachment", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void testFramebufferAttachColorOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final Texture2DRGBAStatic t =
      gl.allocateTextureRGBAStatic(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final FramebufferColorAttachment fbc =
      new FramebufferColorAttachment(t, 0);
    final Framebuffer fb = gl.allocateFramebuffer();
    gl.attachFramebufferStorage(fb, new FramebufferAttachment[] { fbc });
  }

  @Test public void testFramebufferAttachDepthOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final RenderbufferDepth depth = gl.allocateRenderbufferDepth(128, 128);
    final FramebufferDepthAttachment fda =
      new FramebufferDepthAttachment(depth);
    final Framebuffer fb = gl.allocateFramebuffer();
    gl.attachFramebufferStorage(fb, new FramebufferAttachment[] { fda });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachDepthTwice()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final RenderbufferDepth depth = gl.allocateRenderbufferDepth(128, 128);
    final FramebufferDepthAttachment fda =
      new FramebufferDepthAttachment(depth);
    final Framebuffer fb = gl.allocateFramebuffer();
    gl.attachFramebufferStorage(fb, new FramebufferAttachment[] { fda, fda });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final Framebuffer fb = gl.allocateFramebuffer();
    gl.attachFramebufferStorage(fb, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNullElement()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final Framebuffer fb = gl.allocateFramebuffer();
    gl.attachFramebufferStorage(fb, new FramebufferAttachment[] { null });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.attachFramebufferStorage(null, null);
  }
}
