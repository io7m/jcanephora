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
package com.io7m.jcanephora.contracts.gles2;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jcanephora.AttachmentColor;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentDepth;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilRenderbuffer;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferConfigurationGL3ES2;
import com.io7m.jcanephora.FramebufferConfigurationGL3ES2Actual;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.RenderableColor;
import com.io7m.jcanephora.RenderbufferUsable;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStaticUsable;
import com.io7m.jcanephora.TextureCubeStaticUsable;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TestContract;

public abstract class FramebuffersES2Contract implements TestContract
{
  private static @Nonnull Framebuffer makeAssumingSuccess(
    final FramebufferConfigurationGL3ES2 config,
    final JCGLImplementation gi)
    throws JCGLException,
      ConstraintError
  {
    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;
    final Framebuffer fb = success.value;
    return fb;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Requesting the best available color RGBA renderbuffer works.
   */

  @Test public void testColorRGBARenderbuffersBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

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

    final RenderbufferUsable<RenderableColor> rb = car.getRenderbuffer();
    Assert.assertTrue(rb.getType().isColorRenderable());

    fb.delete(gl);
  }

  /**
   * Requesting the best available RGBA 2D texture works.
   */

  @Test public void testColorRGBATexture2DBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTexture2D(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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

    final Texture2DStaticUsable t = cat.getTexture2D();
    Assert
      .assertEquals(t.getWrapS(), TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapT(), TextureWrapT.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilterMinification.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    fb.delete(gl);
  }

  /**
   * Requesting the best available RGBA Cube texture works.
   */

  @Test public void testColorRGBATextureCubeBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);

    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTextureCube(
      TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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

    final TextureCubeStaticUsable t = cat.getTextureCube();
    Assert
      .assertEquals(t.getWrapR(), TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapS(), TextureWrapS.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(t.getWrapT(), TextureWrapT.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilterMinification.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X, cat.getFace());

    fb.delete(gl);
  }

  /**
   * Requesting the best available color RGB renderbuffer works.
   */

  @Test public void testColorRGBRenderbuffersBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

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

    final RenderbufferUsable<RenderableColor> rb = car.getRenderbuffer();
    Assert.assertTrue(rb.getType().isColorRenderable());

    fb.delete(gl);
  }

  /**
   * Requesting the best available RGB 2D texture works.
   */

  @Test public void testColorRGBTexture2DBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTexture2D(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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

    final Texture2DStaticUsable t = cat.getTexture2D();
    Assert
      .assertEquals(t.getWrapS(), TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapT(), TextureWrapT.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilterMinification.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    fb.delete(gl);
  }

  /**
   * Requesting the best available RGB Cube texture works.
   */

  @Test public void testColorRGBTextureCubeBest()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);

    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTextureCube(
      TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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

    final TextureCubeStaticUsable t = cat.getTextureCube();
    Assert
      .assertEquals(t.getWrapR(), TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE);
    Assert.assertEquals(t.getWrapS(), TextureWrapS.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(t.getWrapT(), TextureWrapT.TEXTURE_WRAP_REPEAT);
    Assert.assertEquals(
      t.getMinificationFilter(),
      TextureFilterMinification.TEXTURE_FILTER_LINEAR);
    Assert.assertEquals(
      t.getMagnificationFilter(),
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X, cat.getFace());

    fb.delete(gl);
  }

  /**
   * Deleting a framebuffer marks the framebuffer as deleted.
   */

  @Test public void testDelete()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertFalse(fb.resourceIsDeleted());
    fb.delete(gl);
    Assert.assertTrue(fb.resourceIsDeleted());
  }

  /**
   * Requesting a depth renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   *           , GLUnsupportedException
   */

  @Test public void testDepthRenderbuffer()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestBestRGBAColorRenderbuffer();
    config.requestNoStencil();
    config.requestDepthRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gi);
    switch (result.type) {
      case FAILURE:
      {
        final Failure<Framebuffer, FramebufferStatus> failure =
          (Failure<Framebuffer, FramebufferStatus>) result;
        Assert.fail(failure.value.toString());
        break;
      }
      case SUCCESS:
      {
        break;
      }
    }

    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;
    final Framebuffer fb = success.value;

    Assert.assertTrue(fb.hasDepthAttachment());
    final AttachmentDepth da = fb.getDepthAttachment();

    RenderbufferUsable<?> rb = null;
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

    fb.delete(gl);
  }

  @Test(expected = ConstraintError.class) public void testEmptyFails()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestNoDepth();
    config.requestNoStencil();
    config.make(gi);
  }

  /**
   * Requesting to share a color renderbuffer results in the correct shared
   * renderbuffer attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorRenderbufferRGB()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBColorRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points[0]);

    switch (fb0_attach.type) {
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentColorRenderbuffer fb0_ar =
      (AttachmentColorRenderbuffer) fb0_attach;
    final RenderbufferUsable<RenderableColor> fb0_r =
      fb0_ar.getRenderbuffer();

    /**
     * Create framebuffer that shares the color attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestSharedColor(fb0, points[0]);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasColorAttachment(points[0]));

    final AttachmentColor attach = fb1.getColorAttachment(points[0]);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {

        break;
      }
    }

    final AttachmentSharedColorRenderbuffer shared =
      (AttachmentSharedColorRenderbuffer) attach;

    Assert.assertTrue(shared.getRenderbuffer() == fb0_r);
  }

  /**
   * Requesting to share a color renderbuffer results in the correct shared
   * renderbuffer attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorRenderbufferRGBA()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points[0]);

    switch (fb0_attach.type) {
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentColorRenderbuffer fb0_ar =
      (AttachmentColorRenderbuffer) fb0_attach;
    final RenderbufferUsable<RenderableColor> fb0_r =
      fb0_ar.getRenderbuffer();

    /**
     * Create framebuffer that shares the color attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestSharedColor(fb0, points[0]);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasColorAttachment(points[0]));

    final AttachmentColor attach = fb1.getColorAttachment(points[0]);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {

        break;
      }
    }

    final AttachmentSharedColorRenderbuffer shared =
      (AttachmentSharedColorRenderbuffer) attach;

    Assert.assertTrue(shared.getRenderbuffer() == fb0_r);
  }

  /**
   * Requesting to share a color 2D texture results in the correct shared 2D
   * texture attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorTexture2D()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorTexture2D(
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points[0]);

    switch (fb0_attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_2D:
      {
        break;
      }
    }

    final AttachmentColorTexture2DStatic fb0_at =
      (AttachmentColorTexture2DStatic) fb0_attach;
    final Texture2DStaticUsable fb0_t = fb0_at.getTexture2D();

    /**
     * Create framebuffer that shares the color attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestSharedColor(fb0, points[0]);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasColorAttachment(points[0]));

    final AttachmentColor attach = fb1.getColorAttachment(points[0]);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      {
        break;
      }
    }

    final AttachmentSharedColorTexture2DStatic shared =
      (AttachmentSharedColorTexture2DStatic) attach;

    Assert.assertTrue(shared.getTexture2D() == fb0_t);
  }

  /**
   * Requesting to share a color cube texture results in the correct shared
   * cube texture attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorTextureCube()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorTextureCube(
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points[0]);

    switch (fb0_attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        break;
      }
    }

    final AttachmentColorTextureCubeStatic fb0_at =
      (AttachmentColorTextureCubeStatic) fb0_attach;
    final TextureCubeStaticUsable fb0_t = fb0_at.getTextureCube();

    /**
     * Create framebuffer that shares the color attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestSharedColor(fb0, points[0]);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasColorAttachment(points[0]));

    final AttachmentColor attach = fb1.getColorAttachment(points[0]);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        break;
      }
    }

    final AttachmentSharedColorTextureCubeStatic shared =
      (AttachmentSharedColorTextureCubeStatic) attach;

    Assert.assertTrue(shared.getTextureCube() == fb0_t);
  }

  /**
   * Requesting to share a depth attachment results in the correct shared
   * depth attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedDepth()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorRenderbuffer();
    fb0_config.requestDepthRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentDepth fb0_attach = fb0.getDepthAttachment();

    RenderbufferUsable<?> fb0_r = null;
    switch (fb0_attach.type) {
      case ATTACHMENT_DEPTH_RENDERBUFFER:
      {
        final AttachmentDepthRenderbuffer ar =
          (AttachmentDepthRenderbuffer) fb0_attach;
        fb0_r = ar.getRenderbuffer();
        break;
      }
      case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentDepthStencilRenderbuffer ar =
          (AttachmentDepthStencilRenderbuffer) fb0_attach;
        fb0_r = ar.getRenderbuffer();
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
      case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
    }

    /**
     * Create framebuffer that shares the depth attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestBestRGBAColorRenderbuffer();
    fb1_config.requestSharedDepth(fb0);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasDepthAttachment());

    RenderbufferUsable<?> fb1_r = null;
    final AttachmentDepth attach = fb1.getDepthAttachment();
    switch (attach.type) {
      case ATTACHMENT_DEPTH_RENDERBUFFER:
      case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
      {
        final AttachmentSharedDepthRenderbuffer a =
          (AttachmentSharedDepthRenderbuffer) attach;

        fb1_r = a.getRenderbuffer();
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentSharedDepthStencilRenderbuffer a =
          (AttachmentSharedDepthStencilRenderbuffer) attach;

        fb1_r = a.getRenderbuffer();
        break;
      }
    }

    Assert.assertTrue(fb1_r == fb0_r);
  }

  /**
   * Requesting to share an already-shared color renderbuffer results in the
   * correct shared renderbuffer attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedSharedColorRenderbufferRGBA()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points[0]);

    switch (fb0_attach.type) {
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentColorRenderbuffer fb0_ar =
      (AttachmentColorRenderbuffer) fb0_attach;
    final RenderbufferUsable<?> fb0_r = fb0_ar.getRenderbuffer();

    /**
     * Create framebuffer that shares the color attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestSharedColor(fb0, points[0]);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasColorAttachment(points[0]));

    final AttachmentColor attach1 = fb1.getColorAttachment(points[0]);
    switch (attach1.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach1.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {

        break;
      }
    }

    final AttachmentSharedColorRenderbuffer shared1 =
      (AttachmentSharedColorRenderbuffer) attach1;

    Assert.assertTrue(shared1.getRenderbuffer() == fb0_r);

    /**
     * Create framebuffer that shares the shared color attachment of fb1.
     */

    final FramebufferConfigurationGL3ES2 fb2_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb2_config.requestSharedColor(fb1, points[0]);

    final Framebuffer fb2 =
      FramebuffersES2Contract.makeAssumingSuccess(fb2_config, gi);

    Assert.assertTrue(fb2.hasColorAttachment(points[0]));

    final AttachmentColor attach2 = fb2.getColorAttachment(points[0]);
    switch (attach2.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_2D:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        Assert.fail("unreachable: type = " + attach2.type);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {
        break;
      }
    }

    final AttachmentSharedColorRenderbuffer shared2 =
      (AttachmentSharedColorRenderbuffer) attach2;

    Assert.assertTrue(shared2.getRenderbuffer() == fb0_r);
  }

  /**
   * Requesting to share a stencil attachment results in the correct shared
   * stencil attachment.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedStencil()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3ES2 fb0_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb0_config.requestBestRGBAColorRenderbuffer();
    fb0_config.requestDepthRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersES2Contract.makeAssumingSuccess(fb0_config, gi);
    final AttachmentStencil fb0_attach = fb0.getStencilAttachment();

    RenderbufferUsable<?> fb0_r = null;
    switch (fb0_attach.type) {
      case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
      {
        final AttachmentDepth d_attach = fb0.getDepthAttachment();
        switch (d_attach.type) {
          case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
          {
            final AttachmentDepthStencilRenderbuffer a =
              (AttachmentDepthStencilRenderbuffer) d_attach;
            fb0_r = a.getRenderbuffer();
            break;
          }
          case ATTACHMENT_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
          {
            Assert.fail("unreachable: type = " + d_attach.type);
            break;
          }
        }
        break;
      }
      case ATTACHMENT_STENCIL_RENDERBUFFER:
      {
        final AttachmentStencilRenderbuffer a =
          (AttachmentStencilRenderbuffer) fb0_attach;
        fb0_r = a.getRenderbuffer();
        break;
      }
    }

    /**
     * Create framebuffer that shares the stencil attachment of fb0.
     */

    final FramebufferConfigurationGL3ES2 fb1_config =
      new FramebufferConfigurationGL3ES2Actual(128, 128);
    fb1_config.requestBestRGBAColorRenderbuffer();
    fb1_config.requestSharedStencil(fb0);

    final Framebuffer fb1 =
      FramebuffersES2Contract.makeAssumingSuccess(fb1_config, gi);

    Assert.assertTrue(fb1.hasStencilAttachment());

    RenderbufferUsable<?> fb1_r = null;
    final AttachmentDepth attach = fb1.getDepthAttachment();
    switch (attach.type) {
      case ATTACHMENT_DEPTH_RENDERBUFFER:
      case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
      {
        Assert.fail("unreachable: type = " + fb0_attach.type);
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
      {
        final AttachmentSharedDepthRenderbuffer a =
          (AttachmentSharedDepthRenderbuffer) attach;

        fb1_r = a.getRenderbuffer();
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentSharedDepthStencilRenderbuffer a =
          (AttachmentSharedDepthStencilRenderbuffer) attach;

        fb1_r = a.getRenderbuffer();
        break;
      }
    }

    Assert.assertTrue(fb1_r == fb0_r);
  }

  /**
   * Requesting a stencil renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   *           , GLUnsupportedException
   */

  @Test public void testStencilRenderbuffer()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();

    final FramebufferConfigurationGL3ES2 config =
      new FramebufferConfigurationGL3ES2Actual(128, 256);

    config.requestBestRGBAColorRenderbuffer();
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
            final RenderbufferUsable<?> rb = dsa.getRenderbuffer();
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

    fb.delete(gl);
  }
}
