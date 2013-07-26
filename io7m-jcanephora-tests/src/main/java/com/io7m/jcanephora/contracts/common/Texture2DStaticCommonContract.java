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

package com.io7m.jcanephora.contracts.common;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLTextures2DStaticCommon;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.Texture2DStaticContract;

public abstract class Texture2DStaticCommonContract extends
  Texture2DStaticContract<GLTextures2DStaticCommon>
{
  /**
   * Textures have the correct type.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public void testTextureTypes()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTextures2DStaticCommon gl = this.getGLTexture2DStatic(tc);

    for (final TextureType type : TextureType.get2DTypesCommon()) {
      Texture2DStatic t = null;

      switch (type) {
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        {
          throw new UnreachableCodeException();
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          t =
            gl.texture2DStaticAllocateRGBA8888(
              "image",
              256,
              128,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_LINEAR,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(type, t.getType());
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          t =
            gl.texture2DStaticAllocateRGB888(
              "image",
              256,
              128,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_LINEAR,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

          Assert.assertEquals(type, t.getType());
          break;
        }
      }

      assert t != null;
      gl.texture2DStaticDelete(t);
      Assert.assertTrue(t.resourceIsDeleted());
    }
  }
}
