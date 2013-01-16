package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.TestContext;

public abstract class StencilBuffersContract implements GLES2TestContract
{
  private static FramebufferReference makeFramebuffer(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer sb = g.renderbufferAllocateStencil8(128, 128);
    final Renderbuffer cb = g.renderbufferAllocateRGB565(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);
    g.framebufferDrawAttachStencilRenderbuffer(fb, sb);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  private static FramebufferReference makeFramebufferNoStencil(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final FramebufferReference fb = g.framebufferAllocate();
    final Renderbuffer cb = g.renderbufferAllocateRGB565(128, 128);

    g.framebufferDrawBind(fb);
    g.framebufferDrawAttachColorRenderbuffer(fb, cb);

    final FramebufferStatus expect =
      FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
    final FramebufferStatus status = g.framebufferDrawValidate(fb);
    Assert.assertEquals(expect, status);

    g.framebufferDrawUnbind();
    return fb;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testStencilBufferClear()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferClear(0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferClearWithoutStencil()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebufferNoStencil(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferClear(0);
  }

  @Test public void testStencilBufferEnableDisable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);

    gl.stencilBufferEnable();
    Assert.assertTrue(gl.stencilBufferIsEnabled());
    gl.stencilBufferDisable();
    Assert.assertFalse(gl.stencilBufferIsEnabled());
  }

  @Test public void testStencilBufferFunctions()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);

    for (final FaceSelection face : FaceSelection.values()) {
      for (final StencilFunction function : StencilFunction.values()) {
        gl.stencilBufferFunction(face, function, 0, 0xFF);
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferFunction(null, StencilFunction.STENCIL_ALWAYS, 0, 0xFF);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferFunctionsNullFunction()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferFunction(FaceSelection.FACE_FRONT, null, 0, 0xFF);
  }

  @Test public void testStencilBufferMask()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferMask(FaceSelection.FACE_FRONT_AND_BACK, 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferMaskNullFace()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferMask(null, 0);
  }

  @Test public void testStencilBufferOperations()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);

    for (final FaceSelection face : FaceSelection.values()) {
      for (final StencilOperation stencil_fail : StencilOperation.values()) {
        for (final StencilOperation depth_fail : StencilOperation.values()) {
          for (final StencilOperation pass : StencilOperation.values()) {
            gl.stencilBufferOperation(face, stencil_fail, depth_fail, pass);
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferOperation(
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferOperation(
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferOperation(
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    gl.stencilBufferOperation(
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
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    Assert.assertTrue(gl.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebufferNoStencil(gl);
    gl.framebufferDrawBind(fb);
    Assert.assertEquals(0, gl.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final FramebufferReference fb =
      StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferDrawBind(fb);
    Assert.assertEquals(8, gl.stencilBufferGetBits());
  }
}
