package com.io7m.jcanephora;

import java.io.IOException;

import javax.media.opengl.GLContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;

public class FramebufferAttachmentJOGL30Test
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

  @Test public void testFramebufferAttachColorOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
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

  @Test public void testFramebufferAttachDepthOK()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachDepthTwice()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    final RenderbufferD24S8 depth = gl.renderbufferD24S8Allocate(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(depth);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { fda, fda });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNullElement()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    final Framebuffer fb = gl.framebufferAllocate();
    gl.framebufferAttachStorage(fb, new FramebufferAttachment[] { null });
  }

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachNull()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    gl.framebufferAttachStorage(null, null);
  }
}
