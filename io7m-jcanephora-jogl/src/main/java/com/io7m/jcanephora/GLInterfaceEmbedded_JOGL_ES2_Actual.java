/*
 * Copyright © 2012 http://io7m.com
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.jogamp.common.nio.Buffers;

/**
 * The non-public implementation of the ES2-based interface.
 */

@NotThreadSafe class GLInterfaceEmbedded_JOGL_ES2_Actual implements
  GLInterfaceEmbedded
{
  /**
   * The size of the integer cache, in bytes.
   */

  private static final int INTEGER_CACHE_SIZE = 16 * 4;

  static final @Nonnull BlendEquationEmbedded blendEquationEmbeddedFromGL(
    final int e)
  {
    switch (e) {
      case GL.GL_FUNC_ADD:
        return BlendEquationEmbedded.BLEND_EQUATION_ADD;
      case GL.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquationEmbedded.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL.GL_FUNC_SUBTRACT:
        return BlendEquationEmbedded.BLEND_EQUATION_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static final int blendEquationEmbeddedToGL(
    final @Nonnull BlendEquationEmbedded e)
  {
    switch (e) {
      case BLEND_EQUATION_ADD:
        return GL.GL_FUNC_ADD;
      case BLEND_EQUATION_REVERSE_SUBTRACT:
        return GL.GL_FUNC_REVERSE_SUBTRACT;
      case BLEND_EQUATION_SUBTRACT:
        return GL.GL_FUNC_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull BlendFunction blendFunctionFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int blendFunctionToGL(
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

    throw new UnreachableCodeException();
  }

  static final DepthFunction depthFunctionFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int depthFunctionToGL(
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

    throw new UnreachableCodeException();
  }

  static final @Nonnull FaceSelection faceSelectionFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int faceSelectionToGL(
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

    throw new UnreachableCodeException();
  }

  static final FaceWindingOrder faceWindingOrderFromGL(
    final int f)
  {
    switch (f) {
      case GL.GL_CW:
        return FaceWindingOrder.FRONT_FACE_CLOCKWISE;
      case GL.GL_CCW:
        return FaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
    }

    throw new UnreachableCodeException();
  }

  static final int faceWindingOrderToGL(
    final FaceWindingOrder f)
  {
    switch (f) {
      case FRONT_FACE_CLOCKWISE:
        return GL.GL_CW;
      case FRONT_FACE_COUNTER_CLOCKWISE:
        return GL.GL_CCW;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull Primitives primitiveFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int primitiveToGL(
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

    throw new UnreachableCodeException();
  }

  static final @Nonnull GLScalarType scalarTypeFromGL(
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
    }

    throw new UnreachableCodeException();
  }

  static final int scalarTypeToGL(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
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

  private static final void shaderReadSource(
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

  private static int stencilFunctionToGL(
    final @Nonnull StencilFunction function)
  {
    switch (function) {
      case STENCIL_ALWAYS:
        return GL.GL_ALWAYS;
      case STENCIL_EQUAL:
        return GL.GL_EQUAL;
      case STENCIL_GREATER_THAN:
        return GL.GL_GREATER;
      case STENCIL_GREATER_THAN_OR_EQUAL:
        return GL.GL_GEQUAL;
      case STENCIL_LESS_THAN:
        return GL.GL_LESS;
      case STENCIL_LESS_THAN_OR_EQUAL:
        return GL.GL_LEQUAL;
      case STENCIL_NEVER:
        return GL.GL_NEVER;
      case STENCIL_NOT_EQUAL:
        return GL.GL_NOTEQUAL;
    }

    throw new UnreachableCodeException();
  }

  private static int stencilOperationToGL(
    final @Nonnull StencilOperation op)
  {
    switch (op) {
      case STENCIL_OP_DECREMENT:
        return GL.GL_DECR;
      case STENCIL_OP_DECREMENT_WRAP:
        return GL.GL_DECR_WRAP;
      case STENCIL_OP_INCREMENT:
        return GL.GL_INCR;
      case STENCIL_OP_INCREMENT_WRAP:
        return GL.GL_INCR_WRAP;
      case STENCIL_OP_INVERT:
        return GL.GL_INVERT;
      case STENCIL_OP_KEEP:
        return GL.GL_KEEP;
      case STENCIL_OP_REPLACE:
        return GL.GL_REPLACE;
      case STENCIL_OP_ZERO:
        return GL.GL_ZERO;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull TextureFilter textureFilterFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL.GL_LINEAR:
        return TextureFilter.TEXTURE_FILTER_LINEAR;
      case GL.GL_NEAREST:
        return TextureFilter.TEXTURE_FILTER_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  static final int textureFilterToGL(
    final @Nonnull TextureFilter mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL.GL_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  private static int textureTypeToFormatGL(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_ALPHA_8_1BPP:
        return GL.GL_ALPHA;
      case TEXTURE_TYPE_LUMINANCE_8_1BPP:
        return GL.GL_LUMINANCE;
      case TEXTURE_TYPE_LUMINANCE_ALPHA_88_2BPP:
        return GL.GL_LUMINANCE_ALPHA;
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return GL.GL_RGBA;
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return GL.GL_RGB;
    }

    throw new UnreachableCodeException();
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
        return GL.GL_UNSIGNED_BYTE;
      case TEXTURE_TYPE_RGBA_4444_2BPP:
        return GL.GL_UNSIGNED_SHORT_4_4_4_4;
      case TEXTURE_TYPE_RGBA_5551_2BPP:
        return GL.GL_UNSIGNED_SHORT_5_5_5_1;
      case TEXTURE_TYPE_RGB_565_2BPP:
        return GL.GL_UNSIGNED_SHORT_5_6_5;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull TextureWrap textureWrapFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL.GL_CLAMP_TO_EDGE:
        return TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL.GL_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT;
      case GL.GL_MIRRORED_REPEAT:
        return TextureWrap.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  static final int textureWrapToGL(
    final @Nonnull TextureWrap wrap)
  {
    switch (wrap) {
      case TEXTURE_WRAP_CLAMP_TO_EDGE:
        return GL.GL_CLAMP_TO_EDGE;
      case TEXTURE_WRAP_REPEAT:
        return GL.GL_REPEAT;
      case TEXTURE_WRAP_REPEAT_MIRRORED:
        return GL.GL_MIRRORED_REPEAT;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull Type typeFromGL(
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
      case GL2ES2.GL_FLOAT_MAT3:
        return Type.TYPE_FLOAT_MATRIX_3;
      case GL2ES2.GL_FLOAT_MAT4:
        return Type.TYPE_FLOAT_MATRIX_4;
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
      case GL2ES2.GL_SAMPLER_2D:
        return Type.TYPE_SAMPLER_2D;
      case GL2ES2.GL_SAMPLER_2D_SHADOW:
        return Type.TYPE_SAMPLER_2D_SHADOW;
      case GL2ES2.GL_SAMPLER_3D:
        return Type.TYPE_SAMPLER_3D;
      case GL2ES2.GL_SAMPLER_CUBE:
        return Type.TYPE_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
  }

  static final int typeToGL(
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
      case TYPE_SAMPLER_2D_SHADOW:
        return GL2ES2.GL_SAMPLER_2D_SHADOW;
      case TYPE_SAMPLER_3D:
        return GL2ES2.GL_SAMPLER_3D;
      case TYPE_SAMPLER_CUBE:
        return GL2ES2.GL_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull GLUnsignedType unsignedTypeFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int unsignedTypeToGL(
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

    throw new UnreachableCodeException();
  }

  static final UsageHintEmbedded usageHintEmbeddedFromGL(
    final int hint)
  {
    switch (hint) {
      case GL.GL_DYNAMIC_DRAW:
        return UsageHintEmbedded.USAGE_DYNAMIC_DRAW;
      case GL.GL_STATIC_DRAW:
        return UsageHintEmbedded.USAGE_STATIC_DRAW;
      case GL2ES2.GL_STREAM_DRAW:
        return UsageHintEmbedded.USAGE_STREAM_DRAW;
    }

    throw new UnreachableCodeException();
  }

  static final int usageHintEmbeddedToGL(
    final UsageHintEmbedded hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_DRAW:
        return GL.GL_DYNAMIC_DRAW;
      case USAGE_STATIC_DRAW:
        return GL.GL_STATIC_DRAW;
      case USAGE_STREAM_DRAW:
        return GL2ES2.GL_STREAM_DRAW;
    }

    throw new UnreachableCodeException();
  }

  private final @Nonnull GLContext       context;
  protected final @Nonnull Log           log;
  private final @Nonnull TextureUnit[]   texture_units;
  private boolean                        line_smoothing;
  private final int                      line_aliased_min_width;
  private final int                      line_aliased_max_width;
  private final int                      line_smooth_min_width;
  private final int                      line_smooth_max_width;
  private final int                      point_min_width;
  private final int                      point_max_width;
  protected final @Nonnull ByteBuffer    integer_cache_buffer;
  protected final @Nonnull IntBuffer     integer_cache;
  protected final @Nonnull ByteBuffer    color_buffer_mask_cache;
  protected final @Nonnull StringBuilder log_text;

  public GLInterfaceEmbedded_JOGL_ES2_Actual(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");
    this.log_text = new StringBuilder();

    this.context = Constraints.constrainNotNull(context, "GL context");

    final GL g = this.contextGetGL2ES2();

    this.color_buffer_mask_cache =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());

    this.integer_cache_buffer =
      ByteBuffer.allocateDirect(
        GLInterfaceEmbedded_JOGL_ES2_Actual.INTEGER_CACHE_SIZE).order(
        ByteOrder.nativeOrder());
    this.integer_cache = this.integer_cache_buffer.asIntBuffer();

    this.texture_units = this.textureGetUnitsCache();
    this.line_smoothing = false;

    this.integerCacheReset();
    g.glGetIntegerv(GL.GL_ALIASED_LINE_WIDTH_RANGE, this.integer_cache);
    this.line_aliased_min_width = this.integer_cache.get();
    this.line_aliased_max_width = this.integer_cache.get();
    GLError.check(this);

    this.integerCacheReset();
    g.glGetIntegerv(GL.GL_SMOOTH_LINE_WIDTH_RANGE, this.integer_cache);
    this.line_smooth_min_width = this.integer_cache.get();
    this.line_smooth_max_width = this.integer_cache.get();
    GLError.check(this);

    this.integerCacheReset();
    g.glGetIntegerv(GL.GL_ALIASED_POINT_SIZE_RANGE, this.integer_cache);
    this.point_min_width = this.integer_cache.get();
    this.point_max_width = this.integer_cache.get();
    GLError.check(this);
  }

  @Override public final ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");

    final long size = descriptor.getSize();
    final long bytes_total = elements * size;

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("array-buffer: allocate (");
      this.log_text.append(elements);
      this.log_text.append(" elements, ");
      this.log_text.append(size);
      this.log_text.append(" bytes per element, ");
      this.log_text.append(bytes_total);
      this.log_text.append(" bytes)");
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    gl.glGenBuffers(1, this.integer_cache);
    GLError.check(this);

    final int id = this.integer_cache.get(0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      bytes_total,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("array-buffer: allocated ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    return new ArrayBuffer(id, elements, descriptor);
  }

  @Override public final void arrayBufferBind(
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer.getGLName());
    GLError.check(this);
  }

  @Override public final void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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
      GLInterfaceEmbedded_JOGL_ES2_Actual.scalarTypeToGL(buffer_attribute
        .getType());
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

  @Override public final void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("array-buffer: delete ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    this.integer_cache.put(0, id.getGLName());

    gl.glDeleteBuffers(1, this.integer_cache);
    id.setDeleted();
    GLError.check(this);
  }

  @Override public final boolean arrayBufferIsBound(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = g.glGetBoundBuffer(GL.GL_ARRAY_BUFFER);
    GLError.check(this);
    return b == id.getGLName();
  }

  @Override public final void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public final void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

    gl.glDisableVertexAttribArray(program_attribute.getLocation());
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

    final GL2ES2 g = this.contextGetGL2ES2();

    g.glBufferSubData(
      GL.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    GLError.check(this);
  }

  @Override public final void blendingDisable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glDisable(GL.GL_BLEND);
    GLError.check(this);
  }

  @Override public final void blendingEnable(
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

  @Override public final void blendingEnableSeparate(
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

  @Override public final
    void
    blendingEnableSeparateWithEquationSeparateEmbedded(
      final @Nonnull BlendFunction source_rgb_factor,
      final @Nonnull BlendFunction source_alpha_factor,
      final @Nonnull BlendFunction destination_rgb_factor,
      final @Nonnull BlendFunction destination_alpha_factor,
      final @Nonnull BlendEquationEmbedded equation_rgb,
      final @Nonnull BlendEquationEmbedded equation_alpha)
      throws ConstraintError,
        GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendEquationEmbeddedToGL(equation_rgb),
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendEquationEmbeddedToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendFunctionToGL(source_rgb_factor),
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendFunctionToGL(destination_rgb_factor),
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendFunctionToGL(source_alpha_factor),
      GLInterfaceEmbedded_JOGL_ES2_Actual
        .blendFunctionToGL(destination_alpha_factor));
    GLError.check(this);
  }

  @Override public final void blendingEnableWithEquationEmbedded(
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

  @Override public final void blendingEnableWithEquationSeparateEmbedded(
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

  @Override public final boolean blendingIsEnabled()
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    this.integerCacheReset();
    g.glGetIntegerv(GL.GL_BLEND, this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0) == GL.GL_TRUE;
  }

  @Override public final void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glClearColor(r, g, b, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public final void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glClearColor(r, g, b, a);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public final void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    this.colorBufferClear3f(color.getXF(), color.getYF(), color.getZF());
  }

  @Override public final void colorBufferClearV4f(
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

  @Override public final void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glColorMask(r, g, b, a);
    GLError.check(this);
  }

  private final ByteBuffer colorBufferMaskStatus()
    throws GLException
  {
    this.color_buffer_mask_cache.rewind();
    final GL2ES2 g = this.contextGetGL2ES2();
    g.glGetBooleanv(GL.GL_COLOR_WRITEMASK, this.color_buffer_mask_cache);
    GLError.check(this);
    return this.color_buffer_mask_cache;
  }

  @Override public final boolean colorBufferMaskStatusAlpha()
    throws GLException
  {
    final int a = this.colorBufferMaskStatus().get(3);
    return a != 0;
  }

  @Override public final boolean colorBufferMaskStatusBlue()
    throws GLException
  {
    final int b = this.colorBufferMaskStatus().get(2);
    return b != 0;
  }

  @Override public final boolean colorBufferMaskStatusGreen()
    throws GLException
  {
    final int g = this.colorBufferMaskStatus().get(1);
    return g != 0;
  }

  @Override public final boolean colorBufferMaskStatusRed()
    throws GLException
  {
    final int r = this.colorBufferMaskStatus().get(0);
    return r != 0;
  }

  protected GL2ES2 contextGetGL2ES2()
  {
    return this.context.getGL().getGL2ES2();
  }

  protected GL2GL3 contextGetGL2GL3()
  {
    return this.context.getGL().getGL2GL3();
  }

  protected final int contextGetInteger(
    final GL2ES2 g,
    final int name)
    throws GLException
  {
    this.integerCacheReset();
    g.glGetIntegerv(name, this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0);
  }

  protected final int contextGetProgramInteger(
    final GL2ES2 g,
    final int program,
    final int name)
    throws GLException
  {
    this.integerCacheReset();
    g.glGetProgramiv(program, name, this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0);
  }

  protected final int contextGetShaderInteger(
    final GL2ES2 g,
    final int program,
    final int name)
    throws GLException
  {
    this.integerCacheReset();
    g.glGetShaderiv(program, name, this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0);
  }

  @Override public final void cullingDisable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glDisable(GL.GL_CULL_FACE);
    GLError.check(this);
  }

  @Override public final void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi =
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceSelectionToGL(faces);
    final int oi =
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceWindingOrderToGL(order);

    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(fi);
    gl.glFrontFace(oi);
    GLError.check(this);
  }

  @Override public final boolean cullingIsEnabled()
    throws GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();
    final boolean e = g.glIsEnabled(GL.GL_CULL_FACE);
    GLError.check(this);
    return e;
  }

  @Override public final void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    GLError.check(this);
  }

  @Override public final void depthBufferDisable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glDisable(GL.GL_DEPTH_TEST);
    GLError.check(this);
  }

  @Override public final void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d =
      GLInterfaceEmbedded_JOGL_ES2_Actual.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    GLError.check(this);
  }

  @Override public final int depthBufferGetBits()
    throws GLException
  {
    /**
     * Note that because this package intends to be compatible with ES2, but
     * might be running on an ordinary GL 2.1 or GL 3.0 implementation, it's
     * necessary to check explicitly what the real underlying implementation
     * is, because ES2 requires a different function call to retrieve the
     * current depth buffer bits.
     */

    if (this.implementationIsES2()) {
      return this.depthBufferGetBitsES2();
    }

    return this.depthBufferGetBitsGL2GL3();
  }

  private final int depthBufferGetBitsES2()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    final int bits = this.contextGetInteger(gl, GL.GL_DEPTH_BITS);
    GLError.check(this);
    return bits;
  }

  protected final int depthBufferGetBitsGL2GL3()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    final int framebuffer =
      this.contextGetInteger(gl, GL.GL_FRAMEBUFFER_BINDING);
    GLError.check(this);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits = this.contextGetInteger(gl, GL.GL_DEPTH_BITS);
      GLError.check(this);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    this.integerCacheReset();
    gl.glGetFramebufferAttachmentParameteriv(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
      this.integer_cache);
    GLError.check(this);
    if (this.integer_cache.get(0) == GL.GL_NONE) {
      return 0;
    }

    /**
     * If there's a depth attachment, check the size of it.
     */

    this.integerCacheReset();
    gl.glGetFramebufferAttachmentParameteriv(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL2GL3.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE,
      this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0);
  }

  @Override public final boolean depthBufferIsEnabled()
    throws GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();
    final boolean e = g.glIsEnabled(GL.GL_DEPTH_TEST);
    GLError.check(this);
    return e;
  }

  @Override public final void depthBufferWriteDisable()
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    g.glDepthMask(false);
    GLError.check(this);
  }

  @Override public final void depthBufferWriteEnable()
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainRange(
      this.depthBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    g.glDepthMask(true);
    GLError.check(this);
  }

  @Override public final boolean depthBufferWriteIsEnabled()
    throws GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    final ByteBuffer buffer = Buffers.newDirectByteBuffer(4);
    g.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, buffer);
    GLError.check(this);

    final IntBuffer bi = buffer.asIntBuffer();
    return bi.get(0) == 1;
  }

  @Override public final void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(mode, "Drawing mode");
    Constraints.constrainNotNull(indices, "Index buffer");
    Constraints.constrainArbitrary(
      indices.resourceIsDeleted() == false,
      "Index buffer not deleted");

    final int index_id = indices.getGLName();
    final int index_count = (int) indices.getRange().getInterval();
    final int mode_gl =
      GLInterfaceEmbedded_JOGL_ES2_Actual.primitiveToGL(mode);
    final int type =
      GLInterfaceEmbedded_JOGL_ES2_Actual.unsignedTypeToGL(indices.getType());

    g.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, index_id);
    g.glDrawElements(mode_gl, index_count, type, 0L);
    GLError.check(this);
  }

  @Override public final boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public final void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Fragment shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("fragment-shader: attach ");
      this.log_text.append(program);
      this.log_text.append(" ");
      this.log_text.append(shader);
      this.log.debug(this.log_text.toString());
    }

    g.glAttachShader(program.getGLName(), shader.getGLName());
    GLError.check(this);
  }

  @Override public final FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("fragment-shader: compile \"");
      this.log_text.append(name);
      this.log_text.append("\"");
      this.log.debug(this.log_text.toString());
    }

    final int id = g.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    GLError.check(this);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLInterfaceEmbedded_JOGL_ES2_Actual.shaderReadSource(
      stream,
      lines,
      lengths);
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

  @Override public final void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(id, "Fragment shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("fragment-shader: delete ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    g.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public final Framebuffer framebufferAllocate(
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    final Framebuffer buffer = this.framebufferMake();

    Constraints.constrainNotNull(attachments, "Framebuffer attachments");
    Constraints.constrainRange(
      attachments.length,
      1,
      Long.MAX_VALUE,
      "Framebuffer attachments length");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getGLName());
    GLError.check(this);

    /**
     * Attach all framebuffer storage.
     */

    try {
      boolean have_depth = false;
      boolean have_color = false;
      final Set<Integer> color_indices = new TreeSet<Integer>();

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

            Constraints.constrainArbitrary(
              color_indices.contains(Integer.valueOf(index)) == false,
              "Color buffer not already present at this index");

            final Texture2DStatic texture = color.getTexture();
            Constraints.constrainArbitrary(
              texture.resourceIsDeleted() == false,
              "Texture is not deleted");

            color_indices.add(Integer.valueOf(index));
            have_color = true;

            gl.glFramebufferTexture2D(
              GL.GL_FRAMEBUFFER,
              GL.GL_COLOR_ATTACHMENT0 + index,
              GL.GL_TEXTURE_2D,
              texture.getGLName(),
              0);
            GLError.check(this);

            if (this.log.enabled(Level.LOG_DEBUG)) {
              this.log_text.setLength(0);
              this.log_text.append("framebuffer: attach color ");
              this.log_text.append(buffer);
              this.log_text.append(" ");
              this.log_text.append(color);
              this.log.debug(this.log_text.toString());
            }

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

            gl.glFramebufferRenderbuffer(
              GL.GL_FRAMEBUFFER,
              GL.GL_DEPTH_ATTACHMENT,
              GL.GL_RENDERBUFFER,
              id);
            GLError.check(this);

            gl.glFramebufferRenderbuffer(
              GL.GL_FRAMEBUFFER,
              GL.GL_STENCIL_ATTACHMENT,
              GL.GL_RENDERBUFFER,
              id);
            GLError.check(this);

            if (this.log.enabled(Level.LOG_DEBUG)) {
              this.log_text.setLength(0);
              this.log_text.append("framebuffer: attach depth+stencil ");
              this.log_text.append(buffer);
              this.log_text.append(" ");
              this.log_text.append(depth);
              this.log.debug(this.log_text.toString());
            }

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
      return buffer;
    } finally {
      gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
      GLError.check(this);
    }
  }

  @Override public final void framebufferBind(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getGLName());
    GLError.check(this);
  }

  @Override public final void framebufferDelete(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("framebuffer: delete ");
      this.log_text.append(buffer);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    this.integer_cache.put(0, buffer.getGLName());
    gl.glDeleteFramebuffers(1, this.integer_cache);
    GLError.check(this);
    buffer.setDeleted();
  }

  private final Framebuffer framebufferMake()
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    this.integerCacheReset();
    gl.glGenFramebuffers(1, this.integer_cache);
    GLError.check(this);
    final int id = this.integer_cache.get(0);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("framebuffer: allocated ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    return new Framebuffer(id);
  }

  @Override public final void framebufferUnbind()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    GLError.check(this);
  }

  private final boolean implementationIsES2()
  {
    return this.context.isGL2ES2();
  }

  @Override public final IndexBuffer indexBufferAllocate(
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

  @Override public final @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(type, "Index type");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    final long size = GLUnsignedTypeMeta.getSizeBytes(type);
    final long bytes_total = indices * size;

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("index-buffer: allocate (");
      this.log_text.append(indices);
      this.log_text.append(" elements, ");
      this.log_text.append(size);
      this.log_text.append(" bytes per element, ");
      this.log_text.append(bytes_total);
      this.log_text.append(" bytes)");
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    gl.glGenBuffers(1, this.integer_cache);
    GLError.check(this);

    final int id = this.integer_cache.get(0);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      bytes_total,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("index-buffer: allocated ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  @Override public final void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("index-buffer: delete ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    this.integer_cache.put(0, id.getGLName());
    gl.glDeleteBuffers(1, this.integer_cache);
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

    final GL2ES2 g = this.contextGetGL2ES2();

    g.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, buffer.getGLName());
    g.glBufferSubData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    GLError.check(this);
  }

  private void integerCacheReset()
  {
    this.integer_cache.rewind();
    this.integer_cache_buffer.rewind();
  }

  @Override public final int lineAliasedGetMaximumWidth()
  {
    return this.line_aliased_max_width;
  }

  @Override public final int lineAliasedGetMinimumWidth()
  {
    return this.line_aliased_min_width;
  }

  @Override public final void lineSetWidth(
    final float width)
    throws GLException,
      ConstraintError
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final int lineSmoothGetMaximumWidth()
  {
    return this.line_smooth_max_width;
  }

  @Override public final int lineSmoothGetMinimumWidth()
  {
    return this.line_smooth_min_width;
  }

  @Override public final void lineSmoothingDisable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glDisable(GL.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = false;
  }

  @Override public final void lineSmoothingEnable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glEnable(GL.GL_LINE_SMOOTH);
    GLError.check(this);
    this.line_smoothing = true;
  }

  @Override public final int metaGetError()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    return gl.glGetError();
  }

  @Override public final String metaGetRenderer()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final String x = gl.glGetString(GL.GL_RENDERER);
    GLError.check(this);
    return x;
  }

  @Override public final String metaGetVendor()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final String x = gl.glGetString(GL.GL_VENDOR);
    GLError.check(this);
    return x;
  }

  @Override public final String metaGetVersion()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final String x = gl.glGetString(GL.GL_VERSION);
    GLError.check(this);
    return x;
  }

  @Override public final int pointGetMaximumWidth()
  {
    return this.point_max_width;
  }

  @Override public final int pointGetMinimumWidth()
  {
    return this.point_min_width;
  }

  @Override public final void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    gl.glUseProgram(program.getGLName());
    GLError.check(this);
  }

  @Override public final ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(name, "Program name");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("program: create \"");
      this.log_text.append(name);
      this.log_text.append("\"");
      this.log.debug(this.log_text.toString());
    }

    final int id = gl.glCreateProgram();
    if (id == 0) {
      throw new GLException(0, "glCreateProgram failed");
    }
    GLError.check(this);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("program: created ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    return new ProgramReference(id, name);
  }

  @Override public final void programDeactivate()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glUseProgram(0);
    GLError.check(this);
  }

  @Override public final void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("program: delete ");
      this.log_text.append(program);
      this.log.debug(this.log_text.toString());
    }

    gl.glDeleteProgram(program.getGLName());
    program.setDeleted();
    GLError.check(this);
  }

  @Override public final void programGetAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max =
      this.contextGetProgramInteger(
        gl,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      this.contextGetProgramInteger(
        gl,
        program.getGLName(),
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
      final GLType.Type type =
        GLInterfaceEmbedded_JOGL_ES2_Actual.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetAttribLocation(id, name);
      GLError.check(this);

      if (location == -1) {
        if (this.log.enabled(Level.LOG_DEBUG)) {
          this.log_text.setLength(0);
          this.log_text.append("driver returned active attribute \"");
          this.log_text.append(name);
          this.log_text.append("\" with location -1, ignoring");
          this.log.debug(this.log_text.toString());
        }
        continue;
      }

      assert out.containsKey(name) == false;
      out.put(
        name,
        new ProgramAttribute(program, index, location, name, type));
    }
  }

  @Override public final int programGetMaximumActiveAttributes()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final int max = this.contextGetInteger(gl, GL2ES2.GL_MAX_VERTEX_ATTRIBS);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("implementation supports ");
      this.log_text.append(max);
      this.log_text.append(" active attributes");
      this.log.debug(this.log_text.toString());
    }

    return max;
  }

  @Override public final void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
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
      final GLType.Type type =
        GLInterfaceEmbedded_JOGL_ES2_Actual.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetUniformLocation(id, name);
      GLError.check(this);

      if (location == -1) {
        if (this.log.enabled(Level.LOG_DEBUG)) {
          this.log_text.setLength(0);
          this.log_text.append("driver returned active uniform \"");
          this.log_text.append(name);
          this.log_text.append("\" with location -1, ignoring");
          this.log.debug(this.log_text.toString());
        }
        continue;
      }

      assert (out.containsKey(name) == false);
      out.put(name, new ProgramUniform(program, index, location, name, type));
    }
  }

  @Override public final boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active = this.contextGetInteger(gl, GL2ES2.GL_CURRENT_PROGRAM);
    GLError.check(this);
    return active == program.getGLName();
  }

  @Override public final void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("program: link ");
      this.log_text.append(program);
      this.log.debug(this.log_text.toString());
    }

    gl.glLinkProgram(program.getGLName());
    GLError.check(this);

    final int status =
      this.contextGetProgramInteger(
        gl,
        program.getGLName(),
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl
        .glGetProgramInfoLog(program.getGLName(), 8192, buffer_length, buffer);
      GLError.check(this);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(program.getName(), text);
    }

    GLError.check(this);
  }

  @Override public final void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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
      1,
      false,
      matrix.getFloatBuffer());
    GLError.check(this);
  }

  @Override public final void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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
      1,
      false,
      matrix.getFloatBuffer());
    GLError.check(this);
  }

  @Override public final void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final RenderbufferD24S8 renderbufferD24S8Allocate(
    final int width,
    final int height)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainRange(width, 1, Integer.MAX_VALUE);
    Constraints.constrainRange(height, 1, Integer.MAX_VALUE);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("renderbuffer-ds24s8: allocate ");
      this.log_text.append(width);
      this.log_text.append("x");
      this.log_text.append(height);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    gl.glGenRenderbuffers(1, this.integer_cache);
    GLError.check(this);
    final int id = this.integer_cache.get(0);

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
    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("renderbuffer-ds24s8: allocated ");
      this.log_text.append(r);
      this.log.debug(this.log_text.toString());
    }

    return r;
  }

  @Override public final void renderbufferD24S8Delete(
    final @Nonnull RenderbufferD24S8 buffer)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(buffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("renderbuffer-ds24s8: delete ");
      this.log_text.append(buffer);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    this.integer_cache.put(0, buffer.getGLName());
    gl.glDeleteRenderbuffers(1, this.integer_cache);
    buffer.setDeleted();
    GLError.check(this);
  }

  @Override public final void scissorDisable()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    gl.glDisable(GL.GL_SCISSOR_TEST);
    GLError.check(this);
  }

  @Override public final void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

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

  @Override public final boolean scissorIsEnabled()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final boolean e = gl.glIsEnabled(GL.GL_SCISSOR_TEST);
    GLError.check(this);
    return e;
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      this.stencilBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    final GL2ES2 g = this.contextGetGL2ES2();
    g.glClearStencil(stencil);
    GLError.check(this);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();
    g.glDisable(GL.GL_STENCIL_TEST);
    GLError.check(this);
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      this.stencilBufferGetBits(),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    final GL2ES2 g = this.contextGetGL2ES2();
    g.glEnable(GL.GL_STENCIL_TEST);
    GLError.check(this);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final GL2ES2 g = this.contextGetGL2ES2();
    final int func =
      GLInterfaceEmbedded_JOGL_ES2_Actual.stencilFunctionToGL(function);
    g.glStencilFuncSeparate(
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    GLError.check(this);
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    /**
     * Note that because this package intends to be compatible with ES2, but
     * might be running on an ordinary GL 2.1 or GL 3.0 implementation, it's
     * necessary to check explicitly what the real underlying implementation
     * is, because ES2 requires a different function call to retrieve the
     * current stencil buffer bits.
     */

    if (this.implementationIsES2()) {
      return this.stencilBufferGetBitsES2();
    }

    return this.stencilBufferGetBitsGL2GL3();
  }

  private final int stencilBufferGetBitsES2()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    final int bits = this.contextGetInteger(gl, GL.GL_STENCIL_BITS);
    GLError.check(this);
    return bits;
  }

  protected final int stencilBufferGetBitsGL2GL3()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    final int framebuffer =
      this.contextGetInteger(gl, GL.GL_FRAMEBUFFER_BINDING);
    GLError.check(this);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits = this.contextGetInteger(gl, GL.GL_STENCIL_BITS);
      GLError.check(this);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a stencil
     * attachment.
     */

    this.integerCacheReset();
    gl.glGetFramebufferAttachmentParameteriv(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
      this.integer_cache);
    GLError.check(this);

    final int type = this.integer_cache.get(0);
    if (type == GL.GL_NONE) {
      return 0;
    }

    /**
     * If there's a stencil attachment, check the size of it.
     */

    this.integerCacheReset();
    gl.glGetFramebufferAttachmentParameteriv(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL2GL3.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE,
      this.integer_cache);
    GLError.check(this);
    return this.integer_cache.get(0);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();
    final boolean e = g.glIsEnabled(GL.GL_STENCIL_TEST);
    GLError.check(this);
    return e;
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    final GL2ES2 g = this.contextGetGL2ES2();
    g.glStencilMaskSeparate(
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceSelectionToGL(faces),
      mask);
    GLError.check(this);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(stencil_fail, "Stencil fail operation");
    Constraints.constrainNotNull(depth_fail, "Depth fail operation");
    Constraints.constrainNotNull(pass, "Pass operation");

    final GL2ES2 g = this.contextGetGL2ES2();
    final int sfail =
      GLInterfaceEmbedded_JOGL_ES2_Actual.stencilOperationToGL(stencil_fail);
    final int dfail =
      GLInterfaceEmbedded_JOGL_ES2_Actual.stencilOperationToGL(depth_fail);
    final int dpass =
      GLInterfaceEmbedded_JOGL_ES2_Actual.stencilOperationToGL(pass);
    g.glStencilOpSeparate(
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull TextureFilter min_filter)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(width, 2, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(height, 2, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      final int bytes =
        height * (TextureTypeMeta.bytesPerPixel(type) * width);
      this.log_text.setLength(0);
      this.log_text.append("texture-2D-static: allocate \"");
      this.log_text.append(name);
      this.log_text.append("\" ");
      this.log_text.append(type);
      this.log_text.append(" ");
      this.log_text.append(width);
      this.log_text.append("x");
      this.log_text.append(height);
      this.log_text.append(" ");
      this.log_text.append(bytes);
      this.log_text.append(" bytes");
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    gl.glGenTextures(1, this.integer_cache);
    GLError.check(this);
    final int texture_id = this.integer_cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureWrapToGL(wrap_s));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureWrapToGL(wrap_t));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureFilterToGL(mag_filter));
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureFilterToGL(min_filter));

    gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureTypeToFormatGL(type),
      width,
      height,
      0,
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureTypeToFormatGL(type),
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureTypeToTypeGL(type),
      null);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

    final Texture2DStatic t =
      new Texture2DStatic(name, type, texture_id, width, height);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("texture-2D-static: allocated ");
      this.log_text.append(t);
      this.log.debug(this.log_text.toString());
    }

    return t;
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    GLError.check(this);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("texture-2D-static: delete ");
      this.log_text.append(texture);
      this.log.debug(this.log_text.toString());
    }

    this.integerCacheReset();
    this.integer_cache.put(0, texture.getGLName());
    gl.glDeleteTextures(1, this.integer_cache);
    texture.setDeleted();
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());

    this.integerCacheReset();
    gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, this.integer_cache);
    final int e = this.integer_cache.get(0);
    GLError.check(this);

    return e == texture.getGLName();
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(data, "Texture data");

    final GL2ES2 g = this.contextGetGL2ES2();
    final AreaInclusive area = data.targetArea();
    final Texture2DStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final int format =
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureTypeToFormatGL(type);
    final int gl_type =
      GLInterfaceEmbedded_JOGL_ES2_Actual.textureTypeToTypeGL(type);
    final ByteBuffer buffer = data.targetData();

    g.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    g.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      format,
      gl_type,
      buffer);
    g.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLError.check(this);
  }

  @Override public final int textureGetMaximumSize()
    throws GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();
    return this.contextGetInteger(gl, GL.GL_MAX_TEXTURE_SIZE);
  }

  @Override public final TextureUnit[] textureGetUnits()
    throws GLException
  {
    return this.texture_units;
  }

  private final TextureUnit[] textureGetUnitsCache()
    throws GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    final int max =
      this.contextGetInteger(g, GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("implementation supports ");
      this.log_text.append(max);
      this.log_text.append(" texture units");
      this.log.debug(this.log_text.toString());
    }

    final TextureUnit[] u = new TextureUnit[max];
    for (int index = 0; index < max; ++index) {
      u[index] = new TextureUnit(index);
    }

    return u;
  }

  @Override public void textureUnitUnbind(
    final TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGL2ES2();

    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLError.check(this);
  }

  @Override public final void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Vertex shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("vertex-shader: attach ");
      this.log_text.append(program);
      this.log_text.append(" ");
      this.log_text.append(shader);
      this.log.debug(this.log_text.toString());
    }

    g.glAttachShader(program.getGLName(), shader.getGLName());
    GLError.check(this);
  }

  @Override public final VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("vertex-shader: compile \"");
      this.log_text.append(name);
      this.log_text.append("\"");
      this.log.debug(this.log_text.toString());
    }

    final int id = g.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    GLError.check(this);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLInterfaceEmbedded_JOGL_ES2_Actual.shaderReadSource(
      stream,
      lines,
      lengths);
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

  @Override public final void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

    Constraints.constrainNotNull(id, "Vertex shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.log_text.setLength(0);
      this.log_text.append("vertex-shader: delete ");
      this.log_text.append(id);
      this.log.debug(this.log_text.toString());
    }

    g.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLError.check(this);
  }

  @Override public final void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 g = this.contextGetGL2ES2();

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

    g.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLError.check(this);
  }
}
