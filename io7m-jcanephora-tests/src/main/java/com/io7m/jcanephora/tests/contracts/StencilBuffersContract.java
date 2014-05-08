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

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;

@SuppressWarnings("null") public abstract class StencilBuffersContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLFramebuffersCommonType getGLFramebuffers(
    TestContext tc);

  public abstract JCGLStencilBufferType getGLStencilBuffer(
    TestContext tc);

  public abstract @Nonnull FramebufferType makeFramebufferWithoutStencil(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLImplementationType gi);

  public abstract @Nonnull FramebufferType makeFramebufferWithStencil(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLImplementationType gi);

  @Test public void testStencilBufferClear()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferClear(0);
  }

  @Test(expected = JCGLExceptionNoStencilBuffer.class) public
    void
    testStencilBufferClearWithoutStencil()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferClear(0);
  }

  @Test public void testStencilBufferEnableDisable()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferEnable();
    Assert.assertTrue(gs.stencilBufferIsEnabled());
    gs.stencilBufferDisable();
    Assert.assertFalse(gs.stencilBufferIsEnabled());
  }

  @Test public void testStencilBufferFunctions()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final FaceSelection face : FaceSelection.values()) {
      for (final StencilFunction function : StencilFunction.values()) {
        gs.stencilBufferFunction(face, function, 0, 0xFF);
      }
    }
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferFunctionsNullFace()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferFunction(
      (FaceSelection) TestUtilities.actuallyNull(),
      StencilFunction.STENCIL_ALWAYS,
      0,
      0xFF);
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferFunctionsNullFunction()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferFunction(
      FaceSelection.FACE_FRONT,
      (StencilFunction) TestUtilities.actuallyNull(),
      0,
      0xFF);
  }

  @Test public void testStencilBufferMask()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferMask(FaceSelection.FACE_FRONT_AND_BACK, 0);
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferMaskNullFace()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferMask((FaceSelection) TestUtilities.actuallyNull(), 0);
  }

  @Test public void testStencilBufferOperations()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    for (final FaceSelection face : FaceSelection.values()) {
      for (final StencilOperation stencil_fail : StencilOperation.values()) {
        for (final StencilOperation depth_fail : StencilOperation.values()) {
          for (final StencilOperation pass : StencilOperation.values()) {
            gs.stencilBufferOperation(face, stencil_fail, depth_fail, pass);
          }
        }
      }
    }
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferOperationsNullDepthFail()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      StencilOperation.STENCIL_OP_DECREMENT,
      (StencilOperation) TestUtilities.actuallyNull(),
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferOperationsNullFace()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      (FaceSelection) TestUtilities.actuallyNull(),
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferOperationsNullPass()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT,
      (StencilOperation) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public
    void
    testStencilBufferOperationsNullStencilFail()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      (StencilOperation) TestUtilities.actuallyNull(),
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test public void testStencilBufferWithoutBoundFramebufferWorks()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    Assert.assertTrue(gs.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithoutStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gs.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLStencilBufferType gs = this.getGLStencilBuffer(tc);

    final FramebufferType fb = this.makeFramebufferWithStencil(tc, gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(8, gs.stencilBufferGetBits());
  }
}
