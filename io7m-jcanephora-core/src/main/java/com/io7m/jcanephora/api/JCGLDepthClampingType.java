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

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;

/**
 * Type-safe interface to depth clamping.
 */

public interface JCGLDepthClampingType
{
  /**
   * Disable depth clamping.
   * 
   * @throws JCGLException
   *           On OpenGL errors.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  void depthClampingDisable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;

  /**
   * Enable depth clamping.
   * 
   * @throws JCGLException
   *           On OpenGL errors.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  void depthClampingEnable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;

  /**
   * @return <code>true</code> if depth clamping is enabled.
   * @throws JCGLException
   *           On OpenGL errors.
   * @throws JCGLExceptionNoDepthBuffer
   *           If no depth buffer is available.
   */

  boolean depthClampingIsEnabled()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer;
}
