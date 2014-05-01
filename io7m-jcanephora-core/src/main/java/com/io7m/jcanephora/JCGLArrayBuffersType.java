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

package com.io7m.jcanephora;

/**
 * Simplified interface to array buffers.
 */

public interface JCGLArrayBuffersType
{
  /**
   * Allocate a buffer of <code>elements</code> elements of size
   * {@link ArrayDescriptor#getElementSize()}, informing the implementation
   * that the buffer will be used in the manner specified by
   * <code>usage</code>.
   * 
   * @param elements
   *          The number of elements in the array buffer.
   * @param descriptor
   *          A value describing the type of elements held in the buffer.
   * @param usage
   *          The usage hint
   * @return A reference to the allocated buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  ArrayBufferType arrayBufferAllocate(
    final long elements,
    final ArrayDescriptor descriptor,
    final UsageHint usage)
    throws JCGLRuntimeException;

  /**
   * Bind the array buffer <code>buffer</code> for subsequent calls to
   * {@link JCGLShadersCommon#programAttributeArrayAssociate(ProgramAttribute, ArrayBufferAttribute)}
   * .
   * 
   * @param buffer
   *          The array buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferBind(
    final ArrayBufferUsableType buffer)
    throws JCGLRuntimeException;

  /**
   * Deletes the buffer referenced by <code>id</code>.
   * 
   * @param id
   *          The array buffer.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferDelete(
    final ArrayBufferType id)
    throws JCGLRuntimeException;

  /**
   * @return <code>true</code> iff the array buffer specified by
   *         <code>id</code> is the currently bound buffer in the OpenGL
   *         implementation.
   * 
   * @param id
   *          The array buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean arrayBufferIsBound(
    final ArrayBufferUsableType id)
    throws JCGLRuntimeException;

  /**
   * Unbind the current array buffer.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUnbind()
    throws JCGLRuntimeException;

  /**
   * Replace the contents (or part of the contents) of the array buffer
   * associated with <code>data</code>. The function requires that
   * <code>data.getArrayBuffer()</code> be bound.
   * 
   * @param data
   *          The data to upload.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUpdate(
    final ArrayBufferUpdateUnmappedType data)
    throws JCGLRuntimeException;
}
