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
import com.io7m.jcanephora.jogl.TextureCubeWritableData;
import com.io7m.jtensors.VectorI2D;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3D;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4D;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;

public class TextureCubeWritableDataTest
{
  /**
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor1d()
    throws ConstraintError
  {
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);

      final SpatialCursorWritable1d c = d.getCursor1d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put1d(123.0f);
        } else {
          c.put1d(1370.0f);
        }

        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 1d cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor1dFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 1) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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

  @SuppressWarnings("static-method") @Test public void testGetCursor1f()
    throws ConstraintError
  {
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);

      final SpatialCursorWritable1f c = d.getCursor1f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put1f(123.0f);
        } else {
          c.put1f(1370.0f);
        }

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
      if (type.getComponentCount() != 1) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
    final Set<TextureType> e1_types =
      TextureTypeMeta.getTexturesWithComponents(1);

    for (final TextureType type : e1_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable1i c = d.getCursor1i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put1i(0xff);
        } else {
          c.put1i(0x00);
        }

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
      if (type.getComponentCount() != 1) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor2d()
    throws ConstraintError
  {
    final Set<TextureType> e2_types =
      TextureTypeMeta.getTexturesWithComponents(2);

    for (final TextureType type : e2_types) {
      if (type == TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable2d c = d.getCursor2d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2d(new VectorI2D(0.30f, 0.40f));
        } else {
          c.put2d(new VectorI2D(0.00f, 0.00f));
        }

        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a 2d cursor to a texture that has more than one
   * component fails.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor2dFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor2f()
    throws ConstraintError
  {
    final Set<TextureType> e2_types =
      TextureTypeMeta.getTexturesWithComponents(2);

    for (final TextureType type : e2_types) {
      if (type == TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable2f c = d.getCursor2f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2f(new VectorI2F(0.30f, 0.40f));
        } else {
          c.put2f(new VectorI2F(0.00f, 0.00f));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor2fFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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

  @SuppressWarnings("static-method") @Test public void testGetCursor2i()
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

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable2i c = d.getCursor2i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2i(new VectorI2I(0x30, 0x40));
        } else {
          c.put2i(new VectorI2I(0x00, 0x00));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor2iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 2) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor3d()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable3d c = d.getCursor3d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3d(new VectorI3D(0.30f, 0.40f, 0.50f));
        } else {
          c.put3d(new VectorI3D(0.00f, 0.00f, 0.00f));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor3dFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor3f()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable3f c = d.getCursor3f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3f(new VectorI3F(0.30f, 0.40f, 0.50f));
        } else {
          c.put3f(new VectorI3F(0.00f, 0.00f, 0.00f));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor3fFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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

  @SuppressWarnings("static-method") @Test public void testGetCursor3i()
    throws ConstraintError
  {
    final Set<TextureType> e3_types =
      TextureTypeMeta.getTexturesWithComponents(3);

    for (final TextureType type : e3_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable3i c = d.getCursor3i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3i(new VectorI3I(0x30, 0x40, 0x50));
        } else {
          c.put3i(new VectorI3I(0x00, 0x00, 0x00));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor3iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 3) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor4d()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable4d c = d.getCursor4d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4d(new VectorI4D(0.30f, 0.40f, 0.50f, 0.60f));
        } else {
          c.put4d(new VectorI4D(0.00f, 0.00f, 0.00f, 0.00f));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor4dFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
   * Getting a floating point cursor works for all texture types.
   * 
   * @throws ConstraintError
   */

  @SuppressWarnings("static-method") @Test public void testGetCursor4f()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable4f c = d.getCursor4f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4f(new VectorI4F(0.30f, 0.40f, 0.50f, 0.60f));
        } else {
          c.put4f(new VectorI4F(0.00f, 0.00f, 0.00f, 0.00f));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor4fFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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

  @SuppressWarnings("static-method") @Test public void testGetCursor4i()
    throws ConstraintError
  {
    final Set<TextureType> e4_types =
      TextureTypeMeta.getTexturesWithComponents(4);

    for (final TextureType type : e4_types) {
      if (TextureTypeMeta.isFloatingPoint(type)) {
        continue;
      }

      final TextureCubeStatic t =
        new TextureCubeStatic(
          "xyz",
          type,
          1,
          64,
          TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapS.TEXTURE_WRAP_REPEAT,
          TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final TextureCubeWritableData d = new TextureCubeWritableData(t);
      final SpatialCursorWritable4i c = d.getCursor4i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4i(new VectorI4I(0x30, 0x40, 0x50, 0x60));
        } else {
          c.put4i(new VectorI4I(0x00, 0x00, 0x00, 0x00));
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

  @SuppressWarnings("static-method") @Test public
    void
    testGetCursor4iFailure()
      throws ConstraintError
  {
    for (final TextureType type : TextureType.values()) {
      if (type.getComponentCount() != 4) {
        final TextureCubeStatic t =
          new TextureCubeStatic(
            "xyz",
            type,
            1,
            64,
            TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
            TextureFilterMinification.TEXTURE_FILTER_LINEAR,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        final TextureCubeWritableData d = new TextureCubeWritableData(t);

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
    final TextureCubeStatic t =
      new TextureCubeStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_R_8_1BPP,
        1,
        64,
        TextureWrapR.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT_MIRRORED,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final TextureCubeWritableData d = new TextureCubeWritableData(t);

    Assert.assertTrue(d.getTexture() == t);
    Assert.assertTrue(d.targetArea().getRangeX().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeY().getLower() == 0);
    Assert.assertTrue(d.targetArea().getRangeX().getUpper() == 63);
    Assert.assertTrue(d.targetArea().getRangeY().getUpper() == 63);
    Assert.assertTrue(d.targetData().capacity() == (64 * 64));
  }
}
