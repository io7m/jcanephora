/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.SpatialCursorWritable1dType;
import com.io7m.jcanephora.SpatialCursorWritable1fType;
import com.io7m.jcanephora.SpatialCursorWritable1iType;
import com.io7m.jcanephora.SpatialCursorWritable2dType;
import com.io7m.jcanephora.SpatialCursorWritable2fType;
import com.io7m.jcanephora.SpatialCursorWritable2iType;
import com.io7m.jcanephora.SpatialCursorWritable3dType;
import com.io7m.jcanephora.SpatialCursorWritable3fType;
import com.io7m.jcanephora.SpatialCursorWritable3iType;
import com.io7m.jcanephora.SpatialCursorWritable4dType;
import com.io7m.jcanephora.SpatialCursorWritable4fType;
import com.io7m.jcanephora.SpatialCursorWritable4iType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUpdateType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jtensors.VectorI2D;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3D;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4D;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;

@SuppressWarnings("null") public abstract class TextureCubeStaticUpdateContract<G extends JCGLTexturesCubeStaticCommonType> implements
  TestContract
{
  public abstract G getTextures();

  public abstract TextureFormat getSafeDefaultFormat();

  public abstract Set<TextureFormat> getSupportedTexturesWithComponents(
    int count);

  public abstract
    Set<TextureFormat>
    getSupportedTexturesWithNotEqualComponents(
      int count);

  public abstract TextureCubeStaticType getTexture(
    G g,
    TextureFormat t);

  public abstract TextureCubeStaticUpdateType getTextureUpdate(
    final TextureCubeStaticType t);

  /**
   * Getting a floating point cursor works for all texture types.
   */

  @Test public final void testGetCursor1d()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e1_types =
      this.getSupportedTexturesWithComponents(1);

    for (final TextureFormat type : e1_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable1dType c = d.getCursor1d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put1d(123.0f);
        } else {
          c.put1d(1370.0f);
        }
        c.next();
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor1dFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(1)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor1d();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Getting a floating point cursor works for all texture types.
   */

  @Test public final void testGetCursor1f()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e1_types =
      this.getSupportedTexturesWithComponents(1);

    for (final TextureFormat type : e1_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable1fType c = d.getCursor1f();

      int count = 0;
      while (c.isValid()) {
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
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor1fFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(1)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor1f();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating single element textures works.
   */

  @Test public final void testGetCursor1i()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e1_types =
      this.getSupportedTexturesWithComponents(1);

    for (final TextureFormat type : e1_types) {
      if (TextureFormatMeta.isFloatingPoint(type)) {
        continue;
      }

      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable1iType c = d.getCursor1i();

      int count = 0;
      while (c.isValid()) {
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
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor1iFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(1)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor1i();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Getting a floating point cursor to a texture always works.
   */

  @Test public final void testGetCursor2d()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e2_types =
      this.getSupportedTexturesWithComponents(2);

    for (final TextureFormat type : e2_types) {
      if (type == TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable2dType c = d.getCursor2d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2d(new VectorI2D(0.30000000, 0.40000000));
        } else {
          c.put2d(new VectorI2D(0.00000000, 0.00000000));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor2dFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(2)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor2d();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Getting a floating point cursor to a texture always works.
   */

  @Test public final void testGetCursor2f()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e2_types =
      this.getSupportedTexturesWithComponents(2);

    for (final TextureFormat type : e2_types) {
      if (type == TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable2fType c = d.getCursor2f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2f(new VectorI2F(0.30000000f, 0.40000000f));
        } else {
          c.put2f(new VectorI2F(0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor2fFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(2)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor2f();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating two element textures works.
   */

  @Test public final void testGetCursor2i()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e2_types =
      this.getSupportedTexturesWithComponents(2);

    for (final TextureFormat type : e2_types) {
      if (TextureFormatMeta.isFloatingPoint(type)) {
        continue;
      }
      if (type == TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP) {
        continue;
      }

      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable2iType c = d.getCursor2i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put2i(new VectorI2I(0x30000000, 0x40000000));
        } else {
          c.put2i(new VectorI2I(0x00000000, 0x00000000));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor2iFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(2)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor2i();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating three element textures works.
   */

  @Test public final void testGetCursor3d()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e3_types =
      this.getSupportedTexturesWithComponents(3);

    for (final TextureFormat type : e3_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable3dType c = d.getCursor3d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3d(new VectorI3D(0.30000000f, 0.40000000f, 0.50000000f));
        } else {
          c.put3d(new VectorI3D(0.00000000f, 0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor3dFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(3)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor3d();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating three element textures works.
   */

  @Test public final void testGetCursor3f()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e3_types =
      this.getSupportedTexturesWithComponents(3);

    for (final TextureFormat type : e3_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable3fType c = d.getCursor3f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3f(new VectorI3F(0.30000000f, 0.40000000f, 0.50000000f));
        } else {
          c.put3f(new VectorI3F(0.00000000f, 0.00000000f, 0.00000000f));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor3fFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(3)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor3f();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating three element textures works.
   */

  @Test public final void testGetCursor3i()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e3_types =
      this.getSupportedTexturesWithComponents(3);

    for (final TextureFormat type : e3_types) {
      if (TextureFormatMeta.isFloatingPoint(type)) {
        continue;
      }
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable3iType c = d.getCursor3i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put3i(new VectorI3I(0x30000000, 0x40000000, 0x50000000));
        } else {
          c.put3i(new VectorI3I(0x00000000, 0x00000000, 0x00000000));
        }
        ++count;
      }
    }
  }

  /**
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor3iFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(3)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor3i();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating four element textures works.
   */

  @Test public final void testGetCursor4d()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e4_types =
      this.getSupportedTexturesWithComponents(4);

    for (final TextureFormat type : e4_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable4dType c = d.getCursor4d();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4d(new VectorI4D(
            0.30000000f,
            0.40000000f,
            0.50000000f,
            0.60000000f));
        } else {
          c.put4d(new VectorI4D(
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
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor4dFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(4)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor4d();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating four element textures works.
   */

  @Test public final void testGetCursor4f()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e4_types =
      this.getSupportedTexturesWithComponents(4);

    for (final TextureFormat type : e4_types) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable4fType c = d.getCursor4f();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4f(new VectorI4F(
            0.30000000f,
            0.40000000f,
            0.50000000f,
            0.60000000f));
        } else {
          c.put4f(new VectorI4F(
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
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor4fFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(4)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor4f();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Updating four element textures works.
   */

  @Test public final void testGetCursor4i()
    throws JCGLExceptionTypeError
  {
    final G g = this.getTextures();

    final Set<TextureFormat> e4_types =
      this.getSupportedTexturesWithComponents(4);

    for (final TextureFormat type : e4_types) {
      if (TextureFormatMeta.isFloatingPoint(type)) {
        continue;
      }

      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);
      final SpatialCursorWritable4iType c = d.getCursor4i();

      int count = 0;
      while (c.isValid()) {
        if ((count & 1) == 1) {
          c.put4i(new VectorI4I(
            0x30000000,
            0x40000000,
            0x50000000,
            0x60000000));
        } else {
          c.put4i(new VectorI4I(
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
   * Attempting to obtain a cursor to a texture that has the wrong number of
   * component fails.
   */

  @Test public final void testGetCursor4iFailure()
  {
    final G g = this.getTextures();

    for (final TextureFormat type : this
      .getSupportedTexturesWithNotEqualComponents(4)) {
      final TextureCubeStaticType t = this.getTexture(g, type);
      final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

      try {
        d.getCursor4i();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }

  /**
   * Various identities.
   */

  @Test public final void testIdentities()
  {
    final G g = this.getTextures();

    final TextureFormat f = this.getSafeDefaultFormat();
    final TextureCubeStaticType t = this.getTexture(g, f);
    final TextureCubeStaticUpdateType d = this.getTextureUpdate(t);

    final AreaInclusive a = d.getTargetArea();

    Assert.assertEquals(t, d.getTexture());
    Assert.assertEquals(0, a.getRangeX().getLower());
    Assert.assertEquals(0, a.getRangeY().getLower());
    Assert.assertEquals(127, a.getRangeX().getUpper());
    Assert.assertEquals(127, a.getRangeY().getUpper());

    final long row = a.getRangeX().getInterval() * f.getBytesPerPixel();
    final long size = row * a.getRangeY().getInterval();

    Assert.assertEquals(size, d.getTargetData().capacity());
  }
}
