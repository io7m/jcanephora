package com.io7m.jcanephora;

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
   * Two channel luminance and alpha, 8 bits per channel, two bytes per pixel.
   */

  TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP,

  /**
   * Single channel luminance, 8 bits per channel, one byte per pixel.
   */

  TEXTURE_TYPE_LUMINANCE_8_1BPP,

  /**
   * Single channel alpha, 8 bits per channel, one byte per pixel.
   */

  TEXTURE_TYPE_ALPHA_8_1BPP
}
