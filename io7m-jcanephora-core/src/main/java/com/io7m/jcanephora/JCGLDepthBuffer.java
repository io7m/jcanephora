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

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the depth buffer.
 */

public interface JCGLDepthBuffer
{
  /**
   * <p>
   * Clear the depth buffer with the specified value <code>depth</code>.
   * </p>
   * <p>
   * Clearing will have no effect if writing to the depth buffer is disabled.
   * </p>
   * 
   * @see #depthBufferWriteIsEnabled()
   * @see #depthBufferWriteEnable()
   * @see #depthBufferWriteDisable()
   * 
   * @param depth
   *          The depth value.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff no depth buffer is available.
   */

  void depthBufferClear(
    final float depth)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * Retrieve the number of bits available in the depth buffer for the current
   * framebuffer configuration.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  int depthBufferGetBits()
    throws JCGLRuntimeException;

  /**
   * Disable depth testing.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferTestDisable()
    throws JCGLRuntimeException;

  /**
   * Enable depth testing with the given <code>function</code>. The OpenGL
   * default for <code>function</code> is <code>LESS_THAN</code>.
   * 
   * @param function
   *          The depth function.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>No depth buffer is available (
   *           <code>depthBufferGetBits() == 0</code>).</li>
   *           <li><code>function == null</code>.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferTestEnable(
    final DepthFunction function)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff depth testing is enabled.
   */

  boolean depthBufferTestIsEnabled()
    throws JCGLRuntimeException;

  /**
   * Disable writing to the depth buffer.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Enable writing to the depth buffer.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>No depth buffer is available (
   *           <code>depthBufferGetBits() == 0</code>).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff depth writing is enabled.
   */

  boolean depthBufferWriteIsEnabled()
    throws JCGLRuntimeException;
}
