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

package com.io7m.jcanephora.tests.contracts.gl3;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.SpatialCursorReadable4iType;
import com.io7m.jcanephora.SpatialCursorWritable4iType;
import com.io7m.jcanephora.Texture2DStaticReadableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdate;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.Texture2DStaticContract;
import com.io7m.jtensors.VectorM4I;

@SuppressWarnings("null") public abstract class Texture2DStaticGL3Contract extends
  Texture2DStaticContract<JCGLTextures2DStaticGL3Type>
{
  /**
   * Texture fetching works.
   */

  @Test public final void testTextureImageGet()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextures2DStaticGL3Type gl = this.getGLTexture2DStatic(tc);

    final Texture2DStaticType tx =
      gl.texture2DStaticAllocateRGBA8(
        "image",
        256,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    {
      final Texture2DStaticUpdateType twd =
        Texture2DStaticUpdate.newReplacingAll(tx);
      final SpatialCursorWritable4iType c = twd.getCursor4i();
      final VectorM4I pixel = new VectorM4I();

      for (int y = 0; y < 256; ++y) {
        for (int x = 0; x < 256; ++x) {
          pixel.set4I(x, y, x, y);
          c.seekTo(x, y);
          c.put4i(pixel);
        }
      }

      gl.texture2DStaticUpdate(twd);
    }

    {
      final Texture2DStaticReadableType trd = gl.texture2DStaticGetImage(tx);
      final SpatialCursorReadable4iType c = trd.getCursor4i();
      final VectorM4I v = new VectorM4I();

      for (int y = 0; y < 256; ++y) {
        for (int x = 0; x < 256; ++x) {
          c.seekTo(x, y);
          c.get4i(v);
          Assert.assertEquals(x, v.getXI());
          Assert.assertEquals(y, v.getYI());
          Assert.assertEquals(x, v.getZI());
          Assert.assertEquals(y, v.getWI());
        }
      }
    }
  }

  /**
   * Textures have the correct type.
   */

  @Test public final void testTextureTypes()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextures2DStaticGL3Type gl = this.getGLTexture2DStatic(tc);

    for (final TextureFormat t : TextureFormatMeta
      .getTextures2DRequiredByGL3()) {
      switch (t) {
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA1010102(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_565_2BPP:
        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }

        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA8U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA8I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA16U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA16I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA16(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA16f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA32U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA32I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGB_8_3BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB8(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB8U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB8I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB16U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB16I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_16_6BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB16(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB16f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB32U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB32I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGB32f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RG_8_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG8(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_8U_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG8U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_8I_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG8I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_16U_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG16U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_16I_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG16I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_16_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG16(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_16F_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG16f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_32U_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG32U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_32I_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG32I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RG_32F_8BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRG32f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_R_8_1BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR8(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_8U_1BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR8U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_8I_1BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR8I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_16U_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR16U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_16I_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR16I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_16_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR16(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_16F_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR16f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_32U_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR32U(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_32I_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR32I(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_R_32F_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateR32f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }

        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA32f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_RGBA_8_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateRGBA8(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateDepth16(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateDepth24Stencil8(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateDepth24(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        {
          final Texture2DStaticType tx =
            gl.texture2DStaticAllocateDepth32f(
              t.toString(),
              64,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.textureGetFormat(), t);
          break;
        }
      }
    }
  }
}
