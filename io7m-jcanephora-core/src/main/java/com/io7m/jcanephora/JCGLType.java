/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
 * Type-safe OpenGL shader types.
 */

public enum JCGLType
{
  TYPE_FLOAT("float"),
  TYPE_FLOAT_VECTOR_2("vec2"),
  TYPE_FLOAT_VECTOR_3("vec3"),
  TYPE_FLOAT_VECTOR_4("vec4"),
  TYPE_INTEGER("int"),
  TYPE_INTEGER_VECTOR_2("ivec2"),
  TYPE_INTEGER_VECTOR_3("ivec3"),
  TYPE_INTEGER_VECTOR_4("ivec4"),
  TYPE_BOOLEAN("bool"),
  TYPE_BOOLEAN_VECTOR_2("bvec2"),
  TYPE_BOOLEAN_VECTOR_3("bvec3"),
  TYPE_BOOLEAN_VECTOR_4("bvec4"),
  TYPE_FLOAT_MATRIX_2("mat2"),
  TYPE_FLOAT_MATRIX_3("mat3"),
  TYPE_FLOAT_MATRIX_4("mat4"),
  TYPE_SAMPLER_2D("sampler2D"),
  TYPE_SAMPLER_3D("sampler3D"),
  TYPE_SAMPLER_CUBE("samplerCube"),
  TYPE_SAMPLER_2D_SHADOW("sampler2DShadow");

  private final @Nonnull String name;

  private JCGLType(
    final @Nonnull String name)
  {
    this.name = name;
  }

  public @Nonnull String getName()
  {
    return this.name;
  }

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
        return false;
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_2D_SHADOW:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
        return true;
    }

    throw new UnreachableCodeException();
  }
}
