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
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * Simplified interface to the color buffer.
 */

public interface JCGLColorBuffer
{
  /**
   * Clear the color buffer with the color specified by
   * <code>(r,g,b,1.0)</code> .
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Clear the color buffer with the color specified by <code>(r,g,b,a)</code>
   * .
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Clear the color buffer with the color specified by <code>color</code>.
   * The alpha channel of the buffer is set to 1.0.
   * 
   * @param color
   *          The color to use.
   * @throws ConstraintError
   *           Iff <code>color == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClearV3f(
    final VectorReadable3F color)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Clear the color buffer with the color specified by <code>color</code>.
   * 
   * @param color
   *          The color to use.
   * @throws ConstraintError
   *           Iff <code>color == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferClearV4f(
    final VectorReadable4F color)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Enable/disable writing to the given channels of the current color buffer.
   * A value of <code>true</code> for a given channel indicates that data will
   * be written to that channel.
   * 
   * @param r
   *          Enable/disable writing to the red channel of the color buffer.
   * @param g
   *          Enable/disable writing to the green channel of the color buffer.
   * @param b
   *          Enable/disable writing to the blue channel of the color buffer.
   * @param a
   *          Enable/disable writing to the alpha channel of the color buffer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the alpha channel.
   */

  boolean colorBufferMaskStatusAlpha()
    throws JCGLRuntimeException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the blue channel.
   */

  boolean colorBufferMaskStatusBlue()
    throws JCGLRuntimeException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the green channel.
   */

  boolean colorBufferMaskStatusGreen()
    throws JCGLRuntimeException;

  /**
   * Return <code>true</code> iff writing to the current color buffer will
   * write to the red channel.
   */

  boolean colorBufferMaskStatusRed()
    throws JCGLRuntimeException;
}
