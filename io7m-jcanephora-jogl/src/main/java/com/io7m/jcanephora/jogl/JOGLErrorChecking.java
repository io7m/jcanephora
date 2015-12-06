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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;

final class JOGLErrorChecking
{
  private JOGLErrorChecking()
  {
    throw new UnreachableCodeException();
  }

  static String errorString(final int err)
  {
    switch (err) {
      case GL.GL_INVALID_ENUM:
        return "GL_INVALID_ENUM";
      case GL.GL_INVALID_VALUE:
        return "GL_INVALID_VALUE";
      case GL.GL_INVALID_OPERATION:
        return "GL_INVALID_OPERATION";
      case GL2ES1.GL_STACK_OVERFLOW:
        return "GL_STACK_OVERFLOW";
      case GL2ES1.GL_STACK_UNDERFLOW:
        return "GL_STACK_UNDERFLOW";
      case GL.GL_OUT_OF_MEMORY:
        return "GL_OUT_OF_MEMORY";
      default:
        return "Unrecognized GL error";
    }
  }

  static void checkErrors(final GL g)
  {
    JCGLException ex = null;

    for (int index = 0; index < 10; ++index) {
      final int e = g.glGetError();
      if (e == GL.GL_NO_ERROR) {
        if (ex != null) {
          throw ex;
        }
        return;
      }

      if (ex == null) {
        ex = new JCGLException(
          String.format(
            "%s: %0x", JOGLErrorChecking.errorString(e), Integer.valueOf(e)));
      } else {
        ex.addSuppressed(new JCGLException(
          String.format(
            "%s: %0x", JOGLErrorChecking.errorString(e), Integer.valueOf(e))));
      }
    }
  }
}
