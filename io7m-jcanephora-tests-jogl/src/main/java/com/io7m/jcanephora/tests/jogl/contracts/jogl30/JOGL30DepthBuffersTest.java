/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.jogl.contracts.jogl30;

import javax.annotation.Nonnull;

import org.junit.Assert;

import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.DepthBuffersContract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;

public final class JOGL30DepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull JCGLDepthBufferType getGLDepthBuffer(
    final @Nonnull TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public @Nonnull JCGLFramebuffersCommonType getGLFramebuffers(
    final @Nonnull TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL30Supported();
  }

  @Override public @Nonnull FramebufferType makeFramebufferWithDepth(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGL3Type g = JOGLTestContextUtilities.getGL3(tc);
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final FramebufferType fb = g.framebufferAllocate();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    final RenderbufferType<RenderableDepthStencilKind> db =
      g.renderbufferAllocateDepth24Stencil8(128, 128);
    final RenderbufferType<RenderableColorKind> cb =
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

  @Override public @Nonnull FramebufferType makeFramebufferWithoutDepth(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGL3Type g = JOGLTestContextUtilities.getGL3(tc);
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final FramebufferType fb = g.framebufferAllocate();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    final RenderbufferType<RenderableColorKind> cb =
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
  {
    return JOGLTestContext.makeContextWithOpenGL3_0();
  }
}
