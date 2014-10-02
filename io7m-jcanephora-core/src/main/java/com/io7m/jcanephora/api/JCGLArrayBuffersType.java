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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.UsageHint;

/**
 * Simplified interface to array buffers.
 */

public interface JCGLArrayBuffersType
{
  /**
   * Allocate a buffer of <code>elements</code> elements of size
   * {@link ArrayDescriptor#getElementSizeBytes()}, informing the
   * implementation that the buffer will be used in the manner specified by
   * <code>usage</code>.
   *
   * @param elements
   *          The number of elements in the array buffer.
   * @param descriptor
   *          A value describing the type of elements held in the buffer.
   * @param usage
   *          The usage hint
   * @return A reference to the allocated buffer.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  ArrayBufferType arrayBufferAllocate(
    final long elements,
    final ArrayDescriptor descriptor,
    final UsageHint usage)
    throws JCGLException;

  /**
   * @return <code>true</code> iff any array buffer is currently bound.
   *
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean arrayBufferAnyIsBound()
    throws JCGLException;

  /**
   * Bind the array buffer <code>buffer</code> for subsequent calls to
   * {@link JCGLShadersCommonType#programAttributeArrayAssociate(com.io7m.jcanephora.ProgramAttributeType, com.io7m.jcanephora.ArrayAttributeType)}
   * .
   *
   * @param buffer
   *          The array buffer.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferBind(
    final ArrayBufferUsableType buffer)
    throws JCGLException;

  /**
   * Deletes the buffer referenced by <code>id</code>.
   *
   * @param id
   *          The array buffer.
   *
   * @throws JCGLExceptionDeleted
   *           If the array is already deleted.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferDelete(
    final ArrayBufferType id)
    throws JCGLException,
      JCGLExceptionDeleted;

  /**
   * @return <code>true</code> iff the array buffer specified by
   *         <code>id</code> is the currently bound buffer in the OpenGL
   *         implementation.
   *
   * @param id
   *          The array buffer.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean arrayBufferIsBound(
    final ArrayBufferUsableType id)
    throws JCGLException;

  /**
   * Unbind the current array buffer.
   *
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUnbind()
    throws JCGLException;

  /**
   * Replace the contents (or part of the contents) of the array buffer
   * associated with <code>data</code>.
   *
   * @param data
   *          The data to upload.
   *
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUpdate(
    final ArrayBufferUpdateUnmappedType data)
    throws JCGLException;
}
