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
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLFramebuffersCommon;
import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.JCGLStencilBuffer;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.TestContext;

public abstract class StencilBuffersContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLFramebuffersCommon getGLFramebuffers(
    TestContext tc);

  public abstract JCGLStencilBuffer getGLStencilBuffer(
    TestContext tc);

  public abstract @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      final @Nonnull JCGLImplementation gi)
      throws ConstraintError,
        JCGLException;

  public abstract @Nonnull FramebufferReference makeFramebufferWithStencil(
    final @Nonnull JCGLImplementation gi)
    throws ConstraintError,
      JCGLException;

  @Test public void testStencilBufferClear()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferClear(0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferClearWithoutStencil()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferClear(0);
  }

  @Test public void testStencilBufferEnableDisable()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
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
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
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

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferFunctionsNullFace()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferFunction(null, StencilFunction.STENCIL_ALWAYS, 0, 0xFF);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferFunctionsNullFunction()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferFunction(FaceSelection.FACE_FRONT, null, 0, 0xFF);
  }

  @Test public void testStencilBufferMask()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferMask(FaceSelection.FACE_FRONT_AND_BACK, 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferMaskNullFace()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferMask(null, 0);
  }

  @Test public void testStencilBufferOperations()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
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

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferOperationsNullDepthFail()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      StencilOperation.STENCIL_OP_DECREMENT,
      null,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferOperationsNullFace()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      null,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferOperationsNullPass()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT,
      null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferOperationsNullStencilFail()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      null,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test public void testStencilBufferWithoutBoundFramebufferWorks()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    Assert.assertTrue(gs.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gs.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final JCGLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(8, gs.stencilBufferGetBits());
  }
}
