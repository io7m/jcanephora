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
import com.io7m.jcanephora.core.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.core.JCGLExceptionProgramTypeError;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jtensors.MatrixDirect3x3FType;
import com.io7m.jtensors.MatrixDirect4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

/**
 * The interface to setting uniforms in shaders.
 */

public interface JCGLShaderUniformsType
{
  /**
   * Enable or disable checking of the types of assigned uniforms. Type checking
   * is enabled by default.
   *
   * @param enabled {@code true} iff type checking should be performed.
   */

  void shaderUniformSetTypeCheckingEnabled(
    boolean enabled);

  /**
   * Enable or disable checking that a program is active when attempting to
   * assign values to uniforms. Activity checking is enabled by default.
   *
   * @param enabled {@code true} iff activity checking should be performed.
   */

  void shaderUniformSetActivityCheckingEnabled(
    boolean enabled);

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutFloat(
    JCGLProgramUniformType u,
    float value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutInteger(
    JCGLProgramUniformType u,
    int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutUnsignedInteger(
    JCGLProgramUniformType u,
    int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector2f(
    JCGLProgramUniformType u,
    VectorReadable2FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector3f(
    JCGLProgramUniformType u,
    VectorReadable3FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector4f(
    JCGLProgramUniformType u,
    VectorReadable4FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector2i(
    JCGLProgramUniformType u,
    VectorReadable2IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector3i(
    JCGLProgramUniformType u,
    VectorReadable3IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector4i(
    JCGLProgramUniformType u,
    VectorReadable4IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector2ui(
    JCGLProgramUniformType u,
    VectorReadable2IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector3ui(
    JCGLProgramUniformType u,
    VectorReadable3IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutVector4ui(
    JCGLProgramUniformType u,
    VectorReadable4IType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;


  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutMatrix3x3f(
    JCGLProgramUniformType u,
    MatrixDirect3x3FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   *
   * @throws JCGLException                 Iff an OpenGL error occurs.
   * @throws JCGLExceptionProgramNotActive Iff the program to which the uniform
   *                                       belongs is not active, and program
   *                                       activity checking is enabled
   * @throws JCGLExceptionProgramTypeError Iff the program uniform is of the
   *                                       wrong type, and type checking is
   *                                       enabled
   * @see #shaderUniformSetTypeCheckingEnabled(boolean)
   * @see #shaderUniformSetActivityCheckingEnabled(boolean)
   */

  void shaderUniformPutMatrix4x4f(
    JCGLProgramUniformType u,
    MatrixDirect4x4FType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;
}
