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

import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLException;

/**
 * The interface to OpenGL vertex array objects.
 */

public interface JCGLArrayObjectsType
{
  /**
   * Retrieve a new array object builder. The returned builder can be used
   * indefinitely but is tied to the {@link JCGLContextType} upon which it was
   * created.
   *
   * @return A new array object builder
   *
   * @throws JCGLException On OpenGL errors
   */

  JCGLArrayObjectBuilderType arrayObjectNewBuilder()
    throws JCGLException;

  /**
   * <p>Allocate an array object based on the values given in {@code b}.</p>
   *
   * <p>Calling this method will unbind any currently bound array buffer.</p>
   *
   * @param b The array object builder
   *
   * @return A new array object
   *
   * @throws JCGLException On OpenGL errors
   * @see JCGLArrayBuffersType#arrayBufferUnbind()
   */

  JCGLArrayObjectType arrayObjectAllocate(
    JCGLArrayObjectBuilderType b)
    throws JCGLException;
}
