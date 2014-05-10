/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts.common;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticCommonType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TextureLoaderContract;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

public abstract class TextureLoaderContractCommon<T extends TextureLoaderType> extends
  TextureLoaderContract<JCGLTextures2DStaticCommonType, JCGLTexturesCubeStaticCommonType, T>
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public final void testBug540405e7b9()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/305x448.png";

    final InputStream stream = fs.openFile(PathVirtual.ofString(path));
    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "texture");
    stream.close();

    Assert.assertEquals(448, t.textureGetHeight());
    Assert.assertEquals(305, t.textureGetWidth());
  }

  @Test public final void testTextureTypesInferredGreyscale()
    throws IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
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
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
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
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
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
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/not-an-image.txt"));

    tl.load2DStaticInferred(
      gi,
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
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
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
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLInterfaceCommonType gl = gi.getGLCommon();
    final TextureLoaderType tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

    final Texture2DStaticType t =
      tl.load2DStaticInferred(
        gi,
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
}