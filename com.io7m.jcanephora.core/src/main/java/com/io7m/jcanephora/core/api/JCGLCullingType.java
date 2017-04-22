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
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;

/**
 * OpenGL face culling.
 */

public interface JCGLCullingType
{
  /**
   * Disable face culling.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void cullingDisable()
    throws JCGLException;

  /**
   * Enable face culling for {@code faces} and specify that front faces are
   * specified by giving vertices in the winding order specified by {@code
   * order}. The OpenGL defaults are {@code FACE_BACK} and {@code
   * FRONT_FACE_COUNTER_CLOCKWISE} for {@code faces} and {@code order}
   * respectively.
   *
   * @param faces The faces to cull
   * @param order The order of vertices in front-facing faces
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void cullingEnable(
    final JCGLFaceSelection faces,
    final JCGLFaceWindingOrder order)
    throws JCGLException;

  /**
   * @return {@code true} iff culling is enabled.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean cullingIsEnabled()
    throws JCGLException;
}
