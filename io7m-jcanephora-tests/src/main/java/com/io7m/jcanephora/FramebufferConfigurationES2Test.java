package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public final class FramebufferConfigurationES2Test
{
  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGB2DNulls()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTexture2D(
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(4, rejected);
  }

  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGBA2DNulls()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTexture2D(
        null,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(4, rejected);
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
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
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
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(5, rejected);
  }

  /**
   * Null checks.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testBestRGBCubeNulls()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTextureCube(
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
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        null);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    Assert.assertEquals(5, rejected);
  }

  /**
   * Requesting a cube map texture for a rectangular framebuffer is rejected.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testCubeSizeFails()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);
    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
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
    final FramebufferConfigurationGLES2 c0 =
      new FramebufferConfigurationGLES2Actual(128, 128);

    Assert.assertFalse(c0.equals(null));
    Assert.assertFalse(c0.equals(Integer.valueOf(23)));
    Assert.assertTrue(c0.equals(c0));

    {
      final FramebufferConfigurationGLES2 c1 =
        new FramebufferConfigurationGLES2Actual(128, 129);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGLES2 c1 =
        new FramebufferConfigurationGLES2Actual(127, 128);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGLES2 c1 =
        new FramebufferConfigurationGLES2Actual(128, 128);
      c1.requestBestRGBAColorRenderbuffer();
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationGLES2 c1 =
        new FramebufferConfigurationGLES2Actual(128, 128);
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGLES2 c2 =
        new FramebufferConfigurationGLES2Actual(128, 128);
      c2.requestDepthRenderbuffer();
      c2.requestNoStencil();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationGLES2 c1 =
        new FramebufferConfigurationGLES2Actual(128, 128);
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationGLES2 c2 =
        new FramebufferConfigurationGLES2Actual(128, 128);
      c2.requestNoDepth();
      c2.requestStencilRenderbuffer();
      Assert.assertFalse(c1.equals(c2));
    }
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 c0 =
      new FramebufferConfigurationGLES2Actual(128, 128);
    final FramebufferConfigurationGLES2 c1 =
      new FramebufferConfigurationGLES2Actual(128, 128);

    c0.requestNoDepth();
    c0.requestNoStencil();

    c1.requestNoDepth();
    c1.requestNoStencil();

    Assert.assertEquals(c0.hashCode(), c1.hashCode());

    c1.requestBestRGBAColorRenderbuffer();

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
    final FramebufferConfigurationGLES2 config =
      new FramebufferConfigurationGLES2Actual(128, 256);

    Assert.assertEquals(128, config.getWidth());
    Assert.assertEquals(256, config.getHeight());
  }

  @SuppressWarnings("static-method") @Test public void testString()
    throws ConstraintError
  {
    final FramebufferConfigurationGLES2 c0 =
      new FramebufferConfigurationGLES2Actual(128, 256);
    final FramebufferConfigurationGLES2 c1 =
      new FramebufferConfigurationGLES2Actual(128, 256);
    final FramebufferConfigurationGLES2 c2 =
      new FramebufferConfigurationGLES2Actual(128, 300);

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
    new FramebufferConfigurationGLES2Actual(256, 0);
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
    new FramebufferConfigurationGLES2Actual(0, 256);
  }
}
