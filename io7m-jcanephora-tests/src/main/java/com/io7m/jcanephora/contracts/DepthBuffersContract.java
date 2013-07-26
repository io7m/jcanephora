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
package com.io7m.jcanephora.contracts;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.JCGLDepthBuffer;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLFramebuffersCommon;
import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public abstract class DepthBuffersContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull JCGLDepthBuffer getGLDepthBuffer(
    final @Nonnull TestContext tc);

  public abstract @Nonnull JCGLFramebuffersCommon getGLFramebuffers(
    final @Nonnull TestContext tc);

  public abstract @Nonnull FramebufferReference makeFramebufferWithDepth(
    final @Nonnull JCGLImplementation gi)
    throws ConstraintError,
      JCGLException;

  public abstract @Nonnull FramebufferReference makeFramebufferWithoutDepth(
    final @Nonnull JCGLImplementation gi)
    throws ConstraintError,
      JCGLException;

  @Test public void testDepthBufferClearWorks()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test public void testDepthBufferEnable()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final DepthFunction f : DepthFunction.values()) {
      gd.depthBufferDisable();
      Assert.assertFalse(gd.depthBufferIsEnabled());
      gd.depthBufferEnable(f);
      Assert.assertTrue(gd.depthBufferIsEnabled());
    }
  }

  /**
   * Attempting to query an unbound framebuffer still works (the default
   * framebuffer is queried instead).
   */

  @Test public void testDepthBufferWithoutBoundFramebufferWorks()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    gl.framebufferDrawUnbind();
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    final int bits = gd.depthBufferGetBits();
    Assert.assertTrue(bits >= 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthClearFails()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferClear(1.0f);
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWithoutDepthEnableFails()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferEnable(DepthFunction.DEPTH_EQUAL);
  }

  @Test public void testDepthBufferWithoutDepthHasNoBits()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gd.depthBufferGetBits());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteDisableWithoutDepthFails()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteDisable();
  }

  @Test public void testDepthBufferWriteEnableDisableWorks()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithDepth(gi);
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

  @Test(expected = ConstraintError.class) public
    void
    testDepthBufferWriteEnableWithoutDepthFails()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLDepthBuffer gd = this.getGLDepthBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutDepth(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gd.depthBufferWriteEnable();
  }
}
