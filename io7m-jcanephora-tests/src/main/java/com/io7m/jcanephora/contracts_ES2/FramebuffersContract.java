package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnimplementedCodeException;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;

public abstract class FramebuffersContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Attaching multiple depth/stencil buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testAttachDepthMultiple()
      throws GLException,
        ConstraintError
  {
    final GLInterfaceES2 g = this.makeNewGL();
    final Renderbuffer rb = g.renderbufferAllocateDepth16(128, 128);
    final Texture2DStatic cb =
      g.texture2DStaticAllocateRGBA8888(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    g.framebufferAllocate(new FramebufferAttachment[] {
      new ColorAttachment(cb, 0),
      new RenderbufferD24S8Attachment(rb),
      new RenderbufferD24S8Attachment(rb) });
  }

  /**
   * A framebuffer with a D24S8 buffer attached has 24 bits of depth precision
   * and 8 bits of stencil precision.
   */

  @Test public void testCheckDepthStencilBits()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 g = this.makeNewGL();

    final Renderbuffer rb = g.renderbufferAllocateDepth16(128, 128);
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

    g.framebufferBind(fb);
    Assert.assertTrue(g.depthBufferGetBits() == 24);
    Assert.assertTrue(g.stencilBufferGetBits() == 8);
    g.framebufferUnbind();
  }

  /**
   * Enabling depth-testing on a framebuffer without a depth buffer fails.
   */

  @Test(expected = ConstraintError.class) public void testDepthEnableNone()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 g = this.makeNewGL();

    try {
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
          .framebufferAllocate(new FramebufferAttachment[] { new FramebufferAttachment.ColorAttachment(
            cb,
            0) });
      g.framebufferBind(fb);

    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    g.depthBufferEnable(DepthFunction.DEPTH_LESS_THAN_OR_EQUAL);
  }

  /**
   * Attaching a set of depth/stencil/color buffers to a framebuffer works.
   */

  @Test public void testFramebufferAttachColorDepthOK()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Renderbuffer rb = gl.renderbufferAllocateDepth16(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(rb);
    gl.framebufferAllocate(new FramebufferAttachment[] { fda, fbc });
  }

  /**
   * Attaching a set of depth/stencil/color buffers in a different order makes
   * no difference.
   */

  @Test public void testFramebufferAttachColorDepthOKOrderIrrelevant()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Renderbuffer rb = gl.renderbufferAllocateDepth16(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(rb);
    gl.framebufferAllocate(new FramebufferAttachment[] { fbc, fda });
  }

  /**
   * All of the available texture types are usable as framebuffer textures.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @SuppressWarnings("static-method") @Test public
    void
    testFramebufferAttachColorFormatsOK()
      throws ConstraintError,
        GLException
  {
    /**
     * This won't be written until the new framebuffer API is implemented.
     */

    throw new UnimplementedCodeException();
  }

  /**
   * Attaching a multiple color buffers to a framebuffer works.
   */

  @Test public void testFramebufferAttachColorMultipleOK()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t0 =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc0 = new ColorAttachment(t0, 0);
    final Texture2DStatic t1 =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc1 = new ColorAttachment(t1, 1);
    gl.framebufferAllocate(new FramebufferAttachment[] { fbc0, fbc1 });
  }

  /**
   * Attaching a multiple color buffers to the same index in a framebuffer
   * fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachColorMultipleSameIndexFails()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t0 =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc0 = new ColorAttachment(t0, 0);
    final Texture2DStatic t1 =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc1 = new ColorAttachment(t1, 0);
    gl.framebufferAllocate(new FramebufferAttachment[] { fbc0, fbc1 });
  }

  /**
   * Attaching a single color buffer to a framebuffer works.
   */

  @Test public void testFramebufferAttachColorOK()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
  }

  /**
   * Attaching a two depth/stencil buffers to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachDepthTwice()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Renderbuffer rb = gl.renderbufferAllocateDepth16(128, 128);
    final RenderbufferD24S8Attachment fda =
      new RenderbufferD24S8Attachment(rb);
    gl.framebufferAllocate(new FramebufferAttachment[] { fda, fda });
  }

  /**
   * Attaching nothing to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNone()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.framebufferAllocate(new FramebufferAttachment[] {});
  }

  /**
   * Attaching a list with a null element fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachListNullElement()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.framebufferAllocate(new FramebufferAttachment[] { null });
  }

  /**
   * Attaching null to a framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferAttachNull()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.framebufferAllocate(null);
  }

  /**
   * Binding a deleted framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferBindDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
    fb.resourceDelete(gl);
    gl.framebufferBind(fb);
  }

  /**
   * Clearing the color buffer works.
   */

  @Test public void testFramebufferClearColor()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
    gl.framebufferBind(fb);
    gl.colorBufferClear4f(1.0f, 0.0f, 0.0f, 1.0f);
    gl.colorBufferClear3f(1.0f, 0.0f, 0.0f);
    gl.colorBufferClearV3f(new VectorI3F(1.0f, 0.0f, 1.0f));
    gl.colorBufferClearV4f(new VectorI4F(1.0f, 0.0f, 1.0f, 1.0f));
    gl.framebufferUnbind();
  }

  /**
   * Passing a null clear color fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferClearColorNull3()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.colorBufferClearV3f(null);
  }

  /**
   * Passing a null clear color fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferClearColorNull4()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.colorBufferClearV4f(null);
  }

  /**
   * Trying to create a framebuffer with a deleted color texture fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testFramebufferColorDeleted()
      throws GLException,
        ConstraintError
  {
    GLInterfaceES2 gl = null;
    ColorAttachment fbc = null;

    try {
      gl = this.makeNewGL();
      final Texture2DStatic t =
        gl.texture2DStaticAllocateRGBA8888(
          "buffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);
      t.resourceDelete(gl);

      fbc = new ColorAttachment(t, 0);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    assert fbc != null;

    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
  }

  /**
   * Trying to create a framebuffer with a deleted depth/stencil texture
   * fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testFramebufferD24S8Deleted()
      throws GLException,
        ConstraintError
  {
    GLInterfaceES2 gl = null;
    RenderbufferD24S8Attachment fda = null;

    try {
      gl = this.makeNewGL();
      final Renderbuffer rb = gl.renderbufferAllocateDepth16(128, 128);
      fda = new RenderbufferD24S8Attachment(rb);
      rb.resourceDelete(gl);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert gl != null;
    assert fda != null;

    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fda });
  }

  /**
   * Deleting a framebuffer works.
   */

  @Test public void testFramebufferDeleteOK()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
    fb.resourceDelete(gl);

    Assert.assertTrue(fb.resourceIsDeleted());
  }

  /**
   * Deleting a deleted framebuffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testFramebufferDeleteTwice()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "buffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ColorAttachment fbc = new ColorAttachment(t, 0);
    final Framebuffer fb =
      gl.framebufferAllocate(new FramebufferAttachment[] { fbc });
    fb.resourceDelete(gl);
    fb.resourceDelete(gl);
  }
}
