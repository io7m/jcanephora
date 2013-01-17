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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTexture2D(
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTexture2D(
        null,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        null,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    } catch (final ConstraintError e) {
      ++rejected;
    }

    try {
      config.requestBestRGBAColorTexture2D(
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
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
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);
    config.requestNoDepth();
    config.requestNoStencil();

    int rejected = 0;

    try {
      config.requestBestRGBColorTextureCube(
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
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);
    int rejected = 0;

    try {
      config.requestBestRGBAColorTextureCube(
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
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
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
    final FramebufferConfigurationES2 c0 =
      new FramebufferConfigurationES2(128, 128);

    Assert.assertFalse(c0.equals(null));
    Assert.assertFalse(c0.equals(Integer.valueOf(23)));
    Assert.assertTrue(c0.equals(c0));

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 129);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(127, 128);
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestBestRGBAColorRenderbuffer();
      Assert.assertFalse(c0.equals(c1));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_LINEAR);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGBA4444,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestDepthRenderbuffer();
      c2.requestNoStencil();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestNoDepth();
      c1.requestNoStencil();
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestNoDepth();
      c2.requestStencilRenderbuffer();
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTexture2D(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertFalse(c1.equals(c2));
    }

    {
      final FramebufferConfigurationES2 c1 =
        new FramebufferConfigurationES2(128, 128);
      c1.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      final FramebufferConfigurationES2 c2 =
        new FramebufferConfigurationES2(128, 128);
      c2.requestSpecificColorTextureCube(
        RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
      Assert.assertTrue(c1.equals(c2));
    }
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FramebufferConfigurationES2 c0 =
      new FramebufferConfigurationES2(128, 128);
    final FramebufferConfigurationES2 c1 =
      new FramebufferConfigurationES2(128, 128);

    c0.requestNoDepth();
    c0.requestNoStencil();

    c1.requestNoDepth();
    c1.requestNoStencil();

    Assert.assertEquals(c0.hashCode(), c1.hashCode());

    c1
      .requestSpecificColorRenderbuffer(RequestColorTypeES2.REQUEST_ES2_COLOR_RGB565);

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
    final FramebufferConfigurationES2 config =
      new FramebufferConfigurationES2(128, 256);

    Assert.assertEquals(128, config.getWidth());
    Assert.assertEquals(256, config.getHeight());
  }

  @SuppressWarnings("static-method") @Test public void testString()
    throws ConstraintError
  {
    final FramebufferConfigurationES2 c0 =
      new FramebufferConfigurationES2(128, 256);
    final FramebufferConfigurationES2 c1 =
      new FramebufferConfigurationES2(128, 256);
    final FramebufferConfigurationES2 c2 =
      new FramebufferConfigurationES2(128, 300);

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
    new FramebufferConfigurationES2(256, 0);
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
    new FramebufferConfigurationES2(0, 256);
  }
}
