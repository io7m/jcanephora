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

import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.JCGLExceptionProgramUniformMissing;
import com.io7m.jcanephora.JCGLExceptionProgramValidationError;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jtensors.MatrixDirectReadable3x3FType;
import com.io7m.jtensors.MatrixDirectReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

/**
 * The type of batch executable programs.
 */

public interface JCBProgramType
{
  /**
   * <p>
   * Bind the attribute <tt>x</tt> to the program attribute named <tt>a</tt>.
   * After this function has been called successfully, the attribute
   * <tt>a</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the attribute types do not match.
   * @throws JCGLExceptionAttributeMissing
   *           If the attribute does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   *
   * @param a
   *          The name of the program attribute.
   * @param x
   *          The array attribute.
   */

  void programAttributeBind(
    final String a,
    final ArrayAttributeType x)
    throws JCGLException,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the attribute types do not match.
   * @throws JCGLExceptionAttributeMissing
   *           If the attribute does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   *
   * @param a
   *          The name of the program attribute.
   * @param x
   *          The value.
   */

  void programAttributePutFloat(
    final String a,
    float x)
    throws JCGLException,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the attribute types do not match.
   * @throws JCGLExceptionAttributeMissing
   *           If the attribute does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   *
   * @param a
   *          The name of the program attribute.
   * @param x
   *          The value.
   */

  void programAttributePutVector2F(
    final String a,
    final VectorReadable2FType x)
    throws JCGLException,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the attribute types do not match.
   * @throws JCGLExceptionAttributeMissing
   *           If the attribute does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   *
   * @param a
   *          The name of the program attribute.
   * @param x
   *          The value.
   */

  void programAttributePutVector3F(
    final String a,
    final VectorReadable3FType x)
    throws JCGLException,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing;

  /**
   * <p>
   * Set the value of the attribute named <tt>a</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the
   * attribute <tt>a</tt> will be assumed to have been assigned, for the
   * purposes of validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the attribute types do not match.
   * @throws JCGLExceptionAttributeMissing
   *           If the attribute does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   *
   * @param a
   *          The name of the program attribute.
   * @param x
   *          The value.
   */

  void programAttributePutVector4F(
    final String a,
    final VectorReadable4FType x)
    throws JCGLException,
      JCGLExceptionTypeError,
      JCGLExceptionAttributeMissing;

  /**
   * <p>
   * Execute the given procedure, after calling {@link #programValidate()} to
   * check that the current program is correctly configured.
   * </p>
   *
   * @param procedure
   *          The procedure.
   *
   * @param <E>
   *          The type of exceptions raised by the procedure other than
   *          {@link JCGLException}.
   * @throws JCGLExceptionExecution
   *           Iff <code>procedure</code> throws an exception of a type other
   *           than {@link JCGLException}.
   * @throws JCGLException
   *           Iff an OpenGL error occurs, or <code>procedure</code> throws
   *           {@link JCGLException}.
   */

  <E extends Throwable> void programExecute(
    final JCBProgramProcedureType<E> procedure)
    throws JCGLExceptionExecution,
      JCGLException;

  /**
   * @return A read-only reference to the actual program.
   */

  ProgramUsableType programGet();

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutFloat(
    final String u,
    float x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutInteger(
    final String u,
    int x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutMatrix3x3f(
    final String u,
    final MatrixDirectReadable3x3FType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutMatrix4x4f(
    final String u,
    final MatrixDirectReadable4x4FType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutTextureUnit(
    final String u,
    final TextureUnitType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector2f(
    final String u,
    final VectorReadable2FType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector2i(
    final String u,
    final VectorReadable2IType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector3f(
    final String u,
    final VectorReadable3FType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector3i(
    final String u,
    final VectorReadable3IType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector4f(
    final String u,
    final VectorReadable4FType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

  /**
   * <p>
   * Set the value of the uniform named <tt>u</tt> to the given value
   * <tt>x</tt>. After this function has been called successfully, the uniform
   * <tt>u</tt> will be assumed to have been assigned, for the purposes of
   * validation with {@link #programValidate()}.
   * </p>
   *
   * @throws JCGLExceptionTypeError
   *           If the uniform types do not match.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   * @param x
   *          The value.
   */

  void programUniformPutVector4i(
    final String u,
    final VectorReadable4IType x)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionTypeError;

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
   * @throws JCGLExceptionProgramValidationError
   *           If the uniform has never been assigned.
   * @throws JCGLExceptionProgramUniformMissing
   *           If the uniform does not exist.
   * @throws JCGLException
   *           If any other error occurs.
   * @param u
   *          The name of the program uniform.
   */

  void programUniformUseExisting(
    final String u)
    throws JCGLException,
      JCGLExceptionProgramUniformMissing,
      JCGLExceptionProgramValidationError;

  /**
   * <p>
   * Validate the current execution, checking that all uniforms and attributes
   * have been assigned values.
   * </p>
   *
   * @throws JCGLExceptionProgramValidationError
   *           If the program fails validation.
   */

  void programValidate()
    throws JCGLExceptionProgramValidationError;
}
