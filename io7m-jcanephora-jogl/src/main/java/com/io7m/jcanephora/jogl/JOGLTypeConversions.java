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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;

/**
 * <p>Conversions between enumerations and OpenGL contstants.</p>
 *
 * <p>Note: This file is not part of the public API, but is exposed in order to
 * facilitate easier unit testing.</p>
 */

public final class JOGLTypeConversions
{
  private JOGLTypeConversions()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Convert hints from GL constants.
   *
   * @param hint The GL constant.
   *
   * @return The value.
   */

  public static JCGLUsageHint usageHintFromGL(
    final int hint)
  {
    switch (hint) {
      case GL2ES3.GL_DYNAMIC_COPY:
        return JCGLUsageHint.USAGE_DYNAMIC_COPY;
      case GL.GL_DYNAMIC_DRAW:
        return JCGLUsageHint.USAGE_DYNAMIC_DRAW;
      case GL2ES3.GL_DYNAMIC_READ:
        return JCGLUsageHint.USAGE_DYNAMIC_READ;
      case GL2ES3.GL_STATIC_COPY:
        return JCGLUsageHint.USAGE_STATIC_COPY;
      case GL.GL_STATIC_DRAW:
        return JCGLUsageHint.USAGE_STATIC_DRAW;
      case GL2ES3.GL_STATIC_READ:
        return JCGLUsageHint.USAGE_STATIC_READ;
      case GL2ES3.GL_STREAM_COPY:
        return JCGLUsageHint.USAGE_STREAM_COPY;
      case GL2ES2.GL_STREAM_DRAW:
        return JCGLUsageHint.USAGE_STREAM_DRAW;
      case GL2ES3.GL_STREAM_READ:
        return JCGLUsageHint.USAGE_STREAM_READ;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert hints to GL constants.
   *
   * @param hint The hint.
   *
   * @return The resulting GL constant.
   */

  public static int usageHintToGL(
    final JCGLUsageHint hint)
  {
    switch (hint) {
      case USAGE_DYNAMIC_COPY:
        return GL2ES3.GL_DYNAMIC_COPY;
      case USAGE_DYNAMIC_DRAW:
        return GL.GL_DYNAMIC_DRAW;
      case USAGE_DYNAMIC_READ:
        return GL2ES3.GL_DYNAMIC_READ;
      case USAGE_STATIC_COPY:
        return GL2ES3.GL_STATIC_COPY;
      case USAGE_STATIC_DRAW:
        return GL.GL_STATIC_DRAW;
      case USAGE_STATIC_READ:
        return GL2ES3.GL_STATIC_READ;
      case USAGE_STREAM_COPY:
        return GL2ES3.GL_STREAM_COPY;
      case USAGE_STREAM_DRAW:
        return GL2ES2.GL_STREAM_DRAW;
      case USAGE_STREAM_READ:
        return GL2ES3.GL_STREAM_READ;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types from GL constants.
   *
   * @param type The GL constant.
   *
   * @return The value.
   */

  public static JCGLScalarType scalarTypeFromGL(
    final int type)
  {
    switch (type) {
      case GL.GL_HALF_FLOAT:
        return JCGLScalarType.TYPE_HALF_FLOAT;
      case GL.GL_BYTE:
        return JCGLScalarType.TYPE_BYTE;
      case GL.GL_UNSIGNED_BYTE:
        return JCGLScalarType.TYPE_UNSIGNED_BYTE;
      case GL.GL_SHORT:
        return JCGLScalarType.TYPE_SHORT;
      case GL.GL_UNSIGNED_SHORT:
        return JCGLScalarType.TYPE_UNSIGNED_SHORT;
      case GL2ES2.GL_INT:
        return JCGLScalarType.TYPE_INT;
      case GL.GL_UNSIGNED_INT:
        return JCGLScalarType.TYPE_UNSIGNED_INT;
      case GL.GL_FLOAT:
        return JCGLScalarType.TYPE_FLOAT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int scalarTypeToGL(
    final JCGLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
      case TYPE_HALF_FLOAT:
        return GL.GL_HALF_FLOAT;
      case TYPE_FLOAT:
        return GL.GL_FLOAT;
      case TYPE_INT:
        return GL2ES2.GL_INT;
      case TYPE_SHORT:
        return GL.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Convert types to GL constants.
   *
   * @param type The type.
   *
   * @return The resulting GL constant.
   */

  public static int scalarIntegralTypeToGL(final JCGLScalarIntegralType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return GL.GL_BYTE;
      case TYPE_INT:
        return GL2ES2.GL_INT;
      case TYPE_SHORT:
        return GL.GL_SHORT;
      case TYPE_UNSIGNED_BYTE:
        return GL.GL_UNSIGNED_BYTE;
      case TYPE_UNSIGNED_INT:
        return GL.GL_UNSIGNED_INT;
      case TYPE_UNSIGNED_SHORT:
        return GL.GL_UNSIGNED_SHORT;
    }

    throw new UnreachableCodeException();
  }
}
