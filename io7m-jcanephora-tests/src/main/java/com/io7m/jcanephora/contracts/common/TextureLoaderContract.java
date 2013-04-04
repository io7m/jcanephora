package com.io7m.jcanephora.contracts.common;

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
import com.io7m.jcanephora.GLInterfaceGL3;
import com.io7m.jcanephora.GLInterfaceGLES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;

public abstract class TextureLoaderContract implements
  TextureLoaderTestContract
{
  private static Texture2DStatic convertES2(
    final GLInterfaceGLES2 gl,
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
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return loader.load2DStaticRGB888(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return loader.load2DStaticRGBA4444(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return loader.load2DStaticRGBA5551(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return loader.load2DStaticRGBA8888(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_DEPTH_32_4BPP:
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
    final GLInterfaceGL3 gl,
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
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return loader.load2DStaticRGB888(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return loader.load2DStaticRGBA4444(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return loader.load2DStaticRGBA5551(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return loader.load2DStaticRGBA8888(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        return loader.load2DStaticRG88(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return loader.load2DStaticR8(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return loader.load2DStaticDepth16(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return loader.load2DStaticDepth24(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return loader.load2DStaticDepth32f(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());
      }
      case TEXTURE_TYPE_DEPTH_32_4BPP:
      {
        return loader.load2DStaticDepth32(
          gl,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
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

  /**
   * Inferring a RGB888 texture from a monochrome image works.
   * 
   * @throws GLException
   * 
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testGrey8InferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");

    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gl);
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

  @Test public final void testGrey8InferredGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");

    final Texture2DStatic texture =
      loader.load2DStaticInferredGL3(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(TextureType.TEXTURE_TYPE_R_8_1BPP, texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gl);
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

  @Test public final void testGreyscale8ToTypesSpecificES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testGreyscale8ToTypesSpecificGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testIndexed8InferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testIndexed8InferredGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredGL3(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testIndexed8ToTypesSpecificES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testIndexed8ToTypesSpecificGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testMonoInferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_mono.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testMonoToTypesSpecificES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_mono.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testMonoToTypesSpecificGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_mono.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB8888InferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB8888InferredGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB888InferredES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB888InferredGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferredES2(
        gl,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        stream,
        "image");

    Assert.assertFalse(texture.resourceIsDeleted());
    Assert.assertEquals(256, texture.getWidth());
    Assert.assertEquals(256, texture.getHeight());
    Assert.assertEquals(
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB888ToTypesSpecificES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGB888ToTypesSpecificGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGBA8888ToTypesSpecificES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    final GLInterfaceGLES2 gl = gi.implementationGetGLES2();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.getES2Types()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertES2(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
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

  @Test public final void testRGBA8888ToTypesSpecificGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.newTestContext();
    final GLImplementation gi = tc.getGLImplementation();
    final FilesystemAPI fs = tc.getFilesystem();

    Assume.assumeTrue(gi.implementationProvidesGL3());

    final GLInterfaceGL3 gl = gi.implementationGetGL3();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");

      final Texture2DStatic texture =
        TextureLoaderContract.convertGL3(gl, loader, type, stream);

      assert texture != null;

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gi.implementationGetGLES2());
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }
}
