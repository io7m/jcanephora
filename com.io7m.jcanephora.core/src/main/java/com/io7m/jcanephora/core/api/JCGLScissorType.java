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
import com.io7m.jregions.core.unparameterized.areas.AreaL;

/**
 * OpenGL scissor test interface.
 */

public interface JCGLScissorType
{
  /**
   * Disable the scissor test in the OpenGL pipeline. The scissor test is
   * initially disabled.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void scissorDisable()
    throws JCGLException;

  /**
   * Set the OpenGL scissor region to the given inclusive area. The dimensions
   * and position are specified in pixels and {@code (0, 0)} refers to the
   * bottom left corner of the viewport.
   *
   * @param area The inclusive area
   *
   * @throws JCGLException Iff an OpenGL error occurred
   */

  void scissorEnable(
    final AreaL area)
    throws JCGLException;

  /**
   * @return {@code true} iff scissor testing is enabled.
   *
   * @throws JCGLException Iff an OpenGL error occurred
   */

  boolean scissorIsEnabled()
    throws JCGLException;
}
