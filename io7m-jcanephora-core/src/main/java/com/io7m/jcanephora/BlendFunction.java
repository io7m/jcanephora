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
 * Blend function specification.
 */

public enum BlendFunction
{
  /**
   * Multiply the colour by the current constant alpha, saturating the result;
   * <code>GL_CONSTANT_ALPHA</code>
   */

  BLEND_CONSTANT_ALPHA,

  /**
   * Multiply the colour by the current constant colour, saturating the
   * result; <code>GL_CONSTANT_COLOR</code>
   */

  BLEND_CONSTANT_COLOR,

  /**
   * Multiply the colour by the destination alpha; <code>GL_DST_ALPHA</code>
   */

  BLEND_DESTINATION_ALPHA,

  /**
   * Multiply the colour by the destination colour; <code>GL_DST_COLOR</code>
   */

  BLEND_DESTINATION_COLOR,

  /**
   * Multiply the colour by one; <code>GL_ONE</code>
   */

  BLEND_ONE,

  /**
   * Multiply the colour by one minus the current constant alpha, saturating
   * the result; <code>GL_ONE_MINUS_CONSTANT_ALPHA</code>
   */

  BLEND_ONE_MINUS_CONSTANT_ALPHA,

  /**
   * Multiply the colour by one minus the current constant colour, saturating
   * the result; <code>GL_ONE_MINUS_CONSTANT_COLOR</code>
   */

  BLEND_ONE_MINUS_CONSTANT_COLOR,

  /**
   * Multiply the colour by one minus the destination alpha;
   * <code>GL_ONE_MINUS_DST_ALPHA</code>
   */

  BLEND_ONE_MINUS_DESTINATION_ALPHA,

  /**
   * Multiply the colour by one minus the destination colour;
   * <code>GL_ONE_MINUS_DST_COLOR</code>
   */

  BLEND_ONE_MINUS_DESTINATION_COLOR,

  /**
   * Multiply the colour by one minus the source alpha;
   * <code>GL_ONE_MINUS_SRC_ALPHA</code>
   */

  BLEND_ONE_MINUS_SOURCE_ALPHA,

  /**
   * Multiply the colour by one minus the source colour;
   * <code>GL_ONE_MINUS_SRC_COLOR</code>
   */

  BLEND_ONE_MINUS_SOURCE_COLOR,

  /**
   * Multiply the colour by the source alpha; <code>GL_SRC_ALPHA</code>
   */

  BLEND_SOURCE_ALPHA,

  /**
   * Multiply the colour by the source alpha, saturating the result;
   * <code>GL_SRC_ALPHA</code>
   */

  BLEND_SOURCE_ALPHA_SATURATE,

  /**
   * Multiply the colour by the source colour; <code>GL_SRC_COLOR</code>
   */

  BLEND_SOURCE_COLOR,

  /**
   * Multiply the colour by zero; <code>GL_ZERO</code>
   */

  BLEND_ZERO,
}
