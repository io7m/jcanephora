package com.io7m.jcanephora.contracts.common;

import javax.annotation.Nonnull;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLFramebuffersCommon;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLStencilBuffer;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.TestContext;

public abstract class StencilBuffersContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLStencilBuffer getGLStencilBuffer(
    TestContext tc);

  public abstract GLFramebuffersCommon getGLFramebuffers(
    TestContext tc);

  public abstract @Nonnull
    FramebufferReference
    makeFramebufferWithoutStencil(
      final @Nonnull GLImplementation gi)
      throws ConstraintError,
        GLException;

  public abstract @Nonnull FramebufferReference makeFramebufferWithStencil(
    final @Nonnull GLImplementation gi)
    throws ConstraintError,
      GLException;

  @Test public void testStencilBufferClear()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferClear(0);
  }

  @Test public void testStencilBufferEnableDisable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferFunction(FaceSelection.FACE_FRONT, null, 0, 0xFF);
  }

  @Test public void testStencilBufferMask()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    gs.stencilBufferMask(null, 0);
  }

  @Test public void testStencilBufferOperations()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

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
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    Assert.assertFalse(gl.framebufferDrawAnyIsBound());

    Assert.assertTrue(gs.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithoutStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(0, gs.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLFramebuffersCommon gl = this.getGLFramebuffers(tc);
    final GLStencilBuffer gs = this.getGLStencilBuffer(tc);

    final FramebufferReference fb = this.makeFramebufferWithStencil(gi);
    Assert.assertFalse(gl.framebufferDrawAnyIsBound());
    Assert.assertFalse(gl.framebufferDrawIsBound(fb));

    gl.framebufferDrawBind(fb);
    Assert.assertTrue(gl.framebufferDrawAnyIsBound());
    Assert.assertTrue(gl.framebufferDrawIsBound(fb));

    Assert.assertEquals(8, gs.stencilBufferGetBits());
  }
}
