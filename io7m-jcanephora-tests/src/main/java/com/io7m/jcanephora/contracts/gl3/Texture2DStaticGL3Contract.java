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
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLTextures2DStaticGL3;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorReadable4i;
import com.io7m.jcanephora.SpatialCursorWritable4i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DReadableData;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.Texture2DStaticContract;
import com.io7m.jtensors.VectorM4I;

public abstract class Texture2DStaticGL3Contract extends
  Texture2DStaticContract<JCGLTextures2DStaticGL3>
{
  /**
   * Textures have the correct type.
   */

  @Test public final void testTextureTypes()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextures2DStaticGL3 gl = this.getGLTexture2DStatic(tc);

    for (final TextureType t : TextureType.get2DTypesGL3()) {
      switch (t) {
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        {
          break;
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGBA8888(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGB888(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RG_88_2BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRG88(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_R_8_1BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateR8(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateDepth16(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateDepth24(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateDepth32f(
              t.toString(),
              128,
              128,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
      }
    }
  }

  /**
   * Texture fetching works.
   */

  @Test public final void testTextureImageGet()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextures2DStaticGL3 gl = this.getGLTexture2DStatic(tc);

    final Texture2DStatic tx =
      gl.texture2DStaticAllocateRGBA8888(
        "image",
        256,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    {
      final Texture2DWritableData twd = new Texture2DWritableData(tx);
      final SpatialCursorWritable4i c = twd.getCursor4i();

      for (int y = 0; y < 256; ++y) {
        for (int x = 0; x < 256; ++x) {
          c.seekTo(x, y);
          c.put4i(x, y, x, y);
        }
      }

      gl.texture2DStaticUpdate(twd);
    }

    {
      final Texture2DReadableData trd = gl.texture2DStaticGetImage(tx);
      final SpatialCursorReadable4i c = trd.getCursor4i();
      final VectorM4I v = new VectorM4I();

      for (int y = 0; y < 256; ++y) {
        for (int x = 0; x < 256; ++x) {
          c.seekTo(x, y);
          c.get4i(v);
          Assert.assertEquals(x, v.x);
          Assert.assertEquals(y, v.y);
          Assert.assertEquals(x, v.z);
          Assert.assertEquals(y, v.w);
        }
      }
    }
  }
}
