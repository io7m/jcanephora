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
 * Type-safe interface to the set of texture types guaranteed to be supported
 * by all OpenGL implementations.
 * </p>
 */

public interface JCGLTextures2DStaticCommon
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

  void texture2DStaticBind(
    final  TextureUnit unit,
    final  Texture2DStaticUsable texture)
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

  void texture2DStaticDelete(
    final  Texture2DStatic texture)
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

  boolean texture2DStaticIsBound(
    final  TextureUnit unit,
    final  Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Unbind whatever 2D texture is bound to the texture unit <code>unit</code>
   * (if any).
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

  void texture2DStaticUnbind(
    final  TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Replace the contents (or part of the contents) of the texture
   * <code>data.getTexture()</code> with <code>data</code>.
   * 
   * @param data
   *          The data to upload.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>data == null</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void texture2DStaticUpdate(
    final  Texture2DWritableData data)
    throws ConstraintError,
      JCGLRuntimeException;
}
