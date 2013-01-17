package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.StencilBuffersContract;

public final class JOGL30StencilBuffersTest extends StencilBuffersContract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL3Supported();
  }

  @Override public @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      @Nonnull final GLImplementation gi)
      throws ConstraintError,
        GLException
  {
    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterface3 g = gi.implementationGetGL3();
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
    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterface3 g = gi.implementationGetGL3();
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
    return JOGLTestContext.makeContextWithOpenGL3_X();
  }
}
