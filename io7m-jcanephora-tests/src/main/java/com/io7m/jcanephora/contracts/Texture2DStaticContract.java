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

package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLTextureUnits;
import com.io7m.jcanephora.JCGLTextures2DStaticCommon;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;

public abstract class Texture2DStaticContract<T extends JCGLTextures2DStaticCommon> implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract T getGLTexture2DStatic(
    TestContext tc);

  public abstract JCGLTextureUnits getGLTextureUnits(
    TestContext tc);

  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnits gl = this.getGLTextureUnits(tc);

    final TextureUnit[] u = gl.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }

  /**
   * Binding a texture works.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test public final void testTextureBind()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units[0], t);
    Assert.assertTrue(gl.texture2DStaticIsBound(units[0], t));
    gl.texture2DStaticUnbind(units[0]);
    Assert.assertFalse(gl.texture2DStaticIsBound(units[0], t));
    gl.texture2DStaticDelete(t);
  }

  /**
   * Binding a deleted texture fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticDelete(t);
    gl.texture2DStaticBind(units[0], t);
  }

  /**
   * Binding a null texture fails.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();

    gl.texture2DStaticBind(units[0], null);
  }

  /**
   * Binding a null unit fails.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindUnitNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test public final void testTextureDelete()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  /**
   * Deleting a texture twice fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureDeleteDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
  }

  /**
   * Checking if a deleted texture is bound fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureIsBoundDeleted()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units[0], t);
    gl.texture2DStaticDelete(t);
    gl.texture2DStaticIsBound(units[0], t);
  }

  /**
   * Deleting a null texture fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullDelete()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticDelete(null);
  }

  /**
   * Passing null for the magnification filter fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullFilterMax()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      null);
  }

  /**
   * Passing null for the minification filter fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullFilterMin()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the texture name fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullName()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticAllocateRGBA8888(
      null,
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the S texture wrap parameter fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapS()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      null,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the T texture wrap parameter fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapT()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Maximum texture size is sane.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test public final void testTextureSizeSane()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    Assert.assertTrue(gu.textureGetMaximumSize() >= 128);
  }

  /**
   * Texture updates work.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   */

  @Test public final void testTextureUpdateCompleteSimple()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGB888(
        "xyz",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Texture2DWritableData update = new Texture2DWritableData(t);
    final SpatialCursorWritable3i cursor = update.getCursor3i();

    Assert.assertTrue(cursor.canWrite());

    while (cursor.canWrite()) {
      cursor.put3i(0x0, 0x0, 0xff);
      cursor.next();
    }

    gl.texture2DStaticUpdate(update);
  }

  /**
   * Passing null as a texture update fails.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFails()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticUpdate(null);
  }
}
