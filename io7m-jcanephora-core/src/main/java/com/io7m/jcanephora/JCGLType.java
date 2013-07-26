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

/**
 * Type-safe OpenGL shader types.
 */

public final class JCGLType
{
  /**
   * The common set of types supported by GLSL 1.2 and the shading language in
   * ES2.
   */

  public static enum Type
  {
    TYPE_FLOAT,
    TYPE_FLOAT_VECTOR_2,
    TYPE_FLOAT_VECTOR_3,
    TYPE_FLOAT_VECTOR_4,
    TYPE_INTEGER,
    TYPE_INTEGER_VECTOR_2,
    TYPE_INTEGER_VECTOR_3,
    TYPE_INTEGER_VECTOR_4,
    TYPE_BOOLEAN,
    TYPE_BOOLEAN_VECTOR_2,
    TYPE_BOOLEAN_VECTOR_3,
    TYPE_BOOLEAN_VECTOR_4,
    TYPE_FLOAT_MATRIX_2,
    TYPE_FLOAT_MATRIX_3,
    TYPE_FLOAT_MATRIX_4,
    TYPE_SAMPLER_2D,
    TYPE_SAMPLER_3D,
    TYPE_SAMPLER_CUBE,
    TYPE_SAMPLER_2D_SHADOW
  }
}
