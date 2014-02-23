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

package com.io7m.jcanephora.contracts.gl3;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.JCGLFramebuffersGL3;
import com.io7m.jcanephora.JCGLRenderbuffersGL3ES3;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.RenderableColor;
import com.io7m.jcanephora.RenderableDepth;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.TestContract;

public abstract class FramebufferReadGL3Contract implements TestContract
{
  private final @Nonnull static FramebufferReference makeFramebuffer(
    final JCGLFramebuffersGL3 g,
    final JCGLRenderbuffersGL3ES3 r)
    throws JCGLRuntimeException,
      ConstraintError
  {
    final Renderbuffer<RenderableColor> rbc =
      r.renderbufferAllocateRGB888(128, 128);
    final Renderbuffer<RenderableDepth> rbd =
      r.renderbufferAllocateDepth24(128, 128);

    final FramebufferReference fb = g.framebufferAllocate();
    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, rbc);
    g.framebufferDrawAttachDepthRenderbuffer(fb, rbd);
    final FramebufferStatus s = g.framebufferDrawValidate(fb);
    Assert.assertEquals(FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE, s);
    g.framebufferDrawUnbind();
    return fb;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull JCGLFramebuffersGL3 getFramebuffersGL3(
    final @Nonnull TestContext tc);

  public abstract @Nonnull JCGLRenderbuffersGL3ES3 getRenderbuffersGL3(
    final @Nonnull TestContext tc);

  @Test(expected = ConstraintError.class) public final
    void
    testBlitNotBound_R()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferReference fbw =
      FramebufferReadGL3Contract.makeFramebuffer(
        g,
        this.getRenderbuffersGL3(tc));

    g.framebufferDrawBind(fbw);
    g.framebufferReadUnbind();
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.noneOf(FramebufferBlitBuffer.class),
      FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testBlitNotBound_W()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferReference fb =
      FramebufferReadGL3Contract.makeFramebuffer(
        g,
        this.getRenderbuffersGL3(tc));

    g.framebufferReadBind(fb);
    g.framebufferDrawUnbind();
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.noneOf(FramebufferBlitBuffer.class),
      FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testBlitBadFilter_0()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferReference fb =
      FramebufferReadGL3Contract.makeFramebuffer(
        g,
        this.getRenderbuffersGL3(tc));

    g.framebufferReadBind(fb);
    g.framebufferDrawBind(fb);
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.of(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testBlitBadFilter_1()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferReference fb =
      FramebufferReadGL3Contract.makeFramebuffer(
        g,
        this.getRenderbuffersGL3(tc));

    g.framebufferReadBind(fb);
    g.framebufferDrawBind(fb);
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.of(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL),
      FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test(expected = ConstraintError.class) public final void testBlitNull_0()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    g.framebufferBlit(null, null, null, null);
  }

  @Test(expected = ConstraintError.class) public final void testBlitNull_1()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(area0, null, null, null);
  }

  @Test(expected = ConstraintError.class) public final void testBlitNull_2()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(area0, area0, null, null);
  }

  @Test(expected = ConstraintError.class) public final void testBlitNull_3()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final RangeInclusive range = new RangeInclusive(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.noneOf(FramebufferBlitBuffer.class),
      null);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testDeletedReadBuffer()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final FramebufferReference fb = g.framebufferAllocate();
    g.framebufferDelete(fb);
    g.framebufferReadBind(fb);
  }

  @Test public final void testNoReadBuffer()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    Assert.assertFalse(g.framebufferReadAnyIsBound());
  }

  @Test(expected = ConstraintError.class) public final
    void
    testNullReadBuffer()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    g.framebufferReadBind(null);
  }

  @Test public final void testReadUnbindIsolated()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3 g = this.getFramebuffersGL3(tc);
    final FramebufferReference fbw =
      FramebufferReadGL3Contract.makeFramebuffer(
        g,
        this.getRenderbuffersGL3(tc));

    Assert.assertFalse(g.framebufferDrawIsBound(fbw));
    Assert.assertFalse(g.framebufferReadIsBound(fbw));
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferReadAnyIsBound());

    g.framebufferDrawBind(fbw);
    Assert.assertTrue(g.framebufferDrawIsBound(fbw));
    Assert.assertFalse(g.framebufferReadIsBound(fbw));
    Assert.assertTrue(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferReadAnyIsBound());

    g.framebufferDrawUnbind();
    Assert.assertFalse(g.framebufferDrawIsBound(fbw));
    Assert.assertFalse(g.framebufferReadIsBound(fbw));
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferReadAnyIsBound());

    g.framebufferReadBind(fbw);
    Assert.assertFalse(g.framebufferDrawIsBound(fbw));
    Assert.assertTrue(g.framebufferReadIsBound(fbw));
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertTrue(g.framebufferReadAnyIsBound());

    g.framebufferReadUnbind();
    Assert.assertFalse(g.framebufferDrawIsBound(fbw));
    Assert.assertFalse(g.framebufferReadIsBound(fbw));
    Assert.assertFalse(g.framebufferDrawAnyIsBound());
    Assert.assertFalse(g.framebufferReadAnyIsBound());
  }
}
