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

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNoDepthBuffer;

/**
 * The interface to OpenGL depth buffers.
 */

public interface JCGLDepthBuffersType extends JCGLDepthClampingType
{
  /**
   * <p>Clear the depth buffer with the specified value {@code depth}.</p>
   *
   * <p>Clearing will have no effect if writing to the depth buffer is
   * disabled.</p>
   *
   * @param depth The depth value
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   * @see #depthBufferWriteIsEnabled()
   * @see #depthBufferWriteEnable()
   * @see #depthBufferWriteDisable()
   */

  void depthBufferClear(
    final float depth)
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * @return The number of bits available in the depth buffer for the current
   * framebuffer configuration.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  int depthBufferGetBits()
    throws JCGLException;

  /**
   * Disable depth testing.
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  void depthBufferTestDisable()
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * Enable depth testing with the given {@code function}. The OpenGL default
   * for {@code function} is {@code LESS_THAN}.
   *
   * @param function The depth function
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  void depthBufferTestEnable(
    final JCGLDepthFunction function)
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * @return {@code true} iff depth testing is enabled.
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  boolean depthBufferTestIsEnabled()
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * Disable writing to the depth buffer.
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  void depthBufferWriteDisable()
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * Enable writing to the depth buffer.
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  void depthBufferWriteEnable()
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;

  /**
   * @return {@code true} iff depth writing is enabled.
   *
   * @throws JCGLException              Iff an OpenGL error occurs
   * @throws JCGLExceptionNoDepthBuffer If no depth buffer is available
   */

  boolean depthBufferWriteIsEnabled()
    throws JCGLException,
    JCGLExceptionNoDepthBuffer;
}
