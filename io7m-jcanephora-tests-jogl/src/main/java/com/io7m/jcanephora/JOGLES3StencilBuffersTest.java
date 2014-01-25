/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import org.junit.Assert;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.StencilBuffersContract;

public final class JOGLES3StencilBuffersTest extends StencilBuffersContract
{
  @Override public JCGLFramebuffersCommon getGLFramebuffers(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES3(tc);
  }

  @Override public JCGLStencilBuffer getGLStencilBuffer(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES3Supported();
  }

  @Override public @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      final TestContext tc,
      @Nonnull final JCGLImplementation gi)
      throws ConstraintError,
        JCGLRuntimeException
  {
    final JCGLInterfaceGLES3 g = JOGLTestContextUtilities.getGLES3(tc);

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
    final TestContext tc,
    @Nonnull final JCGLImplementation gi)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final JCGLInterfaceGLES3 g = JOGLTestContextUtilities.getGLES3(tc);

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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL_ES3();
  }
}
