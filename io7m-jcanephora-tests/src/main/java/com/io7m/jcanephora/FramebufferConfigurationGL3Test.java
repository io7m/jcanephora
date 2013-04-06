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
      new FramebufferConfigurationGL3Actual(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTexture2D(
        null,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        null,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
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
      new FramebufferConfigurationGL3Actual(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTexture2D(
        null,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        null,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        point,
        buffer,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
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
      new FramebufferConfigurationGL3Actual(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
        null,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        null,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        null,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
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
      new FramebufferConfigurationGL3Actual(128, 256);
    config.requestNoColor();
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTextureCube(
        null,
        buffer,
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
        point,
        null,
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
        point,
        buffer,
        null,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTextureCube(
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
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
      new FramebufferConfigurationGL3Actual(128, 256);
    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
        point,
        buffer,
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
        point,
        buffer,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(2, rejected);
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final FramebufferColorAttachmentPoint point =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(0);

    final FramebufferConfigurationGL3 c0 =
      new FramebufferConfigurationGL3Actual(128, 128);

    Assert.assertFalse(c0.equals(null));
    Assert.assertFalse(c0.equals(Integer.valueOf(23)));
    Assert.assertTrue(c0.equals(c0));

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3Actual(128, 129);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3Actual(127, 128);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3Actual(128, 128);
      c1.requestBestRGBAColorRenderbuffer(point, buffer);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3Actual(128, 128);
      c1.requestNoColor();
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3Actual(128, 128);
      c2.requestNoColor();
      c2.requestDepthRenderbuffer();
      c2.requestNoStencil();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGL3 c1 =
        new FramebufferConfigurationGL3Actual(128, 128);
      c1.requestNoColor();
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGL3 c2 =
        new FramebufferConfigurationGL3Actual(128, 128);
      c2.requestNoColor();
      c2.requestNoDepth();
      c2.requestStencilRenderbuffer();
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
      new FramebufferConfigurationGL3Actual(128, 128);
    final FramebufferConfigurationGL3 c1 =
      new FramebufferConfigurationGL3Actual(128, 128);

    c0.requestNoColor();
    c0.requestNoDepth();
    c0.requestNoStencil();

    c1.requestNoColor();
    c1.requestNoDepth();
    c1.requestNoStencil();

    Assert.assertEquals(c0.hashCode(), c1.hashCode());

    c1.requestBestRGBAColorRenderbuffer(point, buffer);

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
      new FramebufferConfigurationGL3Actual(128, 256);

    Assert.assertEquals(128, config.getWidth());
    Assert.assertEquals(256, config.getHeight());
  }

  @SuppressWarnings("static-method") @Test public void testString()
    throws ConstraintError
  {
    final FramebufferConfigurationGL3 c0 =
      new FramebufferConfigurationGL3Actual(128, 256);
    final FramebufferConfigurationGL3 c1 =
      new FramebufferConfigurationGL3Actual(128, 256);
    final FramebufferConfigurationGL3 c2 =
      new FramebufferConfigurationGL3Actual(128, 300);

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
    new FramebufferConfigurationGL3Actual(256, 0);
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
    new FramebufferConfigurationGL3Actual(0, 256);
  }
}
