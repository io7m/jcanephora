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

import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLTextureUnits;
import com.io7m.jcanephora.JCGLTexturesCubeStaticCommon;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jtensors.VectorI3I;

public abstract class TextureCubeStaticContract<T extends JCGLTexturesCubeStaticCommon> implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract T getGLTextureCubeStatic(
    final @Nonnull TestContext tc);

  public abstract JCGLTextureUnits getGLTextureUnits(
    final @Nonnull TestContext tc);

  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final List<TextureUnit> u = gu.textureGetUnits();
    Assert.assertTrue(u.size() >= 2);
  }

  /**
   * Binding a texture works.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test public final void testTextureBind()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final List<TextureUnit> units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticBind(units.get(0), t);
    Assert.assertTrue(gl.textureCubeStaticIsBound(units.get(0), t));
    gl.textureCubeStaticUnbind(units.get(0));
    Assert.assertFalse(gl.textureCubeStaticIsBound(units.get(0), t));
    gl.textureCubeStaticDelete(t);
  }

  /**
   * Binding a deleted texture fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final List<TextureUnit> units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticDelete(t);
    gl.textureCubeStaticBind(units.get(0), t);
  }

  /**
   * Binding a null texture fails.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final List<TextureUnit> units = gu.textureGetUnits();

    gl.textureCubeStaticBind(units.get(0), null);
  }

  /**
   * Binding a null unit fails.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindUnitNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test public final void testTextureDelete()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
  }

  /**
   * Deleting a texture twice fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureDeleteDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.textureCubeStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    gl.textureCubeStaticDelete(t);
  }

  /**
   * Checking if a deleted texture is bound fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureIsBoundDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);
    final JCGLTextureUnits gu = this.getGLTextureUnits(tc);

    final List<TextureUnit> units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticBind(units.get(0), t);
    gl.textureCubeStaticDelete(t);
    gl.textureCubeStaticIsBound(units.get(0), t);
  }

  /**
   * Deleting a null texture fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullDelete()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticDelete(null);
  }

  /**
   * Passing null for the magnification filter fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullFilterMax()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      "texture",
      64,
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      null);
  }

  /**
   * Passing null for the minification filter fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullFilterMin()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      "texture",
      64,
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the texture name fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullName()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      null,
      64,
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the R texture wrap parameter fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapR()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      "texture",
      64,
      null,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the S texture wrap parameter fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapS()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      "texture",
      64,
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      null,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Passing null for the T texture wrap parameter fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapT()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticAllocateRGBA8(
      "texture",
      64,
      TextureWrapR.TEXTURE_WRAP_REPEAT,
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Maximum texture size is sane.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test public final void testTextureSizeSane()
    throws JCGLRuntimeException,
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
   * @throws JCGLRuntimeException
   */

  @Test public final void testTextureUpdateCompleteSimple()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGB8(
        "xyz",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final TextureCubeWritableData update = new TextureCubeWritableData(t);
    final SpatialCursorWritable3i cursor = update.getCursor3i();

    Assert.assertTrue(cursor.isValid());

    while (cursor.isValid()) {
      cursor.put3i(new VectorI3I(0x0, 0x0, 0xff));

    }

    gl
      .textureCubeStaticUpdateLH(CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X, update);
  }

  /**
   * Passing null as a face fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFaceFails()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGB8(
        "xyz",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final TextureCubeWritableData update = new TextureCubeWritableData(t);
    gl.textureCubeStaticUpdateLH(null, update);
  }

  /**
   * Passing null as a texture update fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFails()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final T gl = this.getGLTextureCubeStatic(tc);

    gl.textureCubeStaticUpdateLH(CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X, null);
  }
}
