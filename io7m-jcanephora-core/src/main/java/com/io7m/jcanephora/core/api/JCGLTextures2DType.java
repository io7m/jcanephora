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
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;

import java.nio.ByteBuffer;

/**
 * The interface to OpenGL 2D textures.
 */

public interface JCGLTextures2DType
{
  /**
   * Bind the texture {@code texture} to the texture unit {@code unit}.
   *
   * @param unit    The texture unit.
   * @param texture The texture.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  void texture2DBind(
    JCGLTextureUnitType unit,
    JCGLTexture2DUsableType texture)
    throws JCGLException;

  /**
   * Deletes the texture referenced by {@code texture}.
   *
   * @param texture The texture.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  void texture2DDelete(
    JCGLTexture2DType texture)
    throws JCGLException;

  /**
   * @param unit    The texture unit.
   * @param texture The texture.
   *
   * @return {@code true} iff the texture {@code texture} is bound to the
   * texture unit {@code unit}.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  boolean texture2DIsBound(
    JCGLTextureUnitType unit,
    JCGLTexture2DUsableType texture)
    throws JCGLException;

  /**
   * Unbind whatever 2D texture is bound to the texture unit {@code unit} (if
   * any).
   *
   * @param unit The texture unit.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  void texture2DUnbind(
    JCGLTextureUnitType unit)
    throws JCGLException;

  /**
   * <p>Allocate a texture of width {@code width} and height {@code height} of
   * format {@code format}, binding it to {@code unit}.</p>
   *
   * <p>The texture is wrapped around the {@code s} axis using the wrapping mode
   * {@code wrap_s}, with the OpenGL default being {@code
   * TEXTURE_WRAP_REPEAT}.</p>
   *
   * <p>The texture is wrapped around the {@code t} axis using the wrapping mode
   * {@code wrap_t}, with the OpenGL default being {@code
   * TEXTURE_WRAP_REPEAT}.</p>
   *
   * <p>The texture is scaled down using the minification filter {@code
   * min_filter}, with the OpenGL default being {@code TEXURE_FILTER_LINEAR}.
   * </p>
   *
   * <p>The texture is scaled up using the magnification filter {@code
   * mag_filter}, with the OpenGL default being {@code TEXTURE_FILTER_LINEAR}.
   * </p>
   *
   * @param unit       The texture unit to which the texture will be initially
   *                   bound
   * @param width      The width in pixels.
   * @param height     The height in pixels.
   * @param format     The texture format
   * @param wrap_s     The method with which to wrap textures around the {@code
   *                   s} axis.
   * @param wrap_t     The method with which to wrap textures around the {@code
   *                   t} axis.
   * @param min_filter The minification filter.
   * @param mag_filter The magnification filter.
   *
   * @return An allocated texture.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  JCGLTexture2DType texture2DAllocate(
    JCGLTextureUnitType unit,
    long width,
    long height,
    JCGLTextureFormat format,
    JCGLTextureWrapS wrap_s,
    JCGLTextureWrapT wrap_t,
    JCGLTextureFilterMinification min_filter,
    JCGLTextureFilterMagnification mag_filter)
    throws JCGLException;

  /**
   * <p>Replace the contents (or part of the contents) of the texture {@code
   * data.getTexture()} with {@code data}.</p>
   *
   * <p>Updating a texture requires binding and unbinding textures, and the
   * given {@code unit} will be used to perform the update, breaking any
   * associated bindings for that unit. The updated texture will remain bound to
   * {@code unit} when the function returns.</p>
   *
   * @param unit The texture unit that will be used to perform the update
   * @param data The data to upload.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  void texture2DUpdate(
    JCGLTextureUnitType unit,
    JCGLTexture2DUpdateType data)
    throws JCGLException;

  /**
   * <p>Fetch image data for the given texture.</p>
   *
   * <p>Fetching texture data requires binding and unbinding textures, and the
   * given {@code unit} will be used to perform the retrieval, breaking any
   * associated bindings for that unit. The texture will remain bound to {@code
   * unit} when the function returns.</p>
   *
   * @param unit    The texture unit that will be used to fetch the data
   * @param texture The texture.
   *
   * @return The texture image data associated with {@code texture}.
   *
   * @throws JCGLException Iff an OpenGL error occurs.
   */

  ByteBuffer texture2DGetImage(
    JCGLTextureUnitType unit,
    JCGLTexture2DUsableType texture)
    throws JCGLException;
}
