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
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
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

  JOGLShaders(final JOGLContext c)
  {
    this.context = NullCheck.notNull(c);
    this.g3 = c.getGL3();
    this.icache = Buffers.newDirectIntBuffer(1);
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
    final String tt = new String(raw);
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
    final JCGLVertexShaderUsableType v,
    final Optional<JCGLGeometryShaderUsableType> g,
    final JCGLFragmentShaderUsableType f)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(v, "Vertex shader");
    NullCheck.notNull(g, "Geometry shader");
    NullCheck.notNull(f, "Fragment shader");

    final GLContext c = this.context.getContext();
    JOGLCompatibilityChecks.checkVertexShader(c, v);
    JCGLResources.checkNotDeleted(v);
    JOGLCompatibilityChecks.checkFragmentShader(c, f);
    JCGLResources.checkNotDeleted(f);

    g.ifPresent(
      gg -> {
        JOGLCompatibilityChecks.checkGeometryShader(c, gg);
        JCGLResources.checkNotDeleted(gg);
      });

    JOGLShaders.LOG.debug("link program {}", name);
    JOGLShaders.LOG.debug("[{}] vertex {}", name, v.getName());
    g.ifPresent(
      gg -> JOGLShaders.LOG.debug("[{}] geometry {}", name, gg.getName()));
    JOGLShaders.LOG.debug("[{}] fragment {}", name, f.getName());

    final int pid = this.g3.glCreateProgram();
    Assertive.ensure(pid > 0);
    this.g3.glAttachShader(pid, v.getGLName());
    this.g3.glAttachShader(pid, f.getGLName());
    g.ifPresent(gg -> this.g3.glAttachShader(pid, gg.getGLName()));
    this.g3.glLinkProgram(pid);

    this.icache.rewind();
    this.g3.glGetProgramiv(pid, GL3.GL_LINK_STATUS, this.icache);
    final int status = this.icache.get(0);
    if (status == 0) {
      throw JOGLShaders.getLinkError(this.g3, name, pid);
    }

    final Map<String, JCGLProgramAttributeType> attributes = new HashMap<>(16);
    final Map<String, JCGLProgramUniformType> uniforms = new HashMap<>(32);

    final JOGLProgramShader program = new JOGLProgramShader(c, pid, name);

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
}
