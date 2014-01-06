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

package com.io7m.jcanephora.contracts.gl3;

import java.util.List;

import javax.annotation.Nonnull;

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
import com.io7m.jcanephora.FramebufferConfigurationGL3;
import com.io7m.jcanephora.FramebufferConfigurationGL3Actual;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLFramebuffersGL3;
import com.io7m.jcanephora.JCGLRenderbuffersGL3;
import com.io7m.jcanephora.JCGLTextures2DStaticCommon;
import com.io7m.jcanephora.JCGLTexturesCubeStaticCommon;
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

public abstract class FramebuffersGL3Contract<G extends JCGLFramebuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon & JCGLRenderbuffersGL3> implements
  TestContract
{
  private static @Nonnull
    <G extends JCGLFramebuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon & JCGLRenderbuffersGL3>
    Framebuffer
    makeAssumingSuccess(
      final FramebufferConfigurationGL3 config,
      final G gl)
      throws JCGLRuntimeException,
        ConstraintError
  {
    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
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

  public abstract G getGLInterface(
    TestContext context);

  /**
   * Associating an attachment with a draw buffer, then deleting the
   * attachment, breaks the association between the draw buffer and the
   * attachment.
   * 
   * @throws ConstraintError
   * @throws JCGLUnsupportedException
   * @throws JCGLRuntimeException
   */

  @Test public void testBufferAssociationBreaks()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();
    Assume.assumeTrue(points.size() >= 2);
    Assume.assumeTrue(buffers.size() >= 2);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));
    config.requestNoColorAt(points.get(0));
    config.requestBestRGBAColorRenderbuffer(points.get(1), buffers.get(0));
  }

  /**
   * Requesting the best available color RGBA renderbuffer works.
   */

  @Test public void testColorRGBARenderbuffersBest()
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTexture2D(
      points.get(0),
      buffers.get(0),
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 128);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorTextureCube(
      points.get(0),
      buffers.get(0),
      TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorRenderbuffer(points.get(0), buffers.get(0));

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTexture2D(
      points.get(0),
      buffers.get(0),
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 128);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBColorTextureCube(
      points.get(0),
      buffers.get(0),
      TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    final Framebuffer fb = success.value;
    Assert.assertTrue(fb.hasColorAttachment(points.get(0)));

    final AttachmentColor ca = fb.getColorAttachment(points.get(0));
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
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
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
   * @throws JCGLRuntimeException
   * 
   */

  @Test public void testDepthRenderbuffer()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));
    config.requestNoStencil();
    config.requestDepthRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
    Assert.assertTrue(result.isSuccess());
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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.make(gl);
  }

  @Test public void testMultipleDrawBufferMapping()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();
    Assume.assumeTrue(points.size() >= 2);
    Assume.assumeTrue(buffers.size() >= 2);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();
    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    int rejected = 0;

    try {
      config.requestBestRGBAColorRenderbuffer(points.get(1), buffers.get(0));
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorRenderbuffer(points.get(1), buffers.get(0));
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        points.get(1),
        buffers.get(0),
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        points.get(1),
        buffers.get(0),
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        points.get(1),
        buffers.get(0),
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        points.get(1),
        buffers.get(0),
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(6, rejected);
  }

  /**
   * Requesting to share a color renderbuffer results in the correct shared
   * renderbuffer attachment.
   * 
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorRenderbufferRGB()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config.requestBestRGBColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points.get(0));

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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb0,
      points.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

    Assert.assertTrue(fb1.hasColorAttachment(points.get(0)));

    final AttachmentColor attach = fb1.getColorAttachment(points.get(0));
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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorRenderbufferRGBA()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points.get(0));

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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb0,
      points.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

    Assert.assertTrue(fb1.hasColorAttachment(points.get(0)));

    final AttachmentColor attach = fb1.getColorAttachment(points.get(0));
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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorTexture2D()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config.requestBestRGBAColorTexture2D(
      points.get(0),
      buffers.get(0),
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points.get(0));

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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb0,
      points.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

    Assert.assertTrue(fb1.hasColorAttachment(points.get(0)));

    final AttachmentColor attach = fb1.getColorAttachment(points.get(0));
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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedColorTextureCube()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config.requestBestRGBAColorTextureCube(
      points.get(0),
      buffers.get(0),
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points.get(0));

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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb0,
      points.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

    Assert.assertTrue(fb1.hasColorAttachment(points.get(0)));

    final AttachmentColor attach = fb1.getColorAttachment(points.get(0));
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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedDepth()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));
    fb0_config.requestDepthRenderbuffer();

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedDepth(fb0);
    fb1_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedSharedColorRenderbufferRGBA()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
    final AttachmentColor fb0_attach = fb0.getColorAttachment(points.get(0));

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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb0,
      points.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

    Assert.assertTrue(fb1.hasColorAttachment(points.get(0)));

    final AttachmentColor attach1 = fb1.getColorAttachment(points.get(0));
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

    final FramebufferConfigurationGL3 fb2_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb2_config.requestSharedColor(
      points.get(0),
      buffers.get(0),
      fb1,
      points.get(0));

    final Framebuffer fb2 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb2_config, gl);

    Assert.assertTrue(fb2.hasColorAttachment(points.get(0)));

    final AttachmentColor attach2 = fb2.getColorAttachment(points.get(0));
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
   * @throws JCGLRuntimeException
   * @throws JCGLUnsupportedException
   * @throws ConstraintError
   */

  @Test public void testSharedStencil()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    /**
     * Create initial framebuffer.
     */

    final FramebufferConfigurationGL3 fb0_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb0_config.requestDepthRenderbuffer();
    fb0_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb0 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb0_config, gl);
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

    final FramebufferConfigurationGL3 fb1_config =
      new FramebufferConfigurationGL3Actual(128, 128);
    fb1_config.requestSharedStencil(fb0);
    fb1_config
      .requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));

    final Framebuffer fb1 =
      FramebuffersGL3Contract.makeAssumingSuccess(fb1_config, gl);

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
   * @throws JCGLRuntimeException
   * 
   */

  @Test public void testStencilRenderbuffer()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final G gl = this.getGLInterface(tc);

    final List<FramebufferColorAttachmentPoint> points =
      gl.framebufferGetColorAttachmentPoints();
    final List<FramebufferDrawBuffer> buffers =
      gl.framebufferGetDrawBuffers();

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3Actual(128, 256);

    config.requestBestRGBAColorRenderbuffer(points.get(0), buffers.get(0));
    config.requestNoDepth();
    config.requestStencilRenderbuffer();

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      config.make(gl);
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
