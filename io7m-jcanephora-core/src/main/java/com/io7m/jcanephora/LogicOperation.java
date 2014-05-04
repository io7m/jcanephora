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
 * OpenGL pixel logic operations. Each operating is described in terms of an
 * incoming source color <code>s</code> and a destination color <code>d</code>
 * representing the color already in the framebuffer.
 */

public enum LogicOperation
{
  /**
   * The resulting color is <code>s & d</code>.
   */

  LOGIC_AND,

  /**
   * The resulting color is <code>(~s) & d</code>.
   */

  LOGIC_AND_INVERTED,

  /**
   * The resulting color is <code>s & (~d)</code>.
   */

  LOGIC_AND_REVERSE,

  /**
   * The resulting color is <code>0</code>.
   */

  LOGIC_CLEAR,

  /**
   * The resulting color is <code>s</code>.
   */

  LOGIC_COPY,

  /**
   * The resulting color is <code>~s</code>.
   */

  LOGIC_COPY_INVERTED,

  /**
   * The resulting color is <code>~(s ^ d)</code>.
   */

  LOGIC_EQUIV,

  /**
   * The resulting color is <code>~d</code>.
   */

  LOGIC_INVERT,

  /**
   * The resulting color is <code>~(s & d)</code>.
   */

  LOGIC_NAND,

  /**
   * The resulting color is <code>d</code>.
   */

  LOGIC_NO_OP,

  /**
   * The resulting color is <code>~(s | d)</code>.
   */

  LOGIC_NOR,

  /**
   * The resulting color is <code>s | d</code>.
   */

  LOGIC_OR,

  /**
   * The resulting color is <code>(~s) | d</code>.
   */

  LOGIC_OR_INVERTED,

  /**
   * The resulting color is <code>s | (~d)</code>.
   */

  LOGIC_OR_REVERSE,

  /**
   * The resulting color is <code>1</code>.
   */

  LOGIC_SET,

  /**
   * The resulting color is <code>s ^ d</code>.
   */

  LOGIC_XOR
}
