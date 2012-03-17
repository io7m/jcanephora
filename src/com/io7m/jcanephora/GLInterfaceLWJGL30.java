package com.io7m.jcanephora;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * A class implementing GLInterface that uses only non-deprecated features of
 * OpenGL 3.0.
 * 
 * As OpenGL 3.0 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

public final class GLInterfaceLWJGL30 implements GLInterface
{
  static @Nonnull BlendEquation blendEquationFromGL(
    final int e)
  {
    switch (e) {
      case GL14.GL_FUNC_ADD:
        return BlendEquation.BLEND_EQUATION_ADD;
      case GL14.GL_MAX:
        return BlendEquation.BLEND_EQUATION_MAXIMUM;
      case GL14.GL_MIN:
        return BlendEquation.BLEND_EQUATION_MINIMUM;
      case GL14.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquation.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL14.GL_FUNC_SUBTRACT:
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

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull BlendFunction blendFunctionFromGL(
    final int type)
  {
    switch (type) {
      case GL11.GL_CONSTANT_ALPHA:
        return BlendFunction.BLEND_CONSTANT_ALPHA;
      case GL11.GL_CONSTANT_COLOR:
        return BlendFunction.BLEND_CONSTANT_COLOR;
      case GL11.GL_DST_ALPHA:
        return BlendFunction.BLEND_DESTINATION_ALPHA;
      case GL11.GL_DST_COLOR:
        return BlendFunction.BLEND_DESTINATION_COLOR;
      case GL11.GL_ONE:
        return BlendFunction.BLEND_ONE;
      case GL11.GL_ONE_MINUS_CONSTANT_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_CONSTANT_ALPHA;
      case GL11.GL_ONE_MINUS_CONSTANT_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_CONSTANT_COLOR;
      case GL11.GL_ONE_MINUS_DST_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_DESTINATION_ALPHA;
      case GL11.GL_ONE_MINUS_DST_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR;
      case GL11.GL_ONE_MINUS_SRC_ALPHA:
        return BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA;
      case GL11.GL_ONE_MINUS_SRC_COLOR:
        return BlendFunction.BLEND_ONE_MINUS_SOURCE_COLOR;
      case GL11.GL_SRC_ALPHA:
        return BlendFunction.BLEND_SOURCE_ALPHA;
      case GL11.GL_SRC_COLOR:
        return BlendFunction.BLEND_SOURCE_COLOR;
      case GL11.GL_SRC_ALPHA_SATURATE:
        return BlendFunction.BLEND_SOURCE_ALPHA_SATURATE;
      case GL11.GL_ZERO:
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
        return GL11.GL_CONSTANT_ALPHA;
      case BLEND_CONSTANT_COLOR:
        return GL11.GL_CONSTANT_COLOR;
      case BLEND_DESTINATION_ALPHA:
        return GL11.GL_DST_ALPHA;
      case BLEND_DESTINATION_COLOR:
        return GL11.GL_DST_COLOR;
      case BLEND_ONE:
        return GL11.GL_ONE;
      case BLEND_ONE_MINUS_CONSTANT_ALPHA:
        return GL11.GL_ONE_MINUS_CONSTANT_ALPHA;
      case BLEND_ONE_MINUS_CONSTANT_COLOR:
        return GL11.GL_ONE_MINUS_CONSTANT_COLOR;
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

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static DepthFunction depthFunctionFromGL(
    final int d)
  {
    switch (d) {
      case GL11.GL_ALWAYS:
        return DepthFunction.DEPTH_ALWAYS;
      case GL11.GL_EQUAL:
        return DepthFunction.DEPTH_EQUAL;
      case GL11.GL_GREATER:
        return DepthFunction.DEPTH_GREATER_THAN;
      case GL11.GL_GEQUAL:
        return DepthFunction.DEPTH_GREATER_THAN_OR_EQUAL;
      case GL11.GL_LESS:
        return DepthFunction.DEPTH_LESS_THAN;
      case GL11.GL_LEQUAL:
        return DepthFunction.DEPTH_LESS_THAN_OR_EQUAL;
      case GL11.GL_NEVER:
        return DepthFunction.DEPTH_NEVER;
      case GL11.GL_NOTEQUAL:
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

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull FaceSelection faceSelectionFromGL(
    final int faces)
  {
    switch (faces) {
      case GL11.GL_BACK:
        return FaceSelection.FACE_BACK;
      case GL11.GL_FRONT:
        return FaceSelection.FACE_FRONT;
      case GL11.GL_FRONT_AND_BACK:
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
        return GL11.GL_BACK;
      case FACE_FRONT:
        return GL11.GL_FRONT;
      case FACE_FRONT_AND_BACK:
        return GL11.GL_FRONT_AND_BACK;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static FaceWindingOrder faceWindingOrderFromGL(
    final int f)
  {
    switch (f) {
      case GL11.GL_CW:
        return FaceWindingOrder.FRONT_FACE_CLOCKWISE;
      case GL11.GL_CCW:
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
        return GL11.GL_CW;
      case FRONT_FACE_COUNTER_CLOCKWISE:
        return GL11.GL_CCW;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static LogicOperation logicOpFromGL(
    final int op)
  {
    switch (op) {
      case GL11.GL_XOR:
        return LogicOperation.LOGIC_XOR;
      case GL11.GL_SET:
        return LogicOperation.LOGIC_SET;
      case GL11.GL_OR_REVERSE:
        return LogicOperation.LOGIC_OR_REVERSE;
      case GL11.GL_OR_INVERTED:
        return LogicOperation.LOGIC_OR_INVERTED;
      case GL11.GL_OR:
        return LogicOperation.LOGIC_OR;
      case GL11.GL_NOOP:
        return LogicOperation.LOGIC_NO_OP;
      case GL11.GL_NOR:
        return LogicOperation.LOGIC_NOR;
      case GL11.GL_NAND:
        return LogicOperation.LOGIC_NAND;
      case GL11.GL_INVERT:
        return LogicOperation.LOGIC_INVERT;
      case GL11.GL_EQUIV:
        return LogicOperation.LOGIC_EQUIV;
      case GL11.GL_COPY_INVERTED:
        return LogicOperation.LOGIC_COPY_INVERTED;
      case GL11.GL_COPY:
        return LogicOperation.LOGIC_COPY;
      case GL11.GL_CLEAR:
        return LogicOperation.LOGIC_CLEAR;
      case GL11.GL_AND_REVERSE:
        return LogicOperation.LOGIC_AND_REVERSE;
      case GL11.GL_AND_INVERTED:
        return LogicOperation.LOGIC_AND_INVERTED;
      case GL11.GL_AND:
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
        return GL11.GL_AND;
      case LOGIC_AND_INVERTED:
        return GL11.GL_AND_INVERTED;
      case LOGIC_AND_REVERSE:
        return GL11.GL_AND_REVERSE;
      case LOGIC_CLEAR:
        return GL11.GL_CLEAR;
      case LOGIC_COPY:
        return GL11.GL_COPY;
      case LOGIC_COPY_INVERTED:
        return GL11.GL_COPY_INVERTED;
      case LOGIC_EQUIV:
        return GL11.GL_EQUIV;
      case LOGIC_INVERT:
        return GL11.GL_INVERT;
      case LOGIC_NAND:
        return GL11.GL_NAND;
      case LOGIC_NOR:
        return GL11.GL_NOR;
      case LOGIC_NO_OP:
        return GL11.GL_NOOP;
      case LOGIC_OR:
        return GL11.GL_OR;
      case LOGIC_OR_INVERTED:
        return GL11.GL_OR_INVERTED;
      case LOGIC_OR_REVERSE:
        return GL11.GL_OR_REVERSE;
      case LOGIC_SET:
        return GL11.GL_SET;
      case LOGIC_XOR:
        return GL11.GL_XOR;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static PolygonMode polygonModeFromGL(
    final int g)
  {
    switch (g) {
      case GL11.GL_FILL:
        return PolygonMode.POLYGON_FILL;
      case GL11.GL_LINE:
        return PolygonMode.POLYGON_LINES;
      case GL11.GL_POINT:
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
        return GL11.GL_FILL;
      case POLYGON_LINES:
        return GL11.GL_LINE;
      case POLYGON_POINTS:
        return GL11.GL_POINT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull GLScalarType scalarTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL11.GL_BYTE:
        return GLScalarType.TYPE_BYTE;
      case GL11.GL_UNSIGNED_BYTE:
        return GLScalarType.TYPE_UNSIGNED_BYTE;
      case GL11.GL_SHORT:
        return GLScalarType.TYPE_SHORT;
      case GL11.GL_UNSIGNED_SHORT:
        return GLScalarType.TYPE_UNSIGNED_SHORT;
      case GL11.GL_INT:
        return GLScalarType.TYPE_INT;
      case GL11.GL_UNSIGNED_INT:
        return GLScalarType.TYPE_UNSIGNED_INT;
      case GL11.GL_FLOAT:
        return GLScalarType.TYPE_FLOAT;
      case GL11.GL_DOUBLE:
        return GLScalarType.TYPE_DOUBLE;
    }

    throw new AssertionError();
  }

  static int scalarTypeToGL(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL11.GL_BYTE;
      case TYPE_DOUBLE:
        return GL11.GL_DOUBLE;
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

    throw new AssertionError();
  }

  static @Nonnull TextureFilter textureFilterFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL11.GL_LINEAR:
        return TextureFilter.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
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
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static int textureFilterToGL(
    final @Nonnull TextureFilter mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull TextureWrap textureWrapFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL13.GL_CLAMP_TO_BORDER:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_BORDER;
      case GL12.GL_CLAMP_TO_EDGE:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
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
        return GL13.GL_CLAMP_TO_BORDER;
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL12.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL11.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL14.GL_MIRRORED_REPEAT;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }

  static @Nonnull Type typeFromGL(
    final int type)
  {
    switch (type) {
      case GL20.GL_BOOL:
        return Type.TYPE_BOOLEAN;
      case GL20.GL_BOOL_VEC2:
        return Type.TYPE_BOOLEAN_VECTOR_2;
      case GL20.GL_BOOL_VEC3:
        return Type.TYPE_BOOLEAN_VECTOR_3;
      case GL20.GL_BOOL_VEC4:
        return Type.TYPE_BOOLEAN_VECTOR_4;
      case GL11.GL_FLOAT:
        return Type.TYPE_FLOAT;
      case GL20.GL_FLOAT_MAT2:
        return Type.TYPE_FLOAT_MATRIX_2;
      case GL21.GL_FLOAT_MAT2x3:
        return Type.TYPE_FLOAT_MATRIX_2x3;
      case GL21.GL_FLOAT_MAT2x4:
        return Type.TYPE_FLOAT_MATRIX_2x4;
      case GL20.GL_FLOAT_MAT3:
        return Type.TYPE_FLOAT_MATRIX_3;
      case GL21.GL_FLOAT_MAT3x2:
        return Type.TYPE_FLOAT_MATRIX_3x2;
      case GL21.GL_FLOAT_MAT3x4:
        return Type.TYPE_FLOAT_MATRIX_3x4;
      case GL20.GL_FLOAT_MAT4:
        return Type.TYPE_FLOAT_MATRIX_4;
      case GL21.GL_FLOAT_MAT4x2:
        return Type.TYPE_FLOAT_MATRIX_4x2;
      case GL21.GL_FLOAT_MAT4x3:
        return Type.TYPE_FLOAT_MATRIX_4x3;
      case GL20.GL_FLOAT_VEC2:
        return Type.TYPE_FLOAT_VECTOR_2;
      case GL20.GL_FLOAT_VEC3:
        return Type.TYPE_FLOAT_VECTOR_3;
      case GL20.GL_FLOAT_VEC4:
        return Type.TYPE_FLOAT_VECTOR_4;
      case GL11.GL_INT:
        return Type.TYPE_INTEGER;
      case GL20.GL_INT_VEC2:
        return Type.TYPE_INTEGER_VECTOR_2;
      case GL20.GL_INT_VEC3:
        return Type.TYPE_INTEGER_VECTOR_3;
      case GL20.GL_INT_VEC4:
        return Type.TYPE_INTEGER_VECTOR_4;
      case GL20.GL_SAMPLER_1D:
        return Type.TYPE_SAMPLER_1D;
      case GL20.GL_SAMPLER_1D_SHADOW:
        return Type.TYPE_SAMPLER_1D_SHADOW;
      case GL20.GL_SAMPLER_2D:
        return Type.TYPE_SAMPLER_2D;
      case GL20.GL_SAMPLER_2D_SHADOW:
        return Type.TYPE_SAMPLER_2D_SHADOW;
      case GL20.GL_SAMPLER_3D:
        return Type.TYPE_SAMPLER_3D;
      case GL20.GL_SAMPLER_CUBE:
        return Type.TYPE_SAMPLER_CUBE;
    }

    throw new AssertionError();
  }

  static int typeToGL(
    final @Nonnull Type type)
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
      case TYPE_FLOAT_MATRIX_2x3:
        return GL21.GL_FLOAT_MAT2x3;
      case TYPE_FLOAT_MATRIX_2x4:
        return GL21.GL_FLOAT_MAT2x4;
      case TYPE_FLOAT_MATRIX_3:
        return GL20.GL_FLOAT_MAT3;
      case TYPE_FLOAT_MATRIX_3x2:
        return GL21.GL_FLOAT_MAT3x2;
      case TYPE_FLOAT_MATRIX_3x4:
        return GL21.GL_FLOAT_MAT3x4;
      case TYPE_FLOAT_MATRIX_4:
        return GL20.GL_FLOAT_MAT4;
      case TYPE_FLOAT_MATRIX_4x2:
        return GL21.GL_FLOAT_MAT4x2;
      case TYPE_FLOAT_MATRIX_4x3:
        return GL21.GL_FLOAT_MAT4x3;
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
      case TYPE_SAMPLER_1D:
        return GL20.GL_SAMPLER_1D;
      case TYPE_SAMPLER_1D_SHADOW:
        return GL20.GL_SAMPLER_1D_SHADOW;
      case TYPE_SAMPLER_2D:
        return GL20.GL_SAMPLER_2D;
      case TYPE_SAMPLER_2D_SHADOW:
        return GL20.GL_SAMPLER_2D_SHADOW;
      case TYPE_SAMPLER_3D:
        return GL20.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL20.GL_SAMPLER_CUBE;
    }

    throw new AssertionError();
  }

  static @Nonnull GLUnsignedType unsignedTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL11.GL_UNSIGNED_BYTE:
        return GLUnsignedType.TYPE_UNSIGNED_BYTE;
      case GL11.GL_UNSIGNED_SHORT:
        return GLUnsignedType.TYPE_UNSIGNED_SHORT;
      case GL11.GL_UNSIGNED_INT:
        return GLUnsignedType.TYPE_UNSIGNED_INT;
    }

    throw new AssertionError();
  }

  static int unsignedTypeToGL(
    final @Nonnull GLUnsignedType type)
  {
    switch (type) {
      case TYPE_UNSIGNED_BYTE:
        return GL11.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_SHORT:
        return GL11.GL_UNSIGNED_SHORT;
      case TYPE_UNSIGNED_INT:
        return GL11.GL_UNSIGNED_INT;
    }

    throw new AssertionError();
  }

  private final @Nonnull Log log;

  public GLInterfaceLWJGL30(
    final @Nonnull Log log)
    throws ConstraintError
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "gl30");
  }

  @Override public @Nonnull ArrayBuffer allocateArrayBuffer(
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor)
    throws GLException,
      ConstraintError
  {
    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");

    final long size = descriptor.getSize();
    final long bytes = elements * size;

    this.log.debug("allocate vertex buffer ("
      + elements
      + " elements, "
      + size
      + " bytes per element, "
      + bytes
      + " bytes)");

    final int id = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    GLError.check(this);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bytes, GL15.GL_STREAM_DRAW);
    GLError.check(this);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    this.log.debug("allocated vertex buffer " + id);
    return new ArrayBuffer(id, elements, descriptor);
  }

  @Override public @Nonnull IndexBuffer allocateIndexBuffer(
    final @Nonnull Buffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Buffer");
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

    this.log.debug("allocate index buffer ("
      + indices
      + " elements, "
      + GLUnsignedTypeMeta.getSizeBytes(type)
      + " bytes per element, "
      + bytes
      + " bytes)");

    final int id = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      bytes,
      GL15.GL_STREAM_DRAW);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    this.log.debug("allocated index buffer " + id);
    return new IndexBuffer(id, indices, type);
  }

  @Override public @Nonnull PixelUnpackBuffer allocatePixelUnpackBuffer(
    final long elements,
    final long element_size)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(elements, 1, Long.MAX_VALUE);
    Constraints.constrainRange(element_size, 1, Long.MAX_VALUE);

    final long bytes = element_size * elements;

    this.log.debug("allocate pixel unpack buffer ("
      + elements
      + " elements, "
      + element_size
      + " bytes per element, "
      + bytes
      + " bytes)");

    final int id = GL15.glGenBuffers();
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, id);
    GLError.check(this);
    GL15
      .glBufferData(GL21.GL_PIXEL_UNPACK_BUFFER, bytes, GL15.GL_STREAM_DRAW);
    GLError.check(this);
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);

    this.log.debug("allocated pixel unpack buffer " + id);
    return new PixelUnpackBuffer(id, elements, element_size);
  }

  @Override public @Nonnull Texture2DRGBA allocateTextureRGBA(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(width, 2, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(height, 2, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    this.log.debug("allocate 2D RGBA texture \""
      + name
      + "\" "
      + width
      + "x"
      + height);

    final int texture_id = GL11.glGenTextures();
    final @Nonnull PixelUnpackBuffer buffer =
      this.allocatePixelUnpackBuffer(width * height, 4);

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_S,
      GLInterfaceLWJGL30.textureWrapToGL(wrap_s));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_T,
      GLInterfaceLWJGL30.textureWrapToGL(wrap_t));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MAG_FILTER,
      GLInterfaceLWJGL30.textureFilterToGL(mag_filter));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MIN_FILTER,
      GLInterfaceLWJGL30.textureFilterToGL(min_filter));

    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, buffer.getLocation());

    GL11.glTexImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      GL11.GL_RGBA8,
      width,
      height,
      0,
      GL11.GL_RGBA,
      GL11.GL_UNSIGNED_BYTE,
      0L);

    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    final Texture2DRGBA t =
      new Texture2DRGBA(name, texture_id, buffer, width, height);
    this.log.debug("allocated texture " + t);
    return t;
  }

  @Override public void attachFragmentShader(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(shader, "Fragment shader ID");
    Constraints.constrainArbitrary(
      GL20.glIsShader(shader.getLocation()),
      "ID corresponds to valid fragment shader");

    this.log.debug("attach fragment shader " + program + " " + shader);

    GL20.glAttachShader(program.getLocation(), shader.getLocation());
    GLError.check(this);
  }

  @Override public void attachVertexShader(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(shader, "Vertex shader ID");
    Constraints.constrainArbitrary(
      GL20.glIsShader(shader.getLocation()),
      "ID corresponds to valid vertex shader");

    this.log.debug("attach vertex shader " + program + " " + shader);

    GL20.glAttachShader(program.getLocation(), shader.getLocation());
    GLError.check(this);
  }

  @Override public void bindTexture2DRGBA(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DRGBA texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getLocation());
    GLError.check(this);
  }

  @Override public void clearColorBuffer(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color");
    GL11.glClearColor(color.getX(), color.getY(), color.getZ(), color.getW());
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  @Override public void clearDepthBuffer(
    final float depth)
    throws GLException
  {
    GL11.glClearDepth(depth);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

  @Override public FragmentShader compileFragmentShader(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "Input stream");

    this.log.debug("compile fragment shader \"" + name + "\"");

    final int id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
    GLError.check(this);

    final ByteArrayOutputStream array = new ByteArrayOutputStream();
    final byte buffer[] = new byte[1024];

    for (;;) {
      final int read = stream.read(buffer);
      if (read == -1) {
        break;
      }
      array.write(buffer, 0, read);
    }

    GL20.glShaderSource(id, array.toString());
    GL20.glCompileShader(id);

    final int status = GL20.glGetShader(id, GL20.GL_COMPILE_STATUS);
    if (status == 0) {
      throw new GLCompileException(name, GL20.glGetShaderInfoLog(id, 8192));
    }

    GLError.check(this);
    return new FragmentShader(id, name);
  }

  @Override public @Nonnull VertexShader compileVertexShader(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    this.log.debug("compile vertex shader \"" + name + "\"");

    final int id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
    GLError.check(this);

    final ByteArrayOutputStream array = new ByteArrayOutputStream();
    final byte buffer[] = new byte[1024];

    for (;;) {
      final int read = stream.read(buffer);
      if (read == -1) {
        break;
      }
      array.write(buffer, 0, read);
    }

    GL20.glShaderSource(id, array.toString());
    GL20.glCompileShader(id);

    final int status = GL20.glGetShader(id, GL20.GL_COMPILE_STATUS);
    if (status == 0) {
      throw new GLCompileException(name, GL20.glGetShaderInfoLog(id, 8192));
    }

    GLError.check(this);
    return new VertexShader(id, name);
  }

  @Override public ProgramReference createProgram(
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Program name");

    this.log.debug("create program \"" + name + "\"");

    final int id = GL20.glCreateProgram();
    GLError.check(this);
    return new ProgramReference(id, name);
  }

  @Override public void deleteArrayBuffer(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "id");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("delete vertex buffer " + id);

    GL15.glDeleteBuffers(id.getLocation());
    GLError.check(this);

    this.log.debug("deleted vertex buffer " + id);
  }

  @Override public void deleteFragmentShader(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Shader ID");
    Constraints.constrainArbitrary(
      GL20.glIsShader(id.getLocation()),
      "ID corresponds to valid shader");

    this.log.debug("delete fragment shader " + id);

    GL20.glDeleteShader(id.getLocation());
    GLError.check(this);
  }

  @Override public void deleteIndexBuffer(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    this.log.debug("delete index buffer " + id);

    Constraints.constrainNotNull(id, "id");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    GL15.glDeleteBuffers(id.getLocation());
    GLError.check(this);

    this.log.debug("deleted index buffer " + id);
  }

  @Override public void deletePixelUnpackBuffer(
    final PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "id");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("delete pixel unpack buffer " + id);

    GL15.glDeleteBuffers(id.getLocation());
    GLError.check(this);

    this.log.debug("deleted pixel unpack buffer " + id);
  }

  @Override public void deleteProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program");

    this.log.debug("delete shading program " + program);

    GL20.glDeleteProgram(program.getLocation());
    GLError.check(this);
  }

  @Override public void deleteTexture2DRGBA(
    final @Nonnull Texture2DRGBA texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      GL11.glIsTexture(texture.getLocation()),
      "Texture is valid");

    this.log.debug("delete 2D RGBA texture " + texture);

    GL11.glDeleteTextures(texture.getLocation());
    this.deletePixelUnpackBuffer(texture.getBuffer());
  }

  @Override public void deleteVertexShader(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Shader ID");
    Constraints.constrainArbitrary(
      GL20.glIsShader(id.getLocation()),
      "ID corresponds to valid shader");

    this.log.debug("delete vertex shader " + id);

    GL20.glDeleteShader(id.getLocation());
    GLError.check(this);
  }

  @Override public void disableBlending()
    throws GLException
  {
    GL11.glDisable(GL11.GL_BLEND);
    GLError.check(this);
  }

  @Override public void disableCulling()
    throws GLException
  {
    GL11.glDisable(GL11.GL_CULL_FACE);
    GLError.check(this);
  }

  @Override public void disableDepthTest()
    throws GLException
  {
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GLError.check(this);
  }

  @Override public void disableLineSmoothing()
    throws GLException
  {
    GL11.glDisable(GL11.GL_LINE_SMOOTH);
    GLError.check(this);
  }

  @Override public void disableLogicOperations()
    throws GLException
  {
    GL11.glDisable(GL11.GL_LOGIC_OP);
    GLError.check(this);
  }

  @Override public void disablePolygonSmoothing()
    throws GLException
  {
    GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public void disableScissor()
    throws GLException
  {
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    GLError.check(this);
  }

  @Override public void enableBlending(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    this.enableBlendingSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  @Override public void enableBlendingSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    this.enableBlendingSeparateWithEquationSeparate(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquation.BLEND_EQUATION_ADD,
      BlendEquation.BLEND_EQUATION_ADD);
  }

  @Override public void enableBlendingSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    GL11.glEnable(GL11.GL_BLEND);
    GL20.glBlendEquationSeparate(
      GLInterfaceLWJGL30.blendEquationToGL(equation_rgb),
      GLInterfaceLWJGL30.blendEquationToGL(equation_alpha));
    GL14.glBlendFuncSeparate(
      GLInterfaceLWJGL30.blendFunctionToGL(source_rgb_factor),
      GLInterfaceLWJGL30.blendFunctionToGL(destination_rgb_factor),
      GLInterfaceLWJGL30.blendFunctionToGL(source_alpha_factor),
      GLInterfaceLWJGL30.blendFunctionToGL(destination_alpha_factor));
    GLError.check(this);
  }

  @Override public void enableBlendingWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException
  {
    this.enableBlendingSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      BlendEquation.BLEND_EQUATION_ADD,
      BlendEquation.BLEND_EQUATION_ADD);
  }

  @Override public void enableBlendingWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    this.enableBlendingSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void enableCulling(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    GL11.glEnable(GL11.GL_CULL_FACE);

    switch (faces) {
      case FACE_BACK:
        GL11.glCullFace(GL11.GL_BACK);
        break;
      case FACE_FRONT:
        GL11.glCullFace(GL11.GL_FRONT);
        break;
      case FACE_FRONT_AND_BACK:
        GL11.glCullFace(GL11.GL_FRONT_AND_BACK);
        break;
    }

    switch (order) {
      case FRONT_FACE_CLOCKWISE:
        GL11.glFrontFace(GL11.GL_CW);
        break;
      case FRONT_FACE_COUNTER_CLOCKWISE:
        GL11.glFrontFace(GL11.GL_CCW);
        break;
    }

    GLError.check(this);
  }

  @Override public void enableDepthTest(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");

    int d;
    switch (function) {
      case DEPTH_ALWAYS:
        d = GL11.GL_ALWAYS;
        break;
      case DEPTH_EQUAL:
        d = GL11.GL_EQUAL;
        break;
      case DEPTH_GREATER_THAN:
        d = GL11.GL_GREATER;
        break;
      case DEPTH_GREATER_THAN_OR_EQUAL:
        d = GL11.GL_GEQUAL;
        break;
      case DEPTH_LESS_THAN:
        d = GL11.GL_LESS;
        break;
      case DEPTH_LESS_THAN_OR_EQUAL:
        d = GL11.GL_LEQUAL;
        break;
      case DEPTH_NEVER:
        d = GL11.GL_NEVER;
        break;
      case DEPTH_NOT_EQUAL:
        d = GL11.GL_NOTEQUAL;
        break;
      default:
        /* UNREACHABLE */
        throw new AssertionError("unreachable code: report this bug!");
    }

    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDepthFunc(d);
    GLError.check(this);
  }

  @Override public void enableLineSmoothing()
    throws GLException
  {
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GLError.check(this);
  }

  @Override public void enableLogicOperations(
    final LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    GL11.glEnable(GL11.GL_LOGIC_OP);
    GL11.glLogicOp(GLInterfaceLWJGL30.logicOpToGL(operation));
    GLError.check(this);
  }

  @Override public void enablePolygonSmoothing()
    throws GLException
  {
    GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public void enableScissor(
    final @Nonnull VectorM2I position,
    final @Nonnull VectorM2I dimensions)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(position, "Scissor region position");
    Constraints.constrainNotNull(dimensions, "Scissor region dimensions");

    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    GL11.glScissor(position.x, position.y, dimensions.x, dimensions.y);
    GLError.check(this);
  }

  @Override public void getAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, Attribute> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getLocation();
    final int max = GL20.glGetProgram(id, GL20.GL_ACTIVE_ATTRIBUTES);
    final int len =
      GL20.glGetProgram(id, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
    GLError.check(this);

    final ByteBuffer buffer_name =
      ByteBuffer.allocateDirect(len).order(ByteOrder.nativeOrder());
    final IntBuffer buffer_length =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    final IntBuffer buffer_size =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    final IntBuffer buffer_type =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();

    /*
     * Note: some drivers will return built-in attributes here (such as
     * "gl_Vertex") but their locations are -1, so must be explicitly ignored.
     */

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      GL20.glGetActiveAttrib(
        id,
        index,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      GLError.check(this);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLInterfaceLWJGL30.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = GL20.glGetAttribLocation(id, name);
      GLError.check(this);

      if (location == -1) {
        this.log.debug("driver returned active attribute '"
          + name
          + "' with location -1, ignoring");
        continue;
      }

      assert out.containsKey(name) == false;
      out.put(name, new Attribute(program, index, location, name, type));
    }
  }

  @Override public int getError()
  {
    return GL11.glGetError();
  }

  @Override public @Nonnull String getRenderer()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_RENDERER);
    GLError.check(this);
    return x;
  }

  @Override public TextureUnit[] getTextureUnits()
    throws GLException
  {
    final int max = GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
    GLError.check(this);

    this.log.debug("implementation supports " + max + " texture units");

    final TextureUnit[] u = new TextureUnit[max];
    for (int index = 0; index < max; ++index) {
      u[index] = new TextureUnit(index);
    }

    return u;
  }

  @Override public void getUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, Uniform> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getLocation();
    final int max = GL20.glGetProgram(id, GL20.GL_ACTIVE_UNIFORMS);
    final int len = GL20.glGetProgram(id, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    GLError.check(this);

    final ByteBuffer buffer_name =
      ByteBuffer.allocateDirect(len).order(ByteOrder.nativeOrder());
    final IntBuffer buffer_length =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    final IntBuffer buffer_size =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    final IntBuffer buffer_type =
      ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      GL20.glGetActiveUniform(
        id,
        index,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      GLError.check(this);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLInterfaceLWJGL30.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location =
        GL20.glGetUniformLocation(program.getLocation(), name);
      GLError.check(this);

      assert (out.containsKey(name) == false);
      out.put(name, new Uniform(program, index, location, name, type));
    }
  }

  @Override public @Nonnull String getVendor()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_VENDOR);
    GLError.check(this);
    return x;
  }

  @Override public @Nonnull String getVersion()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_VERSION);
    GLError.check(this);
    return x;
  }

  @Override public void linkProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    this.log.debug("link program " + program);

    GL20.glLinkProgram(program.getLocation());
    final int status =
      GL20.glGetProgram(program.getLocation(), GL20.GL_LINK_STATUS);
    if (status == 0) {
      throw new GLCompileException(
        program.getName(),
        GL20.glGetProgramInfoLog(program.getLocation(), 8192));
    }

    GLError.check(this);
  }

  @Override public @Nonnull ByteBuffer mapArrayBufferRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map array buffer " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getLocation());
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
    return b;
  }

  @Override public @Nonnull ArrayBufferWritableMap mapArrayBufferWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map array buffer " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getLocation());
    GL15.glBufferData(
      GL15.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);

    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new ArrayBufferWritableMap(id, b);
  }

  @Override public @Nonnull IndexBufferReadableMap mapIndexBufferRead(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map index buffer " + id);

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferReadableMap(id, b);
  }

  @Override public @Nonnull IndexBufferWritableMap mapIndexBufferWrite(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map index buffer " + id);

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);

    final ByteBuffer b =
      GL15
        .glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferWritableMap(id, b);
  }

  @Override public ByteBuffer mapPixelUnpackBufferRead(
    final PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Pixel unpack buffer ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map pixel unpack buffer " + id);

    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    final ByteBuffer b =
      GL15.glMapBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, GL15.GL_READ_ONLY, null);
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
    return b;
  }

  @Override public ByteBuffer mapPixelUnpackBufferWrite(
    final PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Pixel unpack buffer ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("map pixel unpack buffer " + id);

    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GL15.glBufferData(
      GL21.GL_PIXEL_UNPACK_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);

    final ByteBuffer b =
      GL15.glMapBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, GL15.GL_WRITE_ONLY, null);
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
    return b;
  }

  @Override public void noProgram()
    throws GLException
  {
    GL20.glUseProgram(0);
    GLError.check(this);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    final int active = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
    GLError.check(this);
    return active == program.getLocation();
  }

  @Override public void putUniformFloat(
    final @Nonnull Uniform uniform,
    final float value)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT,
      "Uniform type is float");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform1f(uniform.getLocation(), value);
    GLError.check(this);
  }

  @Override public void putUniformMatrix3x3f(
    final @Nonnull Uniform uniform,
    final @Nonnull MatrixM3x3F matrix)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_3,
      "Uniform type is mat3");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniformMatrix3(
      uniform.getLocation(),
      false,
      MatrixM3x3F.floatBuffer(matrix));
    GLError.check(this);
  }

  @Override public void putUniformMatrix4x4f(
    final @Nonnull Uniform uniform,
    final @Nonnull MatrixM4x4F matrix)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_4,
      "Uniform type is mat4");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniformMatrix4(
      uniform.getLocation(),
      false,
      MatrixM4x4F.floatBuffer(matrix));
    GLError.check(this);
  }

  @Override public void putUniformTextureUnit(
    final @Nonnull Uniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_SAMPLER_2D,
      "Uniform type is sampler_2d");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform1i(uniform.getLocation(), unit.getIndex());
    GLError.check(this);
  }

  @Override public void putUniformVector3f(
    final @Nonnull Uniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_3,
      "Uniform type is vec3");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform3f(
      uniform.getLocation(),
      vector.getX(),
      vector.getY(),
      vector.getZ());
    GLError.check(this);
  }

  @Override public void putUniformVector4f(
    final @Nonnull Uniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_4,
      "Uniform type is vec4");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform4f(
      uniform.getLocation(),
      vector.getX(),
      vector.getY(),
      vector.getZ(),
      vector.getW());
    GLError.check(this);
  }

  @Override public void setLineWidth(
    final float width)
    throws GLException
  {
    GL11.glLineWidth(width);
    GLError.check(this);
  }

  @Override public void setPolygonMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(mode, "Polygon mode");

    int f;
    switch (faces) {
      case FACE_BACK:
        f = GL11.GL_BACK;
        break;
      case FACE_FRONT:
        f = GL11.GL_FRONT;
        break;
      case FACE_FRONT_AND_BACK:
        f = GL11.GL_FRONT_AND_BACK;
        break;
      default:
        /* UNREACHABLE */
        throw new AssertionError("unreachable code: report this bug!");
    }

    switch (mode) {
      case POLYGON_FILL:
        GL11.glPolygonMode(f, GL11.GL_FILL);
        break;
      case POLYGON_LINES:
        GL11.glPolygonMode(f, GL11.GL_LINE);
        break;
      case POLYGON_POINTS:
        GL11.glPolygonMode(f, GL11.GL_POINT);
        break;
    }

    GLError.check(this);
  }

  @Override public void setViewport(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(position, "Viewport position");
    Constraints.constrainNotNull(dimensions, "Viewport dimensions");

    GL11.glViewport(
      position.getX(),
      position.getY(),
      dimensions.getX(),
      dimensions.getY());
    GLError.check(this);
  }

  @Override public void unmapArrayBuffer(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("unmap array buffer " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getLocation());
    GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void unmapIndexBuffer(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("unmap index buffer " + id);

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GL15.glUnmapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void unmapPixelUnpackBuffer(
    final @Nonnull PixelUnpackBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Pixel unpack buffer ID");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(id.getLocation()),
      "ID corresponds to OpenGL buffer");

    this.log.debug("unmap array buffer " + id);

    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GL15.glUnmapBuffer(GL21.GL_PIXEL_UNPACK_BUFFER);
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void updateTexture2DRGBA(
    final @Nonnull Texture2DRGBA texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      GL11.glIsTexture(texture.getLocation()),
      "Texture ID is a valid texture");
    Constraints.constrainArbitrary(
      GL15.glIsBuffer(texture.getBuffer().getLocation()),
      "Pixel buffer is valid");

    this.log.debug("update 2D RGBA texture " + texture);

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getLocation());
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, texture
      .getBuffer()
      .getLocation());
    GL11.glTexSubImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      0,
      0,
      texture.getWidth(),
      texture.getHeight(),
      GL11.GL_RGBA,
      GL11.GL_UNSIGNED_BYTE,
      0L);
    GL15.glBindBuffer(GL21.GL_PIXEL_UNPACK_BUFFER, 0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  @Override public void useProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      GL20.glIsProgram(program.getLocation()),
      "ID corresponds to valid program");

    GL20.glUseProgram(program.getLocation());
    GLError.check(this);
  }
}
