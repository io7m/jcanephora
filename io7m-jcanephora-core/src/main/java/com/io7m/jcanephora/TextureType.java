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

package com.io7m.jcanephora;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;

/**
 * The type of elements in a given texture.
 */

public enum TextureType
{
  /**
   * Four-channel RGBA, 8 bits per channel, four bytes per pixel.
   */

  TEXTURE_TYPE_RGBA_8888_4BPP,

  /**
   * Three-channel RGB, 8 bits per channel, three bytes per pixel.
   */

  TEXTURE_TYPE_RGB_888_3BPP,

  /**
   * Four-channel RGBA, 4 bits per channel, two bytes per pixel.
   */

  TEXTURE_TYPE_RGBA_4444_2BPP,

  /**
   * Four-channel RGBA, 5 bits per (R, G, B) channels, 1 bit alpha, two bytes
   * per pixel.
   */

  TEXTURE_TYPE_RGBA_5551_2BPP,

  /**
   * Four-channel RGBA, 5 bits R, 6 bits G, 5 bits B, two bytes per pixel.
   */

  TEXTURE_TYPE_RGB_565_2BPP,

  /**
   * Two channel red/green, 8 bits per channel, two bytes per pixel.
   */

  TEXTURE_TYPE_RG_88_2BPP,

  /**
   * Single channel red, 8 bits per channel, one byte per pixel.
   */

  TEXTURE_TYPE_R_8_1BPP,

  /**
   * 16 bit depth component texture, two bytes per pixel.
   */

  TEXTURE_TYPE_DEPTH_16_2BPP,

  /**
   * 24 bit depth component texture, four bytes per pixel.
   */

  TEXTURE_TYPE_DEPTH_24_4BPP,

  /**
   * 32 bit depth component texture with floating point components, four bytes
   * per pixel.
   */

  TEXTURE_TYPE_DEPTH_32F_4BPP;

  /**
   * The subset of texture types supported by ES2
   */

  static final @Nonnull TextureType[] ES2_TYPES;

  static {
    ES2_TYPES =
      new TextureType[] {
    TEXTURE_TYPE_RGBA_8888_4BPP,
    TEXTURE_TYPE_RGBA_5551_2BPP,
    TEXTURE_TYPE_RGBA_4444_2BPP,
    TEXTURE_TYPE_RGB_888_3BPP,
    TEXTURE_TYPE_RGB_565_2BPP };
  }

  /**
   * Retrieve a list of the texture types supported by ES2
   */

  public static @Nonnull TextureType[] getES2Types()
  {
    final TextureType[] est = new TextureType[TextureType.ES2_TYPES.length];
    for (int index = 0; index < TextureType.ES2_TYPES.length; ++index) {
      est[index] = TextureType.ES2_TYPES[index];
    }
    return est;
  }

  /**
   * Return all texture types with the given number of components.
   */

  public static Set<TextureType> getWithComponents(
    final int c)
  {
    final EnumSet<TextureType> xs = EnumSet.noneOf(TextureType.class);

    if (c <= 0) {
      return xs;
    }

    for (final TextureType t : TextureType.values()) {
      if (t.getComponents() == c) {
        xs.add(t);
      }
    }

    return xs;
  }

  /**
   * Retrieve the number of bytes per pixel in the texture type.
   */

  public int bytesPerPixel()
  {
    switch (this) {
      case TEXTURE_TYPE_R_8_1BPP:
        return 1;
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
        return 2;
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Return the number of components in the texture type.
   */

  public int getComponents()
  {
    switch (this) {
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
      case TEXTURE_TYPE_RG_88_2BPP:
        return 2;
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
        return 1;
    }

    throw new UnreachableCodeException();
  }

  public boolean isColorRenderable()
  {
    switch (this) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return false;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return true;
      }
    }

    throw new UnreachableCodeException();
  }

  public boolean isDepthRenderable()
  {
    switch (this) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return true;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  public boolean isFloatingPoint()
  {
    switch (this) {
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
        return false;
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
        return true;
    }

    throw new UnreachableCodeException();
  }
}
