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

package com.io7m.jcanephora.tests.contracts.gl3es3;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3ES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TextureCubeStaticContract;
import com.io7m.junreachable.UnreachableCodeException;

@SuppressWarnings("null") public abstract class TextureCubeStaticGL3ES3Contract extends
  TextureCubeStaticContract<JCGLTexturesCubeStaticGL3ES3Type>
{
  /**
   * Textures have the correct type.
   */

  @Test public final void testTextureTypes()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL3ES3Type gl =
      this.getGLTextureCubeStatic(tc);

    for (final TextureFormat t : TextureFormatMeta
      .getTexturesCubeRequiredByGL3ES3()) {
      switch (t) {

        case TEXTURE_FORMAT_R_8_1BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_8U_1BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_8I_1BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_16U_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_16I_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_16F_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_32U_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_32I_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_32F_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateR32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_8_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_8U_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_8I_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_16U_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_16I_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_16F_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_32U_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_32I_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_32F_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRG32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_8_3BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_8U_3BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_8I_3BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_16U_6BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_16I_6BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_16F_6BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_32U_12BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_32I_12BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_32F_12BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGB32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_8_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateRGBA32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateDepth16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateDepth24(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateDepth24Stencil8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        {
          final TextureCubeStaticType tx =
            gl.textureCubeStaticAllocateDepth32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_565_2BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }
      }
    }
  }
}