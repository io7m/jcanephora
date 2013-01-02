package com.io7m.jcanephora.contracts_ES2;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
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
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Inferring a RED texture from a monochrome image works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testGrey8Inferred()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferred(
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
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testGreyscale8ToTypesSpecific()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_grey.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring an RGB565 texture from an 8-bit indexed color image works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testIndexed8Inferred()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferred(
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
      TextureType.TEXTURE_TYPE_RGB_565_2BPP,
      texture.getType());
    Assert.assertEquals("image", texture.getName());
    texture.resourceDelete(gl);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Created textures have the correct types when converted from 8-bit
   * indexed.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testIndexed8ToTypesSpecific()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8_index.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring a RED texture from a monochrome image works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testMonoInferred()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_mono.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferred(
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
   * Created textures have the correct types when converted from 1-bit
   * monochrome.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testMonoToTypesSpecific()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_mono.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Inferring an RGBA8888 texture from an RGBA8888 image works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testRGB8888Inferred()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferred(
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
    texture.resourceDelete(gl);
    Assert.assertTrue(texture.resourceIsDeleted());
    stream.close();
  }

  /**
   * Inferring an RGB888 texture from an RGB888 image works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testRGB888Inferred()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    final InputStream stream =
      fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");
    final Texture2DStatic texture =
      loader.load2DStaticInferred(
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
   * Created textures have the correct types when converted from RGBA888.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testRGB888ToTypesSpecific()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_888_3.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }

  /**
   * Created textures have the correct types when converted from RGBA8888.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testRGBA8888ToTypesSpecific()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    final FilesystemAPI fs = tc.getFilesystem();

    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream =
        fs.openFile("/com/io7m/jcanephora/images/reference_8888_4.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(256, texture.getWidth());
      Assert.assertEquals(256, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }
}
