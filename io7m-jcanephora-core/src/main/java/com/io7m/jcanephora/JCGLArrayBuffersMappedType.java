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

import java.nio.ByteBuffer;

import com.io7m.jranges.RangeInclusiveL;

/**
 * Simplified interface to memory-mapped array buffers.
 */

public interface JCGLArrayBuffersMappedType
{
  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3#arrayBufferUnmap(ArrayBufferUsableType)}.
   * </p>
   * <p>
   * The "untyped" in the name refers to the fact that the mapped buffer is
   * returned as a simple byte array.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  ByteBuffer arrayBufferMapReadUntyped(
    final ArrayBufferUsableType id)
    throws JCGLRuntimeException;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. Only elements in the range
   * described by <code>range</code> will be mapped. The buffer should be
   * unmapped after use with
   * {@link JCGLInterfaceGL3#arrayBufferUnmap(ArrayBufferUsableType)}.
   * </p>
   * <p>
   * The "untyped" in the name refers to the fact that the mapped buffer is
   * returned as a simple byte array.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @param range
   *          The range (in elements) of elements to map.
   * @return A readable byte buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null || range == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           <li><code>range</code> is not included in
   *           <code>id.getRange()</code>.
   *           </ul>
   * @see RangeInclusive#isIncludedIn(RangeInclusive)
   */

  ByteBuffer arrayBufferMapReadUntypedRange(
    final ArrayBufferUsableType id,
    final RangeInclusiveL range)
    throws JCGLRuntimeException;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3#arrayBufferUnmap(ArrayBufferUsableType)}. The
   * previous contents of the buffer are discarded before mapping, to prevent
   * pipeline stalls.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  ArrayBufferUpdateMapped arrayBufferMapWrite(
    final ArrayBuffer id)
    throws JCGLRuntimeException;

  /**
   * <p>
   * Unmap the array buffer specified by <code>id</code>.
   * </p>
   * 
   * @param id
   *          The array buffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUnmap(
    final ArrayBufferUsableType id)
    throws JCGLRuntimeException;
}
