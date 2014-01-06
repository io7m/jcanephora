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
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jaux.functional.Option.Type;
import com.io7m.jcanephora.contracts.DepthBuffersContract;

public final class LWJGLES2DepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull JCGLDepthBuffer getGLDepthBuffer(
    @Nonnull final TestContext tc)
  {
    final Some<JCGLInterfaceGLES2> some =
      (Some<JCGLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public @Nonnull JCGLFramebuffersCommon getGLFramebuffers(
    @Nonnull final TestContext tc)
  {
    final Some<JCGLInterfaceGLES2> some =
      (Some<JCGLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithDepth(
    @Nonnull final JCGLImplementation gi)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final Some<JCGLInterfaceGLES2> some =
      (Some<JCGLInterfaceGLES2>) gi.getGLES2();
    final JCGLInterfaceGLES2 g = some.value;
    final FramebufferReference fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final Option<JCGLExtensionPackedDepthStencil> e =
      g.extensionPackedDepthStencil();
    if (e.type == Type.OPTION_SOME) {
      final JCGLExtensionPackedDepthStencil ex =
        ((Some<JCGLExtensionPackedDepthStencil>) e).value;
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
    @Nonnull final JCGLImplementation gi)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final Some<JCGLInterfaceGLES2> some =
      (Some<JCGLInterfaceGLES2>) gi.getGLES2();
    final JCGLInterfaceGLES2 g = some.value;

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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL_ES2();
  }
}
