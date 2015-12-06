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
 * Blend function specification.
 */

public enum JCGLBlendFunction
{
  /**
   * Multiply the color by the current constant alpha, saturating the result;
   * {@code GL_CONSTANT_ALPHA}
   */

  BLEND_CONSTANT_ALPHA,

  /**
   * Multiply the color by the current constant color, saturating the result;
   * {@code GL_CONSTANT_COLOR}
   */

  BLEND_CONSTANT_COLOR,

  /**
   * Multiply the color by the destination alpha; {@code GL_DST_ALPHA}
   */

  BLEND_DESTINATION_ALPHA,

  /**
   * Multiply the color by the destination color; {@code GL_DST_COLOR}
   */

  BLEND_DESTINATION_COLOR,

  /**
   * Multiply the color by one; {@code GL_ONE}
   */

  BLEND_ONE,

  /**
   * Multiply the color by one minus the current constant alpha, saturating the
   * result; {@code GL_ONE_MINUS_CONSTANT_ALPHA}
   */

  BLEND_ONE_MINUS_CONSTANT_ALPHA,

  /**
   * Multiply the color by one minus the current constant color, saturating the
   * result; {@code GL_ONE_MINUS_CONSTANT_COLOR}
   */

  BLEND_ONE_MINUS_CONSTANT_COLOR,

  /**
   * Multiply the color by one minus the destination alpha; {@code
   * GL_ONE_MINUS_DST_ALPHA}
   */

  BLEND_ONE_MINUS_DESTINATION_ALPHA,

  /**
   * Multiply the color by one minus the destination color; {@code
   * GL_ONE_MINUS_DST_COLOR}
   */

  BLEND_ONE_MINUS_DESTINATION_COLOR,

  /**
   * Multiply the color by one minus the source alpha; {@code
   * GL_ONE_MINUS_SRC_ALPHA}
   */

  BLEND_ONE_MINUS_SOURCE_ALPHA,

  /**
   * Multiply the color by one minus the source color; {@code
   * GL_ONE_MINUS_SRC_COLOR}
   */

  BLEND_ONE_MINUS_SOURCE_COLOR,

  /**
   * Multiply the color by the source alpha; {@code GL_SRC_ALPHA}
   */

  BLEND_SOURCE_ALPHA,

  /**
   * Multiply the color by the source alpha, saturating the result; {@code
   * GL_SRC_ALPHA}
   */

  BLEND_SOURCE_ALPHA_SATURATE,

  /**
   * Multiply the color by the source color; {@code GL_SRC_COLOR}
   */

  BLEND_SOURCE_COLOR,

  /**
   * Multiply the color by zero; {@code GL_ZERO}
   */

  BLEND_ZERO,
}
