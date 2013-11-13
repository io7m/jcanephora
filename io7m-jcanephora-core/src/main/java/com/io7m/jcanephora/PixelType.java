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

/**
 * <p>
 * The type of pixel data in client memory.
 * </p>
 */

public enum PixelType
{
  PIXEL_COMPONENT_BYTE,
  PIXEL_COMPONENT_SHORT,
  PIXEL_COMPONENT_INT,
  PIXEL_COMPONENT_UNSIGNED_BYTE,
  PIXEL_COMPONENT_UNSIGNED_SHORT,
  PIXEL_COMPONENT_UNSIGNED_INT,
  PIXEL_COMPONENT_FLOAT,
  PIXEL_COMPONENT_HALF_FLOAT,
  PIXEL_PACKED_UNSIGNED_SHORT_565,
  PIXEL_PACKED_UNSIGNED_SHORT_4444,
  PIXEL_PACKED_UNSIGNED_SHORT_5551,
  PIXEL_PACKED_UNSIGNED_INT_1010102
}