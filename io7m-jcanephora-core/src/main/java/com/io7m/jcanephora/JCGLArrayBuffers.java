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

/**
 * Simplified interface to array buffers.
 */

public interface JCGLArrayBuffers
{
  /**
   * Allocate a buffer of <code>elements</code> elements of size
   * <code>descriptor.getSize()</code>, informing the implementation that the
   * buffer will be used in the manner specified by <code>usage</code>.
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
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>0 < elements <= Long.MAX_VALUE == false</code>.</li>
   *           <li><code>descriptor == null</code>.</li>
   *           <li><code>usage == null</code>.</li>
   *           </ul>
   */

  public @Nonnull ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLException,
      ConstraintError;

  /**
   * Bind the array buffer <code>buffer</code> for subsequent calls to
   * {@link JCGLArrayBuffers#arrayBufferBindVertexAttribute(ArrayBufferAttribute, ProgramAttribute)}
   * .
   * 
   * @param buffer
   *          The array buffer.
   * @throws JCGLException
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
    final @Nonnull ArrayBufferUsable buffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Enable and bind the array attribute <code>buffer_attribute</code> to the
   * program attribute <code>program_attribute</code>.
   * 
   * @param buffer_attribute
   *          The array buffer attribute for the given array buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>The array that owns <code>buffer_attribute</code> is not
   *           bound.</li>
   *           <li>The array that owns <code>buffer_attribute</code> is
   *           deleted.</li>
   *           <li>The type of <code>buffer_attribute</code> is incompatible
   *           with <code>program_attribute</code>.</li>
   *           <li><code>buffer_attribute == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      JCGLException;

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException;

  /**
   * Unbind the current array buffer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUnbind()
    throws JCGLException,
      ConstraintError;

  /**
   * Disable the array attribute <code>buffer_attribute</code> for the program
   * attribute <code>program_attribute</code>.
   * 
   * @param buffer_attribute
   *          The array buffer attribute for the given buffer.
   * @param program_attribute
   *          The program attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>The array that owns <code>buffer_attribute</code> is not
   *           bound.</li>
   *           <li>The array that owns <code>buffer_attribute</code> is
   *           deleted.</li>
   *           <li><code>buffer_attribute == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws JCGLException,
      ConstraintError;
}
