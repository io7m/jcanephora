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

package com.io7m.jcanephora.tests.jogl.contracts.jogles3;

import org.junit.Assert;

import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceGLES3Type;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.StencilBuffersContract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;
import com.io7m.junreachable.UnreachableCodeException;

public final class JOGLES3StencilBuffersTest extends StencilBuffersContract
{
  @Override public JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES3(tc);
  }

  @Override public JCGLStencilBufferType getGLStencilBuffer(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES3Supported();
  }

  @Override public FramebufferType makeFramebufferWithoutStencil(
    final TestContext tc,
    final JCGLImplementationType gi)
  {
    try {
      final JCGLInterfaceGLES3Type g = JOGLTestContextUtilities.getGLES3(tc);

      final FramebufferType fb = g.framebufferAllocate();
      final RenderbufferType<RenderableColorKind> cb =
        g.renderbufferAllocateRGBA8888(128, 128);

      g.framebufferDrawBind(fb);
      g.framebufferDrawAttachColorRenderbuffer(fb, cb);

      final FramebufferStatus expect =
        FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
      final FramebufferStatus status = g.framebufferDrawValidate(fb);
      Assert.assertEquals(expect, status);

      g.framebufferDrawUnbind();
      return fb;
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public FramebufferType makeFramebufferWithStencil(
    final TestContext tc,
    final JCGLImplementationType gi)
  {
    try {
      final JCGLInterfaceGLES3Type g = JOGLTestContextUtilities.getGLES3(tc);

      final FramebufferType fb = g.framebufferAllocate();
      final RenderbufferType<RenderableDepthStencilKind> db =
        g.renderbufferAllocateDepth24Stencil8(128, 128);
      final RenderbufferType<RenderableColorKind> cb =
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
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL_ES3();
  }
}