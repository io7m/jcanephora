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

package com.io7m.jcanephora.tests.fake.contracts.gles2;

import org.junit.Assert;

import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLExtensionPackedDepthStencilType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.DepthBuffersContract;
import com.io7m.jcanephora.tests.fake.FakeShaderControl;
import com.io7m.jcanephora.tests.fake.FakeTestContext;
import com.io7m.jcanephora.tests.fake.FakeTestContextUtilities;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;

public final class FakeES2DepthBuffersTest extends DepthBuffersContract
{
  @Override public JCGLDepthBufferType getGLDepthBuffer(
    final TestContext tc)
  {
    return FakeTestContextUtilities.getGLES2(tc);
  }

  @Override public JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc)
  {
    return FakeTestContextUtilities.getGLES2(tc);
  }

  @Override public boolean isGLSupported()
  {
    return true;
  }

  @Override public FramebufferType makeFramebufferWithDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGLES2Type g = FakeTestContextUtilities.getGLES2(tc);
    final FramebufferType fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final RenderbufferType<RenderableColorKind> cb =
      g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final OptionType<JCGLExtensionPackedDepthStencilType> e =
      g.extensionPackedDepthStencil();

    if (e.isSome()) {
      final JCGLExtensionPackedDepthStencilType ex =
        ((Some<JCGLExtensionPackedDepthStencilType>) e).get();
      final RenderbufferType<RenderableDepthStencilKind> db =
        ex.renderbufferAllocateDepth24Stencil8(128, 128);
      ex.framebufferDrawAttachDepthStencilRenderbuffer(fb, db);
    } else {
      final RenderbufferType<RenderableDepthKind> db =
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

  @Override public FramebufferType makeFramebufferWithoutDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    final JCGLInterfaceGLES2Type g = FakeTestContextUtilities.getGLES2(tc);

    final FramebufferType fb = g.framebufferAllocate();

    g.framebufferDrawBind(fb);
    final RenderbufferType<RenderableColorKind> cb =
      g.renderbufferAllocateRGBA4444(128, 128);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Override public TestContext newTestContext()
  {
    return FakeTestContext.makeContextWithOpenGL_ES2(new FakeShaderControl());
  }
}
