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

package com.io7m.jcanephora.contracts.gl2;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLTexturesCubeStaticGL2;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorReadable4i;
import com.io7m.jcanephora.SpatialCursorWritable4i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeReadableData;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TextureCubeStaticContract;
import com.io7m.jtensors.VectorM4I;

public abstract class TextureCubeStaticGL2Contract extends
  TextureCubeStaticContract<JCGLTexturesCubeStaticGL2>
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
    final JCGLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStatic(tc);

    for (final TextureType t : TextureType.getCubeTypesGL2()) {
      switch (t) {
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA8888(
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
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB888(
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
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          throw new UnreachableCodeException();
        }
      }
    }
  }

  /**
   * Texture fetching works.
   */

  @Test public final void testTextureImageGetLH()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic tx =
      gl.textureCubeStaticAllocateRGBA8888(
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

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            c.seekTo(x, y);
            c.put4i(x, y, face.ordinal(), y);
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
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic tx =
      gl.textureCubeStaticAllocateRGBA8888(
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

        for (int y = 0; y < 256; ++y) {
          for (int x = 0; x < 256; ++x) {
            c.seekTo(x, y);
            c.put4i(x, y, face.ordinal(), y);
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
}
