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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLTextures2DStaticCommon;
import com.io7m.jcanephora.GLTextures2DStaticGL3;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.common.TextureLoaderTestContract;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class TextureLoaderContractGL3 implements
  TextureLoaderTestContract
{
  private static Texture2DStatic convertES2(
    final GLTextures2DStaticCommon gl,
    final TextureLoader loader,
    final TextureType type,
    final InputStream stream)
    throws ConstraintError,
      GLException,
      IOException
  {
    switch (type) {
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return loader.load2DStaticRGB565(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return loader.load2DStaticRGB888(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return loader.load2DStaticRGBA4444(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return loader.load2DStaticRGBA5551(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return loader.load2DStaticRGBA8888(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        stream.close();
        throw new UnreachableCodeException();
      }
    }

    throw new UnreachableCodeException();
  }

  private static Texture2DStatic convertGL3(
    final GLTextures2DStaticGL3 gl,
    final TextureLoader loader,
    final TextureType type,
    final InputStream stream)
    throws ConstraintError,
      GLException,
      IOException
  {
    switch (type) {
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return loader.load2DStaticRGB565(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return loader.load2DStaticRGB888(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return loader.load2DStaticRGBA4444(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return loader.load2DStaticRGBA5551(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return loader.load2DStaticRGBA8888(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        return loader.load2DStaticRG88(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return loader.load2DStaticR8(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return loader.load2DStaticDepth16(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return loader.load2DStaticDepth24(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return loader.load2DStaticDepth32f(
          gl,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
    }

    throw new UnreachableCodeException();
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLTextures2DStaticGL3 getGLTextures2DStaticGL3(
    TestContext tc);

  /**
   * Inferring a RGB888 texture from a monochrome image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testGrey8InferredES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring a red texture from a monochrome image works on OpenGL 3.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testGrey8InferredGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStatic texture =
      loader.load2DStaticInferredGL3(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Created textures have the correct types when converted from 8-bit
   * greyscale.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testGreyscale8ToTypesSpecificES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from 8-bit
   * greyscale.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testGreyscale8ToTypesSpecificGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring an RGB888 texture from an 8-bit indexed color image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testIndexed8InferredES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring a red texture from an 8-bit indexed color image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testIndexed8InferredGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGL3(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Created textures have the correct types when converted from 8-bit
   * indexed.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testIndexed8ToTypesSpecificES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from 8-bit
   * indexed.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testIndexed8ToTypesSpecificGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring an RGB texture from a monochrome image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final void testMonoInferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_mono.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Created textures have the correct types when converted from 1-bit
   * monochrome.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testMonoToTypesSpecificES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from 1-bit
   * monochrome.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testMonoToTypesSpecificGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring an RGBA8888 texture from an RGBA8888 image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB8888InferredES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring an RGBA8888 texture from an RGBA8888 image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB8888InferredGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring an RGB888 texture from an RGB888 image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB888InferredES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring an RGB888 texture from an RGB888 image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB888InferredGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));
    final Texture2DStatic texture =
      loader.load2DStaticInferredGLES2(
        gl,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    gl.texture2DStaticDelete(texture);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Created textures have the correct types when converted from RGBA888.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB888ToTypesSpecificES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from RGBA888.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGB888ToTypesSpecificGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from RGBA8888.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGBA8888ToTypesSpecificES2()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from RGBA8888.
   * 
   * @throws ConstraintError
   * @throws GLException
   * 
   * @throws FilesystemError
   * @throws IOException
   */

  @Test @SuppressWarnings("resource") public final
    void
    testRGBA8888ToTypesSpecificGL3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        FilesystemError,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FSCapabilityAll fs = tc.getFilesystem();

    Assume.assumeTrue(gi.getGL3().isSome());

    final GLTextures2DStaticGL3 gl = this.getGLTextures2DStaticGL3(tc);
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

      final Texture2DStatic texture =
        TextureLoaderContractGL3.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      gl.texture2DStaticDelete(texture);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }
}
