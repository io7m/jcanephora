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
import java.util.HashSet;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShadersCommon;
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
  private final @Nonnull ProgramReferenceUsable      program;
  private final @Nonnull HashSet<String>             assigned;
  private final @Nonnull HashSet<String>             assigned_ever;
  private boolean                                    preparing;
  private final @Nonnull StringBuilder               message;
  private final @Nonnull ArrayList<ProgramUniform>   missed_uniforms;
  private final @Nonnull ArrayList<ProgramAttribute> missed_attributes;

  @Override public void execCancel()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution is in the preparation stage");

    this.preparing = false;
  }

  protected JCCEExecutionAbstract(
    final @Nonnull ProgramReferenceUsable program)
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

    final ProgramAttribute pa = this.program.getAttributes().get(a);
    if (pa == null) {
      this.execNonexistentAttribute(a);
    }

    gl.programAttributeArrayBind(pa, x);
    this.assigned.add(a);
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

    final ProgramAttribute pa = this.program.getAttributes().get(a);
    if (pa == null) {
      this.execNonexistentAttribute(a);
    }

    gl.programAttributePutFloat(pa, x);
    this.assigned.add(a);
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

    final ProgramAttribute pa = this.program.getAttributes().get(a);
    if (pa == null) {
      this.execNonexistentAttribute(a);
    }

    gl.programAttributePutVector2f(pa, x);
    this.assigned.add(a);
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

    final ProgramAttribute pa = this.program.getAttributes().get(a);
    if (pa == null) {
      this.execNonexistentAttribute(a);
    }

    gl.programAttributePutVector3f(pa, x);
    this.assigned.add(a);
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

    final ProgramAttribute pa = this.program.getAttributes().get(a);
    if (pa == null) {
      this.execNonexistentAttribute(a);
    }

    gl.programAttributePutVector4f(pa, x);
    this.assigned.add(a);
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

    throw new ConstraintError(this.message.toString());
  }

  private final void execNonexistentUniform(
    final String u)
    throws ConstraintError
  {
    this.message.setLength(0);
    this.message
      .append("The current program does not contain a uniform named '");
    this.message.append(u);
    this.message.append("', available uniforms are:\n");

    for (final ProgramUniform pu : this.program.getUniforms().values()) {
      this.message.append("  ");
      this.message.append(pu);
      this.message.append("\n");
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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

    final ProgramUniform pu = this.program.getUniforms().get(u);
    if (pu == null) {
      this.execNonexistentUniform(u);
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
      this.message.append("Program validation failed:");
      this.message.append("\n");

      if (this.missed_uniforms.isEmpty() == false) {
        this.message.append("Uniforms not assigned values: ");
        this.message.append(this.missed_uniforms.toString());
        this.message.append("\n");
      }
      if (this.missed_attributes.isEmpty() == false) {
        this.message.append("Attributes not assigned values: ");
        this.message.append(this.missed_attributes.toString());
        this.message.append("\n");
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
