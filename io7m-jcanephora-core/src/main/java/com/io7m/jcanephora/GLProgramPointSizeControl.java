/*
 * Copyright © 2012 http://io7m.com
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

/**
 * Control over whether shading programs have control of the size of
 * rasterized points.
 */

public interface GLProgramPointSizeControl
{
  /**
   * Disable control of the size of rasterized points by shading programs.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @see GLProgramPointSizeControl#pointProgramSizeControlEnable()
   */

  void pointProgramSizeControlDisable()
    throws GLException;

  /**
   * Enable control of the size of rasterized points by shading programs.
   * 
   * Shading programs should write the desired size to the built-in GLSL
   * variable <code>gl_PointSize</code>.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void pointProgramSizeControlEnable()
    throws GLException;

  /**
   * Returns <code>true</code> iff control of point size by shading programs
   * is enabled.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean pointProgramSizeControlIsEnabled()
    throws GLException;
}
