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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.JCGLFramebufferBlitBuffer;
import com.io7m.jcanephora.core.JCGLFramebufferBlitFilter;
import com.io7m.jcanephora.core.JCGLFramebufferStatus;
import com.io7m.jcanephora.core.JCGLPixelFormat;
import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.junreachable.UnreachableCodeException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengles.GLES20;

import java.util.Set;

/**
 * <p>Conversions between enumerations and OpenGL contstants.</p>
 *
 * <p>Note: This file is not part of the public API, but is exposed in order to
 * facilitate easier unit testing.</p>
 */

public final class LWJGL3TypeConversions
{
  private LWJGL3TypeConversions()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Convert blend functions from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLBlendFunction blendFunctionFromGL(
    final int type)
  {
    switch (type) {
      case GL14.GL_CONSTANT_ALPHA:
        return JCGLBlendFunction.BLEND_CONSTANT_ALPHA;
      case GL14.GL_CONSTANT_COLOR:
        return JCGLBlendFunction.BLEND_CONSTANT_COLOR;
      case GL11.GL_DST_ALPHA:
        return JCGLBlendFunction.BLEND_DESTINATION_ALPHA;
      case GL11.GL_DST_COLOR:
        return JCGLBlendFunction.BLEND_DESTINATION_COLOR;
      case GL11.GL_ONE:
        return JCGLBlendFunction.BLEND_ONE;
      case GL14.GL_ONE_MINUS_CONSTANT_ALPHA:
        return JCGLBlendFunction.BLEND_ONE_MINUS_CONSTANT_ALPHA;
      case GL14.GL_ONE_MINUS_CONSTANT_COLOR:
        return JCGLBlendFunction.BLEND_ONE_MINUS_CONSTANT_COLOR;
      case GL11.GL_ONE_MINUS_DST_ALPHA:
        return JCGLBlendFunction.BLEND_ONE_MINUS_DESTINATION_ALPHA;
      case GL11.GL_ONE_MINUS_DST_COLOR:
        return JCGLBlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR;
      case GL11.GL_ONE_MINUS_SRC_ALPHA:
        return JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA;
      case GL11.GL_ONE_MINUS_SRC_COLOR:
        return JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_COLOR;
      case GL11.GL_SRC_ALPHA:
        return JCGLBlendFunction.BLEND_SOURCE_ALPHA;
      case GL11.GL_SRC_COLOR:
        return JCGLBlendFunction.BLEND_SOURCE_COLOR;
      case GL11.GL_SRC_ALPHA_SATURATE:
        return JCGLBlendFunction.BLEND_SOURCE_ALPHA_SATURATE;
      case GL11.GL_ZERO:
        return JCGLBlendFunction.BLEND_ZERO;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert blend functions to GL constants.
   *
   * @param function The function
   *
   * @return The resulting GL constant
   */

  public static int blendFunctionToGL(
    final JCGLBlendFunction function)
  {
    switch (function) {
      case BLEND_CONSTANT_ALPHA:
        return GL14.GL_CONSTANT_ALPHA;
      case BLEND_CONSTANT_COLOR:
        return GL14.GL_CONSTANT_COLOR;
      case BLEND_DESTINATION_ALPHA:
        return GL11.GL_DST_ALPHA;
      case BLEND_DESTINATION_COLOR:
        return GL11.GL_DST_COLOR;
      case BLEND_ONE:
        return GL11.GL_ONE;
      case BLEND_ONE_MINUS_CONSTANT_ALPHA:
        return GL14.GL_ONE_MINUS_CONSTANT_ALPHA;
      case BLEND_ONE_MINUS_CONSTANT_COLOR:
        return GL14.GL_ONE_MINUS_CONSTANT_COLOR;
      case BLEND_ONE_MINUS_DESTINATION_ALPHA:
        return GL11.GL_ONE_MINUS_DST_ALPHA;
      case BLEND_ONE_MINUS_DESTINATION_COLOR:
        return GL11.GL_ONE_MINUS_DST_COLOR;
      case BLEND_ONE_MINUS_SOURCE_ALPHA:
        return GL11.GL_ONE_MINUS_SRC_ALPHA;
      case BLEND_ONE_MINUS_SOURCE_COLOR:
        return GL11.GL_ONE_MINUS_SRC_COLOR;
      case BLEND_SOURCE_ALPHA:
        return GL11.GL_SRC_ALPHA;
      case BLEND_SOURCE_ALPHA_SATURATE:
        return GL11.GL_SRC_ALPHA_SATURATE;
      case BLEND_SOURCE_COLOR:
        return GL11.GL_SRC_COLOR;
      case BLEND_ZERO:
        return GL11.GL_ZERO;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert polygon modes from GL constants.
   *
   * @param g The GL constant.
   *
   * @return The value.
   */

  public static JCGLPolygonMode polygonModeFromGL(
    final int g)
  {
    switch (g) {
      case GL11.GL_FILL:
        return JCGLPolygonMode.POLYGON_FILL;
      case GL11.GL_LINE:
        return JCGLPolygonMode.POLYGON_LINES;
      case GL11.GL_POINT:
        return JCGLPolygonMode.POLYGON_POINTS;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert polygon modes to GL constants.
   *
   * @param g The mode.
   *
   * @return The resulting GL constant.
   */

  public static int polygonModeToGL(
    final JCGLPolygonMode g)
  {
    switch (g) {
      case POLYGON_FILL:
        return GL11.GL_FILL;
      case POLYGON_LINES:
        return GL11.GL_LINE;
      case POLYGON_POINTS:
        return GL11.GL_POINT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert blend equations from GL constants.
   *
   * @param e The GL constant
   *
   * @return The value
   */

  public static JCGLBlendEquation blendEquationFromGL(
    final int e)
  {
    switch (e) {
      case GL14.GL_FUNC_ADD:
        return JCGLBlendEquation.BLEND_EQUATION_ADD;
      case GL14.GL_MAX:
        return JCGLBlendEquation.BLEND_EQUATION_MAXIMUM;
      case GL14.GL_MIN:
        return JCGLBlendEquation.BLEND_EQUATION_MINIMUM;
      case GL14.GL_FUNC_REVERSE_SUBTRACT:
        return JCGLBlendEquation.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL14.GL_FUNC_SUBTRACT:
        return JCGLBlendEquation.BLEND_EQUATION_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert blend equations to GL constants.
   *
   * @param e The equation.
   *
   * @return The resulting GL constant.
   */

  public static int blendEquationToGL(
    final JCGLBlendEquation e)
  {
    switch (e) {
      case BLEND_EQUATION_ADD:
        return GL14.GL_FUNC_ADD;
      case BLEND_EQUATION_MAXIMUM:
        return GL14.GL_MAX;
      case BLEND_EQUATION_MINIMUM:
        return GL14.GL_MIN;
      case BLEND_EQUATION_REVERSE_SUBTRACT:
        return GL14.GL_FUNC_REVERSE_SUBTRACT;
      case BLEND_EQUATION_SUBTRACT:
        return GL14.GL_FUNC_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert depth functions from GL constants.
   *
   * @param d The GL constant.
   *
   * @return The value.
   */

  public static JCGLDepthFunction depthFunctionFromGL(
    final int d)
  {
    switch (d) {
      case GL11.GL_ALWAYS:
        return JCGLDepthFunction.DEPTH_ALWAYS;
      case GL11.GL_EQUAL:
        return JCGLDepthFunction.DEPTH_EQUAL;
      case GL11.GL_GREATER:
        return JCGLDepthFunction.DEPTH_GREATER_THAN;
      case GL11.GL_GEQUAL:
        return JCGLDepthFunction.DEPTH_GREATER_THAN_OR_EQUAL;
      case GL11.GL_LESS:
        return JCGLDepthFunction.DEPTH_LESS_THAN;
      case GL11.GL_LEQUAL:
        return JCGLDepthFunction.DEPTH_LESS_THAN_OR_EQUAL;
      case GL11.GL_NEVER:
        return JCGLDepthFunction.DEPTH_NEVER;
      case GL11.GL_NOTEQUAL:
        return JCGLDepthFunction.DEPTH_NOT_EQUAL;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert depth functions to GL constants.
   *
   * @param d The function.
   *
   * @return The resulting GL constant.
   */

  public static int depthFunctionToGL(
    final JCGLDepthFunction d)
  {
    switch (d) {
      case DEPTH_ALWAYS:
        return GL11.GL_ALWAYS;
      case DEPTH_EQUAL:
        return GL11.GL_EQUAL;
      case DEPTH_GREATER_THAN:
        return GL11.GL_GREATER;
      case DEPTH_GREATER_THAN_OR_EQUAL:
        return GL11.GL_GEQUAL;
      case DEPTH_LESS_THAN:
        return GL11.GL_LESS;
      case DEPTH_LESS_THAN_OR_EQUAL:
        return GL11.GL_LEQUAL;
      case DEPTH_NEVER:
        return GL11.GL_NEVER;
      case DEPTH_NOT_EQUAL:
        return GL11.GL_NOTEQUAL;
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

  public static JCGLUnsignedType unsignedTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL11.GL_UNSIGNED_BYTE:
        return JCGLUnsignedType.TYPE_UNSIGNED_BYTE;
      case GL11.GL_UNSIGNED_SHORT:
        return JCGLUnsignedType.TYPE_UNSIGNED_SHORT;
      case GL11.GL_UNSIGNED_INT:
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
        return GL11.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_SHORT:
        return GL11.GL_UNSIGNED_SHORT;
      case TYPE_UNSIGNED_INT:
        return GL11.GL_UNSIGNED_INT;
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
      case GL15.GL_DYNAMIC_COPY:
        return JCGLUsageHint.USAGE_DYNAMIC_COPY;
      case GL15.GL_DYNAMIC_DRAW:
        return JCGLUsageHint.USAGE_DYNAMIC_DRAW;
      case GL15.GL_DYNAMIC_READ:
        return JCGLUsageHint.USAGE_DYNAMIC_READ;
      case GL15.GL_STATIC_COPY:
        return JCGLUsageHint.USAGE_STATIC_COPY;
      case GL15.GL_STATIC_DRAW:
        return JCGLUsageHint.USAGE_STATIC_DRAW;
      case GL15.GL_STATIC_READ:
        return JCGLUsageHint.USAGE_STATIC_READ;
      case GL15.GL_STREAM_COPY:
        return JCGLUsageHint.USAGE_STREAM_COPY;
      case GL15.GL_STREAM_DRAW:
        return JCGLUsageHint.USAGE_STREAM_DRAW;
      case GL15.GL_STREAM_READ:
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
        return GL15.GL_DYNAMIC_COPY;
      case USAGE_DYNAMIC_DRAW:
        return GL15.GL_DYNAMIC_DRAW;
      case USAGE_DYNAMIC_READ:
        return GL15.GL_DYNAMIC_READ;
      case USAGE_STATIC_COPY:
        return GL15.GL_STATIC_COPY;
      case USAGE_STATIC_DRAW:
        return GL15.GL_STATIC_DRAW;
      case USAGE_STATIC_READ:
        return GL15.GL_STATIC_READ;
      case USAGE_STREAM_COPY:
        return GL15.GL_STREAM_COPY;
      case USAGE_STREAM_DRAW:
        return GL15.GL_STREAM_DRAW;
      case USAGE_STREAM_READ:
        return GL15.GL_STREAM_READ;
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
      case GL30.GL_HALF_FLOAT:
        return JCGLScalarType.TYPE_HALF_FLOAT;
      case GL11.GL_BYTE:
        return JCGLScalarType.TYPE_BYTE;
      case GL11.GL_UNSIGNED_BYTE:
        return JCGLScalarType.TYPE_UNSIGNED_BYTE;
      case GL11.GL_SHORT:
        return JCGLScalarType.TYPE_SHORT;
      case GL11.GL_UNSIGNED_SHORT:
        return JCGLScalarType.TYPE_UNSIGNED_SHORT;
      case GL11.GL_INT:
        return JCGLScalarType.TYPE_INT;
      case GL11.GL_UNSIGNED_INT:
        return JCGLScalarType.TYPE_UNSIGNED_INT;
      case GL11.GL_FLOAT:
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
        return GL11.GL_BYTE;
      case TYPE_HALF_FLOAT:
        return GL30.GL_HALF_FLOAT;
      case TYPE_FLOAT:
        return GL11.GL_FLOAT;
      case TYPE_INT:
        return GL11.GL_INT;
      case TYPE_SHORT:
        return GL11.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL11.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL11.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL11.GL_UNSIGNED_SHORT;
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
        return GL11.GL_BYTE;
      case TYPE_INT:
        return GL11.GL_INT;
      case TYPE_SHORT:
        return GL11.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL11.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL11.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL11.GL_UNSIGNED_SHORT;
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
      case GL11.GL_BYTE:
        return JCGLScalarIntegralType.TYPE_BYTE;
      case GL11.GL_UNSIGNED_BYTE:
        return JCGLScalarIntegralType.TYPE_UNSIGNED_BYTE;
      case GL11.GL_SHORT:
        return JCGLScalarIntegralType.TYPE_SHORT;
      case GL11.GL_UNSIGNED_SHORT:
        return JCGLScalarIntegralType.TYPE_UNSIGNED_SHORT;
      case GL11.GL_INT:
        return JCGLScalarIntegralType.TYPE_INT;
      case GL11.GL_UNSIGNED_INT:
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
        return GL20.GL_BOOL;
      case TYPE_BOOLEAN_VECTOR_2:
        return GL20.GL_BOOL_VEC2;
      case TYPE_BOOLEAN_VECTOR_3:
        return GL20.GL_BOOL_VEC3;
      case TYPE_BOOLEAN_VECTOR_4:
        return GL20.GL_BOOL_VEC4;

      case TYPE_FLOAT:
        return GL11.GL_FLOAT;
      case TYPE_FLOAT_MATRIX_2:
        return GL20.GL_FLOAT_MAT2;
      case TYPE_FLOAT_MATRIX_3:
        return GL20.GL_FLOAT_MAT3;
      case TYPE_FLOAT_MATRIX_4:
        return GL20.GL_FLOAT_MAT4;
      case TYPE_FLOAT_VECTOR_2:
        return GL20.GL_FLOAT_VEC2;
      case TYPE_FLOAT_VECTOR_3:
        return GL20.GL_FLOAT_VEC3;
      case TYPE_FLOAT_VECTOR_4:
        return GL20.GL_FLOAT_VEC4;

      case TYPE_INTEGER:
        return GL11.GL_INT;
      case TYPE_INTEGER_VECTOR_2:
        return GL20.GL_INT_VEC2;
      case TYPE_INTEGER_VECTOR_3:
        return GL20.GL_INT_VEC3;
      case TYPE_INTEGER_VECTOR_4:
        return GL20.GL_INT_VEC4;

      case TYPE_SAMPLER_2D:
        return GL20.GL_SAMPLER_2D;
      case TYPE_SAMPLER_3D:
        return GL20.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL20.GL_SAMPLER_CUBE;

      case TYPE_UNSIGNED_INTEGER:
        return GL11.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
        return GL30.GL_UNSIGNED_INT_VEC2;
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
        return GL30.GL_UNSIGNED_INT_VEC3;
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
        return GL30.GL_UNSIGNED_INT_VEC4;

      case TYPE_FLOAT_MATRIX_4x3:
        return GL21.GL_FLOAT_MAT4x3;
      case TYPE_FLOAT_MATRIX_4x2:
        return GL21.GL_FLOAT_MAT4x2;

      case TYPE_FLOAT_MATRIX_3x4:
        return GL21.GL_FLOAT_MAT3x4;
      case TYPE_FLOAT_MATRIX_3x2:
        return GL21.GL_FLOAT_MAT3x2;

      case TYPE_FLOAT_MATRIX_2x4:
        return GL21.GL_FLOAT_MAT2x4;
      case TYPE_FLOAT_MATRIX_2x3:
        return GL21.GL_FLOAT_MAT2x3;
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
      case GL20.GL_BOOL:
        return JCGLType.TYPE_BOOLEAN;
      case GL20.GL_BOOL_VEC2:
        return JCGLType.TYPE_BOOLEAN_VECTOR_2;
      case GL20.GL_BOOL_VEC3:
        return JCGLType.TYPE_BOOLEAN_VECTOR_3;
      case GL20.GL_BOOL_VEC4:
        return JCGLType.TYPE_BOOLEAN_VECTOR_4;

      case GL11.GL_FLOAT:
        return JCGLType.TYPE_FLOAT;
      case GL20.GL_FLOAT_MAT2:
        return JCGLType.TYPE_FLOAT_MATRIX_2;
      case GL20.GL_FLOAT_MAT3:
        return JCGLType.TYPE_FLOAT_MATRIX_3;
      case GL20.GL_FLOAT_MAT4:
        return JCGLType.TYPE_FLOAT_MATRIX_4;

      case GL21.GL_FLOAT_MAT4x3:
        return JCGLType.TYPE_FLOAT_MATRIX_4x3;
      case GL21.GL_FLOAT_MAT4x2:
        return JCGLType.TYPE_FLOAT_MATRIX_4x2;

      case GL21.GL_FLOAT_MAT3x4:
        return JCGLType.TYPE_FLOAT_MATRIX_3x4;
      case GL21.GL_FLOAT_MAT3x2:
        return JCGLType.TYPE_FLOAT_MATRIX_3x2;

      case GL21.GL_FLOAT_MAT2x4:
        return JCGLType.TYPE_FLOAT_MATRIX_2x4;
      case GL21.GL_FLOAT_MAT2x3:
        return JCGLType.TYPE_FLOAT_MATRIX_2x3;

      case GL20.GL_FLOAT_VEC2:
        return JCGLType.TYPE_FLOAT_VECTOR_2;
      case GL20.GL_FLOAT_VEC3:
        return JCGLType.TYPE_FLOAT_VECTOR_3;
      case GL20.GL_FLOAT_VEC4:
        return JCGLType.TYPE_FLOAT_VECTOR_4;

      case GL11.GL_INT:
        return JCGLType.TYPE_INTEGER;
      case GL20.GL_INT_VEC2:
        return JCGLType.TYPE_INTEGER_VECTOR_2;
      case GL20.GL_INT_VEC3:
        return JCGLType.TYPE_INTEGER_VECTOR_3;
      case GL20.GL_INT_VEC4:
        return JCGLType.TYPE_INTEGER_VECTOR_4;

      case GL11.GL_UNSIGNED_INT:
        return JCGLType.TYPE_UNSIGNED_INTEGER;
      case GL30.GL_UNSIGNED_INT_VEC2:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2;
      case GL30.GL_UNSIGNED_INT_VEC3:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3;
      case GL30.GL_UNSIGNED_INT_VEC4:
        return JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4;

      case GL20.GL_SAMPLER_2D:
        return JCGLType.TYPE_SAMPLER_2D;
      case GL20.GL_SAMPLER_3D:
        return JCGLType.TYPE_SAMPLER_3D;
      case GL20.GL_SAMPLER_CUBE:
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
      case GL11.GL_LINES:
        return JCGLPrimitives.PRIMITIVE_LINES;
      case GL11.GL_LINE_LOOP:
        return JCGLPrimitives.PRIMITIVE_LINE_LOOP;
      case GL11.GL_POINTS:
        return JCGLPrimitives.PRIMITIVE_POINTS;
      case GL11.GL_TRIANGLES:
        return JCGLPrimitives.PRIMITIVE_TRIANGLES;
      case GL11.GL_TRIANGLE_STRIP:
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
        return GL11.GL_LINES;
      case PRIMITIVE_LINE_LOOP:
        return GL11.GL_LINE_LOOP;
      case PRIMITIVE_TRIANGLES:
        return GL11.GL_TRIANGLES;
      case PRIMITIVE_TRIANGLE_STRIP:
        return GL11.GL_TRIANGLE_STRIP;
      case PRIMITIVE_POINTS:
        return GL11.GL_POINTS;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert filters from GL constants.
   *
   * @param mag_filter The GL constant.
   *
   * @return The value.
   */

  public static JCGLTextureFilterMagnification textureFilterMagFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL11.GL_LINEAR:
        return JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
        return JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert filters to GL constants.
   *
   * @param mag_filter The filter.
   *
   * @return The resulting GL constant.
   */

  public static int textureFilterMagToGL(
    final JCGLTextureFilterMagnification mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert filters from GL constants.
   *
   * @param min_filter The GL constant.
   *
   * @return The value.
   */

  public static JCGLTextureFilterMinification textureFilterMinFromGL(
    final int min_filter)
  {
    switch (min_filter) {
      case GL11.GL_LINEAR:
        return JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
        return JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST;
      case GL11.GL_LINEAR_MIPMAP_LINEAR:
        return JCGLTextureFilterMinification
          .TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR;
      case GL11.GL_LINEAR_MIPMAP_NEAREST:
        return JCGLTextureFilterMinification
          .TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST;
      case GL11.GL_NEAREST_MIPMAP_LINEAR:
        return JCGLTextureFilterMinification
          .TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR;
      case GL11.GL_NEAREST_MIPMAP_NEAREST:
        return JCGLTextureFilterMinification
          .TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert filters to GL constants.
   *
   * @param min_filter The filter.
   *
   * @return The resulting GL constant.
   */

  public static int textureFilterMinToGL(
    final JCGLTextureFilterMinification min_filter)
  {
    switch (min_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
      case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR:
        return GL11.GL_LINEAR_MIPMAP_LINEAR;
      case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
        return GL11.GL_LINEAR_MIPMAP_NEAREST;
      case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
        return GL11.GL_NEAREST_MIPMAP_LINEAR;
      case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST:
        return GL11.GL_NEAREST_MIPMAP_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping modes from GL constants.
   *
   * @param wrap The GL constant.
   *
   * @return The value.
   */

  public static JCGLTextureWrapR textureWrapRFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return JCGLTextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return JCGLTextureWrapR.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return JCGLTextureWrapR.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping mode to GL constants.
   *
   * @param wrap The mode.
   *
   * @return The resulting GL constant.
   */

  public static int textureWrapRToGL(
    final JCGLTextureWrapR wrap)
  {
    switch (wrap) {
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL12.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL11.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL14.GL_MIRRORED_REPEAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping modes from GL constants.
   *
   * @param wrap The GL constant.
   *
   * @return The value.
   */

  public static JCGLTextureWrapS textureWrapSFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return JCGLTextureWrapS.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return JCGLTextureWrapS.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping mode to GL constants.
   *
   * @param wrap The mode.
   *
   * @return The resulting GL constant.
   */

  public static int textureWrapSToGL(
    final JCGLTextureWrapS wrap)
  {
    switch (wrap) {
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL12.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL11.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL14.GL_MIRRORED_REPEAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping modes from GL constants.
   *
   * @param wrap The GL constant.
   *
   * @return The value.
   */

  public static JCGLTextureWrapT textureWrapTFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return JCGLTextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return JCGLTextureWrapT.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return JCGLTextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert wrapping mode to GL constants.
   *
   * @param wrap The mode.
   *
   * @return The resulting GL constant.
   */

  public static int textureWrapTToGL(
    final JCGLTextureWrapT wrap)
  {
    switch (wrap) {
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL12.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL11.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL14.GL_MIRRORED_REPEAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert pixel types from GL constants.
   *
   * @param e The GL constant.
   *
   * @return The value.
   */

  public static JCGLPixelFormat pixelTypeFromGL(
    final int e)
  {
    switch (e) {
      case GL30.GL_UNSIGNED_INT_24_8:
        return JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_INT_24_8;
      case GLES20.GL_UNSIGNED_SHORT_5_6_5:
        return JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_565;
      case GLES20.GL_UNSIGNED_SHORT_5_5_5_1:
        return JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_5551;
      case GLES20.GL_UNSIGNED_SHORT_4_4_4_4:
        return JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_SHORT_4444;
      case GL12.GL_UNSIGNED_INT_10_10_10_2:
        return JCGLPixelFormat.PIXEL_PACKED_UNSIGNED_INT_1010102;
      case GL11.GL_UNSIGNED_SHORT:
        return JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_SHORT;
      case GL11.GL_UNSIGNED_INT:
        return JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_INT;
      case GL11.GL_UNSIGNED_BYTE:
        return JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE;
      case GL11.GL_SHORT:
        return JCGLPixelFormat.PIXEL_COMPONENT_SHORT;
      case GL11.GL_INT:
        return JCGLPixelFormat.PIXEL_COMPONENT_INT;
      case GL11.GL_FLOAT:
        return JCGLPixelFormat.PIXEL_COMPONENT_FLOAT;
      case GL11.GL_BYTE:
        return JCGLPixelFormat.PIXEL_COMPONENT_BYTE;
      case GL30.GL_HALF_FLOAT:
        return JCGLPixelFormat.PIXEL_COMPONENT_HALF_FLOAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert pixel types to GL constants.
   *
   * @param p The pixel type.
   *
   * @return The resulting GL constant.
   */

  public static int pixelTypeToGL(
    final JCGLPixelFormat p)
  {
    switch (p) {
      case PIXEL_COMPONENT_BYTE:
        return GL11.GL_BYTE;
      case PIXEL_COMPONENT_FLOAT:
        return GL11.GL_FLOAT;
      case PIXEL_COMPONENT_INT:
        return GL11.GL_INT;
      case PIXEL_COMPONENT_SHORT:
        return GL11.GL_SHORT;
      case PIXEL_COMPONENT_UNSIGNED_BYTE:
        return GL11.GL_UNSIGNED_BYTE;
      case PIXEL_COMPONENT_UNSIGNED_INT:
        return GL11.GL_UNSIGNED_INT;
      case PIXEL_COMPONENT_UNSIGNED_SHORT:
        return GL11.GL_UNSIGNED_SHORT;
      case PIXEL_PACKED_UNSIGNED_INT_1010102:
        return GL12.GL_UNSIGNED_INT_10_10_10_2;
      case PIXEL_PACKED_UNSIGNED_SHORT_4444:
        return GLES20.GL_UNSIGNED_SHORT_4_4_4_4;
      case PIXEL_PACKED_UNSIGNED_SHORT_5551:
        return GLES20.GL_UNSIGNED_SHORT_5_5_5_1;
      case PIXEL_PACKED_UNSIGNED_SHORT_565:
        return GLES20.GL_UNSIGNED_SHORT_5_6_5;
      case PIXEL_COMPONENT_HALF_FLOAT:
        return GL30.GL_HALF_FLOAT;
      case PIXEL_PACKED_UNSIGNED_INT_24_8:
        return GL30.GL_UNSIGNED_INT_24_8;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert framebuffer blit buffers from GL constants.
   *
   * @param buffer The GL constant.
   *
   * @return The value.
   */

  public static JCGLFramebufferBlitBuffer framebufferBlitBufferFromGL(
    final int buffer)
  {
    switch (buffer) {
      case GL11.GL_COLOR_BUFFER_BIT: {
        return JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_COLOR;
      }
      case GL11.GL_DEPTH_BUFFER_BIT: {
        return JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH;
      }
      case GL11.GL_STENCIL_BUFFER_BIT: {
        return JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert framebuffer blit buffers to GL constants.
   *
   * @param buffers The buffers.
   *
   * @return The resulting GL constant.
   */

  public static int framebufferBlitBufferSetToMask(
    final Set<JCGLFramebufferBlitBuffer> buffers)
  {
    int mask = 0;
    for (final JCGLFramebufferBlitBuffer b : buffers) {
      assert b != null;
      mask |= LWJGL3TypeConversions.framebufferBlitBufferToGL(b);
    }
    return mask;
  }

  /**
   * Convert framebuffer blit buffers to GL constants.
   *
   * @param buffer The buffer.
   *
   * @return The GL constant.
   */

  public static int framebufferBlitBufferToGL(
    final JCGLFramebufferBlitBuffer buffer)
  {
    switch (buffer) {
      case FRAMEBUFFER_BLIT_BUFFER_COLOR: {
        return GL11.GL_COLOR_BUFFER_BIT;
      }
      case FRAMEBUFFER_BLIT_BUFFER_DEPTH: {
        return GL11.GL_DEPTH_BUFFER_BIT;
      }
      case FRAMEBUFFER_BLIT_BUFFER_STENCIL: {
        return GL11.GL_STENCIL_BUFFER_BIT;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert framebuffer blit filter from GL constants.
   *
   * @param filter The GL constant.
   *
   * @return The value.
   */

  public static JCGLFramebufferBlitFilter framebufferBlitFilterFromGL(
    final int filter)
  {
    switch (filter) {
      case GL11.GL_LINEAR: {
        return JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR;
      }
      case GL11.GL_NEAREST: {
        return JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert framebuffer blit filters to GL constants.
   *
   * @param filter The filters.
   *
   * @return The resulting GL constant.
   */

  public static int framebufferBlitFilterToGL(
    final JCGLFramebufferBlitFilter filter)
  {
    switch (filter) {
      case FRAMEBUFFER_BLIT_FILTER_LINEAR: {
        return GL11.GL_LINEAR;
      }
      case FRAMEBUFFER_BLIT_FILTER_NEAREST: {
        return GL11.GL_NEAREST;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert framebuffer status from GL constants.
   *
   * @param status The GL constant.
   *
   * @return The value.
   */

  public static JCGLFramebufferStatus framebufferStatusFromGL(
    final int status)
  {
    switch (status) {
      case GL30.GL_FRAMEBUFFER_COMPLETE:
        return JCGLFramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
        return JCGLFramebufferStatus
          .FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
        return JCGLFramebufferStatus
          .FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
        return JCGLFramebufferStatus
          .FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
        return JCGLFramebufferStatus
          .FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER;
      case GL30.GL_FRAMEBUFFER_UNSUPPORTED:
        return JCGLFramebufferStatus.FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED;
    }

    return JCGLFramebufferStatus.FRAMEBUFFER_STATUS_ERROR_UNKNOWN;
  }

  /**
   * Convert framebuffer status to GL constants.
   *
   * @param status The status.
   *
   * @return The resulting GL constant.
   */

  public static int framebufferStatusToGL(
    final JCGLFramebufferStatus status)
  {
    switch (status) {
      case FRAMEBUFFER_STATUS_COMPLETE:
        return GL30.GL_FRAMEBUFFER_COMPLETE;
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT:
        return GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
      case FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT:
        return GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER:
        return GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER:
        return GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
      case FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED:
        return GL30.GL_FRAMEBUFFER_UNSUPPORTED;
      case FRAMEBUFFER_STATUS_ERROR_UNKNOWN:
        return -1;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert face selections from GL constants.
   *
   * @param faces The GL constant.
   *
   * @return The value.
   */

  public static JCGLFaceSelection faceSelectionFromGL(
    final int faces)
  {
    switch (faces) {
      case GL11.GL_BACK:
        return JCGLFaceSelection.FACE_BACK;
      case GL11.GL_FRONT:
        return JCGLFaceSelection.FACE_FRONT;
      case GL11.GL_FRONT_AND_BACK:
        return JCGLFaceSelection.FACE_FRONT_AND_BACK;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert faces to GL constants.
   *
   * @param faces The faces.
   *
   * @return The resulting GL constant.
   */

  public static int faceSelectionToGL(
    final JCGLFaceSelection faces)
  {
    switch (faces) {
      case FACE_BACK:
        return GL11.GL_BACK;
      case FACE_FRONT:
        return GL11.GL_FRONT;
      case FACE_FRONT_AND_BACK:
        return GL11.GL_FRONT_AND_BACK;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert winding orders to GL constants.
   *
   * @param f The order.
   *
   * @return The resulting GL constant.
   */

  public static JCGLFaceWindingOrder faceWindingOrderFromGL(
    final int f)
  {
    switch (f) {
      case GL11.GL_CW:
        return JCGLFaceWindingOrder.FRONT_FACE_CLOCKWISE;
      case GL11.GL_CCW:
        return JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert face winding orders to GL constants.
   *
   * @param f The face winding order.
   *
   * @return The resulting GL constant.
   */

  public static int faceWindingOrderToGL(
    final JCGLFaceWindingOrder f)
  {
    switch (f) {
      case FRONT_FACE_CLOCKWISE:
        return GL11.GL_CW;
      case FRONT_FACE_COUNTER_CLOCKWISE:
        return GL11.GL_CCW;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert stencil functions to GL constants.
   *
   * @param function The function.
   *
   * @return The resulting GL constant.
   */

  public static int stencilFunctionToGL(
    final JCGLStencilFunction function)
  {
    switch (function) {
      case STENCIL_ALWAYS:
        return GL11.GL_ALWAYS;
      case STENCIL_EQUAL:
        return GL11.GL_EQUAL;
      case STENCIL_GREATER_THAN:
        return GL11.GL_GREATER;
      case STENCIL_GREATER_THAN_OR_EQUAL:
        return GL11.GL_GEQUAL;
      case STENCIL_LESS_THAN:
        return GL11.GL_LESS;
      case STENCIL_LESS_THAN_OR_EQUAL:
        return GL11.GL_LEQUAL;
      case STENCIL_NEVER:
        return GL11.GL_NEVER;
      case STENCIL_NOT_EQUAL:
        return GL11.GL_NOTEQUAL;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert stencil functions from GL constants.
   *
   * @param function The GL constant
   *
   * @return The resulting function
   */

  public static JCGLStencilFunction stencilFunctionFromGL(
    final int function)
  {
    switch (function) {
      case GL11.GL_ALWAYS:
        return JCGLStencilFunction.STENCIL_ALWAYS;
      case GL11.GL_EQUAL:
        return JCGLStencilFunction.STENCIL_EQUAL;
      case GL11.GL_GREATER:
        return JCGLStencilFunction.STENCIL_GREATER_THAN;
      case GL11.GL_GEQUAL:
        return JCGLStencilFunction.STENCIL_GREATER_THAN_OR_EQUAL;
      case GL11.GL_LESS:
        return JCGLStencilFunction.STENCIL_LESS_THAN;
      case GL11.GL_LEQUAL:
        return JCGLStencilFunction.STENCIL_LESS_THAN_OR_EQUAL;
      case GL11.GL_NEVER:
        return JCGLStencilFunction.STENCIL_NEVER;
      case GL11.GL_NOTEQUAL:
        return JCGLStencilFunction.STENCIL_NOT_EQUAL;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert stencil ops to GL constants.
   *
   * @param op The op.
   *
   * @return The resulting GL constant.
   */

  public static int stencilOperationToGL(
    final JCGLStencilOperation op)
  {
    switch (op) {
      case STENCIL_OP_DECREMENT:
        return GL11.GL_DECR;
      case STENCIL_OP_DECREMENT_WRAP:
        return GL14.GL_DECR_WRAP;
      case STENCIL_OP_INCREMENT:
        return GL11.GL_INCR;
      case STENCIL_OP_INCREMENT_WRAP:
        return GL14.GL_INCR_WRAP;
      case STENCIL_OP_INVERT:
        return GL11.GL_INVERT;
      case STENCIL_OP_KEEP:
        return GL11.GL_KEEP;
      case STENCIL_OP_REPLACE:
        return GL11.GL_REPLACE;
      case STENCIL_OP_ZERO:
        return GL11.GL_ZERO;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert stencil ops from GL constants.
   *
   * @param op The GL constant
   *
   * @return The resulting operation
   */

  public static JCGLStencilOperation stencilOperationFromGL(
    final int op)
  {
    switch (op) {
      case GL11.GL_DECR:
        return JCGLStencilOperation.STENCIL_OP_DECREMENT;
      case GL14.GL_DECR_WRAP:
        return JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP;
      case GL11.GL_INCR:
        return JCGLStencilOperation.STENCIL_OP_INCREMENT;
      case GL14.GL_INCR_WRAP:
        return JCGLStencilOperation.STENCIL_OP_INCREMENT_WRAP;
      case GL11.GL_INVERT:
        return JCGLStencilOperation.STENCIL_OP_INVERT;
      case GL11.GL_KEEP:
        return JCGLStencilOperation.STENCIL_OP_KEEP;
      case GL11.GL_REPLACE:
        return JCGLStencilOperation.STENCIL_OP_REPLACE;
      case GL11.GL_ZERO:
        return JCGLStencilOperation.STENCIL_OP_ZERO;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert faces from GL constants.
   *
   * @param face The GL constant.
   *
   * @return The value.
   */

  public static JCGLCubeMapFaceLH cubeFaceFromGL(
    final int face)
  {
    switch (face) {
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X;
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X;

      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Y;
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Y;

      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z;
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z:
        return JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Z;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert cube map faces to GL constants.
   *
   * @param face The face.
   *
   * @return The resulting GL constant.
   */

  public static int cubeFaceToGL(
    final JCGLCubeMapFaceLH face)
  {
    switch (face) {
      case CUBE_MAP_LH_POSITIVE_X:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
      case CUBE_MAP_LH_NEGATIVE_X:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;

      case CUBE_MAP_LH_NEGATIVE_Y:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
      case CUBE_MAP_LH_POSITIVE_Y:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;

      case CUBE_MAP_LH_POSITIVE_Z:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
      case CUBE_MAP_LH_NEGATIVE_Z:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
    }

    throw new UnreachableCodeException();
  }
}
