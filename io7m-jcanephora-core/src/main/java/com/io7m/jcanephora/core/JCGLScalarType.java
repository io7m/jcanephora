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

import com.io7m.junreachable.UnreachableCodeException;

/**
 * Type-safe interface to scalar types.
 */

public enum JCGLScalarType
{
  /**
   * A signed 8-bit integer.
   */

  TYPE_BYTE,

  /**
   * A 16-bit floating-point value.
   */

  TYPE_HALF_FLOAT,

  /**
   * A 32-bit floating-point value.
   */

  TYPE_FLOAT,

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

  private static boolean shaderTypeConvertibleFloat(
    final int elements,
    final JCGLType type)
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
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_FLOAT_MATRIX_2:
      case TYPE_FLOAT_MATRIX_3:
      case TYPE_FLOAT_MATRIX_4:
      case TYPE_INTEGER:
      case TYPE_INTEGER_VECTOR_2:
      case TYPE_INTEGER_VECTOR_3:
      case TYPE_INTEGER_VECTOR_4:
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
      case TYPE_UNSIGNED_INTEGER:
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
      case TYPE_FLOAT_MATRIX_4x3:
      case TYPE_FLOAT_MATRIX_4x2:
      case TYPE_FLOAT_MATRIX_3x4:
      case TYPE_FLOAT_MATRIX_3x2:
      case TYPE_FLOAT_MATRIX_2x4:
      case TYPE_FLOAT_MATRIX_2x3:
        return false;
    }
    throw new UnreachableCodeException();
  }

  private static boolean shaderTypeConvertibleInt(
    final int elements,
    final JCGLType type)
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
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_FLOAT:
      case TYPE_FLOAT_MATRIX_2:
      case TYPE_FLOAT_MATRIX_3:
      case TYPE_FLOAT_MATRIX_4:
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
      case TYPE_UNSIGNED_INTEGER:
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
      case TYPE_FLOAT_MATRIX_4x3:
      case TYPE_FLOAT_MATRIX_4x2:
      case TYPE_FLOAT_MATRIX_3x4:
      case TYPE_FLOAT_MATRIX_3x2:
      case TYPE_FLOAT_MATRIX_2x4:
      case TYPE_FLOAT_MATRIX_2x3:
      case TYPE_FLOAT_VECTOR_2:
      case TYPE_FLOAT_VECTOR_3:
      case TYPE_FLOAT_VECTOR_4:
        return false;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param elements The number of elements.
   * @param type     The GLSL type.
   *
   * @return {@code true} iff the type described by {@code elements} elements of
   * this type are convertible to the GLSL type {@code type}. As an example,
   * {@code 4} elements of type {@code float} are convertible to the GLSL type
   * {@code vec4}, or {@code TYPE_FLOAT_VECTOR_4}. Most combinations are not
   * convertible to any GLSL type.
   */

  public boolean shaderTypeConvertible(
    final int elements,
    final JCGLType type)
  {
    switch (this) {
      case TYPE_HALF_FLOAT:
      case TYPE_FLOAT: {
        return JCGLScalarType.shaderTypeConvertibleFloat(elements, type);
      }
      case TYPE_INT: {
        return JCGLScalarType.shaderTypeConvertibleInt(elements, type);
      }
      case TYPE_BYTE:
      case TYPE_SHORT:
      case TYPE_UNSIGNED_BYTE:
      case TYPE_UNSIGNED_INT:
      case TYPE_UNSIGNED_SHORT:
        return false;
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
      case TYPE_HALF_FLOAT:
        return 2;
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
}
