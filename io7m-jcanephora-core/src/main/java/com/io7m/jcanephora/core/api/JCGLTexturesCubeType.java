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

import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLCubeMapFaceRH;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;

import java.nio.ByteBuffer;

/**
 * The interface to OpenGL cube textures.
 */

public interface JCGLTexturesCubeType
{
  /**
   * Bind the texture {@code texture} to the texture unit {@code unit}. Any
   * existing bound texture (of any type) is unbound.
   *
   * @param unit    The texture unit
   * @param texture The texture
   *
   * @throws JCGLException Iff an OpenGL error occurs
   * @see JCGLTexturesType#textureUnitUnbind(JCGLTextureUnitType)
   */

  void textureCubeBind(
    JCGLTextureUnitType unit,
    JCGLTextureCubeUsableType texture)
    throws JCGLException;

  /**
   * Deletes the texture referenced by {@code texture}.
   *
   * @param texture The texture
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void textureCubeDelete(
    JCGLTextureCubeType texture)
    throws JCGLException;

  /**
   * @param unit    The texture unit
   * @param texture The texture
   *
   * @return {@code true} iff the texture {@code texture} is bound to the
   * texture unit {@code unit}.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean textureCubeIsBound(
    JCGLTextureUnitType unit,
    JCGLTextureCubeUsableType texture)
    throws JCGLException;

  /**
   * @param texture The texture
   *
   * @return {@code true} iff the texture {@code texture} is bound to any
   * texture unit.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  boolean textureCubeIsBoundAnywhere(
    JCGLTextureCubeUsableType texture)
    throws JCGLException;

  /**
   * <p>Replace the contents (or part of the contents) of the face {@code face}
   * of the cube map texture {@code data.getTexture()} with {@code data},
   * assuming a cube map that uses a left-handed coordinate system (the OpenGL
   * default).</p>
   *
   * <p>If the texture has minification filters that require mipmaps, mipmaps
   * will be generated during the update.</p>
   *
   * @param unit The texture unit that will be used to perform the update
   * @param face The cube face to modify
   * @param data The data to upload
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void textureCubeUpdateLH(
    JCGLTextureUnitType unit,
    JCGLCubeMapFaceLH face,
    JCGLTextureCubeUpdateType data)
    throws JCGLException;

  /**
   * <p>Replace the contents (or part of the contents) of the face {@code face}
   * of the cube map texture {@code data.getTexture()} with {@code data},
   * assuming a cube map that uses a right-handed coordinate system.</p>
   *
   * <p>If the texture has minification filters that require mipmaps, mipmaps
   * will be generated during the update.</p>
   *
   * @param unit The texture unit that will be used to perform the update
   * @param face The cube face to modify
   * @param data The data to upload
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  default void textureCubeUpdateRH(
    final JCGLTextureUnitType unit,
    final JCGLCubeMapFaceRH face,
    final JCGLTextureCubeUpdateType data)
    throws JCGLException
  {
    this.textureCubeUpdateLH(unit, JCGLCubeMapFaceLH.fromRH(face), data);
  }

  /**
   * <p>Allocate a cube texture of width {@code size} and height {@code size} of
   * format {@code format}, binding it to {@code unit}.</p>
   *
   * <p>The texture is wrapped around the {@code r} axis using the wrapping mode
   * {@code wrap_r}, with the OpenGL default being {@code
   * TEXTURE_WRAP_REPEAT}.</p>
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
   * @param size       The width and height in pixels
   * @param format     The texture format
   * @param wrap_r     The method with which to wrap textures around the {@code
   *                   r} axis
   * @param wrap_s     The method with which to wrap textures around the {@code
   *                   s} axis
   * @param wrap_t     The method with which to wrap textures around the {@code
   *                   t} axis
   * @param min_filter The minification filter
   * @param mag_filter The magnification filter
   *
   * @return An allocated texture
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  JCGLTextureCubeType textureCubeAllocate(
    JCGLTextureUnitType unit,
    long size,
    JCGLTextureFormat format,
    JCGLTextureWrapR wrap_r,
    JCGLTextureWrapS wrap_s,
    JCGLTextureWrapT wrap_t,
    JCGLTextureFilterMinification min_filter,
    JCGLTextureFilterMagnification mag_filter)
    throws JCGLException;

  /**
   * <p>Fetch image data for the given face of the given texture.</p>
   *
   * <p>Fetching texture data requires binding and unbinding textures, and the
   * given {@code unit} will be used to perform the retrieval, breaking any
   * associated bindings for that unit. The texture will remain bound to {@code
   * unit} when the function returns.</p>
   *
   * @param unit    The texture unit that will be used to fetch the data
   * @param face    The cube map face that will be fetched
   * @param texture The texture
   *
   * @return The texture image data associated with {@code texture}
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  ByteBuffer textureCubeGetImageLH(
    JCGLTextureUnitType unit,
    JCGLCubeMapFaceLH face,
    JCGLTextureCubeUsableType texture)
    throws JCGLException;

  /**
   * <p>Fetch image data for the given face of the given texture.</p>
   *
   * <p>Fetching texture data requires binding and unbinding textures, and the
   * given {@code unit} will be used to perform the retrieval, breaking any
   * associated bindings for that unit. The texture will remain bound to {@code
   * unit} when the function returns.</p>
   *
   * @param unit    The texture unit that will be used to fetch the data
   * @param face    The cube map face that will be fetched
   * @param texture The texture
   *
   * @return The texture image data associated with {@code texture}
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  default ByteBuffer textureCubeGetImageRH(
    final JCGLTextureUnitType unit,
    final JCGLCubeMapFaceRH face,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    return this.textureCubeGetImageLH(
      unit,
      JCGLCubeMapFaceLH.fromRH(face),
      texture);
  }
}
