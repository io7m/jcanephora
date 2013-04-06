package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import org.junit.Assert;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jaux.functional.Option.Type;
import com.io7m.jcanephora.contracts.common.DepthBuffersContract;

public final class LWJGLES2DepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull GLDepthBuffer getGLDepthBuffer(
    @Nonnull final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public @Nonnull GLFramebuffersCommon getGLFramebuffers(
    @Nonnull final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithDepth(
    @Nonnull final GLImplementation gi)
    throws ConstraintError,
      GLException
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) gi.getGLES2();
    final GLInterfaceGLES2 g = some.value;
    final FramebufferReference fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final Option<GLExtensionPackedDepthStencil> e =
      g.extensionPackedDepthStencil().extensionGetSupport();
    if (e.type == Type.OPTION_SOME) {
      final GLExtensionPackedDepthStencil ex =
        ((Some<GLExtensionPackedDepthStencil>) e).value;
      final Renderbuffer<RenderableDepthStencil> db =
        ex.renderbufferAllocateDepth24Stencil8(128, 128);
      ex.framebufferDrawAttachDepthStencilRenderbuffer(fb, db);
    } else {
      final Renderbuffer<RenderableDepth> db =
        g.renderbufferAllocateDepth16(128, 128);
      g.framebufferDrawAttachDepthRenderbuffer(fb, db);
    }

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithoutDepth(
    @Nonnull final GLImplementation gi)
    throws ConstraintError,
      GLException
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) gi.getGLES2();
    final GLInterfaceGLES2 g = some.value;

    final FramebufferReference fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

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
    return LWJGLTestContext.makeContextWithOpenGL_ES2();
  }
}
