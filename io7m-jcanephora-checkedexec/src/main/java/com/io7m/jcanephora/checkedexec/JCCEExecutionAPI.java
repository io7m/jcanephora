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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShadersCommon;
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
 * The API supported by program executions.
 * </p>
 */

public interface JCCEExecutionAPI
{
  /**
   * <p>
   * Bind the attribute <tt>x</tt> to the program attribute named <tt>a</tt>.
   * After this function has been called successfully, the attribute
   * <tt>a</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programAttributeArrayBind(com.io7m.jcanephora.ProgramAttribute, ArrayBufferAttribute)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributeArrayBind(com.io7m.jcanephora.ProgramAttribute, ArrayBufferAttribute)}
   *           .
   */

  void execAttributeBind(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull ArrayBufferAttribute x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programAttributePutFloat(com.io7m.jcanephora.ProgramAttribute, float)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutFloat(com.io7m.jcanephora.ProgramAttribute, float)}
   *           .
   */

  public void execAttributePutFloat(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    float x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector2f(com.io7m.jcanephora.ProgramAttribute, VectorReadable2F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector2f(com.io7m.jcanephora.ProgramAttribute, VectorReadable2F)}
   *           .
   */

  public void execAttributePutVector2F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector3f(com.io7m.jcanephora.ProgramAttribute, VectorReadable3F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector3f(com.io7m.jcanephora.ProgramAttribute, VectorReadable3F)}
   *           .
   */

  public void execAttributePutVector3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector4f(com.io7m.jcanephora.ProgramAttribute, VectorReadable4F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector4f(com.io7m.jcanephora.ProgramAttribute, VectorReadable4F)}
   *           .
   */

  public void execAttributePutVector4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String a,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Prepare to start executing the program associated with this execution.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>p == null || gl == null</code>.
   * @throws JCGLException
   *           Iff the program cannot start, or an OpenGL error occurs.
   */

  public void execPrepare(
    final @Nonnull JCGLShadersCommon gl)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Execute an implementation-specific function with the program specified
   * with {@link #execPrepare(JCGLShadersCommon)} as the current program,
   * after calling {@link #execValidate()} to check that the execution is
   * correctly configured.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>gl == null</code></li>
   *           <li>The {@link #execPrepare(JCGLShadersCommon)} method has not
   *           been called.</li>
   *           <li>At least one of the programs uniforms have not been
   *           assigned values.</li>
   *           <li>At least one of the programs attributes have not been
   *           assigned values.</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws Exception
   *           Propagated from the implementation-specific function.
   */

  public void execRun(
    final @Nonnull JCGLShadersCommon gl)
    throws ConstraintError,
      Exception;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutFloat(com.io7m.jcanephora.ProgramUniform, float)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutFloat(com.io7m.jcanephora.ProgramUniform, float)}
   *           .
   */

  public void execUniformPutFloat(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    float x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix3x3f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.MatrixReadable3x3F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix3x3f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.MatrixReadable3x3F)}
   *           .
   */

  public void execUniformPutMatrix3x3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull MatrixReadable3x3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix4x4f(com.io7m.jcanephora.ProgramUniform, MatrixReadable4x4F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix4x4f(com.io7m.jcanephora.ProgramUniform, MatrixReadable4x4F)}
   *           .
   */

  public void execUniformPutMatrix4x4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull MatrixReadable4x4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutTextureUnit(com.io7m.jcanephora.ProgramUniform, TextureUnit)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutTextureUnit(com.io7m.jcanephora.ProgramUniform, TextureUnit)}
   *           .
   */

  public void execUniformPutTextureUnit(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull TextureUnit x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable2F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable2F)}
   *           .
   */

  public void execUniformPutVector2F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable2I)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable2I)}
   *           .
   */

  public void execUniformPutVector2I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable2I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable3F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable3F)}
   *           .
   */

  public void execUniformPutVector3F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable3I)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable3I)}
   *           .
   */

  public void execUniformPutVector3I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable3I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable4F)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4f(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable4F)}
   *           .
   */

  public void execUniformPutVector4F(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or for the
   *           same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable4I)}
   *           .
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4i(com.io7m.jcanephora.ProgramUniform, com.io7m.jtensors.VectorReadable4I)}
   *           .
   */

  public void execUniformPutVector4I(
    final @Nonnull JCGLShadersCommon gl,
    final @Nonnull String u,
    final @Nonnull VectorReadable4I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Assume that the value of <tt>u</tt> has not changed since the last time a
   * value was assigned in the current program. An example use case for this
   * function would be the typical example of a program that renders a series
   * of meshes using projection and modelview matrices: The projection matrix
   * likely does not change for each mesh (and may not change over the entire
   * course of the program), so reusing the same projection matrix value each
   * time saves GPU bandwidth.
   * </p>
   * <p>
   * After this function has been called successfully, the uniform <tt>u</tt>
   * will be assumed to have been assigned, for the purposes of validation
   * with {@link #execValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <tt>u</tt> does not exist in the given program, or if
   *           <tt>u</tt> has not previously been assigned a value.
   */

  public void execUniformUseExisting(
    final @Nonnull String u)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Validate the current execution, checking that all uniforms and attributes
   * have been assigned values.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff there is an attribute or uniform that has not been assigned
   *           a value.
   */

  public void execValidate()
    throws ConstraintError;
}
