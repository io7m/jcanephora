package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to memory-mapped array buffers.
 */

public interface GLArrayBuffersMapped
{
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