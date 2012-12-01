package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;

/**
 * Functions providing information about texture types.
 */

public final class TextureTypeMeta
{
  public static int components(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_ALPHA_8_1BPP:
      case TEXTURE_TYPE_LUMINANCE_8_1BPP:
        return 1;
      case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
        return 2;
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
    }

    throw new UnreachableCodeException();
  }

  public static int bytesPerPixel(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_ALPHA_8_1BPP:
      case TEXTURE_TYPE_LUMINANCE_8_1BPP:
        return 1;
      case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
        return 2;
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
    }

    throw new UnreachableCodeException();
  }

  private TextureTypeMeta()
  {
    throw new UnreachableCodeException();
  }
}
