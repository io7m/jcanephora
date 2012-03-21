package com.io7m.jcanephora;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Type-safe interface to OpenGL shading programs.
 */

public interface GLShaders
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void attachFragmentShader(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException;

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
   *           <li><code>program</code> does not refer to a valid program
   *           (possibly because the referenced program has been deleted).</li>
   *           <li><code>shader == null</code></li>
   *           <li><code>shader</code> does not refer to a valid shader
   *           (possibly because the referenced shader has been deleted).</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void attachVertexShader(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException;

  /**
   * Read a GLSL fragment shader from the stream referenced by
   * <code>stream</code>. The program will be named <code>name</code> in any
   * error messages.
   * 
   * @param name
   *          The name of the program.
   * @param stream
   *          A readable stream containing a GLSL fragment shader.
   * @return A reference to the compiled fragment shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>stream == null</code></li>
   *           </ul>
   * @throws GLCompileException
   *           Iff a compilation error occurs.
   * @throws IOException
   *           Iff an i/o error occurs whilst reading the stream.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull FragmentShader compileFragmentShader(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException;

  /**
   * Read a GLSL vertex shader from the stream referenced by
   * <code>stream</code>. The program will be named <code>name</code> in any
   * error messages.
   * 
   * @param name
   *          The name of the program.
   * @param stream
   *          A readable stream containing a GLSL vertex shader.
   * @return A reference to the compiled vertex shader.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li><code>stream == null</code></li>
   *           </ul>
   * @throws GLCompileException
   *           Iff a compilation error occurs.
   * @throws IOException
   *           Iff an i/o error occurs whilst reading the stream.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull VertexShader compileVertexShader(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException;

  /**
   * Create a new empty program named <code>name</code>.
   * 
   * @param name
   *          The name of the program.
   * @return A reference to the created program.
   * @throws ConstraintError
   *           Iff <code>name == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull ProgramReference createProgram(
    final @Nonnull String name)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteFragmentShader(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteVertexShader(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void getAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException;

  /**
   * Return the implementation-specific maximum for the number of active
   * attributes.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  int getMaximumActiveAttributes()
    throws GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void getUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void linkProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException;

  /**
   * Disable the current shading program.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void noProgram()
    throws GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixM3x3F matrix)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixM4x4F matrix)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void putUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException;

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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void useProgram(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException;
}
