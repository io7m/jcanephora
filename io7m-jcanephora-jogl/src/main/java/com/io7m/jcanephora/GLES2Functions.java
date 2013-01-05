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
import javax.media.opengl.GL2ES2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
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

final class GLES2Functions
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
      GLTypeConversions.usageHintES2ToGL(usage));

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GLES2Functions.checkError(gl);
    return new ArrayBuffer(id, elements, descriptor);
  }

  static void arrayBufferBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void arrayBufferBindVertexAttribute(
    final @Nonnull GL2ES2 gl,
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

    final boolean bound = GLES2Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    final ArrayBufferDescriptor d = buffer.getDescriptor();
    final ArrayBufferAttribute dba =
      d.getAttribute(buffer_attribute.getName());

    final boolean same_array = dba == buffer_attribute;
    Constraints.constrainArbitrary(
      same_array,
      "Buffer attribute belongs to the array buffer");

    final boolean same_type =
      dba.getType().shaderTypeConvertible(
        dba.getElements(),
        program_attribute.getType());
    Constraints.constrainArbitrary(
      same_type,
      "Buffer attribute is of the same type as the program attribute");

    final int program_attrib_id = program_attribute.getLocation();
    final int count = buffer_attribute.getElements();
    final int type =
      GLTypeConversions.scalarTypeToGL(buffer_attribute.getType());
    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset = d.getAttributeOffset(buffer_attribute.getName());

    gl.glEnableVertexAttribArray(program_attrib_id);
    GLES2Functions.checkError(gl);
    gl.glVertexAttribPointer(
      program_attrib_id,
      count,
      type,
      normalized,
      stride,
      offset);
    GLES2Functions.checkError(gl);
  }

  static void arrayBufferDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull Log log,
    final @Nonnull GLStateCache state,
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
      state.log_text.append("array-buffer: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, id.getGLName());

    gl.glDeleteBuffers(1, cache);
    id.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static boolean arrayBufferIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = gl.glGetBoundBuffer(GL.GL_ARRAY_BUFFER);
    GLES2Functions.checkError(gl);
    return b == id.getGLName();
  }

  static void arrayBufferUnbind(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static void arrayBufferUnbindVertexAttribute(
    final @Nonnull GL2ES2 gl,
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

    final boolean bound = GLES2Functions.arrayBufferIsBound(gl, buffer);
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
    GLES2Functions.checkError(gl);
  }

  static void arrayBufferUpdate(
    final @Nonnull GL2ES2 gl,
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

    final boolean bound = GLES2Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    gl.glBufferSubData(
      GL.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    GLES2Functions.checkError(gl);
  }

  static void blendingDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_BLEND);
    GLES2Functions.checkError(gl);
  }

  static void blendingEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  static void blendingEnableSeparate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationES2.BLEND_EQUATION_ADD,
      BlendEquationES2.BLEND_EQUATION_ADD);
  }

  static void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationES2 equation_rgb,
    final @Nonnull BlendEquationES2 equation_alpha)
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
      GLTypeConversions.blendEquationES2ToGL(equation_rgb),
      GLTypeConversions.blendEquationES2ToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    GLES2Functions.checkError(gl);
  }

  static void blendingEnableWithEquationES2(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationES2 equation)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparateES2(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationES2 equation_rgb,
    final @Nonnull BlendEquationES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static boolean blendingIsEnabled(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_BLEND, cache);
    GLES2Functions.checkError(gl);
    return cache.get(0) == GL.GL_TRUE;
  }

  static void checkError(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final int code = gl.glGetError();

    if (code != 0) {
      throw new GLException(code, "OpenGL error: code " + code);
    }
  }

  static void colorBufferClear3f(
    final @Nonnull GL2ES2 gl,
    final float r,
    final float g,
    final float b)
    throws GLException
  {
    gl.glClearColor(r, g, b, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLES2Functions.checkError(gl);
  }

  static void colorBufferClear4f(
    final @Nonnull GL2ES2 gl,
    final float r,
    final float g,
    final float b,
    final float a)
    throws GLException
  {
    gl.glClearColor(r, g, b, a);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    GLES2Functions.checkError(gl);
  }

  static void colorBufferClearV3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    GLES2Functions.colorBufferClear3f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF());
  }

  static void colorBufferClearV4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    GLES2Functions.colorBufferClear4f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF(),
      color.getWF());
  }

  static void colorBufferMask(
    final @Nonnull GL2ES2 gl,
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws GLException
  {
    gl.glColorMask(r, g, b, a);
    GLES2Functions.checkError(gl);
  }

  private static final ByteBuffer colorBufferMaskStatus(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getColorMaskCache();
    gl.glGetBooleanv(GL.GL_COLOR_WRITEMASK, cache);
    GLES2Functions.checkError(gl);
    return cache;
  }

  static boolean colorBufferMaskStatusAlpha(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int a = GLES2Functions.colorBufferMaskStatus(gl, state).get(3);
    return a != 0;
  }

  static boolean colorBufferMaskStatusBlue(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int b = GLES2Functions.colorBufferMaskStatus(gl, state).get(2);
    return b != 0;
  }

  static boolean colorBufferMaskStatusGreen(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int g = GLES2Functions.colorBufferMaskStatus(gl, state).get(1);
    return g != 0;
  }

  static boolean colorBufferMaskStatusRed(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int r = GLES2Functions.colorBufferMaskStatus(gl, state).get(0);
    return r != 0;
  }

  static int contextGetInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(name, cache);
    GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static int contextGetProgramInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetProgramiv(program, name, cache);
    GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static int contextGetShaderInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetShaderiv(program, name, cache);
    GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static void cullingDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_CULL_FACE);
    GLES2Functions.checkError(gl);
  }

  static void cullingEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi = GLTypeConversions.faceSelectionToGL(faces);
    final int oi = GLTypeConversions.faceWindingOrderToGL(order);

    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(fi);
    gl.glFrontFace(oi);
    GLES2Functions.checkError(gl);
  }

  static boolean cullingIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_CULL_FACE);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void depthBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final float depth)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_DEPTH_TEST);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d = GLTypeConversions.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    GLES2Functions.checkError(gl);
  }

  static int depthBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_DEPTH_BITS);
    GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean depthBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_DEPTH_TEST);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void depthBufferWriteDisable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(false);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferWriteEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(true);
    GLES2Functions.checkError(gl);
  }

  static boolean depthBufferWriteIsEnabled(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, cache);
    GLES2Functions.checkError(gl);

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static void drawElements(
    final @Nonnull GL2ES2 gl,
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
    final int mode_gl = GLTypeConversions.primitiveToGL(mode);
    final int type = GLTypeConversions.unsignedTypeToGL(indices.getType());

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, index_id);
    gl.glDrawElements(mode_gl, index_count, type, 0L);
    GLES2Functions.checkError(gl);
  }

  static void fragmentShaderAttach(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Fragment shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: attach ");
      state.log_text.append(program);
      state.log_text.append(" ");
      state.log_text.append(shader);
      log.debug(state.log_text.toString());
    }

    gl.glAttachShader(program.getGLName(), shader.getGLName());
    GLES2Functions.checkError(gl);
  }

  static FragmentShader fragmentShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    GLES2Functions.checkError(gl);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLES2Functions.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    GLES2Functions.checkError(gl);
    gl.glCompileShader(id);
    GLES2Functions.checkError(gl);
    final int status =
      GLES2Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    GLES2Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      GLES2Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new FragmentShader(id, name);
  }

  static void fragmentShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Fragment shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static @Nonnull FramebufferReference framebufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGenFramebuffers(1, cache);
    GLES2Functions.checkError(gl);
    final int id = cache.get(0);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new FramebufferReference(id);
  }

  static void framebufferDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: delete ");
      state.log_text.append(buffer);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, buffer.getGLName());
    gl.glDeleteFramebuffers(1, cache);
    GLES2Functions.checkError(gl);
    buffer.setDeleted();
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferReadable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isColorRenderable(),
      "Renderbuffer is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticReadable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticReadable texture,
    final @Nonnull CubeMapFace face)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    Constraints.constrainNotNull(face, "Cube map face");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    final int gface = GLTypeConversions.cubeFaceToGL(face);
    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferReadable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  /**
   * Available as an extension in ES2 (OES_packed_depth_stencil).
   */

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferReadable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    final RenderbufferType type = renderbuffer.getType();
    Constraints.constrainArbitrary(
      type.isDepthRenderable(),
      "Renderbuffer is depth renderable");
    Constraints.constrainArbitrary(
      type.isStencilRenderable(),
      "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth+stencil attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticReadable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isDepthRenderable(),
      "Texture is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferReadable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(renderbuffer
      .getType()
      .isStencilRenderable(), "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at stencil attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_FRAMEBUFFER_BINDING, cache);
    GLES2Functions.checkError(gl);
    return cache.get(0) == framebuffer.getGLName();
  }

  static void framebufferDrawUnbind(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, state, framebuffer),
      "Framebuffer is bound");

    final int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    GLES2Functions.checkError(gl);

    return GLTypeConversions.framebufferStatusFromGL(status);
  }

  static @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetAttachmentPointsActual(
      final @Nonnull GL2ES2 gl,
      final @Nonnull GLStateCache state,
      final @Nonnull Log log)
      throws GLException
  {
    final int max =
      GLES2Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_COLOR_ATTACHMENTS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" framebuffer color attachments");
      log.debug(state.log_text.toString());
    }

    final FramebufferColorAttachmentPoint[] a =
      new FramebufferColorAttachmentPoint[max];
    for (int index = 0; index < max; ++index) {
      a[index] = new FramebufferColorAttachmentPoint(index);
    }

    return a;
  }

  static IndexBuffer indexBufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
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

    return GLES2Functions.indexBufferAllocateType(
      gl,
      state,
      log,
      type,
      indices);
  }

  static @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(type, "Index type");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    final long size = type.getSizeBytes();
    final long bytes_total = indices * size;

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocate (");
      state.log_text.append(indices);
      state.log_text.append(" elements, ");
      state.log_text.append(size);
      state.log_text.append(" bytes per element, ");
      state.log_text.append(bytes_total);
      state.log_text.append(" bytes)");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenBuffers(1, cache);
    GLES2Functions.checkError(gl);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      bytes_total,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLES2Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  static void indexBufferDelete(
    final @Nonnull GL2ES2 gl,
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
      state.log_text.append("index-buffer: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, id.getGLName());
    gl.glDeleteBuffers(1, cache);
    id.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static void indexBufferUpdate(
    final @Nonnull GL2ES2 gl,
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

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, buffer.getGLName());
    gl.glBufferSubData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    GLES2Functions.checkError(gl);
  }

  static void lineSetWidth(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final float width)
    throws GLException,
      ConstraintError
  {
    if (state.line_smoothing) {
      Constraints.constrainRange(
        width,
        state.line_smooth_min_width,
        state.line_smooth_max_width,
        "Smooth line width");
    } else {
      Constraints.constrainRange(
        width,
        state.line_aliased_min_width,
        state.line_aliased_max_width,
        "Aliased line width");
    }

    gl.glLineWidth(width);
    GLES2Functions.checkError(gl);
  }

  static void lineSmoothingDisable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    gl.glDisable(GL.GL_LINE_SMOOTH);
    GLES2Functions.checkError(gl);
    state.line_smoothing = false;
  }

  static void lineSmoothingEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    gl.glEnable(GL.GL_LINE_SMOOTH);
    GLES2Functions.checkError(gl);
    state.line_smoothing = true;
  }

  static int metaGetError(
    final @Nonnull GL2ES2 gl)
  {
    return gl.glGetError();
  }

  static String metaGetRenderer(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_RENDERER);
    GLES2Functions.checkError(gl);
    return x;
  }

  static String metaGetVendor(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VENDOR);
    GLES2Functions.checkError(gl);
    return x;
  }

  static String metaGetVersion(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VERSION);
    GLES2Functions.checkError(gl);
    return x;
  }

  static int metaGetVersionMajor(
    final GL2ES2 gl)
  {
    return gl.getContext().getGLVersionMajor();
  }

  static int metaGetVersionMinor(
    final GL2ES2 gl)
  {
    return gl.getContext().getGLVersionMinor();
  }

  static boolean metaIsES(
    final GL2ES2 gl)
  {
    return gl.isGLES();
  }

  static void programActivate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    gl.glUseProgram(program.getGLName());
    GLES2Functions.checkError(gl);
  }

  static ProgramReference programCreate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Program name");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: create \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateProgram();
    if (id == 0) {
      throw new GLException(0, "glCreateProgram failed");
    }
    GLES2Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: created ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new ProgramReference(id, name);
  }

  static void programDeactivate(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glUseProgram(0);
    GLES2Functions.checkError(gl);
  }

  static void programDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: delete ");
      state.log_text.append(program);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteProgram(program.getGLName());
    program.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static void programGetAttributes(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
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
    final int max =
      GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      GLES2Functions.contextGetProgramInteger(
        gl,
        state,
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
      GLES2Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetAttribLocation(id, name);
      GLES2Functions.checkError(gl);

      if (location == -1) {
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("driver returned active attribute \"");
          state.log_text.append(name);
          state.log_text.append("\" with location -1, ignoring");
          log.debug(state.log_text.toString());
        }
        continue;
      }

      assert out.containsKey(name) == false;
      out.put(
        name,
        new ProgramAttribute(program, index, location, name, type));
    }
  }

  static int programGetMaximumActiveAttributes(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      GLES2Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_VERTEX_ATTRIBS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" active attributes");
      log.debug(state.log_text.toString());
    }

    return max;
  }

  static void programGetUniforms(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
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
    final int max =
      GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORMS);
    final int length =
      GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    GLES2Functions.checkError(gl);

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
      GLES2Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetUniformLocation(id, name);
      GLES2Functions.checkError(gl);

      if (location == -1) {
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("driver returned active uniform \"");
          state.log_text.append(name);
          state.log_text.append("\" with location -1, ignoring");
          log.debug(state.log_text.toString());
        }
        continue;
      }

      assert (out.containsKey(name) == false);
      out.put(name, new ProgramUniform(program, index, location, name, type));
    }
  }

  static boolean programIsActive(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active =
      GLES2Functions.contextGetInteger(gl, state, GL2ES2.GL_CURRENT_PROGRAM);
    GLES2Functions.checkError(gl);
    return active == program.getGLName();
  }

  static void programLink(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: link ");
      state.log_text.append(program);
      log.debug(state.log_text.toString());
    }

    gl.glLinkProgram(program.getGLName());
    GLES2Functions.checkError(gl);

    final int status =
      GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl
        .glGetProgramInfoLog(program.getGLName(), 8192, buffer_length, buffer);
      GLES2Functions.checkError(gl);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(program.getName(), text);
    }

    GLES2Functions.checkError(gl);
  }

  static void programPutUniformFloat(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1f(uniform.getLocation(), value);
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformMatrix3x3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix3fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformMatrix4x4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix4fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformTextureUnit(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1i(uniform.getLocation(), unit.getIndex());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector2f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector2i(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform3f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF());
    GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform4f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF(),
      vector.getWF());
    GLES2Functions.checkError(gl);
  }

  static Renderbuffer renderbufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull RenderbufferType type,
    final int width,
    final int height)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(type, "Renderbuffer type");
    Constraints.constrainRange(width, 1, Integer.MAX_VALUE);
    Constraints.constrainRange(height, 1, Integer.MAX_VALUE);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: allocate ");
      state.log_text.append(width);
      state.log_text.append("x");
      state.log_text.append(height);
      state.log_text.append(" ");
      state.log_text.append(type);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenRenderbuffers(1, cache);
    GLES2Functions.checkError(gl);
    final int id = cache.get(0);

    final int gtype = GLTypeConversions.renderbufferTypeToGL(type);

    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    GLES2Functions.checkError(gl);
    gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, gtype, width, height);
    GLES2Functions.checkError(gl);
    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
    GLES2Functions.checkError(gl);

    final Renderbuffer r = new Renderbuffer(type, id, width, height);
    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: allocated ");
      state.log_text.append(r);
      log.debug(state.log_text.toString());
    }

    return r;
  }

  static void renderbufferDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Renderbuffer buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: delete ");
      state.log_text.append(buffer);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, buffer.getGLName());
    gl.glDeleteRenderbuffers(1, cache);
    buffer.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static void scissorDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_SCISSOR_TEST);
    GLES2Functions.checkError(gl);
  }

  static void scissorEnable(
    final @Nonnull GL2ES2 gl,
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

    gl.glEnable(GL.GL_SCISSOR_TEST);
    gl.glScissor(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLES2Functions.checkError(gl);
  }

  static boolean scissorIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_SCISSOR_TEST);
    GLES2Functions.checkError(gl);
    return e;
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

  static void stencilBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int stencil)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      GLES2Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glClearStencil(stencil);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GLES2Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glEnable(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferFunction(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final int func = GLTypeConversions.stencilFunctionToGL(function);
    gl.glStencilFuncSeparate(
      GLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    GLES2Functions.checkError(gl);
  }

  static int stencilBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_STENCIL_BITS);
    GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean stencilBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void stencilBufferMask(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    gl
      .glStencilMaskSeparate(GLTypeConversions.faceSelectionToGL(faces), mask);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferOperation(
    final @Nonnull GL2ES2 gl,
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

    final int sfail = GLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = GLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = GLTypeConversions.stencilOperationToGL(pass);
    gl.glStencilOpSeparate(
      GLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    GLES2Functions.checkError(gl);
  }

  static @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
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
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(min_filter, "Minification filter");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");

    if (log.enabled(Level.LOG_DEBUG)) {
      final int bytes = height * (type.bytesPerPixel() * width);
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static: allocate \"");
      state.log_text.append(name);
      state.log_text.append("\" ");
      state.log_text.append(type);
      state.log_text.append(" ");
      state.log_text.append(width);
      state.log_text.append("x");
      state.log_text.append(height);
      state.log_text.append(" ");
      state.log_text.append(bytes);
      state.log_text.append(" bytes");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenTextures(1, cache);
    GLES2Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      GLTypeConversions.textureWrapToGL(wrap_s));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      GLTypeConversions.textureWrapToGL(wrap_t));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      GLTypeConversions.textureFilterToGL(min_filter));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      GLTypeConversions.textureFilterToGL(mag_filter));
    GLES2Functions.checkError(gl);

    final int internal =
      GLTypeConversions.textureTypeToInternalFormatGL(type);
    final int format = GLTypeConversions.textureTypeToFormatGL(type);
    final int itype = GLTypeConversions.textureTypeToTypeGL(type);

    gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      internal,
      width,
      height,
      0,
      format,
      itype,
      null);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLES2Functions.checkError(gl);

    final Texture2DStatic t =
      new Texture2DStatic(
        name,
        type,
        texture_id,
        width,
        height,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static: allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void texture2DStaticBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticReadable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void texture2DStaticDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static: delete ");
      state.log_text.append(texture);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, texture.getGLName());
    gl.glDeleteTextures(1, cache);
    GLES2Functions.checkError(gl);

    texture.setDeleted();
  }

  static boolean texture2DStaticIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticReadable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, cache);
    final int e = cache.get(0);
    GLES2Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void texture2DStaticUnbind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLES2Functions.checkError(gl);
  }

  static void texture2DStaticUpdate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final Texture2DStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final int format = GLTypeConversions.textureTypeToFormatGL(type);
    final int gl_type = GLTypeConversions.textureTypeToTypeGL(type);
    final ByteBuffer buffer = data.targetData();

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    gl.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      format,
      gl_type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    GLES2Functions.checkError(gl);
  }

  static @Nonnull TextureCubeStatic textureCubeStaticAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrap wrap_r,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(size, 2, Integer.MAX_VALUE, "Size");
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(wrap_r, "Wrap R mode");
    Constraints.constrainNotNull(min_filter, "Minification filter");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");

    if (log.enabled(Level.LOG_DEBUG)) {
      final int bytes = size * (type.bytesPerPixel() * size) * 6;
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static: allocate \"");
      state.log_text.append(name);
      state.log_text.append("\" ");
      state.log_text.append(type);
      state.log_text.append(" ");
      state.log_text.append(size);
      state.log_text.append("x");
      state.log_text.append(size);
      state.log_text.append(" ");
      state.log_text.append(bytes);
      state.log_text.append(" bytes");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenTextures(1, cache);
    GLES2Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture_id);
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_S,
      GLTypeConversions.textureWrapToGL(wrap_s));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_T,
      GLTypeConversions.textureWrapToGL(wrap_t));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL2ES2.GL_TEXTURE_WRAP_R,
      GLTypeConversions.textureWrapToGL(wrap_r));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MIN_FILTER,
      GLTypeConversions.textureFilterToGL(min_filter));
    GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MAG_FILTER,
      GLTypeConversions.textureFilterToGL(mag_filter));
    GLES2Functions.checkError(gl);

    final int internal =
      GLTypeConversions.textureTypeToInternalFormatGL(type);
    final int format = GLTypeConversions.textureTypeToFormatGL(type);
    final int itype = GLTypeConversions.textureTypeToTypeGL(type);

    for (final CubeMapFace face : CubeMapFace.values()) {
      final int gface = GLTypeConversions.cubeFaceToGL(face);

      gl.glTexImage2D(gface, 0, internal, size, size, 0, format, itype, null);
      GLES2Functions.checkError(gl);
    }

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    GLES2Functions.checkError(gl);

    final TextureCubeStatic t =
      new TextureCubeStatic(
        name,
        type,
        texture_id,
        size,
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static: allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void textureCubeStaticBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticReadable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void textureCubeStaticDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static: delete ");
      state.log_text.append(texture);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, texture.getGLName());
    gl.glDeleteTextures(1, cache);
    GLES2Functions.checkError(gl);

    texture.setDeleted();
  }

  static boolean textureCubeStaticIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticReadable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_CUBE_MAP, cache);
    final int e = cache.get(0);
    GLES2Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void textureCubeStaticUnbind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    GLES2Functions.checkError(gl);
  }

  static void textureCubeStaticUpdate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull CubeMapFace face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(face, "Cube map face");
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final TextureCubeStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final int format = GLTypeConversions.textureTypeToFormatGL(type);
    final int gl_type = GLTypeConversions.textureTypeToTypeGL(type);
    final ByteBuffer buffer = data.targetData();
    final int gface = GLTypeConversions.cubeFaceToGL(face);

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    gl.glTexSubImage2D(
      gface,
      0,
      x_offset,
      y_offset,
      width,
      height,
      format,
      gl_type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    GLES2Functions.checkError(gl);
  }

  static int textureGetMaximumSize(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    return GLES2Functions
      .contextGetInteger(gl, state, GL.GL_MAX_TEXTURE_SIZE);
  }

  static TextureUnit[] textureGetUnitsActual(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      GLES2Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" texture units");
      log.debug(state.log_text.toString());
    }

    final TextureUnit[] u = new TextureUnit[max];
    for (int index = 0; index < max; ++index) {
      u[index] = new TextureUnit(index);
    }

    return u;
  }

  static void vertexShaderAttach(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
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

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: attach ");
      state.log_text.append(program);
      state.log_text.append(" ");
      state.log_text.append(shader);
      log.debug(state.log_text.toString());
    }

    gl.glAttachShader(program.getGLName(), shader.getGLName());
    GLES2Functions.checkError(gl);
  }

  static VertexShader vertexShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    GLES2Functions.checkError(gl);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    GLES2Functions.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    GLES2Functions.checkError(gl);
    gl.glCompileShader(id);
    GLES2Functions.checkError(gl);
    final int status =
      GLES2Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    GLES2Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      GLES2Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new VertexShader(id, name);
  }

  static void vertexShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Vertex shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteShader(id.getGLName());
    id.setDeleted();
    GLES2Functions.checkError(gl);
  }

  static void viewportSet(
    final @Nonnull GL2ES2 gl,
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

    gl.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    GLES2Functions.checkError(gl);
  }
}
