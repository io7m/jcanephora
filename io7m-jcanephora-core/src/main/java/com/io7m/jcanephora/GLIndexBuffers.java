package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to index/element buffers.
 */

public interface GLIndexBuffers
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code>.</li>
   *           <li><code>0 < indices <= Integer.MAX_VALUE == false</code>.</li>
   *           </ul>
   */

  @Nonnull IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError;

  /**
   * Allocate a buffer of <code>indices</code> indices of type
   * <code>type</code>.
   * 
   * @param indices
   *          The number of indices.
   * @param type
   *          The type of indices.
   * @return A reference to the allocated buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>type == null</code>.</li>
   *           <li><code>0 < indices <= Integer.MAX_VALUE == false</code>.</li>
   *           </ul>
   */

  @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError;

  /**
   * Deletes the index buffer referenced by <code>id</code>.
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException;

  /**
   * Replace the contents (or part of the contents) of the index buffer
   * <code>buffer</code> with <code>data</code>.
   * 
   * @param buffer
   *          The index buffer.
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

  public void indexBufferUpdate(
    final @Nonnull IndexBuffer buffer,
    final @Nonnull IndexBufferWritableData data)
    throws GLException,
      ConstraintError;
}
