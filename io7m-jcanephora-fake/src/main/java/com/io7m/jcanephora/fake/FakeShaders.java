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

package com.io7m.jcanephora.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.JCGLExceptionProgramOutputMappingsError;
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
import com.io7m.jcanephora.api.JCGLShadersGL2Type;
import com.io7m.jcanephora.api.JCGLShadersGL3Type;
import com.io7m.jcanephora.api.JCGLShadersGLES3Type;
import com.io7m.jcanephora.api.JCGLShadersParametersType;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jtensors.MatrixReadable3x3FType;
import com.io7m.jtensors.MatrixReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

final class FakeShaders implements
  JCGLShadersGL2Type,
  JCGLShadersGL3Type,
  JCGLShadersGLES3Type
{
  private static void bindOutputs(
    final Map<String, FramebufferDrawBufferType> outputs,
    final LogType logx,
    final StringBuilder text)
  {
    for (final Entry<String, FramebufferDrawBufferType> e : outputs
      .entrySet()) {
      final String output = e.getKey();
      final FramebufferDrawBufferType buffer = e.getValue();

      if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
        text.setLength(0);
        text.append("bound output '");
        text.append(output);
        text.append("' to draw buffer ");
        text.append(buffer);
        final String r = text.toString();
        assert r != null;
        logx.debug(r);
      }
    }
  }

  /**
   * Check that the type of the given attribute is <code>t</code>.
   */

  static void checkAttributeType(
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

  static void checkFragmentShader(
    final FakeContext context,
    final FragmentShaderType id)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(id, "Fragment shader");
    FakeCompatibilityChecks.checkFragmentShader(context, id);
    ResourceCheck.notDeleted(id);
  }

  private static void checkOutputs(
    final FakeContext ctx,
    final String name,
    final Map<String, FramebufferDrawBufferType> outputs,
    final List<FramebufferDrawBufferType> available_buffers)
    throws JCGLExceptionWrongContext,
      JCGLExceptionProgramOutputMappingsError
  {
    if (outputs.isEmpty()) {
      final String s =
        String.format("Program %s: Draw buffer mappings are empty", name);
      assert s != null;
      throw new JCGLExceptionProgramOutputMappingsError(s);
    }

    if (outputs.size() > available_buffers.size()) {
      @SuppressWarnings("boxing") final String s =
        String
          .format(
            "Program %s: More output mappings (%d) were provided than there are draw buffers available (%d)",
            name,
            outputs.size(),
            available_buffers.size());
      assert s != null;
      throw new JCGLExceptionProgramOutputMappingsError(s);
    }

    for (final String output_name : outputs.keySet()) {
      NullCheck.notNull(output_name, "Output name");
      final FramebufferDrawBufferType dt = outputs.get(output_name);
      FakeDrawBuffers.checkDrawBuffer(ctx, dt);
    }
  }

  /**
   * Check that the given program:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static void checkProgram(
    final FakeContext context,
    final ProgramUsableType program)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(program, "Program");
    FakeCompatibilityChecks.checkProgram(context, program);
    ResourceCheck.notDeleted(program);
  }

  /**
   * Check that the type of the given attribute is a sampler type.
   */

  static void checkUniformSamplerType(
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

  static void checkUniformType(
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

  static void checkVertexShader(
    final FakeContext context,
    final VertexShaderType id)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(id, "Vertex shader");
    FakeCompatibilityChecks.checkVertexShader(context, id);
    ResourceCheck.notDeleted(id);
  }

  private final FakeArrays              arrays;
  private final FakeContext             context;
  private @Nullable FakeProgram         current_program;
  private final FakeDrawBuffers         draw_buffers;
  private final LogType                 log;
  private int                           pool;
  private final FakeShaderControlType   shader_control;
  private final FakeLogMessageCacheType tcache;

  FakeShaders(
    final FakeContext in_context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache,
    final FakeArrays in_arrays,
    final FakeDrawBuffers in_draw_buffers)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.log = NullCheck.notNull(in_log, "Log").with("shaders");
    this.arrays = NullCheck.notNull(in_arrays, "Arrays");
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
    this.draw_buffers = NullCheck.notNull(in_draw_buffers, "Draw buffers");
    this.shader_control =
      NullCheck.notNull(in_shader_control, "Shader control");
    this.pool = 1;
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

  void checkAttribute(
    final ProgramAttributeType a)
    throws JCGLExceptionWrongContext,
      JCGLException
  {
    NullCheck.notNull(a, "Program attribute");
    FakeCompatibilityChecks.checkProgramAttribute(this.context, a);
    this.checkProgramAndActive(a.attributeGetProgram());
  }

  /**
   * Check that the given program attribute satisfies
   * {@link #checkAttribute(ProgramAttributeType)} and is of type
   * <code>t</code>.
   */

  void checkAttributeAndType(
    final ProgramAttributeType a,
    final JCGLType t)
    throws JCGLException
  {
    this.checkAttribute(a);
    FakeShaders.checkAttributeType(a, t);
  }

  /**
   * Check that the given program satisfies
   * {@link #checkProgram(FakeContext, ProgramUsableType)} and is active.
   */

  void checkProgramAndActive(
    final ProgramUsableType program)
    throws JCGLException
  {
    FakeShaders.checkProgram(this.context, program);

    if (this.programIsActive(program) == false) {
      final String s =
        String.format("Program '%s' is not active", program.programGetName());
      assert s != null;
      throw new JCGLExceptionProgramNotActive(s);
    }
  }

  void checkUniformAndSamplerType(
    final ProgramUniformType pu)
    throws JCGLException
  {
    NullCheck.notNull(pu, "Program uniform");
    FakeCompatibilityChecks.checkProgramUniform(this.context, pu);
    this.checkProgramAndActive(pu.uniformGetProgram());
    FakeShaders.checkUniformSamplerType(pu);
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

  void checkUniformAndType(
    final ProgramUniformType u,
    final JCGLType t)
    throws JCGLException
  {
    NullCheck.notNull(u, "Program uniform");
    FakeCompatibilityChecks.checkProgramUniform(this.context, u);
    this.checkProgramAndActive(u.uniformGetProgram());
    FakeShaders.checkUniformType(u, t);
  }

  @Override public FragmentShaderType fragmentShaderCompile(
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

    final int id = this.freshID();
    final FakeFragmentShader f =
      new FakeFragmentShader(this.context, id, name);
    this.shader_control.onFragmentShaderCompile(name, f);
    return f;
  }

  @Override public void fragmentShaderDelete(
    final FragmentShaderType id)
    throws JCGLException
  {
    FakeShaders.checkFragmentShader(this.context, id);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    ((FakeObjectDeletable) id).resourceSetDeleted();
  }

  private int freshID()
  {
    final int id = this.pool;
    ++this.pool;
    return id;
  }

  @Override public void programActivate(
    final ProgramUsableType program)
    throws JCGLException
  {
    FakeShaders.checkProgram(this.context, program);
    this.current_program = (FakeProgram) program;
  }

  @Override public void programAttributeArrayAssociate(
    final ProgramAttributeType pa,
    final ArrayAttributeType aa)
    throws JCGLException
  {
    NullCheck.notNull(aa, "Array attribute");
    this.checkAttribute(pa);

    final ArrayBufferUsableType array = aa.getArray();
    FakeArrays.checkArray(this.context, array);

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
  }

  @Override public void programAttributeArrayDisassociate(
    final ProgramAttributeType pa)
    throws JCGLException
  {
    this.checkAttribute(pa);
  }

  @Override public void programAttributePutFloat(
    final ProgramAttributeType program_attribute,
    final float x)
    throws JCGLException
  {
    this.checkAttributeAndType(program_attribute, JCGLType.TYPE_FLOAT);
  }

  @Override public void programAttributePutVector2f(
    final ProgramAttributeType program_attribute,
    final VectorReadable2FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(
      program_attribute,
      JCGLType.TYPE_FLOAT_VECTOR_2);
    NullCheck.notNull(x, "Value");
  }

  @Override public void programAttributePutVector3f(
    final ProgramAttributeType program_attribute,
    final VectorReadable3FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(
      program_attribute,
      JCGLType.TYPE_FLOAT_VECTOR_3);
    NullCheck.notNull(x, "Value");
  }

  @Override public void programAttributePutVector4f(
    final ProgramAttributeType program_attribute,
    final VectorReadable4FType x)
    throws JCGLException
  {
    this.checkAttributeAndType(
      program_attribute,
      JCGLType.TYPE_FLOAT_VECTOR_4);
    NullCheck.notNull(x, "Value");
  }

  @Override public ProgramType programCreateCommon(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f)
    throws JCGLException,
      JCGLExceptionProgramCompileError
  {
    NullCheck.notNull(name, "Name");
    FakeShaders.checkFragmentShader(this.context, f);
    FakeShaders.checkVertexShader(this.context, v);

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

    final int id = this.freshID();

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

    final FakeProgram program =
      new FakeProgram(this.context, id, name, uniforms, attributes);

    this.shader_control.onProgramCreate(name, program, uniforms, attributes);
    return program;
  }

  @Override public ProgramType programCreateWithOutputs(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f,
    final Map<String, FramebufferDrawBufferType> outputs)
    throws JCGLException,
      JCGLExceptionProgramCompileError
  {
    NullCheck.notNull(name, "Program name");
    NullCheck.notNull(outputs, "Outputs");

    FakeShaders.checkVertexShader(this.context, v);
    FakeShaders.checkFragmentShader(this.context, f);

    final List<FramebufferDrawBufferType> available_buffers =
      this.draw_buffers.getDrawBuffers();

    final StringBuilder text = this.tcache.getTextCache();

    FakeShaders.checkOutputs(this.context, name, outputs, available_buffers);

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

    final int id = this.freshID();

    FakeShaders.bindOutputs(outputs, this.log, text);

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

    final FakeProgram program =
      new FakeProgram(this.context, id, name, uniforms, attributes);

    this.shader_control.onProgramCreate(name, program, uniforms, attributes);
    return program;
  }

  @Override public void programDeactivate()
    throws JCGLException
  {
    this.current_program = null;
  }

  @Override public void programDelete(
    final ProgramType program)
    throws JCGLException
  {
    FakeShaders.checkProgram(this.context, program);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(program);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    ((FakeObjectDeletable) program).resourceSetDeleted();
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLException
  {
    final int max = 16;

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

  @Override public boolean programIsActive(
    final ProgramUsableType program)
    throws JCGLException
  {
    FakeShaders.checkProgram(this.context, program);
    final FakeProgram p = this.current_program;
    if (p == null) {
      return false;
    }
    return program.equals(p);
  }

  @Override public void programUniformPutFloat(
    final ProgramUniformType uniform,
    final float value)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT);
  }

  @Override public void programUniformPutInteger(
    final ProgramUniformType uniform,
    final int value)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_INTEGER);
  }

  @Override public void programUniformPutMatrix3x3f(
    final ProgramUniformType uniform,
    final MatrixReadable3x3FType matrix)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT_MATRIX_3);
    NullCheck.notNull(matrix, "Value");
  }

  @Override public void programUniformPutMatrix4x4f(
    final ProgramUniformType uniform,
    final MatrixReadable4x4FType matrix)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT_MATRIX_4);
    NullCheck.notNull(matrix, "Value");
  }

  @Override public void programUniformPutTextureUnit(
    final ProgramUniformType uniform,
    final TextureUnitType unit)
    throws JCGLException
  {
    this.checkUniformAndSamplerType(uniform);
    NullCheck.notNull(unit, "Value");
  }

  @Override public void programUniformPutVector2f(
    final ProgramUniformType uniform,
    final VectorReadable2FType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT_VECTOR_2);
    NullCheck.notNull(vector, "Value");
  }

  @Override public void programUniformPutVector2i(
    final ProgramUniformType uniform,
    final VectorReadable2IType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_INTEGER_VECTOR_2);
    NullCheck.notNull(vector, "Value");
  }

  @Override public void programUniformPutVector3f(
    final ProgramUniformType uniform,
    final VectorReadable3FType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT_VECTOR_3);
    NullCheck.notNull(vector, "Value");
  }

  @Override public void programUniformPutVector3i(
    final ProgramUniformType uniform,
    final VectorReadable3IType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_INTEGER_VECTOR_3);
    NullCheck.notNull(vector, "Value");
  }

  @Override public void programUniformPutVector4f(
    final ProgramUniformType uniform,
    final VectorReadable4FType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_FLOAT_VECTOR_4);
    NullCheck.notNull(vector, "Value");
  }

  @Override public void programUniformPutVector4i(
    final ProgramUniformType uniform,
    final VectorReadable4IType vector)
    throws JCGLException
  {
    this.checkUniformAndType(uniform, JCGLType.TYPE_INTEGER_VECTOR_4);
    NullCheck.notNull(vector, "Value");
  }

  @Override public VertexShaderType vertexShaderCompile(
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

    final int id = this.freshID();
    final FakeVertexShader v = new FakeVertexShader(this.context, id, name);

    this.shader_control.onVertexShaderCompile(name, v);
    return v;
  }

  @Override public void vertexShaderDelete(
    final VertexShaderType id)
    throws JCGLException
  {
    FakeShaders.checkVertexShader(this.context, id);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    ((FakeObjectDeletable) id).resourceSetDeleted();
  }

  @Override public JCGLShadersParametersType programGetUncheckedInterface()
  {
    return this;
  }
}
