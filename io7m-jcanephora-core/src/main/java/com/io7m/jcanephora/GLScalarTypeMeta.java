/*
 * Copyright © 2012 http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;

/**
 * Information about scalar OpenGL types.
 */

public final class GLScalarTypeMeta
{
  /**
   * Return the size in bytes of the given type.
   */

  public static int getSizeBytes(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return 1;
      case TYPE_FLOAT:
        return 4;
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

  /**
   * Return <code>true</code> iff the type described by <code>elements</code>
   * elements of <code>element_type</code> are convertible to the GLSL type
   * <code>type</code>. As an example, <code>4</code> elements of type
   * <code>float</code> are convertible to the GLSL type <code>vec4</code>, or
   * <code>TYPE_FLOAT_VECTOR_4</code>. Most combinations are not convertible
   * to any GLSL type.
   * 
   * @param element_type
   *          The type of each element.
   * @param elements
   *          The number of elements.
   * @param type
   *          The GLSL type.
   */

  public static boolean shaderTypeConvertible(
    final @Nonnull GLScalarType element_type,
    final int elements,
    final @Nonnull GLType.Type type)
  {
    switch (element_type) {
      case TYPE_FLOAT:
      {
        switch (type) {
          case TYPE_FLOAT:
            return elements == 1;
          case TYPE_FLOAT_VECTOR_2:
            return elements == 2;
          case TYPE_FLOAT_VECTOR_3:
            return elements == 3;
          case TYPE_FLOAT_VECTOR_4:
            return elements == 4;
            // $CASES-OMITTED$
          default:
            return false;
        }
      }
      case TYPE_INT:
      {
        switch (type) {
          case TYPE_INTEGER:
            return elements == 1;
          case TYPE_INTEGER_VECTOR_2:
            return elements == 2;
          case TYPE_INTEGER_VECTOR_3:
            return elements == 3;
          case TYPE_INTEGER_VECTOR_4:
            return elements == 4;
            // $CASES-OMITTED$
          default:
            return false;
        }
      }
      // $CASES-OMITTED$
      default:
        return false;
    }
  }
}
