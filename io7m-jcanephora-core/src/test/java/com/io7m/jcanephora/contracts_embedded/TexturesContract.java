package com.io7m.jcanephora.contracts_embedded;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;

public abstract class TexturesContract implements GLEmbeddedTestContract
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocate(
        "texture",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocate(
        "texture",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    final Texture2DStatic t =
      gl.texture2DStaticAllocate(
        "texture",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    final Texture2DStatic t =
      gl.texture2DStaticAllocate(
        "texture",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    final TextureUnit[] units = gl.textureGetUnits();
    final Texture2DStatic t =
      gl.texture2DStaticAllocate(
        "texture",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    gl.texture2DStaticAllocate(
      "texture",
      64,
      64,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    gl.texture2DStaticAllocate(
      "texture",
      64,
      64,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    gl.texture2DStaticAllocate(
      null,
      64,
      64,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    gl.texture2DStaticAllocate(
      "texture",
      64,
      64,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();

    gl.texture2DStaticAllocate(
      "texture",
      64,
      64,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    Assert.assertTrue(gl.textureGetMaximumSize() >= 128);
  }
}
