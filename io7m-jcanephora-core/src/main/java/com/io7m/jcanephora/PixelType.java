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

package com.io7m.jcanephora;

/**
 * <p>
 * The type of pixel data in client memory.
 * </p>
 */

public enum PixelType
{
  /**
   * 8-bit signed bytes.
   */

  PIXEL_COMPONENT_BYTE,

  /**
   * 32-bit floating point values.
   */

  PIXEL_COMPONENT_FLOAT,

  /**
   * 16-bit floating point values.
   */

  PIXEL_COMPONENT_HALF_FLOAT,

  /**
   * 32-bit signed integers.
   */

  PIXEL_COMPONENT_INT,

  /**
   * 16-bit signed integers.
   */

  PIXEL_COMPONENT_SHORT,

  /**
   * 8-bit unsigned bytes.
   */

  PIXEL_COMPONENT_UNSIGNED_BYTE,

  /**
   * 32-bit unsigned integers.
   */

  PIXEL_COMPONENT_UNSIGNED_INT,

  /**
   * 16-bit unsigned integers.
   */

  PIXEL_COMPONENT_UNSIGNED_SHORT,

  /**
   * A packed 32-bit unsigned integer.
   */

  PIXEL_PACKED_UNSIGNED_INT_1010102,

  /**
   * A packed 32-bit unsigned integer.
   */

  PIXEL_PACKED_UNSIGNED_INT_24_8,

  /**
   * 16-bit packed unsigned integers.
   */

  PIXEL_PACKED_UNSIGNED_SHORT_4444,

  /**
   * 16-bit packed unsigned integers.
   */

  PIXEL_PACKED_UNSIGNED_SHORT_5551,

  /**
   * 16-bit packed unsigned integers.
   */

  PIXEL_PACKED_UNSIGNED_SHORT_565
}
