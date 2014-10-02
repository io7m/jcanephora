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

package com.io7m.jcanephora;

import java.util.EnumSet;
import java.util.Set;

import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Information about texture formats.
 */

public final class TextureFormatMeta
{
  /**
   * Check that the texture is of a color-renderable format.
   *
   * @param t
   *          The texture
   * @param version
   *          The OpenGL version
   * @param extensions
   *          The OpenGL extensions
   * @throws JCGLExceptionFormatError
   *           If the texture is not of the correct format
   */

  public static void checkColorRenderableTexture2D(
    final Texture2DStaticUsableType t,
    final JCGLVersion version,
    final JCGLNamedExtensionsType extensions)
    throws JCGLExceptionFormatError
  {
    final TextureFormat format = t.textureGetFormat();
    if (TextureFormatMeta.isColorRenderable2D(format, version, extensions) == false) {
      final String m =
        String.format("Format %s is not color-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture is of a color-renderable format.
   *
   * @param t
   *          The texture
   * @param version
   *          The OpenGL version
   * @param extensions
   *          The OpenGL extensions
   * @throws JCGLExceptionFormatError
   *           If the texture is not of the correct format
   */

  public static void checkColorRenderableTextureCube(
    final TextureCubeStaticUsableType t,
    final JCGLVersion version,
    final JCGLNamedExtensionsType extensions)
    throws JCGLExceptionFormatError
  {
    final TextureFormat format = t.textureGetFormat();
    if (TextureFormatMeta.isColorRenderable2D(format, version, extensions) == false) {
      final String m =
        String.format("Format %s is not color-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture is of a depth (not stencil) renderable format.
   *
   * @param t
   *          The texture
   * @param extensions
   *          The OpenGL extensions
   * @throws JCGLExceptionFormatError
   *           If the texture is not of the correct format
   */

  public static void checkDepthOnlyRenderableTexture2D(
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions)
    throws JCGLExceptionFormatError
  {
    final TextureFormat format = t.textureGetFormat();
    if (TextureFormatMeta.isDepthRenderable2D(format, extensions) == false) {
      final String m =
        String.format("Format %s is not depth-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
    if (TextureFormatMeta.isStencilRenderable(format)) {
      final String m =
        String
          .format(
            "Format %s is stencil-renderable: Must be used as a depth+stencil attachment",
            format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture is of a depth+stencil-renderable format.
   *
   * @param t
   *          The texture
   * @param extensions
   *          The OpenGL extensions
   * @throws JCGLExceptionFormatError
   *           If the texture is not of the correct format
   */

  public static void checkDepthStencilRenderableTexture2D(
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions)
    throws JCGLExceptionFormatError
  {
    final TextureFormat format = t.textureGetFormat();
    if ((TextureFormatMeta.isDepthRenderable2D(format, extensions) == false)
      || (TextureFormatMeta.isStencilRenderable(format) == false)) {
      final String m =
        String.format("Format %s is not depth+stencil-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * @param format
   *          The texture format.
   * @return The number of depth bits in the given texture format.
   */

  public static int getDepthBits(
    final TextureFormat format)
  {
    switch (format) {
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return 0;
      }
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      {
        return 32;
      }
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      {
        return 16;
      }
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      {
        return 24;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param format
   *          The texture format.
   * @return The number of stencil bits in the given texture format.
   */

  public static int getStencilBits(
    final TextureFormat format)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return 0;
      }
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      {
        return 8;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL 2.1 contexts.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGL21()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);
    s.add(TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP);
    s.add(TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP);
    return s;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL 3.* contexts.
   */

  public static Set<TextureFormat> getTextures2DRequiredByGL3()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      switch (t) {

      /**
       * Not required by GL3.
       */

        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        case TEXTURE_FORMAT_RGB_565_2BPP:
        {
          break;
        }

        /**
         * See page 119, 3.8 "Required Texture Formats" of the OpenGL 3.1
         * specification.
         */

        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:

        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_RGBA_8_4BPP:

        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:

        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:

        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        {
          s.add(t);
        }
      }
    }

    assert s != null;
    return s;
  }

  /**
   * @return The set of 2D texture formats required by the common subset of
   *         GL3 and ES3.
   */

  public static Set<TextureFormat> getTextures2DRequiredByGL3ES3()
  {
    final Set<TextureFormat> gl3 =
      TextureFormatMeta.getTextures2DRequiredByGL3();
    final Set<TextureFormat> es3 =
      TextureFormatMeta.getTextures2DRequiredByGLES3();
    final Set<TextureFormat> all = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    assert all != null;
    return all;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL ES2 contexts.
   */

  public static Set<TextureFormat> getTextures2DRequiredByGLES2()
  {
    final Set<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    /**
     * Color-renderable formats implicitly required by section 4.4.5 of the
     * ES2 standard.
     */

    s.add(TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP);
    s.add(TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP);
    s.add(TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP);
    return s;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL ES 3 contexts.
   */

  public static Set<TextureFormat> getTextures2DRequiredByGLES3()
  {
    final Set<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      switch (t) {

      /**
       * See page 126, 3.8 "Required Texture Formats" of the OpenGL ES 3
       * specification.
       */

        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        case TEXTURE_FORMAT_RGB_565_2BPP:

        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:

        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:

        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_RGBA_8_4BPP:

        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:

        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:

        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:

        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:

        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:

        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        {
          s.add(t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        {
          break;
        }
      }
    }

    assert s != null;
    return s;
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL 2.1 contexts.
   */

  public static Set<TextureFormat> getTexturesCubeRequiredByGL21()
  {
    return TextureFormatMeta.getTextures2DRequiredByGL21();
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL 3.* contexts.
   */

  public static Set<TextureFormat> getTexturesCubeRequiredByGL3()
  {
    return TextureFormatMeta.getTextures2DRequiredByGL3();
  }

  /**
   * @return The set of cube texture formats required by the common subset of
   *         GL3 and ES3.
   */

  public static Set<TextureFormat> getTexturesCubeRequiredByGL3ES3()
  {
    final Set<TextureFormat> gl3 =
      TextureFormatMeta.getTexturesCubeRequiredByGL3();
    final Set<TextureFormat> es3 =
      TextureFormatMeta.getTexturesCubeRequiredByGLES3();
    final Set<TextureFormat> all = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    assert all != null;
    return all;
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL ES 2 contexts.
   */

  public static Set<TextureFormat> getTexturesCubeRequiredByGLES2()
  {
    return TextureFormatMeta.getTextures2DRequiredByGLES2();
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL ES 3 contexts.
   */

  public static Set<TextureFormat> getTexturesCubeRequiredByGLES3()
  {
    return TextureFormatMeta.getTextures2DRequiredByGLES3();
  }

  /**
   * @return All texture formats that have <code>i</code> components.
   *
   * @param i
   *          The number of components.
   */

  public static Set<TextureFormat> getTexturesWithComponents(
    final int i)
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (t.getComponentCount() == i) {
        s.add(t);
      }
    }

    assert s != null;
    return s;
  }

  /**
   * @return <code>true</code> iff the given 2D texture format is
   *         color-renderable on the given version of OpenGL assuming
   *         <code>extensions</code>.
   *
   * @param format
   *          The texture format.
   * @param version
   *          The OpenGL version.
   * @param extensions
   *          The available extensions.
   */

  public static boolean isColorRenderable2D(
    final TextureFormat format,
    final JCGLVersion version,
    final JCGLNamedExtensionsType extensions)
  {
    switch (version.getAPI()) {
      case JCGL_ES:
        if (version.getVersionMajor() >= 3) {
          return TextureFormatMeta.isColorRenderable2DES3(format, extensions);
        }
        return TextureFormatMeta.isColorRenderable2DES2(format, extensions);
      case JCGL_FULL:
        return TextureFormatMeta.isColorRenderable2DGL3(format, extensions);
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param extensions
   *          Unused currently.
   */

  private static boolean isColorRenderable2DES2(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      {
        return true;
      }
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * See OpenGL ES 3.0 specification, page 126 "Required texture formats".
   */

  private static boolean isColorRenderable2DES3(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      {
        return false;
      }
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:

      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:

      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:

      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return true;
      }

      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      {
        return extensions
          .extensionIsVisible(JCGLExtensionNames.GL_EXT_COLOR_BUFFER_FLOAT);
      }

      case TEXTURE_FORMAT_RGBA_16_8BPP:

      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:

      case TEXTURE_FORMAT_RG_16_4BPP:

      case TEXTURE_FORMAT_R_16_2BPP:

      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * See the OpenGL 3.1 standard, page 119 "Required texture formats".
   *
   * @param extensions
   *          Unused currently.
   */

  private static boolean isColorRenderable2DGL3(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return true;
      }

      /**
       * This is not actually required by OpenGL 3.*, but in practice, it's
       * color-renderable everywhere.
       */

      case TEXTURE_FORMAT_RGB_8_3BPP:
      {
        return true;
      }

      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return <code>true</code> iff the given 2D texture format is
   *         depth-renderable.
   * @param format
   *          The texture format.
   * @param extensions
   *          The current extensions.
   */

  public static boolean isDepthRenderable2D(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      {
        return true;
      }

      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param format
   *          The texture format.
   * @return <code>true</code> if the given texture format is floating point.
   */

  public static boolean isFloatingPoint(
    final TextureFormat format)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      {
        return true;
      }
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return <code>true</code> iff the given 2D texture format is
   *         stencil-renderable.
   * @param format
   *          The texture format.
   */

  public static boolean isStencilRenderable(
    final TextureFormat format)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_4444_2BPP:
      case TEXTURE_FORMAT_RGBA_5551_2BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_565_2BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      {
        return false;
      }
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      {
        return true;
      }
    }

    throw new UnreachableCodeException();
  }

  private TextureFormatMeta()
  {
    throw new UnreachableCodeException();
  }
}
