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

import java.nio.ByteBuffer;

import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateMappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Simplified interface to memory-mapped array buffers.
 */

public interface JCGLArrayBuffersMappedType
{
  /**
   * @return <code>true</code> if the given array is mapped.
   * 
   * @param id
   *          The array buffer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean arrayBufferIsMapped(
    final ArrayBufferUsableType id)
    throws JCGLException;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3Type#arrayBufferUnmap(ArrayBufferUsableType)}.
   * </p>
   * <p>
   * The "untyped" in the name refers to the fact that the mapped buffer is
   * returned as a simple byte array.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the array is already mapped.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  ByteBuffer arrayBufferMapReadUntyped(
    final ArrayBufferUsableType id)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. Only elements in the range
   * described by <code>range</code> will be mapped. The buffer should be
   * unmapped after use with
   * {@link JCGLInterfaceGL3Type#arrayBufferUnmap(ArrayBufferUsableType)}.
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
   * 
   * @return A readable byte buffer.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the array is already mapped.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * 
   * @see RangeInclusive#isIncludedIn(RangeInclusive)
   */

  ByteBuffer arrayBufferMapReadUntypedRange(
    final ArrayBufferUsableType id,
    final RangeInclusiveL range)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with
   * {@link JCGLInterfaceGL3Type#arrayBufferUnmap(ArrayBufferUsableType)}. The
   * previous contents of the buffer are discarded before mapping, to prevent
   * pipeline stalls.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLExceptionBufferMappedMultiple
   *           If the array is already mapped.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  ArrayBufferUpdateMappedType arrayBufferMapWrite(
    final ArrayBufferType id)
    throws JCGLException,
      JCGLExceptionBufferMappedMultiple;

  /**
   * <p>
   * Unmap the array buffer specified by <code>id</code>.
   * </p>
   * 
   * @param id
   *          The array buffer.
   * 
   * @throws JCGLExceptionBufferMappedNot
   *           If the array is not mapped.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUnmap(
    final ArrayBufferUsableType id)
    throws JCGLException,
      JCGLExceptionBufferMappedNot;
}
