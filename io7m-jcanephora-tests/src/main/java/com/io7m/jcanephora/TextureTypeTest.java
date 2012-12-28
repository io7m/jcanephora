package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureTypeTest
{
  @SuppressWarnings("static-method") @Test public void testBytesPerPixel()
  {
    Assert.assertEquals(
      1,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_R_8_1BPP));
    Assert.assertEquals(
      2,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RG_88_2BPP));
    Assert.assertEquals(
      2,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RGB_565_2BPP));
    Assert.assertEquals(
      3,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RGB_888_3BPP));
    Assert.assertEquals(
      2,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RGBA_4444_2BPP));
    Assert.assertEquals(
      3,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RGB_888_3BPP));
    Assert.assertEquals(
      4,
      TextureTypeMeta.bytesPerPixel(TextureType.TEXTURE_TYPE_RGBA_8888_4BPP));
  }
}
