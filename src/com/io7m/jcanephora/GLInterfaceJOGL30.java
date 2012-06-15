package com.io7m.jcanephora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.jogamp.common.nio.Buffers;

/**
 * A class implementing GLInterface that uses only non-deprecated features of
 * OpenGL 3.0, using JOGL as the backend. A
 * {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and all methods in the interface make this context "current" upon calling.
 * 
 * As OpenGL 3.0 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

public final class GLInterfaceJOGL30 implements GLInterface
{
  static @Nonnull BlendEquation blendEquationFromGL(
    final int e)
  {
    switch (e) {
      case GL.GL_FUNC_ADD:
        return BlendEquation.BLEND_EQUATION_ADD;
      case GL2GL3.GL_MAX:
        return BlendEquation.BLEND_EQUATION_MAXIMUM;
      case GL2GL3.GL_MIN:
        return BlendEquation.BLEND_EQUATION_MINIMUM;
      case GL.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquation.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL.GL_FUNC_SUBTRACT:
        return BlendEquation.BLEND_EQUATION_SUBTRACT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int blendEquationToGL(
    final @Nonnull BlendEquation e)
  {
    switch (e) {
      case BLEND_EQUATION_ADD:
        return GL.GL_FUNC_ADD;
      case BLEND_EQUATION_MAXIMUM:
        return GL2GL3.GL_MAX;
      case BLEND_EQUATION_MINIMUM:
        return GL2GL3.GL_MIN;
      case BLEND_EQUATION_REVERSE_SUBTRACT:
        return GL.GL_FUNC_REVERSE_SUBTRACT;
      case BLEND_EQUATION_SUBTRACT:
        return GL.GL_FUNC_SUBTRACT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull BlendFunction blendFunctionFromGL(
    final int type)
  {
    switch (type) {
      case GL2ES2.GL_CONSTANT_ALPHA:
        return BlendFunction.BLEND_CONSTANT_ALPHA;
      case GL2ES2.GL_CONSTANT_COLOR:
        return BlendFunction.BLEND_CONSTANT_COLOR;
      case GL.GL_DST_ALPHA:
        return BlendFunction.BLEND_DESTINATION_ALPHA;
      case GL.GL_DST_COLOR:
        return BlendFunction.BLEND_DESTINATION_COLOR;
      case GL.GL_ONE:
        return BlendFunction.BLEND_ONE;
      case GL2ES2.GL_ONE_MINUS_CONSTANT_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_CONSTANT_ALPHA;
      case GL2ES2.GL_ONE_MINUS_CONSTANT_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_CONSTANT_COLOR;
      case GL.GL_ONE_MINUS_DST_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_DESTINATION_ALPHA;
      case GL.GL_ONE_MINUS_DST_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR;
      case GL.GL_ONE_MINUS_SRC_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA;
      case GL.GL_ONE_MINUS_SRC_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_SOURCE_COLOR;
      case GL.GL_SRC_ALPHA:
        return BlendFunction.BLEND_SOURCE_ALPHA;
      case GL.GL_SRC_COLOR:
        return BlendFunction.BLEND_SOURCE_COLOR;
      case GL.GL_SRC_ALPHA_SATURATE:
        return BlendFunction.BLEND_SOURCE_ALPHA_SATURATE;
      case GL.GL_ZERO:
        return BlendFunction.BLEND_ZERO;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int blendFunctionToGL(
    final @Nonnull BlendFunction function)
  {
    switch (function) {
      case BLEND_CONSTANT_ALPHA:
        return GL2ES2.GL_CONSTANT_ALPHA;
      case BLEND_CONSTANT_COLOR:
        return GL2ES2.GL_CONSTANT_COLOR;
      case BLEND_DESTINATION_ALPHA:
        return GL.GL_DST_ALPHA;
      case BLEND_DESTINATION_COLOR:
        return GL.GL_DST_COLOR;
      case BLEND_ONE:
        return GL.GL_ONE;
      case BLEND_ONE_MINUS_CONSTANT_ALPHA:
        return GL2ES2.GL_ONE_MINUS_CONSTANT_ALPHA;
      case BLEND_ONE_MINUS_CONSTANT_COLOR:
        return GL2ES2.GL_ONE_MINUS_CONSTANT_COLOR;
      case BLEND_ONE_MINUS_DESTINATION_ALPHA:
        return GL.GL_ONE_MINUS_DST_ALPHA;
      case BLEND_ONE_MINUS_DESTINATION_COLOR:
        return GL.GL_ONE_MINUS_DST_COLOR;
      case BLEND_ONE_MINUS_SOURCE_ALPHA:
        return GL.GL_ONE_MINUS_SRC_ALPHA;
      case BLEND_ONE_MINUS_SOURCE_COLOR:
        return GL.GL_ONE_MINUS_SRC_COLOR;
      case BLEND_SOURCE_ALPHA:
        return GL.GL_SRC_ALPHA;
      case BLEND_SOURCE_ALPHA_SATURATE:
        return GL.GL_SRC_ALPHA_SATURATE;
      case BLEND_SOURCE_COLOR:
        return GL.GL_SRC_COLOR;
      case BLEND_ZERO:
        return GL.GL_ZERO;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static DepthFunction depthFunctionFromGL(
    final int d)
  {
    switch (d) {
      case GL.GL_ALWAYS:
        return DepthFunction.DEPTH_ALWAYS;
      case GL.GL_EQUAL:
        return DepthFunction.DEPTH_EQUAL;
      case GL.GL_GREATER:
        return DepthFunction.DEPTH_GREATER_THAN;
      case GL.GL_GEQUAL:
        return DepthFunction.DEPTH_GREATER_THAN_OR_EQUAL;
      case GL.GL_LESS:
        return DepthFunction.DEPTH_LESS_THAN;
      case GL.GL_LEQUAL:
        return DepthFunction.DEPTH_LESS_THAN_OR_EQUAL;
      case GL.GL_NEVER:
        return DepthFunction.DEPTH_NEVER;
      case GL.GL_NOTEQUAL:
        return DepthFunction.DEPTH_NOT_EQUAL;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int depthFunctionToGL(
    final DepthFunction d)
  {
    switch (d) {
      case DEPTH_ALWAYS:
        return GL.GL_ALWAYS;
      case DEPTH_EQUAL:
        return GL.GL_EQUAL;
      case DEPTH_GREATER_THAN:
        return GL.GL_GREATER;
      case DEPTH_GREATER_THAN_OR_EQUAL:
        return GL.GL_GEQUAL;
      case DEPTH_LESS_THAN:
        return GL.GL_LESS;
      case DEPTH_LESS_THAN_OR_EQUAL:
        return GL.GL_LEQUAL;
      case DEPTH_NEVER:
        return GL.GL_NEVER;
      case DEPTH_NOT_EQUAL:
        return GL.GL_NOTEQUAL;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull FaceSelection faceSelectionFromGL(
    final int faces)
  {
    switch (faces) {
      case GL.GL_BACK:
        return FaceSelection.FACE_BACK;
      case GL.GL_FRONT:
        return FaceSelection.FACE_FRONT;
      case GL.GL_FRONT_AND_BACK:
        return FaceSelection.FACE_FRONT_AND_BACK;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int faceSelectionToGL(
    final @Nonnull FaceSelection faces)
  {
    switch (faces) {
      case FACE_BACK:
        return GL.GL_BACK;
      case FACE_FRONT:
        return GL.GL_FRONT;
      case FACE_FRONT_AND_BACK:
        return GL.GL_FRONT_AND_BACK;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static FaceWindingOrder faceWindingOrderFromGL(
    final int f)
  {
    switch (f) {
      case GL.GL_CW:
        return FaceWindingOrder.FRONT_FACE_CLOCKWISE;
      case GL.GL_CCW:
        return FaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int faceWindingOrderToGL(
    final FaceWindingOrder f)
  {
    switch (f) {
      case FRONT_FACE_CLOCKWISE:
        return GL.GL_CW;
      case FRONT_FACE_COUNTER_CLOCKWISE:
        return GL.GL_CCW;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static LogicOperation logicOpFromGL(
    final int op)
  {
    switch (op) {
      case GL.GL_XOR:
        return LogicOperation.LOGIC_XOR;
      case GL.GL_SET:
        return LogicOperation.LOGIC_SET;
      case GL.GL_OR_REVERSE:
        return LogicOperation.LOGIC_OR_REVERSE;
      case GL.GL_OR_INVERTED:
        return LogicOperation.LOGIC_OR_INVERTED;
      case GL.GL_OR:
        return LogicOperation.LOGIC_OR;
      case GL.GL_NOOP:
        return LogicOperation.LOGIC_NO_OP;
      case GL.GL_NOR:
        return LogicOperation.LOGIC_NOR;
      case GL.GL_NAND:
        return LogicOperation.LOGIC_NAND;
      case GL.GL_INVERT:
        return LogicOperation.LOGIC_INVERT;
      case GL.GL_EQUIV:
        return LogicOperation.LOGIC_EQUIV;
      case GL.GL_COPY_INVERTED:
        return LogicOperation.LOGIC_COPY_INVERTED;
      case GL.GL_COPY:
        return LogicOperation.LOGIC_COPY;
      case GL.GL_CLEAR:
        return LogicOperation.LOGIC_CLEAR;
      case GL.GL_AND_REVERSE:
        return LogicOperation.LOGIC_AND_REVERSE;
      case GL.GL_AND_INVERTED:
        return LogicOperation.LOGIC_AND_INVERTED;
      case GL.GL_AND:
        return LogicOperation.LOGIC_AND;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int logicOpToGL(
    final @Nonnull LogicOperation op)
  {
    switch (op) {
      case LOGIC_AND:
        return GL.GL_AND;
      case LOGIC_AND_INVERTED:
        return GL.GL_AND_INVERTED;
      case LOGIC_AND_REVERSE:
        return GL.GL_AND_REVERSE;
      case LOGIC_CLEAR:
        return GL.GL_CLEAR;
      case LOGIC_COPY:
        return GL.GL_COPY;
      case LOGIC_COPY_INVERTED:
        return GL.GL_COPY_INVERTED;
      case LOGIC_EQUIV:
        return GL.GL_EQUIV;
      case LOGIC_INVERT:
        return GL.GL_INVERT;
      case LOGIC_NAND:
        return GL.GL_NAND;
      case LOGIC_NOR:
        return GL.GL_NOR;
      case LOGIC_NO_OP:
        return GL.GL_NOOP;
      case LOGIC_OR:
        return GL.GL_OR;
      case LOGIC_OR_INVERTED:
        return GL.GL_OR_INVERTED;
      case LOGIC_OR_REVERSE:
        return GL.GL_OR_REVERSE;
      case LOGIC_SET:
        return GL.GL_SET;
      case LOGIC_XOR:
        return GL.GL_XOR;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static PolygonMode polygonModeFromGL(
    final int g)
  {
    switch (g) {
      case GL2GL3.GL_FILL:
        return PolygonMode.POLYGON_FILL;
      case GL2GL3.GL_LINE:
        return PolygonMode.POLYGON_LINES;
      case GL2GL3.GL_POINT:
        return PolygonMode.POLYGON_POINTS;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int polygonModeToGL(
    final PolygonMode g)
  {
    switch (g) {
      case POLYGON_FILL:
        return GL2GL3.GL_FILL;
      case POLYGON_LINES:
        return GL2GL3.GL_LINE;
      case POLYGON_POINTS:
        return GL2GL3.GL_POINT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull Primitives primitiveFromGL(
    final int code)
  {
    switch (code) {
      case GL.GL_LINES:
        return Primitives.PRIMITIVE_LINES;
      case GL.GL_LINE_LOOP:
        return Primitives.PRIMITIVE_LINE_LOOP;
      case GL.GL_POINTS:
        return Primitives.PRIMITIVE_POINTS;
      case GL.GL_TRIANGLES:
        return Primitives.PRIMITIVE_TRIANGLES;
      case GL.GL_TRIANGLE_STRIP:
        return Primitives.PRIMITIVE_TRIANGLE_STRIP;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int primitiveToGL(
    final Primitives p)
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

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull GLScalarType scalarTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_BYTE:
        return GLScalarType.TYPE_BYTE;
      case GL.GL_UNSIGNED_BYTE:
        return GLScalarType.TYPE_UNSIGNED_BYTE;
      case GL.GL_SHORT:
        return GLScalarType.TYPE_SHORT;
      case GL.GL_UNSIGNED_SHORT:
        return GLScalarType.TYPE_UNSIGNED_SHORT;
      case GL2ES2.GL_INT:
        return GLScalarType.TYPE_INT;
      case GL.GL_UNSIGNED_INT:
        return GLScalarType.TYPE_UNSIGNED_INT;
      case GL.GL_FLOAT:
        return GLScalarType.TYPE_FLOAT;
      case GL2GL3.GL_DOUBLE:
        return GLScalarType.TYPE_DOUBLE;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int scalarTypeToGL(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
      case TYPE_DOUBLE:
        return GL2GL3.GL_DOUBLE;
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

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  private static void shaderReadSource(
    final @Nonnull InputStream stream,
    final @Nonnull ArrayList<String> lines,
    final @Nonnull ArrayList<Integer> lengths)
    throws IOException
  {

    final BufferedReader reader =
      new BufferedReader(new InputStreamReader(stream));

    for (;;) {
      final String line = reader.readLine();
      if (line == null) {
        break;
      }
      lines.add(line + "\n");
      lengths.add(Integer.valueOf(line.length() + 1));
    }

    assert (lines.size() == lengths.size());
  }

  static @Nonnull TextureFilter textureFilterFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL.GL_LINEAR:
        return TextureFilter.TEXTURE_FILTER_LINEAR;
      case GL.GL_NEAREST:
        return TextureFilter.TEXTURE_FILTER_NEAREST;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int textureFilterFromGL(
    final @Nonnull TextureFilter mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL.GL_NEAREST;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int textureFilterToGL(
    final @Nonnull TextureFilter mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL.GL_NEAREST;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull TextureWrap textureWrapFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL2GL3.GL_CLAMP_TO_BORDER:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_BORDER;
      case GL.GL_CLAMP_TO_EDGE:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL.GL_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT;
      case GL.GL_MIRRORED_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int textureWrapToGL(
    final @Nonnull TextureWrap wrap)
  {
    switch (wrap) {
      case TEXTURE_WRAP_CLAMP_TO_BORDER:
        return GL2GL3.GL_CLAMP_TO_BORDER;
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL.GL_MIRRORED_REPEAT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull Type typeFromGL(
    final int type)
  {
    switch (type) {
      case GL2ES2.GL_BOOL:
        return Type.TYPE_BOOLEAN;
      case GL2ES2.GL_BOOL_VEC2:
        return Type.TYPE_BOOLEAN_VECTOR_2;
      case GL2ES2.GL_BOOL_VEC3:
        return Type.TYPE_BOOLEAN_VECTOR_3;
      case GL2ES2.GL_BOOL_VEC4:
        return Type.TYPE_BOOLEAN_VECTOR_4;
      case GL.GL_FLOAT:
        return Type.TYPE_FLOAT;
      case GL2ES2.GL_FLOAT_MAT2:
        return Type.TYPE_FLOAT_MATRIX_2;
      case GL2GL3.GL_FLOAT_MAT2x3:
        return Type.TYPE_FLOAT_MATRIX_2x3;
      case GL2GL3.GL_FLOAT_MAT2x4:
        return Type.TYPE_FLOAT_MATRIX_2x4;
      case GL2ES2.GL_FLOAT_MAT3:
        return Type.TYPE_FLOAT_MATRIX_3;
      case GL2GL3.GL_FLOAT_MAT3x2:
        return Type.TYPE_FLOAT_MATRIX_3x2;
      case GL2GL3.GL_FLOAT_MAT3x4:
        return Type.TYPE_FLOAT_MATRIX_3x4;
      case GL2ES2.GL_FLOAT_MAT4:
        return Type.TYPE_FLOAT_MATRIX_4;
      case GL2GL3.GL_FLOAT_MAT4x2:
        return Type.TYPE_FLOAT_MATRIX_4x2;
      case GL2GL3.GL_FLOAT_MAT4x3:
        return Type.TYPE_FLOAT_MATRIX_4x3;
      case GL2ES2.GL_FLOAT_VEC2:
        return Type.TYPE_FLOAT_VECTOR_2;
      case GL2ES2.GL_FLOAT_VEC3:
        return Type.TYPE_FLOAT_VECTOR_3;
      case GL2ES2.GL_FLOAT_VEC4:
        return Type.TYPE_FLOAT_VECTOR_4;
      case GL2ES2.GL_INT:
        return Type.TYPE_INTEGER;
      case GL2ES2.GL_INT_VEC2:
        return Type.TYPE_INTEGER_VECTOR_2;
      case GL2ES2.GL_INT_VEC3:
        return Type.TYPE_INTEGER_VECTOR_3;
      case GL2ES2.GL_INT_VEC4:
        return Type.TYPE_INTEGER_VECTOR_4;
      case GL2GL3.GL_SAMPLER_1D:
        return Type.TYPE_SAMPLER_1D;
      case GL2GL3.GL_SAMPLER_1D_SHADOW:
        return Type.TYPE_SAMPLER_1D_SHADOW;
      case GL2ES2.GL_SAMPLER_2D:
        return Type.TYPE_SAMPLER_2D;
      case GL2ES2.GL_SAMPLER_2D_SHADOW:
        return Type.TYPE_SAMPLER_2D_SHADOW;
      case GL2ES2.GL_SAMPLER_3D:
        return Type.TYPE_SAMPLER_3D;
      case GL2ES2.GL_SAMPLER_CUBE:
        return Type.TYPE_SAMPLER_CUBE;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int typeToGL(
    final @Nonnull Type type)
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
      case TYPE_FLOAT_MATRIX_2x3:
        return GL2GL3.GL_FLOAT_MAT2x3;
      case TYPE_FLOAT_MATRIX_2x4:
        return GL2GL3.GL_FLOAT_MAT2x4;
      case TYPE_FLOAT_MATRIX_3:
        return GL2ES2.GL_FLOAT_MAT3;
      case TYPE_FLOAT_MATRIX_3x2:
        return GL2GL3.GL_FLOAT_MAT3x2;
      case TYPE_FLOAT_MATRIX_3x4:
        return GL2GL3.GL_FLOAT_MAT3x4;
      case TYPE_FLOAT_MATRIX_4:
        return GL2ES2.GL_FLOAT_MAT4;
      case TYPE_FLOAT_MATRIX_4x2:
        return GL2GL3.GL_FLOAT_MAT4x2;
      case TYPE_FLOAT_MATRIX_4x3:
        return GL2GL3.GL_FLOAT_MAT4x3;
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
      case TYPE_SAMPLER_1D:
        return GL2GL3.GL_SAMPLER_1D;
      case TYPE_SAMPLER_1D_SHADOW:
        return GL2GL3.GL_SAMPLER_1D_SHADOW;
      case TYPE_SAMPLER_2D:
        return GL2ES2.GL_SAMPLER_2D;
      case TYPE_SAMPLER_2D_SHADOW:
        return GL2ES2.GL_SAMPLER_2D_SHADOW;
      case TYPE_SAMPLER_3D:
        return GL2ES2.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL2ES2.GL_SAMPLER_CUBE;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull GLUnsignedType unsignedTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_UNSIGNED_BYTE:
        return GLUnsignedType.TYPE_UNSIGNED_BYTE;
      case GL.GL_UNSIGNED_SHORT:
        return GLUnsignedType.TYPE_UNSIGNED_SHORT;
      case GL.GL_UNSIGNED_INT:
        return GLUnsignedType.TYPE_UNSIGNED_INT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int unsignedTypeToGL(
    final @Nonnull GLUnsignedType type)
  {
    switch (type) {
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static UsageHint usageHintFromGL(
    final int hint)
  {
    switch (hint) {
      case GL2GL3.GL_DYNAMIC_COPY:
        return UsageHint.USAGE_DYNAMIC_COPY;
      case GL.GL_DYNAMIC_DRAW:
        return UsageHint.USAGE_DYNAMIC_DRAW;
      case GL2GL3.GL_DYNAMIC_READ:
        return UsageHint.USAGE_DYNAMIC_READ;
      case GL2GL3.GL_STATIC_COPY:
        return UsageHint.USAGE_STATIC_COPY;
      case GL.GL_STATIC_DRAW:
        return UsageHint.USAGE_STATIC_DRAW;
      case GL2GL3.GL_STATIC_READ:
        return UsageHint.USAGE_STATIC_READ;
      case GL2GL3.GL_STREAM_COPY:
        return UsageHint.USAGE_STREAM_COPY;
      case GL2ES2.GL_STREAM_DRAW:
        return UsageHint.USAGE_STREAM_DRAW;
      case GL2GL3.GL_STREAM_READ:
        return UsageHint.USAGE_STREAM_READ;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int usageHintToGL(
    final UsageHint hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_COPY:
        return GL2GL3.GL_DYNAMIC_COPY;
      case USAGE_DYNAMIC_DRAW:
        return GL.GL_DYNAMIC_DRAW;
      case USAGE_DYNAMIC_READ:
        return GL2GL3.GL_DYNAMIC_READ;
      case USAGE_STATIC_COPY:
        return GL2GL3.GL_STATIC_COPY;
      case USAGE_STATIC_DRAW:
        return GL.GL_STATIC_DRAW;
      case USAGE_STATIC_READ:
        return GL2GL3.GL_STATIC_READ;
      case USAGE_STREAM_COPY:
        return GL2GL3.GL_STREAM_COPY;
      case USAGE_STREAM_DRAW:
        return GL2ES2.GL_STREAM_DRAW;
      case USAGE_STREAM_READ:
        return GL2GL3.GL_STREAM_READ;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  private final @Nonnull GLContext     context;
  private final @Nonnull Log           log;
  private final @Nonnull TextureUnit[] texture_units;
  private boolean                      line_smoothing;
  private int                          line_aliased_min_width;
  private int                          line_aliased_max_width;
  private int                          line_smooth_min_width;
  private int                          line_smooth_max_width;
  private int                          point_min_width;

  private int                          point_max_width;

  public GLInterfaceJOGL30(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");

    this.context = Constraints.constrainNotNull(context, "GL context");
    final GL g = this.contextMakeCurrentIfNecessary();

    this.texture_units = this.textureGetUnitsCache();
    this.line_smoothing = false;

    {
      final IntBuffer buffer = Buffers.newDirectIntBuffer(2);

      {
        buffer.rewind();
        g.glGetIntegerv(GL.GL_ALIASED_LINE_WIDTH_RANGE, buffer);
        this.line_aliased_min_width = buffer.get();
        this.line_aliased_max_width = buffer.get();
        GLError.check(this);
      }

      {
        buffer.rewind();
        g.glGetIntegerv(GL.GL_SMOOTH_LINE_WIDTH_RANGE, buffer);
        this.line_smooth_min_width = buffer.get();
        this.line_smooth_max_width = buffer.get();
        GLError.check(this);
      }

      {
        buffer.rewind();
        g.glGetIntegerv(GL2GL3.GL_POINT_SIZE_RANGE, buffer);
        this.point_min_width = buffer.get();
        this.point_max_width = buffer.get();
        GLError.check(this);
      }
    }
  }

  @Override public ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");

    final long size = descriptor.getSize();
    final long bytes = elements * size;

    this.log.debug("vertex-buffer: allocate ("
      + elements
      + " elements, "
      + size
      + " bytes per element, "
      + bytes
      + " bytes)");

    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    gl.glGenBuffers(1, buffer);
    GLError.check(this);

    final int id = buffer.get(0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, bytes, null, GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    this.log.debug("vertex-buffer: allocated " + id);
    return new ArrayBuffer(id, elements, descriptor);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer.getLocation());
    GLError.check(this);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferDescriptor d = buffer.getDescriptor();

    Constraints.constrainArbitrary(
      d.getAttribute(buffer_attribute.getName()) == buffer_attribute,
      "Buffer attribute belongs to the array buffer");

    final int program_attrib_id = program_attribute.getLocation();
    final int count = buffer_attribute.getElements();
    final int type =
      GLInterfaceJOGL30.scalarTypeToGL(buffer_attribute.getType());
    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset = d.getAttributeOffset(buffer_attribute.getName());

    gl.glEnableVertexAttribArray(program_attrib_id);
    GLError.check(this);
    gl.glVertexAttribPointer(
      program_attrib_id,
      count,
      type,
      normalized,
      stride,
      offset);
    GLError.check(this);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: delete " + id);

    gl.glDeleteBuffers(
      1,
      Buffers.newDirectIntBuffer(new int[] { id.getLocation() }));
    id.setDeleted();
    GLError.check(this);
  }

  @Override public ByteBuffer arrayBufferMapRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    return b;
  }

  @Override public ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b = gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new ArrayBufferWritableMap(id, b);
  }

  @Override public void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferDescriptor d = buffer.getDescriptor();

    Constraints.constrainArbitrary(
      d.getAttribute(buffer_attribute.getName()) == buffer_attribute,
      "Buffer attribute belongs to the array buffer");

    gl.glEnableVertexAttribArray(program_attribute.getLocation());
    GLError.check(this);
  }

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: unmap " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void blendingDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL.GL_BLEND);
    GLError.check(this);
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquation.BLEND_EQUATION_ADD,
      BlendEquation.BLEND_EQUATION_ADD);
  }

  @Override public void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(source_rgb_factor, "Source RGB factor");
    Constraints.constrainNotNull(source_alpha_factor, "Source alpha factor");
    Constraints.constrainNotNull(
      destination_rgb_factor,
      "Destination RGB factor");
    Constraints.constrainNotNull(
      destination_alpha_factor,
      "Destination alpha factor");
    Constraints.constrainNotNull(equation_rgb, "Equation RGB");
    Constraints.constrainNotNull(equation_alpha, "Equation alpha");

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      GLInterfaceJOGL30.blendEquationToGL(equation_rgb),
      GLInterfaceJOGL30.blendEquationToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      GLInterfaceJOGL30.blendFunctionToGL(source_rgb_factor),
      GLInterfaceJOGL30.blendFunctionToGL(destination_rgb_factor),
      GLInterfaceJOGL30.blendFunctionToGL(source_alpha_factor),
      GLInterfaceJOGL30.blendFunctionToGL(destination_alpha_factor));
    GLError.check(this);
  }

  @Override public void blendingEnableWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      BlendEquation.BLEND_EQUATION_ADD,
      BlendEquation.BLEND_EQUATION_ADD);
  }

  @Override public void blendingEnableWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    gl.glClearColor(r, g, b, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    gl.glClearColor(r, g, b, a);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    this.colorBufferClear3f(color.getXF(), color.getYF(), color.getZF());
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    this.colorBufferClear4f(
      color.getXF(),
      color.getYF(),
      color.getZF(),
      color.getWF());
  }

  private int contextGetInteger(
    final GL2GL3 g,
    final int name)
    throws GLException
  {
    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    g.glGetIntegerv(name, buffer);
    GLError.check(this);
    return buffer.get(0);
  }

  private int contextGetProgramInteger(
    final GL2GL3 g,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    g.glGetProgramiv(program, name, buffer);
    GLError.check(this);
    return buffer.get(0);
  }

  private int contextGetShaderInteger(
    final GL2GL3 g,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    g.glGetShaderiv(program, name, buffer);
    GLError.check(this);
    return buffer.get(0);
  }

  private GL2GL3 contextMakeCurrentIfNecessary()
    throws GLException
  {
    if (this.context.isCurrent() == false) {
      final int r = this.context.makeCurrent();
      if (r == GLContext.CONTEXT_NOT_CURRENT) {
        throw new GLException(0, "GL context could not be made current");
      }
    }

    return this.context.getGL().getGL2GL3();
  }

  @Override public void cullingDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    gl.glDisable(GL.GL_CULL_FACE);
    GLError.check(this);
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi = GLInterfaceJOGL30.faceSelectionToGL(faces);
    final int oi = GLInterfaceJOGL30.faceWindingOrderToGL(order);

    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(fi);
    gl.glFrontFace(oi);
    GLError.check(this);
  }

  @Override public void depthBufferClear(
    final float depth)
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public void depthBufferDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL.GL_DEPTH_TEST);
    GLError.check(this);
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      this.contextGetInteger(gl, GL.GL_DEPTH_BITS),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits");

    final int d = GLInterfaceJOGL30.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    GLError.check(this);
  }

  @Override public int depthBufferGetBits()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    return this.contextGetInteger(gl, GL.GL_DEPTH_BITS);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(mode, "Drawing mode");
    Constraints.constrainNotNull(indices, "Index buffer");
    Constraints.constrainArbitrary(
      indices.resourceIsDeleted() == false,
      "Index buffer not deleted");

    final int index_id = indices.getLocation();
    final int index_count = (int) indices.getElements();
    final int mode_gl = GLInterfaceJOGL30.primitiveToGL(mode);
    final int type = GLInterfaceJOGL30.unsignedTypeToGL(indices.getType());

    g.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, index_id);
    g.glDrawElements(mode_gl, index_count, type, 0L);
    GLError.check(this);
  }

  @Override public void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Fragment shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    this.log.debug("fragment-shader: attach " + program + " " + shader);

    g.glAttachShader(program.getLocation(), shader.getLocation());
    GLError.check(this);
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    this.log.debug("fragment-shader: compile \"" + name + "\"");

    final int id = g.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    GLError.check(this);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLInterfaceJOGL30.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    g.glShaderSource(id, line_array.length, line_array, line_lengths);
    GLError.check(this);
    g.glCompileShader(id);
    GLError.check(this);
    final int status =
      this.contextGetShaderInteger(g, id, GL2ES2.GL_COMPILE_STATUS);
    GLError.check(this);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      g.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      GLError.check(this);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new FragmentShader(id, name);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Fragment shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    this.log.debug("fragment-shader: delete " + id);

    g.glDeleteShader(id.getLocation());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public Framebuffer framebufferAllocate()
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    gl.glGenFramebuffers(1, buffer);
    GLError.check(this);
    final int id = buffer.get(0);
    this.log.debug("framebuffer: allocated " + id);
    return new Framebuffer(id);
  }

  @Override public void framebufferAttachStorage(
    final @Nonnull Framebuffer buffer,
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    Constraints.constrainNotNull(attachments, "Framebuffer attachments");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getLocation());
    GLError.check(this);

    /**
     * Attach all framebuffer storage.
     */

    try {
      boolean have_depth = false;

      final int max_color =
        this.contextGetInteger(gl, GL2ES2.GL_MAX_COLOR_ATTACHMENTS);

      for (final FramebufferAttachment attachment : attachments) {
        Constraints.constrainNotNull(attachment, "Attachment");

        switch (attachment.type) {
          case ATTACHMENT_COLOR:
          {
            final ColorAttachment color = (ColorAttachment) attachment;
            final int index = color.getIndex();
            Constraints.constrainRange(
              index,
              0,
              max_color - 1,
              "Color buffer attachment index in range");

            gl.glFramebufferTexture2D(
              GL.GL_FRAMEBUFFER,
              GL.GL_COLOR_ATTACHMENT0 + index,
              GL.GL_TEXTURE_2D,
              color.getTexture().getLocation(),
              0);
            GLError.check(this);

            this.log.debug("framebuffer: attach color "
              + buffer
              + " "
              + color);
            break;
          }
          case ATTACHMENT_D24S8:
          {
            Constraints.constrainArbitrary(
              have_depth == false,
              "Only one depth+stencil buffer provided");
            have_depth = true;

            final RenderbufferD24S8Attachment depth =
              (RenderbufferD24S8Attachment) attachment;

            gl.glFramebufferRenderbuffer(
              GL.GL_FRAMEBUFFER,
              GL2GL3.GL_DEPTH_STENCIL_ATTACHMENT,
              GL.GL_RENDERBUFFER,
              depth.getRenderbuffer().getLocation());
            GLError.check(this);

            this.log.debug("framebuffer: attach depth+stencil "
              + buffer
              + " "
              + depth);
            break;
          }
          default:
            throw new AssertionError("unreachable code");
        }
      }

      /**
       * Check framebuffer status.
       */

      final int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
      GLError.check(this);

      switch (status) {
        case GL.GL_FRAMEBUFFER_COMPLETE:
          break;
        case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
          throw new GLException(
            GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT,
            "Framebuffer is incomplete");
        case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
          throw new GLException(
            GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT,
            "Framebuffer is incomplete - missing image attachment");
        case GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
          throw new GLException(
            GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER,
            "Framebuffer is incomplete - missing draw buffer");
        case GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
          throw new GLException(
            GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER,
            "Framebuffer is incomplete - missing read buffer");
        case GL.GL_FRAMEBUFFER_UNSUPPORTED:
          throw new GLException(
            GL.GL_FRAMEBUFFER_UNSUPPORTED,
            "Framebuffer configuration unsupported");
        default:
          throw new GLException(status, "Unknown framebuffer error");
      }
    } finally {
      gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
      GLError.check(this);
    }
  }

  @Override public void framebufferBind(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getLocation());
    GLError.check(this);
  }

  @Override public void framebufferDelete(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    this.log.debug("framebuffer: delete " + buffer);

    final IntBuffer b =
      Buffers.newDirectIntBuffer(new int[] { buffer.getLocation() });
    gl.glDeleteFramebuffers(1, b);
    GLError.check(this);
  }

  @Override public void framebufferUnbind()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    GLError.check(this);
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_BYTE;

    long size = 1;
    if (buffer.getElements() > 0xff) {
      type = GLUnsignedType.TYPE_UNSIGNED_SHORT;
      size = 2;
    }
    if (buffer.getElements() > 0xffff) {
      type = GLUnsignedType.TYPE_UNSIGNED_INT;
      size = 4;
    }

    final long bytes = indices * size;

    this.log.debug("index-buffer: allocate ("
      + indices
      + " elements, "
      + GLUnsignedTypeMeta.getSizeBytes(type)
      + " bytes per element, "
      + bytes
      + " bytes)");

    final IntBuffer ib = Buffers.newDirectIntBuffer(1);
    gl.glGenBuffers(1, ib);
    GLError.check(this);

    final int id = ib.get(0);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      bytes,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    this.log.debug("index-buffer: allocated " + id);
    return new IndexBuffer(id, indices, type);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: delete " + id);

    gl.glDeleteBuffers(
      1,
      Buffers.newDirectIntBuffer(new int[] { id.getLocation() }));
    id.setDeleted();
    GLError.check(this);
  }

  @Override public IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferReadableMap(id, b);
  }

  @Override public IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferWritableMap(id, b);
  }

  @Override public void indexBufferUnmap(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: unmap " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public int lineAliasedGetMaximumWidth()
  {
    return this.line_aliased_max_width;
  }

  @Override public int lineAliasedGetMinimumWidth()
  {
    return this.line_aliased_min_width;
  }

  @Override public void lineSetWidth(
    final float width)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    if (this.line_smoothing) {
      Constraints.constrainRange(
        width,
        this.line_smooth_min_width,
        this.line_smooth_max_width,
        "Smooth line width");
    } else {
      Constraints.constrainRange(
        width,
        this.line_aliased_min_width,
        this.line_aliased_max_width,
        "Aliased line width");
    }

    gl.glLineWidth(width);
    GLError.check(this);
  }

  @Override public int lineSmoothGetMaximumWidth()
  {
    return this.line_smooth_max_width;
  }

  @Override public int lineSmoothGetMinimumWidth()
  {
    return this.line_smooth_min_width;
  }

  @Override public void lineSmoothingDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = false;
  }

  @Override public void lineSmoothingEnable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glEnable(GL.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = true;
  }

  @Override public void logicOperationsDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL2.GL_LOGIC_OP);
    GLError.check(this);
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL2.GL_LOGIC_OP);
    gl.glLogicOp(GLInterfaceJOGL30.logicOpToGL(operation));
    GLError.check(this);
  }

  @Override public int metaGetError()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    return gl.glGetError();
  }

  @Override public String metaGetRenderer()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    final String x = gl.glGetString(GL.GL_RENDERER);
    GLError.check(this);
    return x;
  }

  @Override public String metaGetVendor()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    final String x = gl.glGetString(GL.GL_VENDOR);
    GLError.check(this);
    return x;
  }

  @Override public String metaGetVersion()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    final String x = gl.glGetString(GL.GL_VERSION);
    GLError.check(this);
    return x;
  }

  @Override public PixelUnpackBuffer pixelUnpackBufferAllocate(
    final long elements,
    final @Nonnull GLScalarType type,
    final long element_values,
    final @Nonnull UsageHint hint)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainRange(elements, 1, Long.MAX_VALUE, "Element count");
    Constraints.constrainRange(
      element_values,
      1,
      Long.MAX_VALUE,
      "Element values");
    Constraints.constrainNotNull(hint, "Usage hint");
    Constraints.constrainNotNull(type, "Element type");

    final long bytes =
      (element_values * GLScalarTypeMeta.getSizeBytes(type)) * elements;

    this.log.debug("pixel-unpack-buffer: allocate ("
      + elements
      + " elements of type ("
      + type
      + ", "
      + element_values
      + "), "
      + bytes
      + " bytes)");

    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    gl.glGenBuffers(1, buffer);
    GLError.check(this);

    final int id = buffer.get(0);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(
      GL2GL3.GL_PIXEL_UNPACK_BUFFER,
      bytes,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    this.log.debug("pixel-unpack-buffer: allocated " + id);
    return new PixelUnpackBuffer(id, elements, type, element_values);
  }

  @Override public void pixelUnpackBufferDelete(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: delete " + id);

    gl.glDeleteBuffers(
      1,
      Buffers.newDirectIntBuffer(new int[] { id.getLocation() }));
    id.setDeleted();
    GLError.check(this);
  }

  @Override public ByteBuffer pixelUnpackBufferMapRead(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: map " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);

    return b;
  }

  @Override public PixelUnpackBufferWritableMap pixelUnpackBufferMapWrite(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: map " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL2GL3.GL_PIXEL_UNPACK_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b =
      gl.glMapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);

    return new PixelUnpackBufferWritableMap(id, b);
  }

  @Override public void pixelUnpackBufferUnmap(
    final @Nonnull PixelUnpackBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: unmap " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void pointDisableProgramSizeControl()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL3.GL_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public void pointEnableProgramSizeControl()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public int pointGetMaximumWidth()
  {
    return this.point_max_width;
  }

  @Override public int pointGetMinimumWidth()
  {
    return this.point_min_width;
  }

  @Override public void polygonSetMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLInterfaceJOGL30.polygonModeToGL(mode);
    final int fm = GLInterfaceJOGL30.faceSelectionToGL(faces);
    gl.glPolygonMode(fm, im);
    GLError.check(this);
  }

  @Override public void polygonSmoothingDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public void polygonSmoothingEnable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      gl.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    gl.glUseProgram(program.getLocation());
    GLError.check(this);
  }

  @Override public ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(name, "Program name");

    this.log.debug("program: create \"" + name + "\"");

    final int id = gl.glCreateProgram();
    GLError.check(this);
    return new ProgramReference(id, name);
  }

  @Override public void programDeactivate()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glUseProgram(0);
    GLError.check(this);
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    this.log.debug("program: delete " + program);

    gl.glDeleteProgram(program.getLocation());
    program.setDeleted();
    GLError.check(this);
  }

  @Override public void programGetAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      gl.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getLocation();
    final int max =
      this.contextGetProgramInteger(
        gl,
        program.getLocation(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      this.contextGetProgramInteger(
        gl,
        program.getLocation(),
        GL2ES2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    /*
     * Note: some drivers will return built-in attributes here (such as
     * "gl_Vertex") but their locations are -1, so must be explicitly ignored.
     */

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      gl.glGetActiveAttrib(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      GLError.check(this);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLInterfaceJOGL30.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetAttribLocation(id, name);
      GLError.check(this);

      if (location == -1) {
        this.log.debug("driver returned active attribute '"
          + name
          + "' with location -1, ignoring");
        continue;
      }

      assert out.containsKey(name) == false;
      out.put(
        name,
        new ProgramAttribute(program, index, location, name, type));
    }
  }

  @Override public int programGetMaximimActiveAttributes()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    final int max = this.contextGetInteger(gl, GL2ES2.GL_MAX_VERTEX_ATTRIBS);
    this.log.debug("implementation supports " + max + " active attributes");
    return max;
  }

  @Override public void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      gl.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getLocation();
    final int max =
      this.contextGetProgramInteger(gl, id, GL2ES2.GL_ACTIVE_UNIFORMS);
    final int length =
      this.contextGetProgramInteger(
        gl,
        id,
        GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    GLError.check(this);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      gl.glGetActiveUniform(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      GLError.check(this);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLInterfaceJOGL30.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetUniformLocation(id, name);
      GLError.check(this);

      assert (out.containsKey(name) == false);
      out.put(name, new ProgramUniform(program, index, location, name, type));
    }
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      gl.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    final int active = this.contextGetInteger(gl, GL2ES2.GL_CURRENT_PROGRAM);
    GLError.check(this);
    return active == program.getLocation();
  }

  @Override public void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      gl.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    this.log.debug("program: link " + program);

    gl.glLinkProgram(program.getLocation());
    GLError.check(this);

    final int status =
      this.contextGetProgramInteger(
        gl,
        program.getLocation(),
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetProgramInfoLog(
        program.getLocation(),
        8192,
        buffer_length,
        buffer);
      GLError.check(this);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(program.getName(), text);
    }

    GLError.check(this);
  }

  @Override public void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT,
      "Uniform type is float");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1f(uniform.getLocation(), value);
    GLError.check(this);
  }

  @Override public void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixM3x3F matrix)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_3,
      "Uniform type is mat3");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix3fv(
      uniform.getLocation(),
      9,
      false,
      MatrixM3x3F.floatBuffer(matrix));
    GLError.check(this);
  }

  @Override public void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixM4x4F matrix)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_4,
      "Uniform type is mat4");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix4fv(
      uniform.getLocation(),
      16,
      false,
      MatrixM4x4F.floatBuffer(matrix));
    GLError.check(this);
  }

  @Override public void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_SAMPLER_2D,
      "Uniform type is sampler_2d");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1i(uniform.getLocation(), unit.getIndex());
    GLError.check(this);
  }

  @Override public void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    GLError.check(this);
  }

  @Override public void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_INTEGER_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    GLError.check(this);
  }

  @Override public void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_3,
      "Uniform type is vec3");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform3f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF());
    GLError.check(this);
  }

  @Override public void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_4,
      "Uniform type is vec4");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform4f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF(),
      vector.getWF());
    GLError.check(this);
  }

  @Override public RenderbufferD24S8 renderbufferD24S8Allocate(
    final int width,
    final int height)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainRange(width, 1, Integer.MAX_VALUE);
    Constraints.constrainRange(height, 1, Integer.MAX_VALUE);

    this.log.debug("renderbuffer-d24s8: allocate " + width + "x" + height);

    final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
    gl.glGenRenderbuffers(1, buffer);
    GLError.check(this);
    final int id = buffer.get(0);

    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    GLError.check(this);
    gl.glRenderbufferStorage(
      GL.GL_RENDERBUFFER,
      GL.GL_DEPTH24_STENCIL8,
      width,
      height);
    GLError.check(this);
    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
    GLError.check(this);

    final RenderbufferD24S8 r = new RenderbufferD24S8(id, width, height);
    this.log.debug("renderbuffer-d24s8: allocated " + r);
    return r;
  }

  @Override public void renderbufferD24S8Delete(
    final @Nonnull RenderbufferD24S8 buffer)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(buffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    this.log.debug("renderbuffer-d24s8: delete " + buffer);

    gl.glDeleteRenderbuffers(
      1,
      Buffers.newDirectIntBuffer(new int[] { buffer.getLocation() }));
    buffer.setDeleted();
    GLError.check(this);
  }

  @Override public void scissorDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    gl.glDisable(GL.GL_SCISSOR_TEST);
    GLError.check(this);
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(position, "Scissor region position");
    Constraints.constrainNotNull(dimensions, "Scissor region dimensions");
    Constraints.constrainRange(
      dimensions.getXI(),
      0,
      Integer.MAX_VALUE,
      "Scissor width");
    Constraints.constrainRange(
      dimensions.getYI(),
      0,
      Integer.MAX_VALUE,
      "Scissor height");

    gl.glEnable(GL.GL_SCISSOR_TEST);
    gl.glScissor(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLError.check(this);
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    return this.contextGetInteger(gl, GL.GL_STENCIL_BITS);
  }

  @Override public Texture2DRGBAStatic texture2DRGBAStaticAllocate(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull TextureFilter min_filter)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(width, 2, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(height, 2, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    this.log.debug("texture-2DRGBA: allocate \""
      + name
      + "\" "
      + width
      + "x"
      + height);

    final IntBuffer ib = Buffers.newDirectIntBuffer(1);
    gl.glGenTextures(1, ib);
    GLError.check(this);
    final int texture_id = ib.get(0);

    final @Nonnull PixelUnpackBuffer buffer =
      this.pixelUnpackBufferAllocate(
        width * height,
        GLScalarType.TYPE_UNSIGNED_BYTE,
        4,
        UsageHint.USAGE_STATIC_DRAW);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      GLInterfaceJOGL30.textureWrapToGL(wrap_s));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      GLInterfaceJOGL30.textureWrapToGL(wrap_t));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      GLInterfaceJOGL30.textureFilterToGL(mag_filter));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      GLInterfaceJOGL30.textureFilterToGL(min_filter));

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, buffer.getLocation());
    gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      GL.GL_RGBA8,
      width,
      height,
      0,
      GL.GL_RGBA,
      GL.GL_UNSIGNED_BYTE,
      0L);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

    final Texture2DRGBAStatic t =
      new Texture2DRGBAStatic(name, texture_id, buffer, width, height);
    this.log.debug("texture-2DRGBA: allocated " + t);
    return t;
  }

  @Override public void texture2DRGBAStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getLocation());
    GLError.check(this);
  }

  @Override public void texture2DRGBAStaticDelete(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    this.log.debug("texture-2DRGBA: delete " + texture);

    gl.glDeleteTextures(
      1,
      Buffers.newDirectIntBuffer(new int[] { texture.getLocation() }));
    texture.setDeleted();
    this.pixelUnpackBufferDelete(texture.getBuffer());
  }

  @Override public ByteBuffer texture2DRGBAStaticGetImage(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    final ByteBuffer buffer =
      ByteBuffer.allocateDirect(texture.getWidth() * texture.getHeight() * 4);

    final GL2 gl2 = gl.getGL2();
    gl2.glGetTexImage(
      GL.GL_TEXTURE_2D,
      0,
      GL.GL_RGBA,
      GL.GL_UNSIGNED_BYTE,
      buffer);
    GLError.check(this);
    return buffer;
  }

  @Override public void texture2DRGBAStaticReplace(
    final @Nonnull Texture2DRGBAStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getBuffer().resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("texture-2DRGBA: update " + texture);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getLocation());
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, texture
      .getBuffer()
      .getLocation());
    GLError.check(this);
    gl.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      0,
      0,
      texture.getWidth(),
      texture.getHeight(),
      GL.GL_RGBA,
      GL.GL_UNSIGNED_BYTE,
      0L);
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLError.check(this);
  }

  @Override public int textureGetMaximumSize()
    throws GLException
  {
    final GL2GL3 gl = this.contextMakeCurrentIfNecessary();
    return this.contextGetInteger(gl, GL.GL_MAX_TEXTURE_SIZE);
  }

  @Override public TextureUnit[] textureGetUnits()
    throws GLException
  {
    return this.texture_units;
  }

  private TextureUnit[] textureGetUnitsCache()
    throws GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    final int max =
      this.contextGetInteger(g, GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);
    this.log.debug("implementation supports " + max + " texture units");

    final TextureUnit[] u = new TextureUnit[max];
    for (int index = 0; index < max; ++index) {
      u[index] = new TextureUnit(index);
    }

    return u;
  }

  @Override public void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Vertex shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    this.log.debug("vertex-shader: attach " + program + " " + shader);

    g.glAttachShader(program.getLocation(), shader.getLocation());
    GLError.check(this);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    this.log.debug("vertex-shader: compile \"" + name + "\"");

    final int id = g.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    GLError.check(this);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLInterfaceJOGL30.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    g.glShaderSource(id, line_array.length, line_array, line_lengths);
    GLError.check(this);
    g.glCompileShader(id);
    GLError.check(this);
    final int status =
      this.contextGetShaderInteger(g, id, GL2ES2.GL_COMPILE_STATUS);
    GLError.check(this);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      g.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      GLError.check(this);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new VertexShader(id, name);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(id, "Vertex shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    this.log.debug("vertex-shader: delete " + id);

    g.glDeleteShader(id.getLocation());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextMakeCurrentIfNecessary();

    Constraints.constrainNotNull(position, "Viewport position");
    Constraints.constrainNotNull(dimensions, "Viewport dimensions");

    g.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLError.check(this);
  }
}
