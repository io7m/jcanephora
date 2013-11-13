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

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.UnreachableCodeException;

/**
 * Information about texture types.
 */

@Immutable public final class TextureTypeMeta
{
  /**
   * Retrieve the set of 2D texture types guaranteed to be available on the
   * common subset of OpenGL 3.*, ES2, ES3, 2.1...
   */

  public static @Nonnull
    EnumSet<TextureType>
    getTextures2DRequiredByCommonSubset()
  {
    final EnumSet<TextureType> es3 =
      TextureTypeMeta.getTextures2DRequiredByGLES3();
    final EnumSet<TextureType> gl3 =
      TextureTypeMeta.getTextures2DRequiredByGL3();
    final EnumSet<TextureType> es2 =
      TextureTypeMeta.getTextures2DRequiredByGLES2();

    final EnumSet<TextureType> common = EnumSet.noneOf(TextureType.class);
    for (final TextureType t : TextureType.values()) {
      if (es3.contains(t)) {
        if (gl3.contains(t)) {
          if (es2.contains(t)) {
            common.add(t);
          }
        }
      }
    }

    /**
     * Explicitly remove DEPTH_16, as it's known to be unavailable on various
     * 2.1 implementations.
     */

    common.remove(TextureType.TEXTURE_TYPE_DEPTH_16_2BPP);
    return common;
  }

  /**
   * Retrieve the set of 2D texture types guaranteed to be available on OpenGL
   * 2.1 contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTextures2DRequiredByGL21()
  {
    return TextureTypeMeta.getTextures2DRequiredByCommonSubset();
  }

  /**
   * Retrieve the set of 2D texture types guaranteed to be available on OpenGL
   * 3.* contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTextures2DRequiredByGL3()
  {
    final EnumSet<TextureType> s = EnumSet.noneOf(TextureType.class);

    for (final TextureType t : TextureType.values()) {
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
   * Retrieve the set of 2D texture types required by the common subset of GL3
   * and ES3.
   */

  public static @Nonnull EnumSet<TextureType> getTextures2DRequiredByGL3ES3()
  {
    final EnumSet<TextureType> gl3 =
      TextureTypeMeta.getTextures2DRequiredByGL3();
    final EnumSet<TextureType> es3 =
      TextureTypeMeta.getTextures2DRequiredByGLES3();
    final EnumSet<TextureType> all = EnumSet.noneOf(TextureType.class);

    for (final TextureType t : TextureType.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    return all;
  }

  /**
   * Retrieve the set of 2D texture types guaranteed to be available on OpenGL
   * ES2 contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTextures2DRequiredByGLES2()
  {
    final EnumSet<TextureType> s = EnumSet.noneOf(TextureType.class);

    /**
     * Color-renderable formats implicitly required by section 4.4.5 of the
     * ES2 standard.
     */

    s.add(TextureType.TEXTURE_TYPE_DEPTH_16_2BPP);
    s.add(TextureType.TEXTURE_TYPE_RGBA_4444_2BPP);
    s.add(TextureType.TEXTURE_TYPE_RGBA_5551_2BPP);
    s.add(TextureType.TEXTURE_TYPE_RGB_565_2BPP);

    /**
     * Not explicitly required by ES2, but in practice, available everywhere.
     */

    s.add(TextureType.TEXTURE_TYPE_RGB_8_3BPP);
    s.add(TextureType.TEXTURE_TYPE_RGBA_8_4BPP);
    return s;
  }

  /**
   * Retrieve the set of 2D texture types guaranteed to be available on OpenGL
   * ES 3 contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTextures2DRequiredByGLES3()
  {
    final EnumSet<TextureType> s = EnumSet.noneOf(TextureType.class);

    for (final TextureType t : TextureType.values()) {
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
   * Retrieve the set of cube textures guaranteed to be available on the
   * common subset of OpenGL 3.*, ES2, ES3, 2.1...
   */

  public static @Nonnull
    EnumSet<TextureType>
    getTexturesCubeRequiredByCommonSubset()
  {
    return TextureTypeMeta.getTextures2DRequiredByCommonSubset();
  }

  /**
   * Retrieve the set of cube texture types guaranteed to be available on
   * OpenGL 2.1 contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTexturesCubeRequiredByGL21()
  {
    return TextureTypeMeta.getTextures2DRequiredByGL21();
  }

  /**
   * Retrieve the set of cube texture types guaranteed to be available on
   * OpenGL 3.* contexts.
   */

  public static @Nonnull EnumSet<TextureType> getTexturesCubeRequiredByGL3()
  {
    return TextureTypeMeta.getTextures2DRequiredByGL3();
  }

  /**
   * Retrieve the set of cube texture types required by the common subset of
   * GL3 and ES3.
   */

  public static @Nonnull
    EnumSet<TextureType>
    getTexturesCubeRequiredByGL3ES3()
  {
    final EnumSet<TextureType> gl3 =
      TextureTypeMeta.getTexturesCubeRequiredByGL3();
    final EnumSet<TextureType> es3 =
      TextureTypeMeta.getTexturesCubeRequiredByGLES3();
    final EnumSet<TextureType> all = EnumSet.noneOf(TextureType.class);

    for (final TextureType t : TextureType.values()) {
      if (gl3.contains(t)) {
        if (es3.contains(t)) {
          all.add(t);
        }
      }
    }

    return all;
  }

  /**
   * Retrieve the set of cube texture types guaranteed to be available on
   * OpenGL ES 2 contexts.
   */

  public static @Nonnull
    EnumSet<TextureType>
    getTexturesCubeRequiredByGLES2()
  {
    return TextureTypeMeta.getTextures2DRequiredByGLES2();
  }

  /**
   * Retrieve the set of cube texture types guaranteed to be available on
   * OpenGL ES 3 contexts.
   */

  public static @Nonnull
    EnumSet<TextureType>
    getTexturesCubeRequiredByGLES3()
  {
    return TextureTypeMeta.getTextures2DRequiredByGLES3();
  }

  /**
   * Retrieve all texture types that have <code>i</code> components.
   */

  public static @Nonnull EnumSet<TextureType> getTexturesWithComponents(
    final int i)
  {
    final EnumSet<TextureType> s = EnumSet.noneOf(TextureType.class);

    for (final TextureType t : TextureType.values()) {
      if (t.getComponentCount() == i) {
        s.add(t);
      }
    }

    return s;
  }

  /**
   * Return <code>true</code> iff the given texture type is colour-renderable
   * on the given version of OpenGL.
   */

  public static boolean isColourRenderable(
    final @Nonnull TextureType type,
    final @Nonnull JCGLVersion version)
  {
    switch (version.getAPI()) {
      case JCGL_ES:
        if (version.getVersionMajor() >= 3) {
          TextureTypeMeta.isColourRenderableES3(type);
        }
        return TextureTypeMeta.isColourRenderableES2(type);
      case JCGL_FULL:
        return TextureTypeMeta.isColourRenderableGL3(type);
    }

    throw new UnreachableCodeException();
  }

  private static boolean isColourRenderableES2(
    final @Nonnull TextureType type)
  {
    switch (type) {
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
   * 
   * @param type
   */

  private static boolean isColourRenderableES3(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
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

  /**
   * See the OpenGL 3.1 standard, page 119 "Required texture formats".
   */

  private static boolean isColourRenderableGL3(
    final TextureType type)
  {
    switch (type) {
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
   * Return <code>true</code> iff the given texture type is depth-renderable.
   */

  public static boolean isDepthRenderable(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
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
    final @Nonnull TextureType type)
  {
    switch (type) {
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

  private TextureTypeMeta()
  {
    throw new UnreachableCodeException();
  }
}