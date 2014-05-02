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
 * Simplified interface to index/element buffers.
 */

public interface JCGLIndexBuffersType
{
  /**
   * Allocate a buffer of <code>indices</code> indices. The function allocates
   * a buffer using indices of 1, 2, or 4 bytes depending on the number of
   * elements in <code>buffer</code>.
   * 
   * @param buffer
   *          The array buffer for which the index buffer is intended. Note
   *          that the index buffer is NOT restricted for use with the given
   *          array buffer.
   * @param indices
   *          The number of indices.
   * @return A reference to the allocated buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  IndexBufferType indexBufferAllocate(
    final ArrayBufferUsableType buffer,
    final int indices)
    throws JCGLRuntimeException;

  /**
   * Allocate a buffer of <code>indices</code> indices of type
   * <code>type</code>.
   * 
   * @param indices
   *          The number of indices.
   * @param type
   *          The type of indices.
   * @return A reference to the allocated buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  IndexBufferType indexBufferAllocateType(
    final JCGLUnsignedType type,
    final int indices)
    throws JCGLRuntimeException;

  /**
   * Deletes the index buffer referenced by <code>id</code>.
   * 
   * @param id
   *          The index buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void indexBufferDelete(
    final IndexBufferType id)
    throws JCGLRuntimeException;

  /**
   * Replace the contents (or part of the contents) of the index buffer
   * <code>data.getIndexBuffer()</code> with <code>data</code>.
   * 
   * @param data
   *          The data to upload.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void indexBufferUpdate(
    final IndexBufferUpdateUnmapped data)
    throws JCGLRuntimeException;
}
