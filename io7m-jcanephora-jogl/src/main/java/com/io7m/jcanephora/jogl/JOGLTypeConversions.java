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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL3;

/**
 * <p>Conversions between enumerations and OpenGL contstants.</p>
 *
 * <p>Note: This file is not part of the public API, but is exposed in order to
 * facilitate easier unit testing.</p>
 */

public final class JOGLTypeConversions
{
  private JOGLTypeConversions()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Convert types from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLUnsignedType unsignedTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_UNSIGNED_BYTE:
        return JCGLUnsignedType.TYPE_UNSIGNED_BYTE;
      case GL.GL_UNSIGNED_SHORT:
        return JCGLUnsignedType.TYPE_UNSIGNED_SHORT;
      case GL.GL_UNSIGNED_INT:
        return JCGLUnsignedType.TYPE_UNSIGNED_INT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int unsignedTypeToGL(
    final JCGLUnsignedType type)
  {
    switch (type) {
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert hints from GL constants.
   *
   * @param hint The GL constant.
   *
   * @return The value.
   */

  public static JCGLUsageHint usageHintFromGL(
    final int hint)
  {
    switch (hint) {
      case GL2ES3.GL_DYNAMIC_COPY:
        return JCGLUsageHint.USAGE_DYNAMIC_COPY;
      case GL.GL_DYNAMIC_DRAW:
        return JCGLUsageHint.USAGE_DYNAMIC_DRAW;
      case GL2ES3.GL_DYNAMIC_READ:
        return JCGLUsageHint.USAGE_DYNAMIC_READ;
      case GL2ES3.GL_STATIC_COPY:
        return JCGLUsageHint.USAGE_STATIC_COPY;
      case GL.GL_STATIC_DRAW:
        return JCGLUsageHint.USAGE_STATIC_DRAW;
      case GL2ES3.GL_STATIC_READ:
        return JCGLUsageHint.USAGE_STATIC_READ;
      case GL2ES3.GL_STREAM_COPY:
        return JCGLUsageHint.USAGE_STREAM_COPY;
      case GL2ES2.GL_STREAM_DRAW:
        return JCGLUsageHint.USAGE_STREAM_DRAW;
      case GL2ES3.GL_STREAM_READ:
        return JCGLUsageHint.USAGE_STREAM_READ;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert hints to GL constants.
   *
   * @param hint The hint.
   *
   * @return The resulting GL constant.
   */

  public static int usageHintToGL(
    final JCGLUsageHint hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_COPY:
        return GL2ES3.GL_DYNAMIC_COPY;
      case USAGE_DYNAMIC_DRAW:
        return GL.GL_DYNAMIC_DRAW;
      case USAGE_DYNAMIC_READ:
        return GL2ES3.GL_DYNAMIC_READ;
      case USAGE_STATIC_COPY:
        return GL2ES3.GL_STATIC_COPY;
      case USAGE_STATIC_DRAW:
        return GL.GL_STATIC_DRAW;
      case USAGE_STATIC_READ:
        return GL2ES3.GL_STATIC_READ;
      case USAGE_STREAM_COPY:
        return GL2ES3.GL_STREAM_COPY;
      case USAGE_STREAM_DRAW:
        return GL2ES2.GL_STREAM_DRAW;
      case USAGE_STREAM_READ:
        return GL2ES3.GL_STREAM_READ;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLScalarType scalarTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_HALF_FLOAT:
        return JCGLScalarType.TYPE_HALF_FLOAT;
      case GL.GL_BYTE:
        return JCGLScalarType.TYPE_BYTE;
      case GL.GL_UNSIGNED_BYTE:
        return JCGLScalarType.TYPE_UNSIGNED_BYTE;
      case GL.GL_SHORT:
        return JCGLScalarType.TYPE_SHORT;
      case GL.GL_UNSIGNED_SHORT:
        return JCGLScalarType.TYPE_UNSIGNED_SHORT;
      case GL2ES2.GL_INT:
        return JCGLScalarType.TYPE_INT;
      case GL.GL_UNSIGNED_INT:
        return JCGLScalarType.TYPE_UNSIGNED_INT;
      case GL.GL_FLOAT:
        return JCGLScalarType.TYPE_FLOAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int scalarTypeToGL(
    final JCGLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
      case TYPE_HALF_FLOAT:
        return GL.GL_HALF_FLOAT;
      case TYPE_FLOAT:
        return GL.GL_FLOAT;
      case TYPE_INT:
        return GL2ES2.GL_INT;
      case TYPE_SHORT:
        return GL.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int scalarIntegralTypeToGL(final JCGLScalarIntegralType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
      case TYPE_INT:
        return GL2ES2.GL_INT;
      case TYPE_SHORT:
        return GL.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLScalarIntegralType scalarIntegralTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_BYTE:
        return JCGLScalarIntegralType.TYPE_BYTE;
      case GL.GL_UNSIGNED_BYTE:
        return JCGLScalarIntegralType.TYPE_UNSIGNED_BYTE;
      case GL.GL_SHORT:
        return JCGLScalarIntegralType.TYPE_SHORT;
      case GL.GL_UNSIGNED_SHORT:
        return JCGLScalarIntegralType.TYPE_UNSIGNED_SHORT;
      case GL2ES2.GL_INT:
        return JCGLScalarIntegralType.TYPE_INT;
      case GL.GL_UNSIGNED_INT:
        return JCGLScalarIntegralType.TYPE_UNSIGNED_INT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int typeToGL(
    final JCGLType type)
  {
    switch (type) {
      case TYPE_BOOLEAN:
        return GL2ES2.GL_BOOL;
      case TYPE_BOOLEAN_VECTOR_2:
        return GL2ES2.GL_BOOL_VEC2;
      case TYPE_BOOLEAN_VECTOR_3:
        return GL2ES2.GL_BOOL_VEC3;
      case TYPE_BOOLEAN_VECTOR_4:
        return GL2ES2.GL_BOOL_VEC4;

      case TYPE_FLOAT:
        return GL.GL_FLOAT;
      case TYPE_FLOAT_MATRIX_2:
        return GL2ES2.GL_FLOAT_MAT2;
      case TYPE_FLOAT_MATRIX_3:
        return GL2ES2.GL_FLOAT_MAT3;
      case TYPE_FLOAT_MATRIX_4:
        return GL2ES2.GL_FLOAT_MAT4;
      case TYPE_FLOAT_VECTOR_2:
        return GL2ES2.GL_FLOAT_VEC2;
      case TYPE_FLOAT_VECTOR_3:
        return GL2ES2.GL_FLOAT_VEC3;
      case TYPE_FLOAT_VECTOR_4:
        return GL2ES2.GL_FLOAT_VEC4;

      case TYPE_INTEGER:
        return GL2ES2.GL_INT;
      case TYPE_INTEGER_VECTOR_2:
        return GL2ES2.GL_INT_VEC2;
      case TYPE_INTEGER_VECTOR_3:
        return GL2ES2.GL_INT_VEC3;
      case TYPE_INTEGER_VECTOR_4:
        return GL2ES2.GL_INT_VEC4;

      case TYPE_SAMPLER_2D:
        return GL2ES2.GL_SAMPLER_2D;
      case TYPE_SAMPLER_3D:
        return GL2ES2.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL2ES2.GL_SAMPLER_CUBE;

      case TYPE_UNSIGNED_INTEGER:
        return GL3.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
        return GL3.GL_UNSIGNED_INT_VEC2;
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
        return GL3.GL_UNSIGNED_INT_VEC3;
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
        return GL3.GL_UNSIGNED_INT_VEC4;

      case TYPE_FLOAT_MATRIX_4x3:
        return GL3.GL_FLOAT_MAT4x3;
      case TYPE_FLOAT_MATRIX_4x2:
        return GL3.GL_FLOAT_MAT4x2;

      case TYPE_FLOAT_MATRIX_3x4:
        return GL3.GL_FLOAT_MAT3x4;
      case TYPE_FLOAT_MATRIX_3x2:
        return GL3.GL_FLOAT_MAT3x2;

      case TYPE_FLOAT_MATRIX_2x4:
        return GL3.GL_FLOAT_MAT2x4;
      case TYPE_FLOAT_MATRIX_2x3:
        return GL3.GL_FLOAT_MAT2x3;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLType typeFromGL(
    final int type)
  {
    switch (type) {
      case GL2ES2.GL_BOOL:
        return JCGLType.TYPE_BOOLEAN;
      case GL2ES2.GL_BOOL_VEC2:
        return JCGLType.TYPE_BOOLEAN_VECTOR_2;
      case GL2ES2.GL_BOOL_VEC3:
        return JCGLType.TYPE_BOOLEAN_VECTOR_3;
      case GL2ES2.GL_BOOL_VEC4:
        return JCGLType.TYPE_BOOLEAN_VECTOR_4;

      case GL.GL_FLOAT:
        return JCGLType.TYPE_FLOAT;
      case GL2ES2.GL_FLOAT_MAT2:
        return JCGLType.TYPE_FLOAT_MATRIX_2;
      case GL2ES2.GL_FLOAT_MAT3:
        return JCGLType.TYPE_FLOAT_MATRIX_3;
      case GL2ES2.GL_FLOAT_MAT4:
        return JCGLType.TYPE_FLOAT_MATRIX_4;

      case GL3.GL_FLOAT_MAT4x3:
        return JCGLType.TYPE_FLOAT_MATRIX_4x3;
      case GL3.GL_FLOAT_MAT4x2:
        return JCGLType.TYPE_FLOAT_MATRIX_4x2;

      case GL3.GL_FLOAT_MAT3x4:
        return JCGLType.TYPE_FLOAT_MATRIX_3x4;
      case GL3.GL_FLOAT_MAT3x2:
        return JCGLType.TYPE_FLOAT_MATRIX_3x2;

      case GL3.GL_FLOAT_MAT2x4:
        return JCGLType.TYPE_FLOAT_MATRIX_2x4;
      case GL3.GL_FLOAT_MAT2x3:
        return JCGLType.TYPE_FLOAT_MATRIX_2x3;

      case GL2ES2.GL_FLOAT_VEC2:
        return JCGLType.TYPE_FLOAT_VECTOR_2;
      case GL2ES2.GL_FLOAT_VEC3:
        return JCGLType.TYPE_FLOAT_VECTOR_3;
      case GL2ES2.GL_FLOAT_VEC4:
        return JCGLType.TYPE_FLOAT_VECTOR_4;

      case GL2ES2.GL_INT:
        return JCGLType.TYPE_INTEGER;
      case GL2ES2.GL_INT_VEC2:
        return JCGLType.TYPE_INTEGER_VECTOR_2;
      case GL2ES2.GL_INT_VEC3:
        return JCGLType.TYPE_INTEGER_VECTOR_3;
      case GL2ES2.GL_INT_VEC4:
        return JCGLType.TYPE_INTEGER_VECTOR_4;

      case GL2ES2.GL_UNSIGNED_INT:
        return JCGLType.TYPE_UNSIGNED_INTEGER;
      case GL3.GL_UNSIGNED_INT_VEC2:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2;
      case GL3.GL_UNSIGNED_INT_VEC3:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3;
      case GL3.GL_UNSIGNED_INT_VEC4:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4;

      case GL2ES2.GL_SAMPLER_2D:
        return JCGLType.TYPE_SAMPLER_2D;
      case GL2ES2.GL_SAMPLER_3D:
        return JCGLType.TYPE_SAMPLER_3D;
      case GL2ES2.GL_SAMPLER_CUBE:
        return JCGLType.TYPE_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert primitives from GL constants.
   *
   * @param code The GL constant.
   *
   * @return The value.
   */

  public static JCGLPrimitives primitiveFromGL(
    final int code)
  {
    switch (code) {
      case GL.GL_LINES:
        return JCGLPrimitives.PRIMITIVE_LINES;
      case GL.GL_LINE_LOOP:
        return JCGLPrimitives.PRIMITIVE_LINE_LOOP;
      case GL.GL_POINTS:
        return JCGLPrimitives.PRIMITIVE_POINTS;
      case GL.GL_TRIANGLES:
        return JCGLPrimitives.PRIMITIVE_TRIANGLES;
      case GL.GL_TRIANGLE_STRIP:
        return JCGLPrimitives.PRIMITIVE_TRIANGLE_STRIP;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert primitives to GL constants.
   *
   * @param p The primitives.
   *
   * @return The resulting GL constant.
   */

  public static int primitiveToGL(
    final JCGLPrimitives p)
  {
    switch (p) {
      case PRIMITIVE_LINES:
        return GL.GL_LINES;
      case PRIMITIVE_LINE_LOOP:
        return GL.GL_LINE_LOOP;
      case PRIMITIVE_TRIANGLES:
        return GL.GL_TRIANGLES;
      case PRIMITIVE_TRIANGLE_STRIP:
        return GL.GL_TRIANGLE_STRIP;
      case PRIMITIVE_POINTS:
        return GL.GL_POINTS;
    }

    throw new UnreachableCodeException();
  }

}
