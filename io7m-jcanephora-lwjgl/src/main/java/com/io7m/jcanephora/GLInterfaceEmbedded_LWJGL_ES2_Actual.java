package com.io7m.jcanephora;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * The non-public implementation of the ES2-compatible interface.
 */

@NotThreadSafe class GLInterfaceEmbedded_LWJGL_ES2_Actual implements
  GLInterfaceEmbedded
{
  /**
   * The size of the integer cache, in bytes.
   */

  private static final int INTEGER_CACHE_SIZE = 16 * 4;

  static @Nonnull BlendEquationEmbedded blendEquationEmbeddedFromGL(
    final int e)
  {
    switch (e) {
      case GL14.GL_FUNC_ADD:
        return BlendEquationEmbedded.BLEND_EQUATION_ADD;
      case GL14.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquationEmbedded.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL14.GL_FUNC_SUBTRACT:
        return BlendEquationEmbedded.BLEND_EQUATION_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static int blendEquationEmbeddedToGL(
    final @Nonnull BlendEquationEmbedded e)
  {
    switch (e) {
      case BLEND_EQUATION_ADD:
        return GL14.GL_FUNC_ADD;
      case BLEND_EQUATION_REVERSE_SUBTRACT:
        return GL14.GL_FUNC_REVERSE_SUBTRACT;
      case BLEND_EQUATION_SUBTRACT:
        return GL14.GL_FUNC_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull BlendFunction blendFunctionFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int blendFunctionToGL(
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
  }

  static @Nonnull Primitives primitiveFromGL(
    final int code)
  {
    switch (code) {
      case GL11.GL_LINES:
        return Primitives.PRIMITIVE_LINES;
      case GL11.GL_LINE_LOOP:
        return Primitives.PRIMITIVE_LINE_LOOP;
      case GL11.GL_POINTS:
        return Primitives.PRIMITIVE_POINTS;
      case GL11.GL_TRIANGLES:
        return Primitives.PRIMITIVE_TRIANGLES;
      case GL11.GL_TRIANGLE_STRIP:
        return Primitives.PRIMITIVE_TRIANGLE_STRIP;
    }

    throw new UnreachableCodeException();
  }

  static int primitiveToGL(
    final Primitives p)
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
    }

    throw new UnreachableCodeException();
  }

  static int scalarTypeToGL(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL11.GL_BYTE;
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

  static @Nonnull TextureFilter textureFilterFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL11.GL_LINEAR:
        return TextureFilter.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
        return TextureFilter.TEXTURE_FILTER_NEAREST;
    }

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
  }

  static @Nonnull TextureWrap textureWrapFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  static int textureWrapToGL(
    final @Nonnull TextureWrap wrap)
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
      case GL20.GL_FLOAT_MAT3:
        return Type.TYPE_FLOAT_MATRIX_3;
      case GL20.GL_FLOAT_MAT4:
        return Type.TYPE_FLOAT_MATRIX_4;
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
      case GL20.GL_SAMPLER_2D:
        return Type.TYPE_SAMPLER_2D;
      case GL20.GL_SAMPLER_2D_SHADOW:
        return Type.TYPE_SAMPLER_2D_SHADOW;
      case GL20.GL_SAMPLER_3D:
        return Type.TYPE_SAMPLER_3D;
      case GL20.GL_SAMPLER_CUBE:
        return Type.TYPE_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
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
      case TYPE_SAMPLER_2D_SHADOW:
        return GL20.GL_SAMPLER_2D_SHADOW;
      case TYPE_SAMPLER_3D:
        return GL20.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL20.GL_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
  }

  static UsageHintEmbedded usageHintEmbeddedFromGL(
    final int hint)
  {
    switch (hint) {
      case GL15.GL_DYNAMIC_DRAW:
        return UsageHintEmbedded.USAGE_DYNAMIC_DRAW;
      case GL15.GL_STATIC_DRAW:
        return UsageHintEmbedded.USAGE_STATIC_DRAW;
      case GL15.GL_STREAM_DRAW:
        return UsageHintEmbedded.USAGE_STREAM_DRAW;
    }

    throw new UnreachableCodeException();
  }

  static int usageHintEmbeddedToGL(
    final UsageHintEmbedded hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_DRAW:
        return GL15.GL_DYNAMIC_DRAW;
      case USAGE_STATIC_DRAW:
        return GL15.GL_STATIC_DRAW;
      case USAGE_STREAM_DRAW:
        return GL15.GL_STREAM_DRAW;
    }

    throw new UnreachableCodeException();
  }

  protected final @Nonnull Log log;
  private final int            line_aliased_min_width;
  private final int            line_aliased_max_width;
  private final int            line_smooth_max_width;
  private final int            line_smooth_min_width;
  private boolean              line_smoothing;
  private final int            point_min_width;
  private final int            point_max_width;
  private final TextureUnit[]  texture_units;
  private final ByteBuffer     integer_cache_buffer;
  private final IntBuffer      integer_cache;

  public GLInterfaceEmbedded_LWJGL_ES2_Actual(
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "lwjgl30");

    this.texture_units = this.textureGetUnitsCache();
    this.line_smoothing = false;

    this.integer_cache_buffer =
      ByteBuffer.allocateDirect(
        GLInterfaceEmbedded_LWJGL_ES2_Actual.INTEGER_CACHE_SIZE).order(
        ByteOrder.nativeOrder());
    this.integer_cache = this.integer_cache_buffer.asIntBuffer();

    this.integer_cache.rewind();
    GL11.glGetInteger(GL12.GL_ALIASED_LINE_WIDTH_RANGE, this.integer_cache);
    this.line_aliased_min_width = this.integer_cache.get();
    this.line_aliased_max_width = this.integer_cache.get();
    GLError.check(this);

    this.integer_cache.rewind();
    GL11.glGetInteger(GL12.GL_SMOOTH_LINE_WIDTH_RANGE, this.integer_cache);
    this.line_smooth_min_width = this.integer_cache.get();
    this.line_smooth_max_width = this.integer_cache.get();
    GLError.check(this);

    this.integer_cache.rewind();
    GL11.glGetInteger(GL12.GL_ALIASED_POINT_SIZE_RANGE, this.integer_cache);
    this.point_min_width = this.integer_cache.get();
    this.point_max_width = this.integer_cache.get();
    GLError.check(this);
  }

  @Override public @Nonnull ArrayBuffer arrayBufferAllocate(
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

    this.log.debug("vertex-buffer: allocate ("
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

    this.log.debug("vertex-buffer: allocated " + id);
    return new ArrayBuffer(id, elements, descriptor);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.getGLName());
    GLError.check(this);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final boolean bound = this.arrayBufferIsBound(buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    final ArrayBufferDescriptor d = buffer.getDescriptor();
    final ArrayBufferAttribute dba =
      d.getAttribute(buffer_attribute.getName());

    final boolean same_array = dba == buffer_attribute;
    Constraints.constrainArbitrary(
      same_array,
      "Buffer attribute belongs to the array buffer");

    final boolean same_type =
      GLScalarTypeMeta.shaderTypeConvertible(
        dba.getType(),
        dba.getElements(),
        program_attribute.getType());
    Constraints.constrainArbitrary(
      same_type,
      "Buffer attribute is of the same type as the program attribute");

    final int program_attrib_id = program_attribute.getLocation();
    final int count = buffer_attribute.getElements();
    final int type =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.scalarTypeToGL(buffer_attribute
        .getType());
    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset = d.getAttributeOffset(buffer_attribute.getName());

    GL20.glEnableVertexAttribArray(program_attrib_id);
    GL20.glVertexAttribPointer(
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
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: delete " + id);

    GL15.glDeleteBuffers(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
    GLError.check(this);
    return b == id.getGLName();
  }

  @Override public void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = this.arrayBufferIsBound(buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferDescriptor d = buffer.getDescriptor();
    final ArrayBufferAttribute ba =
      d.getAttribute(buffer_attribute.getName());

    final boolean same_array = ba == buffer_attribute;
    Constraints.constrainArbitrary(
      same_array,
      "Buffer attribute belongs to the array buffer");

    GL20.glDisableVertexAttribArray(program_attribute.getLocation());
    GLError.check(this);
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainNotNull(data, "Array data");

    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = this.arrayBufferIsBound(buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    GL15.glBufferSubData(
      GL15.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetData());
    GLError.check(this);
  }

  @Override public void blendingDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_BLEND);
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
    this.blendingEnableSeparateWithEquationSeparateEmbedded(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationEmbedded.BLEND_EQUATION_ADD,
      BlendEquationEmbedded.BLEND_EQUATION_ADD);
  }

  @Override public void blendingEnableSeparateWithEquationSeparateEmbedded(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationEmbedded equation_rgb,
    final @Nonnull BlendEquationEmbedded equation_alpha)
    throws ConstraintError,
      GLException
  {
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

    Constraints.constrainArbitrary(
      destination_rgb_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    Constraints.constrainArbitrary(
      destination_alpha_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination alpha factor not SOURCE_ALPHA_SATURATE");

    GL11.glEnable(GL11.GL_BLEND);
    GL20.glBlendEquationSeparate(
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendEquationEmbeddedToGL(equation_rgb),
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendEquationEmbeddedToGL(equation_alpha));
    GL14.glBlendFuncSeparate(
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendFunctionToGL(source_rgb_factor),
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendFunctionToGL(destination_rgb_factor),
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendFunctionToGL(source_alpha_factor),
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .blendFunctionToGL(destination_alpha_factor));
    GLError.check(this);
  }

  @Override public void blendingEnableWithEquationEmbedded(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationEmbedded equation)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparateWithEquationSeparateEmbedded(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparateEmbedded(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationEmbedded equation_rgb,
    final @Nonnull BlendEquationEmbedded equation_alpha)
    throws ConstraintError,
      GLException
  {
    this.blendingEnableSeparateWithEquationSeparateEmbedded(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      GLException
  {
    final boolean e = GL11.glGetBoolean(GL11.GL_BLEND);
    GLError.check(this);
    return e;
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException
  {
    GL11.glClearColor(r, g, b, 1.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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
    GL11.glClearColor(r, g, b, a);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException
  {
    GL11.glColorMask(r, g, b, a);
    GLError.check(this);
  }

  private ByteBuffer colorBufferMaskStatus()
    throws GLException
  {
    final ByteBuffer b =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());
    GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK, b);
    GLError.check(this);
    return b;
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws GLException
  {
    return this.colorBufferMaskStatus().get(3) == GL11.GL_TRUE;
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws GLException
  {
    return this.colorBufferMaskStatus().get(2) == GL11.GL_TRUE;
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws GLException
  {
    return this.colorBufferMaskStatus().get(1) == GL11.GL_TRUE;
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws GLException
  {
    return this.colorBufferMaskStatus().get(0) == GL11.GL_TRUE;
  }

  @Override public void cullingDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_CULL_FACE);
    GLError.check(this);
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.faceSelectionToGL(faces);
    final int oi =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.faceWindingOrderToGL(order);

    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glCullFace(fi);
    GL11.glFrontFace(oi);
    GLError.check(this);
  }

  @Override public boolean cullingIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_CULL_FACE);
    GLError.check(this);
    return e;
  }

  @Override public void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glClearDepth(depth);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

  @Override public void depthBufferDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GLError.check(this);
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.depthFunctionToGL(function);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDepthFunc(d);
    GLError.check(this);
  }

  @Override public int depthBufferGetBits()
    throws GLException
  {
    final int framebuffer = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
    GLError.check(this);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits = GL11.glGetInteger(GL11.GL_DEPTH_BITS);
      GLError.check(this);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    final int type =
      GL30.glGetFramebufferAttachmentParameter(
        GL30.GL_FRAMEBUFFER,
        GL30.GL_DEPTH_ATTACHMENT,
        GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
    GLError.check(this);

    if (type == GL11.GL_NONE) {
      return 0;
    }

    /**
     * If there's a depth attachment, check the size of it.
     */

    final int bits =
      GL30.glGetFramebufferAttachmentParameter(
        GL30.GL_FRAMEBUFFER,
        GL30.GL_DEPTH_ATTACHMENT,
        GL30.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE);
    GLError.check(this);
    return bits;
  }

  @Override public boolean depthBufferIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
    GLError.check(this);
    return e;
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glDepthMask(false);
    GLError.check(this);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glDepthMask(true);
    GLError.check(this);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws GLException
  {
    final boolean b = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
    GLError.check(this);
    return b;
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Drawing mode");
    Constraints.constrainNotNull(indices, "Index buffer");
    Constraints.constrainArbitrary(
      indices.resourceIsDeleted() == false,
      "Index buffer not deleted");

    final int index_id = indices.getGLName();
    final int index_count = (int) indices.getRange().getInterval();
    final int mode_gl =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.primitiveToGL(mode);
    final int type =
      GLInterfaceEmbedded_LWJGL_ES2_Actual
        .unsignedTypeToGL(indices.getType());

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index_id);
    GL11.glDrawElements(mode_gl, index_count, type, 0L);
    GLError.check(this);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL11.GL_INVALID_OPERATION;
  }

  @Override public void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Fragment shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    this.log.debug("fragment-shader: attach " + program + " " + shader);

    GL20.glAttachShader(program.getGLName(), shader.getGLName());
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
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "Input stream");

    this.log.debug("fragment-shader: compile \"" + name + "\"");

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

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Fragment shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    this.log.debug("fragment-shader: delete " + id);

    GL20.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public @Nonnull Framebuffer framebufferAllocate(
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException
  {
    final Framebuffer buffer = this.framebufferMake();

    Constraints.constrainNotNull(attachments, "Framebuffer attachments");
    Constraints.constrainRange(
      attachments.length,
      1,
      Long.MAX_VALUE,
      "Framebuffer attachments length");

    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer.getGLName());
    GLError.check(this);

    /**
     * Attach all framebuffer storage.
     */

    try {
      boolean have_depth = false;
      boolean have_color = false;
      final Set<Integer> color_indices = new TreeSet<Integer>();

      final int max_color = GL11.glGetInteger(GL30.GL_MAX_COLOR_ATTACHMENTS);
      GLError.check(this);

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

            Constraints.constrainArbitrary(
              color_indices.contains(Integer.valueOf(index)) == false,
              "Color buffer not already present at this index");

            final Texture2DStatic texture = color.getTexture();
            Constraints.constrainArbitrary(
              texture.resourceIsDeleted() == false,
              "Texture is not deleted");

            color_indices.add(Integer.valueOf(index));
            have_color = true;

            GL30.glFramebufferTexture2D(
              GL30.GL_FRAMEBUFFER,
              GL30.GL_COLOR_ATTACHMENT0 + index,
              GL11.GL_TEXTURE_2D,
              color.getTexture().getGLName(),
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

            final RenderbufferD24S8 depth_buffer = depth.getRenderbuffer();

            Constraints.constrainArbitrary(
              depth_buffer.resourceIsDeleted() == false,
              "Depth+Stencil buffer is not deleted");

            final int id = depth_buffer.getGLName();

            GL30.glFramebufferRenderbuffer(
              GL30.GL_FRAMEBUFFER,
              GL30.GL_DEPTH_ATTACHMENT,
              GL30.GL_RENDERBUFFER,
              id);
            GLError.check(this);

            GL30.glFramebufferRenderbuffer(
              GL30.GL_FRAMEBUFFER,
              GL30.GL_STENCIL_ATTACHMENT,
              GL30.GL_RENDERBUFFER,
              id);
            GLError.check(this);

            this.log.debug("framebuffer: attach depth+stencil "
              + buffer
              + " "
              + depth);
            break;
          }
          default:
            throw new UnreachableCodeException();
        }
      }

      Constraints.constrainArbitrary(
        have_color,
        "Framebuffer has at least one color buffer");

      /**
       * Check framebuffer status.
       */

      final int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
      GLError.check(this);

      switch (status) {
        case GL30.GL_FRAMEBUFFER_COMPLETE:
          break;
        case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
          throw new GLException(
            GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT,
            "Framebuffer is incomplete");
        case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
          throw new GLException(
            GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT,
            "Framebuffer is incomplete - missing image attachment");
        case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
          throw new GLException(
            GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER,
            "Framebuffer is incomplete - missing draw buffer");
        case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
          throw new GLException(
            GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER,
            "Framebuffer is incomplete - missing read buffer");
        case GL30.GL_FRAMEBUFFER_UNSUPPORTED:
          throw new GLException(
            GL30.GL_FRAMEBUFFER_UNSUPPORTED,
            "Framebuffer configuration unsupported");
        default:
          throw new GLException(status, "Unknown framebuffer error");
      }

      return buffer;
    } finally {
      GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
      GLError.check(this);
    }
  }

  @Override public void framebufferBind(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer.getGLName());
    GLError.check(this);
  }

  @Override public void framebufferDelete(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    this.log.debug("framebuffer: delete " + buffer);

    GL30.glDeleteFramebuffers(buffer.getGLName());
    GLError.check(this);
    buffer.setDeleted();
  }

  private @Nonnull Framebuffer framebufferMake()
    throws ConstraintError,
      GLException
  {
    final int id = GL30.glGenFramebuffers();
    GLError.check(this);
    this.log.debug("framebuffer: allocated " + id);
    return new Framebuffer(id);
  }

  @Override public void framebufferUnbind()
    throws GLException
  {
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    GLError.check(this);
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_BYTE;
    if (buffer.getRange().getInterval() > 0xff) {
      type = GLUnsignedType.TYPE_UNSIGNED_SHORT;
    }
    if (buffer.getRange().getInterval() > 0xffff) {
      type = GLUnsignedType.TYPE_UNSIGNED_INT;
    }

    return this.indexBufferAllocateType(type, indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(type, "Index type");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    final long size = GLUnsignedTypeMeta.getSizeBytes(type);
    final long bytes = indices * size;

    this.log.debug("index-buffer: allocate ("
      + indices
      + " elements, "
      + size
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

    this.log.debug("index-buffer: allocated " + id);
    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: delete " + id);

    GL15.glDeleteBuffers(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBuffer buffer,
    final @Nonnull IndexBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Index buffer");
    Constraints.constrainNotNull(data, "Index data");

    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Index buffer not deleted");

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer.getGLName());
    GL15.glBufferSubData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetData());
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

    GL11.glLineWidth(width);
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
    GL11.glDisable(GL11.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = false;
  }

  @Override public void lineSmoothingEnable()
    throws GLException
  {
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = true;
  }

  @Override public int metaGetError()
  {
    return GL11.glGetError();
  }

  @Override public @Nonnull String metaGetRenderer()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_RENDERER);
    GLError.check(this);
    return x;
  }

  @Override public @Nonnull String metaGetVendor()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_VENDOR);
    GLError.check(this);
    return x;
  }

  @Override public @Nonnull String metaGetVersion()
    throws GLException
  {
    final String x = GL11.glGetString(GL11.GL_VERSION);
    GLError.check(this);
    return x;
  }

  @Override public int pointGetMaximumWidth()
  {
    return this.point_max_width;
  }

  @Override public int pointGetMinimumWidth()
  {
    return this.point_min_width;
  }

  @Override public void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    GL20.glUseProgram(program.getGLName());
    GLError.check(this);
  }

  @Override public ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Program name");

    this.log.debug("program: create \"" + name + "\"");

    final int id = GL20.glCreateProgram();
    GLError.check(this);
    return new ProgramReference(id, name);
  }

  @Override public void programDeactivate()
    throws GLException
  {
    GL20.glUseProgram(0);
    GLError.check(this);
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    this.log.debug("program: delete " + program);

    GL20.glDeleteProgram(program.getGLName());
    program.setDeleted();
    GLError.check(this);
  }

  @Override public void programGetAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
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
      final GLType.Type type =
        GLInterfaceEmbedded_LWJGL_ES2_Actual.typeFromGL(type_raw);

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
      out.put(
        name,
        new ProgramAttribute(program, index, location, name, type));
    }
  }

  @Override public int programGetMaximimActiveAttributes()
    throws GLException
  {
    final int max = GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS);
    GLError.check(this);

    this.log.debug("implementation supports " + max + " active attributes");
    return max;
  }

  @Override public void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
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
      final GLType.Type type =
        GLInterfaceEmbedded_LWJGL_ES2_Actual.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location =
        GL20.glGetUniformLocation(program.getGLName(), name);
      GLError.check(this);

      if (location == -1) {
        this.log.debug("driver returned active uniform '"
          + name
          + "' with location -1, ignoring");
        continue;
      }

      assert (out.containsKey(name) == false);
      out.put(name, new ProgramUniform(program, index, location, name, type));
    }
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
    GLError.check(this);
    return active == program.getGLName();
  }

  @Override public void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    this.log.debug("program: link " + program);

    GL20.glLinkProgram(program.getGLName());
    final int status =
      GL20.glGetProgram(program.getGLName(), GL20.GL_LINK_STATUS);
    if (status == 0) {
      throw new GLCompileException(
        program.getName(),
        GL20.glGetProgramInfoLog(program.getGLName(), 8192));
    }

    GLError.check(this);
  }

  @Override public void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
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

  @Override public void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
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
      matrix.getFloatBuffer());
    GLError.check(this);
  }

  @Override public void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
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
      matrix.getFloatBuffer());
    GLError.check(this);
  }

  @Override public void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
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

  @Override public void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    GLError.check(this);
  }

  @Override public void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_INTEGER_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      this.programIsActive(uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    GLError.check(this);
  }

  @Override public void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
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
    Constraints.constrainRange(width, 1, Integer.MAX_VALUE);
    Constraints.constrainRange(height, 1, Integer.MAX_VALUE);

    this.log.debug("renderbuffer-d24s8: allocate " + width + "x" + height);

    final int id = GL30.glGenRenderbuffers();
    GLError.check(this);

    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
    GLError.check(this);
    GL30.glRenderbufferStorage(
      GL30.GL_RENDERBUFFER,
      GL30.GL_DEPTH24_STENCIL8,
      width,
      height);
    GLError.check(this);
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
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
    Constraints.constrainNotNull(buffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    this.log.debug("renderbuffer-d24s8: delete " + buffer);

    GL30.glDeleteRenderbuffers(buffer.getGLName());
    buffer.setDeleted();
    GLError.check(this);
  }

  @Override public void scissorDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    GLError.check(this);
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
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

    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    GL11.glScissor(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLError.check(this);
  }

  @Override public boolean scissorIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
    GLError.check(this);
    return e;
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    final int framebuffer = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
    GLError.check(this);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits = GL11.glGetInteger(GL11.GL_STENCIL_BITS);
      GLError.check(this);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    final int type =
      GL30.glGetFramebufferAttachmentParameter(
        GL30.GL_FRAMEBUFFER,
        GL30.GL_STENCIL_ATTACHMENT,
        GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
    GLError.check(this);
    if (type == GL11.GL_NONE) {
      return 0;
    }

    /**
     * If there's a stencil attachment, check the size of it.
     */

    final int bits =
      GL30.glGetFramebufferAttachmentParameter(
        GL30.GL_FRAMEBUFFER,
        GL30.GL_STENCIL_ATTACHMENT,
        GL30.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE);
    GLError.check(this);
    return bits;
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureType type,
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

    this.log.debug("texture-2D-static: allocate \""
      + name
      + "\" "
      + width
      + "x"
      + height);

    final int texture_id = GL11.glGenTextures();
    GLError.check(this);

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_S,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureWrapToGL(wrap_s));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_T,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureWrapToGL(wrap_t));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MAG_FILTER,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureFilterToGL(mag_filter));
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MIN_FILTER,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureFilterToGL(min_filter));

    GL11.glTexImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureTypeToFormatGL(type),
      width,
      height,
      0,
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureTypeToFormatGL(type),
      GLInterfaceEmbedded_LWJGL_ES2_Actual.textureTypeToTypeGL(type),
      (ByteBuffer) null);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    final Texture2DStatic t =
      new Texture2DStatic(name, type, texture_id, width, height);
    this.log.debug("texture-2D-static: allocated " + t);
    return t;
  }

  private static int textureTypeToTypeGL(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_ALPHA_8_1BPP:
      case TEXTURE_TYPE_LUMINANCE_8_1BPP:
      case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return GL11.GL_UNSIGNED_BYTE;
      case TEXTURE_TYPE_RGBA_4444_2BPP:
        return GL12.GL_UNSIGNED_SHORT_4_4_4_4;
      case TEXTURE_TYPE_RGBA_5551_2BPP:
        return GL12.GL_UNSIGNED_SHORT_5_5_5_1;
      case TEXTURE_TYPE_RGB_565_2BPP:
        return GL12.GL_UNSIGNED_SHORT_5_6_5;
    }

    throw new UnreachableCodeException();
  }

  private static int textureTypeToFormatGL(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_ALPHA_8_1BPP:
        return GL11.GL_ALPHA;
      case TEXTURE_TYPE_LUMINANCE_8_1BPP:
        return GL11.GL_LUMINANCE;
      case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
        return GL11.GL_LUMINANCE_ALPHA;
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return GL11.GL_RGBA;
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return GL11.GL_RGB;
    }

    throw new UnreachableCodeException();
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGLName());
    GLError.check(this);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    this.log.debug("texture-2D-static: delete " + texture);

    GL11.glDeleteTextures(texture.getGLName());
    texture.setDeleted();
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());

    final int e = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    GLError.check(this);

    return e == texture.getGLName();
  }

  @Override public int textureGetMaximumSize()
    throws GLException
  {
    final int size = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
    GLError.check(this);
    return size;
  }

  @Override public TextureUnit[] textureGetUnits()
    throws GLException
  {
    return this.texture_units;
  }

  private TextureUnit[] textureGetUnitsCache()
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

  @Override public void textureUnitUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    GLError.check(this);
  }

  @Override public void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Vertex shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    this.log.debug("vertex-shader: attach " + program + " " + shader);

    GL20.glAttachShader(program.getGLName(), shader.getGLName());
    GLError.check(this);
  }

  @Override public @Nonnull VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    this.log.debug("vertex-shader: compile \"" + name + "\"");

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
    GLError.check(this);
    GL20.glCompileShader(id);
    GLError.check(this);
    final int status = GL20.glGetShader(id, GL20.GL_COMPILE_STATUS);
    GLError.check(this);
    if (status == 0) {
      throw new GLCompileException(name, GL20.glGetShaderInfoLog(id, 8192));
    }

    return new VertexShader(id, name);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Vertex shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    this.log.debug("vertex-shader: delete " + id);

    GL20.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(position, "Viewport position");
    Constraints.constrainNotNull(dimensions, "Viewport dimensions");
    Constraints.constrainRange(
      dimensions.getXI(),
      0,
      Integer.MAX_VALUE,
      "Viewport width");
    Constraints.constrainRange(
      dimensions.getYI(),
      0,
      Integer.MAX_VALUE,
      "Viewport height");

    GL11.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLError.check(this);
  }
}
