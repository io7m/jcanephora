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

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

/**
 * The interface to the common subset of shading program functionality
 * supported by OpenGL ES and OpenGL 3.*.
 */

public interface JCGLShadersCommon
{
  /**
   * Read a GLSL fragment shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
   * @see ShaderUtilities#isEmpty(List)
   * @param name
   *          The name of the program.
   * @param lines
   *          A list of lines of GLSL source code.
   * @return A reference to the compiled fragment shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>lines == null</code></li>
   *           <li><code>ShaderUtilities.isEmpty(lines) == true</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff a compilation error occurs.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

   FragmentShader fragmentShaderCompile(
    final  String name,
    final  List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLRuntimeException;

  /**
   * Deletes the fragment shader referenced by <code>id</code>.
   * 
   * @param id
   *          The fragment shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid shader (possible
   *           if the shader has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void fragmentShaderDelete(
    final  FragmentShader id)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Make the program referenced by <code>program</code> active.
   * 
   * @param program
   *          The program.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programActivate(
    final  ProgramReferenceUsableType program)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Associate the array attribute <code>array_attribute</code> with the
   * program attribute <code>program_attribute</code>.
   * 
   * @param array_attribute
   *          The array buffer attribute for the given array buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>array_attribute == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           <li>The array that owns <code>array_attribute</code> is not
   *           bound.</li>
   *           <li>The array that owns <code>array_attribute</code> is
   *           deleted.</li>
   *           <li>The type of <code>array_attribute</code> is incompatible
   *           with <code>program_attribute</code>.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  public void programAttributeArrayAssociate(
    final  ProgramAttribute program_attribute,
    final  ArrayBufferAttribute array_attribute)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * Disassociate the program attribute <code>program_attribute</code> with
   * the array attribute to which it was previously associated (if any).
   * 
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program_attribute == null</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  public void programAttributeArrayDisassociate(
    final  ProgramAttribute program_attribute)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see #programAttributeArrayAssociate(ProgramAttribute,
   *      ArrayBufferAttribute)
   * @see #programAttributeArrayDisassociate(ProgramAttribute)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program_attribute == null</code></li>
   *           <li>The type of <code>program_attribute</code> does not match
   *           that of <code>x</code>.</li>
   *           <li>The program that <code>program_attribute</code> belongs to
   *           is not active.</li>
   *           </ul>
   */

  public void programAttributePutFloat(
    final  ProgramAttribute program_attribute,
    float x)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see #programAttributeArrayAssociate(ProgramAttribute,
   *      ArrayBufferAttribute)
   * @see #programAttributeArrayDisassociate(ProgramAttribute)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program_attribute == null</code></li>
   *           <li><code>x == null</code></li>
   *           <li>The type of <code>program_attribute</code> does not match
   *           that of <code>x</code>.</li>
   *           <li>The program that <code>program_attribute</code> belongs to
   *           is not active.</li>
   *           </ul>
   */

  public void programAttributePutVector2f(
    final  ProgramAttribute program_attribute,
    final  VectorReadable2F x)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see #programAttributeArrayAssociate(ProgramAttribute,
   *      ArrayBufferAttribute)
   * @see #programAttributeArrayDisassociate(ProgramAttribute)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program_attribute == null</code></li>
   *           <li><code>x == null</code></li>
   *           <li>The type of <code>program_attribute</code> does not match
   *           that of <code>x</code>.</li>
   *           <li>The program that <code>program_attribute</code> belongs to
   *           is not active.</li>
   *           </ul>
   */

  public void programAttributePutVector3f(
    final  ProgramAttribute program_attribute,
    final  VectorReadable3F x)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see #programAttributeArrayAssociate(ProgramAttribute,
   *      ArrayBufferAttribute)
   * @see #programAttributeArrayDisassociate(ProgramAttribute)
   * 
   * @param program_attribute
   *          The program attribute.
   * @param x
   *          The value.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program_attribute == null</code></li>
   *           <li><code>x == null</code></li>
   *           <li>The type of <code>program_attribute</code> does not match
   *           that of <code>x</code>.</li>
   *           <li>The program that <code>program_attribute</code> belongs to
   *           is not active.</li>
   *           </ul>
   */

  public void programAttributePutVector4f(
    final  ProgramAttribute program_attribute,
    final  VectorReadable4F x)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see JCGLShadersGL3#programCreateWithOutputs(String, VertexShader,
   *      FragmentShader, Map)
   * 
   * @param name
   *          The name of the program.
   * @param v
   *          A compiled vertex shader.
   * @param f
   *          A compiled fragment shader.
   * 
   * @return A reference to the created program.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null || v == null || f == null</code></li>
   *           <li><code>v</code> is deleted.</li>
   *           <li><code>f</code> is deleted.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws JCGLCompileException
   *           Iff the program fails to link.
   */

   ProgramReference programCreateCommon(
    final  String name,
    final  VertexShader v,
    final  FragmentShader f)
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLCompileException;

  /**
   * Disable the current shading program.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programDeactivate()
    throws JCGLRuntimeException;

  /**
   * Deletes the shading program referenced by <code>program</code>.
   * 
   * @param program
   *          The referenced program.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programDelete(
    final  ProgramReference program)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return the implementation-specific maximum for the number of active
   * attributes.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  int programGetMaximumActiveAttributes()
    throws JCGLRuntimeException;

  /**
   * Return <code>true</code> iff the program referenced by
   * <code>program</code> is currently active.
   * 
   * @param program
   *          The program.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean programIsActive(
    final  ProgramReferenceUsableType program)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the value <code>value</code> to the uniform <code>uniform</code>.
   * 
   * @param uniform
   *          The uniform variable.
   * @param value
   *          The value.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutFloat(
    final  ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the value <code>value</code> to the uniform <code>uniform</code>.
   * 
   * @param uniform
   *          The uniform variable.
   * @param value
   *          The value.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_INTEGER</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutInteger(
    final  ProgramUniform uniform,
    final int value)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the matrix <code>matrix</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param matrix
   *          The matrix.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>matrix == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT_MATRIX_3</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutMatrix3x3f(
    final  ProgramUniform uniform,
    final  MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the matrix <code>matrix</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param matrix
   *          The matrix.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>matrix == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT_MATRIX_4</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutMatrix4x4f(
    final  ProgramUniform uniform,
    final  MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the texture unit index <code>unit</code> to the uniform
   * <code>uniform</code> .
   * 
   * @param uniform
   *          The uniform variable.
   * @param unit
   *          The texture unit.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>unit == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_SAMPLER_2D</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutTextureUnit(
    final  ProgramUniform uniform,
    final  TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT_VECTOR_2</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector2f(
    final  ProgramUniform uniform,
    final  VectorReadable2F vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_INT_VECTOR_2</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector2i(
    final  ProgramUniform uniform,
    final  VectorReadable2I vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT_VECTOR_3</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector3f(
    final  ProgramUniform uniform,
    final  VectorReadable3F vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_INT_VECTOR_3</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector3i(
    final  ProgramUniform uniform,
    final  VectorReadable3I vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_FLOAT_VECTOR_4</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector4f(
    final  ProgramUniform uniform,
    final  VectorReadable4F vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Upload the vector <code>vector</code> to the uniform <code>uniform</code>
   * .
   * 
   * @param uniform
   *          The uniform variable.
   * @param vector
   *          The vector.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>uniform == null</code>.</li>
   *           <li><code>vector == null</code>.</li>
   *           <li><code>uniform.getType() != TYPE_INT_VECTOR_4</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void programUniformPutVector4i(
    final  ProgramUniform uniform,
    final  VectorReadable4I vector)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Read a GLSL vertex shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
   * @see ShaderUtilities#isEmpty(List)
   * @param name
   *          The name of the program.
   * @param lines
   *          A list of lines of GLSL source code.
   * @return A reference to the compiled vertex shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>lines == null</code></li>
   *           <li><code>ShaderUtilities.isEmpty(lines) == true</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff a compilation error occurs.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

   VertexShader vertexShaderCompile(
    final  String name,
    final  List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLRuntimeException;

  /**
   * Deletes the vertex shader referenced by <code>id</code>.
   * 
   * @param id
   *          The vertex shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid shader (possible
   *           if the shader has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void vertexShaderDelete(
    final  VertexShader id)
    throws ConstraintError,
      JCGLRuntimeException;
}
