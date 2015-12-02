/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.jogl;

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
import com.io7m.jtensors.MatrixDirect3x3FType;
import com.io7m.jtensors.MatrixDirect4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

final class JOGLShaders implements JCGLShadersType
{
  private static final Logger  LOG;
  private static final Pattern NON_EMPTY;

  static {
    LOG = LoggerFactory.getLogger(JOGLShaders.class);
    NON_EMPTY = Pattern.compile("^\\s*$");
  }

  private final     JOGLContext                 context;
  private final     GL3                         g3;
  private final     IntBuffer                   icache;
  private @Nullable JCGLProgramShaderUsableType current;
  private           boolean                     check_type;
  private           boolean                     check_active;

  JOGLShaders(final JOGLContext c)
  {
    this.context = NullCheck.notNull(c);
    this.g3 = c.getGL3();
    this.icache = Buffers.newDirectIntBuffer(1);
    this.check_active = true;
    this.check_type = true;
  }

  private static boolean isEmpty(final List<String> lines)
  {
    NullCheck.notNull(lines, "Lines");

    for (final String line : lines) {
      NullCheck.notNull(line, "Line");
      if (!JOGLShaders.NON_EMPTY.matcher(line).matches()) {
        return false;
      }
    }

    return true;
  }

  private static JCGLExceptionProgramCompileError getCompilationError(
    final GL3 g3,
    final String name,
    final int id)
  {
    final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    g3.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);

    final byte[] raw = new byte[buffer_length.get(0)];
    log_buffer.get(raw);
    final String tt = new String(raw, StandardCharsets.UTF_8);
    return new JCGLExceptionProgramCompileError(name, tt);
  }

  private static JCGLExceptionProgramCompileError getLinkError(
    final GL3 g3,
    final String name,
    final int id)
  {
    final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    g3.glGetProgramInfoLog(id, 8192, buffer_length, log_buffer);

    final byte[] raw = new byte[buffer_length.get(0)];
    log_buffer.get(raw);
    final String tt = new String(raw);
    return new JCGLExceptionProgramCompileError(name, tt);
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

  @Override public void shaderDeleteProgram(final JCGLProgramShaderType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p);

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkProgramShader(c, p);
    JCGLResources.checkNotDeleted(p);

    JOGLShaders.LOG.debug("delete program shader {}", p.getName());

    this.g3.glDeleteProgram(p.getGLName());
    ((JOGLObjectDeletable) p).setDeleted();

    if (p.equals(this.current)) {
      this.current = null;
    }
  }

  @Override public void shaderDeleteVertex(final JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(v);

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkVertexShader(c, v);
    JCGLResources.checkNotDeleted(v);

    JOGLShaders.LOG.debug("delete vertex shader {}", v.getName());

    this.g3.glDeleteShader(v.getGLName());
    ((JOGLObjectDeletable) v).setDeleted();
  }

  @Override public void shaderDeleteFragment(final JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(f);

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkFragmentShader(c, f);
    JCGLResources.checkNotDeleted(f);

    JOGLShaders.LOG.debug("delete fragment shader {}", f.getName());

    this.g3.glDeleteShader(f.getGLName());
    ((JOGLObjectDeletable) f).setDeleted();
  }

  @Override public void shaderDeleteGeometry(final JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(g);

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkGeometryShader(c, g);
    JCGLResources.checkNotDeleted(g);

    JOGLShaders.LOG.debug("delete geometry shader {}", g.getName());

    this.g3.glDeleteShader(g.getGLName());
    ((JOGLObjectDeletable) g).setDeleted();
  }

  @Override public JCGLVertexShaderType shaderCompileVertex(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    JOGLShaders.LOG.debug(
      "compile vertex shader {} ({} lines)", name, Integer.valueOf(size));

    if (JOGLShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = this.g3.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    Assertive.ensure(id > 0);

    try {
      this.compileSources(name, lines, size, id);
      JOGLShaders.LOG.debug(
        "compiled vertex shader {} ⇒ {}", name, Integer.valueOf(id));
    } catch (final GLException | JCGLException e) {
      JOGLShaders.LOG.debug(
        "delete vertex shader {} ⇒ {}", name, Integer.valueOf(id));
      this.g3.glDeleteShader(id);
      throw e;
    }

    return new JOGLVertexShader(
      NullCheck.notNull(this.g3.getContext()), id, name);
  }

  private void compileSources(
    final String name,
    final List<String> lines,
    final int size,
    final int id)
  {
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(size);
    final String[] line_array = new String[size];
    JOGLShaders.measureLines(lines, size, line_array, line_lengths);

    this.g3.glShaderSource(id, line_array.length, line_array, line_lengths);
    this.g3.glCompileShader(id);

    this.icache.rewind();
    this.g3.glGetShaderiv(id, GL3.GL_COMPILE_STATUS, this.icache);
    final int status = this.icache.get(0);
    if (status == 0) {
      throw JOGLShaders.getCompilationError(this.g3, name, id);
    }
  }

  @Override public JCGLFragmentShaderType shaderCompileFragment(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    JOGLShaders.LOG.debug(
      "compile fragment shader {} ({} lines)", name, Integer.valueOf(size));

    if (JOGLShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = this.g3.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    Assertive.ensure(id > 0);

    try {
      this.compileSources(name, lines, size, id);
      JOGLShaders.LOG.debug(
        "compiled fragment shader {} ⇒ {}", name, Integer.valueOf(id));
    } catch (final GLException | JCGLException e) {
      JOGLShaders.LOG.debug(
        "delete fragment shader {} ⇒ {}", name, Integer.valueOf(id));
      this.g3.glDeleteShader(id);
      throw e;
    }

    return new JOGLFragmentShader(
      NullCheck.notNull(this.g3.getContext()), id, name);
  }

  @Override public JCGLGeometryShaderType shaderCompileGeometry(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    JOGLShaders.LOG.debug(
      "compile geometry shader {} ({} lines)", name, Integer.valueOf(size));

    if (JOGLShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    final int id = this.g3.glCreateShader(GL3.GL_GEOMETRY_SHADER);
    Assertive.ensure(id > 0);

    try {
      this.compileSources(name, lines, size, id);
      JOGLShaders.LOG.debug(
        "compiled geometry shader {} ⇒ {}", name, Integer.valueOf(id));
    } catch (final GLException | JCGLException e) {
      JOGLShaders.LOG.debug(
        "delete geometry shader {} ⇒ {}", name, Integer.valueOf(id));
      this.g3.glDeleteShader(id);
      throw e;
    }

    return new JOGLGeometryShader(
      NullCheck.notNull(this.g3.getContext()), id, name);
  }

  @Override public JCGLProgramShaderType shaderLinkProgram(
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

    final GLContext c = this.context.getContext();
    final JOGLVertexShader v = JOGLCompatibilityChecks.checkVertexShader(c, jv);
    JCGLResources.checkNotDeleted(v);
    final JOGLFragmentShader f =
      JOGLCompatibilityChecks.checkFragmentShader(c, jf);
    JCGLResources.checkNotDeleted(f);

    final Optional<JOGLGeometryShader> g = jg.map(
      gg -> {
        final JOGLGeometryShader rg =
          JOGLCompatibilityChecks.checkGeometryShader(c, gg);
        JCGLResources.checkNotDeleted(rg);
        return rg;
      });

    JOGLShaders.LOG.debug("link program {}", name);
    JOGLShaders.LOG.debug("[{}] vertex {}", name, v.getName());
    jg.ifPresent(
      gg -> JOGLShaders.LOG.debug("[{}] geometry {}", name, gg.getName()));
    JOGLShaders.LOG.debug("[{}] fragment {}", name, f.getName());

    final int pid = this.g3.glCreateProgram();
    Assertive.ensure(pid > 0);
    this.g3.glAttachShader(pid, v.getGLName());
    this.g3.glAttachShader(pid, f.getGLName());
    jg.ifPresent(gg -> this.g3.glAttachShader(pid, gg.getGLName()));
    this.g3.glLinkProgram(pid);

    this.icache.rewind();
    this.g3.glGetProgramiv(pid, GL3.GL_LINK_STATUS, this.icache);
    final int status = this.icache.get(0);
    if (status == 0) {
      throw JOGLShaders.getLinkError(this.g3, name, pid);
    }

    final Map<String, JCGLProgramAttributeType> attributes = new HashMap<>(16);
    final Map<String, JCGLProgramUniformType> uniforms = new HashMap<>(32);

    final JOGLProgramShader program = new JOGLProgramShader(
      c, pid, name, v, g, f);

    this.getAttributes(program, attributes);
    this.getUniforms(program, uniforms);

    program.setAttributes(attributes);
    program.setUniforms(uniforms);
    return program;
  }

  @Override public void shaderActivateProgram(
    final JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p);

    JOGLShaders.LOG.trace("activate {}", p.getName());

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkProgramShader(c, p);
    JCGLResources.checkNotDeleted(p);

    this.g3.glUseProgram(p.getGLName());
    this.current = p;
  }

  @Override public void shaderDeactivateProgram()
    throws JCGLException
  {
    JOGLShaders.LOG.trace("deactivate");

    this.g3.glUseProgram(0);
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
    final GLContext c = this.context.getContext();

    final int id = program.getGLName();
    this.g3.glGetProgramiv(id, GL3.GL_ACTIVE_ATTRIBUTES, this.icache);
    final int max = this.icache.get(0);
    this.g3.glGetProgramiv(id, GL3.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, this.icache);
    final int length = this.icache.get(0);

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

      this.g3.glGetActiveAttrib(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.g3.glGetAttribLocation(id, name);
      if (location == -1) {
        JOGLShaders.LOG.trace(
          "ignoring active attribute '{}' with location -1", name);
        continue;
      }

      JOGLShaders.LOG.trace(
        "[{}] attribute {} {} {} {}",
        program.getName(),
        Integer.valueOf(index),
        name,
        Integer.valueOf(location),
        type);

      Assertive.require(!out.containsKey(name));
      final JOGLProgramAttribute attrib = new JOGLProgramAttribute(
        c, program, location, name, type);
      out.put(name, attrib);
    }
  }

  private void getUniforms(
    final JCGLProgramShaderUsableType program,
    final Map<String, JCGLProgramUniformType> out)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();

    final int id = program.getGLName();
    this.g3.glGetProgramiv(id, GL3.GL_ACTIVE_UNIFORMS, this.icache);
    final int max = this.icache.get(0);
    this.g3.glGetProgramiv(id, GL3.GL_ACTIVE_UNIFORM_MAX_LENGTH, this.icache);
    final int length = this.icache.get(0);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      this.g3.glGetActiveUniform(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.g3.glGetUniformLocation(id, name);
      if (location == -1) {
        JOGLShaders.LOG.trace(
          "ignoring active uniform '{}' with location -1", name);
        continue;
      }

      JOGLShaders.LOG.trace(
        "[{}] uniform {} {} {} {}",
        program.getName(),
        Integer.valueOf(index),
        name,
        Integer.valueOf(location),
        type);

      Assertive.require(!out.containsKey(name));
      final JOGLProgramUniform uniform =
        new JOGLProgramUniform(c, program, location, name, type);
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

  @Override public void shaderUniformPutFloat(
    final JCGLProgramUniformType u,
    final float value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT);
    this.g3.glUniform1f(u.getGLName(), value);
  }

  @Override public void shaderUniformPutInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER);
    this.g3.glUniform1i(u.getGLName(), value);
  }

  @Override public void shaderUniformPutUnsignedInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER);
    this.g3.glUniform1ui(u.getGLName(), value);
  }

  @Override public void shaderUniformPutVector2f(
    final JCGLProgramUniformType u,
    final VectorReadable2FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
    this.g3.glUniform2f(u.getGLName(), value.getXF(), value.getYF());
  }

  @Override public void shaderUniformPutVector3f(
    final JCGLProgramUniformType u,
    final VectorReadable3FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
    this.g3.glUniform3f(
      u.getGLName(), value.getXF(), value.getYF(), value.getZF());
  }

  @Override public void shaderUniformPutVector4f(
    final JCGLProgramUniformType u,
    final VectorReadable4FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
    this.g3.glUniform4f(
      u.getGLName(),
      value.getXF(),
      value.getYF(),
      value.getZF(),
      value.getWF());
  }

  @Override public void shaderUniformPutVector2i(
    final JCGLProgramUniformType u,
    final VectorReadable2IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
    this.g3.glUniform2i(
      u.getGLName(), value.getXI(), value.getYI());
  }

  @Override public void shaderUniformPutVector3i(
    final JCGLProgramUniformType u,
    final VectorReadable3IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
    this.g3.glUniform3i(
      u.getGLName(), value.getXI(), value.getYI(), value.getZI());
  }

  @Override public void shaderUniformPutVector4i(
    final JCGLProgramUniformType u,
    final VectorReadable4IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
    this.g3.glUniform4i(
      u.getGLName(),
      value.getXI(),
      value.getYI(),
      value.getZI(),
      value.getWI());
  }

  @Override public void shaderUniformPutVector2ui(
    final JCGLProgramUniformType u,
    final VectorReadable2IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
    this.g3.glUniform2ui(
      u.getGLName(), value.getXI(), value.getYI());
  }

  @Override public void shaderUniformPutVector3ui(
    final JCGLProgramUniformType u,
    final VectorReadable3IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
    this.g3.glUniform3ui(
      u.getGLName(), value.getXI(), value.getYI(), value.getZI());
  }

  @Override public void shaderUniformPutVector4ui(
    final JCGLProgramUniformType u,
    final VectorReadable4IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
    this.g3.glUniform4ui(
      u.getGLName(),
      value.getXI(),
      value.getYI(),
      value.getZI(),
      value.getWI());
  }

  @Override public void shaderUniformPutMatrix3x3f(
    final JCGLProgramUniformType u,
    final MatrixDirect3x3FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
    this.g3.glUniformMatrix3fv(
      u.getGLName(), 1, false, value.getDirectFloatBuffer());
  }

  @Override public void shaderUniformPutMatrix4x4f(
    final JCGLProgramUniformType u,
    final MatrixDirect4x4FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
    this.g3.glUniformMatrix4fv(
      u.getGLName(), 1, false, value.getDirectFloatBuffer());
  }

  @Override public void shaderUniformPutTexture2DUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_2D);
    this.g3.glUniform1i(u.getGLName(), value.unitGetIndex());
  }

  @Override public void shaderUniformPutTextureCubeUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_CUBE);
    this.g3.glUniform1i(u.getGLName(), value.unitGetIndex());
  }

  private void checkActiveAndType(
    final JCGLProgramUniformType u,
    final JCGLType type_given)
  {
    final JCGLProgramShaderUsableType u_program = u.getProgram();
    if (this.check_active) {
      if (!u_program.equals(this.current)) {
        throw this.errorNotActive(u_program);
      }
    }

    if (this.check_type) {
      final JCGLType type_uniform = u.getType();
      if (!type_uniform.equals(type_given)) {
        throw JOGLShaders.errorWrongType(u, type_given);
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
