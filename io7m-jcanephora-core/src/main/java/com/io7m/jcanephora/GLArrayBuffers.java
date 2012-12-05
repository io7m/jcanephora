/*
 * Copyright © 2012 http://io7m.com
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

/**
 * Simplified interface to array buffers.
 */

public interface GLArrayBuffers
{
  /**
   * Allocate a buffer of <code>elements</code> elements of size
   * <code>descriptor.getSize()</code>.
   * 
   * @param elements
   *          The number of elements in the buffer.
   * @param descriptor
   *          A value describing the type of elements held in the buffer.
   * @return A reference to the allocated buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>0 < elements <= Long.MAX_VALUE == false</code>.</li>
   *           <li><code>descriptor == null</code>.</li>
   *           </ul>
   */

  public @Nonnull ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor)
    throws GLException,
      ConstraintError;

  /**
   * Bind the array buffer <code>buffer</code> for subsequent calls to
   * {@link GLArrayBuffers#arrayBufferBindVertexAttribute(ArrayBuffer, ArrayBufferAttribute, ProgramAttribute)}
   * .
   * 
   * @param buffer
   *          The array buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li>The given buffer is not a valid buffer (possibly because it
   *           has been deleted).</li>
   *           </ul>
   */

  public void arrayBufferBind(
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError;

  /**
   * Enable the array attribute <code>buffer_attribute</code> for buffer
   * <code>buffer</code> to the program attribute
   * <code>program_attribute</code>.
   * 
   * @param buffer
   *          The array buffer.
   * @param buffer_attribute
   *          The buffer attribute for the given buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>buffer_attribute == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError;

  /**
   * Deletes the buffer referenced by <code>id</code>.
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
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff the array buffer specified by
   * <code>id</code> is the currently bound buffer in the OpenGL
   * implementation.
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public boolean arrayBufferIsBound(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException;

  /**
   * Unbind the current array buffer.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUnbind()
    throws GLException,
      ConstraintError;

  /**
   * Disable the array attribute <code>buffer_attribute</code> for buffer
   * <code>buffer</code> for the program attribute
   * <code>program_attribute</code>.
   * 
   * @param buffer
   *          The array buffer.
   * @param buffer_attribute
   *          The buffer attribute for the given buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>buffer_attribute == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError;

  /**
   * Replace the contents (or part of the contents) of the array buffer
   * <code>buffer</code> with <code>data</code>.
   * 
   * @param buffer
   *          The array buffer.
   * @param data
   *          The data to upload.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>data == null</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws GLException,
      ConstraintError;
}
