package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jcanephora.AttachmentColor;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentDepth;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilRenderbuffer;
import com.io7m.jcanephora.CubeMapFace;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferConfigurationES2;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.RenderbufferReadable;
import com.io7m.jcanephora.RequestColorTypeES2;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStaticReadable;
import com.io7m.jcanephora.TextureCubeStaticReadable;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;

public abstract class FramebuffersContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Requesting all possible color texture types works, and creates textures
   * of the correct types.
   */

  @Test public void testColor2DSpecific()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    for (final RequestColorTypeES2 c : RequestColorTypeES2.values()) {
      config.requestSpecificColorTexture2D(
        c,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_NEAREST);

      final Indeterminate<Framebuffer, FramebufferStatus> result =
        config.make(gi);
      Assert.assertTrue(result.isSuccess());
      final Success<Framebuffer, FramebufferStatus> success =
        (Success<Framebuffer, FramebufferStatus>) result;

      final Framebuffer fb = success.value;

      Assert.assertTrue(fb.hasColorAttachment(points[0]));

      final AttachmentColor ca = fb.getColorAttachment(points[0]);
      switch (ca.type) {
        case ATTACHMENT_COLOR_RENDERBUFFER:
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          Assert.fail("unreachable, type = " + ca.type);
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_2D:
        {
          break;
        }
      }

      final AttachmentColorTexture2DStatic cat =
        (AttachmentColorTexture2DStatic) ca;

      final Texture2DStaticReadable t = cat.getTexture2D();
      Assert.assertEquals(c.equivalentTextureType(), t.getType());

      fb.resourceDelete(gl);
    }
  }

  /**
   * Requesting all possible color cube-map texture types works, and creates
   * textures of the correct types.
   */

  @Test public void testColorCubeSpecific()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 128);

    for (final RequestColorTypeES2 c : RequestColorTypeES2.values()) {
      config.requestSpecificColorTextureCube(
        c,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_NEAREST);

      final Indeterminate<Framebuffer, FramebufferStatus> result =
        config.make(gi);
      Assert.assertTrue(result.isSuccess());
      final Success<Framebuffer, FramebufferStatus> success =
        (Success<Framebuffer, FramebufferStatus>) result;

      final Framebuffer fb = success.value;
      Assert.assertTrue(fb.hasColorAttachment(points[0]));

      final AttachmentColor ca = fb.getColorAttachment(points[0]);
      switch (ca.type) {
        case ATTACHMENT_COLOR_RENDERBUFFER:
        case ATTACHMENT_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          Assert.fail("unreachable, type = " + ca.type);
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        {
          break;
        }
      }

      final AttachmentColorTextureCubeStatic cat =
        (AttachmentColorTextureCubeStatic) ca;

      final TextureCubeStaticReadable t = cat.getTextureCube();

      Assert.assertEquals(c.equivalentTextureType(), t.getType());
      Assert.assertEquals(CubeMapFace.CUBE_MAP_POSITIVE_X, cat.getFace());

      fb.resourceDelete(gl);
    }
  }

  /**
   * Requesting all possible color renderbuffer types works, and creates
   * buffers of the correct types.
   */

  @Test public void testColorRenderbuffersSpecific()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    for (final RequestColorTypeES2 c : RequestColorTypeES2.values()) {
      config.requestSpecificColorRenderbuffer(c);

      final Indeterminate<Framebuffer, FramebufferStatus> result =
        config.make(gi);
      Assert.assertTrue(result.isSuccess());
      final Success<Framebuffer, FramebufferStatus> success =
        (Success<Framebuffer, FramebufferStatus>) result;

      final Framebuffer fb = success.value;
      Assert.assertTrue(fb.hasColorAttachment(points[0]));

      final AttachmentColor ca = fb.getColorAttachment(points[0]);
      switch (ca.type) {
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        case ATTACHMENT_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          Assert.fail("unreachable, type = " + ca.type);
          break;
        }
        case ATTACHMENT_COLOR_RENDERBUFFER:
        {
          break;
        }
      }

      final AttachmentColorRenderbuffer car =
        (AttachmentColorRenderbuffer) ca;

      final RenderbufferReadable rb = car.getRenderbuffer();
      Assert.assertEquals(c.equivalentRenderbufferType(), rb.getType());

      fb.resourceDelete(gl);
    }
  }

  /**
   * Requesting the best available color RGBA renderbuffer works.
   */

  @Test public void testColorRGBARenderbuffersBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentColorRenderbuffer car = (AttachmentColorRenderbuffer) ca;

    final RenderbufferReadable rb = car.getRenderbuffer();
    Assert.assertTrue(rb.getType().isColorRenderable());

    fb.resourceDelete(gl);
  }

  /**
   * Requesting the best available RGBA 2D texture works.
   */

  @Test public void testColorRGBATexture2DBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTexture2D(
      TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_LINEAR,
      TextureFilter.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_2D:
      {
        break;
      }
    }

    final AttachmentColorTexture2DStatic cat =
      (AttachmentColorTexture2DStatic) ca;

    final Texture2DStaticReadable t = cat.getTexture2D();
    Assert.assertEquals(t.getWrapS(), TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapT(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilter.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilter.TEXTURE_FILTER_NEAREST);

    fb.resourceDelete(gl);
  }

  /**
   * Requesting the best available RGBA Cube texture works.
   */

  @Test public void testColorRGBATextureCubeBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 128);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTextureCube(
      TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_LINEAR,
      TextureFilter.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:

      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        break;
      }
    }

    final AttachmentColorTextureCubeStatic cat =
      (AttachmentColorTextureCubeStatic) ca;

    final TextureCubeStaticReadable t = cat.getTextureCube();
    Assert.assertEquals(t.getWrapR(), TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapS(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(t.getWrapT(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilter.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(CubeMapFace.CUBE_MAP_POSITIVE_X, cat.getFace());

    fb.resourceDelete(gl);
  }

  /**
   * Requesting the best available color RGB renderbuffer works.
   */

  @Test public void testColorRGBRenderbuffersBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentColorRenderbuffer car = (AttachmentColorRenderbuffer) ca;

    final RenderbufferReadable rb = car.getRenderbuffer();
    Assert.assertTrue(rb.getType().isColorRenderable());

    fb.resourceDelete(gl);
  }

  /**
   * Requesting the best available RGB 2D texture works.
   */

  @Test public void testColorRGBTexture2DBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTexture2D(
      TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_LINEAR,
      TextureFilter.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_2D:
      {
        break;
      }
    }

    final AttachmentColorTexture2DStatic cat =
      (AttachmentColorTexture2DStatic) ca;

    final Texture2DStaticReadable t = cat.getTexture2D();
    Assert.assertEquals(t.getWrapS(), TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapT(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilter.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilter.TEXTURE_FILTER_NEAREST);

    fb.resourceDelete(gl);
  }

  /**
   * Requesting the best available RGB Cube texture works.
   */

  @Test public void testColorRGBTextureCubeBest()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 128);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTextureCube(
      TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_LINEAR,
      TextureFilter.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points[0]));

    final AttachmentColor ca = fb.getColorAttachment(points[0]);
    switch (ca.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:

      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable, type = " + ca.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        break;
      }
    }

    final AttachmentColorTextureCubeStatic cat =
      (AttachmentColorTextureCubeStatic) ca;

    final TextureCubeStaticReadable t = cat.getTextureCube();
    Assert.assertEquals(t.getWrapR(), TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapS(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(t.getWrapT(), TextureWrap.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilter.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(CubeMapFace.CUBE_MAP_POSITIVE_X, cat.getFace());

    fb.resourceDelete(gl);
  }

  /**
   * Requesting a depth renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public void testDepthRenderbuffer()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoStencil();
    config.requestDepthRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;
    final Framebuffer fb = success.value;

    Assert.assertTrue(fb.hasDepthAttachment());
    final AttachmentDepth da = fb.getDepthAttachment();

    RenderbufferReadable rb = null;
    switch (da.type) {
      case ATTACHMENT_DEPTH_RENDERBUFFER:
      {
        final AttachmentDepthRenderbuffer adr =
          (AttachmentDepthRenderbuffer) da;
        rb = adr.getRenderbuffer();
        break;
      }
      case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentDepthStencilRenderbuffer adr =
          (AttachmentDepthStencilRenderbuffer) da;
        rb = adr.getRenderbuffer();
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
      case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + da.type);
        break;
      }
    }

    assert rb != null;
    Assert.assertTrue(rb.getType().isDepthRenderable());

    fb.resourceDelete(gl);
  }

  @Test(expected = ConstraintError.class) public void testEmptyFails()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.make(gi);
  }

  /**
   * Requesting a stencil renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public void testStencilRenderbuffer()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestStencilRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;
    final Framebuffer fb = success.value;

    Assert.assertTrue(fb.hasStencilAttachment());

    final AttachmentStencil a = fb.getStencilAttachment();
    switch (a.type) {
      case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + a.type);
        break;
      }

      /**
       * Implementation required a depth/stencil buffer.
       */

      case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
      {
        final AttachmentDepth da = fb.getDepthAttachment();
        switch (da.type) {
          case ATTACHMENT_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
          {
            Assert.fail("unreachable: type = " + da.type);
            break;
          }
          case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
          {
            final AttachmentDepthStencilRenderbuffer dsa =
              (AttachmentDepthStencilRenderbuffer) da;
            final RenderbufferReadable rb = dsa.getRenderbuffer();
            Assert.assertTrue(rb.getType().isDepthRenderable());
            Assert.assertTrue(rb.getType().isStencilRenderable());
            break;
          }
        }

        break;
      }

      /**
       * Implementation actually used a seperate depth/stencil buffer.
       */

      case ATTACHMENT_STENCIL_RENDERBUFFER:
      {
        final AttachmentStencilRenderbuffer ar =
          (AttachmentStencilRenderbuffer) a;
        Assert.assertTrue(ar
          .getRenderbuffer()
          .getType()
          .isStencilRenderable());
        break;
      }
    }

    fb.resourceDelete(gl);
  }
}
