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

import com.io7m.junreachable.UnreachableCodeException;

/**
 * Information about texture formats.
 */

public final class TextureFormatMeta
{
  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL 2.1 contexts.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGL21()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);
    s.add(TextureFormat.TEXTURE_TYPE_RGBA_8_4BPP);
    s.add(TextureFormat.TEXTURE_TYPE_RGB_8_3BPP);
    return s;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL 3.* contexts.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGL3()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      switch (t) {

      /**
       * Not required by GL3.
       */

        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          break;
        }

        /**
         * See page 119, 3.8 "Required Texture Formats" of the OpenGL 3.1
         * specification.
         */

        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:

        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGBA_8_4BPP:

        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RGB_8_3BPP:

        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_RG_8_2BPP:

        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        {
          s.add(t);
        }
      }
    }

    return s;
  }

  /**
   * @return The set of 2D texture formats required by the common subset of
   *         GL3 and ES3.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGL3ES3()
  {
    final EnumSet<TextureFormat> gl3 =
      TextureFormatMeta.getTextures2DRequiredByGL3();
    final EnumSet<TextureFormat> es3 =
      TextureFormatMeta.getTextures2DRequiredByGLES3();
    final EnumSet<TextureFormat> all = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    return all;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL ES2 contexts.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGLES2()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    /**
     * Color-renderable formats implicitly required by section 4.4.5 of the
     * ES2 standard.
     */

    s.add(TextureFormat.TEXTURE_TYPE_RGBA_4444_2BPP);
    s.add(TextureFormat.TEXTURE_TYPE_RGBA_5551_2BPP);
    s.add(TextureFormat.TEXTURE_TYPE_RGB_565_2BPP);
    return s;
  }

  /**
   * @return The set of 2D texture formats guaranteed to be available on
   *         OpenGL ES 3 contexts.
   */

  public static EnumSet<TextureFormat> getTextures2DRequiredByGLES3()
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      switch (t) {

      /**
       * See page 126, 3.8 "Required Texture Formats" of the OpenGL ES 3
       * specification.
       */

        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:

        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:

        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:

        case TEXTURE_TYPE_RGBA_32F_16BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGBA_8_4BPP:

        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:

        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RGB_8_3BPP:

        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:

        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_RG_8_2BPP:

        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:

        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        {
          s.add(t);
          break;
        }

        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        {
          break;
        }
      }
    }

    return s;
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL 2.1 contexts.
   */

  public static EnumSet<TextureFormat> getTexturesCubeRequiredByGL21()
  {
    return TextureFormatMeta.getTextures2DRequiredByGL21();
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL 3.* contexts.
   */

  public static EnumSet<TextureFormat> getTexturesCubeRequiredByGL3()
  {
    return TextureFormatMeta.getTextures2DRequiredByGL3();
  }

  /**
   * @return The set of cube texture formats required by the common subset of
   *         GL3 and ES3.
   */

  public static EnumSet<TextureFormat> getTexturesCubeRequiredByGL3ES3()
  {
    final EnumSet<TextureFormat> gl3 =
      TextureFormatMeta.getTexturesCubeRequiredByGL3();
    final EnumSet<TextureFormat> es3 =
      TextureFormatMeta.getTexturesCubeRequiredByGLES3();
    final EnumSet<TextureFormat> all = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    return all;
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL ES 2 contexts.
   */

  public static EnumSet<TextureFormat> getTexturesCubeRequiredByGLES2()
  {
    return TextureFormatMeta.getTextures2DRequiredByGLES2();
  }

  /**
   * @return The set of cube texture formats guaranteed to be available on
   *         OpenGL ES 3 contexts.
   */

  public static EnumSet<TextureFormat> getTexturesCubeRequiredByGLES3()
  {
    return TextureFormatMeta.getTextures2DRequiredByGLES3();
  }

  /**
   * @return All texture formats that have <code>i</code> components.
   */

  public static EnumSet<TextureFormat> getTexturesWithComponents(
    final int i)
  {
    final EnumSet<TextureFormat> s = EnumSet.noneOf(TextureFormat.class);

    for (final TextureFormat t : TextureFormat.values()) {
      if (t.getComponentCount() == i) {
        s.add(t);
      }
    }

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
          return TextureFormatMeta
            .isColorRenderable2D_ES3(format, extensions);
        }
        return TextureFormatMeta.isColorRenderable2D_ES2(format, extensions);
      case JCGL_FULL:
        return TextureFormatMeta.isColorRenderable2D_GL3(format, extensions);
    }

    throw new UnreachableCodeException();
  }

  private static boolean isColorRenderable2D_ES2(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return true;
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * See OpenGL ES 3.0 specification, page 126 "Required texture formats".
   */

  private static boolean isColorRenderable2D_ES3(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return false;
      }
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:

      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:

      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:

      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return true;
      }

      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      {
        return extensions
          .extensionIsVisible(JCGLExtensionNames.GL_EXT_COLOR_BUFFER_FLOAT);
      }

      case TEXTURE_TYPE_RGBA_16_8BPP:

      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:

      case TEXTURE_TYPE_RG_16_4BPP:

      case TEXTURE_TYPE_R_16_2BPP:

      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * See the OpenGL 3.1 standard, page 119 "Required texture formats".
   */

  private static boolean isColorRenderable2D_GL3(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return true;
      }

      /**
       * This is not actually required by OpenGL 3.*, but in practice, it's
       * color-renderable everywhere.
       */

      case TEXTURE_TYPE_RGB_8_3BPP:
      {
        return true;
      }

      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Return <code>true</code> iff the given 2D texture format is
   * depth-renderable.
   */

  public static boolean isDepthRenderable2D(
    final TextureFormat format,
    final JCGLNamedExtensionsType extensions)
  {
    switch (format) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return true;
      }

      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  public static boolean isFloatingPoint(
    final TextureFormat format)
  {
    switch (format) {
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_16F_8BPP:
      case TEXTURE_TYPE_RGBA_32F_16BPP:
      case TEXTURE_TYPE_RGB_16F_6BPP:
      case TEXTURE_TYPE_RGB_32F_12BPP:
      case TEXTURE_TYPE_RG_16F_4BPP:
      case TEXTURE_TYPE_RG_32F_8BPP:
      case TEXTURE_TYPE_R_16F_2BPP:
      case TEXTURE_TYPE_R_32F_4BPP:
      {
        return true;
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_TYPE_RGBA_1010102_4BPP:
      case TEXTURE_TYPE_RGBA_16I_8BPP:
      case TEXTURE_TYPE_RGBA_16U_8BPP:
      case TEXTURE_TYPE_RGBA_16_8BPP:
      case TEXTURE_TYPE_RGBA_32I_16BPP:
      case TEXTURE_TYPE_RGBA_32U_16BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8I_4BPP:
      case TEXTURE_TYPE_RGBA_8U_4BPP:
      case TEXTURE_TYPE_RGBA_8_4BPP:
      case TEXTURE_TYPE_RGB_16I_6BPP:
      case TEXTURE_TYPE_RGB_16U_6BPP:
      case TEXTURE_TYPE_RGB_16_6BPP:
      case TEXTURE_TYPE_RGB_32I_12BPP:
      case TEXTURE_TYPE_RGB_32U_12BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_8I_3BPP:
      case TEXTURE_TYPE_RGB_8U_3BPP:
      case TEXTURE_TYPE_RGB_8_3BPP:
      case TEXTURE_TYPE_RG_16I_4BPP:
      case TEXTURE_TYPE_RG_16U_4BPP:
      case TEXTURE_TYPE_RG_16_4BPP:
      case TEXTURE_TYPE_RG_32I_8BPP:
      case TEXTURE_TYPE_RG_32U_8BPP:
      case TEXTURE_TYPE_RG_8I_2BPP:
      case TEXTURE_TYPE_RG_8U_2BPP:
      case TEXTURE_TYPE_RG_8_2BPP:
      case TEXTURE_TYPE_R_16I_2BPP:
      case TEXTURE_TYPE_R_16U_2BPP:
      case TEXTURE_TYPE_R_16_2BPP:
      case TEXTURE_TYPE_R_32I_4BPP:
      case TEXTURE_TYPE_R_32U_4BPP:
      case TEXTURE_TYPE_R_8I_1BPP:
      case TEXTURE_TYPE_R_8U_1BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  private TextureFormatMeta()
  {
    throw new UnreachableCodeException();
  }
}
