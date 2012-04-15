package com.io7m.jcanephora;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.annotation.Nonnull;

final class TextureUtils
{
  private TextureUtils()
  {
    // Unused.
  }

  static final @Nonnull BufferedImage convertImageToABGR(
    final @Nonnull BufferedImage image)
  {
    BufferedImage converted = null;

    final int width = image.getWidth();
    final int height = image.getHeight();

    switch (image.getType()) {
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      {
        converted = image;
        return converted;
      }
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_USHORT_GRAY:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_CUSTOM:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      default:
      {
        converted =
          new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, converted);
        return converted;
      }
    }
  }
}
