package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
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

  @Test public void testGetUnits()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final TextureUnit[] u = gl.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }

  /**
   * Binding a texture works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public void testTextureBind()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

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
    final GLInterface gl = this.getGL();

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

  @Test(expected = ConstraintError.class) public void testTextureBindNull()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
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
    final GLInterface gl = this.getGL();
    gl.texture2DRGBAStaticBind(null, null);
  }

  /**
   * Deleting a texture works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public void testTextureDelete()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

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
    final GLInterface gl = this.getGL();

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
   * Deleting a null texture fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public void testTextureNullDelete()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
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
    final GLInterface gl = this.getGL();

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
    final GLInterface gl = this.getGL();

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

  @Test(expected = ConstraintError.class) public void testTextureNullName()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

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

  @Test(expected = ConstraintError.class) public void testTextureNullWrapS()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

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

  @Test(expected = ConstraintError.class) public void testTextureNullWrapT()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

    gl.texture2DRGBAStaticAllocate(
      "texture",
      64,
      64,
      TextureWrap.TEXTURE_WRAP_REPEAT,
      null,
      TextureFilter.TEXTURE_FILTER_NEAREST,
      TextureFilter.TEXTURE_FILTER_NEAREST);
  }
}
