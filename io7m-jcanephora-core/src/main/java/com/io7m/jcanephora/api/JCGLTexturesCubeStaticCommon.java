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

package com.io7m.jcanephora.api;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureCubeStaticUsable;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureUnitType;

/**
 * <p>
 * Type-safe interface to the set of cube texture types guaranteed to be
 * supported by all OpenGL implementations.
 * </p>
 */

public interface JCGLTexturesCubeStaticCommon
{
  /**
   * Bind the texture <code>texture</code> to the texture unit
   * <code>unit</code>.
   * 
   * @param unit
   *          The texture unit.
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>unit == null</code>.</li>
   *           <li><code>texture == null</code>.</li>
   *           <li><code>texture</code> does not refer to a valid texture
   *           (possible if the texture has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void textureCubeStaticBind(
    final  TextureUnitType unit,
    final  TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Deletes the texture referenced by <code>texture</code>.
   * 
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null</code>.</li>
   *           <li><code>texture</code> does not refer to a valid texture
   *           (possible if the texture has already been deleted).</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void textureCubeStaticDelete(
    final  TextureCubeStatic texture)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff the texture <code>texture</code> is bound to
   * the texture unit <code>unit</code>.
   * 
   * @param unit
   *          The texture unit.
   * @param texture
   *          The texture. Iff any of the following hold:
   *          <ul>
   *          <li><code>unit == null</code>.</li>
   *          <li><code>texture == null</code>.</li>
   *          <li><code>texture</code> does not refer to a valid texture
   *          (possible if the texture has already been deleted).</li>
   *          </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean textureCubeStaticIsBound(
    final  TextureUnitType unit,
    final  TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Unbind whatever cube map texture is bound to the texture unit
   * <code>unit</code> (if any).
   * 
   * @param unit
   *          The texture unit.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>unit == null</code>.</li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void textureCubeStaticUnbind(
    final  TextureUnitType unit)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Replace the contents (or part of the contents) of the face
   * <code>face</code> of the cube map texture <code>data.getTexture()</code>
   * with <code>data</code>, assuming a cube map that uses a left-handed
   * coordinate system (the OpenGL default).
   * 
   * @param face
   *          The cube face to modify.
   * @param data
   *          The data to upload.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>face == null || data == null</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void textureCubeStaticUpdateLH(
    final  CubeMapFaceLH face,
    final  TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Replace the contents (or part of the contents) of the face
   * <code>face</code> of the cube map texture <code>data.getTexture()</code>
   * with <code>data</code>, assuming a cube map that uses a right-handed
   * coordinate system.
   * 
   * @param face
   *          The cube face to modify.
   * @param data
   *          The data to upload.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>face == null || data == null</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void textureCubeStaticUpdateRH(
    final  CubeMapFaceRH face,
    final  TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException;
}
