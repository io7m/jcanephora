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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the scissor test.
 */

public interface JCGLScissor
{
  /**
   * Disable the scissor test in the OpenGL pipeline. The scissor test is
   * initially disabled.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void scissorDisable()
    throws JCGLRuntimeException;

  /**
   * Set the OpenGL scissor region to the given inclusive area. The dimensions
   * and position are specified in pixels and <code>(0, 0)</code> refers to
   * the bottom left corner of the viewport.
   * 
   * @param area
   *          The inclusive area
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>area == null</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurred.
   */

  void scissorEnable(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff scissor testing is enabled.
   */

  boolean scissorIsEnabled()
    throws JCGLRuntimeException;
}
