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

package com.io7m.jcanephora.tests.contracts;

import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.SpatialCursorWritable4iType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdate;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;
import com.io7m.jtensors.VectorI4I;

@SuppressWarnings("null") public abstract class Texture2DStaticContract<T extends JCGLTextures2DStaticCommonType> implements
  TestContract
{
  public abstract Texture2DStaticType allocateTextureRGBA(
    final T t,
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification filter_min,
    final TextureFilterMagnification filter_mag)
    throws JCGLException;

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract T getGLTexture2DStatic(
    TestContext tc);

  public abstract JCGLTextureUnitsType getGLTextureUnits(
    TestContext tc);

  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnitsType gl = this.getGLTextureUnits(tc);

    final List<TextureUnitType> u = gl.textureGetUnits();
    Assert.assertTrue(u.size() >= 2);
  }

  /**
   * The list of texture units is not modifiable.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnitsImmutable()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnitsType gl = this.getGLTextureUnits(tc);
    final List<TextureUnitType> u = gl.textureGetUnits();

    boolean caught = false;
    try {
      u.remove(0);
    } catch (final UnsupportedOperationException x) {
      caught = true;
    }

    Assert.assertTrue(caught);
  }

  /**
   * Binding a texture works.
   */

  @Test public final void testTextureBind()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnitsType gu = this.getGLTextureUnits(tc);

    final List<TextureUnitType> units = gu.textureGetUnits();
    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units.get(0), t);
    Assert.assertTrue(gl.texture2DStaticIsBound(units.get(0), t));
    gl.texture2DStaticUnbind(units.get(0));
    Assert.assertFalse(gl.texture2DStaticIsBound(units.get(0), t));
    gl.texture2DStaticDelete(t);
  }

  /**
   * Binding a deleted texture fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testTextureBindDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnitsType gu = this.getGLTextureUnits(tc);

    final List<TextureUnitType> units = gu.textureGetUnits();
    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticDelete(t);
    gl.texture2DStaticBind(units.get(0), t);
  }

  /**
   * Binding a null texture fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureBindNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnitsType gu = this.getGLTextureUnits(tc);

    final List<TextureUnitType> units = gu.textureGetUnits();
    gl.texture2DStaticBind(
      units.get(0),
      (Texture2DStaticUsableType) TestUtilities.actuallyNull());
  }

  /**
   * Binding a null unit fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureBindUnitNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticBind(
      (TextureUnitType) TestUtilities.actuallyNull(),
      (Texture2DStaticUsableType) TestUtilities.actuallyNull());
  }

  /**
   * Deleting a texture works.
   */

  @Test public final void testTextureDelete()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
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
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testTextureDeleteDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
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
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testTextureIsBoundDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);
    final JCGLTextureUnitsType gu = this.getGLTextureUnits(tc);

    final List<TextureUnitType> units = gu.textureGetUnits();
    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
        "texture",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units.get(0), t);
    gl.texture2DStaticDelete(t);
    gl.texture2DStaticIsBound(units.get(0), t);
  }

  /**
   * Deleting a null texture fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullDelete()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticDelete((Texture2DStaticType) TestUtilities
      .actuallyNull());
  }

  /**
   * Passing null for the magnification filter fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullFilterMax()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    this.allocateTextureRGBA(
      gl,
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      (TextureFilterMagnification) TestUtilities.actuallyNull());
  }

  /**
   * Passing null for the minification filter fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullFilterMin()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    this.allocateTextureRGBA(
      gl,
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      (TextureFilterMinification) TestUtilities.actuallyNull(),
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the texture name fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    this.allocateTextureRGBA(
      gl,
      (String) TestUtilities.actuallyNull(),
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the S texture wrap parameter fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullWrapS()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    this.allocateTextureRGBA(
      gl,
      "texture",
      64,
      64,
      (TextureWrapS) TestUtilities.actuallyNull(),
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the T texture wrap parameter fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureNullWrapT()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    this.allocateTextureRGBA(
      gl,
      "texture",
      64,
      64,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      (TextureWrapT) TestUtilities.actuallyNull(),
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Maximum texture size is sane.
   */

  @Test public final void testTextureSizeSane()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnitsType gu = this.getGLTextureUnits(tc);

    Assert.assertTrue(gu.textureGetMaximumSize() >= 128);
  }

  /**
   * Texture updates work.
   */

  @Test public final void testTextureUpdateCompleteSimple()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    final Texture2DStaticType t =
      this.allocateTextureRGBA(
        gl,
        "xyz",
        64,
        64,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final Texture2DStaticUpdateType update =
      Texture2DStaticUpdate.newReplacingAll(t);

    final SpatialCursorWritable4iType cursor = update.getCursor4i();

    Assert.assertTrue(cursor.isValid());

    while (cursor.isValid()) {
      cursor.put4i(new VectorI4I(0x0, 0x0, 0xff, 0xff));

    }

    gl.texture2DStaticUpdate(update);
  }

  /**
   * Passing null as a texture update fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testTextureUpdateNullFails()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTexture2DStatic(tc);

    gl.texture2DStaticUpdate((Texture2DStaticUpdateType) TestUtilities
      .actuallyNull());
  }
}
