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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class TextureTypeTest
{
  @SuppressWarnings("static-method") @Test public void testBytesPerPixel()
  {
    Assert.assertEquals(
      1,
      TextureType.TEXTURE_TYPE_R_8_1BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RG_8_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RGB_565_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      3,
      TextureType.TEXTURE_TYPE_RGB_8_3BPP.getBytesPerPixel());
    Assert.assertEquals(
      2,
      TextureType.TEXTURE_TYPE_RGBA_4444_2BPP.getBytesPerPixel());
    Assert.assertEquals(
      3,
      TextureType.TEXTURE_TYPE_RGB_8_3BPP.getBytesPerPixel());
    Assert.assertEquals(
      4,
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP.getBytesPerPixel());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testColorRenderableGL3()
      throws ConstraintError
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_FULL,
        "OpenGL 3.0 Something");

    final JCGLNamedExtensions extensions = new JCGLNamedExtensions() {
      @Override public boolean extensionIsSupported(
        final String name)
        throws ConstraintError
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
        throws ConstraintError
      {
        return false;
      }
    };

    for (final TextureType tt : TextureTypeMeta.getTextures2DRequiredByGL3()) {
      switch (tt) {
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureTypeMeta.isColourRenderable2D(tt, version, extensions));
      }
    }
  }

  @SuppressWarnings("static-method") @Test public
    void
    testColorRenderableGLES2()
      throws ConstraintError
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(2, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 2.0 Something");

    final JCGLNamedExtensions extensions = new JCGLNamedExtensions() {
      @Override public boolean extensionIsSupported(
        final String name)
        throws ConstraintError
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
        throws ConstraintError
      {
        return false;
      }
    };

    for (final TextureType tt : TextureTypeMeta
      .getTextures2DRequiredByGLES2()) {
      switch (tt) {
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
          Assert.assertTrue(
            tt.toString(),
            TextureTypeMeta.isColourRenderable2D(tt, version, extensions));
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertFalse(
            tt.toString(),
            TextureTypeMeta.isColourRenderable2D(tt, version, extensions));
      }
    }
  }

  @SuppressWarnings("static-method") @Test public
    void
    testColorRenderableGLES3WithColorBufferFloat()
      throws ConstraintError
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 3.0 Something");

    final JCGLNamedExtensions extensions = new JCGLNamedExtensions() {
      @Override public boolean extensionIsSupported(
        final String name)
        throws ConstraintError
      {
        if (name.equals(JCGLExtensionNames.GL_EXT_COLOR_BUFFER_FLOAT)) {
          return true;
        }
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
        throws ConstraintError
      {
        return this.extensionIsSupported(name);
      }
    };

    for (final TextureType tt : TextureTypeMeta
      .getTextures2DRequiredByGLES3()) {
      switch (tt) {
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureTypeMeta.isColourRenderable2D(tt, version, extensions));
      }
    }
  }

  @SuppressWarnings("static-method") @Test public
    void
    testColorRenderableGLES3()
      throws ConstraintError
  {
    final JCGLVersion version =
      JCGLVersion.make(
        new JCGLVersionNumber(3, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES 3.0 Something");

    final JCGLNamedExtensions extensions = new JCGLNamedExtensions() {
      @Override public boolean extensionIsSupported(
        final String name)
        throws ConstraintError
      {
        return false;
      }

      @Override public boolean extensionIsVisible(
        final String name)
        throws ConstraintError
      {
        return false;
      }
    };

    for (final TextureType tt : TextureTypeMeta
      .getTextures2DRequiredByGLES3()) {
      switch (tt) {
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
          break;
        // $CASES-OMITTED$
        default:
          Assert.assertTrue(
            tt.toString(),
            TextureTypeMeta.isColourRenderable2D(tt, version, extensions));
      }
    }
  }

  @SuppressWarnings("static-method") @Test public void testComponents()
  {
    Assert.assertEquals(
      EnumSet.noneOf(TextureType.class),
      TextureTypeMeta.getTexturesWithComponents(0));
  }
}
