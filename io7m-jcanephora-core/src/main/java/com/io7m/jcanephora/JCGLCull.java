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
 * Simplified and type-safe interface to OpenGL face culling.
 */

public interface JCGLCull
{
  /**
   * Disable face culling.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void cullingDisable()
    throws JCGLRuntimeException;

  /**
   * Enable face culling for <code>faces</code> and specify that front faces
   * are specified by giving vertices in the winding order specified by
   * <code>order</code>. The OpenGL defaults are <code>FACE_CULL_BACK</code>
   * and <code>FRONT_FACE_COUNTER_CLOCKWISE</code> for <code>faces</code> and
   * <code>order</code> respectively.
   * 
   * @param faces
   *          The faces to cull.
   * @param order
   *          The order of vertices in front-facing faces.
   * @throws ConstraintError
   *           Iff <code>faces == null | order == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff culling is enabled.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean cullingIsEnabled()
    throws JCGLRuntimeException;
}
