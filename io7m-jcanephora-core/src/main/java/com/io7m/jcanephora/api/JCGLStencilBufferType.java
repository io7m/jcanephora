/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;

/**
 * Simplified interface to the stencil buffer.
 */

public interface JCGLStencilBufferType
{
  /**
   * Clear the stencil buffer with the specified value <code>stencil</code>.
   * 
   * @param stencil
   *          The stencil value.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

  /**
   * Disable the stencil test.
   * 
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  void stencilBufferDisable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

  /**
   * Enable the stencil test.
   * 
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  void stencilBufferEnable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

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
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  void stencilBufferFunction(
    final FaceSelection faces,
    final StencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

  /**
   * @return The number of bits available in the stencil buffer for the
   *         current framebuffer configuration.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  int stencilBufferGetBits()
    throws JCGLException;

  /**
   * @return <code>true</code> iff stencil testing is enabled.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  boolean stencilBufferIsEnabled()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

  /**
   * Control the writing of bits in the stencil buffer for the faces given by
   * <code>faces</code>. The least significant bits in the value
   * <code>mask</code> are used to control writing of bits; Where a 1 appears
   * in the mask, it's possible to write to the corresponding bit in the
   * stencil buffer. Where a 0 appears, the corresponding bit is
   * write-protected. Initially, all bits are enabled for writing.
   * 
   * @param mask
   *          The stencil mask.
   * @param faces
   *          The face selection.
   * 
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLExceptionNoStencilBuffer
   *           If no stencil buffer is available.
   */

  void stencilBufferMask(
    final FaceSelection faces,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer;

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
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   */

  void stencilBufferOperation(
    final FaceSelection faces,
    final StencilOperation stencil_fail,
    final StencilOperation depth_fail,
    final StencilOperation pass)
    throws JCGLException;
}
