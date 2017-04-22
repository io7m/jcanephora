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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLCubeMapFaceRH;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataType;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureUpdateProviderType;
import com.io7m.jranges.RangeCheckException;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

/**
 * Texture update provider contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTLTextureUpdateProviderContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLTLTextureUpdateProviderType getUpdateProvider();

  protected abstract JCGLTLTextureDataProviderType getDataProvider();

  protected abstract JCGLTexturesType getTextures(String name);

  @Test
  public final void testTexture2DWidthSmall()
    throws Exception
  {
    final JCGLTexturesType t = this.getTextures("main");
    final JCGLTLTextureUpdateProviderType up = this.getUpdateProvider();
    final JCGLTLTextureDataProviderType dp = this.getDataProvider();
    final List<JCGLTextureUnitType> units = t.textureGetUnits();

    final JCGLTLTextureDataType data =
      dp.loadFromStream(
        JCGLTLTextureUpdateProviderContract.class.getResourceAsStream(
          "basn6a08.png"));

    final JCGLTexture2DType tt = t.texture2DAllocate(
      units.get(0), 8L, 32L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(RangeCheckException.class);
    this.expected.expectMessage(new StringContains("Texture width"));
    up.createTextureUpdate2D(tt, data);
  }

  @Test
  public final void testTexture2DHeightSmall()
    throws Exception
  {
    final JCGLTexturesType t = this.getTextures("main");
    final JCGLTLTextureUpdateProviderType up = this.getUpdateProvider();
    final JCGLTLTextureDataProviderType dp = this.getDataProvider();
    final List<JCGLTextureUnitType> units = t.textureGetUnits();

    final JCGLTLTextureDataType data =
      dp.loadFromStream(
        JCGLTLTextureUpdateProviderContract.class.getResourceAsStream(
          "basn6a08.png"));

    final JCGLTexture2DType tt = t.texture2DAllocate(
      units.get(0), 32L, 8L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(RangeCheckException.class);
    this.expected.expectMessage(new StringContains("Texture height"));
    up.createTextureUpdate2D(tt, data);
  }

  @Test
  public final void testTexture2DUpdates()
    throws Exception
  {
    final JCGLTexturesType t = this.getTextures("main");
    final JCGLTLTextureUpdateProviderType up = this.getUpdateProvider();
    final JCGLTLTextureDataProviderType dp = this.getDataProvider();
    final List<JCGLTextureUnitType> units = t.textureGetUnits();

    final JCGLTLTextureDataType data =
      dp.loadFromStream(
        JCGLTLTextureUpdateProviderContract.class.getResourceAsStream(
          "basn6a08.png"));

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      switch (v) {
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP: {
          continue;
        }

        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        case TEXTURE_FORMAT_RGBA_8_4BPP: {

          final JCGLTexture2DType tt = t.texture2DAllocate(
            units.get(0), 32L, 32L, v,
            JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
            JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
            JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
            JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

          final JCGLTexture2DUpdateType u = up.createTextureUpdate2D(tt, data);
          Assert.assertEquals(tt, u.texture());

          t.texture2DUpdate(units.get(0), u);
          break;
        }
      }
    }
  }

  @Test
  public final void testTextureCubeSizeSmall()
    throws Exception
  {
    final JCGLTexturesType t = this.getTextures("main");
    final JCGLTLTextureUpdateProviderType up = this.getUpdateProvider();
    final JCGLTLTextureDataProviderType dp = this.getDataProvider();
    final List<JCGLTextureUnitType> units = t.textureGetUnits();

    final JCGLTLTextureDataType data =
      dp.loadFromStream(
        JCGLTLTextureUpdateProviderContract.class.getResourceAsStream(
          "basn6a08.png"));

    final JCGLTextureCubeType tt = t.textureCubeAllocate(
      units.get(0),
      8L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(RangeCheckException.class);
    this.expected.expectMessage(new StringContains("Texture size"));
    up.createTextureUpdateCube(tt, data);
  }

  @Test
  public final void testTextureCubeUpdates()
    throws Exception
  {
    final JCGLTexturesType t = this.getTextures("main");
    final JCGLTLTextureUpdateProviderType up = this.getUpdateProvider();
    final JCGLTLTextureDataProviderType dp = this.getDataProvider();
    final List<JCGLTextureUnitType> units = t.textureGetUnits();

    final JCGLTLTextureDataType data =
      dp.loadFromStream(
        JCGLTLTextureUpdateProviderContract.class.getResourceAsStream(
          "basn6a08.png"));

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      switch (v) {
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP: {
          continue;
        }

        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        case TEXTURE_FORMAT_RGBA_8_4BPP: {

          final JCGLTextureCubeType tt = t.textureCubeAllocate(
            units.get(0),
            32L,
            v,
            JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
            JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
            JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
            JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
            JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

          final JCGLTextureCubeUpdateType u = up.createTextureUpdateCube(tt, data);
          Assert.assertEquals(tt, u.texture());

          for (final JCGLCubeMapFaceRH face : JCGLCubeMapFaceRH.values()) {
            t.textureCubeUpdateRH(units.get(0), face, u);
          }
          break;
        }
      }
    }
  }
}
