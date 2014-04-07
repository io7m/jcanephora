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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

public final class JCBExecutor implements JCBExecutionAPI
{
  private static class AttributeState
  {
    final @CheckForNull ProgramAttribute actual;
    boolean                              assigned = false;
    final @Nonnull String                name;
    private final @Nonnull String        string;
    final @Nonnull JCGLType              type;

    AttributeState(
      final @Nonnull String name1,
      final @Nonnull JCGLType type1,
      final @CheckForNull ProgramAttribute actual1)
    {
      this.name = name1;
      this.type = type1;
      this.actual = actual1;
      this.string = this.memoToString();
    }

    private @Nonnull String memoToString()
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
      return m.toString();
    }

    @Override public @Nonnull String toString()
    {
      return this.string;
    }
  }

  private static class Program implements JCBProgram
  {
    private static boolean programTypesCompatible(
      final @Nonnull JCGLType incoming,
      final @Nonnull JCGLType actual)
    {
      if (incoming == actual) {
        return true;
      }
      return incoming.isSamplerType() && actual.isSamplerType();
    }

    private final @Nonnull HashMap<String, Integer>  attribute_names;
    private final @Nonnull ArrayList<AttributeState> attributes;
    private final @Nonnull JCGLShadersCommon         gc;
    private final @Nonnull StringBuilder             message;
    private final @Nonnull ArrayList<AttributeState> missed_attributes;
    private final @Nonnull ArrayList<UniformState>   missed_uniforms;
    private final @Nonnull ProgramReference          program;
    private final @Nonnull HashMap<String, Integer>  uniform_names;

    private final @Nonnull ArrayList<UniformState>   uniforms;

    Program(
      final @Nonnull JCGLShadersCommon gc1,
      final @Nonnull ProgramReference program1,
      final @CheckForNull Map<String, JCGLType> declared_uniforms,
      final @CheckForNull Map<String, JCGLType> declared_attributes,
      final @Nonnull Log log)
      throws ConstraintError
    {
      this.gc = gc1;
      this.program = program1;
      this.message = new StringBuilder();

      /**
       * Create state for all uniforms. Reject any programs that contain
       * uniforms not in the given list of declarations (if a list is given).
       */

      {
        this.uniform_names = new HashMap<String, Integer>();
        this.uniforms = new ArrayList<UniformState>();

        final Map<String, ProgramUniform> program_uniforms =
          program1.getUniforms();

        int current = 0;
        for (final String name : program_uniforms.keySet()) {
          final ProgramUniform p = program_uniforms.get(name);

          if (declared_uniforms != null) {
            if (declared_uniforms.containsKey(name) == false) {
              throw this.errorProgramContainsUndeclaredUniform(
                p,
                declared_uniforms);
            }

            final JCGLType du = declared_uniforms.get(name);
            if (p.getType() != du) {
              throw this.errorProgramContainsIncompatibleUniform(p, du);
            }
          }

          final UniformState state = new UniformState(name, p.getType(), p);
          this.uniform_names.put(name, Integer.valueOf(current));
          this.uniforms.add(state);
          ++current;

          log.debug(state.toString());
        }

        if (declared_uniforms != null) {
          for (final String name : declared_uniforms.keySet()) {
            final JCGLType t = declared_uniforms.get(name);
            if (this.uniform_names.containsKey(name) == false) {
              final UniformState state = new UniformState(name, t, null);
              this.uniform_names.put(name, Integer.valueOf(current));
              this.uniforms.add(state);
              ++current;

              log.debug(state.toString());
            }
          }
        }
      }

      /**
       * Create state for all attributes. Reject any programs that contain
       * attributes not in the given list of declarations (if a list is
       * given).
       */

      {
        this.attributes = new ArrayList<AttributeState>();
        this.attribute_names = new HashMap<String, Integer>();

        final Map<String, ProgramAttribute> program_attributes =
          program1.getAttributes();

        int current = 0;
        for (final String name : program_attributes.keySet()) {
          final ProgramAttribute p = program_attributes.get(name);

          if (declared_attributes != null) {
            if (declared_attributes.containsKey(name) == false) {
              throw this.errorProgramContainsUndeclaredAttribute(
                p,
                declared_attributes);
            }

            final JCGLType da = declared_attributes.get(name);
            if (p.getType() != da) {
              throw this.errorProgramContainsIncompatibleAttribute(p, da);
            }
          }

          final AttributeState state =
            new AttributeState(name, p.getType(), p);
          this.attribute_names.put(name, Integer.valueOf(current));
          this.attributes.add(state);
          ++current;

          log.debug(state.toString());
        }

        if (declared_attributes != null) {
          for (final String name : declared_attributes.keySet()) {
            final JCGLType t = declared_attributes.get(name);
            if (this.attribute_names.containsKey(name) == false) {
              final AttributeState state = new AttributeState(name, t, null);
              this.attribute_names.put(name, Integer.valueOf(current));
              this.attributes.add(state);
              ++current;

              log.debug(state.toString());
            }
          }
        }
      }

      this.missed_attributes =
        new ArrayList<AttributeState>(this.attributes.size());
      this.missed_uniforms =
        new ArrayList<UniformState>(this.uniforms.size());
    }

    private @Nonnull ConstraintError errorAttributeNonexistent(
      final @Nonnull String a)
    {
      this.message.setLength(0);
      this.message.append("The program does not contain an attribute '");
      this.message.append(a);
      this.message.append("'\n");
      this.message.append("Attributes include:\n");

      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState v = this.attributes.get(index);
        this.message.append("  ");
        this.message.append(v);
        this.message.append("\n");
      }

      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorAttributeWrongType(
      final @Nonnull AttributeState state,
      final @Nonnull JCGLType given)
    {
      this.message.setLength(0);
      this.message.append("The attribute '");
      this.message.append(state.name);
      this.message.append("' has type ");
      this.message.append(state.type);
      this.message.append(" but the given array buffer attribute has type ");
      this.message.append(given);
      this.message.append("\n");
      return new ConstraintError(this.message.toString());
    }

    private @Nonnull
      ConstraintError
      errorProgramContainsIncompatibleAttribute(
        final @Nonnull ProgramAttribute actual,
        final @Nonnull JCGLType declared)
    {
      this.message.setLength(0);
      this.message.append("The program contains an attribute '");
      this.message.append(actual.getName());
      this.message.append("' of type ");
      this.message.append(actual.getType());
      this.message
        .append(" but the declared attributes claim it should have type ");
      this.message.append(declared);
      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorProgramContainsIncompatibleUniform(
      final @Nonnull ProgramUniform actual,
      final @Nonnull JCGLType declared)
    {
      this.message.setLength(0);
      this.message.append("The program contains a uniform '");
      this.message.append(actual.getName());
      this.message.append("' of type ");
      this.message.append(actual.getType());
      this.message
        .append(" but the declared uniforms claim it should have type ");
      this.message.append(declared);
      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorProgramContainsUndeclaredAttribute(
      final @Nonnull ProgramAttribute p,
      final @Nonnull Map<String, JCGLType> declared_attributes)
    {
      this.message.setLength(0);
      this.message.append("The program contains an attribute '");
      this.message.append(p.getName());
      this.message.append("' of type ");
      this.message.append(p.getType());
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

      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorProgramContainsUndeclaredUniform(
      final @Nonnull ProgramUniform p,
      final @Nonnull Map<String, JCGLType> declared_uniforms)
    {
      this.message.setLength(0);
      this.message.append("The program contains a uniform parameter '");
      this.message.append(p.getName());
      this.message.append("' of type ");
      this.message.append(p.getType());
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

      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorUniformNonexistent(
      final @Nonnull String u)
    {
      this.message.setLength(0);
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

      return new ConstraintError(this.message.toString());
    }

    private @Nonnull ConstraintError errorUniformWrongType(
      final @Nonnull UniformState state,
      final @Nonnull JCGLType given)
    {
      this.message.setLength(0);
      this.message.append("The uniform '");
      this.message.append(state.name);
      this.message.append("' has type ");
      this.message.append(state.type);
      this.message.append(" but the given value has type ");
      this.message.append(given);
      this.message.append("\n");
      return new ConstraintError(this.message.toString());
    }

    private final void execValidateAttributes()
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        if ((a.assigned == false) && (a.actual != null)) {
          this.missed_attributes.add(a);
        }
      }
    }

    private final void execValidateUniforms()
    {
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        if ((u.assigned == false) && (u.actual != null)) {
          this.missed_uniforms.add(u);
        }
      }
    }

    @Override public final void programAttributeBind(
      final @Nonnull String a,
      final @Nonnull ArrayBufferAttribute x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(x, "Array attribute");

      final AttributeState state =
        this.programCheckAttributeAndType(a, x.getDescriptor().getJCGLType());
      if (state.actual != null) {
        this.gc.programAttributeArrayAssociate(state.actual, x);
      }
      state.assigned = true;
    }

    @Override public final void programAttributePutFloat(
      final @Nonnull String a,
      final float x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT);
      if (state.actual != null) {
        this.gc.programAttributePutFloat(state.actual, x);
      }
      state.assigned = true;
    }

    @Override public final void programAttributePutVector2F(
      final @Nonnull String a,
      final @Nonnull VectorReadable2F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_2);
      if (state.actual != null) {
        this.gc.programAttributePutVector2f(state.actual, x);
      }
      state.assigned = true;
    }

    @Override public final void programAttributePutVector3F(
      final @Nonnull String a,
      final @Nonnull VectorReadable3F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(this.gc, "OpenGL interface");
      Constraints.constrainNotNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_3);
      if (state.actual != null) {
        this.gc.programAttributePutVector3f(state.actual, x);
      }
      state.assigned = true;
    }

    @Override public final void programAttributePutVector4F(
      final @Nonnull String a,
      final @Nonnull VectorReadable4F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(this.gc, "OpenGL interface");
      Constraints.constrainNotNull(a, "Attribute name");

      final AttributeState state =
        this.programCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_4);
      if (state.actual != null) {
        this.gc.programAttributePutVector4f(state.actual, x);
      }
      state.assigned = true;
    }

    private @CheckForNull AttributeState programCheckAttributeAndType(
      final @Nonnull String a,
      final @Nonnull JCGLType t)
      throws ConstraintError
    {
      Constraints.constrainNotNull(a, "Attribute name");

      final Integer index = this.attribute_names.get(a);
      if (index == null) {
        throw this.errorAttributeNonexistent(a);
      }
      final AttributeState state = this.attributes.get(index.intValue());
      if (Program.programTypesCompatible(t, state.type) == false) {
        throw this.errorAttributeWrongType(state, t);
      }
      return state;
    }

    private @CheckForNull UniformState programCheckUniform(
      final @Nonnull String u)
      throws ConstraintError
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final Integer index = this.uniform_names.get(u);
      if (index == null) {
        throw this.errorUniformNonexistent(u);
      }
      return this.uniforms.get(index.intValue());
    }

    private @CheckForNull UniformState programCheckUniformAndType(
      final @Nonnull String u,
      final @Nonnull JCGLType t)
      throws ConstraintError
    {
      final UniformState state = this.programCheckUniform(u);
      if (Program.programTypesCompatible(t, state.type) == false) {
        throw this.errorUniformWrongType(state, t);
      }
      return state;
    }

    private void programClearAll()
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        a.assigned = false;
      }
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        u.assigned = false;
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
        a.assigned = false;
      }
      for (int index = 0; index < this.uniforms.size(); ++index) {
        final UniformState u = this.uniforms.get(index);
        u.assigned = false;
        u.assigned_ever = false;
      }
    }

    @Override public void programExecute(
      final @Nonnull JCBProgramProcedure procedure)
      throws ConstraintError,
        JCBExecutionException,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(procedure, "Procedure");

      this.programValidate();

      try {
        procedure.call();
      } catch (final JCGLRuntimeException x) {
        throw x;
      } catch (final ConstraintError x) {
        throw x;
      } catch (final Throwable x) {
        throw new JCBExecutionException(x);
      } finally {
        this.programClearAll();
      }
    }

    @Override public @Nonnull ProgramReferenceUsable programGet()
    {
      return this.program;
    }

    void programUnbindArrayAttributes(
      final @Nonnull JCGLShadersCommon gl)
      throws JCGLRuntimeException,
        ConstraintError
    {
      for (int index = 0; index < this.attributes.size(); ++index) {
        final AttributeState a = this.attributes.get(index);
        if (a.actual != null) {
          gl.programAttributeArrayDisassociate(a.actual);
        }
      }
    }

    @Override public void programUniformPutFloat(
      final @Nonnull String u,
      final float x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT);
      if (state.actual != null) {
        this.gc.programUniformPutFloat(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutInteger(
      final @Nonnull String u,
      final int x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER);
      if (state.actual != null) {
        this.gc.programUniformPutInteger(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutMatrix3x3f(
      final @Nonnull String u,
      final @Nonnull MatrixReadable3x3F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
      if (state.actual != null) {
        this.gc.programUniformPutMatrix3x3f(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutMatrix4x4f(
      final @Nonnull String u,
      final @Nonnull MatrixReadable4x4F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
      if (state.actual != null) {
        this.gc.programUniformPutMatrix4x4f(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutTextureUnit(
      final @Nonnull String u,
      final @Nonnull TextureUnit x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_SAMPLER_2D);
      if (state.actual != null) {
        this.gc.programUniformPutTextureUnit(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector2f(
      final @Nonnull String u,
      final @Nonnull VectorReadable2F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
      if (state.actual != null) {
        this.gc.programUniformPutVector2f(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector2i(
      final @Nonnull String u,
      final @Nonnull VectorReadable2I x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
      if (state.actual != null) {
        this.gc.programUniformPutVector2i(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector3f(
      final @Nonnull String u,
      final @Nonnull VectorReadable3F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
      if (state.actual != null) {
        this.gc.programUniformPutVector3f(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector3i(
      final @Nonnull String u,
      final @Nonnull VectorReadable3I x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
      if (state.actual != null) {
        this.gc.programUniformPutVector3i(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector4f(
      final @Nonnull String u,
      final @Nonnull VectorReadable4F x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
      if (state.actual != null) {
        this.gc.programUniformPutVector4f(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformPutVector4i(
      final @Nonnull String u,
      final @Nonnull VectorReadable4I x)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state =
        this.programCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
      if (state.actual != null) {
        this.gc.programUniformPutVector4i(state.actual, x);
      }
      state.assigned = true;
      state.assigned_ever = true;
    }

    @Override public void programUniformUseExisting(
      final @Nonnull String u)
      throws ConstraintError,
        JCGLRuntimeException
    {
      Constraints.constrainNotNull(u, "Uniform name");

      final UniformState state = this.programCheckUniform(u);
      if (state.actual != null) {
        Constraints.constrainArbitrary(
          state.assigned_ever,
          "Uniform has been assigned at least once");
      }
      state.assigned = true;
    }

    @Override public final void programValidate()
      throws ConstraintError
    {
      this.execValidateUniforms();
      this.execValidateAttributes();

      final boolean ok =
        (this.missed_uniforms.isEmpty())
          && (this.missed_attributes.isEmpty());

      if (!ok) {
        this.message.setLength(0);
        this.message.append("Program validation failed:\n");

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
        throw new ConstraintError(this.message.toString());
      }
    }
  }

  private static class UniformState
  {
    final @CheckForNull ProgramUniform actual;
    boolean                            assigned      = false;
    boolean                            assigned_ever = false;
    final @Nonnull String              name;
    private final @Nonnull String      string;
    final @Nonnull JCGLType            type;

    UniformState(
      final @Nonnull String name1,
      final @Nonnull JCGLType type1,
      final @CheckForNull ProgramUniform actual1)
    {
      this.name = name1;
      this.type = type1;
      this.actual = actual1;
      this.string = this.memoToString();
    }

    private @Nonnull String memoToString()
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
      return m.toString();
    }

    @Override public @Nonnull String toString()
    {
      return this.string;
    }
  }

  public static @Nonnull JCBExecutionAPI newExecutorWithDeclarations(
    final @Nonnull JCGLShadersCommon gc,
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, JCGLType> declared_uniforms,
    final @Nonnull Map<String, JCGLType> declared_attributes,
    final @Nonnull Log log)
    throws ConstraintError
  {
    Constraints.constrainNotNull(declared_uniforms, "Uniforms");
    Constraints.constrainNotNull(declared_attributes, "Attributes");

    return new JCBExecutor(
      gc,
      program,
      declared_uniforms,
      declared_attributes,
      log);
  }

  public static @Nonnull JCBExecutionAPI newExecutorWithoutDeclarations(
    final @Nonnull JCGLShadersCommon gc,
    final @Nonnull ProgramReference program,
    final @Nonnull Log log)
    throws ConstraintError
  {
    return new JCBExecutor(gc, program, null, null, log);
  }

  private final @Nonnull JCGLShadersCommon gc;
  private final @Nonnull Program           jprogram;
  private final @Nonnull Log               log;
  private final @Nonnull ProgramReference  program;

  private JCBExecutor(
    final @Nonnull JCGLShadersCommon gc1,
    final @Nonnull ProgramReference program1,
    final @CheckForNull Map<String, JCGLType> declared_uniforms,
    final @CheckForNull Map<String, JCGLType> declared_attributes,
    final @Nonnull Log log1)
    throws ConstraintError
  {
    this.gc = Constraints.constrainNotNull(gc1, "JCGL interface");

    this.program = Constraints.constrainNotNull(program1, "Program");
    Constraints.constrainArbitrary(
      program1.resourceIsDeleted() == false,
      "Program not deleted");

    this.log = new Log(Constraints.constrainNotNull(log1, "Log"), "executor");

    this.jprogram =
      new Program(
        gc1,
        program1,
        declared_uniforms,
        declared_attributes,
        this.log);
  }

  @Override public void execRun(
    final @Nonnull JCBExecutorProcedure procedure)
    throws ConstraintError,
      JCGLRuntimeException,
      JCBExecutionException
  {
    Constraints.constrainNotNull(procedure, "Procedure");

    try {
      this.gc.programActivate(this.program);
      procedure.call(this.jprogram);
    } catch (final ConstraintError e) {
      throw e;
    } catch (final JCGLRuntimeException e) {
      throw e;
    } catch (final Throwable e) {
      throw new JCBExecutionException(e);
    } finally {
      this.jprogram.programClearAllFinish();
      this.jprogram.programUnbindArrayAttributes(this.gc);
      this.gc.programDeactivate();
    }
  }
}
