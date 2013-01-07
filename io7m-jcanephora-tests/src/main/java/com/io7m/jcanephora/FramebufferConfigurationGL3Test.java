package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public final class FramebufferConfigurationGL3Test
{
  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGB2DNulls()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTexture2D(
        null,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(6, rejected);
  }

  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGBA2DNulls()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTexture2D(
        null,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(6, rejected);
  }

  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testBestRGBACubeNulls()
      throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
        null,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(7, rejected);
  }

  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGBCubeNulls()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTextureCube(
        null,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(7, rejected);
  }

  /**
   * Requesting a cube map texture for a rectangular framebuffer is rejected.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testCubeSizeFails()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);
    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestSpecificColorTextureCube(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(3, rejected);
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 c0 =
      new FramebufferConfigurationGL3(128, 128);

    Assert.assertFalse(c0.equals(null));
    Assert.assertFalse(c0.equals(Integer.valueOf(23)));
    Assert.assertTrue(c0.equals(c0));

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 129);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(127, 128);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestBestRGBAColorRenderbuffer(point, buffer);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_LINEAR);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGBA4444,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestNoColor();
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestNoColor();
      c2.requestDepthRenderbuffer();
      c2.requestNoStencil();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestNoColor();
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestNoColor();
      c2.requestNoDepth();
      c2.requestStencilRenderbuffer();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTexture2D(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTextureCube(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTextureCube(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3(128, 128);
      c1.requestSpecificColorTextureCube(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3(128, 128);
      c2.requestSpecificColorTextureCube(
        point,
        buffer,
        RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 c0 =
      new FramebufferConfigurationGL3(128, 128);
    final FramebufferConfigurationGL3 c1 =
      new FramebufferConfigurationGL3(128, 128);

    c0.requestNoColor();
    c0.requestNoDepth();
    c0.requestNoStencil();

    c1.requestNoColor();
    c1.requestNoDepth();
    c1.requestNoStencil();

    Assert.assertEquals(c0.hashCode(), c1.hashCode());

    c1.requestSpecificColorRenderbuffer(
      point,
      buffer,
      RequestColorTypeGL3.REQUEST_GL3_COLOR_RGB565);

    Assert.assertFalse(c0.hashCode() == c1.hashCode());
  }

  /**
   * Simple get/set.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final FramebufferConfigurationGL3 config =
      new FramebufferConfigurationGL3(128, 256);

    Assert.assertEquals(128, config.getWidth());
    Assert.assertEquals(256, config.getHeight());
  }

  @SuppressWarnings("static-method") @Test public void testString()
    throws ConstraintError
  {
    final FramebufferConfigurationGL3 c0 =
      new FramebufferConfigurationGL3(128, 256);
    final FramebufferConfigurationGL3 c1 =
      new FramebufferConfigurationGL3(128, 256);
    final FramebufferConfigurationGL3 c2 =
      new FramebufferConfigurationGL3(128, 300);

    Assert.assertEquals(c0.toString(), c0.toString());
    Assert.assertEquals(c0.toString(), c1.toString());
    Assert.assertFalse(c0.toString().equals(c2.toString()));
  }

  /**
   * Invalid height.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testZeroHeight()
    throws ConstraintError
  {
    new FramebufferConfigurationGL3(256, 0);
  }

  /**
   * Invalid width.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testZeroWidth()
    throws ConstraintError
  {
    new FramebufferConfigurationGL3(0, 256);
  }
}
