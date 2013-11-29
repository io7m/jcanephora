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

package com.io7m.jcanephora.checkedexec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShadersCommon;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReferenceUsable;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

/**
 * <p>
 * An abstract implementation of the {@link JCCEExecutionAPI} interface.
 * </p>
 * <p>
 * Implementations must complete the {@link #execRunActual()} function in
 * order to be usable as concrete {@link JCCEExecutionAPI} implementations.
 * </p>
 * <p>
 * Values of this type cannot be manipulated by multiple threads without
 * explicit synchronization.
 * </p>
 */

@NotThreadSafe public abstract class JCCEExecutionAbstract implements
  JCCEExecutionAPI
{
  private static class AttributeState
  {
    final @CheckForNull ProgramAttribute actual;
    boolean                              assigned = false;
    final @Nonnull String                name;
    final @Nonnull JCGLType              type;

    AttributeState(
      final @Nonnull String name,
      final @Nonnull JCGLType type,
      final @CheckForNull ProgramAttribute actual)
    {
      this.name = name;
      this.type = type;
      this.actual = actual;
    }
  }

  private static class UniformState
  {
    final @CheckForNull ProgramUniform actual;
    boolean                            assigned      = false;
    boolean                            assigned_ever = false;
    final @Nonnull String              name;
    final @Nonnull JCGLType            type;

    UniformState(
      final @Nonnull String name,
      final @Nonnull JCGLType type,
      final @CheckForNull ProgramUniform actual)
    {
      this.name = name;
      this.type = type;
      this.actual = actual;
    }
  }

  private static boolean execCheckTypesCompatible(
    final @Nonnull JCGLType want_type,
    final @Nonnull JCGLType declared_type)
  {
    if (declared_type == want_type) {
      return true;
    }

    switch (declared_type) {
      case TYPE_BOOLEAN:
      case TYPE_BOOLEAN_VECTOR_2:
      case TYPE_BOOLEAN_VECTOR_3:
      case TYPE_BOOLEAN_VECTOR_4:
      case TYPE_FLOAT:
      case TYPE_FLOAT_MATRIX_2:
      case TYPE_FLOAT_MATRIX_3:
      case TYPE_FLOAT_MATRIX_4:
      case TYPE_FLOAT_VECTOR_2:
      case TYPE_FLOAT_VECTOR_3:
      case TYPE_FLOAT_VECTOR_4:
      case TYPE_INTEGER:
      case TYPE_INTEGER_VECTOR_2:
      case TYPE_INTEGER_VECTOR_3:
      case TYPE_INTEGER_VECTOR_4:
      {
        return false;
      }
      case TYPE_SAMPLER_2D:
      case TYPE_SAMPLER_2D_SHADOW:
      case TYPE_SAMPLER_3D:
      case TYPE_SAMPLER_CUBE:
      {
        switch (want_type) {
          case TYPE_BOOLEAN:
          case TYPE_BOOLEAN_VECTOR_2:
          case TYPE_BOOLEAN_VECTOR_3:
          case TYPE_BOOLEAN_VECTOR_4:
          case TYPE_FLOAT:
          case TYPE_FLOAT_MATRIX_2:
          case TYPE_FLOAT_MATRIX_3:
          case TYPE_FLOAT_MATRIX_4:
          case TYPE_FLOAT_VECTOR_2:
          case TYPE_FLOAT_VECTOR_3:
          case TYPE_FLOAT_VECTOR_4:
          case TYPE_INTEGER:
          case TYPE_INTEGER_VECTOR_2:
          case TYPE_INTEGER_VECTOR_3:
          case TYPE_INTEGER_VECTOR_4:
          {
            return false;
          }
          case TYPE_SAMPLER_2D:
          case TYPE_SAMPLER_2D_SHADOW:
          case TYPE_SAMPLER_3D:
          case TYPE_SAMPLER_CUBE:
          {
            return true;
          }
        }
      }
    }

    throw new UnreachableCodeException();
  }

  private final @Nonnull HashMap<String, AttributeState> attributes;
  private final @Nonnull StringBuilder                   message;
  private final @Nonnull ArrayList<AttributeState>       missed_attributes;
  private final @Nonnull ArrayList<UniformState>         missed_uniforms;
  private boolean                                        preparing;
  private final @Nonnull ProgramReferenceUsable          program;
  private final @Nonnull HashMap<String, UniformState>   uniforms;

  protected JCCEExecutionAbstract(
    final @Nonnull ProgramReferenceUsable program,
    final @CheckForNull Map<String, JCGLType> declared_uniforms,
    final @CheckForNull Map<String, JCGLType> declared_attributes)
    throws ConstraintError
  {
    this.program = Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    this.message = new StringBuilder();

    /**
     * Create state for all uniforms. Reject any programs that contain
     * uniforms not in the given list of declarations (if a list is given).
     */

    {
      this.uniforms =
        new HashMap<String, JCCEExecutionAbstract.UniformState>();

      final Map<String, ProgramUniform> program_uniforms =
        program.getUniforms();

      for (final Entry<String, ProgramUniform> e : program_uniforms
        .entrySet()) {
        final String name = e.getKey();
        final ProgramUniform p = e.getValue();

        if (declared_uniforms != null) {
          if (declared_uniforms.containsKey(name) == false) {
            throw this.errorProgramContainsUndeclaredUniform(
              p,
              declared_uniforms);
          }

          final JCGLType du = declared_uniforms.get(name);
          if (JCCEExecutionAbstract.execCheckTypesCompatible(p.getType(), du) == false) {
            throw this.errorProgramContainsIncompatibleUniform(p, du);
          }
        }

        final UniformState state = new UniformState(name, p.getType(), p);
        this.uniforms.put(name, state);
      }

      if (declared_uniforms != null) {
        for (final Entry<String, JCGLType> e : declared_uniforms.entrySet()) {
          final String name = e.getKey();
          final JCGLType t = e.getValue();
          if (this.uniforms.containsKey(name) == false) {
            final UniformState state = new UniformState(name, t, null);
            this.uniforms.put(name, state);
          }
        }
      }
    }

    /**
     * Create state for all attributes. Reject any programs that contain
     * attributes not in the given list of declarations (if a list is given).
     */

    {
      this.attributes =
        new HashMap<String, JCCEExecutionAbstract.AttributeState>();

      final Map<String, ProgramAttribute> program_attributes =
        program.getAttributes();

      for (final Entry<String, ProgramAttribute> e : program_attributes
        .entrySet()) {
        final String name = e.getKey();
        final ProgramAttribute p = e.getValue();

        if (declared_attributes != null) {
          if (declared_attributes.containsKey(name) == false) {
            throw this.errorProgramContainsUndeclaredAttribute(
              p,
              declared_attributes);
          }

          final JCGLType da = declared_attributes.get(name);
          if (JCCEExecutionAbstract.execCheckTypesCompatible(p.getType(), da) == false) {
            throw this.errorProgramContainsIncompatibleAttribute(p, da);
          }
        }

        final AttributeState state = new AttributeState(name, p.getType(), p);
        this.attributes.put(name, state);
      }

      if (declared_attributes != null) {
        for (final Entry<String, JCGLType> e : declared_attributes.entrySet()) {
          final String name = e.getKey();
          final JCGLType t = e.getValue();
          if (this.attributes.containsKey(name) == false) {
            final AttributeState state = new AttributeState(name, t, null);
            this.attributes.put(name, state);
          }
        }
      }
    }

    this.missed_attributes =
      new ArrayList<AttributeState>(this.attributes.size());
    this.missed_uniforms = new ArrayList<UniformState>(this.uniforms.size());
  }

  private @Nonnull ConstraintError errorProgramContainsIncompatibleAttribute(
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

  private @Nonnull ConstraintError errorAttributeNonexistent(
    final @Nonnull String a)
  {
    this.message.setLength(0);
    this.message.append("The program does not contain an attribute '");
    this.message.append(a);
    this.message.append("'\n");
    this.message.append("Attributes include:\n");

    for (final Entry<String, AttributeState> e : this.attributes.entrySet()) {
      this.message.append("  ");
      this.message.append(e.getKey());
      this.message.append(" ");
      this.message.append(e.getValue());
      if (e.getValue().actual == null) {
        this.message.append("(declared, optimized out)");
      }
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
      .append(" but no such parameter exists in the given declared attribyutes.\n");
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

    for (final Entry<String, UniformState> e : this.uniforms.entrySet()) {
      this.message.append("  ");
      this.message.append(e.getKey());
      this.message.append(" ");
      this.message.append(e.getValue());
      if (e.getValue().actual == null) {
        this.message.append("(declared, optimized out)");
      }
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

  @Override public final void execAttributeBind(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull ArrayBufferAttribute x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(x, "Array attribute");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final AttributeState state =
      this.execCheckAttributeAndType(a, x.getDescriptor().getJCGLType());
    if (state.actual != null) {
      gl.programAttributeArrayAssociate(state.actual, x);
    }
    state.assigned = true;
  }

  @Override public final void execAttributePutFloat(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final float x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(a, "Attribute name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final AttributeState state =
      this.execCheckAttributeAndType(a, JCGLType.TYPE_FLOAT);
    if (state.actual != null) {
      gl.programAttributePutFloat(state.actual, x);
    }
    state.assigned = true;
  }

  @Override public final void execAttributePutVector2F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(a, "Attribute name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final AttributeState state =
      this.execCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_2);
    if (state.actual != null) {
      gl.programAttributePutVector2f(state.actual, x);
    }
    state.assigned = true;
  }

  @Override public final void execAttributePutVector3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(a, "Attribute name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final AttributeState state =
      this.execCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_3);
    if (state.actual != null) {
      gl.programAttributePutVector3f(state.actual, x);
    }
    state.assigned = true;
  }

  @Override public final void execAttributePutVector4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(a, "Attribute name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final AttributeState state =
      this.execCheckAttributeAndType(a, JCGLType.TYPE_FLOAT_VECTOR_4);
    if (state.actual != null) {
      gl.programAttributePutVector4f(state.actual, x);
    }
    state.assigned = true;
  }

  @Override public void execCancel()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    this.preparing = false;
  }

  private @CheckForNull AttributeState execCheckAttributeAndType(
    final @Nonnull String a,
    final @Nonnull JCGLType t)
    throws ConstraintError
  {
    Constraints.constrainNotNull(a, "Attribute name");

    final AttributeState state = this.attributes.get(a);
    if (state == null) {
      throw this.errorAttributeNonexistent(a);
    }
    if (JCCEExecutionAbstract.execCheckTypesCompatible(state.type, t) == false) {
      throw this.errorAttributeWrongType(state, t);
    }
    return state;
  }

  private @CheckForNull UniformState execCheckUniform(
    final @Nonnull String u)
    throws ConstraintError
  {
    Constraints.constrainNotNull(u, "Uniform name");

    final UniformState state = this.uniforms.get(u);
    if (state == null) {
      throw this.errorUniformNonexistent(u);
    }
    return state;
  }

  private @CheckForNull UniformState execCheckUniformAndType(
    final @Nonnull String u,
    final @Nonnull JCGLType t)
    throws ConstraintError
  {
    final UniformState state = this.execCheckUniform(u);
    if (JCCEExecutionAbstract.execCheckTypesCompatible(state.type, t) == false) {
      throw this.errorUniformWrongType(state, t);
    }
    return state;
  }

  @Override public @Nonnull ProgramReferenceUsable execGetProgram()
    throws ConstraintError,
      JCGLException
  {
    return this.program;
  }

  @Override public final void execPrepare(
    final @Nonnull JCGLShadersCommon gl)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainArbitrary(
      this.preparing == false,
      "Execution is not already in the preparation stage");

    gl.programActivate(this.program);
    this.preparing = true;

    for (final String name : this.attributes.keySet()) {
      final AttributeState a = this.attributes.get(name);
      a.assigned = false;
    }
    for (final String name : this.uniforms.keySet()) {
      final UniformState u = this.uniforms.get(name);
      u.assigned = false;
    }
  }

  @Override public final void execRun(
    final @Nonnull JCGLShadersCommon gl)
    throws ConstraintError,
      Exception
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    assert this.program != null;
    this.execValidate();
    this.preparing = false;

    try {
      this.execRunActual();
    } finally {
      this.execUnbindArrayAttributes(gl);
      gl.programDeactivate();
    }
  }

  /**
   * <p>
   * A function containing OpenGL rendering instructions, executed with the
   * program given to the constructor of the type as the current program.
   * </p>
   */

  protected abstract void execRunActual()
    throws ConstraintError,
      Exception;

  private void execUnbindArrayAttributes(
    final @Nonnull JCGLShadersCommon gl)
    throws JCGLException,
      ConstraintError
  {
    for (final AttributeState a : this.attributes.values()) {
      if (a.actual != null) {
        gl.programAttributeArrayDisassociate(a.actual);
      }
    }
  }

  @Override public final void execUniformPutFloat(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final float x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT);
    if (state.actual != null) {
      gl.programUniformPutFloat(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutMatrix3x3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull MatrixReadable3x3F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
    if (state.actual != null) {
      gl.programUniformPutMatrix3x3f(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutMatrix4x4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull MatrixReadable4x4F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
    if (state.actual != null) {
      gl.programUniformPutMatrix4x4f(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutTextureUnit(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull TextureUnit x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_SAMPLER_2D);
    if (state.actual != null) {
      gl.programUniformPutTextureUnit(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector2F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
    if (state.actual != null) {
      gl.programUniformPutVector2f(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector2I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable2I x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
    if (state.actual != null) {
      gl.programUniformPutVector2i(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
    if (state.actual != null) {
      gl.programUniformPutVector3f(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector3I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable3I x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
    if (state.actual != null) {
      gl.programUniformPutVector3i(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
    if (state.actual != null) {
      gl.programUniformPutVector4f(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformPutVector4I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable4I x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state =
      this.execCheckUniformAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
    if (state.actual != null) {
      gl.programUniformPutVector4i(state.actual, x);
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execUniformUseExisting(
    final @Nonnull String u)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(u, "Uniform name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final UniformState state = this.execCheckUniform(u);
    if (state.actual != null) {
      Constraints.constrainArbitrary(
        state.assigned_ever,
        "Uniform has been assigned at least once");
    }
    state.assigned = true;
    state.assigned_ever = true;
  }

  @Override public final void execValidate()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    this.execValidateUniforms();
    this.execValidateAttributes();

    final boolean ok =
      (this.missed_uniforms.isEmpty()) && (this.missed_attributes.isEmpty());

    if (!ok) {
      this.message.setLength(0);
      this.message.append("Program validation failed:\n");

      if (this.missed_uniforms.isEmpty() == false) {
        this.message.append("Uniforms not assigned values:\n");
        for (final UniformState u : this.missed_uniforms) {
          this.message.append("  ");
          this.message.append(u.name);
          this.message.append(" ");
          this.message.append(u.type);
          this.message.append("\n");
        }
      }
      if (this.missed_attributes.isEmpty() == false) {
        this.message.append("Attributes not assigned values:\n");
        for (final AttributeState a : this.missed_attributes) {
          this.message.append("  ");
          this.message.append(a.name);
          this.message.append(" ");
          this.message.append(a.type);
          this.message.append("\n");
        }
      }
      throw new ConstraintError(this.message.toString());
    }
  }

  private final void execValidateAttributes()
  {
    for (final String name : this.attributes.keySet()) {
      final AttributeState a = this.attributes.get(name);
      if ((a.assigned == false) && (a.actual != null)) {
        this.missed_attributes.add(a);
      }
    }
  }

  private final void execValidateUniforms()
  {
    for (final String name : this.uniforms.keySet()) {
      final UniformState u = this.uniforms.get(name);
      if ((u.assigned == false) && (u.actual != null)) {
        this.missed_uniforms.add(u);
      }
    }
  }
}
