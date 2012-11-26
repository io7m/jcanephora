package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only non-deprecated features of
 * OpenGL 3.0, using LWJGL as the backend.
 * 
 * As OpenGL 3.0 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

public final class GLInterface_LWJGL30 extends
  GLInterfaceEmbedded_LWJGL_ES2_Actual implements GLInterface
{
  public GLInterface_LWJGL30(
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(log);
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

  static int usageHintToGL(
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

    throw new UnreachableCodeException();
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

    throw new UnreachableCodeException();
  }

  @Override public @Nonnull ByteBuffer arrayBufferMapRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
    return b;
  }

  @Override public @Nonnull ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
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

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: unmap " + id);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
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

    throw new UnreachableCodeException();
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

  @Override public void logicOperationsDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_LOGIC_OP);
    GLError.check(this);
  }

  @Override public void logicOperationsEnable(
    final LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    GL11.glEnable(GL11.GL_LOGIC_OP);
    GL11.glLogicOp(GLInterface_LWJGL30.logicOpToGL(operation));
    GLError.check(this);
  }

  @Override public boolean logicOperationsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_LOGIC_OP);
    GLError.check(this);
    return e;
  }

  @Override public void pointProgramSizeControlDisable()
    throws GLException
  {
    GL11.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public void pointProgramSizeControlEnable()
    throws GLException
  {
    GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public boolean pointProgramSizeControlIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
    return e;
  }

  @Override public @Nonnull PolygonMode polygonGetModeBack()
    throws ConstraintError,
      GLException
  {
    final IntBuffer ib =
      ByteBuffer
        .allocateDirect(16 * 4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    GL11.glGetInteger(GL11.GL_POLYGON_MODE, ib);
    GLError.check(this);
    return GLInterface_LWJGL30.polygonModeFromGL(ib.get(1));
  }

  @Override public @Nonnull PolygonMode polygonGetModeFront()
    throws ConstraintError,
      GLException
  {
    final IntBuffer ib =
      ByteBuffer
        .allocateDirect(16 * 4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer();
    GL11.glGetInteger(GL11.GL_POLYGON_MODE, ib);
    GLError.check(this);
    return GLInterface_LWJGL30.polygonModeFromGL(ib.get(0));
  }

  @Override public void polygonSetMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLInterface_LWJGL30.polygonModeToGL(mode);
    final int fm =
      GLInterfaceEmbedded_LWJGL_ES2_Actual.faceSelectionToGL(faces);
    GL11.glPolygonMode(fm, im);
    GLError.check(this);
  }

  @Override public void polygonSmoothingDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public void polygonSmoothingEnable()
    throws GLException
  {
    GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public boolean polygonSmoothingIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_POLYGON_SMOOTH);
    GLError.check(this);
    return e;
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
      equation,
      equation);
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

  @Override public void blendingEnableSeparateWithEquationSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquation equation_rgb,
    final BlendEquation equation_alpha)
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
      GLInterface_LWJGL30.blendEquationToGL(equation_rgb),
      GLInterface_LWJGL30.blendEquationToGL(equation_alpha));
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
}
