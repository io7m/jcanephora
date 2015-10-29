/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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
 * Type-safe interface to integral scalar types.
 */

public enum JCGLScalarIntegralType
{
  /**
   * A signed 8-bit integer.
   */

  TYPE_BYTE,

  /**
   * A signed 32-bit integer.
   */

  TYPE_INT,

  /**
   * A signed 16-bit integer.
   */

  TYPE_SHORT,

  /**
   * An unsigned 8-bit integer.
   */

  TYPE_UNSIGNED_BYTE,

  /**
   * An unsigned 32-bit integer.
   */

  TYPE_UNSIGNED_INT,

  /**
   * An unsigned 16-bit integer.
   */

  TYPE_UNSIGNED_SHORT;

}
