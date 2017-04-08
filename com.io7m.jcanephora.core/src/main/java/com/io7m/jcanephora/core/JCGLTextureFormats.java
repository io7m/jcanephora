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

package com.io7m.jcanephora.core;

import com.io7m.junreachable.UnreachableCodeException;

/**
 * Information about texture formats.
 */

public final class JCGLTextureFormats
{
  private JCGLTextureFormats()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Check that the texture format is a color-renderable format.
   *
   * @param t The texture format
   *
   * @throws JCGLExceptionFormatError If the texture is not of the correct
   *                                  format
   */

  public static void checkColorRenderableTexture2D(
    final JCGLTextureFormat t)
    throws JCGLExceptionFormatError
  {
    if (!isColorRenderable2D(t)) {
      final String m = String.format(
        "Format %s is not color-renderable for 2D textures", t);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture format is a depth-renderable format.
   *
   * @param t The texture format
   *
   * @throws JCGLExceptionFormatError If the texture is not of the correct
   *                                  format
   */

  public static void checkDepthRenderableTexture2D(
    final JCGLTextureFormat t)
    throws JCGLExceptionFormatError
  {
    if (!isDepthRenderable(t)) {
      final String m = String.format(
        "Format %s is not depth-renderable for 2D textures", t);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture is of a depth (not stencil) renderable format.
   *
   * @param t The texture format
   *
   * @throws JCGLExceptionFormatError If the texture is not of the correct
   *                                  format
   */

  public static void checkDepthOnlyRenderableTexture2D(
    final JCGLTextureFormat t)
    throws JCGLExceptionFormatError
  {
    if (!isDepthRenderable(t)) {
      final String m =
        String.format("Format %s is not depth-renderable", t);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }

    if (isStencilRenderable(t)) {
      final String m = String.format(
        "Format %s is stencil-renderable: Must be used as a depth+stencil "
          + "attachment",
        t);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the texture is of a depth+stencil-renderable format.
   *
   * @param t The texture
   *
   * @throws JCGLExceptionFormatError If the texture is not of the correct
   *                                  format
   */

  public static void checkDepthStencilRenderableTexture2D(
    final JCGLTextureFormat t)
    throws JCGLExceptionFormatError
  {
    if (isDepthRenderable(t)
      && isStencilRenderable(t)) {
      return;
    }

    final String m =
      String.format("Format %s is not depth+stencil-renderable", t);
    assert m != null;
    throw new JCGLExceptionFormatError(m);
  }

  /**
   * @param f A texture format
   *
   * @return {@code true} iff {@code f} is depth-renderable
   */

  public static boolean isDepthRenderable(final JCGLTextureFormat f)
  {
    switch (f) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        return true;
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
        return false;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param f A texture format
   *
   * @return The number of depth bits specified by format {@code f}
   */

  public static int getDepthBits(final JCGLTextureFormat f)
  {
    switch (f) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
        return 16;
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        return 24;
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        return 32;
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
        return 0;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param f A texture format
   *
   * @return {@code true} iff {@code f} is color-renderable
   */

  public static boolean isColorRenderable2D(final JCGLTextureFormat f)
  {
    switch (f) {
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
      case TEXTURE_FORMAT_R_8_1BPP: {
        return true;
      }

      /*
       * This is not actually required by OpenGL 3.*, but in practice, it's
       * color-renderable everywhere.
       */

      case TEXTURE_FORMAT_RGB_8_3BPP: {
        return true;
      }

      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param f The texture format
   *
   * @return {@code true} iff {@code f} is a floating-point f
   */

  public static boolean isFloatingPoint(
    final JCGLTextureFormat f)
  {
    switch (f) {
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP: {
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
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
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
      case TEXTURE_FORMAT_R_8_1BPP: {
        return false;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param f The texture format
   *
   * @return {@code true} iff {@code f} is a stencil-renderable format
   */

  public static boolean isStencilRenderable(
    final JCGLTextureFormat f)
  {
    switch (f) {
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
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
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
      case TEXTURE_FORMAT_R_8_1BPP: {
        return false;
      }
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP: {
        return true;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * @param f A texture format
   *
   * @return The number of stencil bits specified by format {@code f}
   */

  public static int getStencilBits(final JCGLTextureFormat f)
  {
    switch (f) {
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        return 8;
      case TEXTURE_FORMAT_DEPTH_16_2BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP:
      case TEXTURE_FORMAT_DEPTH_32F_4BPP:
      case TEXTURE_FORMAT_R_16_2BPP:
      case TEXTURE_FORMAT_R_16F_2BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_32F_4BPP:
      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_8_1BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_RG_16_4BPP:
      case TEXTURE_FORMAT_RG_16F_4BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_32F_8BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8_2BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RGB_16_6BPP:
      case TEXTURE_FORMAT_RGB_16F_6BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32F_12BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8_3BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16_8BPP:
      case TEXTURE_FORMAT_RGBA_16F_8BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_32F_16BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8_4BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
        return 0;
    }

    throw new UnreachableCodeException();
  }
}
