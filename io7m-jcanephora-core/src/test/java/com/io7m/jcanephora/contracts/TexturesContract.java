package com.io7m.jcanephora.contracts;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.PixelUnpackBuffer;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;

public abstract class TexturesContract implements GLTestContract
{
  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public final void testGetUnits()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
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
    final GLInterface gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DRGBAStaticBind(units[0], t);
    Assert.assertTrue(gl.texture2DRGBAStaticIsBound(units[0], t));
    gl.texture2DRGBAStaticUnbind(units[0]);
    Assert.assertFalse(gl.texture2DRGBAStaticIsBound(units[0], t));
    gl.texture2DRGBAStaticDelete(t);
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
    final GLInterface gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DRGBAStaticDelete(t);
    gl.texture2DRGBAStaticBind(units[0], t);
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
    final GLInterface gl = this.makeNewGL();
    final TextureUnit[] units = gl.textureGetUnits();

    gl.texture2DRGBAStaticBind(units[0], null);
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
    final GLInterface gl = this.makeNewGL();
    gl.texture2DRGBAStaticBind(null, null);
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
    final GLInterface gl = this.makeNewGL();

    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DRGBAStaticDelete(t);
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
    final GLInterface gl = this.makeNewGL();

    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    Assert.assertFalse(t.resourceIsDeleted());
    gl.texture2DRGBAStaticDelete(t);
    Assert.assertTrue(t.resourceIsDeleted());
    gl.texture2DRGBAStaticDelete(t);
  }

  /**
   * Retrieving the image of a texture doesn't fail.
   */

  @Test public final void testTextureGetImage()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final ByteBuffer b = gl.texture2DRGBAStaticGetImage(t);

    Assert.assertEquals((64 * 4) * 64, b.capacity());
  }

  /**
   * Retrieving the image of a deleted texture fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureGetImageDeleted()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    t.resourceDelete(gl);
    gl.texture2DRGBAStaticGetImage(t);
  }

  /**
   * Retrieving the image of a null texture fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testTextureGetImageNull()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.texture2DRGBAStaticGetImage(null);
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
    final GLInterface gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    gl.texture2DRGBAStaticBind(units[0], t);
    gl.texture2DRGBAStaticDelete(t);
    gl.texture2DRGBAStaticIsBound(units[0], t);
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
    final GLInterface gl = this.makeNewGL();
    gl.texture2DRGBAStaticDelete(null);
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
    final GLInterface gl = this.makeNewGL();

    gl.texture2DRGBAStaticAllocate(
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
    final GLInterface gl = this.makeNewGL();

    gl.texture2DRGBAStaticAllocate(
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
    final GLInterface gl = this.makeNewGL();

    gl.texture2DRGBAStaticAllocate(
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
    final GLInterface gl = this.makeNewGL();

    gl.texture2DRGBAStaticAllocate(
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
    final GLInterface gl = this.makeNewGL();

    gl.texture2DRGBAStaticAllocate(
      "texture",
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilter.TEXTURE_FILTER_NEAREST,
      TextureFilter.TEXTURE_FILTER_NEAREST);
  }

  /**
   * Replacing a texture doesn't fail.
   */

  @Test public final void testTextureReplace()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.makeNewGL();

    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    {
      final ByteBuffer b = gl.texture2DRGBAStaticGetImage(t);
      Assert.assertEquals((64 * 4) * 64, b.capacity());
    }

    gl.texture2DRGBAStaticReplace(t);

    {
      final ByteBuffer b = gl.texture2DRGBAStaticGetImage(t);
      Assert.assertEquals((64 * 4) * 64, b.capacity());
    }
  }

  /**
   * Replacing a deleted texture fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureReplaceDeleted()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();

    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    t.resourceDelete(gl);
    gl.texture2DRGBAStaticReplace(t);
  }

  /**
   * Replacing a null texture fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureReplaceNull()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.texture2DRGBAStaticReplace(null);
  }

  /**
   * Replacing a texture with a deleted unpack buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testTextureReplaceUnpackBufferDeleted()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();

    final Texture2DRGBAStatic t =
      gl.texture2DRGBAStaticAllocate(
        "texture",
        64,
        64,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final PixelUnpackBuffer b = t.getBuffer();
    b.resourceDelete(gl);

    gl.texture2DRGBAStaticReplace(t);
  }
}
