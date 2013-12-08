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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

public interface JCBProgram
{
  /**
   * <p>
   * Bind the attribute <tt>x</tt> to the program attribute named <tt>a</tt>.
   * After this function has been called successfully, the attribute
   * <tt>a</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programAttributeArrayAssociate(com.io7m.jcanephora.ProgramAttribute, ArrayBufferAttribute)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributeArrayAssociate(com.io7m.jcanephora.ProgramAttribute, ArrayBufferAttribute)}
   *           .
   */

  void programAttributeBind(
    final @Nonnull String a,
    final @Nonnull ArrayBufferAttribute x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programAttributePutFloat(com.io7m.jcanephora.ProgramAttribute, float)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutFloat(com.io7m.jcanephora.ProgramAttribute, float)}
   *           .
   */

  public void programAttributePutFloat(
    final @Nonnull String a,
    float x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programAttributePutVector2f(com.io7m.jcanephora.ProgramAttribute, VectorReadable2F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector2f(com.io7m.jcanephora.ProgramAttribute, VectorReadable2F)}
   *           .
   */

  public void programAttributePutVector2F(
    final @Nonnull String a,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programAttributePutVector3f(com.io7m.jcanephora.ProgramAttribute, VectorReadable3F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector3f(com.io7m.jcanephora.ProgramAttribute, VectorReadable3F)}
   *           .
   */

  public void programAttributePutVector3F(
    final @Nonnull String a,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programAttributePutVector4f(com.io7m.jcanephora.ProgramAttribute, VectorReadable4F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programAttributePutVector4f(com.io7m.jcanephora.ProgramAttribute, VectorReadable4F)}
   *           .
   */

  public void programAttributePutVector4F(
    final @Nonnull String a,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Execute the given procedure, after calling {@link #programValidate()} to
   * check that the current program is correctly configured.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>procedure == null</code></li>
   *           <li>At least one of the programs uniforms have not been
   *           assigned values.</li>
   *           <li>At least one of the programs attributes have not been
   *           assigned values.</li>
   *           <li><code>procedure</code> throws {@link ConstraintError}.</li>
   *           </ul>
   * @throws JCBExecutionException
   *           Iff <code>procedure</code> throws an exception of a type other
   *           than {@link ConstraintError} or {@link JCGLException}.
   * @throws JCGLException
   *           Iff an OpenGL error occurs, or <code>procedure</code> throws
   *           {@link JCGLException}.
   */

  public void programExecute(
    final @Nonnull JCBProgramProcedure procedure)
    throws ConstraintError,
      JCBExecutionException,
      JCGLException;

  /**
   * Retrieve a read-only reference to the actual program.
   */

  public @Nonnull ProgramReferenceUsable programGet();

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutFloat(com.io7m.jcanephora.ProgramUniform, float)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutFloat(com.io7m.jcanephora.ProgramUniform, float)}
   *           .
   */

  public void programUniformPutFloat(
    final @Nonnull String u,
    float x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutInteger(com.io7m.jcanephora.ProgramUniform, int)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutInteger(com.io7m.jcanephora.ProgramUniform, int)}
   *           .
   */

  public void programUniformPutInteger(
    final @Nonnull String u,
    int x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutMatrix3x3f(ProgramUniform, MatrixReadable3x3F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix3x3f(ProgramUniform, MatrixReadable3x3F)}
   *           .
   */

  public void programUniformPutMatrix3x3f(
    final @Nonnull String u,
    final @Nonnull MatrixReadable3x3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutMatrix4x4f(ProgramUniform, MatrixReadable4x4F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutMatrix4x4f(ProgramUniform, MatrixReadable4x4F)}
   *           .
   */

  public void programUniformPutMatrix4x4f(
    final @Nonnull String u,
    final @Nonnull MatrixReadable4x4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutTextureUnit(ProgramUniform, TextureUnit)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutTextureUnit(ProgramUniform, TextureUnit)}
   *           .
   */

  public void programUniformPutTextureUnit(
    final @Nonnull String u,
    final @Nonnull TextureUnit x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector2f(ProgramUniform, com.io7m.jtensors.VectorReadable2F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2f(ProgramUniform, com.io7m.jtensors.VectorReadable2F)}
   *           .
   */

  public void programUniformPutVector2f(
    final @Nonnull String u,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector2i(ProgramUniform, com.io7m.jtensors.VectorReadable2I)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector2i(ProgramUniform, com.io7m.jtensors.VectorReadable2I)}
   *           .
   */

  public void programUniformPutVector2i(
    final @Nonnull String u,
    final @Nonnull VectorReadable2I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector3f(ProgramUniform, com.io7m.jtensors.VectorReadable3F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3f(ProgramUniform, com.io7m.jtensors.VectorReadable3F)}
   *           .
   */

  public void programUniformPutVector3f(
    final @Nonnull String u,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector3i(ProgramUniform, com.io7m.jtensors.VectorReadable3I)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector3i(ProgramUniform, com.io7m.jtensors.VectorReadable3I)}
   *           .
   */

  public void programUniformPutVector3i(
    final @Nonnull String u,
    final @Nonnull VectorReadable3I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector4f(ProgramUniform, com.io7m.jtensors.VectorReadable4F)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4f(ProgramUniform, com.io7m.jtensors.VectorReadable4F)}
   *           .
   */

  public void programUniformPutVector4f(
    final @Nonnull String u,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>x == null</tt></li>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li>Any of the preconditions of
   *           {@link JCGLShadersCommon#programUniformPutVector4i(ProgramUniform, com.io7m.jtensors.VectorReadable4I)}
   *           are not met.</li>
   *           </ul>
   * @throws JCGLException
   *           For the same reasons as
   *           {@link JCGLShadersCommon#programUniformPutVector4i(ProgramUniform, com.io7m.jtensors.VectorReadable4I)}
   *           .
   */

  public void programUniformPutVector4i(
    final @Nonnull String u,
    final @Nonnull VectorReadable4I x)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Assume that the value of <tt>u</tt> has not changed since the last time a
   * value was assigned in the current program. Because all uniforms and
   * attributes <i>must</i> be assigned values prior to each execution, this
   * function allows the caller to save GPU bandwidth and maintain correctness
   * by indicating that he/she has not forgotten to assign a value to the
   * uniform, but does not need to provide a new value.
   * </p>
   * <p>
   * An example use case for this function would be the typical example of a
   * program that renders a series of meshes using projection and modelview
   * matrices: The projection matrix likely does not change for each mesh (and
   * may not change over the entire course of the program), so reusing the
   * same projection matrix value each time saves GPU bandwidth.
   * </p>
   * <p>
   * After this function has been called successfully, the uniform <tt>u</tt>
   * will be assumed to have been assigned, for the purposes of validation
   * with {@link #programValidate()}.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><tt>u</tt> does not exist in the given program</li>
   *           <li><tt>u</tt> has not previously been assigned a value.</li>
   *           <li>The current execution is not in the preparation stage.</li>
   *           </ul>
   */

  public void programUniformUseExisting(
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
   *           Iff any of the following hold:
   *           <ul>
   *           <li>There is an attribute or uniform that has not been assigned
   *           a value.</li>
   *           </ul>
   */

  public void programValidate()
    throws ConstraintError;
}
