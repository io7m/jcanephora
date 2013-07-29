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
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Type-safe interface to OpenGL shading programs.
 */

public interface JCGLShaders
{
  /**
   * Attach the fragment shader <code>shader</code> to the program referenced
   * by <code>program</code>.
   * 
   * @param program
   *          The target program.
   * @param shader
   *          The fragment shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code></li>
   *           <li><code>shader == null</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      JCGLException;

  /**
   * Read a GLSL fragment shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
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
   *           <li><code>∃n. lines.get(n) == null</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff a compilation error occurs.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException;

  /**
   * Create a new empty program named <code>name</code>.
   * 
   * @param name
   *          The name of the program.
   * @return A reference to the created program.
   * @throws ConstraintError
   *           Iff <code>name == null</code>.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException;

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
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException;

  /**
   * Return all active attributes for the program <code>program</code> in
   * <code>out</code>.
   * 
   * @param program
   *          The program.
   * @param out
   *          The map to which attributes will be saved.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programGetAttributes(
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      JCGLException;

  /**
   * Return the implementation-specific maximum for the number of active
   * attributes.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  int programGetMaximumActiveAttributes()
    throws JCGLException;

  /**
   * Return all active attributes for the program <code>program</code> in
   * <code>out</code>.
   * 
   * @param program
   *          The program.
   * @param out
   *          The map to which attributes will be saved.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code>.</li>
   *           <li><code>program</code> does not refer to a valid program
   *           (possible if the program has already been deleted).</li>
   *           <li><code>map == null</code>.</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programGetUniforms(
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean programIsActive(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException;

  /**
   * Link the program referenced by <code>program</code>.
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException;

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
   *           <li><code>uniform.getType() != TYPE_FLOAT_MATRIX_3</code>.</li>
   *           <li>The program that <code>uniform</code> belongs to is not
   *           active.</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLException;

  /**
   * Attach the vertex shader <code>shader</code> to the program referenced by
   * <code>program</code>.
   * 
   * @param program
   *          The target program.
   * @param shader
   *          The vertex shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>program == null</code></li>
   *           <li><code>shader == null</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException;

  /**
   * Read a GLSL vertex shader from the lines of GLSL source code given in
   * <code>lines</code>. Each line must be terminated with an end-of-line
   * terminator (GLSL accepts both LF and CRLF as EOL tokens). The program
   * will be named <code>name</code> in any error messages.
   * 
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
   *           <li><code>∃n. lines.get(n) == null</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff a compilation error occurs.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLException;
}
