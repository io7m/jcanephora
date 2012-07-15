package com.io7m.jcanephora;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;

public class FramebufferAttachmentLWJGL30Test
{
  private Pbuffer buffer;

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
  }

  @SuppressWarnings("static-method") @Test public void testFramebufferAttachColorOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
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

  @SuppressWarnings("static-method") @Test public void testFramebufferAttachDepthOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda });
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachDepthTwice()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda, fda });
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, null);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNullElement()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { null });
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.framebufferAttachStorage(null, null);
  }
}
