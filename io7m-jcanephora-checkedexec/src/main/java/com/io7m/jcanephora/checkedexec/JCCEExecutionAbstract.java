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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

  private static <T> boolean isSubsetOf(
    final @Nonnull Set<T> a,
    final @Nonnull Set<T> b)
  {
    if (a.size() <= b.size()) {
      for (final T x : a) {
        if (b.contains(x) == false) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private final @Nonnull HashSet<String>                       assigned;
  private final @Nonnull HashSet<String>                       assigned_ever;
  private final @Nonnull HashMap<String, ArrayBufferAttribute> attribute_bindings;
  private final @CheckForNull Map<String, JCGLType>            declared_attributes;
  private final @CheckForNull Map<String, JCGLType>            declared_uniforms;
  private final @Nonnull StringBuilder                         message;
  private final @Nonnull ArrayList<ProgramAttribute>           missed_attributes;
  private final @Nonnull ArrayList<ProgramUniform>             missed_uniforms;
  private boolean                                              preparing;
  private final @Nonnull ProgramReferenceUsable                program;

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

    this.assigned = new HashSet<String>();
    this.assigned_ever = new HashSet<String>();
    this.preparing = false;
    this.message = new StringBuilder();
    this.missed_attributes = new ArrayList<ProgramAttribute>();
    this.missed_uniforms = new ArrayList<ProgramUniform>();
    this.attribute_bindings = new HashMap<String, ArrayBufferAttribute>();

    this.declared_uniforms = declared_uniforms;
    this.declared_attributes = declared_attributes;

    if (this.declared_uniforms != null) {
      final Map<String, ProgramUniform> program_uniforms =
        this.program.getUniforms();
      final Set<String> program_uniform_names = program_uniforms.keySet();
      final Set<String> declared_uniform_names = declared_uniforms.keySet();

      Constraints.constrainArbitrary(
        JCCEExecutionAbstract.isSubsetOf(
          program_uniform_names,
          declared_uniform_names),
        "Program uniforms are a subset of the declared uniforms");

      for (final String name : program_uniform_names) {
        final ProgramUniform u = program_uniforms.get(name);
        final JCGLType declared = declared_uniforms.get(name);
        if (u.getType() != declared) {
          this.message.setLength(0);
          this.message.append("The declared type of the uniform '");
          this.message.append(name);
          this.message.append("' is ");
          this.message.append(declared);
          this.message
            .append(" but a uniform with the same name exists of type ");
          this.message.append(u.getType());
          throw new ConstraintError(this.message.toString());
        }
      }
    }

    if (this.declared_attributes != null) {
      final Map<String, ProgramAttribute> program_attributes =
        this.program.getAttributes();
      final Set<String> program_attribute_names = program_attributes.keySet();
      final Set<String> declared_attribute_names =
        declared_attributes.keySet();

      Constraints.constrainArbitrary(
        JCCEExecutionAbstract.isSubsetOf(
          program_attribute_names,
          declared_attribute_names),
        "Program attributes are a subset of the declared attributes");

      for (final String name : program_attribute_names) {
        final ProgramAttribute u = program_attributes.get(name);
        final JCGLType declared = declared_attributes.get(name);
        if (u.getType() != declared) {
          this.message.setLength(0);
          this.message.append("The declared type of the attribute '");
          this.message.append(name);
          this.message.append("' is ");
          this.message.append(declared);
          this.message
            .append(" but a attribute with the same name exists of type ");
          this.message.append(u.getType());
          throw new ConstraintError(this.message.toString());
        }
      }
    }
  }

  @Override public final void execAttributeBind(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull ArrayBufferAttribute x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(a, "Attribute name");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    final JCGLType t = x.getDescriptor().getJCGLType();
    final ProgramAttribute pa = this.execCheckAttribute(a, t);
    if (pa == null) {
      return;
    }

    gl.programAttributeArrayAssociate(pa, x);
    this.assigned.add(a);
    this.attribute_bindings.put(a, x);
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

    final ProgramAttribute pa =
      this.execCheckAttribute(a, JCGLType.TYPE_FLOAT);
    if (pa == null) {
      return;
    }

    gl.programAttributePutFloat(pa, x);
    this.assigned.add(a);
    this.execRemoveExistingAttributeBinding(a);
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

    final ProgramAttribute pa =
      this.execCheckAttribute(a, JCGLType.TYPE_FLOAT_VECTOR_2);
    if (pa == null) {
      return;
    }

    gl.programAttributePutVector2f(pa, x);
    this.assigned.add(a);
    this.execRemoveExistingAttributeBinding(a);
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

    final ProgramAttribute pa =
      this.execCheckAttribute(a, JCGLType.TYPE_FLOAT_VECTOR_3);
    if (pa == null) {
      return;
    }

    gl.programAttributePutVector3f(pa, x);
    this.assigned.add(a);
    this.execRemoveExistingAttributeBinding(a);
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

    final ProgramAttribute pa =
      this.execCheckAttribute(a, JCGLType.TYPE_FLOAT_VECTOR_4);
    if (pa == null) {
      return;
    }

    gl.programAttributePutVector4f(pa, x);
    this.assigned.add(a);
    this.execRemoveExistingAttributeBinding(a);
  }

  @Override public void execCancel()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    this.preparing = false;
  }

  private @CheckForNull ProgramAttribute execCheckAttribute(
    final @Nonnull String a,
    final @Nonnull JCGLType type)
    throws ConstraintError
  {
    final ProgramAttribute pa = this.program.getAttributes().get(a);

    /**
     * Perhaps the attribute has been optimized out by the GLSL compiler...
     */

    if (pa == null) {

      /**
       * If a list of declared attributes was provided, and the list contains
       * the given attribute name, then assume it exists and check that it has
       * the correct type.
       */

      if (this.declared_attributes != null) {
        if (this.declared_attributes.containsKey(a)) {
          final JCGLType declared = this.declared_attributes.get(a);
          if (JCCEExecutionAbstract.execCheckTypesCompatible(type, declared) == false) {
            this.message.setLength(0);
            this.message.append("Declared attribute '");
            this.message.append(a);
            this.message.append("' has type ");
            this.message.append(declared);
            this.message.append(" but a value was given of type ");
            this.message.append(type);
            throw new ConstraintError(this.message.toString());
          }
          return null;
        }
      }

      this.execNonexistentAttribute(a);
    }

    return pa;
  }

  private @CheckForNull ProgramUniform execCheckUniform(
    final @Nonnull String u,
    final @Nonnull JCGLType type)
    throws ConstraintError
  {
    final ProgramUniform pu = this.program.getUniforms().get(u);

    if (pu == null) {

      /**
       * Perhaps the uniform has been optimized out by the GLSL compiler...
       * 
       * If a list of declared uniforms was provided, and the list contains
       * the given uniform name, then assume it exists, and check that it has
       * the type given by <code>type</code>.
       */

      if (this.declared_uniforms != null) {
        if (this.declared_uniforms.containsKey(u)) {
          final JCGLType declared = this.declared_uniforms.get(u);
          if (JCCEExecutionAbstract.execCheckTypesCompatible(type, declared) == false) {
            this.message.setLength(0);
            this.message.append("Declared uniform '");
            this.message.append(u);
            this.message.append("' has type ");
            this.message.append(declared);
            this.message.append(" but a value was given of type ");
            this.message.append(type);
            throw new ConstraintError(this.message.toString());
          }
          return null;
        }
      }

      this.execNonexistentUniform(u);
    }
    return pu;
  }

  private @CheckForNull ProgramUniform execCheckUniformUnknownType(
    final @Nonnull String u)
    throws ConstraintError
  {
    final ProgramUniform pu = this.program.getUniforms().get(u);

    if (pu == null) {

      /**
       * Perhaps the uniform has been optimized out by the GLSL compiler...
       * 
       * If a list of declared uniforms was provided, and the list contains
       * the given uniform name, then assume it exists.
       */

      if (this.declared_uniforms != null) {
        if (this.declared_uniforms.containsKey(u)) {
          return null;
        }
      }

      this.execNonexistentUniform(u);
    }
    return pu;
  }

  @Override public @Nonnull ProgramReferenceUsable execGetProgram()
    throws ConstraintError,
      JCGLException
  {
    return this.program;
  }

  private final void execNonexistentAttribute(
    final @Nonnull String a)
    throws ConstraintError
  {
    this.message.setLength(0);
    this.message
      .append("The current program does not contain an attribute named '");
    this.message.append(a);
    this.message.append("', available attributes are:\n");

    for (final ProgramAttribute pa : this.program.getAttributes().values()) {
      this.message.append("  ");
      this.message.append(pa);
      this.message.append("\n");
    }

    if (this.declared_attributes != null) {
      this.message
        .append("The program also has the following declared attributes that may have been optimized out by the GLSL compiler:\n");
      final Set<Entry<String, JCGLType>> entries =
        this.declared_attributes.entrySet();
      final Map<String, ProgramAttribute> program_attributes =
        this.program.getAttributes();

      for (final Entry<String, JCGLType> e : entries) {
        if (program_attributes.containsKey(e.getKey()) == false) {
          this.message.append("  ");
          this.message.append(e.getKey());
          this.message.append(" ");
          this.message.append(e.getValue());
          this.message.append("\n");
        }
      }
    }

    throw new ConstraintError(this.message.toString());
  }

  private final void execNonexistentUniform(
    final String u)
    throws ConstraintError
  {
    this.message.setLength(0);
    this.message.append("The program '");
    this.message.append(this.program);
    this.message.append("' does not contain a uniform named '");
    this.message.append(u);
    this.message.append("', available uniforms are:\n");

    for (final ProgramUniform pu : this.program.getUniforms().values()) {
      this.message.append("  ");
      this.message.append(pu);
      this.message.append("\n");
    }

    if (this.declared_uniforms != null) {
      this.message
        .append("The program also has the following declared uniforms that may have been optimized out by the GLSL compiler:\n");
      final Set<Entry<String, JCGLType>> entries =
        this.declared_uniforms.entrySet();
      final Map<String, ProgramUniform> program_uniforms =
        this.program.getUniforms();

      for (final Entry<String, JCGLType> e : entries) {
        if (program_uniforms.containsKey(e.getKey()) == false) {
          this.message.append("  ");
          this.message.append(e.getKey());
          this.message.append(" ");
          this.message.append(e.getValue());
          this.message.append("\n");
        }
      }
    }

    throw new ConstraintError(this.message.toString());
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
    this.assigned.clear();
    this.missed_attributes.clear();
    this.missed_uniforms.clear();
    this.attribute_bindings.clear();
  }

  private void execRemoveExistingAttributeBinding(
    final String a)
  {
    if (this.attribute_bindings.containsKey(a)) {
      this.attribute_bindings.remove(a);
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
    final Map<String, ProgramAttribute> prog_attributes =
      this.program.getAttributes();

    for (final Entry<String, ProgramAttribute> e : prog_attributes.entrySet()) {
      gl.programAttributeArrayDisassociate(e.getValue());
    }
  }

  private final void execUniformAssign(
    final @Nonnull String u)
  {
    this.assigned_ever.add(u);
    this.assigned.add(u);
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

    final ProgramUniform pu = this.execCheckUniform(u, JCGLType.TYPE_FLOAT);
    if (pu == null) {
      return;
    }

    gl.programUniformPutFloat(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_FLOAT_MATRIX_3);
    if (pu == null) {
      return;
    }

    gl.programUniformPutMatrix3x3f(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_FLOAT_MATRIX_4);
    if (pu == null) {
      return;
    }

    gl.programUniformPutMatrix4x4f(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_SAMPLER_2D);
    if (pu == null) {
      return;
    }

    gl.programUniformPutTextureUnit(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_FLOAT_VECTOR_2);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector2f(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_INTEGER_VECTOR_2);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector2i(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_FLOAT_VECTOR_3);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector3f(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_INTEGER_VECTOR_3);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector3i(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_FLOAT_VECTOR_4);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector4f(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu =
      this.execCheckUniform(u, JCGLType.TYPE_INTEGER_VECTOR_4);
    if (pu == null) {
      return;
    }

    gl.programUniformPutVector4i(pu, x);
    this.execUniformAssign(u);
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

    final ProgramUniform pu = this.execCheckUniformUnknownType(u);
    if (pu == null) {
      return;
    }

    Constraints.constrainArbitrary(
      this.assigned_ever.contains(u),
      "Uniform has been assigned at least once");

    this.execUniformAssign(u);
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
        for (final ProgramUniform u : this.missed_uniforms) {
          this.message.append("  ");
          this.message.append(u);
          this.message.append("\n");
        }
      }
      if (this.missed_attributes.isEmpty() == false) {
        this.message.append("Attributes not assigned values:\n");
        for (final ProgramAttribute a : this.missed_attributes) {
          this.message.append("  ");
          this.message.append(a);
          this.message.append("\n");
        }
      }
      throw new ConstraintError(this.message.toString());
    }
  }

  private final void execValidateAttributes()
  {
    for (final Entry<String, ProgramAttribute> e : this.program
      .getAttributes()
      .entrySet()) {
      if (this.assigned.contains(e.getKey()) == false) {
        this.missed_attributes.add(e.getValue());
      }
    }
  }

  private final void execValidateUniforms()
  {
    for (final Entry<String, ProgramUniform> e : this.program
      .getUniforms()
      .entrySet()) {
      if (this.assigned.contains(e.getKey()) == false) {
        this.missed_uniforms.add(e.getValue());
      }
    }
  }
}
