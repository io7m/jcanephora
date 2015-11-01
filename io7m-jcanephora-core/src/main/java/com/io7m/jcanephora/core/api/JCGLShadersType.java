/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;

import java.util.List;
import java.util.Optional;

/**
 * The interface to shader compilation and use.
 */

public interface JCGLShadersType
{
  /**
   * Delete the given shader.
   *
   * @param p The shader
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the shader is already deleted
   */

  void shaderDeleteProgram(
    JCGLProgramShaderType p)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Delete the given shader.
   *
   * @param v The shader
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the shader is already deleted
   */

  void shaderDeleteVertex(
    JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Delete the given shader.
   *
   * @param f The shader
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the shader is already deleted
   */

  void shaderDeleteFragment(
    JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Delete the given shader.
   *
   * @param g The shader
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the shader is already deleted
   */

  void shaderDeleteGeometry(
    JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Read a GLSL vertex shader from the lines of GLSL source code given in
   * {@code lines}. Each line must be terminated with an end-of-line terminator
   * (GLSL accepts both LF and CRLF as EOL tokens). The program will be named
   * {@code name} in any error messages.
   *
   * @param name  The name of the program
   * @param lines A list of lines of GLSL source code
   *
   * @return A reference to the compiled vertex shader
   *
   * @throws JCGLExceptionProgramCompileError Iff a compilation error occurs
   * @throws JCGLException                    Iff an OpenGL error occurs
   */

  JCGLVertexShaderType shaderCompileVertex(
    String name,
    List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException;

  /**
   * Read a GLSL fragment shader from the lines of GLSL source code given in
   * {@code lines}. Each line must be terminated with an end-of-line terminator
   * (GLSL accepts both LF and CRLF as EOL tokens). The program will be named
   * {@code name} in any error messages.
   *
   * @param name  The name of the program
   * @param lines A list of lines of GLSL source code
   *
   * @return A reference to the compiled fragment shader
   *
   * @throws JCGLExceptionProgramCompileError Iff a compilation error occurs
   * @throws JCGLException                    Iff an OpenGL error occurs
   */

  JCGLFragmentShaderType shaderCompileFragment(
    String name,
    List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException;

  /**
   * Read a GLSL geometry shader from the lines of GLSL source code given in
   * {@code lines}. Each line must be terminated with an end-of-line terminator
   * (GLSL accepts both LF and CRLF as EOL tokens). The program will be named
   * {@code name} in any error messages.
   *
   * @param name  The name of the program
   * @param lines A list of lines of GLSL source code
   *
   * @return A reference to the compiled geometry shader
   *
   * @throws JCGLExceptionProgramCompileError Iff a compilation error occurs
   * @throws JCGLException                    Iff an OpenGL error occurs
   */

  JCGLGeometryShaderType shaderCompileGeometry(
    String name,
    List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException;

  /**
   * Link a program consisting of the vertex shader {@code v}, the optional
   * geometry shader {@code g}, and the fragment shader {@code f}.
   *
   * @param name The name of the program
   * @param v    A vertex shader
   * @param g    A geometry shader, if required
   * @param f    A fragment shader
   *
   * @return A linked program
   *
   * @throws JCGLExceptionProgramCompileError Iff the program cannot be linked
   * @throws JCGLExceptionDeleted             Iff any of the shaders have been
   *                                          deleted
   * @throws JCGLException                    Iff an OpenGL error occurs
   */

  JCGLProgramShaderType shaderLinkProgram(
    String name,
    JCGLVertexShaderUsableType v,
    Optional<JCGLGeometryShaderUsableType> g,
    JCGLFragmentShaderUsableType f)
    throws
    JCGLExceptionProgramCompileError,
    JCGLException,
    JCGLExceptionDeleted;

  /**
   * Activate the given program for use in rendering.
   *
   * @param p The program
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the shader has been deleted
   */

  void shaderActivateProgram(JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Deactivate any active program. If no program is active, the call has no
   * effect.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void shaderDeactivateProgram()
    throws JCGLException;

  /**
   * @return The currently activated program, if any
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  Optional<JCGLProgramShaderUsableType> shaderActivatedProgram()
    throws JCGLException;

}
