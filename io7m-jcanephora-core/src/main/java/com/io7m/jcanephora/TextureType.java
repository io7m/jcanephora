/*
 * Copyright © 2012 http://io7m.com
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
}
