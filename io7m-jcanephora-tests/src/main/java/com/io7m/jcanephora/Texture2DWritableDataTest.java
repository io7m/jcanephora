package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class Texture2DWritableDataTest
{
  private static final TextureType e3_types[];
  private static final TextureType e1_types[];
  private static final TextureType e2_types[];
  private static final TextureType e4_types[];

  static {
    e3_types =
      new TextureType[] {
    TextureType.TEXTURE_TYPE_RGB_565_2BPP,
    TextureType.TEXTURE_TYPE_RGB_888_3BPP };

    e1_types = new TextureType[] { TextureType.TEXTURE_TYPE_R_8_1BPP };

    e2_types = new TextureType[] { TextureType.TEXTURE_TYPE_RG_88_2BPP };

    e4_types =
      new TextureType[] {
    TextureType.TEXTURE_TYPE_RGBA_4444_2BPP,
    TextureType.TEXTURE_TYPE_RGBA_5551_2BPP,
    TextureType.TEXTURE_TYPE_RGBA_8888_4BPP };
  }

  /**
   * Updating single element textures works.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor1i()
    throws ConstraintError
  {
    for (final TextureType type : Texture2DWritableDataTest.e1_types) {
      final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
      if (TextureTypeMeta.components(type) != 1) {
        final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
    for (final TextureType type : Texture2DWritableDataTest.e2_types) {
      final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
      if (TextureTypeMeta.components(type) != 2) {
        final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
    for (final TextureType type : Texture2DWritableDataTest.e3_types) {
      final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
      if (TextureTypeMeta.components(type) != 3) {
        final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
    for (final TextureType type : Texture2DWritableDataTest.e4_types) {
      final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
      if (TextureTypeMeta.components(type) != 4) {
        final Texture2DStatic t = new Texture2DStatic("xyz", type, 1, 64, 64);
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
        128);
    final Texture2DWritableData d = new Texture2DWritableData(t);

    Assert.assertTrue(d.getTexture() == t);
    Assert.assertTrue(d.targetArea().getRangeX().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeY().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeX().getUpper() == 63);
    Assert.assertTrue(d.targetArea().getRangeY().getUpper() == 127);
    Assert.assertTrue(d.targetData().capacity() == (64 * 128));
  }
}
