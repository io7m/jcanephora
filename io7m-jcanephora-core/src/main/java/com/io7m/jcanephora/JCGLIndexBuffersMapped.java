/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * Simplified interface to memory-mapped index buffers.
 */

public interface JCGLIndexBuffersMapped
{
  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with {@link JCGLInterfaceGL3#indexBufferUnmap(IndexBuffer)}. Note
   * that the type of indices in the buffer is given by
   * <code>id.getType()</code>.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code> .</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  public @Nonnull IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull IndexBuffer id)
    throws JCGLException,
      ConstraintError;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. Only elements in the range
   * described by <code>range</code> will be mapped. The buffer should be
   * unmapped after use with
   * {@link JCGLInterfaceGL3#indexBufferUnmap(IndexBuffer)}. Note that the type
   * of indices in the buffer is given by <code>id.getType()</code>.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @param range
   *          The range (in elements) of elements to map.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null || range == null</code> .</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           <li><code>range</code> is not included in
   *           <code>id.getRange()</code>.
   *           </ul>
   * @see RangeInclusive#isIncludedIn(RangeInclusive)
   */

  public @Nonnull IndexBufferReadableMap indexBufferMapReadRange(
    final @Nonnull IndexBuffer id,
    final @Nonnull RangeInclusive range)
    throws JCGLException,
      ConstraintError;

  /**
   * <p>
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with {@link JCGLInterfaceGL3#indexBufferUnmap(IndexBuffer)}. Note
   * that the type of indices in the buffer is given by
   * <code>id.getType()</code>. The previous contents of the buffer are
   * discarded to prevent pipeline stalls.
   * </p>
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code> .</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  public @Nonnull IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull IndexBuffer id)
    throws JCGLException,
      ConstraintError;

  /**
   * <p>
   * Unmap the index buffer specified by <code>id</code>.
   * </p>
   * 
   * @param id
   *          The index buffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void indexBufferUnmap(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLException;
}
