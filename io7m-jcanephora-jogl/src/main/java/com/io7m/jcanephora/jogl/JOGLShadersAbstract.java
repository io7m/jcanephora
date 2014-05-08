/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLShadersGL2Type;
import com.io7m.jcanephora.api.JCGLShadersGLES2Type;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.MatrixReadable3x3FType;
import com.io7m.jtensors.MatrixReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.jogamp.common.nio.Buffers;

abstract class JOGLShadersAbstract implements
  JCGLShadersGLES2Type,
  JCGLShadersGL2Type
{
  /**
   * Check that the type of the given attribute is <code>t</code>.
   */

  protected static final void checkAttributeType(
    final ProgramAttributeType pa,
    final JCGLType t)
    throws JCGLExceptionTypeError
  {
    final JCGLType at = pa.attributeGetType();

    if (at != t) {
      final StringBuilder b = new StringBuilder();
      b.append("The program attribute '");
      b.append(pa.attributeGetName());
      b.append("' is of type ");
      b.append(pa.attributeGetType());
      b.append(" but the given value is of type ");
      b.append(t);
      final String r = b.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  /**
   * Check that the given fragment shader:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  protected static final void checkFragmentShader(
    final GL2ES2 g,
    final FragmentShaderType id)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(id, "Fragment shader");
    final GLContext ctx = g.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkFragmentShader(ctx, id);
    ResourceCheck.notDeleted(id);
  }

  /**
   * Check that the given program:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  protected static final void checkProgram(
    final GL2ES2 g,
    final ProgramUsableType program)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(program, "Program");
    final GLContext ctx = g.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgram(ctx, program);
    ResourceCheck.notDeleted(program);
  }

  /**
   * Check that the type of the given attribute is a sampler type.
   */

  protected static final void checkUniformSamplerType(
    final ProgramUniformType pu)
    throws JCGLExceptionTypeError
  {
    final JCGLType at = pu.uniformGetType();

    if (at.isSamplerType() == false) {
      final StringBuilder b = new StringBuilder();
      b.append("The program uniform '");
      b.append(pu.uniformGetName());
      b.append("' is of type ");
      b.append(pu.uniformGetType());
      b.append(" but was expected to be of a sampler type");
      final String r = b.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  /**
   * Check that the type of the given uniform is <code>t</code>.
   */

  protected static final void checkUniformType(
    final ProgramUniformType pu,
    final JCGLType t)
    throws JCGLExceptionTypeError
  {
    final JCGLType at = pu.uniformGetType();

    if (at != t) {
      final StringBuilder b = new StringBuilder();
      b.append("The program uniform '");
      b.append(pu.uniformGetName());
      b.append("' is of type ");
      b.append(pu.uniformGetType());
      b.append(" but the given value is of type ");
      b.append(t);
      final String r = b.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }
  }

  /**
   * Check that the given vertex shader:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  protected static final void checkVertexShader(
    final GL2ES2 g,
    final VertexShaderType id)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(id, "Vertex shader");
    final GLContext ctx = g.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkVertexShader(ctx, id);
    ResourceCheck.notDeleted(id);
  }

  private final JCGLArrayBuffersType    arrays;

  private final GL2ES2                  gl;

  private final JOGLIntegerCacheType    icache;
  private final LogType                 log;
  private final JOGLLogMessageCacheType tcache;

  JOGLShadersAbstract(
    final GL2ES2 in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JCGLArrayBuffersType in_arrays)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("shaders");
    this.arrays = NullCheck.notNull(in_arrays, "Arrays");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
  }

  /**
   * Check that the given program attribute:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Belongs to a program that:
   * <ul>
   * <li>Is not null</li>
   * <li>Is active</li>
   * <li>Is not deleted</li>
   * </ul>
   * </li>
   * </ul>
   */

  protected final void checkAttribute(
    final ProgramAttributeType a)
    throws JCGLExceptionWrongContext,
      JCGLException
  {
    NullCheck.notNull(a, "Program attribute");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramAttribute(ctx, a);
    this.checkProgramAndActive(this.gl, a.attributeGetProgram());
  }

  /**
   * Check that the given program attribute satisfies
   * {@link #checkAttribute(ProgramAttributeType)} and is of type
   * <code>t</code>.
   */

  protected final void checkAttributeAndType(
    final ProgramAttributeType a,
    final JCGLType t)
    throws JCGLException
  {
    this.checkAttribute(a);
    JOGLShadersAbstract.checkAttributeType(a, t);
  }

  /**
   * Check that the given program satisfies
   * {@link #checkProgram(GL2ES2, ProgramUsableType)} and is active.
   */

  protected final void checkProgramAndActive(
    final GL2ES2 g,
    final ProgramUsableType program)
    throws JCGLException
  {
    JOGLShadersAbstract.checkProgram(g, program);

    if (this.programIsActive(program) == false) {
      final String s =
        String.format("Program '%s' is not active", program.programGetName());
      assert s != null;
      throw new JCGLExceptionProgramNotActive(s);
    }
  }

  protected final void checkUniformAndSamplerType(
    final ProgramUniformType pu)
    throws JCGLException
  {
    NullCheck.notNull(pu, "Program uniform");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramUniform(ctx, pu);
    this.checkProgramAndActive(this.gl, pu.uniformGetProgram());
    JOGLShadersAbstract.checkUniformSamplerType(pu);
  }

  /**
   * Check that the given program uniform:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Belongs to a program that:
   * <ul>
   * <li>Is not null</li>
   * <li>Is active</li>
   * <li>Is not deleted</li>
   * </ul>
   * </li>
   * <li>Has type <code>t</code></li>
   * </ul>
   */

  protected final void checkUniformAndType(
    final ProgramUniformType u,
    final JCGLType t)
    throws JCGLException
  {
    NullCheck.notNull(u, "Program uniform");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramUniform(ctx, u);
    this.checkProgramAndActive(this.gl, u.uniformGetProgram());
    JOGLShadersAbstract.checkUniformType(u, t);
  }

  @Override public final FragmentShaderType fragmentShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    if (ShaderUtilities.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError("<none>", "Empty program");
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("compile \"");
      text.append(name);
      text.append("\"");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final int id = this.gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    JOGLErrors.check(this.gl);

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    this.gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGLErrors.check(this.gl);
    this.gl.glCompileShader(id);
    JOGLErrors.check(this.gl);
    final int status =
      this.icache.getShaderInteger(this.gl, id, GL2ES2.GL_COMPILE_STATUS);
    JOGLErrors.check(this.gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGLErrors.check(this.gl);

      final byte[] raw = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String tt = new String(raw);
      throw new JCGLExceptionProgramCompileError(name, tt);
    }

    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    return new JOGLFragmentShader(ctx, id, name);
  }

  @Override public final void fragmentShaderDelete(
    final FragmentShaderType id)
    throws JCGLException
  {
    JOGLShadersAbstract.checkFragmentShader(this.gl, id);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    this.gl.glDeleteShader(id.getGLName());
    ((JOGLObjectDeletable) id).resourceSetDeleted();
    JOGLErrors.check(this.gl);
  }

  protected final JOGLIntegerCacheType getIcache()
  {
    return this.icache;
  }

  public LogType getLog()
  {
    return this.log;
  }

  protected final JOGLLogMessageCacheType getTcache()
  {
    return this.tcache;
  }

  @Override public final void programActivate(
    final ProgramUsableType program)
    throws JCGLException,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLShadersAbstract.checkProgram(this.gl, program);
    this.gl.glUseProgram(program.getGLName());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributeArrayAssociate(
    final ProgramAttributeType pa,
    final ArrayAttributeType aa)
    throws JCGLException
  {
    NullCheck.notNull(aa, "Array attribute");
    this.checkAttribute(pa);

    final ArrayBufferUsableType array = aa.getArray();
    JOGLArrays.checkArray(this.gl, array);

    if (this.arrays.arrayBufferIsBound(array) == false) {
      throw JCGLExceptionBufferNotBound.notBound(array);
    }

    final ArrayAttributeDescriptor ad = aa.getDescriptor();
    final JCGLType pt = pa.attributeGetType();
    final JCGLType at = ad.getJCGLType();

    final StringBuilder text = this.tcache.getTextCache();
    if (pt.equals(at) == false) {
      text.setLength(0);
      text.append("Program and array attributes incompatible.\n");
      text.append("  Program attribute ");
      text.append(pa.attributeGetName());
      text.append(" has type: ");
      text.append(pa.attributeGetType());
      text.append("  Array attribute ");
      text.append(ad.getName());
      text.append(" has type: ");
      text.append(ad.getType());
      final String r = text.toString();
      assert r != null;
      throw new JCGLExceptionTypeError(r);
    }

    final int pa_id = pa.attributeGetLocation();
    final int type = JOGL_GLTypeConversions.scalarTypeToGL(ad.getType());
    final boolean normalized = false;
    final int stride = (int) array.bufferGetElementSizeBytes();
    final long offset =
      array.arrayGetDescriptor().getAttributeOffset(ad.getName());

    this.gl.glEnableVertexAttribArray(pa_id);
    JOGLErrors.check(this.gl);
    this.gl.glVertexAttribPointer(
      pa_id,
      ad.getComponents(),
      type,
      normalized,
      stride,
      offset);
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributeArrayDisassociate(
    final ProgramAttributeType pa)
    throws JCGLException
  {
    this.checkAttribute(pa);
    this.gl.glDisableVertexAttribArray(pa.attributeGetLocation());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributePutFloat(
    final ProgramAttributeType pa,
    final float x)
    throws JCGLException
  {
    this.checkAttributeAndType(pa, JCGLType.TYPE_FLOAT);
    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib1f(pal, x);
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributePutVector2f(
    final ProgramAttributeType pa,
    final VectorReadable2FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_2);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib2f(pal, x.getXF(), x.getYF());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributePutVector3f(
    final ProgramAttributeType pa,
    final VectorReadable3FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_3);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib3f(pal, x.getXF(), x.getYF(), x.getZF());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programAttributePutVector4f(
    final ProgramAttributeType pa,
    final VectorReadable4FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_4);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib4f(pal, x.getXF(), x.getYF(), x.getZF(), x.getWF());
    JOGLErrors.check(this.gl);
  }

  @Override public final ProgramType programCreateCommon(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f)
    throws JCGLException,
      JCGLExceptionProgramCompileError
  {
    NullCheck.notNull(name, "Name");
    JOGLShadersAbstract.checkFragmentShader(this.gl, f);
    JOGLShadersAbstract.checkVertexShader(this.gl, v);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("create \"");
      text.append(name);
      text.append("\" with ");
      text.append(v);
      text.append(" ");
      text.append(f);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final int id = this.gl.glCreateProgram();
    if (id == 0) {
      throw new JCGLExceptionRuntime(0, "glCreateProgram failed");
    }
    JOGLErrors.check(this.gl);

    this.gl.glAttachShader(id, v.getGLName());
    JOGLErrors.check(this.gl);
    this.gl.glAttachShader(id, f.getGLName());
    JOGLErrors.check(this.gl);
    this.gl.glLinkProgram(id);
    JOGLErrors.check(this.gl);

    final int status =
      this.icache.getProgramInteger(this.gl, id, GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetProgramInfoLog(id, 8192, buffer_length, buffer);
      JOGLErrors.check(this.gl);

      final byte[] raw = new byte[buffer.remaining()];
      buffer.get(raw);
      final String tt = new String(raw);
      throw new JCGLExceptionProgramCompileError(name, tt);
    }

    JOGLErrors.check(this.gl);

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("created ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final Map<String, ProgramAttributeType> attributes =
      new HashMap<String, ProgramAttributeType>();
    final Map<String, ProgramUniformType> uniforms =
      new HashMap<String, ProgramUniformType>();

    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    final JOGLProgram program =
      new JOGLProgram(ctx, id, name, uniforms, attributes);

    this.programGetAttributes(program, attributes);
    this.programGetUniforms(program, uniforms);
    return program;
  }

  @Override public final void programDeactivate()
    throws JCGLException
  {
    this.gl.glUseProgram(0);
    JOGLErrors.check(this.gl);
  }

  @Override public final void programDelete(
    final ProgramType program)
    throws JCGLException,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLShadersAbstract.checkProgram(this.gl, program);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(program);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    this.gl.glDeleteProgram(program.getGLName());
    ((JOGLObjectDeletable) program).resourceSetDeleted();
    JOGLErrors.check(this.gl);
  }

  protected final void programGetAttributes(
    final ProgramUsableType program,
    final Map<String, ProgramAttributeType> out)
    throws JCGLExceptionRuntime
  {

    final int id = program.getGLName();
    final int max =
      this.icache.getProgramInteger(
        this.gl,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      this.icache.getProgramInteger(
        this.gl,
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

      this.gl.glGetActiveAttrib(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      JOGLErrors.check(this.gl);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.gl.glGetAttribLocation(id, name);
      JOGLErrors.check(this.gl);

      final StringBuilder text = this.tcache.getTextCache();
      if (location == -1) {
        if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("driver returned active attribute \"");
          text.append(name);
          text.append("\" with location -1, ignoring");
          final String r = text.toString();
          assert r != null;
          this.log.debug(r);
        }
        continue;
      }

      assert out.containsKey(name) == false;
      final GLContext ctx = this.gl.getContext();
      assert ctx != null;
      out.put(name, new JOGLProgramAttribute(
        ctx,
        program,
        index,
        location,
        name,
        type));
    }
  }

  @Override public final int programGetMaximumActiveAttributes()
    throws JCGLException
  {
    final int max =
      this.icache.getInteger(this.gl, GL2ES2.GL_MAX_VERTEX_ATTRIBS);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("implementation supports ");
      text.append(max);
      text.append(" active attributes");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    return max;
  }

  protected final void programGetUniforms(
    final ProgramUsableType program,
    final Map<String, ProgramUniformType> out)
    throws JCGLExceptionRuntime
  {
    final int id = program.getGLName();
    final int max =
      this.icache.getProgramInteger(this.gl, id, GL2ES2.GL_ACTIVE_UNIFORMS);
    final int length =
      this.icache.getProgramInteger(
        this.gl,
        id,
        GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    JOGLErrors.check(this.gl);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      this.gl.glGetActiveUniform(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      JOGLErrors.check(this.gl);

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.gl.glGetUniformLocation(id, name);
      JOGLErrors.check(this.gl);

      final StringBuilder text = this.tcache.getTextCache();
      if (location == -1) {
        if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("driver returned active uniform \"");
          text.append(name);
          text.append("\" with location -1, ignoring");
          final String r = text.toString();
          assert r != null;
          this.log.debug(r);
        }
        continue;
      }

      final GLContext ctx = this.gl.getContext();
      assert ctx != null;
      assert (out.containsKey(name) == false);
      out.put(name, new JOGLProgramUniform(
        ctx,
        program,
        index,
        location,
        name,
        type));
    }
  }

  @Override public final boolean programIsActive(
    final ProgramUsableType program)
    throws JCGLException
  {
    JOGLShadersAbstract.checkProgram(this.gl, program);

    final int active =
      this.icache.getInteger(this.gl, GL2ES2.GL_CURRENT_PROGRAM);
    JOGLErrors.check(this.gl);
    return active == program.getGLName();
  }

  @Override public final void programUniformPutFloat(
    final ProgramUniformType pu,
    final float x)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT);
    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1f(pul, x);
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutInteger(
    final ProgramUniformType pu,
    final int x)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_INTEGER);
    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1i(pul, x);
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutMatrix3x3f(
    final ProgramUniformType pu,
    final MatrixReadable3x3FType matrix)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_MATRIX_3);
    NullCheck.notNull(matrix, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniformMatrix3fv(pul, 1, false, matrix.getFloatBuffer());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutMatrix4x4f(
    final ProgramUniformType pu,
    final MatrixReadable4x4FType matrix)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_MATRIX_4);
    NullCheck.notNull(matrix, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniformMatrix4fv(pul, 1, false, matrix.getFloatBuffer());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutTextureUnit(
    final ProgramUniformType pu,
    final TextureUnitType unit)
    throws JCGLException
  {
    this.checkUniformAndSamplerType(pu);
    NullCheck.notNull(unit, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1i(pul, unit.unitGetIndex());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector2f(
    final ProgramUniformType pu,
    final VectorReadable2FType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_2);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform2f(pul, v.getXF(), v.getYF());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector2i(
    final ProgramUniformType pu,
    final VectorReadable2IType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_2);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform2i(pul, v.getXI(), v.getYI());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector3f(
    final ProgramUniformType pu,
    final VectorReadable3FType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_3);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform3f(pul, v.getXF(), v.getYF(), v.getZF());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector3i(
    final ProgramUniformType pu,
    final VectorReadable3IType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_3);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform3i(pul, v.getXI(), v.getYI(), v.getZI());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector4f(
    final ProgramUniformType pu,
    final VectorReadable4FType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_4);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform4f(pul, v.getXF(), v.getYF(), v.getZF(), v.getWF());
    JOGLErrors.check(this.gl);
  }

  @Override public final void programUniformPutVector4i(
    final ProgramUniformType pu,
    final VectorReadable4IType v)
    throws JCGLException
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_4);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform4i(pul, v.getXI(), v.getYI(), v.getZI(), v.getWI());
    JOGLErrors.check(this.gl);
  }

  @Override public final VertexShaderType vertexShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    if (ShaderUtilities.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError("<none>", "Empty program");
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("compile \"");
      text.append(name);
      text.append("\"");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final int id = this.gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    JOGLErrors.check(this.gl);

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    this.gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGLErrors.check(this.gl);
    this.gl.glCompileShader(id);
    JOGLErrors.check(this.gl);
    final int status =
      this.icache.getShaderInteger(this.gl, id, GL2ES2.GL_COMPILE_STATUS);
    JOGLErrors.check(this.gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGLErrors.check(this.gl);

      final byte[] raw = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String tt = new String(raw);
      throw new JCGLExceptionProgramCompileError(name, tt);
    }

    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    return new JOGLVertexShader(ctx, id, name);
  }

  @Override public final void vertexShaderDelete(
    final VertexShaderType id)
    throws JCGLException
  {
    JOGLShadersAbstract.checkVertexShader(this.gl, id);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    this.gl.glDeleteShader(id.getGLName());
    ((JOGLObjectDeletable) id).resourceSetDeleted();
    JOGLErrors.check(this.gl);
  }
}
