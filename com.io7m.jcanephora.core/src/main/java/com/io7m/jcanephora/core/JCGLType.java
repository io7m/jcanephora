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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Type-safe OpenGL shader types.
 */

public enum JCGLType
{
  /**
   * GLSL boolean type.
   */

  TYPE_BOOLEAN("bool"),

  /**
   * GLSL boolean vector type.
   */

  TYPE_BOOLEAN_VECTOR_2("bvec2"),

  /**
   * GLSL boolean vector type.
   */

  TYPE_BOOLEAN_VECTOR_3("bvec3"),

  /**
   * GLSL boolean vector type.
   */

  TYPE_BOOLEAN_VECTOR_4("bvec4"),

  /**
   * GLSL floating-point type.
   */

  TYPE_FLOAT("float"),

  /**
   * GLSL 2x2 floating-point matrix type.
   */

  TYPE_FLOAT_MATRIX_2("mat2"),

  /**
   * GLSL 3x3 floating-point matrix type.
   */

  TYPE_FLOAT_MATRIX_3("mat3"),

  /**
   * GLSL 4x4 floating-point matrix type.
   */

  TYPE_FLOAT_MATRIX_4("mat4"),

  /**
   * GLSL floating-point vector type.
   */

  TYPE_FLOAT_VECTOR_2("vec2"),

  /**
   * GLSL floating-point vector type.
   */

  TYPE_FLOAT_VECTOR_3("vec3"),

  /**
   * GLSL floating-point vector type.
   */

  TYPE_FLOAT_VECTOR_4("vec4"),

  /**
   * GLSL integer type.
   */

  TYPE_INTEGER("int"),

  /**
   * GLSL integer vector type.
   */

  TYPE_INTEGER_VECTOR_2("ivec2"),

  /**
   * GLSL integer vector type.
   */

  TYPE_INTEGER_VECTOR_3("ivec3"),

  /**
   * GLSL integer vector type.
   */

  TYPE_INTEGER_VECTOR_4("ivec4"),

  /**
   * GLSL 2D sampler type.
   */

  TYPE_SAMPLER_2D("sampler2D"),

  /**
   * GLSL 3D sampler type.
   */

  TYPE_SAMPLER_3D("sampler3D"),

  /**
   * GLSL cube sampler type.
   */

  TYPE_SAMPLER_CUBE("samplerCube"),

  /**
   * GLSL unsigned integer type.
   */

  TYPE_UNSIGNED_INTEGER("uint"),

  /**
   * GLSL unsigned integer vector type.
   */

  TYPE_UNSIGNED_INTEGER_VECTOR_2("uvec2"),

  /**
   * GLSL unsigned integer vector type.
   */

  TYPE_UNSIGNED_INTEGER_VECTOR_3("uvec3"),

  /**
   * GLSL unsigned integer vector type.
   */

  TYPE_UNSIGNED_INTEGER_VECTOR_4("uvec4"),

  /**
   * GLSL 4x3 float matrix type.
   */

  TYPE_FLOAT_MATRIX_4x3("mat4x3"),

  /**
   * GLSL 4x2 float matrix type.
   */

  TYPE_FLOAT_MATRIX_4x2("mat4x2"),

  /**
   * GLSL 3x4 float matrix type.
   */

  TYPE_FLOAT_MATRIX_3x4("mat3x4"),

  /**
   * GLSL 3x2 float matrix type.
   */

  TYPE_FLOAT_MATRIX_3x2("mat3x2"),

  /**
   * GLSL 2x4 float matrix type.
   */

  TYPE_FLOAT_MATRIX_2x4("mat2x4"),

  /**
   * GLSL 2x3 float matrix type.
   */

  TYPE_FLOAT_MATRIX_2x3("mat2x3");

  private static final Map<String, JCGLType> NAMES = getNames();
  private final String name;

  JCGLType(
    final String in_name)
  {
    this.name = NullCheck.notNull(in_name, "Name");
  }

  /**
   * @param name The name of the type.
   *
   * @return The type corresponding to the given type name as it appears in a
   * GLSL program.
   *
   * @throws NoSuchElementException If the type is unknown.
   */

  public static JCGLType fromName(
    final String name)
    throws NoSuchElementException
  {
    final JCGLType r = NAMES.get(NullCheck.notNull(name, "Name"));
    if (r == null) {
      throw new NoSuchElementException(
        String.format("Unknown type '%s'", name));
    }
    return r;
  }

  private static Map<String, JCGLType> getNames()
  {
    final JCGLType[] vs = JCGLType.values();
    final Map<String, JCGLType> m = new HashMap<>(vs.length);
    for (final JCGLType t : vs) {
      m.put(t.name, t);
    }
    return m;
  }

  /**
   * @return The name of the type.
   */

  public String getName()
  {
    return this.name;
  }

  /**
   * @return {@code true} if the current type is a sampler type.
   */

  public boolean isSamplerType()
  {
    switch (this) {
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_FLOAT:
      case TYPE_FLOAT_MATRIX_2:
      case TYPE_FLOAT_MATRIX_3:
      case TYPE_FLOAT_MATRIX_4:
      case TYPE_FLOAT_VECTOR_2:
      case TYPE_FLOAT_VECTOR_3:
      case TYPE_FLOAT_VECTOR_4:
      case TYPE_INTEGER:
      case TYPE_INTEGER_VECTOR_2:
      case TYPE_INTEGER_VECTOR_3:
      case TYPE_INTEGER_VECTOR_4:
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
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
        return true;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return {@code true} if the current type is a floating point type.
   */

  public boolean isFloatingPointType()
  {
    switch (this) {
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
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
        return false;
      case TYPE_FLOAT_MATRIX_4x3:
      case TYPE_FLOAT_MATRIX_4x2:
      case TYPE_FLOAT_MATRIX_3x4:
      case TYPE_FLOAT_MATRIX_3x2:
      case TYPE_FLOAT_MATRIX_2x4:
      case TYPE_FLOAT_MATRIX_2x3:
      case TYPE_FLOAT:
      case TYPE_FLOAT_MATRIX_2:
      case TYPE_FLOAT_MATRIX_3:
      case TYPE_FLOAT_MATRIX_4:
      case TYPE_FLOAT_VECTOR_2:
      case TYPE_FLOAT_VECTOR_3:
      case TYPE_FLOAT_VECTOR_4:
        return true;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return The number of scalar elements that the current type implies
   */

  public int getElementCount()
  {
    switch (this) {
      case TYPE_INTEGER:
      case TYPE_BOOLEAN:
      case TYPE_FLOAT:
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
      case TYPE_UNSIGNED_INTEGER:
        return 1;
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_FLOAT_VECTOR_2:
      case TYPE_INTEGER_VECTOR_2:
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
        return 2;
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_FLOAT_VECTOR_3:
      case TYPE_INTEGER_VECTOR_3:
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
        return 3;
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_FLOAT_VECTOR_4:
      case TYPE_INTEGER_VECTOR_4:
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
        return 4;
      case TYPE_FLOAT_MATRIX_2:
        return 2 * 2;
      case TYPE_FLOAT_MATRIX_3:
        return 3 * 3;
      case TYPE_FLOAT_MATRIX_4:
        return 4 * 4;
      case TYPE_FLOAT_MATRIX_4x3:
        return 4 * 3;
      case TYPE_FLOAT_MATRIX_4x2:
        return 4 * 2;
      case TYPE_FLOAT_MATRIX_3x4:
        return 3 * 4;
      case TYPE_FLOAT_MATRIX_3x2:
        return 3 * 2;
      case TYPE_FLOAT_MATRIX_2x4:
        return 2 * 4;
      case TYPE_FLOAT_MATRIX_2x3:
        return 2 * 3;
    }

    throw new UnreachableCodeException();
  }
}
