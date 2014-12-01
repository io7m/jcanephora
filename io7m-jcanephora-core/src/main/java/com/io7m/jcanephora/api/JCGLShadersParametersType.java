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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUniformType;
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
 * The interface used to set parameters (attributes, uniforms) for programs.
 */

public interface JCGLShadersParametersType
{

  /**
   * Associate the array attribute <code>array_attribute</code> with the
   * program attribute <code>program_attribute</code>.
   *
   * @param array_attribute
   *          The array buffer attribute for the given array buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributeArrayAssociate(
    ProgramAttributeType program_attribute,
    ArrayAttributeType array_attribute)
    throws JCGLException;

  /**
   * Disassociate the program attribute <code>program_attribute</code> with
   * the array attribute to which it was previously associated (if any).
   *
   * @param program_attribute
   *          The program attribute.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributeArrayDisassociate(
    ProgramAttributeType program_attribute)
    throws JCGLException;

  /**
   * <p>
   * Set the value of the program attribute <code>program_attribute</code> to
   * <code>x</code>. This essentially instructs OpenGL to behave as if it was
   * reading from an array buffer where every element is equal to
   * <code>x</code>.
   * </p>
   * <p>
   * The function will disassociate the program attribute with any array
   * attribute with which it may be associated.
   * </p>
   *
   * @see #programAttributeArrayAssociate(ProgramAttributeType,ArrayAttributeType)
   * @see #programAttributeArrayDisassociate(ProgramAttributeType)
   *
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutFloat(
    ProgramAttributeType program_attribute,
    float x)
    throws JCGLException;

  /**
   * <p>
   * Set the value of the program attribute <code>program_attribute</code> to
   * <code>x</code>. This essentially instructs OpenGL to behave as if it was
   * reading from an array buffer where every element is equal to
   * <code>x</code>.
   * </p>
   * <p>
   * The function will disassociate the program attribute with any array
   * attribute with which it may be associated.
   * </p>
   *
   * @see #programAttributeArrayAssociate(ProgramAttributeType,ArrayAttributeType)
   * @see #programAttributeArrayDisassociate(ProgramAttributeType)
   *
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector2f(
    ProgramAttributeType program_attribute,
    VectorReadable2FType x)
    throws JCGLException;

  /**
   * <p>
   * Set the value of the program attribute <code>program_attribute</code> to
   * <code>x</code>. This essentially instructs OpenGL to behave as if it was
   * reading from an array buffer where every element is equal to
   * <code>x</code>.
   * </p>
   * <p>
   * The function will disassociate the program attribute with any array
   * attribute with which it may be associated.
   * </p>
   *
   * @see #programAttributeArrayAssociate(ProgramAttributeType,ArrayAttributeType)
   * @see #programAttributeArrayDisassociate(ProgramAttributeType)
   *
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector3f(
    ProgramAttributeType program_attribute,
    VectorReadable3FType x)
    throws JCGLException;

  /**
   * <p>
   * Set the value of the program attribute <code>program_attribute</code> to
   * <code>x</code>. This essentially instructs OpenGL to behave as if it was
   * reading from an array buffer where every element is equal to
   * <code>x</code>.
   * </p>
   * <p>
   * The function will disassociate the program attribute with any array
   * attribute with which it may be associated.
   * </p>
   *
   * @see #programAttributeArrayAssociate(ProgramAttributeType,ArrayAttributeType)
   * @see #programAttributeArrayDisassociate(ProgramAttributeType)
   *
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector4f(
    ProgramAttributeType program_attribute,
    VectorReadable4FType x)
    throws JCGLException;

  /**
   * Upload the value <code>value</code> to the uniform <code>uniform</code>.
   *
   * @param uniform
   *          The uniform variable.
   * @param value
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutFloat(
    ProgramUniformType uniform,
    float value)
    throws JCGLException;

  /**
   * Upload the value <code>value</code> to the uniform <code>uniform</code>.
   *
   * @param uniform
   *          The uniform variable.
   * @param value
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutInteger(
    ProgramUniformType uniform,
    int value)
    throws JCGLException;

  /**
   * Upload the matrix <code>matrix</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param matrix
   *          The matrix.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutMatrix3x3f(
    ProgramUniformType uniform,
    MatrixDirectReadable3x3FType matrix)
    throws JCGLException;

  /**
   * Upload the matrix <code>matrix</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param matrix
   *          The matrix.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutMatrix4x4f(
    ProgramUniformType uniform,
    MatrixDirectReadable4x4FType matrix)
    throws JCGLException;

  /**
   * Upload the texture unit index <code>unit</code> to the uniform
   * <code>uniform</code> .
   *
   * @param uniform
   *          The uniform variable.
   * @param unit
   *          The texture unit.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutTextureUnit(
    ProgramUniformType uniform,
    TextureUnitType unit)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector2f(
    ProgramUniformType uniform,
    VectorReadable2FType vector)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector2i(
    ProgramUniformType uniform,
    VectorReadable2IType vector)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector3f(
    ProgramUniformType uniform,
    VectorReadable3FType vector)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector3i(
    ProgramUniformType uniform,
    VectorReadable3IType vector)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector4f(
    ProgramUniformType uniform,
    VectorReadable4FType vector)
    throws JCGLException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   *
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector4i(
    ProgramUniformType uniform,
    VectorReadable4IType vector)
    throws JCGLException;

}
