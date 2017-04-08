/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.core.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.core.JCGLExceptionProgramTypeError;
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix3x3D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4I;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered3x3Type;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered3x3s32;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered4x4Type;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered4x4s32;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.mutable.numbers.core.MutableLong;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

final class LWJGL3Shaders implements JCGLShadersType
{
  private static final Logger LOG;
  private static final Pattern NON_EMPTY;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3Shaders.class);
    NON_EMPTY = Pattern.compile("^\\s*$");
  }

  private final LWJGL3Context context;
  private final FloatBuffer m3x3_buffer_view;
  private final MatrixByteBuffered3x3Type m3x3;
  private final FloatBuffer m4x4_buffer_view;
  private final MatrixByteBuffered4x4Type m4x4;
  private @Nullable JCGLProgramShaderUsableType current;
  private boolean check_type;
  private boolean check_active;

  LWJGL3Shaders(final LWJGL3Context c)
  {
    this.context = NullCheck.notNull(c, "Context");
    this.check_active = true;
    this.check_type = true;

    /*
     * Configure baseline defaults.
     */

    GL20.glUseProgram(0);
    LWJGL3ErrorChecking.checkErrors();

    final ByteBuffer m3x3_buffer =
      ByteBuffer.allocateDirect(3 * 3 * 4).order(ByteOrder.nativeOrder());
    this.m3x3_buffer_view =
      m3x3_buffer.asFloatBuffer();
    this.m3x3 =
      MatrixByteBuffered3x3s32.createWithBase(
        m3x3_buffer, MutableLong.create(), 0);

    final ByteBuffer m4x4_buffer =
      ByteBuffer.allocateDirect(4 * 4 * 4).order(ByteOrder.nativeOrder());
    this.m4x4_buffer_view =
      m4x4_buffer.asFloatBuffer();
    this.m4x4 =
      MatrixByteBuffered4x4s32.createWithBase(
        m4x4_buffer, MutableLong.create(), 0);
  }

  private static boolean isEmpty(final List<String> lines)
  {
    NullCheck.notNull(lines, "Lines");

    for (final String line : lines) {
      NullCheck.notNull(line, "Line");
      if (!NON_EMPTY.matcher(line).matches()) {
        return false;
      }
    }

    return true;
  }

  private static JCGLExceptionProgramCompileError getCompilationError(
    final String name,
    final int id)
  {
    return new JCGLExceptionProgramCompileError(
      name, GL20.glGetShaderInfoLog(id));
  }

  private static JCGLExceptionProgramCompileError getLinkError(
    final String name,
    final int id)
  {
    return new JCGLExceptionProgramCompileError(
      name, GL20.glGetProgramInfoLog(id));
  }

  private static void measureLines(
    final List<String> lines,
    final int size,
    final String[] line_array,
    final IntBuffer line_lengths)
  {
    for (int index = 0; index < size; ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }
  }

  private static JCGLExceptionProgramTypeError errorWrongType(
    final JCGLProgramUniformType u,
    final JCGLType t)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Uniform type error.");
    sb.append(System.lineSeparator());
    sb.append("Expected: ");
    sb.append(u.getType());
    sb.append(System.lineSeparator());
    sb.append("Actual: ");
    sb.append(t);
    return new JCGLExceptionProgramTypeError(sb.toString());
  }

  private static void compileSources(
    final String name,
    final List<String> lines,
    final int size,
    final int id)
  {
    final String[] line_array = new String[size];
    lines.toArray(line_array);

    GL20.glShaderSource(id, (CharSequence[]) line_array);
    GL20.glCompileShader(id);

    final int status = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
    if (status == 0) {
      throw getCompilationError(name, id);
    }
  }

  @Override
  public void shaderDeleteProgram(final JCGLProgramShaderType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p, "Shader");

    LWJGL3ProgramShader.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete program shader {}", p.getName());
    }

    GL20.glDeleteProgram(p.getGLName());
    ((LWJGL3ObjectDeletable) p).setDeleted();

    if (Objects.equals(p, this.current)) {
      this.current = null;
    }
  }

  @Override
  public void shaderDeleteVertex(final JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(v, "Shader");

    LWJGL3VertexShader.checkVertexShader(this.context, v);
    JCGLResources.checkNotDeleted(v);

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete vertex shader {}", v.getName());
    }

    GL20.glDeleteShader(v.getGLName());
    ((LWJGL3ObjectDeletable) v).setDeleted();
  }

  @Override
  public void shaderDeleteFragment(final JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(f, "Shader");

    LWJGL3FragmentShader.checkFragmentShader(this.context, f);
    JCGLResources.checkNotDeleted(f);

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete fragment shader {}", f.getName());
    }

    GL20.glDeleteShader(f.getGLName());
    ((LWJGL3ObjectDeletable) f).setDeleted();
  }

  @Override
  public void shaderDeleteGeometry(final JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(g, "Shader");

    LWJGL3GeometryShader.checkGeometryShader(this.context, g);
    JCGLResources.checkNotDeleted(g);

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete geometry shader {}", g.getName());
    }

    GL20.glDeleteShader(g.getGLName());
    ((LWJGL3ObjectDeletable) g).setDeleted();
  }

  @Override
  public JCGLVertexShaderType shaderCompileVertex(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();

    if (LOG.isDebugEnabled()) {
      LOG.debug(
        "compile vertex shader {} ({} lines)", name, Integer.valueOf(size));
    }

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
    Preconditions.checkPreconditionI(
      id, id > 0, ignored -> "Generated shader ID must be positive");

    try {
      LWJGL3Shaders.compileSources(name, lines, size, id);
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "compiled vertex shader {} ⇒ {}", name, Integer.valueOf(id));
      }
    } catch (final JCGLException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "delete vertex shader {} ⇒ {}", name, Integer.valueOf(id));
      }
      GL20.glDeleteShader(id);
      throw e;
    }

    return new LWJGL3VertexShader(
      NullCheck.notNull(this.context, "Context"), id, name);
  }

  @Override
  public JCGLFragmentShaderType shaderCompileFragment(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();

    if (LOG.isDebugEnabled()) {
      LOG.debug(
        "compile fragment shader {} ({} lines)", name, Integer.valueOf(size));
    }

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
    Preconditions.checkPreconditionI(
      id, id > 0, ignored -> "Generated shader ID must be positive");

    try {
      LWJGL3Shaders.compileSources(name, lines, size, id);
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "compiled fragment shader {} ⇒ {}", name, Integer.valueOf(id));
      }
    } catch (final JCGLException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "delete fragment shader {} ⇒ {}", name, Integer.valueOf(id));
      }
      GL20.glDeleteShader(id);
      throw e;
    }

    return new LWJGL3FragmentShader(
      NullCheck.notNull(this.context, "Context"), id, name);
  }

  @Override
  public JCGLGeometryShaderType shaderCompileGeometry(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    if (LOG.isDebugEnabled()) {
      LOG.debug(
        "compile geometry shader {} ({} lines)", name, Integer.valueOf(size));
    }

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
    Preconditions.checkPreconditionI(
      id, id > 0, ignored -> "Generated shader ID must be positive");

    try {
      LWJGL3Shaders.compileSources(name, lines, size, id);
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "compiled geometry shader {} ⇒ {}", name, Integer.valueOf(id));
      }
    } catch (final JCGLException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(
          "delete geometry shader {} ⇒ {}", name, Integer.valueOf(id));
      }
      GL20.glDeleteShader(id);
      throw e;
    }

    return new LWJGL3GeometryShader(
      NullCheck.notNull(this.context, "Context"), id, name);
  }

  @Override
  public JCGLProgramShaderType shaderLinkProgram(
    final String name,
    final JCGLVertexShaderUsableType jv,
    final Optional<JCGLGeometryShaderUsableType> jg,
    final JCGLFragmentShaderUsableType jf)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(jv, "Vertex shader");
    NullCheck.notNull(jg, "Geometry shader");
    NullCheck.notNull(jf, "Fragment shader");

    final LWJGL3VertexShader v =
      LWJGL3VertexShader.checkVertexShader(this.context, jv);
    JCGLResources.checkNotDeleted(v);
    final LWJGL3FragmentShader f =
      LWJGL3FragmentShader.checkFragmentShader(this.context, jf);
    JCGLResources.checkNotDeleted(f);

    final Optional<LWJGL3GeometryShader> g = jg.map(
      gg -> {
        final LWJGL3GeometryShader rg =
          LWJGL3GeometryShader.checkGeometryShader(this.context, gg);
        JCGLResources.checkNotDeleted(rg);
        return rg;
      });

    if (LOG.isDebugEnabled()) {
      LOG.debug("link program {}", name);
      LOG.debug("[{}] vertex {}", name, v.getName());
      jg.ifPresent(
        gg -> LOG.debug("[{}] geometry {}", name, gg.getName()));
      LOG.debug("[{}] fragment {}", name, f.getName());
    }

    final int pid = GL20.glCreateProgram();
    Preconditions.checkPreconditionI(
      pid, pid > 0, ignored -> "Generated program ID must be positive");

    GL20.glAttachShader(pid, v.getGLName());
    GL20.glAttachShader(pid, f.getGLName());
    jg.ifPresent(gg -> GL20.glAttachShader(pid, gg.getGLName()));
    GL20.glLinkProgram(pid);

    final int status = GL20.glGetProgrami(pid, GL20.GL_LINK_STATUS);
    if (status == 0) {
      throw getLinkError(name, pid);
    }

    final Map<String, JCGLProgramAttributeType> attributes = new HashMap<>(16);
    final Map<String, JCGLProgramUniformType> uniforms = new HashMap<>(32);

    final LWJGL3ProgramShader program = new LWJGL3ProgramShader(
      this.context, pid, name, v, g, f);

    this.getAttributes(program, attributes);
    this.getUniforms(program, uniforms);

    program.setAttributes(attributes);
    program.setUniforms(uniforms);
    return program;
  }

  @Override
  public void shaderActivateProgram(
    final JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p, "Shader");

    if (LOG.isTraceEnabled()) {
      LOG.trace("activate {}", p.getName());
    }

    LWJGL3ProgramShader.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);

    GL20.glUseProgram(p.getGLName());
    this.current = p;
  }

  @Override
  public void shaderDeactivateProgram()
    throws JCGLException
  {
    LOG.trace("deactivate");

    GL20.glUseProgram(0);
    this.current = null;
  }

  @Override
  public Optional<JCGLProgramShaderUsableType> shaderActivatedProgram()
    throws JCGLException
  {
    return Optional.ofNullable(this.current);
  }

  private void getAttributes(
    final JCGLProgramShaderUsableType program,
    final Map<String, JCGLProgramAttributeType> out)
    throws JCGLException
  {
    final int id = program.getGLName();
    final int max =
      GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES);
    final int length =
      GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

    final ByteBuffer buffer_name = BufferUtils.createByteBuffer(length);
    final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_size = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_type = BufferUtils.createIntBuffer(1);

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
        id, index, buffer_length, buffer_size, buffer_type, buffer_name);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = LWJGL3TypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = GL20.glGetAttribLocation(id, name);
      if (location == -1) {
        if (LOG.isTraceEnabled()) {
          LOG.trace(
            "ignoring active attribute '{}' with location -1", name);
        }
        continue;
      }

      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "[{}] attribute {} {} {} {}",
          program.getName(),
          Integer.valueOf(index),
          name,
          Integer.valueOf(location),
          type);
      }

      Preconditions.checkPrecondition(
        name,
        !out.containsKey(name),
        ignored -> "Attribute name must be unique");

      final LWJGL3ProgramAttribute attrib = new LWJGL3ProgramAttribute(
        this.context, program, location, name, type);
      out.put(name, attrib);
    }
  }

  private void getUniforms(
    final JCGLProgramShaderUsableType program,
    final Map<String, JCGLProgramUniformType> out)
    throws JCGLException
  {
    final int id = program.getGLName();
    final int max =
      GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS);
    final int length =
      GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);

    final ByteBuffer buffer_name = BufferUtils.createByteBuffer(length);
    final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_size = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_type = BufferUtils.createIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      GL20.glGetActiveUniform(
        id, index, buffer_length, buffer_size, buffer_type, buffer_name);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = LWJGL3TypeConversions.typeFromGL(type_raw);

      final int size = buffer_size.get(0);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = GL20.glGetUniformLocation(id, name);
      if (location == -1) {
        if (LOG.isTraceEnabled()) {
          LOG.trace(
            "ignoring active uniform '{}' with location -1", name);
        }
        continue;
      }

      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "[{}] uniform {} {} {} {} (size {})",
          program.getName(),
          Integer.valueOf(index),
          name,
          Integer.valueOf(location),
          type,
          Integer.valueOf(size));
      }

      Preconditions.checkPrecondition(
        name,
        !out.containsKey(name),
        ignored -> "Uniform name must be unique");

      final LWJGL3ProgramUniform uniform = new LWJGL3ProgramUniform(
        this.context, program, location, name, type, size);
      out.put(name, uniform);
    }
  }

  @Override
  public void shaderUniformSetTypeCheckingEnabled(final boolean enabled)
  {
    this.check_type = enabled;
  }

  @Override
  public void shaderUniformSetActivityCheckingEnabled(final boolean enabled)
  {
    this.check_active = enabled;
  }

  @Override
  public void shaderUniformPutFloat(
    final JCGLProgramUniformType u,
    final float value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT);
    GL20.glUniform1f(u.getGLName(), value);
  }

  @Override
  public void shaderUniformPutInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER);
    GL20.glUniform1i(u.getGLName(), value);
  }

  @Override
  public void shaderUniformPutUnsignedInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER);
    GL30.glUniform1ui(u.getGLName(), value);
  }

  @Override
  public void shaderUniformPutVectorf(
    final JCGLProgramUniformType u,
    final FloatBuffer value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActive(u);
    this.checkIsFloatingPoint(u);

    final int available = value.capacity();
    final JCGLType type = u.getType();
    final int required = type.getElementCount();
    if (available < required) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Uniform data error.");
      sb.append(System.lineSeparator());
      sb.append("Expected: A buffer containing at least ");
      sb.append(required);
      sb.append(" floating point values");
      sb.append(System.lineSeparator());
      sb.append("Actual: A buffer containing ");
      sb.append(available);
      sb.append(" floating point values");
      throw new JCGLExceptionProgramTypeError(sb.toString());
    }

    final int location = u.getGLName();
    final int elements = u.getSize();
    switch (type) {
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_INTEGER:
      case TYPE_INTEGER_VECTOR_2:
      case TYPE_INTEGER_VECTOR_3:
      case TYPE_INTEGER_VECTOR_4:
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
      case TYPE_UNSIGNED_INTEGER:
      case TYPE_UNSIGNED_INTEGER_VECTOR_2:
      case TYPE_UNSIGNED_INTEGER_VECTOR_3:
      case TYPE_UNSIGNED_INTEGER_VECTOR_4:
        throw new UnreachableCodeException();
      case TYPE_FLOAT:
        GL20.glUniform1fv(location, value);
        break;
      case TYPE_FLOAT_VECTOR_2:
        GL20.glUniform2fv(location, value);
        break;
      case TYPE_FLOAT_VECTOR_3:
        GL20.glUniform3fv(location, value);
        break;
      case TYPE_FLOAT_VECTOR_4:
        GL20.glUniform4fv(location, value);
        break;
      case TYPE_FLOAT_MATRIX_2:
        GL20.glUniformMatrix2fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_3:
        GL20.glUniformMatrix3fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_4:
        GL20.glUniformMatrix4fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_4x3:
        GL21.glUniformMatrix4x3fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_4x2:
        GL21.glUniformMatrix4x2fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_3x4:
        GL21.glUniformMatrix3x4fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_3x2:
        GL21.glUniformMatrix3x2fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_2x4:
        GL21.glUniformMatrix2x4fv(location, false, value);
        break;
      case TYPE_FLOAT_MATRIX_2x3:
        GL21.glUniformMatrix2x3fv(location, false, value);
        break;
    }
  }

  @Override
  public void shaderUniformPutVector2f(
    final JCGLProgramUniformType u,
    final Vector2D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
    GL20.glUniform2f(
      u.getGLName(),
      (float) value.x(),
      (float) value.y());
  }

  @Override
  public void shaderUniformPutVector3f(
    final JCGLProgramUniformType u,
    final Vector3D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
    GL20.glUniform3f(
      u.getGLName(),
      (float) value.x(),
      (float) value.y(),
      (float) value.z());
  }

  @Override
  public void shaderUniformPutVector4f(
    final JCGLProgramUniformType u,
    final Vector4D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
    GL20.glUniform4f(
      u.getGLName(),
      (float) value.x(),
      (float) value.y(),
      (float) value.z(),
      (float) value.w());
  }

  @Override
  public void shaderUniformPutVector2i(
    final JCGLProgramUniformType u,
    final Vector2I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
    GL20.glUniform2i(
      u.getGLName(), value.x(), value.y());
  }

  @Override
  public void shaderUniformPutVector3i(
    final JCGLProgramUniformType u,
    final Vector3I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
    GL20.glUniform3i(
      u.getGLName(), value.x(), value.y(), value.z());
  }

  @Override
  public void shaderUniformPutVector4i(
    final JCGLProgramUniformType u,
    final Vector4I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
    GL20.glUniform4i(
      u.getGLName(),
      value.x(),
      value.y(),
      value.z(),
      value.w());
  }

  @Override
  public void shaderUniformPutVector2ui(
    final JCGLProgramUniformType u,
    final Vector2I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
    GL30.glUniform2ui(u.getGLName(), value.x(), value.y());
  }

  @Override
  public void shaderUniformPutVector3ui(
    final JCGLProgramUniformType u,
    final Vector3I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
    GL30.glUniform3ui(
      u.getGLName(), value.x(), value.y(), value.z());
  }

  @Override
  public void shaderUniformPutVector4ui(
    final JCGLProgramUniformType u,
    final Vector4I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
    GL30.glUniform4ui(
      u.getGLName(),
      value.x(),
      value.y(),
      value.z(),
      value.w());
  }

  @Override
  public void shaderUniformPutMatrix3x3f(
    final JCGLProgramUniformType u,
    final Matrix3x3D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
    this.m3x3.setMatrix3x3D(value);
    GL20.glUniformMatrix3fv(u.getGLName(), false, this.m3x3_buffer_view);
  }

  @Override
  public void shaderUniformPutMatrix4x4f(
    final JCGLProgramUniformType u,
    final Matrix4x4D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
    this.m4x4.setMatrix4x4D(value);
    GL20.glUniformMatrix4fv(u.getGLName(), false, this.m4x4_buffer_view);
  }

  @Override
  public void shaderUniformPutTexture2DUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_2D);
    GL20.glUniform1i(u.getGLName(), value.unitGetIndex());
  }

  @Override
  public void shaderUniformPutTextureCubeUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_CUBE);
    GL20.glUniform1i(u.getGLName(), value.unitGetIndex());
  }

  private void checkActiveAndType(
    final JCGLProgramUniformType u,
    final JCGLType type_given)
  {
    this.checkActive(u);
    this.checkType(u, type_given);
  }

  private void checkType(
    final JCGLProgramUniformType u,
    final JCGLType type_given)
  {
    if (this.check_type) {
      final JCGLType type_uniform = u.getType();
      if (!Objects.equals(type_uniform, type_given)) {
        throw errorWrongType(u, type_given);
      }
    }
  }

  private void checkIsFloatingPoint(final JCGLProgramUniformType u)
  {
    if (this.check_type) {
      final JCGLType type_uniform = u.getType();
      if (!type_uniform.isFloatingPointType()) {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("Uniform type error.");
        sb.append(System.lineSeparator());
        sb.append("Expected: A floating point type");
        sb.append(System.lineSeparator());
        sb.append("Actual: ");
        sb.append(type_uniform);
        throw new JCGLExceptionProgramTypeError(sb.toString());
      }
    }
  }

  private void checkActive(final JCGLProgramUniformType u)
  {
    final JCGLProgramShaderUsableType u_program = u.getProgram();
    if (this.check_active) {
      if (!Objects.equals(u_program, this.current)) {
        throw this.errorNotActive(u_program);
      }
    }
  }

  private JCGLExceptionProgramNotActive errorNotActive(
    final JCGLProgramShaderUsableType u_program)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Program not active.");
    sb.append(System.lineSeparator());
    sb.append("Expected: ");
    sb.append(u_program);
    sb.append(System.lineSeparator());
    sb.append("Actual: ");
    sb.append(this.current);
    return new JCGLExceptionProgramNotActive(sb.toString());
  }
}
