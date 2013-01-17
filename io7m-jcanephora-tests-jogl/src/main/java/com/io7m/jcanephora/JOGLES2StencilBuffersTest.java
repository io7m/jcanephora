package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import org.junit.Assert;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jaux.functional.Option.Type;
import com.io7m.jcanephora.contracts_ES2.StencilBuffersContract;

public final class JOGLES2StencilBuffersTest extends StencilBuffersContract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      @Nonnull final GLImplementation gi)
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 g = gi.implementationGetGLES2();
    final FramebufferReference fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final Renderbuffer cb = g.renderbufferAllocateRGBA4444(128, 128);
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
    final GLInterfaceES2 g = gi.implementationGetGLES2();
    final FramebufferReference fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final Renderbuffer cb = g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final Option<GLExtensionPackedDepthStencil> e =
      g.extensionPackedDepthStencil().extensionGetSupport();

    if (e.type == Type.OPTION_SOME) {
      final GLExtensionPackedDepthStencil ex =
        ((Some<GLExtensionPackedDepthStencil>) e).value;
      final Renderbuffer db =
        ex.renderbufferAllocateDepth24Stencil8(128, 128);
      ex.framebufferDrawAttachDepthStencilRenderbuffer(fb, db);
    } else {
      final Renderbuffer db = g.renderbufferAllocateStencil8(128, 128);
      g.framebufferDrawAttachStencilRenderbuffer(fb, db);
    }

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
    return JOGLTestContext.makeContextWithOpenGL_ES2();
  }
}