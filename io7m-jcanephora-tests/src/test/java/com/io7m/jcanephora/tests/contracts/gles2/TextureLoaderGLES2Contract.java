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

package com.io7m.jcanephora.tests.contracts.gles2;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.CMFNegativeXKind;
import com.io7m.jcanephora.CMFNegativeYKind;
import com.io7m.jcanephora.CMFNegativeZKind;
import com.io7m.jcanephora.CMFPositiveXKind;
import com.io7m.jcanephora.CMFPositiveYKind;
import com.io7m.jcanephora.CMFPositiveZKind;
import com.io7m.jcanephora.CubeMapFaceInputStream;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGLES2Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGLES2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TextureLoaderContract;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

@SuppressWarnings({ "null" }) public abstract class TextureLoaderGLES2Contract<T extends TextureLoaderType> extends
  TextureLoaderContract<JCGLTextures2DStaticGLES2Type, JCGLTexturesCubeStaticGLES2Type, T>
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  private static void loadSpecific(
    final FilesystemType fs,
    final JCGLTextures2DStaticGLES2Type gl,
    final TextureLoaderType tl,
    final String path)
    throws FilesystemError,
      JCGLException,
      IOException
  {
    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGLES2()) {
      Texture2DStaticType t = null;
      final InputStream stream = fs.openFile(PathVirtual.ofString(path));

      switch (tt) {
        case TEXTURE_FORMAT_RGBA_8_4BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        {
          stream.close();
          throw new UnreachableCodeException(
            new AssertionError(tt.toString()));
        }
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        {
          continue;
        }

        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        {
          t =
            tl.load2DStaticRGBA4444(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
            t.textureGetFormat());
          break;
        }
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        {
          t =
            tl.load2DStaticRGBA5551(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
            t.textureGetFormat());
          break;
        }
        case TEXTURE_FORMAT_RGB_565_2BPP:
        {
          t =
            tl.load2DStaticRGB565(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
            t.textureGetFormat());
          break;
        }
      }

      assert t != null;
      Assert.assertFalse(t.resourceIsDeleted());
      Assert.assertEquals(256, t.textureGetWidth());
      Assert.assertEquals(256, t.textureGetHeight());
      Assert.assertEquals(
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        t.textureGetMagnificationFilter());
      Assert.assertEquals(
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        t.textureGetMinificationFilter());
      Assert.assertEquals("image", t.textureGetName());
      Assert.assertEquals(
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        t.textureGetWrapS());
      Assert.assertEquals(
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        t.textureGetWrapT());

      gl.texture2DStaticDelete(t);
      Assert.assertTrue(t.resourceIsDeleted());

      stream.close();
    }
  }

  @Test public final void testTextureTypesCubeLHInferredGreyscale()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_grey.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeLHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredIndexed()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_index.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeLHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredMono()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_mono.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeLHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredRGB()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_888_3.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeLHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredGreyscale()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_grey.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeLHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredIndexed()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_index.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeRHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredMono()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_mono.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeRHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredRGB()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGLES2Type gc = this.getGLTexturesCube(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_888_3.png";
    final CubeMapFaceInputStream<CMFPositiveZKind> pos_z =
      new CubeMapFaceInputStream<CMFPositiveZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeZKind> neg_z =
      new CubeMapFaceInputStream<CMFNegativeZKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveYKind> pos_y =
      new CubeMapFaceInputStream<CMFPositiveYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeYKind> neg_y =
      new CubeMapFaceInputStream<CMFNegativeYKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFPositiveXKind> pos_x =
      new CubeMapFaceInputStream<CMFPositiveXKind>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFNegativeXKind> neg_x =
      new CubeMapFaceInputStream<CMFNegativeXKind>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStaticType t =
      tl.loadCubeRHStaticInferred(
        tc.getGLImplementation(),
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        pos_z,
        neg_z,
        pos_y,
        neg_y,
        pos_x,
        neg_x,
        "image");

    pos_z.close();
    neg_z.close();
    pos_y.close();
    neg_y.close();
    pos_x.close();
    neg_x.close();

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      t.textureGetFormat());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesGreyscaleToSpecific()
    throws JCGLException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8_grey.png";
    TextureLoaderGLES2Contract.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesIndexedToSpecific()
    throws JCGLException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8_index.png";
    TextureLoaderGLES2Contract.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesInferredGreyscale()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredIndexed()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredMono()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test(expected = IOException.class) public final
    void
    testTextureTypesInferredNotAnImage()
      throws IOException,
        FilesystemError,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/not-an-image.txt"));

    tl.load2DStaticInferred(
      tc.getGLImplementation(),
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      stream,
      "image");

    stream.close();
  }

  @Test public final void testTextureTypesInferredRGB()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredRGBA()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.textureGetWidth());
    Assert.assertEquals(256, t.textureGetHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.textureGetMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.textureGetMinificationFilter());
    Assert.assertEquals("image", t.textureGetName());
    Assert.assertEquals(
      TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.textureGetWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInvalidToSpecific()
    throws FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/not-an-image.txt";
    int io_exception_count = 0;

    for (final TextureFormat tt : TextureFormatMeta
      .getTextures2DRequiredByGLES2()) {
      final InputStream stream = fs.openFile(PathVirtual.ofString(path));

      switch (tt) {
        case TEXTURE_FORMAT_RGBA_8_4BPP:
        case TEXTURE_FORMAT_RGB_8_3BPP:
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        case TEXTURE_FORMAT_RGB_16_6BPP:
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        case TEXTURE_FORMAT_RG_16F_4BPP:
        case TEXTURE_FORMAT_RG_16I_4BPP:
        case TEXTURE_FORMAT_RG_16U_4BPP:
        case TEXTURE_FORMAT_RG_16_4BPP:
        case TEXTURE_FORMAT_RG_32F_8BPP:
        case TEXTURE_FORMAT_RG_32I_8BPP:
        case TEXTURE_FORMAT_RG_32U_8BPP:
        case TEXTURE_FORMAT_RG_8I_2BPP:
        case TEXTURE_FORMAT_RG_8U_2BPP:
        case TEXTURE_FORMAT_R_16F_2BPP:
        case TEXTURE_FORMAT_R_16I_2BPP:
        case TEXTURE_FORMAT_R_16U_2BPP:
        case TEXTURE_FORMAT_R_16_2BPP:
        case TEXTURE_FORMAT_R_32F_4BPP:
        case TEXTURE_FORMAT_R_32I_4BPP:
        case TEXTURE_FORMAT_R_32U_4BPP:
        case TEXTURE_FORMAT_R_8I_1BPP:
        case TEXTURE_FORMAT_R_8U_1BPP:
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        case TEXTURE_FORMAT_RG_8_2BPP:
        case TEXTURE_FORMAT_R_8_1BPP:
        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        {
          stream.close();
          throw new UnreachableCodeException(
            new AssertionError(tt.toString()));
        }
        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        {
          try {
            tl.load2DStaticRGBA4444(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");
          } catch (final IOException e) {
            ++io_exception_count;
          } catch (final Exception e) {
            Assert.fail(e.getMessage());
          }
          break;
        }
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        {
          try {
            tl.load2DStaticRGBA5551(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");
          } catch (final IOException e) {
            ++io_exception_count;
          } catch (final Exception e) {
            Assert.fail(e.getMessage());
          }
          break;
        }
        case TEXTURE_FORMAT_RGB_565_2BPP:
        {
          try {
            tl.load2DStaticRGB565(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");
          } catch (final IOException e) {
            ++io_exception_count;
          } catch (final Exception e) {
            Assert.fail(e.getMessage());
          }
          break;
        }
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        {
          // Not yet implemented.
          ++io_exception_count;
          continue;
        }
      }

      stream.close();
    }

    Assert.assertEquals(TextureFormatMeta
      .getTextures2DRequiredByGLES2()
      .size(), io_exception_count);
  }

  @Test public final void testTextureTypesMonoToSpecific()
    throws JCGLException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_mono.png";
    TextureLoaderGLES2Contract.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesRGBAToSpecific()
    throws JCGLException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8888_4.png";
    TextureLoaderGLES2Contract.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesRGBToSpecific()
    throws JCGLException,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLTextures2DStaticGLES2Type gl = this.getGLTextures2D(tc);
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_888_3.png";
    TextureLoaderGLES2Contract.loadSpecific(fs, gl, tl, path);
  }

}
