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

package com.io7m.jcanephora.tests.contracts.gl3;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.JCGLExceptionParameterError;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLRenderbuffersGL3ES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings("null") public abstract class FramebufferReadGL3Contract implements
  TestContract
{
  private final static FramebufferType makeFramebuffer(
    final JCGLFramebuffersGL3Type g,
    final JCGLRenderbuffersGL3ES3Type r)
    throws JCGLException
  {
    final RenderbufferType<RenderableColorKind> rbc =
      r.renderbufferAllocateRGB888(128, 128);
    final RenderbufferType<RenderableDepthKind> rbd =
      r.renderbufferAllocateDepth24(128, 128);

    final FramebufferType fb = g.framebufferAllocate();
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

  public abstract JCGLFramebuffersGL3Type getFramebuffersGL3(
    final TestContext tc);

  public abstract JCGLRenderbuffersGL3ES3Type getRenderbuffersGL3(
    final TestContext tc);

  @Test(expected = JCGLExceptionParameterError.class) public final
    void
    testBlitBadFilter_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferType fb =
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

  @Test(expected = JCGLExceptionParameterError.class) public final
    void
    testBlitBadFilter_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferType fb =
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

  @Test(expected = JCGLExceptionFramebufferNotBound.class) public final
    void
    testBlitNotBound_R()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferType fbw =
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

  @Test(expected = JCGLExceptionFramebufferNotBound.class) public final
    void
    testBlitNotBound_W()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);

    final FramebufferType fb =
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

  @SuppressWarnings("unchecked") @Test(expected = NullCheckException.class) public final
    void
    testBlitNull_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    g.framebufferBlit(
      (AreaInclusive) TestUtilities.actuallyNull(),
      (AreaInclusive) TestUtilities.actuallyNull(),
      (Set<FramebufferBlitBuffer>) TestUtilities.actuallyNull(),
      (FramebufferBlitFilter) TestUtilities.actuallyNull());
  }

  @SuppressWarnings("unchecked") @Test(expected = NullCheckException.class) public final
    void
    testBlitNull_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(
      area0,
      (AreaInclusive) TestUtilities.actuallyNull(),
      (Set<FramebufferBlitBuffer>) TestUtilities.actuallyNull(),
      (FramebufferBlitFilter) TestUtilities.actuallyNull());
  }

  @SuppressWarnings("unchecked") @Test(expected = NullCheckException.class) public final
    void
    testBlitNull_2()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(
      area0,
      area0,
      (Set<FramebufferBlitBuffer>) TestUtilities.actuallyNull(),
      (FramebufferBlitFilter) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public final
    void
    testBlitNull_3()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final RangeInclusiveL range = new RangeInclusiveL(0L, 100L);
    final AreaInclusive area0 = new AreaInclusive(range, range);
    g.framebufferBlit(
      area0,
      area0,
      EnumSet.noneOf(FramebufferBlitBuffer.class),
      (FramebufferBlitFilter) TestUtilities.actuallyNull());
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testDeletedReadBuffer()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final FramebufferType fb = g.framebufferAllocate();
    g.framebufferDelete(fb);
    g.framebufferReadBind(fb);
  }

  @Test public final void testNoReadBuffer()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    Assert.assertFalse(g.framebufferReadAnyIsBound());
  }

  @Test(expected = NullCheckException.class) public final
    void
    testNullReadBuffer()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    g.framebufferReadBind((FramebufferUsableType) TestUtilities
      .actuallyNull());
  }

  @Test public final void testReadUnbindIsolated()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type g = this.getFramebuffersGL3(tc);
    final FramebufferType fbw =
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
