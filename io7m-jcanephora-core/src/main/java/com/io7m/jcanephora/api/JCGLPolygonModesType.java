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

import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.PolygonMode;

/**
 * Simplified interface to polygon modes.
 */

public interface JCGLPolygonModesType
{
  /**
   * @return The <code>PolygonMode</code> used for polygons.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  PolygonMode polygonGetMode()
    throws JCGLRuntimeException;

  /**
   * Set the polygon rasterization mode for the polygons to <code>mode</code>.
   * The OpenGL default is <code>POLYGON_FILL</code>.
   * 
   * @param mode
   *          The rasterization mode.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void polygonSetMode(
    final PolygonMode mode)
    throws JCGLRuntimeException;
}
