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
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix3x3D;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix4x4D;
import com.io7m.jtensors.core.parameterized.vectors.PVector2D;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVector3D;
import com.io7m.jtensors.core.parameterized.vectors.PVector3I;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.jtensors.core.parameterized.vectors.PVector4I;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix3x3D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4I;

import java.nio.FloatBuffer;

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
   * This method is provided to allow for the use of array-typed uniforms in
   * GLSL programs, where the type of the array elements are scalar floating
   * point values, or vector floating point values.
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

  void shaderUniformPutVectorf(
    JCGLProgramUniformType u,
    FloatBuffer value)
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
    Vector2D value)
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
    Vector3D value)
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
    Vector4D value)
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
    Vector2I value)
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
    Vector3I value)
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
    Vector4I value)
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
    Vector2I value)
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
    Vector3I value)
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
    Vector4I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector2f(
    JCGLProgramUniformType u,
    PVector2D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector3f(
    JCGLProgramUniformType u,
    PVector3D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector4f(
    JCGLProgramUniformType u,
    PVector4D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector2i(
    JCGLProgramUniformType u,
    PVector2I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector3i(
    JCGLProgramUniformType u,
    PVector3I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector4i(
    JCGLProgramUniformType u,
    PVector4I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector2ui(
    JCGLProgramUniformType u,
    PVector2I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector3ui(
    JCGLProgramUniformType u,
    PVector3I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <T>   A phantom type parameter
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

  <T> void shaderUniformPutPVector4ui(
    JCGLProgramUniformType u,
    PVector4I<T> value)
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
    Matrix3x3D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <S>   A phantom type parameter
   * @param <T>   A phantom type parameter
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

  <S, T> void shaderUniformPutPMatrix3x3f(
    JCGLProgramUniformType u,
    PMatrix3x3D<S, T> value)
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
    Matrix4x4D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;

  /**
   * Upload the value {@code value} to the uniform {@code u}.
   *
   * @param u     The u variable.
   * @param value The value.
   * @param <S>   A phantom type parameter
   * @param <T>   A phantom type parameter
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

  <S, T> void shaderUniformPutPMatrix4x4f(
    JCGLProgramUniformType u,
    PMatrix4x4D<S, T> value)
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

  void shaderUniformPutTexture2DUnit(
    JCGLProgramUniformType u,
    JCGLTextureUnitType value)
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

  void shaderUniformPutTextureCubeUnit(
    JCGLProgramUniformType u,
    JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError;
}
