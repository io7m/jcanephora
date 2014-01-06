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

/**
 * A simple class for checking if an OpenGL error has occurred.
 */

public final class JCGLError
{
  /**
   * Raise <code>GLException</code> iff the current OpenGL error state is not
   * <code>GL_NO_ERROR</code>.
   * 
   * @param gl
   *          The current OpenGL interface.
   * @throws JCGLRuntimeException
   *           Iff the current OpenGL error state is not
   *           <code>GL_NO_ERROR</code>.
   */

  public static void check(
    final JCGLMeta gl)
    throws JCGLRuntimeException
  {
    final int code = gl.metaGetError();

    if (code != 0) {
      throw new JCGLRuntimeException(code, "OpenGL error: code " + code);
    }
  }
}