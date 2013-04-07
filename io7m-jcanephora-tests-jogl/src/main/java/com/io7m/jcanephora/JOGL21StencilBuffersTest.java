package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import org.junit.Assert;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.StencilBuffersContract;

public final class JOGL21StencilBuffersTest extends StencilBuffersContract
{
  @Override public GLFramebuffersCommon getGLFramebuffers(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public GLStencilBuffer getGLStencilBuffer(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL21WithExtensionsSupported();
  }

  @Override public @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      @Nonnull final GLImplementation gi)
      throws ConstraintError,
        GLException
  {
    final Some<GLInterfaceGL3> some = (Some<GLInterfaceGL3>) gi.getGL3();
    final GLInterfaceGL3 g = some.value;

    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithStencil(
    @Nonnull final GLImplementation gi)
    throws ConstraintError,
      GLException
  {
    final Some<GLInterfaceGL3> some = (Some<GLInterfaceGL3>) gi.getGL3();
    final GLInterfaceGL3 g = some.value;

    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer<RenderableDepthStencil> db =
      g.renderbufferAllocateDepth24Stencil8(128, 128);
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);
    g.framebufferDrawAttachDepthStencilRenderbuffer(fb, db);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL2_1();
  }
}
