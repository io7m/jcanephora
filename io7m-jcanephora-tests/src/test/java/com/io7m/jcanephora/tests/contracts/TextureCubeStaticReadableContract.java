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

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.SpatialCursorReadable1dType;
import com.io7m.jcanephora.SpatialCursorReadable1fType;
import com.io7m.jcanephora.SpatialCursorReadable1iType;
import com.io7m.jcanephora.SpatialCursorReadable2dType;
import com.io7m.jcanephora.SpatialCursorReadable2fType;
import com.io7m.jcanephora.SpatialCursorReadable2iType;
import com.io7m.jcanephora.SpatialCursorReadable3dType;
import com.io7m.jcanephora.SpatialCursorReadable3fType;
import com.io7m.jcanephora.SpatialCursorReadable3iType;
import com.io7m.jcanephora.SpatialCursorReadable4dType;
import com.io7m.jcanephora.SpatialCursorReadable4fType;
import com.io7m.jcanephora.SpatialCursorReadable4iType;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jtensors.VectorM2D;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorM3D;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4D;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorM4I;

@SuppressWarnings("null") public abstract class TextureCubeStaticReadableContract<G extends JCGLTexturesCubeStaticCommonType> implements
  TestContract
{
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

  public abstract TextureCubeStaticReadableType getTextureReadable(
    G g,
    final TextureCubeStaticType t,
    final CubeMapFaceLH face);

  public abstract G getTextures();

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable1dType c = d.getCursor1d();

      while (c.isValid()) {
        c.get1d();
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable1fType c = d.getCursor1f();

      while (c.isValid()) {
        c.get1f();
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable1iType c = d.getCursor1i();

      while (c.isValid()) {
        c.get1i();
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable2dType c = d.getCursor2d();

      final VectorM2D v = new VectorM2D();
      while (c.isValid()) {
        c.get2d(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable2fType c = d.getCursor2f();

      final VectorM2F v = new VectorM2F();
      while (c.isValid()) {
        c.get2f(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable2iType c = d.getCursor2i();

      final VectorM2I v = new VectorM2I();
      while (c.isValid()) {
        c.get2i(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable3dType c = d.getCursor3d();

      final VectorM3D v = new VectorM3D();
      while (c.isValid()) {
        c.get3d(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable3fType c = d.getCursor3f();

      final VectorM3F v = new VectorM3F();
      while (c.isValid()) {
        c.get3f(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable3iType c = d.getCursor3i();

      final VectorM3I v = new VectorM3I();
      while (c.isValid()) {
        c.get3i(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable4dType c = d.getCursor4d();

      final VectorM4D v = new VectorM4D();
      while (c.isValid()) {
        c.get4d(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable4fType c = d.getCursor4f();

      final VectorM4F v = new VectorM4F();
      while (c.isValid()) {
        c.get4f(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);
      final SpatialCursorReadable4iType c = d.getCursor4i();

      final VectorM4I v = new VectorM4I();
      while (c.isValid()) {
        c.get4i(v);
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
      final TextureCubeStaticReadableType d =
        this.getTextureReadable(g, t, CubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z);

      try {
        d.getCursor4i();
      } catch (final JCGLExceptionTypeError e) {
        continue;
      }

      Assert.fail("Did not raise constraint error for " + type);
    }
  }
}
