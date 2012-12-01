package com.io7m.jcanephora;

/**
 * Type-safe OpenGL shader types.
 */

public final class GLType
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
