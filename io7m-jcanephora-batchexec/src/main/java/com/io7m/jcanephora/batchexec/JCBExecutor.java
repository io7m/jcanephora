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

package com.io7m.jcanephora.batchexec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramUniformMissing;
import com.io7m.jcanephora.JCGLExceptionProgramValidationError;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.api.JCGLShadersCommonType;
import com.io7m.jcanephora.api.JCGLShadersParametersType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jtensors.MatrixDirectReadable3x3FType;
import com.io7m.jtensors.MatrixDirectReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

/**
 * The main implementation of the {@link JCBExecutorType} type.
 */

public final class JCBExecutor implements JCBExecutorType
{
  private final static class AttributeState
  {
    private final @Nullable ProgramAttributeType actual;
    private boolean                              assigned;
    private final String                         name;
    private final String                         string;
    private final JCGLType                       type;

    AttributeState(
      final String in_name,
      final JCGLType in_type,
      final @Nullable ProgramAttributeType in_attribute)
    {
      this.name = in_name;
      this.type = in_type;
      this.actual = in_attribute;
      this.string = this.memoToString();
    }

    @Nullable ProgramAttributeType getActual()
    {
      return this.actual;
    }

    String getName()
    {
      return this.name;
    }

    JCGLType getType()
    {
      return this.type;
    }

    boolean isAssigned()
    {
      return this.assigned;
    }

    private String memoToString()
    {
      final StringBuilder m = new StringBuilder();
      m.append("[Attribute ");
      m.append(this.name);
      m.append(" : ");
      m.append(this.type);
      if (this.actual == null) {
        m.append(" (declared, optimized out)");
      }
      m.append("]");
      final String r = m.toString();
      assert r != null;
      return r;
    }

    void setAssigned(
      final boolean b)
    {
      this.assigned = b;
    }

    @Override public String toString()
    {
      return this.string;
    }
  }

  private static class Program implements JCBProgramType
  {
    private static boolean programTypesCompatible(
      final JCGLType incoming,
      final JCGLType actual)
    {
      if (incoming == actual) {
        return true;
      }
      return incoming.isSamplerType() && actual.isSamplerType();
    }

    private final Map<String, Integer>      attribute_names;
    private final List<AttributeState>      attributes;
    private final JCGLShadersParametersType gc;
    private final StringBuilder             message;
    private final List<AttributeState>      missed_attributes;
    private final List<UniformState>        missed_uniforms;
    private final ProgramType               program;
    private final Map<String, Integer>      uniform_names;
    private final List<UniformState>        uniforms;

    Program(
      final JCGLShadersParametersType in_gc,
      final ProgramType in_program,
      final @Nullable Map<String, JCGLType> in_declared_uniforms,
      final @Nullable Map<String, JCGLType> in_declared_attributes,
      final LogUsableType in_log)
      throws JCGLExceptionProgramUniformMissing,
        JCGLExceptionTypeError,
        JCGLExceptionAttributeMissing
    {
      this.gc = in_gc;
      this.program = in_program;
      this.message = new StringBuilder();

      this.attributes = new ArrayList<AttributeState>();
      this.attribute_names = new HashMap<String, Integer>();
      this.uniform_names = new HashMap<String, Integer>();
      this.uniforms = new ArrayList<UniformState>();

      this.createUniformStates(in_program, in_declared_uniforms, in_log);
      this.createAttributeStates(in_program, in_declared_attributes, in_log);

      this.missed_attributes =
        new ArrayList<AttributeState>(this.attributes.size());
      this.missed_uniforms =
        new ArrayList<UniformState>(this.uniforms.size());
    }

    /**
     * Create state for all attributes. Reject any programs that contain
     * attributes not in the given list of declarations (if a list is given).
     */

    private void createAttributeStates(
      final ProgramType in_program,
      final @Nullable Map<String, JCGLType> in_declared_attributes,
      final LogUsableType in_log)
      throws JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
    {
      final Map<String, ProgramAttributeType> program_attributes =
        in_program.programGetAttributes();

      int current = 0;
      for (final String name : program_attributes.keySet()) {
        assert name != null;
        final ProgramAttributeType p = program_attributes.get(name);
        assert p != null;

        if (in_declared_attributes != null) {
          if (in_declared_attributes.containsKey(name) == false) {
            throw this.errorProgramContainsUndeclaredAttribute(
              p,
              in_declared_attributes);
          }

          final JCGLType da = in_declared_attributes.get(name);
          assert da != null;
          if (p.attributeGetType() != da) {
            throw this.errorProgramContainsIncompatibleAttribute(p, da);
          }
        }

        final AttributeState state =
          new AttributeState(name, p.attributeGetType(), p);
        this.attribute_names.put(name, Integer.valueOf(current));
        this.attributes.add(state);
        ++current;

        in_log.debug(state.toString());
      }

      if (in_declared_attributes != null) {
        for (final String name : in_declared_attributes.keySet()) {
          assert name != null;
          final JCGLType t = in_declared_attributes.get(name);
          assert t != null;

          if (this.attribute_names.containsKey(name) == false) {
            final AttributeState state = new AttributeState(name, t, null);
            this.attribute_names.put(name, Integer.valueOf(current));
            this.attributes.add(state);
            ++current;

            in_log.debug(state.toString());
          }
        }
      }
    }

    /**
     * Create state for all uniforms. Reject any programs that contain
     * uniforms not in the given list of declarations (if a list is given).
     */

    private void createUniformStates(
      final ProgramType in_program,
      final @Nullable Map<String, JCGLType> in_declared_uniforms,
      final LogUsableType in_log)
      throws JCGLExceptionProgramUniformMissing,
        JCGLExceptionTypeError
    {
      final Map<String, ProgramUniformType> program_uniforms =
        in_program.programGetUniforms();

      int current = 0;
      for (final String name : program_uniforms.keySet()) {
        assert name != null;
        final ProgramUniformType p = program_uniforms.get(name);
        assert p != null;

        if (in_declared_uniforms != null) {
          if (in_declared_uniforms.containsKey(name) == false) {
            throw this.errorProgramContainsUndeclaredUniform(
              p,
              in_declared_uniforms);
          }

          final JCGLType du = in_declared_uniforms.get(name);
          assert du != null;

          if (p.uniformGetType() != du) {
            throw this.errorProgramContainsIncompatibleUniform(p, du);
          }
        }

        final UniformState state =
          new UniformState(name, p.uniformGetType(), p);
        this.uniform_names.put(name, Integer.valueOf(current));
        this.uniforms.add(state);
        ++current;

        in_log.debug(state.toString());
      }

      if (in_declared_uniforms != null) {
        for (final String name : in_declared_uniforms.keySet()) {
          assert name != null;
          final JCGLType t = in_declared_uniforms.get(name);
          assert t != null;

          if (this.uniform_names.containsKey(name) == false) {
            final UniformState state = new UniformState(name, t, null);
            this.uniform_names.put(name, Integer.valueOf(current));
            this.uniforms.add(state);
            ++current;

            in_log.debug(state.toString());
          }
        }
      }

    }

    private JCGLExceptionAttributeMissing errorAttributeNonexistent(
      final String a)
    {
      this.message.setLength(0);
      this.message.append("The program '");
      this.message.append(this.program.programGetName());
      this.message.append("' does not contain an attribute '");
      this.message.append(a);
      this.message.append("'\n");
      this.message.append("Attributes include:\n");

      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState v = this.attributes.get(index);
        this.message.append("  ");
        this.message.append(v);
        this.message.append("\n");
      }

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionAttributeMissing(r);
    }

    private JCGLExceptionTypeError errorAttributeWrongType(
      final AttributeState state,
      final JCGLType given)
    {
      this.message.setLength(0);
      this.message
        .append("Wrong type of value given for attribute in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The attribute '");
      this.message.append(state.getName());
      this.message.append("' has type ");
      this.message.append(state.getType());
      this.message.append(" but the given array buffer attribute has type ");
      this.message.append(given);
      this.message.append("\n");

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionTypeError(r);
    }

    private JCGLExceptionTypeError errorProgramContainsIncompatibleAttribute(
      final ProgramAttributeType actual,
      final JCGLType declared)
    {
      this.message.setLength(0);
      this.message.append("Incompatible type for attribute in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The program contains an attribute '");
      this.message.append(actual.attributeGetName());
      this.message.append("' of type ");
      this.message.append(actual.attributeGetType());
      this.message
        .append(" but the declared attributes claim it should have type ");
      this.message.append(declared);

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionTypeError(r);
    }

    private JCGLExceptionTypeError errorProgramContainsIncompatibleUniform(
      final ProgramUniformType actual,
      final JCGLType declared)
    {
      this.message.setLength(0);
      this.message.append("Incompatible type for uniform in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The program contains a uniform '");
      this.message.append(actual.uniformGetName());
      this.message.append("' of type ");
      this.message.append(actual.uniformGetType());
      this.message
        .append(" but the declared uniforms claim it should have type ");
      this.message.append(declared);

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionTypeError(r);
    }

    private
      JCGLExceptionAttributeMissing
      errorProgramContainsUndeclaredAttribute(
        final ProgramAttributeType p,
        final Map<String, JCGLType> declared_attributes)
    {
      this.message.setLength(0);
      this.message.append("Undeclared attribute in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The program contains an attribute '");
      this.message.append(p.attributeGetName());
      this.message.append("' of type ");
      this.message.append(p.attributeGetType());
      this.message
        .append(" but no such parameter exists in the given declared attributes.\n");
      this.message.append("Declared attributes include:\n");

      for (final Entry<String, JCGLType> e : declared_attributes.entrySet()) {
        this.message.append("  ");
        this.message.append(e.getKey());
        this.message.append(" ");
        this.message.append(e.getValue());
        this.message.append("\n");
      }

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionAttributeMissing(r);
    }

    private
      JCGLExceptionProgramUniformMissing
      errorProgramContainsUndeclaredUniform(
        final ProgramUniformType p,
        final Map<String, JCGLType> declared_uniforms)
    {
      this.message.setLength(0);
      this.message.append("Undeclared uniform in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The program contains a uniform parameter '");
      this.message.append(p.uniformGetName());
      this.message.append("' of type ");
      this.message.append(p.uniformGetType());
      this.message
        .append(" but no such parameter exists in the given declared uniforms.\n");
      this.message.append("Declared uniforms include:\n");

      for (final Entry<String, JCGLType> e : declared_uniforms.entrySet()) {
        this.message.append("  ");
        this.message.append(e.getKey());
        this.message.append(" ");
        this.message.append(e.getValue());
        this.message.append("\n");
      }

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionProgramUniformMissing(r);
    }

    private JCGLExceptionProgramUniformMissing errorUniformNonexistent(
      final String u)
    {
      this.message.setLength(0);
      this.message.append("Noexistent uniform in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The program does not contain a uniform '");
      this.message.append(u);
      this.message.append("'\n");
      this.message.append("Uniforms include:\n");

      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState v = this.uniforms.get(index);
        this.message.append("  ");
        this.message.append(v);
        this.message.append("\n");
      }

      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionProgramUniformMissing(r);
    }

    private JCGLExceptionTypeError errorUniformWrongType(
      final UniformState state,
      final JCGLType given)
    {
      this.message.setLength(0);
      this.message
        .append("Incorrect type of value given for uniform in program '");
      this.message.append(this.program.programGetName());
      this.message.append("'\n");
      this.message.append("The uniform '");
      this.message.append(state.getName());
      this.message.append("' has type ");
      this.message.append(state.getType());
      this.message.append(" but the given value has type ");
      this.message.append(given);
      this.message.append("\n");
      final String r = this.message.toString();
      assert r != null;
      return new JCGLExceptionTypeError(r);
    }

    private void execValidateAttributes()
    {
      this.missed_attributes.clear();
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        if (a.isAssigned() == false) {
          this.missed_attributes.add(a);
        }
      }
    }

    private void execValidateUniforms()
    {
      this.missed_uniforms.clear();
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        if (u.isAssigned() == false) {
          this.missed_uniforms.add(u);
        }
      }
    }

    @Override public final void programAttributeBind(
      final String a,
      final ArrayAttributeType x)
      throws JCGLException
    {
      NullCheck.notNull(x, "Array attribute");

      final AttributeState state =
        this.programCheckAttributeAndType(a, x.getDescriptor().getJCGLType());

      final ProgramAttributeType r = state.getActual();
      if (r != null) {
        this.gc.programAttributeArrayAssociate(r, x);
      }
      state.setAssigned(true);
    }

    @Override public final void programAttributePutFloat(
      final String a,
      final float x)
      throws JCGLException
    {
      NullCheck.notNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT);

      final ProgramAttributeType r = state.getActual();
      if (r != null) {
        this.gc.programAttributePutFloat(r, x);
      }
      state.setAssigned(true);
    }

    @Override public final void programAttributePutVector2F(
      final String a,
      final VectorReadable2FType x)
      throws JCGLException
    {
      NullCheck.notNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_2);

      final ProgramAttributeType r = state.getActual();
      if (r != null) {
        this.gc.programAttributePutVector2f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public final void programAttributePutVector3F(
      final String a,
      final VectorReadable3FType x)
      throws JCGLException
    {
      NullCheck.notNull(this.gc, "OpenGL interface");
      NullCheck.notNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_3);

      final ProgramAttributeType r = state.getActual();
      if (r != null) {
        this.gc.programAttributePutVector3f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public final void programAttributePutVector4F(
      final String a,
      final VectorReadable4FType x)
      throws JCGLException
    {
      NullCheck.notNull(this.gc, "OpenGL interface");
      NullCheck.notNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_4);

      final ProgramAttributeType r = state.getActual();
      if (r != null) {
        this.gc.programAttributePutVector4f(r, x);
      }
      state.setAssigned(true);
    }

    private AttributeState programCheckAttributeAndType(
      final String a,
      final JCGLType t)
      throws JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
    {
      NullCheck.notNull(a, "Attribute name");

      final Integer index = this.attribute_names.get(a);
      if (index == null) {
        throw this.errorAttributeNonexistent(a);
      }
      final AttributeState state = this.attributes.get(index.intValue());
      if (Program.programTypesCompatible(t, state.getType()) == false) {
        throw this.errorAttributeWrongType(state, t);
      }
      return state;
    }

    private UniformState programCheckUniform(
      final String u)
      throws JCGLExceptionProgramUniformMissing
    {
      NullCheck.notNull(u, "Uniform name");

      final Integer index = this.uniform_names.get(u);
      if (index == null) {
        throw this.errorUniformNonexistent(u);
      }
      final UniformState s = this.uniforms.get(index.intValue());
      assert s != null;
      return s;
    }

    private UniformState programCheckUniformAndType(
      final String u,
      final JCGLType t)
      throws JCGLExceptionProgramUniformMissing,
        JCGLExceptionTypeError
    {
      final UniformState state = this.programCheckUniform(u);
      if (Program.programTypesCompatible(t, state.getType()) == false) {
        throw this.errorUniformWrongType(state, t);
      }
      return state;
    }

    private void programClearAll()
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        a.setAssigned(false);
      }
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        u.setAssigned(false);
      }
    }

    /**
     * Reset all attributes and uniforms to their unassigned state, and
     * pretend that values have never been assigned.
     */

    void programClearAllFinish()
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        a.setAssigned(false);
      }
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        u.setAssigned(false);
        u.setAssignedEver(false);
      }
    }

    @Override public <E extends Throwable> void programExecute(
      final JCBProgramProcedureType<E> procedure)
      throws JCGLException
    {
      NullCheck.notNull(procedure, "Procedure");

      this.programValidate();

      try {
        procedure.call();
      } catch (final JCGLException x) {
        throw x;
      } catch (final Throwable x) {
        throw new JCGLExceptionExecution(x);
      } finally {
        this.programClearAll();
      }
    }

    @Override public ProgramUsableType programGet()
    {
      return this.program;
    }

    void programUnbindArrayAttributes()
      throws JCGLException
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        final ProgramAttributeType r = a.getActual();
        if (r != null) {
          this.gc.programAttributeArrayDisassociate(r);
        }
      }
    }

    @Override public void programUniformPutFloat(
      final String u,
      final float x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutFloat(r, x);
      }

      state.setAssigned(true);
    }

    @Override public void programUniformPutInteger(
      final String u,
      final int x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutInteger(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutMatrix3x3f(
      final String u,
      final MatrixDirectReadable3x3FType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutMatrix3x3f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutMatrix4x4f(
      final String u,
      final MatrixDirectReadable4x4FType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutMatrix4x4f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutTextureUnit(
      final String u,
      final TextureUnitType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_SAMPLER_2D);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutTextureUnit(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector2f(
      final String u,
      final VectorReadable2FType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector2f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector2i(
      final String u,
      final VectorReadable2IType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector2i(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector3f(
      final String u,
      final VectorReadable3FType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector3f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector3i(
      final String u,
      final VectorReadable3IType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector3i(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector4f(
      final String u,
      final VectorReadable4FType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector4f(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformPutVector4i(
      final String u,
      final VectorReadable4IType x)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
      final ProgramUniformType r = state.getActual();
      if (r != null) {
        this.gc.programUniformPutVector4i(r, x);
      }
      state.setAssigned(true);
    }

    @Override public void programUniformUseExisting(
      final String u)
      throws JCGLException
    {
      NullCheck.notNull(u, "Uniform name");

      final UniformState state = this.programCheckUniform(u);
      if (state.getActual() != null) {
        if (state.isAssignedEver() == false) {
          final String s =
            String.format("Uniform '%s' has never been assigned a value", u);
          assert s != null;
          throw new JCGLExceptionProgramValidationError(s);
        }
      }

      state.setAssigned(true);
    }

    @Override public final void programValidate()
      throws JCGLExceptionProgramValidationError
    {
      this.execValidateUniforms();
      this.execValidateAttributes();

      final boolean ok =
        (this.missed_uniforms.isEmpty())
          && (this.missed_attributes.isEmpty());

      if (!ok) {
        this.message.setLength(0);
        this.message.append("Validation of execution of program '");
        this.message.append(this.program.programGetName());
        this.message.append("' failed:\n");

        if (this.missed_uniforms.isEmpty() == false) {
          this.message.append("Uniforms not assigned values:\n");
          for (final UniformState u : this.missed_uniforms) {
            this.message.append("  ");
            this.message.append(u);
            this.message.append("\n");
          }
        }
        if (this.missed_attributes.isEmpty() == false) {
          this.message.append("Attributes not assigned values:\n");
          for (final AttributeState a : this.missed_attributes) {
            this.message.append("  ");
            this.message.append(a);
            this.message.append("\n");
          }
        }

        final String r = this.message.toString();
        assert r != null;
        throw new JCGLExceptionProgramValidationError(r);
      }
    }
  }

  private static final class UniformState
  {
    private final @Nullable ProgramUniformType actual;
    private boolean                            assigned;
    private boolean                            assigned_ever;
    private final String                       name;
    private final String                       string;
    private final JCGLType                     type;

    UniformState(
      final String in_name,
      final JCGLType in_type,
      final @Nullable ProgramUniformType in_actual)
    {
      this.name = in_name;
      this.type = in_type;
      this.actual = in_actual;
      this.string = this.memoToString();
    }

    @Nullable ProgramUniformType getActual()
    {
      return this.actual;
    }

    String getName()
    {
      return this.name;
    }

    JCGLType getType()
    {
      return this.type;
    }

    boolean isAssigned()
    {
      return this.assigned;
    }

    boolean isAssignedEver()
    {
      return this.assigned_ever;
    }

    private String memoToString()
    {
      final StringBuilder m = new StringBuilder();
      m.append("[Uniform ");
      m.append(this.name);
      m.append(" : ");
      m.append(this.type);
      if (this.actual == null) {
        m.append(" (declared, optimized out)");
      }
      m.append("]");
      final String r = m.toString();
      assert r != null;
      return r;
    }

    void setAssigned(
      final boolean e)
    {
      this.assigned = e;
      this.assigned_ever = this.assigned_ever || e;
    }

    void setAssignedEver(
      final boolean b)
    {
      this.assigned_ever = b;
    }

    @Override public String toString()
    {
      return this.string;
    }
  }

  /**
   * Construct a new executor for the given program. The program is assumed to
   * have the given uniform and attribute declarations (and these will be
   * checked against the program).
   *
   * @param gc
   *          A shader interface.
   * @param program
   *          The program.
   * @param declared_uniforms
   *          The program's declared uniforms.
   * @param declared_attributes
   *          The program's declared attributes.
   * @param log
   *          A log interface.
   * @return A new executor.
   * @throws JCGLExceptionDeleted
   *           If the program has been deleted.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the program's declared uniforms do not match the actuals.
   * @throws JCGLExceptionTypeError
   *           If a declared uniform or attribute has the wrong type.
   * @throws JCGLExceptionAttributeMissing
   *           If the program's declared attributes do not match the actuals.
   */

  public static JCBExecutorType newExecutorWithDeclarations(
    final JCGLShadersCommonType gc,
    final ProgramType program,
    final Map<String, JCGLType> declared_uniforms,
    final Map<String, JCGLType> declared_attributes,
    final LogUsableType log)
    throws JCGLExceptionDeleted,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing
  {
    NullCheck.notNull(declared_uniforms, "Uniforms");
    NullCheck.notNull(declared_attributes, "Attributes");

    return new JCBExecutor(
      gc,
      program,
      declared_uniforms,
      declared_attributes,
      log);
  }

  /**
   * Construct a new executor for the given program.
   *
   * @param gc
   *          A shader interface.
   * @param program
   *          The program.
   * @param log
   *          A log interface.
   * @return A new executor.
   * @throws JCGLExceptionDeleted
   *           If the program has been deleted.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the program's declared uniforms do not match the actuals.
   * @throws JCGLExceptionTypeError
   *           If a declared uniform or attribute has the wrong type.
   * @throws JCGLExceptionAttributeMissing
   *           If the program's declared attributes do not match the actuals.
   */

  public static JCBExecutorType newExecutorWithoutDeclarations(
    final JCGLShadersCommonType gc,
    final ProgramType program,
    final LogUsableType log)
    throws JCGLExceptionDeleted,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing
  {
    return new JCBExecutor(gc, program, null, null, log);
  }

  private final JCGLShadersCommonType gc;
  private final Program               jprogram;
  private final LogUsableType         log;
  private final ProgramType           program;

  private JCBExecutor(
    final JCGLShadersCommonType in_gc,
    final ProgramType in_program,
    final @Nullable Map<String, JCGLType> declared_uniforms,
    final @Nullable Map<String, JCGLType> declared_attributes,
    final LogUsableType in_log)
    throws JCGLExceptionDeleted,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing
  {
    this.gc = NullCheck.notNull(in_gc, "JCGL interface");
    this.program = NullCheck.notNull(in_program, "Program");
    ResourceCheck.notDeleted(in_program);
    this.log = NullCheck.notNull(in_log, "Log").with("executor");
    this.jprogram =
      new Program(
        in_gc.programGetUncheckedInterface(),
        in_program,
        declared_uniforms,
        declared_attributes,
        this.log);
  }

  @Override public <E extends Throwable> void execRun(
    final JCBExecutorProcedureType<E> procedure)
    throws JCGLException
  {
    NullCheck.notNull(procedure, "Procedure");

    try {
      this.gc.programActivate(this.program);
      procedure.call(this.jprogram);
    } catch (final JCGLException e) {
      throw e;
    } catch (final Throwable e) {
      throw new JCGLExceptionExecution(e);
    } finally {
      this.jprogram.programClearAllFinish();
      this.jprogram.programUnbindArrayAttributes();
      this.gc.programDeactivate();
    }
  }
}
