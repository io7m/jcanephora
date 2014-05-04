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

package com.io7m.jcanephora.contracts.gl3;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLTexturesCubeStaticGL3;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorReadable4i;
import com.io7m.jcanephora.SpatialCursorWritable4i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureTypeMeta;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TextureCubeStaticContract;
import com.io7m.jcanephora.jogl.TextureCubeReadableData;
import com.io7m.jcanephora.jogl.TextureCubeWritableData;
import com.io7m.jtensors.VectorM4I;

public abstract class TextureCubeStaticGL3Contract extends
  TextureCubeStaticContract<JCGLTexturesCubeStaticGL3>
{
  /**
   * Texture fetching works.
   */

  @Test public final void testTextureImageGetLH()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL3 gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic tx =
      gl.textureCubeStaticAllocateRGBA8(
        "image",
        256,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    for (final CubeMapFaceLH face : CubeMapFaceLH.values()) {
      {
        final TextureCubeWritableData twd = new TextureCubeWritableData(tx);
        final SpatialCursorWritable4i c = twd.getCursor4i();
        final VectorM4I pixel = new VectorM4I();

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            pixel.x = x;
            pixel.y = y;
            pixel.z = face.ordinal();
            pixel.w = y;
            c.seekTo(x, y);
            c.put4i(pixel);
          }
        }

        gl.textureCubeStaticUpdateLH(face, twd);
      }

      {
        final TextureCubeReadableData trd =
          gl.textureCubeStaticGetImageLH(tx, face);
        final SpatialCursorReadable4i c = trd.getCursor4i();
        final VectorM4I v = new VectorM4I();

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            c.seekTo(x, y);
            c.get4i(v);
            Assert.assertEquals(x, v.x);
            Assert.assertEquals(y, v.y);
            Assert.assertEquals(face.ordinal(), v.z);
            Assert.assertEquals(y, v.w);
          }
        }
      }
    }
  }

  /**
   * Texture fetching works.
   */

  @Test public final void testTextureImageGetRH()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL3 gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic tx =
      gl.textureCubeStaticAllocateRGBA8(
        "image",
        256,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    for (final CubeMapFaceRH face : CubeMapFaceRH.values()) {
      {
        final TextureCubeWritableData twd = new TextureCubeWritableData(tx);
        final SpatialCursorWritable4i c = twd.getCursor4i();
        final VectorM4I pixel = new VectorM4I();

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            pixel.x = x;
            pixel.y = y;
            pixel.z = face.ordinal();
            pixel.w = y;
            c.seekTo(x, y);
            c.put4i(pixel);
          }
        }

        gl.textureCubeStaticUpdateRH(face, twd);
      }

      {
        final TextureCubeReadableData trd =
          gl.textureCubeStaticGetImageRH(tx, face);
        final SpatialCursorReadable4i c = trd.getCursor4i();
        final VectorM4I v = new VectorM4I();

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            c.seekTo(x, y);
            c.get4i(v);
            Assert.assertEquals(x, v.x);
            Assert.assertEquals(y, v.y);
            Assert.assertEquals(face.ordinal(), v.z);
            Assert.assertEquals(y, v.w);
          }
        }
      }
    }
  }

  /**
   * Textures have the correct type.
   */

  @Test public final void testTextureTypes()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL3 gl = this.getGLTextureCubeStatic(tc);

    for (final TextureType t : TextureTypeMeta.getTexturesCubeRequiredByGL3()) {
      switch (t) {
        case TEXTURE_TYPE_R_8_1BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_8U_1BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_8I_1BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_16_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_16U_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_16I_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_16F_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_32U_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_32I_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_R_32F_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateR32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_8_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_8U_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_8I_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_16_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_16U_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_16I_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_16F_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_32U_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_32I_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RG_32F_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRG32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_8U_3BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_8I_3BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_16_6BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_16U_6BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_16I_6BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_16F_6BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_32U_12BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_32I_12BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGB_32F_12BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_8U_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA8U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_8I_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA8I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_16_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_16U_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA16U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_16I_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA16I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_16F_8BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA16f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_32U_16BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA32U(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_32I_16BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA32I(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_DEPTH_16_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateDepth16(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateDepth24(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateDepth24Stencil8(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateDepth32f(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA1010102(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }

        case TEXTURE_TYPE_RGBA_4444_2BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }

        case TEXTURE_TYPE_RGBA_5551_2BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }

        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }
      }
    }
  }

}
