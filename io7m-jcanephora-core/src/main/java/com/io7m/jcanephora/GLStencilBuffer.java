package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the stencil buffer.
 */

public interface GLStencilBuffer
{
  /**
   * Clear the stencil buffer with the specified value <code>stencil</code>.
   * 
   * @param stencil
   *          The stencil value.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff no stencil buffer is available.
   */

  void stencilBufferClear(
    final int stencil)
    throws GLException,
      ConstraintError;

  /**
   * Disable the stencil test.
   */

  void stencilBufferDisable()
    throws ConstraintError,
      GLException;

  /**
   * Enable the stencil test.
   * 
   * @throws ConstraintError
   *           Iff no stencil buffer exists (
   *           <code>stencilBufferGetBits() == 0</code>).
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   */

  void stencilBufferEnable()
    throws ConstraintError,
      GLException;

  /**
   * Set the stencil function and reference value for the faces given by
   * <code>faces</code>. The value <code>mask</code> is ANDed with the
   * reference value and the stored stencil buffer value when
   * <code>function</code> is evaluated.
   * 
   * @param faces
   *          The face selection.
   * @param function
   *          The stencil function.
   * @param reference
   *          The reference value.
   * @param mask
   *          The value to AND with the reference and stored stencil values.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   */

  void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException;

  /**
   * Retrieve the number of bits available in the stencil buffer for the
   * current framebuffer configuration.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  int stencilBufferGetBits()
    throws GLException;

  /**
   * Return <code>true</code> iff depth testing is enabled.
   */

  boolean stencilBufferIsEnabled()
    throws GLException;

  /**
   * Control the writing of bits in the stencil buffer for the faces given by
   * <code>faces</code>. The least significant bits in the value
   * <code>mask</code> are used to control writing of bits; Where a 1 appears
   * in the mask, it's possible to write to the corresponding bit in the
   * stencil buffer. Where a 0 appears, the corresponding bit is
   * write-protected. Initially, all bits are enabled for writing.
   * 
   * @param faces
   *          The face selection.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   */

  void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException;

  /**
   * Configure the operations to be performed on the stencil buffer for the
   * faces given by <code>faces</code>
   * 
   * @param faces
   *          The face selection.
   * @param stencil_fail
   *          The operation to perform when the stencil test fails.
   * @param depth_fail
   *          The operation to perform when the stencil test passes but the
   *          depth test fails.
   * @param pass
   *          The operation to perform when the stencil and depth test (if
   *          any) passes.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   */

  void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException;
}
