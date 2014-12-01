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
import com.io7m.jcanephora.api.JCGLShadersParametersType;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.MatrixDirectReadable3x3FType;
import com.io7m.jtensors.MatrixDirectReadable4x4FType;
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
  @SuppressWarnings("synthetic-access") private final class Unchecked implements
    JCGLShadersParametersType
  {
    public Unchecked()
    {
      // Nothing
    }

    @Override public void programAttributeArrayAssociate(
      final ProgramAttributeType program_attribute,
      final ArrayAttributeType array_attribute)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributeArrayAssociate(
        program_attribute,
        array_attribute,
        false);
    }

    @Override public void programAttributeArrayDisassociate(
      final ProgramAttributeType program_attribute)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributeArrayDisassociate(
        program_attribute,
        false);
    }

    @Override public void programAttributePutFloat(
      final ProgramAttributeType program_attribute,
      final float x)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributePutFloat(program_attribute, x, false);
    }

    @Override public void programAttributePutVector2f(
      final ProgramAttributeType program_attribute,
      final VectorReadable2FType x)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributePutVector2f(
        program_attribute,
        x,
        false);
    }

    @Override public void programAttributePutVector3f(
      final ProgramAttributeType program_attribute,
      final VectorReadable3FType x)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributePutVector3f(
        program_attribute,
        x,
        false);
    }

    @Override public void programAttributePutVector4f(
      final ProgramAttributeType program_attribute,
      final VectorReadable4FType x)
      throws JCGLException
    {
      JOGLShadersAbstract.this.attributePutVector4f(
        program_attribute,
        x,
        false);
    }

    @Override public void programUniformPutFloat(
      final ProgramUniformType uniform,
      final float value)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutFloat(uniform, value, false);
    }

    @Override public void programUniformPutInteger(
      final ProgramUniformType uniform,
      final int value)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutInteger(uniform, value, false);
    }

    @Override public void programUniformPutMatrix3x3f(
      final ProgramUniformType uniform,
      final MatrixDirectReadable3x3FType matrix)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutMatrix3x3f(uniform, matrix, false);
    }

    @Override public void programUniformPutMatrix4x4f(
      final ProgramUniformType uniform,
      final MatrixDirectReadable4x4FType matrix)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutMatrix4x4f(uniform, matrix, false);
    }

    @Override public void programUniformPutTextureUnit(
      final ProgramUniformType uniform,
      final TextureUnitType unit)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutTextureUnit(uniform, unit, false);
    }

    @Override public void programUniformPutVector2f(
      final ProgramUniformType uniform,
      final VectorReadable2FType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector2f(uniform, vector, false);
    }

    @Override public void programUniformPutVector2i(
      final ProgramUniformType uniform,
      final VectorReadable2IType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector2i(uniform, vector, false);
    }

    @Override public void programUniformPutVector3f(
      final ProgramUniformType uniform,
      final VectorReadable3FType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector3f(uniform, vector, false);
    }

    @Override public void programUniformPutVector3i(
      final ProgramUniformType uniform,
      final VectorReadable3IType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector3i(uniform, vector, false);
    }

    @Override public void programUniformPutVector4f(
      final ProgramUniformType uniform,
      final VectorReadable4FType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector4f(uniform, vector, false);
    }

    @Override public void programUniformPutVector4i(
      final ProgramUniformType uniform,
      final VectorReadable4IType vector)
      throws JCGLException
    {
      JOGLShadersAbstract.this.uniformPutVector4i(uniform, vector, false);
    }
  }

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

  private void attributeArrayAssociate(
    final ProgramAttributeType pa,
    final ArrayAttributeType aa,
    final boolean check_active)
  {
    NullCheck.notNull(aa, "Array attribute");
    this.checkAttribute(pa, check_active);

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
    final int type = JOGLTypeConversions.scalarTypeToGL(ad.getType());
    final boolean normalized = false;
    final int stride = (int) array.bufferGetElementSizeBytes();
    final long offset =
      array.arrayGetDescriptor().getAttributeOffset(ad.getName());

    this.gl.glEnableVertexAttribArray(pa_id);
    this.gl.glVertexAttribPointer(
      pa_id,
      ad.getComponents(),
      type,
      normalized,
      stride,
      offset);
  }

  private void attributeArrayDisassociate(
    final ProgramAttributeType pa,
    final boolean check_active)
  {
    this.checkAttribute(pa, check_active);
    this.gl.glDisableVertexAttribArray(pa.attributeGetLocation());
  }

  private void attributePutFloat(
    final ProgramAttributeType pa,
    final float x,
    final boolean check_active)
  {
    this.checkAttributeAndType(pa, JCGLType.TYPE_FLOAT, check_active);
    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib1f(pal, x);
  }

  private void attributePutVector2f(
    final ProgramAttributeType pa,
    final VectorReadable2FType x,
    final boolean check_active)
  {
    this
      .checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_2, check_active);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib2f(pal, x.getXF(), x.getYF());
  }

  private void attributePutVector3f(
    final ProgramAttributeType pa,
    final VectorReadable3FType x,
    final boolean check_active)
  {
    this
      .checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_3, check_active);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib3f(pal, x.getXF(), x.getYF(), x.getZF());
  }

  private void attributePutVector4f(
    final ProgramAttributeType pa,
    final VectorReadable4FType x,
    final boolean check_active)
  {
    this
      .checkAttributeAndType(pa, JCGLType.TYPE_FLOAT_VECTOR_4, check_active);
    NullCheck.notNull(x, "Value");

    final int pal = pa.attributeGetLocation();
    this.gl.glDisableVertexAttribArray(pal);
    this.gl.glVertexAttrib4f(pal, x.getXF(), x.getYF(), x.getZF(), x.getWF());
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
    final ProgramAttributeType a,
    final boolean check_active)
    throws JCGLExceptionWrongContext,
      JCGLException
  {
    NullCheck.notNull(a, "Program attribute");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramAttribute(ctx, a);
    this
      .checkProgramAndActive(this.gl, a.attributeGetProgram(), check_active);
  }

  /**
   * Check that the given program attribute satisfies
   * {@link #checkAttribute(ProgramAttributeType)} and is of type
   * <code>t</code>.
   */

  protected final void checkAttributeAndType(
    final ProgramAttributeType a,
    final JCGLType t,
    final boolean check_active)
    throws JCGLException
  {
    this.checkAttribute(a, check_active);
    JOGLShadersAbstract.checkAttributeType(a, t);
  }

  /**
   * Check that the given program satisfies
   * {@link #checkProgram(GL2ES2, ProgramUsableType)} and is active (if
   * activity checks are enabled).
   */

  protected final void checkProgramAndActive(
    final GL2ES2 g,
    final ProgramUsableType program,
    final boolean check_active)
    throws JCGLException
  {
    JOGLShadersAbstract.checkProgram(g, program);

    if (check_active) {
      if (this.programIsActive(program) == false) {
        final String s =
          String.format(
            "Program '%s' is not active",
            program.programGetName());
        assert s != null;
        throw new JCGLExceptionProgramNotActive(s);
      }
    }
  }

  protected final void checkUniformAndSamplerType(
    final ProgramUniformType pu,
    final boolean check_active)
    throws JCGLException
  {
    NullCheck.notNull(pu, "Program uniform");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramUniform(ctx, pu);
    this.checkProgramAndActive(this.gl, pu.uniformGetProgram(), check_active);
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
    final JCGLType t,
    final boolean check_active)
    throws JCGLException
  {
    NullCheck.notNull(u, "Program uniform");
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkProgramUniform(ctx, u);
    this.checkProgramAndActive(this.gl, u.uniformGetProgram(), check_active);
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

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    this.gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    this.gl.glCompileShader(id);
    final int status =
      this.icache.getShaderInteger(this.gl, id, GL2ES2.GL_COMPILE_STATUS);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);

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
  }

  @Override public final void programAttributeArrayAssociate(
    final ProgramAttributeType pa,
    final ArrayAttributeType aa)
    throws JCGLException
  {
    this.attributeArrayAssociate(pa, aa, true);
  }

  @Override public final void programAttributeArrayDisassociate(
    final ProgramAttributeType pa)
    throws JCGLException
  {
    this.attributeArrayDisassociate(pa, true);
  }

  @Override public final void programAttributePutFloat(
    final ProgramAttributeType pa,
    final float x)
    throws JCGLException
  {
    this.attributePutFloat(pa, x, true);
  }

  @Override public final void programAttributePutVector2f(
    final ProgramAttributeType pa,
    final VectorReadable2FType x)
    throws JCGLException
  {
    this.attributePutVector2f(pa, x, true);
  }

  @Override public final void programAttributePutVector3f(
    final ProgramAttributeType pa,
    final VectorReadable3FType x)
    throws JCGLException
  {
    this.attributePutVector3f(pa, x, true);
  }

  @Override public final void programAttributePutVector4f(
    final ProgramAttributeType pa,
    final VectorReadable4FType x)
    throws JCGLException
  {
    this.attributePutVector4f(pa, x, true);
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

    this.gl.glAttachShader(id, v.getGLName());
    this.gl.glAttachShader(id, f.getGLName());
    this.gl.glLinkProgram(id);

    final int status =
      this.icache.getProgramInteger(this.gl, id, GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetProgramInfoLog(id, 8192, buffer_length, buffer);

      final byte[] raw = new byte[buffer.remaining()];
      buffer.get(raw);
      final String tt = new String(raw);
      throw new JCGLExceptionProgramCompileError(name, tt);
    }

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

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.gl.glGetAttribLocation(id, name);

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

  @Override public JCGLShadersParametersType programGetUncheckedInterface()
  {
    return new Unchecked();
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

      final int type_raw = buffer_type.get(0);
      final JCGLType type = JOGLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte[] temp_buffer = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = this.gl.glGetUniformLocation(id, name);

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
    return active == program.getGLName();
  }

  @Override public final void programUniformPutFloat(
    final ProgramUniformType pu,
    final float x)
    throws JCGLException
  {
    this.uniformPutFloat(pu, x, true);
  }

  @Override public final void programUniformPutInteger(
    final ProgramUniformType pu,
    final int x)
    throws JCGLException
  {
    this.uniformPutInteger(pu, x, true);
  }

  @Override public final void programUniformPutMatrix3x3f(
    final ProgramUniformType pu,
    final MatrixDirectReadable3x3FType matrix)
    throws JCGLException
  {
    this.uniformPutMatrix3x3f(pu, matrix, true);
  }

  @Override public final void programUniformPutMatrix4x4f(
    final ProgramUniformType pu,
    final MatrixDirectReadable4x4FType matrix)
    throws JCGLException
  {
    this.uniformPutMatrix4x4f(pu, matrix, true);
  }

  @Override public final void programUniformPutTextureUnit(
    final ProgramUniformType pu,
    final TextureUnitType unit)
    throws JCGLException
  {
    this.uniformPutTextureUnit(pu, unit, true);
  }

  @Override public final void programUniformPutVector2f(
    final ProgramUniformType pu,
    final VectorReadable2FType v)
    throws JCGLException
  {
    this.uniformPutVector2f(pu, v, true);
  }

  @Override public final void programUniformPutVector2i(
    final ProgramUniformType pu,
    final VectorReadable2IType v)
    throws JCGLException
  {
    this.uniformPutVector2i(pu, v, true);
  }

  @Override public final void programUniformPutVector3f(
    final ProgramUniformType pu,
    final VectorReadable3FType v)
    throws JCGLException
  {
    this.uniformPutVector3f(pu, v, true);
  }

  @Override public final void programUniformPutVector3i(
    final ProgramUniformType pu,
    final VectorReadable3IType v)
    throws JCGLException
  {
    this.uniformPutVector3i(pu, v, true);
  }

  @Override public final void programUniformPutVector4f(
    final ProgramUniformType pu,
    final VectorReadable4FType v)
    throws JCGLException
  {
    this.uniformPutVector4f(pu, v, true);
  }

  @Override public final void programUniformPutVector4i(
    final ProgramUniformType pu,
    final VectorReadable4IType v)
    throws JCGLException
  {
    this.uniformPutVector4i(pu, v, true);
  }

  private void uniformPutFloat(
    final ProgramUniformType pu,
    final float x,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT, check_active);
    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1f(pul, x);
  }

  private void uniformPutInteger(
    final ProgramUniformType pu,
    final int x,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_INTEGER, check_active);
    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1i(pul, x);
  }

  private void uniformPutMatrix3x3f(
    final ProgramUniformType pu,
    final MatrixDirectReadable3x3FType matrix,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_MATRIX_3, check_active);
    NullCheck.notNull(matrix, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniformMatrix3fv(pul, 1, false, matrix.getDirectFloatBuffer());
  }

  private void uniformPutMatrix4x4f(
    final ProgramUniformType pu,
    final MatrixDirectReadable4x4FType matrix,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_MATRIX_4, check_active);
    NullCheck.notNull(matrix, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniformMatrix4fv(pul, 1, false, matrix.getDirectFloatBuffer());
  }

  private void uniformPutTextureUnit(
    final ProgramUniformType pu,
    final TextureUnitType unit,
    final boolean check_active)
  {
    this.checkUniformAndSamplerType(pu, check_active);
    NullCheck.notNull(unit, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform1i(pul, unit.unitGetIndex());
  }

  private void uniformPutVector2f(
    final ProgramUniformType pu,
    final VectorReadable2FType v,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_2, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform2f(pul, v.getXF(), v.getYF());
  }

  private void uniformPutVector2i(
    final ProgramUniformType pu,
    final VectorReadable2IType v,
    final boolean check_active)
  {
    this
      .checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_2, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform2i(pul, v.getXI(), v.getYI());
  }

  private void uniformPutVector3f(
    final ProgramUniformType pu,
    final VectorReadable3FType v,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_3, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform3f(pul, v.getXF(), v.getYF(), v.getZF());
  }

  private void uniformPutVector3i(
    final ProgramUniformType pu,
    final VectorReadable3IType v,
    final boolean check_active)
  {
    this
      .checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_3, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform3i(pul, v.getXI(), v.getYI(), v.getZI());
  }

  private void uniformPutVector4f(
    final ProgramUniformType pu,
    final VectorReadable4FType v,
    final boolean check_active)
  {
    this.checkUniformAndType(pu, JCGLType.TYPE_FLOAT_VECTOR_4, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform4f(pul, v.getXF(), v.getYF(), v.getZF(), v.getWF());
  }

  private void uniformPutVector4i(
    final ProgramUniformType pu,
    final VectorReadable4IType v,
    final boolean check_active)
  {
    this
      .checkUniformAndType(pu, JCGLType.TYPE_INTEGER_VECTOR_4, check_active);
    NullCheck.notNull(v, "Value");

    final int pul = pu.uniformGetLocation();
    this.gl.glUniform4i(pul, v.getXI(), v.getYI(), v.getZI(), v.getWI());
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

    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());
    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    this.gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    this.gl.glCompileShader(id);
    final int status =
      this.icache.getShaderInteger(this.gl, id, GL2ES2.GL_COMPILE_STATUS);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);

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
  }
}
