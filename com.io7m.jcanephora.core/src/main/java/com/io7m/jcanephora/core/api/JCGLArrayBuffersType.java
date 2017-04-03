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

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLUsageHint;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * The interface to OpenGL array buffers.
 */

public interface JCGLArrayBuffersType
{
  /**
   * @param a The array buffer that will be read
   * @param f A function used to allocate the buffer
   *
   * @return The contents of the array buffer
   *
   * @throws JCGLException               Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted        If the array buffer has already been
   *                                     deleted
   * @throws JCGLExceptionBufferNotBound If the array buffer {@code a} is not
   *                                     bound
   */

  ByteBuffer arrayBufferRead(
    JCGLArrayBufferUsableType a,
    JCGLByteBufferProducerType f)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound;

  /**
   * <p>Allocate and bind an array buffer of {@code size} bytes, informing the
   * implementation that the buffer will be used in the manner specified by
   * {@code usage}.</p>
   *
   * <p>Calling this method will unbind any currently bound array buffer.</p>
   *
   * @param size  The size in bytes of the array buffer
   * @param usage The usage hint
   *
   * @return A reference to the allocated buffer.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  JCGLArrayBufferType arrayBufferAllocate(
    long size,
    JCGLUsageHint usage)
    throws JCGLException;

  /**
   * <p>Reallocate the storage associated with the array buffer {@code a}.</p>
   *
   * <p>This is intended to facilitate streaming of buffer contents without
   * incurring implicit synchronization by the OpenGL driver. The existing array
   * buffer storage is "orphaned" and replaced by storage of the same size and
   * with the same usage hint.</p>
   *
   * @param a The array buffer
   *
   * @throws JCGLException               Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted        If the array buffer has already been
   *                                     deleted
   * @throws JCGLExceptionBufferNotBound If the array buffer {@code a} is not
   *                                     bound
   */

  void arrayBufferReallocate(
    JCGLArrayBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound;

  /**
   * @return The currently bound array buffer, if one exists
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  Optional<JCGLArrayBufferUsableType> arrayBufferGetCurrentlyBound()
    throws JCGLException;

  /**
   * @return {@code true} iff any array buffer is bound
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean arrayBufferAnyIsBound()
    throws JCGLException;

  /**
   * @param a The array buffer
   *
   * @return {@code true} iff the given array buffer is bound
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean arrayBufferIsBound(JCGLArrayBufferUsableType a)
    throws JCGLException;

  /**
   * Bind the given array buffer.
   *
   * @param a The array buffer
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the given array buffer has already been
   *                              deleted
   */

  void arrayBufferBind(
    JCGLArrayBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Unbind the current array buffer. If no buffer is bound, this call has no
   * effect.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void arrayBufferUnbind()
    throws JCGLException;

  /**
   * <p>Deletes the buffer referenced by {@code a}.</p>
   *
   * <p>Calling this method will unbind {@code a} iff it is bound.</p>
   *
   * @param a The array buffer
   *
   * @throws JCGLExceptionDeleted If the array is already deleted
   * @throws JCGLException        Iff an OpenGL error occurs
   */

  void arrayBufferDelete(
    JCGLArrayBufferType a)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * <p>Perform the array buffer update {@code u}.</p>
   *
   * @param u The update
   *
   * @throws JCGLException               Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted        If the array buffer has already been
   *                                     deleted
   * @throws JCGLExceptionBufferNotBound If the array buffer in {@code u} is not
   *                                     bound
   */

  void arrayBufferUpdate(
    JCGLBufferUpdateType<JCGLArrayBufferType> u)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound;
}
