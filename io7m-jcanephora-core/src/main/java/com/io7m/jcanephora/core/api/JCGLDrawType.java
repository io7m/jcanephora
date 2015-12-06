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
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLPrimitives;

/**
 * Drawing commands.
 */

public interface JCGLDrawType
{
  /**
   * Draw {@code count - 1} primitives of type {@code p}, starting at element
   * {@code first}, taking the data from each currently active vertex
   * attribute.
   *
   * @param p     The type of primitives
   * @param first The first element
   * @param count The number of primitives
   *
   * @throws JCGLException On OpenGL errors
   * @see JCGLArrayObjectsType
   */

  void draw(
    JCGLPrimitives p,
    int first,
    int count)
    throws JCGLException;

  /**
   * Draw primitives of type {@code p}, taking the data from each currently
   * active vertex attribute using elements taken from the currently bound index
   * buffer.
   *
   * @param p The type of primitives
   *
   * @throws JCGLException               On OpenGL errors
   * @throws JCGLExceptionBufferNotBound If no index buffer is currently bound
   * @see JCGLArrayObjectsType
   */

  void drawElements(
    JCGLPrimitives p)
    throws JCGLException, JCGLExceptionBufferNotBound;
}
