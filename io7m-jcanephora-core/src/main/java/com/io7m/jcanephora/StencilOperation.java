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
 * Stencil operation specification.
 */

public enum StencilOperation
{
  /**
   * Decrements the current stencil buffer value. Clamps to 0.
   */

  STENCIL_OP_DECREMENT,

  /**
   * Decrements the current stencil buffer value. Wraps stencil buffer value
   * to the maximum representable unsigned value when decrementing a stencil
   * buffer value of zero.
   */

  STENCIL_OP_DECREMENT_WRAP,

  /**
   * Increments the current stencil buffer value. Clamps to the maximum
   * representable unsigned value.
   */

  STENCIL_OP_INCREMENT,

  /**
   * Increments the current stencil buffer value. Wraps stencil buffer value
   * to zero when incrementing the maximum representable unsigned value.
   */

  STENCIL_OP_INCREMENT_WRAP,

  /**
   * Bitwise inverts the current stencil buffer value.
   */

  STENCIL_OP_INVERT,

  /**
   * Keeps the current value.
   */

  STENCIL_OP_KEEP,

  /**
   * Sets the stencil buffer value to ref, as specified by
   * {@link com.io7m.jcanephora.api.JCGLStencilBufferType#stencilBufferFunction(FaceSelection, StencilFunction, int, int)}
   * .
   */

  STENCIL_OP_REPLACE,

  /**
   * Sets the stencil buffer value to 0.
   */

  STENCIL_OP_ZERO
}
