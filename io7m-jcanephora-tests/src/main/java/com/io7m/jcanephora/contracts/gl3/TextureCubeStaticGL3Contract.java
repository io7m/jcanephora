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
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLTexturesCubeStaticGL3ES3;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TextureCubeStaticContract;

public abstract class TextureCubeStaticGL3Contract extends
  TextureCubeStaticContract<JCGLTexturesCubeStaticGL3ES3>
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
    final JCGLTexturesCubeStaticGL3ES3 gl = this.getGLTextureCubeStatic(tc);

    for (final TextureType t : TextureType.getCubeTypesGL3()) {
      switch (t) {
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          throw new UnreachableCodeException();
        }
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
      }
    }
  }

}
