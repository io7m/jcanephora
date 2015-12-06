/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core;

/**
 * Texture formats
 */

public enum JCGLTextureFormat
{
  /**
   * 16 bit, depth component, unsigned short components.
   */

  TEXTURE_FORMAT_DEPTH_16_2BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    1,
    2),

  /**
   * 24 bit, depth component, unsigned integer components.
   */

  TEXTURE_FORMAT_DEPTH_24_4BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT,
    1,
    4),

  /**
   * 32 bit, 24 bit depth component, 8 bit stencil, unsigned int components.
   */

  TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP(
    JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_INT_24_8,
    2,
    4),

  /**
   * 32 bit, depth component, floating point components.
   */

  TEXTURE_FORMAT_DEPTH_32F_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_FLOAT, 1, 4),

  /**
   * 16 bit, red channel, unsigned normalized fixed-point components.
   */

  TEXTURE_FORMAT_R_16_2BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    1,
    2),

  /**
   * 16 bit, red channel, half-precision floating point components.
   */

  TEXTURE_FORMAT_R_16F_2BPP(JCGLPixelFormat.PIXEL_COMPONENT_HALF_FLOAT, 1, 2),

  /**
   * 16 bit, red channel, signed integer components.
   */

  TEXTURE_FORMAT_R_16I_2BPP(JCGLPixelFormat.PIXEL_COMPONENT_SHORT, 1, 2),

  /**
   * 16 bit, red channel, unsigned integer components.
   */

  TEXTURE_FORMAT_R_16U_2BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    1,
    2),

  /**
   * 32 bit, red channel, single-precision floating point components.
   */

  TEXTURE_FORMAT_R_32F_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_FLOAT, 1, 4),

  /**
   * 32 bit, red channel, signed integer components.
   */

  TEXTURE_FORMAT_R_32I_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_INT, 1, 4),

  /**
   * 32 bit, red channel, unsigned integer components.
   */

  TEXTURE_FORMAT_R_32U_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT, 1, 4),

  /**
   * 8 bit, red channel, unsigned normalized fixed-point components.
   */

  TEXTURE_FORMAT_R_8_1BPP(JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE, 1, 1),

  /**
   * 8 bit, red channel, signed integer components.
   */

  TEXTURE_FORMAT_R_8I_1BPP(JCGLPixelFormat.PIXEL_COMPONENT_BYTE, 1, 1),

  /**
   * 8 bit, red channel, unsigned integer components.
   */

  TEXTURE_FORMAT_R_8U_1BPP(JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE, 1, 1),

  /**
   * 16 bit, red/green channels, unsigned normalized fixed-point components.
   */

  TEXTURE_FORMAT_RG_16_4BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    2,
    4),

  /**
   * 16 bit, red/green channels, half-precision floating point components.
   */

  TEXTURE_FORMAT_RG_16F_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_HALF_FLOAT, 2, 4),

  /**
   * 16 bit, red/green channels, signed integer components.
   */

  TEXTURE_FORMAT_RG_16I_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_SHORT, 2, 4),

  /**
   * 16 bit, red/green channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RG_16U_4BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    2,
    4),

  /**
   * 32 bit, red/green channels, single-precision floating-point components.
   */

  TEXTURE_FORMAT_RG_32F_8BPP(JCGLPixelFormat.PIXEL_COMPONENT_FLOAT, 2, 8),

  /**
   * 32 bit, red/green channels, signed integer components.
   */

  TEXTURE_FORMAT_RG_32I_8BPP(JCGLPixelFormat.PIXEL_COMPONENT_INT, 2, 8),

  /**
   * 32 bit, red/green channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RG_32U_8BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT,
    2,
    8),

  /**
   * 8 bit, red/green channels, unsigned normalized fixed-point components.
   */

  TEXTURE_FORMAT_RG_8_2BPP(JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE, 2, 2),

  /**
   * 8 bit, red/green channels, signed integer components.
   */

  TEXTURE_FORMAT_RG_8I_2BPP(JCGLPixelFormat.PIXEL_COMPONENT_BYTE, 2, 2),

  /**
   * 8 bit, red/green channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RG_8U_2BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE,
    2,
    2),

  /**
   * 16 bit, red/green/blue channels, unsigned normalized fixed-point
   * components.
   */

  TEXTURE_FORMAT_RGB_16_6BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    3,
    6),

  /**
   * 16 bit, red/green/blue channels, half-precision floating point components.
   */

  TEXTURE_FORMAT_RGB_16F_6BPP(JCGLPixelFormat.PIXEL_COMPONENT_HALF_FLOAT, 3, 6),

  /**
   * 16 bit, red/green/blue channels, signed integer components.
   */

  TEXTURE_FORMAT_RGB_16I_6BPP(JCGLPixelFormat.PIXEL_COMPONENT_SHORT, 3, 6),

  /**
   * 16 bit, red/green/blue channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGB_16U_6BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    3,
    6),

  /**
   * 32 bit, red/green/blue channels, floating point components.
   */

  TEXTURE_FORMAT_RGB_32F_12BPP(JCGLPixelFormat.PIXEL_COMPONENT_FLOAT, 3, 12),

  /**
   * 32 bit, red/green/blue channels, signed integer components.
   */

  TEXTURE_FORMAT_RGB_32I_12BPP(JCGLPixelFormat.PIXEL_COMPONENT_INT, 3, 12),

  /**
   * 32 bit, red/green/blue channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGB_32U_12BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT,
    3,
    12),

  /**
   * 16 bit, red5/green6/blue5 channels, packed components.
   */

  //  TEXTURE_FORMAT_RGB_565_2BPP(
  //    JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_565,
  //    3,
  //    2),

  /**
   * 8 bit, red/green/blue channels, unsigned normalized fixed-point
   * components.
   */

  TEXTURE_FORMAT_RGB_8_3BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE,
    3,
    3),

  /**
   * 8 bit, red/green/blue channels, signed integer components.
   */

  TEXTURE_FORMAT_RGB_8I_3BPP(JCGLPixelFormat.PIXEL_COMPONENT_BYTE, 3, 3),

  /**
   * 8 bit, red/green/blue channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGB_8U_3BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE,
    3,
    3),

  /**
   * 32 bit, red10/green10/blue10/alpha2 channels, packed components.
   */

  TEXTURE_FORMAT_RGBA_1010102_4BPP(
    JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_INT_1010102,
    4,
    4),

  /**
   * 16 bit, red/green/blue/alpha channels, unsigned normalized fixed-point
   * components.
   */

  TEXTURE_FORMAT_RGBA_16_8BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    4,
    8),

  /**
   * 16 bit, red/green/blue/alpha channels, half-precision floating point
   * components.
   */

  TEXTURE_FORMAT_RGBA_16F_8BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_HALF_FLOAT,
    4,
    8),

  /**
   * 16 bit, red/green/blue/alpha channels, signed integer components.
   */

  TEXTURE_FORMAT_RGBA_16I_8BPP(JCGLPixelFormat.PIXEL_COMPONENT_SHORT, 4, 8),

  /**
   * 16 bit, red/green/blue/alpha channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGBA_16U_8BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT,
    4,
    8),

  /**
   * 32 bit, red/green/blue/alpha channels, floating point components.
   */

  TEXTURE_FORMAT_RGBA_32F_16BPP(JCGLPixelFormat.PIXEL_COMPONENT_FLOAT, 4, 16),

  /**
   * 32 bit, red/green/blue/alpha channels, signed integer components.
   */

  TEXTURE_FORMAT_RGBA_32I_16BPP(JCGLPixelFormat.PIXEL_COMPONENT_INT, 4, 16),

  /**
   * 32 bit, red/green/blue/alpha channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGBA_32U_16BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT,
    4,
    16),

  /**
   * 16 bit, red4/green4/blue4/alpha4 channels, packed components.
   */

  //  TEXTURE_FORMAT_RGBA_4444_2BPP(
  //    JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_4444,
  //    4,
  //    2),

  /**
   * 16 bit, red5/green5/blue5/alpha1 channels, packed components.
   */

  //  TEXTURE_FORMAT_RGBA_5551_2BPP(
  //    JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_5551,
  //    4,
  //    2),

  /**
   * 8 bit, red/green/blue/alpha channels, unsigned normalized fixed-point
   * components.
   */

  TEXTURE_FORMAT_RGBA_8_4BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE,
    4,
    4),

  /**
   * 8 bit, red/green/blue/alpha channels, signed integer components.
   */

  TEXTURE_FORMAT_RGBA_8I_4BPP(JCGLPixelFormat.PIXEL_COMPONENT_BYTE, 4, 4),

  /**
   * 8 bit, red/green/blue/alpha channels, unsigned integer components.
   */

  TEXTURE_FORMAT_RGBA_8U_4BPP(
    JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE,
    4,
    4);

  private final int             bytes_per_pixel;
  private final int             components;
  private final JCGLPixelFormat pixel_format;

  JCGLTextureFormat(
    final JCGLPixelFormat in_pixel_format,
    final int in_components,
    final int in_bytes_per_pixel)
  {
    this.pixel_format = in_pixel_format;
    this.components = in_components;
    this.bytes_per_pixel = in_bytes_per_pixel;
  }

  /**
   * @return The number of bytes per pixel that this texture format requires.
   */

  public int getBytesPerPixel()
  {
    return this.bytes_per_pixel;
  }

  /**
   * @return The number of components in this texture format.
   */

  public int getComponentCount()
  {
    return this.components;
  }

  /**
   * @return The format used for the components.
   */

  public JCGLPixelFormat getComponentType()
  {
    return this.pixel_format;
  }
}
