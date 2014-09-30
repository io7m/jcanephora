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

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.VertexShaderType;

/**
 * The interface to the common subset of shading program functionality
 * supported by OpenGL ES and OpenGL 3.*.
 */

public interface JCGLShadersCommonType extends JCGLShadersParametersType
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
   * <p>
   * When using any of the functions in {@link JCGLShadersParametersType}, the
   * package does activity checks to ensure that the program to which values
   * are being assigned is the currently active program. This has a
   * performance cost however (querying OpenGL state may stall pipelines), and
   * higher-level APIs are capable of performing this check once rather than
   * on every call. This function returns an interface without per-call
   * activity checking. Use at your own risk!
   * </p>
   *
   * @return A value of type {@link JCGLShadersParametersType} that does not
   *         perform activity checking.
   */

  JCGLShadersParametersType programGetUncheckedInterface();

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
