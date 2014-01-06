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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.io7m.jaux.UnreachableCodeException;

final class LWJGL_GLTypeConversions
{
  static final @Nonnull BlendEquationGLES2 blendEquationES2FromGL(
    final int e)
  {
    switch (e) {
      case GL14.GL_FUNC_ADD:
        return BlendEquationGLES2.BLEND_EQUATION_ADD;
      case GL14.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquationGLES2.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL14.GL_FUNC_SUBTRACT:
        return BlendEquationGLES2.BLEND_EQUATION_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static final int blendEquationES2ToGL(
    final @Nonnull BlendEquationGLES2 e)
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

  static @Nonnull BlendEquationGL3 blendEquationFromGL(
    final int e)
  {
    switch (e) {
      case GL14.GL_FUNC_ADD:
        return BlendEquationGL3.BLEND_EQUATION_ADD;
      case GL14.GL_MAX:
        return BlendEquationGL3.BLEND_EQUATION_MAXIMUM;
      case GL14.GL_MIN:
        return BlendEquationGL3.BLEND_EQUATION_MINIMUM;
      case GL14.GL_FUNC_REVERSE_SUBTRACT:
        return BlendEquationGL3.BLEND_EQUATION_REVERSE_SUBTRACT;
      case GL14.GL_FUNC_SUBTRACT:
        return BlendEquationGL3.BLEND_EQUATION_SUBTRACT;
    }

    throw new UnreachableCodeException();
  }

  static int blendEquationToGL(
    final @Nonnull BlendEquationGL3 e)
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

  static @Nonnull CubeMapFaceLH cubeFaceFromGL(
    final int face)
  {
    switch (face) {
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X:
        return CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X;
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X:
        return CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X;

      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y:
        return CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Y;
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y:
        return CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Y;

      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z:
        return CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Z;
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z:
        return CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z;
    }

    throw new UnreachableCodeException();
  }

  static int cubeFaceToGL(
    final @Nonnull CubeMapFaceLH face)
  {
    switch (face) {
      case CUBE_MAP_LH_NEGATIVE_X:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
      case CUBE_MAP_LH_POSITIVE_X:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

      case CUBE_MAP_LH_NEGATIVE_Y:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
      case CUBE_MAP_LH_POSITIVE_Y:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;

      case CUBE_MAP_LH_NEGATIVE_Z:
        return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
      case CUBE_MAP_LH_POSITIVE_Z:
        return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
    }

    throw new UnreachableCodeException();
  }

  static final DepthFunction depthFunctionFromGL(
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

  static final int depthFunctionToGL(
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

  static final @Nonnull FaceSelection faceSelectionFromGL(
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

  static final int faceSelectionToGL(
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

  static final FaceWindingOrder faceWindingOrderFromGL(
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

  static final int faceWindingOrderToGL(
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

  static @Nonnull FramebufferStatus framebufferStatusFromGL(
    final int status)
  {
    switch (status) {
      case GL30.GL_FRAMEBUFFER_COMPLETE:
        return FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER;
      case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER;
      case GL30.GL_FRAMEBUFFER_UNSUPPORTED:
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED;

    }

    return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_UNKNOWN;
  }

  static final LogicOperation logicOpFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int logicOpToGL(
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

    throw new UnreachableCodeException();
  }

  static final @Nonnull PixelType pixelTypeFromGL(
    final int e)
  {
    switch (e) {
      case GL12.GL_UNSIGNED_SHORT_5_6_5:
        return PixelType.PIXEL_PACKED_UNSIGNED_SHORT_565;
      case GL12.GL_UNSIGNED_SHORT_5_5_5_1:
        return PixelType.PIXEL_PACKED_UNSIGNED_SHORT_5551;
      case GL12.GL_UNSIGNED_SHORT_4_4_4_4:
        return PixelType.PIXEL_PACKED_UNSIGNED_SHORT_4444;
      case GL12.GL_UNSIGNED_INT_10_10_10_2:
        return PixelType.PIXEL_PACKED_UNSIGNED_INT_1010102;
      case GL11.GL_UNSIGNED_SHORT:
        return PixelType.PIXEL_COMPONENT_UNSIGNED_SHORT;
      case GL11.GL_UNSIGNED_INT:
        return PixelType.PIXEL_COMPONENT_UNSIGNED_INT;
      case GL11.GL_UNSIGNED_BYTE:
        return PixelType.PIXEL_COMPONENT_UNSIGNED_BYTE;
      case GL11.GL_SHORT:
        return PixelType.PIXEL_COMPONENT_SHORT;
      case GL11.GL_INT:
        return PixelType.PIXEL_COMPONENT_INT;
      case GL11.GL_FLOAT:
        return PixelType.PIXEL_COMPONENT_FLOAT;
      case GL11.GL_BYTE:
        return PixelType.PIXEL_COMPONENT_BYTE;
      case GL30.GL_HALF_FLOAT:
        return PixelType.PIXEL_COMPONENT_HALF_FLOAT;
    }

    throw new UnreachableCodeException();
  }

  static final int pixelTypeToGL(
    final @Nonnull PixelType p)
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
        return GL12.GL_UNSIGNED_SHORT_4_4_4_4;
      case PIXEL_PACKED_UNSIGNED_SHORT_5551:
        return GL12.GL_UNSIGNED_SHORT_5_5_5_1;
      case PIXEL_PACKED_UNSIGNED_SHORT_565:
        return GL12.GL_UNSIGNED_SHORT_5_6_5;
      case PIXEL_COMPONENT_HALF_FLOAT:
        return GL30.GL_HALF_FLOAT;
      case PIXEL_PACKED_UNSIGNED_INT_24_8:
        return GL30.GL_UNSIGNED_INT_24_8;
    }

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
  }

  static final @Nonnull Primitives primitiveFromGL(
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

  static final int primitiveToGL(
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

  public static int renderbufferTypeToGL(
    final RenderbufferType type)
  {
    switch (type) {
      case RENDERBUFFER_COLOR_RGBA_4444:
        return GL11.GL_RGBA4;
      case RENDERBUFFER_COLOR_RGBA_5551:
        return GL11.GL_RGB5_A1;
      case RENDERBUFFER_COLOR_RGB_565:
        /**
         * Apparently not available in LWJGL yet (GL_RGB565).
         */
        return 0x8D62;
      case RENDERBUFFER_DEPTH_16:
        return GL14.GL_DEPTH_COMPONENT16;
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return GL30.GL_DEPTH24_STENCIL8;
      case RENDERBUFFER_STENCIL_8:
        return GL30.GL_STENCIL_INDEX8;
      case RENDERBUFFER_COLOR_RGBA_8888:
        return GL11.GL_RGBA8;
      case RENDERBUFFER_COLOR_RGB_888:
        return GL11.GL_RGB8;
      case RENDERBUFFER_DEPTH_24:
        return GL14.GL_DEPTH_COMPONENT24;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull JCGLScalarType scalarTypeFromGL(
    final int type)
  {
    switch (type) {
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

  static final int scalarTypeToGL(
    final @Nonnull JCGLScalarType type)
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

  static int stencilFunctionToGL(
    final @Nonnull StencilFunction function)
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

  static int stencilOperationToGL(
    final @Nonnull StencilOperation op)
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

  static final @Nonnull TextureFilterMagnification textureFilterMagFromGL(
    final int mag_filter)
  {
    switch (mag_filter) {
      case GL11.GL_LINEAR:
        return TextureFilterMagnification.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
        return TextureFilterMagnification.TEXTURE_FILTER_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  static final int textureFilterMagToGL(
    final @Nonnull TextureFilterMagnification mag_filter)
  {
    switch (mag_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull TextureFilterMinification textureFilterMinFromGL(
    final int min_filter)
  {
    switch (min_filter) {
      case GL11.GL_LINEAR:
        return TextureFilterMinification.TEXTURE_FILTER_LINEAR;
      case GL11.GL_NEAREST:
        return TextureFilterMinification.TEXTURE_FILTER_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  static final int textureFilterMinToGL(
    final @Nonnull TextureFilterMinification min_filter)
  {
    switch (min_filter) {
      case TEXTURE_FILTER_LINEAR:
        return GL11.GL_LINEAR;
      case TEXTURE_FILTER_NEAREST:
        return GL11.GL_NEAREST;
    }

    throw new UnreachableCodeException();
  }

  static final @Nonnull TextureWrapR textureWrapRFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return TextureWrapR.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return TextureWrapR.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  static final int textureWrapRToGL(
    final @Nonnull TextureWrapR wrap)
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

  static final @Nonnull TextureWrapS textureWrapSFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return TextureWrapS.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return TextureWrapS.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  static final int textureWrapSToGL(
    final @Nonnull TextureWrapS wrap)
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

  static final @Nonnull TextureWrapT textureWrapTFromGL(
    final int wrap)
  {
    switch (wrap) {
      case GL12.GL_CLAMP_TO_EDGE:
        return TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE;
      case GL11.GL_REPEAT:
        return TextureWrapT.TEXTURE_WRAP_REPEAT;
      case GL14.GL_MIRRORED_REPEAT:
        return TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED;
    }

    throw new UnreachableCodeException();
  }

  static final int textureWrapTToGL(
    final @Nonnull TextureWrapT wrap)
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

  static final @Nonnull JCGLType typeFromGL(
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
      case GL20.GL_SAMPLER_2D:
        return JCGLType.TYPE_SAMPLER_2D;
      case GL20.GL_SAMPLER_2D_SHADOW:
        return JCGLType.TYPE_SAMPLER_2D_SHADOW;
      case GL20.GL_SAMPLER_3D:
        return JCGLType.TYPE_SAMPLER_3D;
      case GL20.GL_SAMPLER_CUBE:
        return JCGLType.TYPE_SAMPLER_CUBE;
    }

    throw new UnreachableCodeException();
  }

  static final int typeToGL(
    final @Nonnull JCGLType type)
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

  static final @Nonnull JCGLUnsignedType unsignedTypeFromGL(
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

  static final int unsignedTypeToGL(
    final @Nonnull JCGLUnsignedType type)
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

  static final int usageHintES2ToGL(
    final UsageHint hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_COPY:
      case USAGE_DYNAMIC_READ:
      case USAGE_DYNAMIC_DRAW:
        return GL15.GL_DYNAMIC_DRAW;
      case USAGE_STATIC_COPY:
      case USAGE_STATIC_READ:
      case USAGE_STATIC_DRAW:
        return GL15.GL_STATIC_DRAW;
      case USAGE_STREAM_COPY:
      case USAGE_STREAM_READ:
      case USAGE_STREAM_DRAW:
        return GL15.GL_STREAM_DRAW;
    }

    throw new UnreachableCodeException();
  }

  static UsageHint usageHintFromGL(
    final int hint)
  {
    switch (hint) {
      case GL15.GL_DYNAMIC_COPY:
        return UsageHint.USAGE_DYNAMIC_COPY;
      case GL15.GL_DYNAMIC_DRAW:
        return UsageHint.USAGE_DYNAMIC_DRAW;
      case GL15.GL_DYNAMIC_READ:
        return UsageHint.USAGE_DYNAMIC_READ;
      case GL15.GL_STATIC_COPY:
        return UsageHint.USAGE_STATIC_COPY;
      case GL15.GL_STATIC_DRAW:
        return UsageHint.USAGE_STATIC_DRAW;
      case GL15.GL_STATIC_READ:
        return UsageHint.USAGE_STATIC_READ;
      case GL15.GL_STREAM_COPY:
        return UsageHint.USAGE_STREAM_COPY;
      case GL15.GL_STREAM_DRAW:
        return UsageHint.USAGE_STREAM_DRAW;
      case GL15.GL_STREAM_READ:
        return UsageHint.USAGE_STREAM_READ;
    }

    throw new UnreachableCodeException();
  }

  static final int usageHintToGL(
    final UsageHint hint)
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

}
