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

import com.io7m.jcanephora.IndexBufferReadMappedType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateMappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Simplified interface to memory-mapped index buffers.
 */

public interface JCGLIndexBuffersMappedType
{
  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3Type#indexBufferUnmap(IndexBufferUsableType)}.
   * Note that the type of indices in the buffer is given by
   * <code>id.getType()</code>.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the index buffer is already mapped.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  IndexBufferReadMappedType indexBufferMapRead(
    final IndexBufferUsableType id)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. Only elements in the range
   * described by <code>range</code> will be mapped. The buffer should be
   * unmapped after use with
   * {@link JCGLInterfaceGL3Type#indexBufferUnmap(IndexBufferUsableType)}.
   * Note that the type of indices in the buffer is given by
   * <code>id.getType()</code>.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @param range
   *          The range (in elements) of elements to map.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the index buffer is already mapped.
   * @see RangeInclusive#isIncludedIn(RangeInclusive)
   */

  IndexBufferReadMappedType indexBufferMapReadRange(
    final IndexBufferUsableType id,
    final RangeInclusiveL range)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3Type#indexBufferUnmap(IndexBufferUsableType)}.
   * Note that the type of indices in the buffer is given by
   * <code>id.getType()</code>. The previous contents of the buffer are
   * discarded to prevent pipeline stalls.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the index buffer is already mapped.
   */

  IndexBufferUpdateMappedType indexBufferMapWrite(
    final IndexBufferType id)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Unmap the index buffer specified by <code>id</code>.
   * </p>
   * 
   * @param id
   *          The index buffer.
   * @throws JCGLExceptionBufferMappedNot
   *           If the index buffer is not mapped.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void indexBufferUnmap(
    final IndexBufferUsableType id)
    throws JCGLException,
      JCGLExceptionBufferMappedNot;
}
