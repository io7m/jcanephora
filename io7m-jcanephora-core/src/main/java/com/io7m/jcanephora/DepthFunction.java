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
 * Depth function specification.
 */

public enum DepthFunction
{
  /**
   * The always-true depth function; <code>GL_ALWAYS</code>
   */

  DEPTH_ALWAYS,

  /**
   * True if the current depth value is equal to the incoming depth value;
   * <code>GL_EQUAL</code>
   */

  DEPTH_EQUAL,

  /**
   * True if the current depth value is greater than the incoming depth value;
   * <code>GL_GREATER</code>
   */

  DEPTH_GREATER_THAN,

  /**
   * True if the current depth value is greater than or equal to the incoming
   * depth value; <code>GL_GEQUAL</code>
   */

  DEPTH_GREATER_THAN_OR_EQUAL,

  /**
   * True if the current depth value is less than the incoming depth value;
   * <code>GL_LESS</code>
   */

  DEPTH_LESS_THAN,

  /**
   * True if the current depth value is less than or equal to the incoming
   * depth value; <code>GL_LEQUAL</code>
   */

  DEPTH_LESS_THAN_OR_EQUAL,

  /**
   * Never true; <code>GL_NEVER</code>
   */

  DEPTH_NEVER,

  /**
   * True if the current depth value is not equal to the incoming depth value;
   * <code>GL_NOTEQUAL</code>
   */

  DEPTH_NOT_EQUAL
}
