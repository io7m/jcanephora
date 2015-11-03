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

import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;

import java.util.Optional;

/**
 * The interface to OpenGL index buffers.
 */

public interface JCGLIndexBuffersType
{
  /**
   * <p>Allocate and bind an index buffer of {@code indices} values of type
   * {@code type}, informing the implementation that the buffer will be used in
   * the manner specified by {@code usage}.</p>
   *
   * <p>The index buffer is bound to the current array object.</p>
   *
   * @param indices The number of indices
   * @param type    The type of indices
   * @param usage   The usage hint
   *
   * @return An index buffer
   *
   * @see JCGLArrayObjectsType
   */

  JCGLIndexBufferType indexBufferAllocate(
    long indices,
    JCGLUnsignedType type,
    JCGLUsageHint usage);

  /**
   * @return The currently bound index buffer, if one exists
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  Optional<JCGLIndexBufferUsableType> indexBufferGetCurrentlyBound()
    throws JCGLException;

  /**
   * Bind the given index buffer.
   *
   * @param a The index buffer
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the given index buffer has already been
   *                              deleted
   */

  void indexBufferBind(
    JCGLIndexBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Unbind the current index buffer. If no buffer is bound, this call has no
   * effect.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void indexBufferUnbind()
    throws JCGLException;

  /**
   * <p>Deletes the buffer referenced by {@code a}.</p>
   *
   * <p>Calling this method will unbind {@code a} iff it is bound.</p>
   *
   * @param a The index buffer
   *
   * @throws JCGLExceptionDeleted If the index is already deleted
   * @throws JCGLException        Iff an OpenGL error occurs
   */

  void indexBufferDelete(
    JCGLIndexBufferType a)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * <p>Perform the index buffer update {@code u}.</p>
   *
   * @param u The update
   *
   * @throws JCGLException               Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted        If the index buffer has already been
   *                                     deleted
   * @throws JCGLExceptionBufferNotBound If the index buffer in {@code u} is not
   *                                     bound
   */

  void indexBufferUpdate(
    JCGLBufferUpdateType<JCGLIndexBufferType> u)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound;

  /**
   * @return {@code true} iff an index buffer is currently bound
   */

  boolean indexBufferIsBound();
}
