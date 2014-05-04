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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnimplementedCodeException;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.CMFKNegativeX;
import com.io7m.jcanephora.CMFKNegativeY;
import com.io7m.jcanephora.CMFKNegativeZ;
import com.io7m.jcanephora.CMFKPositiveX;
import com.io7m.jcanephora.CMFKPositiveY;
import com.io7m.jcanephora.CMFKPositiveZ;
import com.io7m.jcanephora.CubeMapFaceInputStream;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLTextures2DStaticGL3;
import com.io7m.jcanephora.JCGLTexturesCubeStaticGL3;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureTypeMeta;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TextureLoaderContract;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class TextureLoaderContractGL3<T extends TextureLoaderType> extends
  TextureLoaderContract<JCGLTextures2DStaticGL3, JCGLTexturesCubeStaticGL3, T>
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  private void loadCubeLH(
    final @Nonnull FSCapabilityRead fs,
    final @Nonnull T tl,
    final @Nonnull JCGLTexturesCubeStaticGL3 gt,
    final @Nonnull String path)
    throws JCGLRuntimeException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    for (final TextureType tt : TextureTypeMeta
      .getTexturesCubeRequiredByGL3()) {
      TextureCubeStatic t = null;

      final CubeMapFaceInputStream<CMFKPositiveZ> stream_pz =
        new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeZ> stream_nz =
        new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKPositiveY> stream_py =
        new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeY> stream_ny =
        new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKPositiveX> stream_px =
        new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeX> stream_nx =
        new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
          .ofString(path)));

      switch (tt) {
        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          t =
            tl.loadCubeLHStaticRGBA8(
              gt,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream_pz,
              stream_nz,
              stream_py,
              stream_ny,
              stream_px,
              stream_nx,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          t =
            tl.loadCubeLHStaticRGB8(
              gt,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream_pz,
              stream_nz,
              stream_py,
              stream_ny,
              stream_px,
              stream_nx,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGB_8_3BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RG_8_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          stream_pz.close();
          stream_nz.close();
          stream_py.close();
          stream_ny.close();
          stream_px.close();
          stream_nx.close();
          System.err.println("Not yet part of the interface (" + tt + ")");
          return;
        }
      }

      assert t != null;
      Assert.assertFalse(t.resourceIsDeleted());
      Assert.assertEquals(256, t.getWidth());
      Assert.assertEquals(256, t.getHeight());
      Assert.assertEquals(
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        t.getMagnificationFilter());
      Assert.assertEquals(
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        t.getMinificationFilter());
      Assert.assertEquals("image", t.getName());
      Assert.assertEquals(TextureWrapR.TEXTURE_WRAP_REPEAT, t.getWrapR());
      Assert.assertEquals(
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        t.getWrapS());
      Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

      gt.textureCubeStaticDelete(t);
      Assert.assertTrue(t.resourceIsDeleted());

      stream_pz.close();
      stream_nz.close();
      stream_py.close();
      stream_ny.close();
      stream_px.close();
      stream_nx.close();
    }
  }

  private void loadCubeRH(
    final @Nonnull FSCapabilityRead fs,
    final @Nonnull T tl,
    final @Nonnull JCGLTexturesCubeStaticGL3 gt,
    final @Nonnull String path)
    throws JCGLRuntimeException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    for (final TextureType tt : TextureTypeMeta
      .getTexturesCubeRequiredByGL3()) {
      TextureCubeStatic t = null;

      final CubeMapFaceInputStream<CMFKPositiveZ> stream_pz =
        new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeZ> stream_nz =
        new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKPositiveY> stream_py =
        new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeY> stream_ny =
        new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKPositiveX> stream_px =
        new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
          .ofString(path)));
      final CubeMapFaceInputStream<CMFKNegativeX> stream_nx =
        new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
          .ofString(path)));

      switch (tt) {
        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          t =
            tl.loadCubeRHStaticRGBA8(
              gt,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream_pz,
              stream_nz,
              stream_py,
              stream_ny,
              stream_px,
              stream_nx,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          t =
            tl.loadCubeRHStaticRGB8(
              gt,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream_pz,
              stream_nz,
              stream_py,
              stream_ny,
              stream_px,
              stream_nx,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGB_8_3BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RG_8_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          stream_pz.close();
          stream_nz.close();
          stream_py.close();
          stream_ny.close();
          stream_px.close();
          stream_nx.close();
          System.err.println("Not yet part of the interface (" + tt + ")");
          return;
        }
      }

      assert t != null;
      Assert.assertFalse(t.resourceIsDeleted());
      Assert.assertEquals(256, t.getWidth());
      Assert.assertEquals(256, t.getHeight());
      Assert.assertEquals(
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        t.getMagnificationFilter());
      Assert.assertEquals(
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        t.getMinificationFilter());
      Assert.assertEquals("image", t.getName());
      Assert.assertEquals(TextureWrapR.TEXTURE_WRAP_REPEAT, t.getWrapR());
      Assert.assertEquals(
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        t.getWrapS());
      Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

      gt.textureCubeStaticDelete(t);
      Assert.assertTrue(t.resourceIsDeleted());

      stream_pz.close();
      stream_nz.close();
      stream_py.close();
      stream_ny.close();
      stream_px.close();
      stream_nx.close();
    }
  }

  private void loadSpecific(
    final @Nonnull FSCapabilityRead fs,
    final @Nonnull JCGLTextures2DStaticGL3 gl,
    final @Nonnull T tl,
    final @Nonnull String path)
    throws FilesystemError,
      ConstraintError,
      JCGLRuntimeException,
      IOException
  {
    for (final TextureType tt : TextureTypeMeta.getTextures2DRequiredByGL3()) {
      Texture2DStatic t = null;
      final InputStream stream = fs.openFile(PathVirtual.ofString(path));

      switch (tt) {
        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        {
          stream.close();
          throw new UnreachableCodeException();
        }
        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          t =
            tl.load2DStaticRGBA8(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          t =
            tl.load2DStaticRGB8(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_RGB_8_3BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        {
          t =
            tl.load2DStaticDepth16(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_DEPTH_16_2BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        {
          t =
            tl.load2DStaticDepth24(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_DEPTH_24_4BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          t =
            tl.load2DStaticDepth32f(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(
            TextureType.TEXTURE_TYPE_DEPTH_32F_4BPP,
            t.getType());
          break;
        }
        case TEXTURE_TYPE_RG_8_2BPP:
        {
          t =
            tl.load2DStaticRG8(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert
            .assertEquals(TextureType.TEXTURE_TYPE_RG_8_2BPP, t.getType());
          break;
        }
        case TEXTURE_TYPE_R_8_1BPP:
        {
          t =
            tl.load2DStaticR8(
              gl,
              TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
              stream,
              "image");

          Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
          break;
        }
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          stream.close();
          throw new UnimplementedCodeException();
        }
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          stream.close();
          throw new UnreachableCodeException();
        }
      }

      assert t != null;
      Assert.assertFalse(t.resourceIsDeleted());
      Assert.assertEquals(256, t.getWidth());
      Assert.assertEquals(256, t.getHeight());
      Assert.assertEquals(
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        t.getMagnificationFilter());
      Assert.assertEquals(
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        t.getMinificationFilter());
      Assert.assertEquals("image", t.getName());
      Assert.assertEquals(
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        t.getWrapS());
      Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

      gl.texture2DStaticDelete(t);
      Assert.assertTrue(t.resourceIsDeleted());

      stream.close();
    }
  }

  @Test public final void testCubeLH()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final T tl = this.makeTextureLoader(tc, this.getGLTextures2D(tc));
    final String path = "/com/io7m/jcanephora/images/reference_888_3.png";
    this.loadCubeLH(fs, tl, this.getGLTexturesCube(tc), path);
  }

  @Test public final void testCubeRH()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final T tl = this.makeTextureLoader(tc, this.getGLTextures2D(tc));
    final String path = "/com/io7m/jcanephora/images/reference_888_3.png";
    this.loadCubeRH(fs, tl, this.getGLTexturesCube(tc), path);
  }

  @Test public final void testTextureTypesCubeLHInferredGreyscale()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_grey.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredIndexed()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_index.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredMono()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_mono.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeLHInferredRGB()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_888_3.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredGreyscale()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_grey.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredIndexed()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_8_index.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredMono()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_mono.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesCubeRHInferredRGB()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gt = this.getGLTextures2D(tc);
    final JCGLTexturesCubeStaticGL3 gc = this.getGLTexturesCube(tc);
    final T tl = this.makeTextureLoader(tc, gt);

    final String file = "/com/io7m/jcanephora/images/reference_888_3.png";
    final CubeMapFaceInputStream<CMFKPositiveZ> pos_z =
      new CubeMapFaceInputStream<CMFKPositiveZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeZ> neg_z =
      new CubeMapFaceInputStream<CMFKNegativeZ>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveY> pos_y =
      new CubeMapFaceInputStream<CMFKPositiveY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeY> neg_y =
      new CubeMapFaceInputStream<CMFKNegativeY>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKPositiveX> pos_x =
      new CubeMapFaceInputStream<CMFKPositiveX>(fs.openFile(PathVirtual
        .ofString(file)));
    final CubeMapFaceInputStream<CMFKNegativeX> neg_x =
      new CubeMapFaceInputStream<CMFKNegativeX>(fs.openFile(PathVirtual
        .ofString(file)));

    final TextureCubeStatic t =
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

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gc.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  @Test public final void testTextureTypesGreyscaleToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8_grey.png";
    this.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesIndexedToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8_index.png";
    this.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesInferredGreyscale()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredIndexed()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredMono()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test(expected = IOException.class) public final
    void
    testTextureTypesInferredNotAnImage()
      throws ConstraintError,
        IOException,
        FilesystemError,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

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
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGB_8_3BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInferredRGBA()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        tc.getGLImplementation(),
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

    Assert.assertEquals(TextureType.TEXTURE_TYPE_RGBA_8_4BPP, t.getType());
    Assert.assertFalse(t.resourceIsDeleted());
    Assert.assertEquals(256, t.getWidth());
    Assert.assertEquals(256, t.getHeight());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
      t.getMagnificationFilter());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      t.getMinificationFilter());
    Assert.assertEquals("image", t.getName());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapS());
    Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t.getWrapT());

    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    stream.close();
  }

  @Test public final void testTextureTypesInvalidToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/not-an-image.txt";
    int io_exception_count = 0;

    for (final TextureType tt : TextureTypeMeta.getTextures2DRequiredByGL3()) {
      final InputStream stream = fs.openFile(PathVirtual.ofString(path));

      switch (tt) {
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        {
          stream.close();
          throw new UnreachableCodeException();
        }
        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          try {
            tl.load2DStaticRGBA8(
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
        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          try {
            tl.load2DStaticRGB8(
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
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        {
          try {
            tl.load2DStaticDepth16(
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
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        {
          try {
            tl.load2DStaticDepth24(
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
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          try {
            tl.load2DStaticDepth32f(
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
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          stream.close();
          throw new UnreachableCodeException();
        }
        case TEXTURE_TYPE_RG_8_2BPP:
        {
          try {
            tl.load2DStaticRG8(
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
        case TEXTURE_TYPE_R_8_1BPP:
        {
          try {
            tl.load2DStaticR8(
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
      }

      stream.close();
    }

    Assert.assertEquals(
      TextureTypeMeta.getTextures2DRequiredByGL3().size(),
      io_exception_count);
  }

  @Test public final void testTextureTypesMonoToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_mono.png";
    this.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesRGBAToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_8888_4.png";
    this.loadSpecific(fs, gl, tl, path);
  }

  @Test public final void testTextureTypesRGBToSpecific()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      IOException,
      FilesystemError
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLTextures2DStaticGL3 gl = this.getGLTextures2D(tc);
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/reference_888_3.png";
    this.loadSpecific(fs, gl, tl, path);
  }
}
