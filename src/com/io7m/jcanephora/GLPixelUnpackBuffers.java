package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to pixel unpack buffers.
 */

public interface GLPixelUnpackBuffers
{
  /**
   * Allocate a buffer of <code>elements</code> elements of size
   * <code>element_size</code>.
   * 
   * @param elements
   *          The number of elements in the buffer.
   * @param element_size
   *          The size of each element in bytes.
   * @return A reference to the allocated buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>0 < elements <= Long.MAX_VALUE == false</code>.</li>
   *           <li><code>0 < element_size <= Long.MAX_VALUE == false</code>.</li>
   *           </ul>
   */

  @Nonnull PixelUnpackBuffer allocatePixelUnpackBuffer(
    final long elements,
    final long element_size)
    throws GLException,
      ConstraintError;

  /**
   * Deletes the buffer referenced by <code>id</code>.
   * 
   * @param id
   *          The pixel unpack buffer.
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

  void deletePixelUnpackBuffer(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with
   * {@link GLInterface#unmapPixelUnpackBuffer(PixelUnpackBuffer)}.
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

  @Nonnull ByteBuffer mapPixelUnpackBufferRead(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with
   * {@link GLInterface#unmapPixelUnpackBuffer(PixelUnpackBuffer)}. The
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

  @Nonnull PixelUnpackBufferWritableMap mapPixelUnpackBufferWrite(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Unmap the pixel unpack buffer specified by <code>id</code>.
   * 
   * @param id
   *          The pixel unpack buffer.
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

  void unmapPixelUnpackBuffer(
    final @Nonnull PixelUnpackBuffer id)
    throws ConstraintError,
      GLException;
}
