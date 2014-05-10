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

import java.util.List;

import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jtensors.MatrixReadable3x3FType;
import com.io7m.jtensors.MatrixReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

/**
 * The interface to the common subset of shading program functionality
 * supported by OpenGL ES and OpenGL 3.*.
 */

public interface JCGLShadersCommonType
{
  /**
   * Read a GLSL fragment shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
   * @see com.io7m.jcanephora.utilities.ShaderUtilities#isEmpty(List)
   * @param name
   *          The name of the program.
   * @param lines
   *          A list of lines of GLSL source code.
   * @return A reference to the compiled fragment shader.
   * 
   * @throws JCGLExceptionProgramCompileError
   *           Iff a compilation error occurs.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  FragmentShaderType fragmentShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException;

  /**
   * Deletes the fragment shader referenced by <code>id</code>.
   * 
   * @param id
   *          The fragment shader.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void fragmentShaderDelete(
    final FragmentShaderType id)
    throws JCGLException;

  /**
   * Make the program referenced by <code>program</code> active.
   * 
   * @param program
   *          The program.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programActivate(
    final ProgramUsableType program)
    throws JCGLException;

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
    final ProgramAttributeType program_attribute,
    final ArrayAttributeType array_attribute)
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
    final ProgramAttributeType program_attribute)
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
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayAssociate(ProgramAttributeType,
   *      ArrayAttributeType)
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayDisassociate(ProgramAttributeType)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutFloat(
    final ProgramAttributeType program_attribute,
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
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayAssociate(ProgramAttributeType,
   *      ArrayAttributeType)
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayDisassociate(ProgramAttributeType)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector2f(
    final ProgramAttributeType program_attribute,
    final VectorReadable2FType x)
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
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayAssociate(ProgramAttributeType,
   *      ArrayAttributeType)
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayDisassociate(ProgramAttributeType)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector3f(
    final ProgramAttributeType program_attribute,
    final VectorReadable3FType x)
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
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayAssociate(ProgramAttributeType,
   *      ArrayAttributeType)
   * @see com.io7m.jcanephora.api.JCGLShadersCommonType#programAttributeArrayDisassociate(ProgramAttributeType)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programAttributePutVector4f(
    final ProgramAttributeType program_attribute,
    final VectorReadable4FType x)
    throws JCGLException;

  /**
   * <p>
   * Create a new empty program named <code>name</code>, attach <code>v</code>
   * as the vertex shader and <code>f</code> as the fragment shader, link the
   * program, and return a reference to the linked program.
   * </p>
   * <p>
   * This function is only necessary when using fragment shaders that have
   * single outputs; a program that only has a single output is automatically
   * mapped to draw buffer 0.
   * </p>
   * 
   * @see com.io7m.jcanephora.api.JCGLShadersGL3Type#programCreateWithOutputs(String,
   *      VertexShaderType, FragmentShaderType, java.util.Map)
   * 
   * @param name
   *          The name of the program.
   * @param v
   *          A compiled vertex shader.
   * @param f
   *          A compiled fragment shader.
   * 
   * @return A reference to the created program.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramCompileError
   *           Iff the program fails to link.
   */

  ProgramType programCreateCommon(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f)
    throws JCGLException,
      JCGLExceptionProgramCompileError;

  /**
   * Disable the current shading program.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programDeactivate()
    throws JCGLException;

  /**
   * Deletes the shading program referenced by <code>program</code>.
   * 
   * @param program
   *          The referenced program.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programDelete(
    final ProgramType program)
    throws JCGLException;

  /**
   * @return The implementation-specific maximum for the number of active
   *         attributes.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  int programGetMaximumActiveAttributes()
    throws JCGLException;

  /**
   * @return <code>true</code> iff the program referenced by
   *         <code>program</code> is currently active.
   * @param program
   *          The program.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean programIsActive(
    final ProgramUsableType program)
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
    final ProgramUniformType uniform,
    final float value)
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
    final ProgramUniformType uniform,
    final int value)
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
    final ProgramUniformType uniform,
    final MatrixReadable3x3FType matrix)
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
    final ProgramUniformType uniform,
    final MatrixReadable4x4FType matrix)
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
    final ProgramUniformType uniform,
    final TextureUnitType unit)
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
    final ProgramUniformType uniform,
    final VectorReadable2FType vector)
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
    final ProgramUniformType uniform,
    final VectorReadable2IType vector)
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
    final ProgramUniformType uniform,
    final VectorReadable3FType vector)
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
    final ProgramUniformType uniform,
    final VectorReadable3IType vector)
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
    final ProgramUniformType uniform,
    final VectorReadable4FType vector)
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
    final ProgramUniformType uniform,
    final VectorReadable4IType vector)
    throws JCGLException;

  /**
   * Read a GLSL vertex shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
   * @see com.io7m.jcanephora.utilities.ShaderUtilities#isEmpty(List)
   * @param name
   *          The name of the program.
   * @param lines
   *          A list of lines of GLSL source code.
   * @return A reference to the compiled vertex shader.
   * @throws JCGLExceptionProgramCompileError
   *           Iff a compilation error occurs.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  VertexShaderType vertexShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException;

  /**
   * Deletes the vertex shader referenced by <code>id</code>.
   * 
   * @param id
   *          The vertex shader.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void vertexShaderDelete(
    final VertexShaderType id)
    throws JCGLException;
}
