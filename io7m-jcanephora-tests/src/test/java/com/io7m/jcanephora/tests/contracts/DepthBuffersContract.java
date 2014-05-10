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

package com.io7m.jcanephora.tests.contracts;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.tests.TestContext;

@SuppressWarnings({ "null" }) public abstract class DepthBuffersContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLDepthBufferType getGLDepthBuffer(
    final TestContext tc);

  public abstract JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc);

  public abstract FramebufferType makeFramebufferWithDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  public abstract FramebufferType makeFramebufferWithoutDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  @Test public void testDepthBufferClearWorks()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final DepthFunction f : DepthFunction.values()) {
      gd.depthBufferTestDisable();
      Assert.assertFalse(gd.depthBufferTestIsEnabled());
      gd.depthBufferTestEnable(f);
      Assert.assertTrue(gd.depthBufferTestIsEnabled());
    }
  }

  /**
   * Attempting to query an unbound framebuffer still works (the default
   * framebuffer is queried instead).
   */

  @Test public void testDepthBufferWithoutBoundFramebufferWorks()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    gl.framebufferDrawUnbind();
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    final int bits = gd.depthBufferGetBits();
    Assert.assertTrue(bits >= 0);
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthBufferWithoutDepthClearFails()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthBufferWithoutDepthEnableFails()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferTestEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gd.depthBufferGetBits());
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthBufferWriteDisableWithoutDepthFails()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteEnable();
    Assert.assertTrue(gd.depthBufferWriteIsEnabled());
    gd.depthBufferWriteDisable();
    Assert.assertFalse(gd.depthBufferWriteIsEnabled());
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthBufferWriteEnableWithoutDepthFails()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthBufferType gd = this.getGLDepthBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteEnable();
  }
}
