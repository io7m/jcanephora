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
 * <p>
 * Type-safe interface to the set of cube texture types guaranteed to be
 * supported by OpenGL 2.1.
 * </p>
 */

public interface JCGLTexturesCubeStaticGL2 extends
  JCGLTexturesCubeStaticCommon
{
  /**
   * Retrieve the texture image data associated with the face
   * <code>face</code> of the left-handed cube texture <code>texture</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null || face == null</code></li>
   *           <li><code>texture</code> has been deleted</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeReadableData textureCubeStaticGetImageLH(
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws ConstraintError,
      JCGLException;

  /**
   * Retrieve the texture image data associated with the face
   * <code>face</code> of the right-handed cube texture <code>texture</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null || face == null</code></li>
   *           <li><code>texture</code> has been deleted</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureCubeReadableData textureCubeStaticGetImageRH(
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceRH face)
    throws ConstraintError,
      JCGLException;
}
