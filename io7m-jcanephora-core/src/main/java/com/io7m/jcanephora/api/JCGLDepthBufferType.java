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

import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;

/**
 * Type-safe interface to the depth buffer.
 */

public interface JCGLDepthBufferType
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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  void depthBufferClear(
    final float depth)
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;

  /**
   * @return The number of bits available in the depth buffer for the current
   *         framebuffer configuration.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  int depthBufferGetBits()
    throws JCGLException;

  /**
   * Disable depth testing.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  void depthBufferTestDisable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;

  /**
   * Enable depth testing with the given <code>function</code>. The OpenGL
   * default for <code>function</code> is <code>LESS_THAN</code>.
   * 
   * @param function
   *          The depth function.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  void depthBufferTestEnable(
    final DepthFunction function)
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;

  /**
   * @return <code>true</code> iff depth testing is enabled.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean depthBufferTestIsEnabled()
    throws JCGLException;

  /**
   * Disable writing to the depth buffer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferWriteDisable()
    throws JCGLException;

  /**
   * Enable writing to the depth buffer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  void depthBufferWriteEnable()
    throws JCGLException;

  /**
   * @return <code>true</code> iff depth writing is enabled.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  boolean depthBufferWriteIsEnabled()
    throws JCGLException;
}
