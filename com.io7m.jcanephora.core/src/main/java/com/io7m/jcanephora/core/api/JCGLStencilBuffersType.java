/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;

/**
 * OpenGL interface to stencil buffers.
 */

public interface JCGLStencilBuffersType
{
  /**
   * Clear the stencil buffer with the specified value {@code stencil}.
   *
   * @param stencil The stencil value.
   *
   * @throws JCGLException                Iff an OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  void stencilBufferClear(
    final int stencil)
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * Disable the stencil test.
   *
   * @throws JCGLException                Iff an internal OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  void stencilBufferDisable()
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * Enable the stencil test.
   *
   * @throws JCGLException                Iff an internal OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  void stencilBufferEnable()
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * Set the stencil function and reference value for the faces given by {@code
   * faces}. The value {@code mask} is ANDed with the reference value and the
   * stored stencil buffer value when {@code function} is evaluated.
   *
   * @param faces     The face selection
   * @param function  The stencil function
   * @param reference The reference value
   * @param mask      The value to AND with the reference and stored stencil
   *                  values
   *
   * @throws JCGLException                Iff an internal OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  void stencilBufferFunction(
    final JCGLFaceSelection faces,
    final JCGLStencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * @return The number of bits available in the stencil buffer for the current
   * framebuffer configuration
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  int stencilBufferGetBits()
    throws JCGLException;

  /**
   * @return {@code true} iff stencil testing is enabled
   *
   * @throws JCGLException                Iff an OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  boolean stencilBufferIsEnabled()
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * Control the writing of bits in the stencil buffer for the faces given by
   * {@code faces}. The least significant bits in the value {@code mask} are
   * used to control writing of bits; Where a 1 appears in the mask, it's
   * possible to write to the corresponding bit in the stencil buffer. Where a 0
   * appears, the corresponding bit is write-protected. Initially, all bits are
   * enabled for writing.
   *
   * @param mask  The stencil mask
   * @param faces The face selection
   *
   * @throws JCGLException                Iff an internal OpenGL error occurs
   * @throws JCGLExceptionNoStencilBuffer If no stencil buffer is available
   */

  void stencilBufferMask(
    final JCGLFaceSelection faces,
    final int mask)
    throws JCGLException,
    JCGLExceptionNoStencilBuffer;

  /**
   * Configure the operations to be performed on the stencil buffer for the
   * faces given by {@code faces}.
   *
   * @param faces        The face selection
   * @param stencil_fail The operation to perform when the stencil test fails
   * @param depth_fail   The operation to perform when the stencil test passes
   *                     but the depth test fails
   * @param pass         The operation to perform when the stencil and depth
   *                     test (if any) passes
   *
   * @throws JCGLException Iff an internal OpenGL error occurs
   */

  void stencilBufferOperation(
    final JCGLFaceSelection faces,
    final JCGLStencilOperation stencil_fail,
    final JCGLStencilOperation depth_fail,
    final JCGLStencilOperation pass)
    throws JCGLException;

  /**
   * @return The mask value specified for front faces
   */

  int stencilBufferGetMaskFrontFaces();

  /**
   * @return The mask value specified for back faces
   */

  int stencilBufferGetMaskBackFaces();

  /**
   * @return The operation specified for back faces that fail the stencil test
   */

  JCGLStencilOperation stencilBufferGetOperationStencilFailBack();

  /**
   * @return The operation specified for back faces that fail the depth test
   */

  JCGLStencilOperation stencilBufferGetOperationDepthFailBack();

  /**
   * @return The operation specified for back faces that pass all tests
   */

  JCGLStencilOperation stencilBufferGetOperationPassBack();

  /**
   * @return The operation specified for front faces that fail the stencil test
   */

  JCGLStencilOperation stencilBufferGetOperationStencilFailFront();

  /**
   * @return The operation specified for front faces that fail the depth test
   */

  JCGLStencilOperation stencilBufferGetOperationDepthFailFront();

  /**
   * @return The operation specified for front faces that pass all tests
   */

  JCGLStencilOperation stencilBufferGetOperationPassFront();

  /**
   * @return The stencil function for front faces
   */

  JCGLStencilFunction stencilBufferGetFunctionFront();

  /**
   * @return The stencil function for back faces
   */

  JCGLStencilFunction stencilBufferGetFunctionBack();

  /**
   * @return The stencil function reference value for front faces
   */

  int stencilBufferGetFunctionReferenceFront();

  /**
   * @return The stencil function mask value for front faces
   */

  int stencilBufferGetFunctionMaskFront();

  /**
   * @return The stencil function reference value for back faces
   */

  int stencilBufferGetFunctionReferenceBack();

  /**
   * @return The stencil function mask value for back faces
   */

  int stencilBufferGetFunctionMaskBack();
}
