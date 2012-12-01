package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to memory-mapped index buffers.
 */

public interface GLIndexBuffersMapped
{
  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with {@link GLInterface#indexBufferUnmap(IndexBuffer)}. Note
   * that the type of indices in the buffer is given by
   * <code>id.getType()</code>.
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws GLException
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
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with {@link GLInterface#indexBufferUnmap(IndexBuffer)}. Note
   * that the type of indices in the buffer is given by
   * <code>id.getType()</code>. The previous contents of the buffer are
   * discarded to prevent pipeline stalls.
   * 
   * @param id
   *          The buffer.
   * @return A readable byte buffer.
   * @throws GLException
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
    throws GLException,
      ConstraintError;

  /**
   * Unmap the index buffer specified by <code>id</code>.
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

  public void indexBufferUnmap(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException;
}
