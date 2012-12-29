package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GL3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;

final class GL3Functions
{
  static final @Nonnull ArrayBuffer arrayBufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull Log log,
    final @Nonnull GLStateCache state,
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws GLException,
      ConstraintError
  {
    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");
    Constraints.constrainNotNull(usage, "Usage hint");

    final long size = descriptor.getSize();
    final long bytes_total = elements * size;

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocate (");
      state.log_text.append(elements);
      state.log_text.append(" elements, ");
      state.log_text.append(size);
      state.log_text.append(" bytes per element, ");
      state.log_text.append(bytes_total);
      state.log_text.append(" bytes, usage ");
      state.log_text.append(usage);
      state.log_text.append("))");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenBuffers(1, cache);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      bytes_total,
      null,
      GLTypeConversions.usageHintToGL(usage));

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GLES2Functions.checkError(gl);
    return new ArrayBuffer(id, elements, descriptor);
  }

  static ByteBuffer arrayBufferMapRead(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return b;
  }

  static ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLES2Functions.checkError(gl);

    final ByteBuffer b = gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new ArrayBufferWritableMap(id, b);
  }

  static void arrayBufferUnmap(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull GL3 gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
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

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      GLTypeConversions.blendEquationToGL(equation_rgb),
      GLTypeConversions.blendEquationToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    GLES2Functions.checkError(gl);
  }

  static void blendingEnableWithEquation(
    final @Nonnull GL3 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparate(
    final @Nonnull GL3 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static int depthBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int framebuffer =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_FRAMEBUFFER_BINDING);
    GLES2Functions.checkError(gl);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits =
        GLES2Functions.contextGetInteger(gl, state, GL.GL_DEPTH_BITS);
      GLES2Functions.checkError(gl);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    {
      final IntBuffer cache = state.getIntegerCache();
      gl.glGetFramebufferAttachmentParameteriv(
        GL.GL_FRAMEBUFFER,
        GL.GL_DEPTH_ATTACHMENT,
        GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
        cache);
      GLES2Functions.checkError(gl);
      if (cache.get(0) == GL.GL_NONE) {
        return 0;
      }
    }

    /**
     * If there's a depth attachment, check the size of it.
     */

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetFramebufferAttachmentParameteriv(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL2GL3.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE,
      cache);
    GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLES2Functions.checkError(gl);

    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new IndexBufferWritableMap(id, b);
  }

  static void indexBufferUnmap(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static void logicOperationsDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError(gl);
  }

  static void logicOperationsEnable(
    final @Nonnull GL3 gl,
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL.GL_COLOR_LOGIC_OP);
    gl.glLogicOp(GLTypeConversions.logicOpToGL(operation));
    GLES2Functions.checkError(gl);
  }

  static boolean logicOperationsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void pointProgramSizeControlDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
  }

  static void pointProgramSizeControlEnable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
  }

  static boolean pointProgramSizeControlIsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
    return e;
  }

  static @Nonnull PolygonMode polygonGetMode(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL2.GL_POLYGON_MODE, cache);
    GLES2Functions.checkError(gl);
    return GLTypeConversions.polygonModeFromGL(cache.get(1));
  }

  static void polygonSetMode(
    final @Nonnull GL3 gl,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLTypeConversions.polygonModeToGL(mode);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, im);
    GLES2Functions.checkError(gl);
  }

  static void polygonSmoothingDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
  }

  static void polygonSmoothingEnable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
  }

  static boolean polygonSmoothingIsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
    return e;
  }

  static int stencilBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int framebuffer =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_FRAMEBUFFER_BINDING);
    GLES2Functions.checkError(gl);

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    if (framebuffer == 0) {
      final int bits =
        GLES2Functions.contextGetInteger(gl, state, GL.GL_STENCIL_BITS);
      GLES2Functions.checkError(gl);
      return bits;
    }

    /**
     * If a framebuffer is bound, check to see if there's a stencil
     * attachment.
     */

    {
      final IntBuffer cache = state.getIntegerCache();
      gl.glGetFramebufferAttachmentParameteriv(
        GL.GL_FRAMEBUFFER,
        GL.GL_STENCIL_ATTACHMENT,
        GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
        cache);
      GLES2Functions.checkError(gl);

      final int type = cache.get(0);
      if (type == GL.GL_NONE) {
        return 0;
      }
    }

    /**
     * If there's a stencil attachment, check the size of it.
     */

    {
      final IntBuffer cache = state.getIntegerCache();
      gl.glGetFramebufferAttachmentParameteriv(
        GL.GL_FRAMEBUFFER,
        GL.GL_STENCIL_ATTACHMENT,
        GL2GL3.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE,
        cache);
      GLES2Functions.checkError(gl);
      return cache.get(0);
    }
  }
}
