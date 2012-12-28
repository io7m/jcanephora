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

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the rasterization section of the pipeline.
 */

public interface GLRasterization
{
  /**
   * Return the maximum width of aliased lines supported by the
   * implementation.
   */

  int lineAliasedGetMaximumWidth();

  /**
   * Return the minimum width of aliased lines supported by the
   * implementation.
   */

  int lineAliasedGetMinimumWidth();

  /**
   * Set the width in pixels of rasterized lines.
   * 
   * @param width
   *          The width in pixels.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>Line smoothing is enabled and
   *           <code>lineSmoothGetMinimumWidth() <= width <= lineSmoothGetMaximumWidth() == false</code>
   *           </li>
   *           <li>Line smoothing is disabled and
   *           <code>lineAliasedGetMinimumWidth() <= width <= lineAliasedGetMaximumWidth() == false</code>
   *           </li>
   *           </ul>
   */

  void lineSetWidth(
    final float width)
    throws GLException,
      ConstraintError;

  /**
   * Return the maximum width of antialiased lines supported by the
   * implementation.
   */

  int lineSmoothGetMaximumWidth();

  /**
   * Return the minimum width of antialiased lines supported by the
   * implementation.
   */

  int lineSmoothGetMinimumWidth();

  /**
   * Disable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingDisable()
    throws GLException;

  /**
   * Enable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingEnable()
    throws GLException;

  /**
   * Return the maximum width of rasterized points supported by the
   * implementation.
   */

  int pointGetMaximumWidth();

  /**
   * Return the minimum width of rasterized points supported by the
   * implementation.
   */

  int pointGetMinimumWidth();
}
