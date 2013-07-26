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

public final class LWJGL21DepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull GLDepthBuffer getGLDepthBuffer(
    @Nonnull final TestContext tc)
  {
    final Some<GLInterfaceGL2> some =
      (Some<GLInterfaceGL2>) tc.getGLImplementation().getGL2();
    return some.value;
  }

  @Override public @Nonnull GLFramebuffersCommon getGLFramebuffers(
    @Nonnull final TestContext tc)
  {
    final Some<GLInterfaceGL2> some =
      (Some<GLInterfaceGL2>) tc.getGLImplementation().getGL2();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGL21Supported();
  }

  @Override public @Nonnull FramebufferReference makeFramebufferWithDepth(
    @Nonnull final GLImplementation gi)
    throws ConstraintError,
      GLException
  {
    final Some<GLInterfaceGL2> some = (Some<GLInterfaceGL2>) gi.getGL2();
    final GLInterfaceGL2 g = some.value;
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
    @Nonnull final GLImplementation gi)
    throws ConstraintError,
      GLException
  {
    final Some<GLInterfaceGL2> some = (Some<GLInterfaceGL2>) gi.getGL2();
    final GLInterfaceGL2 g = some.value;
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
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL21_X();
  }
}
