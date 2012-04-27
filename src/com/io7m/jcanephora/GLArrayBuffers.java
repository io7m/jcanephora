package com.io7m.jcanephora;

import java.nio.ByteBuffer;

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

  @Nonnull ArrayBuffer arrayBufferAllocate(
    final long elements,
    final ArrayBufferDescriptor descriptor)
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

  void arrayBufferBind(
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

  void arrayBufferBindVertexAttribute(
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

  void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with {@link GLInterface#arrayBufferUnmap(ArrayBuffer)}.
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  @Nonnull ByteBuffer arrayBufferMapRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with {@link GLInterface#arrayBufferUnmap(ArrayBuffer)}. The
   * previous contents of the buffer are discarded before mapping, to prevent
   * pipeline stalls.
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>id == null</code>.</li>
   *           <li><code>id</code> does not refer to a valid buffer (possible
   *           if the buffer has already been deleted).</li>
   *           </ul>
   */

  @Nonnull ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Unbind the current array buffer.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void arrayBufferUnbind()
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

  void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError;

  /**
   * Unmap the array buffer specified by <code>id</code>.
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

  void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException;
}
