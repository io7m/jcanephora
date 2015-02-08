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

package com.io7m.jcanephora.tests.jogl.contracts.jogl33;

import org.junit.Assert;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLDepthClampingType;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gl3.DepthClampContract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;

public final class JOGL33DepthClampTest extends DepthClampContract
{
  @Override public JCGLDepthClampingType getGLDepthClamping(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL33Supported();
  }

  @Override public FramebufferType makeFramebufferWithDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGL3Type g = JOGLTestContextUtilities.getGL3(tc);
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      g.framebufferNewBuilderGL3ES3();

    final RenderbufferType<RenderableDepthStencilKind> db =
      g.renderbufferAllocateDepth24Stencil8(128, 128);
    final RenderbufferType<RenderableColorKind> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    fbb.attachColorRenderbuffer(cb);
    fbb.attachDepthStencilRenderbuffer(db);
    final FramebufferType fb = g.framebufferAllocate(fbb);

    g.framebufferDrawBind(fb);
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferDrawIsBound(fb));

    g.framebufferDrawUnbind();
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    return fb;
  }

  @Override public FramebufferType makeFramebufferWithoutDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGL3Type g = JOGLTestContextUtilities.getGL3(tc);
    Assert.assertFalse(g.framebufferDrawAnyIsBound());

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      g.framebufferNewBuilderGL3ES3();
    final RenderbufferType<RenderableColorKind> cb =
      g.renderbufferAllocateRGBA8888(128, 128);

    fbb.attachColorRenderbuffer(cb);
    final FramebufferType fb = g.framebufferAllocate(fbb);
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferDrawIsBound(fb));

    return fb;
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL3_p(false);
  }
}
