package com.io7m.jcanephora;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

public class TextureTypeTest
{
  @SuppressWarnings("static-method") @Test public void testBytesPerPixel()
  {
    Assert.assertEquals(1, TextureType.TEXTURE_TYPE_R_8_1BPP.bytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RG_88_2BPP.bytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RGB_565_2BPP.bytesPerPixel());
    Assert.assertEquals(
      3,
      TextureType.TEXTURE_TYPE_RGB_888_3BPP.bytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RGBA_4444_2BPP.bytesPerPixel());
    Assert.assertEquals(
      3,
      TextureType.TEXTURE_TYPE_RGB_888_3BPP.bytesPerPixel());
    Assert.assertEquals(
      4,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP.bytesPerPixel());
  }

  @SuppressWarnings("static-method") @Test public void testComponents()
  {
    Assert.assertEquals(
      EnumSet.noneOf(TextureType.class),
      TextureType.getWithComponents(0));
  }
}
