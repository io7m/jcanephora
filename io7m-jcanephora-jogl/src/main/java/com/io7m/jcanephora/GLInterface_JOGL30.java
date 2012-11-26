package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only non-deprecated features of
 * OpenGL 3.0, using JOGL as the backend.
 * 
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceJOGL30</code> interface has the same
 * thread safe/unsafe behaviour.
 * 
 * The <code>GLInterfaceJOGL30</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * 
 * As OpenGL 3.0 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

@NotThreadSafe public final class GLInterface_JOGL30 extends
  GLInterfaceEmbedded_JOGL_ES2_Actual implements GLInterface
{
  static final @Nonnull BlendEquation blendEquationFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int blendEquationToGL(
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

    throw new UnreachableCodeException();
  }

  static final LogicOperation logicOpFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int logicOpToGL(
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

    throw new UnreachableCodeException();
  }

  static final PolygonMode polygonModeFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int polygonModeToGL(
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

    throw new UnreachableCodeException();
  }

  static final UsageHint usageHintFromGL(
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

    throw new UnreachableCodeException();
  }

  static final int usageHintToGL(
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

    throw new UnreachableCodeException();
  }

  public GLInterface_JOGL30(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(context, log);
  }

  @Override public ByteBuffer arrayBufferMapRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
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
    final GL2GL3 gl = this.contextGetGL2GL3();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
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

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: unmap " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
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
      GLInterface_JOGL30.blendEquationToGL(equation_rgb),
      GLInterface_JOGL30.blendEquationToGL(equation_alpha));
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

  @Override public void logicOperationsDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    gl.glDisable(GL2.GL_LOGIC_OP);
    GLError.check(this);
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL2.GL_LOGIC_OP);
    gl.glLogicOp(GLInterface_JOGL30.logicOpToGL(operation));
    GLError.check(this);
  }

  @Override public boolean logicOperationsEnabled()
    throws GLException
  {
    final GL2GL3 g = this.contextGetGL2GL3();
    final boolean e = g.glIsEnabled(GL2.GL_LOGIC_OP);
    GLError.check(this);
    return e;
  }

  @Override public final void pointProgramSizeControlDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    gl.glDisable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public final void pointProgramSizeControlEnable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
  }

  @Override public final boolean pointProgramSizeControlIsEnabled()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    final boolean e = gl.glIsEnabled(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLError.check(this);
    return e;
  }

  @Override public @Nonnull PolygonMode polygonGetModeBack()
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextGetGL2GL3();

    this.integer_cache.rewind();
    g.glGetIntegerv(GL2.GL_POLYGON_MODE, this.integer_cache);
    GLError.check(this);
    return GLInterface_JOGL30.polygonModeFromGL(this.integer_cache.get(1));
  }

  @Override public @Nonnull PolygonMode polygonGetModeFront()
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextGetGL2GL3();

    this.integer_cache.rewind();
    g.glGetIntegerv(GL2.GL_POLYGON_MODE, this.integer_cache);
    GLError.check(this);
    return GLInterface_JOGL30.polygonModeFromGL(this.integer_cache.get(0));
  }

  @Override public void polygonSetMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();

    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLInterface_JOGL30.polygonModeToGL(mode);
    final int fm =
      GLInterfaceEmbedded_JOGL_ES2_Actual.faceSelectionToGL(faces);
    gl.glPolygonMode(fm, im);
    GLError.check(this);
  }

  @Override public final void polygonSmoothingDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public final void polygonSmoothingEnable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL2GL3();
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    GLError.check(this);
  }

  @Override public final boolean polygonSmoothingIsEnabled()
    throws GLException
  {
    final GL2GL3 g = this.contextGetGL2GL3();
    final boolean e = g.glIsEnabled(GL2GL3.GL_POLYGON_SMOOTH);
    GLError.check(this);
    return e;
  }
}
