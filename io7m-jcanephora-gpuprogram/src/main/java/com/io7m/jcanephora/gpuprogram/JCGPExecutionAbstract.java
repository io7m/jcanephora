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

package com.io7m.jcanephora.gpuprogram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShaders;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;

/**
 * <p>
 * An abstract implementation of the {@link JCGPExecutionAPI} interface.
 * </p>
 * <p>
 * Implementations must complete the {@link #execRunActual()} function in
 * order to be usable as concrete {@link JCGPExecutionAPI} implementations.
 * </p>
 * <p>
 * Values of this type cannot be manipulated by multiple threads without
 * explicit synchronization.
 * </p>
 */

@NotThreadSafe public abstract class JCGPExecutionAbstract implements
  JCGPExecutionAPI
{
  private @CheckForNull JCGPProgram                  program;
  private final @Nonnull HashSet<String>             assigned;
  private boolean                                    preparing;
  private final @Nonnull StringBuilder               message;
  private final @Nonnull ArrayList<ProgramUniform>   missed_uniforms;
  private final @Nonnull ArrayList<ProgramAttribute> missed_attributes;

  protected JCGPExecutionAbstract()
  {
    this.assigned = new HashSet<String>();
    this.preparing = false;
    this.message = new StringBuilder();
    this.missed_attributes = new ArrayList<ProgramAttribute>();
    this.missed_uniforms = new ArrayList<ProgramUniform>();
  }

  @Override public final void execPrepare(
    final @Nonnull JCGLShaders gl,
    final @Nonnull JCGPProgram new_program)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "Shader interface");
    this.program = Constraints.constrainNotNull(new_program, "Program");
    gl.programActivate(this.program.getProgram());
    this.preparing = true;
    this.assigned.clear();
    this.missed_attributes.clear();
    this.missed_uniforms.clear();
  }

  @Override public final void execRun(
    final @Nonnull JCGLShaders gl)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "Shader interface");
    Constraints.constrainArbitrary(
      this.preparing,
      "Execution has been prepared");

    assert this.program != null;

    this.preparing = false;
    this.execValidate();
    this.execRunActual();
  }

  /**
   * A function containing OpenGL rendering instructions, executed with the
   * program specified with {@link #execPrepare(JCGLShaders, JCGPProgram)} as
   * the current program.
   */

  protected abstract void execRunActual();

  /**
   * Validate the current execution, checking that all uniforms and attributes
   * have been assigned values.
   */

  private final void execValidate()
    throws ConstraintError
  {
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
      }
      if (this.missed_attributes.isEmpty() == false) {
        this.message.append("Attributes not assigned values: ");
        this.message.append(this.missed_attributes.toString());
      }
      throw new ConstraintError(this.message.toString());
    }
  }

  private final void execValidateAttributes()
  {
    final Map<String, ProgramAttribute> attributes =
      this.program.getAttributes();
    for (final Entry<String, ProgramAttribute> e : attributes.entrySet()) {
      if (this.assigned.contains(e.getKey()) == false) {
        this.missed_attributes.add(e.getValue());
      }
    }
  }

  private final void execValidateUniforms()
  {
    final Map<String, ProgramUniform> uniforms = this.program.getUniforms();
    for (final Entry<String, ProgramUniform> e : uniforms.entrySet()) {
      if (this.assigned.contains(e.getKey()) == false) {
        this.missed_uniforms.add(e.getValue());
      }
    }
  }
}
