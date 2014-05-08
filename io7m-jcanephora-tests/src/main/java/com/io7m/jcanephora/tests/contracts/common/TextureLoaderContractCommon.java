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

package com.io7m.jcanephora.tests.contracts.common;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.tests.JCGLImplementation;
import com.io7m.jcanephora.tests.JCGLInterfaceCommon;
import com.io7m.jcanephora.tests.JCGLTextures2DStaticCommon;
import com.io7m.jcanephora.tests.JCGLTexturesCubeStaticCommon;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.Texture2DStatic;
import com.io7m.jcanephora.tests.contracts.TextureLoaderContract;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class TextureLoaderContractCommon<T extends TextureLoaderType> extends
  TextureLoaderContract<JCGLTextures2DStaticCommon, JCGLTexturesCubeStaticCommon, T>
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public final void testBug540405e7b9()
    throws IOException,
      ConstraintError,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);
    final String path = "/com/io7m/jcanephora/images/305x448.png";

    final InputStream stream = fs.openFile(PathVirtual.ofString(path));
    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        stream,
        "texture");
    stream.close();

    Assert.assertEquals(448, t.getHeight());
    Assert.assertEquals(305, t.getWidth());
  }

  @Test public final void testTextureTypesInferredGreyscale()
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_grey.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

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
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8_index.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

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
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_mono.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

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
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

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
    throws ConstraintError,
      IOException,
      FilesystemError,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityRead fs = tc.getFilesystem();
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_888_3.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

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
    final JCGLImplementation gi = tc.getGLImplementation();
    final JCGLInterfaceCommon gl = gi.getGLCommon();
    final T tl = this.makeTextureLoader(tc, gl);

    final InputStream stream =
      fs.openFile(PathVirtual
        .ofString("/com/io7m/jcanephora/images/reference_8888_4.png"));

    final Texture2DStatic t =
      tl.load2DStaticInferred(
        gi,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        stream,
        "image");

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
}
