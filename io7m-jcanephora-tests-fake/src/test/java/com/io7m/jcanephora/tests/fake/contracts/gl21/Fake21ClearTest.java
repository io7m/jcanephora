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

package com.io7m.jcanephora.tests.fake.contracts.gl21;

import org.junit.Assert;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLClearType;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.ClearContract;
import com.io7m.jcanephora.tests.fake.FakeShaderControl;
import com.io7m.jcanephora.tests.fake.FakeTestContext;
import com.io7m.jcanephora.tests.fake.FakeTestContextUtilities;
import com.io7m.junreachable.UnreachableCodeException;

public final class Fake21ClearTest extends ClearContract
{
  @Override public JCGLClearType getClear(
    final TestContext context)
  {
    return FakeTestContextUtilities.getGL2(context);
  }

  @Override public JCGLFramebuffersCommonType getFramebuffers(
    final TestContext context)
  {
    return FakeTestContextUtilities.getGL2(context);
  }

  @Override public boolean isGLSupported()
  {
    return true;
  }

  @Override public FramebufferType makeFramebuffer(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    try {
      final JCGLInterfaceGL2Type g = FakeTestContextUtilities.getGL2(tc);
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
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public FramebufferType makeFramebufferWithDepth(
    final TestContext context,
    final JCGLImplementationType gi)
  {
    try {
      final JCGLInterfaceGL2Type g = FakeTestContextUtilities.getGL2(context);
      Assert.assertFalse(g.framebufferDrawAnyIsBound());

      final JCGLFramebufferBuilderGL3ES3Type fbb =
        g.framebufferNewBuilderGL3ES3();

      final RenderbufferType<RenderableDepthKind> db =
        g.renderbufferAllocateDepth24(128, 128);
      final RenderbufferType<RenderableColorKind> cb =
        g.renderbufferAllocateRGBA8888(128, 128);

      fbb.attachColorRenderbuffer(cb);
      fbb.attachDepthRenderbuffer(db);
      final FramebufferType fb = g.framebufferAllocate(fbb);

      g.framebufferDrawBind(fb);
      Assert.assertTrue(g.framebufferDrawAnyIsBound());
      Assert.assertTrue(g.framebufferDrawIsBound(fb));

      g.framebufferDrawUnbind();
      Assert.assertFalse(g.framebufferDrawAnyIsBound());
      Assert.assertFalse(g.framebufferDrawIsBound(fb));

      return fb;
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public FramebufferType makeFramebufferWithDepthStencil(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException
  {
    try {
      final JCGLInterfaceGL2Type g = FakeTestContextUtilities.getGL2(tc);
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
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public TestContext newTestContext()
  {
    return FakeTestContext.makeContextWithOpenGL2_1(new FakeShaderControl());
  }
}
