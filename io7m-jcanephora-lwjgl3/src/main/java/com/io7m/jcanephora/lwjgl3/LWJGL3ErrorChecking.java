/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.junreachable.UnreachableCodeException;
import org.lwjgl.opengl.GL11;

final class LWJGL3ErrorChecking
{
  private LWJGL3ErrorChecking()
  {
    throw new UnreachableCodeException();
  }

  static String errorString(final int err)
  {
    switch (err) {
      case GL11.GL_INVALID_ENUM:
        return "GL_INVALID_ENUM";
      case GL11.GL_INVALID_VALUE:
        return "GL_INVALID_VALUE";
      case GL11.GL_INVALID_OPERATION:
        return "GL_INVALID_OPERATION";
      case GL11.GL_STACK_OVERFLOW:
        return "GL_STACK_OVERFLOW";
      case GL11.GL_STACK_UNDERFLOW:
        return "GL_STACK_UNDERFLOW";
      case GL11.GL_OUT_OF_MEMORY:
        return "GL_OUT_OF_MEMORY";
      default:
        return "Unrecognized GL error";
    }
  }

  static void checkErrors()
  {
    JCGLException ex = null;

    for (int index = 0; index < 10; ++index) {
      final int e = GL11.glGetError();
      if (e == GL11.GL_NO_ERROR) {
        if (ex != null) {
          throw ex;
        }
        return;
      }

      if (ex == null) {
        ex = new JCGLException(
          String.format(
            "%s: %0x", LWJGL3ErrorChecking.errorString(e), Integer.valueOf(e)));
      } else {
        ex.addSuppressed(new JCGLException(
          String.format(
            "%s: %0x",
            LWJGL3ErrorChecking.errorString(e),
            Integer.valueOf(e))));
      }
    }
  }
}