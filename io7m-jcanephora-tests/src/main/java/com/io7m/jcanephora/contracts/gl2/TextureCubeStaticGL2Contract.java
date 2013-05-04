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

package com.io7m.jcanephora.contracts.gl2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.CubeMapFace;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLTextureUnits;
import com.io7m.jcanephora.GLTexturesCubeStaticGL2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class TextureCubeStaticGL2Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLTexturesCubeStaticGL2 getGLTextureCubeStaticGL2(
    TestContext tc);

  public abstract GLTextureUnits getGLTextureUnits(
    TestContext tc);

  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] u = gu.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }

  /**
   * Binding a texture works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testTextureBind()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8888(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticBind(units[0], t);
    Assert.assertTrue(gl.textureCubeStaticIsBound(units[0], t));
    gl.textureCubeStaticUnbind(units[0]);
    Assert.assertFalse(gl.textureCubeStaticIsBound(units[0], t));
    gl.textureCubeStaticDelete(t);
  }

  /**
   * Binding a deleted texture fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureBindDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8888(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticDelete(t);
    gl.textureCubeStaticBind(units[0], t);
  }

  /**
   * Binding a null texture fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();

    gl.textureCubeStaticBind(units[0], null);
  }

  /**
   * Binding a null unit fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureBindUnitNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testTextureDelete()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureDeleteDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureIsBoundDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    final TextureUnit[] units = gu.textureGetUnits();
    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGBA8888(
        "texture",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    gl.textureCubeStaticBind(units[0], t);
    gl.textureCubeStaticDelete(t);
    gl.textureCubeStaticIsBound(units[0], t);
  }

  /**
   * Deleting a null texture fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullDelete()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticDelete(null);
  }

  /**
   * Passing null for the magnification filter fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureNullFilterMax()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureNullFilterMin()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullName()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapR()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapS()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapT()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticAllocateRGBA8888(
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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testTextureSizeSane()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTextureUnits gu = this.getGLTextureUnits(tc);

    Assert.assertTrue(gu.textureGetMaximumSize() >= 128);
  }

  /**
   * Textures have the correct type.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public final void testTextureTypes()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    for (final TextureType t : TextureType.values()) {
      switch (t) {
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA4444(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA5551(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGBA8888(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB565(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          final TextureCubeStatic tx =
            gl.textureCubeStaticAllocateRGB888(
              t.toString(),
              128,
              TextureWrapR.TEXTURE_WRAP_REPEAT,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_32_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
          break;
      }
    }
  }

  /**
   * Texture updates work.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public final void testTextureUpdateCompleteSimple()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGB888(
        "xyz",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final TextureCubeWritableData update = new TextureCubeWritableData(t);
    final SpatialCursorWritable3i cursor = update.getCursor3i();

    Assert.assertTrue(cursor.canWrite());

    while (cursor.canWrite()) {
      cursor.put3i(0x0, 0x0, 0xff);
      cursor.next();
    }

    gl.textureCubeStaticUpdate(CubeMapFace.CUBE_MAP_NEGATIVE_X, update);
  }

  /**
   * Passing null as a face fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFaceFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    final TextureCubeStatic t =
      gl.textureCubeStaticAllocateRGB888(
        "xyz",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final TextureCubeWritableData update = new TextureCubeWritableData(t);
    gl.textureCubeStaticUpdate(null, update);
  }

  /**
   * Passing null as a texture update fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLTexturesCubeStaticGL2 gl = this.getGLTextureCubeStaticGL2(tc);

    gl.textureCubeStaticUpdate(CubeMapFace.CUBE_MAP_NEGATIVE_X, null);
  }
}
