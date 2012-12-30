package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class TextureCubeStaticTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final TextureCubeStatic t0 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final TextureCubeStatic t1 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256);
    final TextureCubeStatic t2 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);

    Assert.assertEquals(t0, t0);
    Assert.assertEquals(t0, t2);
    Assert.assertEquals(t2, t0);
    Assert.assertFalse(t0.equals(t1));
    Assert.assertFalse(t0.equals(null));
    Assert.assertFalse(t0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final TextureCubeStatic t0 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final TextureCubeStatic t1 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256);
    final TextureCubeStatic t2 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final TextureCubeStatic t3 =
      new TextureCubeStatic(
        "abc",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256);
    final TextureCubeStatic t4 =
      new TextureCubeStatic(
        "abc",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);

    Assert.assertTrue(t0.hashCode() == t0.hashCode());
    Assert.assertTrue(t0.hashCode() == t2.hashCode());
    Assert.assertTrue(t2.hashCode() == t0.hashCode());

    Assert.assertTrue(t0.hashCode() != t1.hashCode());
    Assert.assertTrue(t0.hashCode() != t3.hashCode());
    Assert.assertTrue(t0.hashCode() == t4.hashCode());

    Assert.assertTrue(t3.hashCode() != t4.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      final TextureCubeStatic t0 =
        new TextureCubeStatic("xyz", type, 1, 128, 256);

      Assert.assertEquals(1, t0.getGLName());
      Assert.assertEquals("xyz", t0.getName());
      Assert.assertEquals(128, t0.getWidth());
      Assert.assertEquals(256, t0.getHeight());
      Assert.assertEquals(0, t0.getRangeX().getLower());
      Assert.assertEquals(127, t0.getRangeX().getUpper());
      Assert.assertEquals(0, t0.getRangeY().getLower());
      Assert.assertEquals(255, t0.getRangeY().getUpper());
      Assert.assertEquals(t0.getRangeX(), t0.getArea().getRangeX());
      Assert.assertEquals(t0.getRangeY(), t0.getArea().getRangeY());
      Assert.assertEquals(type, t0.getType());
    }
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final TextureCubeStatic t0 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final TextureCubeStatic t1 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256);
    final TextureCubeStatic t2 =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);

    Assert.assertEquals(t0.toString(), t0.toString());
    Assert.assertEquals(t0.toString(), t2.toString());
    Assert.assertEquals(t2.toString(), t0.toString());
    Assert.assertFalse(t0.toString().equals(t1.toString()));
    Assert.assertFalse(t0.toString().equals(null));
    Assert.assertFalse(t0.toString().equals(Integer.valueOf(23)));
  }
}
