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

import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

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

  /**
   * Convert the given type to a scalar integral type.
   *
   * @param s The scalar type
   *
   * @return An integral type
   *
   * @throws IllegalArgumentException Iff the given type is not integral
   * @since 0.53.1
   */

  public static JCGLScalarIntegralType fromScalar(
    final JCGLScalarType s)
    throws IllegalArgumentException
  {
    NullCheck.notNull(s, "Type");
    switch (s) {
      case TYPE_HALF_FLOAT:
      case TYPE_FLOAT:
        throw new IllegalArgumentException(
          "Cannot convert " + s + " to an integral type");
      case TYPE_BYTE:
        return TYPE_BYTE;
      case TYPE_INT:
        return TYPE_INT;
      case TYPE_SHORT:
        return TYPE_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return TYPE_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return TYPE_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return TYPE_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert the given type to a scalar integral type.
   *
   * @param s The unsigned type
   *
   * @return An integral type
   *
   * @since 0.53.1
   */

  public static JCGLScalarIntegralType fromScalarUnsigned(
    final JCGLUnsignedType s)
  {
    NullCheck.notNull(s, "Type");
    switch (s) {
      case TYPE_UNSIGNED_BYTE:
        return TYPE_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return TYPE_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return TYPE_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return The size in bytes of values of the current type.
   *
   * @since 0.53.0
   */

  public int getSizeBytes()
  {
    switch (this) {
      case TYPE_BYTE:
        return 1;
      case TYPE_INT:
        return 4;
      case TYPE_SHORT:
        return 2;
      case TYPE_UNSIGNED_BYTE:
        return 1;
      case TYPE_UNSIGNED_INT:
        return 4;
      case TYPE_UNSIGNED_SHORT:
        return 2;
    }

    throw new UnreachableCodeException();
  }
}
