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
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.DepthBuffersContract;

public final class LWJGL3xDepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull JCGLDepthBuffer getGLDepthBuffer(
    @Nonnull final TestContext tc)
  {
    final Some<JCGLInterfaceGL3> some =
      (Some<JCGLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public @Nonnull JCGLFramebuffersCommon getGLFramebuffers(
    @Nonnull final TestContext tc)
  {
    final Some<JCGLInterfaceGL3> some =
      (Some<JCGLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGL3xSupported();
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithDepth(
    @Nonnull final JCGLImplementation gi)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final Some<JCGLInterfaceGL3> some = (Some<JCGLInterfaceGL3>) gi.getGL3();
    final JCGLInterfaceGL3 g = some.value;
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final FramebufferReference fb = g.framebufferAllocate();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    final Renderbuffer<RenderableDepthStencil> db =
      g.renderbufferAllocateDepth24Stencil8(128, 128);
    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    g.framebufferDrawBind(fb);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    g.framebufferDrawAttachColorRenderbuffer(fb, cb);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    g.framebufferDrawAttachDepthStencilRenderbuffer(fb, db);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    return fb;
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithoutDepth(
    @Nonnull final JCGLImplementation gi)
    throws ConstraintError,
      JCGLRuntimeException
  {
    final Some<JCGLInterfaceGL3> some = (Some<JCGLInterfaceGL3>) gi.getGL3();
    final JCGLInterfaceGL3 g = some.value;
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final FramebufferReference fb = g.framebufferAllocate();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    final Renderbuffer<RenderableColor> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    g.framebufferDrawBind(fb);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    g.framebufferDrawAttachColorRenderbuffer(fb, cb);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    return fb;
  }

  @Override public @Nonnull TestContext newTestContext()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL3_X();
  }
}
