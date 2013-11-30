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

package com.io7m.jcanephora;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;

public class Texture2DReadableDataTest
{
  @SuppressWarnings("static-method") private
    Texture2DReadableData
    makeTexture(
      final Texture2DStatic t)
  {
    try {
      return new Texture2DReadableData(t.getType(), t.getArea());
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  @SuppressWarnings("static-method") private
    Texture2DStatic
    makeTextureActual(
      final TextureType type)
      throws ConstraintError
  {
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
    return t;
  }

  /**
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor1d()
    throws ConstraintError
  {
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable1d c = d.getCursor1d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get1d();
        } else {
          c.get1d();
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

  @Test public void testGetCursor1dFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 1) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor1d();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor1f()
    throws ConstraintError
  {
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);

      final SpatialCursorReadable1f c = d.getCursor1f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get1f();
        } else {
          c.get1f();
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

  @Test public void testGetCursor1fFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 1) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor1f();
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

  @Test public void testGetCursor1i()
    throws ConstraintError
  {
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }

      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable1i c = d.getCursor1i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get1i();
        } else {
          c.get1i();
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

  @Test public void testGetCursor1iFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 1) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

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
   * Getting a floating point cursor to a texture always works.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor2d()
    throws ConstraintError
  {
    final Set<TextureType> e2_types =
      TextureTypeMeta.getTexturesWithComponents(2);

    for (final TextureType type : e2_types) {
      if (type == TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable2d c = d.getCursor2d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get2d(new VectorM2D(0.30000000, 0.40000000));
        } else {
          c.get2d(new VectorM2D(0.00000000, 0.00000000));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 2f cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor2dFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor2d();
        } catch (final ConstraintError e) {
          continue;
        }

        Assert.fail("Did not raise constraint error");
      }
    }
  }

  /**
   * Getting a floating point cursor to a texture always works.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor2f()
    throws ConstraintError
  {
    final Set<TextureType> e2_types =
      TextureTypeMeta.getTexturesWithComponents(2);

    for (final TextureType type : e2_types) {
      if (type == TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable2f c = d.getCursor2f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get2f(new VectorM2F(0.30000000f, 0.40000000f));
        } else {
          c.get2f(new VectorM2F(0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 2f cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor2fFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor2f();
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

  @Test public void testGetCursor2i()
    throws ConstraintError
  {
    final Set<TextureType> e2_types =
      TextureTypeMeta.getTexturesWithComponents(2);

    for (final TextureType type : e2_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }
      if (type == TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable2i c = d.getCursor2i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get2i(new VectorM2I(0x30000000, 0x40000000));
        } else {
          c.get2i(new VectorM2I(0x00000000, 0x00000000));
        }
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

  @Test public void testGetCursor2iFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

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

  @Test public void testGetCursor3d()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable3d c = d.getCursor3d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get3d(new VectorM3D(0.30000000f, 0.40000000f, 0.50000000f));
        } else {
          c.get3d(new VectorM3D(0.00000000f, 0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 3d cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor3dFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor3d();
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

  @Test public void testGetCursor3f()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable3f c = d.getCursor3f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get3f(new VectorM3F(0.30000000f, 0.40000000f, 0.50000000f));
        } else {
          c.get3f(new VectorM3F(0.00000000f, 0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 3f cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor3fFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor3f();
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

  @Test public void testGetCursor3i()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable3i c = d.getCursor3i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get3i(new VectorM3I(0x30000000, 0x40000000, 0x50000000));
        } else {
          c.get3i(new VectorM3I(0x00000000, 0x00000000, 0x00000000));
        }
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

  @Test public void testGetCursor3iFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

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

  @Test public void testGetCursor4d()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable4d c = d.getCursor4d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get4d(new VectorM4D(
            0.30000000f,
            0.40000000f,
            0.50000000f,
            0.60000000f));
        } else {
          c.get4d(new VectorM4D(
            0.00000000f,
            0.00000000f,
            0.00000000f,
            0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 4d cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor4dFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor4d();
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

  @Test public void testGetCursor4f()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable4f c = d.getCursor4f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get4f(new VectorM4F(
            0.30000000f,
            0.40000000f,
            0.50000000f,
            0.60000000f));
        } else {
          c.get4f(new VectorM4F(
            0.00000000f,
            0.00000000f,
            0.00000000f,
            0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 4f cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @Test public void testGetCursor4fFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

        try {
          d.getCursor4f();
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

  @Test public void testGetCursor4i()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }

      final Texture2DStatic t = this.makeTextureActual(type);
      final Texture2DReadableData d = this.makeTexture(t);
      final SpatialCursorReadable4i c = d.getCursor4i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.get4i(new VectorM4I(
            0x30000000,
            0x40000000,
            0x50000000,
            0x60000000));
        } else {
          c.get4i(new VectorM4I(
            0x00000000,
            0x00000000,
            0x00000000,
            0x00000000));
        }
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

  @Test public void testGetCursor4iFailure()
    throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final Texture2DStatic t = this.makeTextureActual(type);
        final Texture2DReadableData d = this.makeTexture(t);

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

  @Test public void testIdentities()
    throws ConstraintError
  {
    final Texture2DStatic t =
      this.makeTextureActual(TextureType.TEXTURE_TYPE_DEPTH_16_2BPP);
    final Texture2DReadableData d = this.makeTexture(t);
    Assert.assertTrue(d.getArea().getRangeX().getLower() == 0);
    Assert.assertTrue(d.getArea().getRangeY().getLower() == 0);
    Assert.assertTrue(d.getArea().getRangeX().getUpper() == 63);
    Assert.assertTrue(d.getArea().getRangeY().getUpper() == 63);
    Assert.assertTrue(d.getData().capacity() == (64 * (2 * 64)));
  }
}
