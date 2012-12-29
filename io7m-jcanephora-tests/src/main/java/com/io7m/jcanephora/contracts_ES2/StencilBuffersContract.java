package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;

public abstract class StencilBuffersContract implements GLES2TestContract
{
  private static Framebuffer makeFramebuffer(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final RenderbufferD24S8 rb = g.renderbufferD24S8Allocate(128, 128);
    final Texture2DStatic cb =
      g.texture2DStaticAllocateRGBA8888(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g.framebufferAllocate(new FramebufferAttachment[] {
        new ColorAttachment(cb, 0),
        new RenderbufferD24S8Attachment(rb) });
    return fb;
  }

  private static Framebuffer makeFramebufferNoStencil(
    final GLInterfaceES2 g)
    throws GLException,
      ConstraintError
  {
    final Texture2DStatic cb =
      g.texture2DStaticAllocateRGBA8888(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g
        .framebufferAllocate(new FramebufferAttachment[] { new ColorAttachment(
          cb,
          0) });
    return fb;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testStencilBufferClear()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferClear(0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferClearWithoutStencil()
      throws GLException,
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb =
      StencilBuffersContract.makeFramebufferNoStencil(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferClear(0);
  }

  @Test public void testStencilBufferEnableDisable()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);

    gl.stencilBufferEnable();
    Assert.assertTrue(gl.stencilBufferIsEnabled());
    gl.stencilBufferDisable();
    Assert.assertFalse(gl.stencilBufferIsEnabled());
  }

  @Test public void testStencilBufferFunctions()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);

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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferFunction(null, StencilFunction.STENCIL_ALWAYS, 0, 0xFF);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferFunctionsNullFunction()
      throws GLException,
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferFunction(FaceSelection.FACE_FRONT, null, 0, 0xFF);
  }

  @Test public void testStencilBufferMask()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferMask(FaceSelection.FACE_FRONT_AND_BACK, 0);
  }

  @Test(expected = ConstraintError.class) public
    void
    testStencilBufferMaskNullFace()
      throws GLException,
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferMask(null, 0);
  }

  @Test public void testStencilBufferOperations()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);

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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    gl.stencilBufferOperation(
      FaceSelection.FACE_FRONT_AND_BACK,
      null,
      StencilOperation.STENCIL_OP_DECREMENT,
      StencilOperation.STENCIL_OP_DECREMENT);
  }

  @Test public void testStencilBufferWithoutBoundFramebufferWorks()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    Assert.assertTrue(gl.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb =
      StencilBuffersContract.makeFramebufferNoStencil(gl);
    gl.framebufferBind(fb);
    Assert.assertEquals(0, gl.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    Assert.assertEquals(8, gl.stencilBufferGetBits());
  }
}
