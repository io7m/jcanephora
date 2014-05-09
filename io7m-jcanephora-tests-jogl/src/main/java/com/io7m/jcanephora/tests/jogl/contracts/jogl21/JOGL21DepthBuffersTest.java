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

package com.io7m.jcanephora.tests.jogl.contracts.jogl21;

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
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.DepthBuffersContract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;
import com.io7m.junreachable.UnreachableCodeException;

public final class JOGL21DepthBuffersTest extends DepthBuffersContract
{
  @Override public JCGLDepthBufferType getGLDepthBuffer(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL2(tc);
  }

  @Override public JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL2(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL21WithExtensionsSupported();
  }

  @Override public FramebufferType makeFramebufferWithDepth(
    final TestContext context,
    final JCGLImplementationType gi)
  {
    try {
      final JCGLInterfaceGL2Type g = JOGLTestContextUtilities.getGL2(context);
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
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public FramebufferType makeFramebufferWithoutDepth(
    final TestContext context,
    final JCGLImplementationType gi)
  {
    try {
      final JCGLInterfaceGL2Type g = JOGLTestContextUtilities.getGL2(context);
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
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL2_1();
  }
}
