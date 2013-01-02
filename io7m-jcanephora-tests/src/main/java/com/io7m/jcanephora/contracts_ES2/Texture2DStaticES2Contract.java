package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;

public abstract class Texture2DStaticES2Contract implements GLES2TestContract
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
      GLException,
      GLUnsupportedException
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final TextureUnit[] u = gl.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }

  /**
   * Binding a texture works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testTextureBind()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
    gl.texture2DStaticUnbind(units[0]);
    Assert.assertFalse(gl.texture2DStaticIsBound(units[0], t));
    gl.texture2DStaticDelete(t);
  }

  /**
   * Binding a deleted texture fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureBindDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureBindNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    final TextureUnit[] units = gl.textureGetUnits();

    gl.texture2DStaticBind(units[0], null);
  }

  /**
   * Binding a null unit fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureBindUnitNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    gl.texture2DStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testTextureDelete()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureDeleteDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureIsBoundDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullDelete()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    gl.texture2DStaticDelete(null);
  }

  /**
   * Passing null for the magnification filter fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureNullFilterMax()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureNullFilterMin()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullName()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapS()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureNullWrapT()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testTextureSizeSane()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    Assert.assertTrue(gl.textureGetMaximumSize() >= 128);
  }

  /**
   * Textures have the correct type.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testTextureTypes()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

    for (final TextureType t : TextureType.getES2Types()) {
      Texture2DStatic tx = null;

      switch (t) {
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        {
          tx =
            gl.texture2DStaticAllocateRGBA4444(
              t.toString(),
              64,
              128,
              TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_LINEAR,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        {
          tx =
            gl.texture2DStaticAllocateRGBA5551(
              t.toString(),
              64,
              128,
              TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_LINEAR,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          tx =
            gl.texture2DStaticAllocateRGBA8888(
              t.toString(),
              64,
              128,
              TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_LINEAR,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_565_2BPP:
        {
          tx =
            gl.texture2DStaticAllocateRGB565(
              t.toString(),
              64,
              128,
              TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_LINEAR,
              TextureFilter.TEXTURE_FILTER_NEAREST);
          Assert.assertEquals(tx.getType(), t);
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          tx =
            gl.texture2DStaticAllocateRGB888(
              t.toString(),
              64,
              128,
              TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
              TextureWrap.TEXTURE_WRAP_REPEAT,
              TextureFilter.TEXTURE_FILTER_LINEAR,
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

      assert tx != null;

      Assert.assertEquals(t.toString(), tx.getName());
      Assert.assertEquals(64, tx.getWidth());
      Assert.assertEquals(128, tx.getHeight());
      Assert.assertEquals(
        TextureWrap.TEXTURE_WRAP_CLAMP_TO_EDGE,
        tx.getWrapS());
      Assert.assertEquals(TextureWrap.TEXTURE_WRAP_REPEAT, tx.getWrapT());
      Assert.assertEquals(
        TextureFilter.TEXTURE_FILTER_LINEAR,
        tx.getMinificationFilter());
      Assert.assertEquals(
        TextureFilter.TEXTURE_FILTER_NEAREST,
        tx.getMagnificationFilter());
    }
  }

  /**
   * Texture updates work.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testTextureUpdateCompleteSimple()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();

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
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureUpdateNullFails()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final GLInterfaceES2 gl =
      this.makeNewGLImplementation().implementationGetGLES2();
    gl.texture2DStaticUpdate(null);
  }
}
