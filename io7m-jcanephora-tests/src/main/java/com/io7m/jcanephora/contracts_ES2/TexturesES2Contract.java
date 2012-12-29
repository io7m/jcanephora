package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;

public abstract class TexturesES2Contract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final TextureUnit[] u = gl.textureGetUnits();
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
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units[0], t);
    Assert.assertTrue(gl.texture2DStaticIsBound(units[0], t));
    gl.textureUnitUnbind(units[0]);
    Assert.assertFalse(gl.texture2DStaticIsBound(units[0], t));
    gl.texture2DStaticDelete(t);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticDelete(t);
    gl.texture2DStaticBind(units[0], t);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    final TextureUnit[] units = gl.textureGetUnits();

    gl.texture2DStaticBind(units[0], null);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.texture2DStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testTextureDelete()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    gl.texture2DStaticDelete(t);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGBA8888(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DStaticBind(units[0], t);
    gl.texture2DStaticDelete(t);
    gl.texture2DStaticIsBound(units[0], t);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.texture2DStaticDelete(null);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_NEAREST,
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilter.TEXTURE_FILTER_NEAREST);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    gl.texture2DStaticAllocateRGBA8888(
      null,
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_NEAREST,
      TextureFilter.TEXTURE_FILTER_NEAREST);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      null,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      TextureFilter.TEXTURE_FILTER_NEAREST,
      TextureFilter.TEXTURE_FILTER_NEAREST);
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
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    gl.texture2DStaticAllocateRGBA8888(
      "texture",
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilter.TEXTURE_FILTER_NEAREST,
      TextureFilter.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Maximum texture size is sane.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testTextureSizeSane()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    Assert.assertTrue(gl.textureGetMaximumSize() >= 128);
  }

  /**
   * Textures have the correct type.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public final void testTextureTypes()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    for (final TextureType t : TextureType.getES2Types()) {
      switch (t) {
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGBA4444(
              t.toString(),
              128,
              128,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_NEAREST,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGBA5551(
              t.toString(),
              128,
              128,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_NEAREST,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGBA8888(
              t.toString(),
              128,
              128,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_NEAREST,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGB565(
              t.toString(),
              128,
              128,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_NEAREST,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          final Texture2DStatic tx =
            gl.texture2DStaticAllocateRGB888(
              t.toString(),
              128,
              128,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_NEAREST,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_32_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        {
          throw new UnreachableCodeException();
        }
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
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final Texture2DStatic t =
      gl.texture2DStaticAllocateRGB888(
        "xyz",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFails()
      throws GLException,
        ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();
    gl.texture2DStaticUpdate(null);
  }
}
