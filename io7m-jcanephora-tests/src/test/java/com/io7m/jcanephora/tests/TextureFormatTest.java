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

package com.io7m.jcanephora.tests;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLExtensionNames;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.JCGLVersionNumber;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;

@SuppressWarnings("static-method") public class TextureFormatTest
{
  @Test public void testBytesPerPixel()
  {
    Assert.assertEquals(
      1,
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      3,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      3,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP.getBytesPerPixel());
    Assert.assertEquals(
      4,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP.getBytesPerPixel());
  }

  @Test public void testColorRenderableGL3()
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_FULL,
        "OpenGL 3.0 Something");

    final JCGLNamedExtensionsType extensions = new JCGLNamedExtensionsType() {
      @Override public boolean extensionIsSupported(
        final String name)
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
      {
        return false;
      }
    };

    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGL3()) {
      switch (tt) {
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureFormatMeta.isColorRenderable2D(tt, version, extensions));
      }
    }
  }

  @Test public void testColorRenderableGLES2()
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(2, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 2.0 Something");

    final JCGLNamedExtensionsType extensions = new JCGLNamedExtensionsType() {
      @Override public boolean extensionIsSupported(
        final String name)
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
      {
        return false;
      }
    };

    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGLES2()) {
      switch (tt) {
        case TEXTURE_FORMAT_RGB_565_2BPP:
        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
          Assert.assertTrue(
            tt.toString(),
            TextureFormatMeta.isColorRenderable2D(tt, version, extensions));
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertFalse(
            tt.toString(),
            TextureFormatMeta.isColorRenderable2D(tt, version, extensions));
      }
    }
  }

  @Test public void testColorRenderableGLES3()
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 3.0 Something");

    final JCGLNamedExtensionsType extensions = new JCGLNamedExtensionsType() {
      @Override public boolean extensionIsSupported(
        final String name)
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
      {
        return false;
      }
    };

    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGLES3()) {
      switch (tt) {
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureFormatMeta.isColorRenderable2D(tt, version, extensions));
      }
    }
  }

  @Test public void testColorRenderableGLES3WithColorBufferFloat()
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 3.0 Something");

    final JCGLNamedExtensionsType extensions = new JCGLNamedExtensionsType() {
      @Override public boolean extensionIsSupported(
        final String name)
      {
        if (name.equals(JCGLExtensionNames.GL_EXT_COLOR_BUFFER_FLOAT)) {
          return true;
        }
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
      {
        return this.extensionIsSupported(name);
      }
    };

    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGLES3()) {
      switch (tt) {
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureFormatMeta.isColorRenderable2D(tt, version, extensions));
      }
    }
  }

  @Test public void testComponents()
  {
    Assert.assertEquals(
      EnumSet.noneOf(TextureFormat.class),
      TextureFormatMeta.getTexturesWithComponents(0));
  }
}
