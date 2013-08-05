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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.media.opengl.GL2ES2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;
import com.jogamp.common.nio.Buffers;

final class JOGL_GL2ES2_Functions
{
  static int contextGetProgramInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final int program,
    final int name)
    throws JCGLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetProgramiv(program, name, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0);
  }

  static int contextGetShaderInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final int program,
    final int name)
    throws JCGLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetShaderiv(program, name, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0);
  }

  static FragmentShader fragmentShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(lines, "Source lines");
    Constraints.constrainArbitrary(
      ShaderUtilities.isEmpty(lines) == false,
      "Source lines not empty");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    JOGL_GL_Functions.checkError(gl);

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGL_GL_Functions.checkError(gl);
    gl.glCompileShader(id);
    JOGL_GL_Functions.checkError(gl);
    final int status =
      JOGL_GL2ES2_Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    JOGL_GL_Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGL_GL_Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    return new FragmentShader(id, name);
  }

  static void fragmentShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLException
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
    id.resourceSetDeleted();
    JOGL_GL_Functions.checkError(gl);
  }

  static void programAttributeArrayBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBufferAttribute buffer_attribute)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferUsable buffer = buffer_attribute.getArray();
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = JOGL_GL_Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    Constraints.constrainArbitrary(
      buffer_attribute.getArray().equals(buffer),
      "Array attribute belongs to the given array");

    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(
        gl,
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final ArrayBufferAttributeDescriptor buffer_attribute_type =
      buffer_attribute.getDescriptor();

    final JCGLScalarType buffer_attribute_gl_type =
      buffer_attribute_type.getType();
    final int buffer_attribute_elements = buffer_attribute_type.getElements();

    final boolean convertible =
      buffer_attribute_gl_type.shaderTypeConvertible(
        buffer_attribute_elements,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("Array buffer attribute '");
      b.append(buffer_attribute_type.getName());
      b.append("' is of type ");
      b.append(buffer_attribute_gl_type);
      b.append(" with ");
      b.append(buffer_attribute_elements);
      b.append(" elements, but the program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(", which is incompatible");
      Constraints.constrainArbitrary(convertible, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();

    final int type =
      JOGL_GLTypeConversions.scalarTypeToGL(buffer_attribute_gl_type);

    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset =
      buffer
        .getType()
        .getTypeDescriptor()
        .getAttributeOffset(buffer_attribute.getName());

    gl.glEnableVertexAttribArray(program_attrib_id);
    JOGL_GL_Functions.checkError(gl);
    gl.glVertexAttribPointer(
      program_attrib_id,
      buffer_attribute_elements,
      type,
      normalized,
      stride,
      offset);
    JOGL_GL_Functions.checkError(gl);
  }

  static void programAttributeArrayUnbind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBufferAttribute buffer_attribute)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferUsable buffer = buffer_attribute.getArray();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = JOGL_GL_Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    Constraints.constrainArbitrary(
      buffer_attribute.getArray().equals(buffer),
      "Array attribute belongs to the given array");

    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(
        gl,
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    gl.glDisableVertexAttribArray(program_attribute.getLocation());
    JOGL_GL_Functions.checkError(gl);
  }

  static ProgramReference programCreateCommon(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException
  {
    Constraints.constrainNotNull(name, "Program name");
    Constraints.constrainNotNull(v, "Vertex shader");
    Constraints.constrainNotNull(f, "Fragment shader");
    Constraints.constrainArbitrary(
      v.resourceIsDeleted() == false,
      "Vertex shader not deleted");
    Constraints.constrainArbitrary(
      f.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: create \"");
      state.log_text.append(name);
      state.log_text.append("\" with ");
      state.log_text.append(v);
      state.log_text.append(" ");
      state.log_text.append(f);
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateProgram();
    if (id == 0) {
      throw new JCGLException(0, "glCreateProgram failed");
    }
    JOGL_GL_Functions.checkError(gl);

    gl.glAttachShader(id, v.getGLName());
    JOGL_GL_Functions.checkError(gl);
    gl.glAttachShader(id, f.getGLName());
    JOGL_GL_Functions.checkError(gl);
    gl.glLinkProgram(id);
    JOGL_GL_Functions.checkError(gl);

    final int status =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetProgramInfoLog(id, 8192, buffer_length, buffer);
      JOGL_GL_Functions.checkError(gl);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    JOGL_GL_Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: created ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    final Map<String, ProgramUniform> uniforms =
      new HashMap<String, ProgramUniform>();

    final ProgramReference program =
      new ProgramReference(id, name, uniforms, attributes);

    JOGL_GL2ES2_Functions.programGetAttributes(
      gl,
      state,
      log,
      program,
      attributes);

    JOGL_GL2ES2_Functions.programGetUniforms(
      gl,
      state,
      log,
      program,
      uniforms);

    return program;
  }

  static void programDeactivate(
    final @Nonnull GL2ES2 gl)
    throws JCGLException
  {
    gl.glUseProgram(0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void programDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
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
    program.resourceSetDeleted();
    JOGL_GL_Functions.checkError(gl);
  }

  static void programGetAttributes(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
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
      JOGL_GL_Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetAttribLocation(id, name);
      JOGL_GL_Functions.checkError(gl);

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
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log)
    throws JCGLException
  {
    final int max =
      JOGL_GL_Functions.contextGetInteger(
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
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORMS);
    final int length =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    JOGL_GL_Functions.checkError(gl);

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
      JOGL_GL_Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetUniformLocation(id, name);
      JOGL_GL_Functions.checkError(gl);

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
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active =
      JOGL_GL_Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_CURRENT_PROGRAM);
    JOGL_GL_Functions.checkError(gl);
    return active == program.getGLName();
  }

  static void programPutUniformFloat(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT,
      "Uniform type is float");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1f(uniform.getLocation(), value);
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformMatrix3x3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_MATRIX_3,
      "Uniform type is mat3");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix3fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformMatrix4x4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_MATRIX_4,
      "Uniform type is mat4");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix4fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformTextureUnit(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_SAMPLER_2D,
      "Uniform type is sampler_2d");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1i(uniform.getLocation(), unit.getIndex());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector2f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector2i(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_2,
      "Uniform type is ivec2");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_3,
      "Uniform type is vec3");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform3f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector3i(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_3,
      "Uniform type is ivec3");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform3i(
      uniform.getLocation(),
      vector.getXI(),
      vector.getYI(),
      vector.getZI());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_4,
      "Uniform type is vec4");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform4f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF(),
      vector.getWF());
    JOGL_GL_Functions.checkError(gl);
  }

  static void programPutUniformVector4i(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_4,
      "Uniform type is ivec4");
    Constraints.constrainArbitrary(
      JOGL_GL2ES2_Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform4i(
      uniform.getLocation(),
      vector.getXI(),
      vector.getYI(),
      vector.getZI(),
      vector.getWI());
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferFunction(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final int func = JOGL_GLTypeConversions.stencilFunctionToGL(function);
    gl.glStencilFuncSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferMask(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    gl.glStencilMaskSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      mask);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferOperation(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(stencil_fail, "Stencil fail operation");
    Constraints.constrainNotNull(depth_fail, "Depth fail operation");
    Constraints.constrainNotNull(pass, "Pass operation");

    final int sfail =
      JOGL_GLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = JOGL_GLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = JOGL_GLTypeConversions.stencilOperationToGL(pass);
    gl.glStencilOpSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    JOGL_GL_Functions.checkError(gl);
  }

  static VertexShader vertexShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(lines, "Input lines");
    Constraints.constrainArbitrary(
      ShaderUtilities.isEmpty(lines) == false,
      "Source lines not empty");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    JOGL_GL_Functions.checkError(gl);

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGL_GL_Functions.checkError(gl);
    gl.glCompileShader(id);
    JOGL_GL_Functions.checkError(gl);
    final int status =
      JOGL_GL2ES2_Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    JOGL_GL_Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGL_GL_Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    return new VertexShader(id, name);
  }

  static void vertexShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLException
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
    id.resourceSetDeleted();
    JOGL_GL_Functions.checkError(gl);
  }
}
