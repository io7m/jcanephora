package com.io7m.jcanephora;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class Texture2DWritableDataTest
{
  /**
   * Updating single element floating point textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor1f()
    throws ConstraintError
  {
    final Set<TextureType> e1_types = TextureType.getWithComponents(1);

    for (final TextureType type : e1_types) {
      if (type.isFloatingPoint() == false) {
        continue;
      }

      final Texture2DStatic t =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          64,
          64,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final Texture2DWritableData d = new Texture2DWritableData(t);

      final SpatialCursorWritable1f c = d.getCursor1f();

      int count = 0;
      while (c.canWrite()) {
        if ((count & 1) == 1) {
          c.put1f(123.0f);
        } else {
          c.put1f(1370.0f);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 1f cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor1fFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponents() != 1) {
        if (type.isFloatingPoint() == false) {
          continue;
        }

        final Texture2DStatic t =
          new Texture2DStatic(
            "xyz",
            type,
            1,
            64,
            64,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final Texture2DWritableData d = new Texture2DWritableData(t);

        try {
          d.getCursor1i();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Updating single element textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor1i()
    throws ConstraintError
  {
    final Set<TextureType> e1_types = TextureType.getWithComponents(1);

    for (final TextureType type : e1_types) {
      if (type.isFloatingPoint()) {
        continue;
      }

      final Texture2DStatic t =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          64,
          64,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final Texture2DWritableData d = new Texture2DWritableData(t);
      final SpatialCursorWritable1i c = d.getCursor1i();

      int count = 0;
      while (c.canWrite()) {
        if ((count & 1) == 1) {
          c.put1i(0xff);
        } else {
          c.put1i(0x00);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 1i cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor1iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponents() != 1) {
        final Texture2DStatic t =
          new Texture2DStatic(
            "xyz",
            type,
            1,
            64,
            64,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final Texture2DWritableData d = new Texture2DWritableData(t);

        try {
          d.getCursor1i();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Updating two element textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor2i()
    throws ConstraintError
  {
    final Set<TextureType> e2_types = TextureType.getWithComponents(2);

    for (final TextureType type : e2_types) {
      final Texture2DStatic t =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          64,
          64,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final Texture2DWritableData d = new Texture2DWritableData(t);
      final SpatialCursorWritable2i c = d.getCursor2i();

      int count = 0;
      while (c.canWrite()) {
        if ((count & 1) == 1) {
          c.put2i(0x30, 0x40);
        } else {
          c.put2i(0x00, 0x00);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 2i cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor2iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponents() != 2) {
        final Texture2DStatic t =
          new Texture2DStatic(
            "xyz",
            type,
            1,
            64,
            64,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final Texture2DWritableData d = new Texture2DWritableData(t);

        try {
          d.getCursor2i();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Updating three element textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor3i()
    throws ConstraintError
  {
    final Set<TextureType> e3_types = TextureType.getWithComponents(3);

    for (final TextureType type : e3_types) {
      final Texture2DStatic t =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          64,
          64,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final Texture2DWritableData d = new Texture2DWritableData(t);
      final SpatialCursorWritable3i c = d.getCursor3i();

      int count = 0;
      while (c.canWrite()) {
        if ((count & 1) == 1) {
          c.put3i(0x30, 0x40, 0x50);
        } else {
          c.put3i(0x00, 0x00, 0x00);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 3i cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor3iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponents() != 3) {
        final Texture2DStatic t =
          new Texture2DStatic(
            "xyz",
            type,
            1,
            64,
            64,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final Texture2DWritableData d = new Texture2DWritableData(t);

        try {
          d.getCursor3i();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Updating four element textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor4i()
    throws ConstraintError
  {
    final Set<TextureType> e4_types = TextureType.getWithComponents(4);

    for (final TextureType type : e4_types) {
      final Texture2DStatic t =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          64,
          64,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_NEAREST,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final Texture2DWritableData d = new Texture2DWritableData(t);
      final SpatialCursorWritable4i c = d.getCursor4i();

      int count = 0;
      while (c.canWrite()) {
        if ((count & 1) == 1) {
          c.put4i(0x30, 0x40, 0x50, 0x60);
        } else {
          c.put4i(0x00, 0x00, 0x00, 0x00);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 4i cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor4iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponents() != 4) {
        final Texture2DStatic t =
          new Texture2DStatic(
            "xyz",
            type,
            1,
            64,
            64,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final Texture2DWritableData d = new Texture2DWritableData(t);

        try {
          d.getCursor4i();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Various identities.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final Texture2DStatic t =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_R_8_1BPP,
        1,
        64,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DWritableData d = new Texture2DWritableData(t);

    Assert.assertTrue(d.getTexture() == t);
    Assert.assertTrue(d.targetArea().getRangeX().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeY().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeX().getUpper() == 63);
    Assert.assertTrue(d.targetArea().getRangeY().getUpper() == 127);
    Assert.assertTrue(d.targetData().capacity() == (64 * 128));
  }
}
