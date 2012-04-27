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
   * Allocate a buffer of <code>elements</code> elements, each element
   * consisting of <code>type_elements</code> values of type <code>type</code>
   * .
   * 
   * @param elements
   *          The number of elements in the buffer.
   * @param type_elements
   *          The number of values in a given element.
   * @param type
   *          The type of values in an element.
   * @param hint
   *          A hint to the implementation as to how the buffer will be used.
   * @return A reference to the allocated buffer.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>0 < elements <= Long.MAX_VALUE == false</code>.</li>
   *           <li><code>type == null</code>.</li>
   *           <li><code>0 < type_elements <= Long.MAX_VALUE == false</code>.</li>
   *           </ul>
   */

  @Nonnull PixelUnpackBuffer pixelUnpackBufferAllocate(
    final long elements,
    final @Nonnull GLScalarType type,
    final long type_elements,
    final @Nonnull UsageHint hint)
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

  void pixelUnpackBufferDelete(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped read-only. The buffer should be unmapped
   * after use with
   * {@link GLInterface#pixelUnpackBufferUnmap(PixelUnpackBuffer)}.
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

  @Nonnull ByteBuffer pixelUnpackBufferMapRead(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError;

  /**
   * Map the buffer referenced by <code>id</code> into the program's address
   * space. The buffer is mapped write-only. The buffer should be unmapped
   * after use with
   * {@link GLInterface#pixelUnpackBufferUnmap(PixelUnpackBuffer)}. The
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

  @Nonnull PixelUnpackBufferWritableMap pixelUnpackBufferMapWrite(
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

  void pixelUnpackBufferUnmap(
    final @Nonnull PixelUnpackBuffer id)
    throws ConstraintError,
      GLException;
}
