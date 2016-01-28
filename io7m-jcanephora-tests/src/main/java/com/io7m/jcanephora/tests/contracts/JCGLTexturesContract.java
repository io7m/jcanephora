/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jareas.core.AreaInclusiveUnsignedL;
import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureUpdates;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Texture contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTexturesContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLTexturesType getTextures(String name);

  @Test public final void testTextureSize()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final int s = t.textureGetMaximumSize();
    Assert.assertTrue(s >= 1024);
  }

  @Test public final void testTextureUnits()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    for (int index = 0; index < 16; ++index) {
      final JCGLTextureUnitType u = us.get(index);
      Assert.assertEquals((long) index, (long) u.unitGetIndex());
    }
  }

  @Test public final void testTextureEmptyBindings()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    for (int index = 0; index < 16; ++index) {
      final JCGLTextureUnitType u = us.get(index);
      Assert.assertFalse(t.textureUnitIsBound(u));
    }
  }

  @Test public final void testTexture2DBoundAnywhere()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureUnitType u1 = us.get(1);

    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u0,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));

    t.textureUnitUnbind(u0);
    Assert.assertFalse(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.texture2DIsBound(u1, ta));
    Assert.assertFalse(t.textureUnitIsBound(u1));

    t.texture2DBind(u1, ta);
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.texture2DIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.texture2DBind(u0, ta);
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));
    Assert.assertTrue(t.texture2DIsBound(u0, ta));
    Assert.assertTrue(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.texture2DIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.textureUnitUnbind(u0);
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.texture2DIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.textureUnitUnbind(u1);
    Assert.assertFalse(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.texture2DIsBound(u1, ta));
    Assert.assertFalse(t.textureUnitIsBound(u1));
  }

  @Test public final void testTexture2DBinding()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        us.get(0),
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType u = us.get(index);

      t.textureUnitUnbind(u);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        Assert.assertFalse(t.textureUnitIsBound(ku));
        Assert.assertFalse(t.texture2DIsBound(ku, ta));
      }

      t.texture2DBind(u, ta);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        if (k == index) {
          Assert.assertTrue(t.textureUnitIsBound(ku));
          Assert.assertTrue(t.texture2DIsBound(ku, ta));
        } else {
          Assert.assertFalse(t.textureUnitIsBound(ku));
          Assert.assertFalse(t.texture2DIsBound(ku, ta));
        }
      }

      t.texture2DBind(u, ta);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        if (k == index) {
          Assert.assertTrue(t.textureUnitIsBound(ku));
          Assert.assertTrue(t.texture2DIsBound(ku, ta));
        } else {
          Assert.assertFalse(t.textureUnitIsBound(ku));
          Assert.assertFalse(t.texture2DIsBound(ku, ta));
        }
      }

      t.textureUnitUnbind(u);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        Assert.assertFalse(t.textureUnitIsBound(ku));
        Assert.assertFalse(t.texture2DIsBound(ku, ta));
      }
    }
  }

  @Test public final void testTexture2DDeleteUnbinds()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    Assert.assertTrue(t.textureUnitIsBound(u));
    Assert.assertTrue(t.texture2DIsBound(u, ta));
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));

    t.texture2DDelete(ta);
    Assert.assertTrue(ta.isDeleted());

    Assert.assertFalse(t.textureUnitIsBound(u));
  }

  @Test public final void testTexture2DAllocate()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      final JCGLTexture2DType ta =
        t.texture2DAllocate(
          u,
          128L,
          256L,
          v,
          JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
          JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
          JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

      Assert.assertTrue(t.textureUnitIsBound(u));
      Assert.assertTrue(t.texture2DIsBound(u, ta));

      Assert.assertEquals(128L, ta.textureGetWidth());
      Assert.assertEquals(256L, ta.textureGetHeight());
      Assert.assertEquals(v, ta.textureGetFormat());
      Assert.assertEquals(
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        ta.textureGetWrapS());
      Assert.assertEquals(
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        ta.textureGetWrapT());
      Assert.assertEquals(
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        ta.textureGetMinificationFilter());
      Assert.assertEquals(
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        ta.textureGetMagnificationFilter());

      Assert.assertFalse(ta.isDeleted());
      t.texture2DDelete(ta);
      Assert.assertTrue(ta.isDeleted());
    }
  }

  @Test public final void testTexture2DGetImageIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      final JCGLTexture2DType ta =
        t.texture2DAllocate(
          u,
          128L,
          256L,
          v,
          JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
          JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
          JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

      Assert.assertTrue(t.textureUnitIsBound(u));
      Assert.assertTrue(t.texture2DIsBound(u, ta));

      final ByteBuffer i = t.texture2DGetImage(u, ta);
      Assert.assertEquals(ByteOrder.nativeOrder(), i.order());
      Assert.assertEquals(
        128L * 256L * (long) v.getBytesPerPixel(),
        (long) i.capacity());

      Assert.assertFalse(ta.isDeleted());
      t.texture2DDelete(ta);
      Assert.assertTrue(ta.isDeleted());
    }
  }

  @Test public final void testTexture2DUpdateAreaNonInclusive()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    this.expected.expect(RangeCheckException.class);
    JCGLTextureUpdates.newUpdateReplacingArea2D(
      ta,
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(0L, 512L),
        new UnsignedRangeInclusiveL(0L, 512L)));
  }

  @Test public final void testTexture2DUpdateAllIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLTexture2DUpdateType up =
      JCGLTextureUpdates.newUpdateReplacingAll2D(ta);

    Assert.assertEquals(ta, up.getTexture());

    final AreaInclusiveUnsignedL expected_area =
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(0L, 127L),
        new UnsignedRangeInclusiveL(0L, 255L));
    Assert.assertEquals(expected_area, up.getArea());

    final ByteBuffer data = up.getData();
    final long expected_size = 128L * 256L * 4L;
    Assert.assertEquals(expected_size, (long) data.capacity());
    final UnsignedRangeInclusiveL expected_range =
      new UnsignedRangeInclusiveL(0L, expected_size - 1L);
    Assert.assertEquals(expected_range, up.getDataUpdateRange());
  }

  @Test public final void testTexture2DUpdateAreaIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final AreaInclusiveUnsignedL expected_area =
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

    final JCGLTexture2DUpdateType up =
      JCGLTextureUpdates.newUpdateReplacingArea2D(ta, expected_area);

    Assert.assertEquals(ta, up.getTexture());
    Assert.assertEquals(expected_area, up.getArea());

    final ByteBuffer data = up.getData();
    final long expected_size = 128L * 128L;
    Assert.assertEquals(expected_size, (long) data.capacity());
    final UnsignedRangeInclusiveL expected_range =
      new UnsignedRangeInclusiveL(0L, expected_size - 1L);
    Assert.assertEquals(expected_range, up.getDataUpdateRange());
  }

  @Test public final void testTexture2DUpdateAllGet1BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0xff);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
      }
    }
  }

  @Test public final void testTexture2DUpdateAllGet4BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0xff);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
      }
    }
  }

  @Test public final void testTexture2DUpdateAllGet3BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0xff);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
      }
    }
  }

  @Test public final void testTexture2DUpdateAllGet2BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0xff);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
      }
    }
  }

  @Test public final void testTexture2DUpdateAreaGet1BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingArea2D(ta, area);
      final ByteBuffer data = up.getData();
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0xff);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L, (long) data.capacity());

      for (int y = 0; y < 512; ++y) {
        for (int x = 0; x < 512; ++x) {
          final int index = (y * 512) + x;
          final long val = Byte.toUnsignedLong(data.get(index));
          if (x >= 128 && x < 256 && y >= 128 && y < 256) {
            Assert.assertEquals(0xffL, val);
          } else {
            Assert.assertEquals(0L, val);
          }
        }
      }
    }
  }

  @Test public final void testTexture2DUpdateAreaGet4BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingArea2D(ta, area);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(128L * 128L * 4L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); index += 4) {
        data.put(index + 0, (byte) 0x01);
        data.put(index + 1, (byte) 0x02);
        data.put(index + 2, (byte) 0x03);
        data.put(index + 3, (byte) 0x04);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
      final int row_bytes = 512 * 4;

      for (int y = 0; y < 512; ++y) {
        for (int x = 0; x < 512; ++x) {
          final int base = (y * row_bytes) + (x * 4);
          final int index0 = base + 0;
          final int index1 = base + 1;
          final int index2 = base + 2;
          final int index3 = base + 3;

          final long val0 = Byte.toUnsignedLong(data.get(index0));
          final long val1 = Byte.toUnsignedLong(data.get(index1));
          final long val2 = Byte.toUnsignedLong(data.get(index2));
          final long val3 = Byte.toUnsignedLong(data.get(index3));

          if (x >= 128 && x < 256 && y >= 128 && y < 256) {
            Assert.assertEquals(0x01L, val0);
            Assert.assertEquals(0x02L, val1);
            Assert.assertEquals(0x03L, val2);
            Assert.assertEquals(0x04L, val3);
          } else {
            Assert.assertEquals(0x0L, val0);
            Assert.assertEquals(0x0L, val1);
            Assert.assertEquals(0x0L, val2);
            Assert.assertEquals(0x0L, val3);
          }
        }
      }
    }
  }

  @Test public final void testTexture2DUpdateAreaGet3BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingArea2D(ta, area);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(128L * 128L * 3L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); index += 3) {
        data.put(index + 0, (byte) 0x01);
        data.put(index + 1, (byte) 0x02);
        data.put(index + 2, (byte) 0x03);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
      final int row_bytes = 512 * 3;

      for (int y = 0; y < 512; ++y) {
        for (int x = 0; x < 512; ++x) {
          final int base = (y * row_bytes) + (x * 3);
          final int index0 = base + 0;
          final int index1 = base + 1;
          final int index2 = base + 2;

          final long val0 = Byte.toUnsignedLong(data.get(index0));
          final long val1 = Byte.toUnsignedLong(data.get(index1));
          final long val2 = Byte.toUnsignedLong(data.get(index2));

          if (x >= 128 && x < 256 && y >= 128 && y < 256) {
            Assert.assertEquals(0x01L, val0);
            Assert.assertEquals(0x02L, val1);
            Assert.assertEquals(0x03L, val2);
          } else {
            Assert.assertEquals(0x0L, val0);
            Assert.assertEquals(0x0L, val1);
            Assert.assertEquals(0x0L, val2);
          }
        }
      }
    }
  }

  @Test public final void testTexture2DUpdateAreaGet2BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u,
        512L,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    {
      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(ta);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); ++index) {
        data.put(index, (byte) 0);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingArea2D(ta, area);
      final ByteBuffer data = up.getData();
      Assert.assertEquals(128L * 128L * 2L, (long) data.capacity());
      for (int index = 0; index < data.capacity(); index += 2) {
        data.put(index + 0, (byte) 0x01);
        data.put(index + 1, (byte) 0x02);
      }
      t.texture2DUpdate(u, up);
    }

    {
      final ByteBuffer data = t.texture2DGetImage(u, ta);
      Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
      final int row_bytes = 512 * 2;

      for (int y = 0; y < 512; ++y) {
        for (int x = 0; x < 512; ++x) {
          final int base = (y * row_bytes) + (x * 2);
          final int index0 = base + 0;
          final int index1 = base + 1;

          final long val0 = Byte.toUnsignedLong(data.get(index0));
          final long val1 = Byte.toUnsignedLong(data.get(index1));

          if (x >= 128 && x < 256 && y >= 128 && y < 256) {
            Assert.assertEquals(0x01L, val0);
            Assert.assertEquals(0x02L, val1);
          } else {
            Assert.assertEquals(0x0L, val0);
            Assert.assertEquals(0x0L, val1);
          }
        }
      }
    }
  }

  @Test public final void testTextureCubeBoundAnywhere()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureUnitType u1 = us.get(1);

    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u0,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(ta));

    t.textureUnitUnbind(u0);
    Assert.assertFalse(t.textureCubeIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.textureCubeIsBound(u1, ta));
    Assert.assertFalse(t.textureUnitIsBound(u1));

    t.textureCubeBind(u1, ta);
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.textureCubeIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.textureCubeBind(u0, ta);
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(ta));
    Assert.assertTrue(t.textureCubeIsBound(u0, ta));
    Assert.assertTrue(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.textureCubeIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.textureUnitUnbind(u0);
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.textureCubeIsBound(u1, ta));
    Assert.assertTrue(t.textureUnitIsBound(u1));

    t.textureUnitUnbind(u1);
    Assert.assertFalse(t.textureCubeIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, ta));
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.textureCubeIsBound(u1, ta));
    Assert.assertFalse(t.textureUnitIsBound(u1));
  }

  @Test public final void testTextureCubeBinding()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        us.get(0),
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType u = us.get(index);

      t.textureUnitUnbind(u);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        Assert.assertFalse(t.textureUnitIsBound(ku));
        Assert.assertFalse(t.textureCubeIsBound(ku, ta));
      }

      t.textureCubeBind(u, ta);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        if (k == index) {
          Assert.assertTrue(t.textureUnitIsBound(ku));
          Assert.assertTrue(t.textureCubeIsBound(ku, ta));
        } else {
          Assert.assertFalse(t.textureUnitIsBound(ku));
          Assert.assertFalse(t.textureCubeIsBound(ku, ta));
        }
      }

      t.textureCubeBind(u, ta);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        if (k == index) {
          Assert.assertTrue(t.textureUnitIsBound(ku));
          Assert.assertTrue(t.textureCubeIsBound(ku, ta));
        } else {
          Assert.assertFalse(t.textureUnitIsBound(ku));
          Assert.assertFalse(t.textureCubeIsBound(ku, ta));
        }
      }

      t.textureUnitUnbind(u);

      for (int k = 0; k < us.size(); ++k) {
        final JCGLTextureUnitType ku = us.get(k);
        Assert.assertFalse(t.textureUnitIsBound(ku));
        Assert.assertFalse(t.textureCubeIsBound(ku, ta));
      }
    }
  }

  @Test public final void testTextureCubeDeleteUnbinds()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();

    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    Assert.assertTrue(t.textureUnitIsBound(u));
    Assert.assertTrue(t.textureCubeIsBound(u, ta));
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(ta));

    t.textureCubeDelete(ta);
    Assert.assertTrue(ta.isDeleted());

    Assert.assertFalse(t.textureUnitIsBound(u));
  }

  @Test public final void testTextureCubeAllocate()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      final JCGLTextureCubeType ta =
        t.textureCubeAllocate(
          u,
          128L,
          v,
          JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
          JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
          JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

      Assert.assertTrue(t.textureUnitIsBound(u));
      Assert.assertTrue(t.textureCubeIsBound(u, ta));

      Assert.assertEquals(128L, ta.textureGetWidth());
      Assert.assertEquals(128L, ta.textureGetHeight());
      Assert.assertEquals(v, ta.textureGetFormat());
      Assert.assertEquals(
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        ta.textureGetWrapS());
      Assert.assertEquals(
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        ta.textureGetWrapT());
      Assert.assertEquals(
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        ta.textureGetMinificationFilter());
      Assert.assertEquals(
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR,
        ta.textureGetMagnificationFilter());

      Assert.assertFalse(ta.isDeleted());
      t.textureCubeDelete(ta);
      Assert.assertTrue(ta.isDeleted());
    }
  }

  @Test public final void testTextureCubeGetImageLHIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);

    for (final JCGLTextureFormat v : JCGLTextureFormat.values()) {
      final JCGLTextureCubeType ta =
        t.textureCubeAllocate(
          u,
          128L,
          v,
          JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
          JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
          JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
          JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

      Assert.assertTrue(t.textureUnitIsBound(u));
      Assert.assertTrue(t.textureCubeIsBound(u, ta));

      for (final JCGLCubeMapFaceLH f : JCGLCubeMapFaceLH.values()) {
        final ByteBuffer i = t.textureCubeGetImageLH(u, f, ta);
        Assert.assertEquals(ByteOrder.nativeOrder(), i.order());
        Assert.assertEquals(
          128L * 128L * (long) v.getBytesPerPixel(),
          (long) i.capacity());
      }

      Assert.assertFalse(ta.isDeleted());
      t.textureCubeDelete(ta);
      Assert.assertTrue(ta.isDeleted());
    }
  }

  @Test public final void testTextureCubeUpdateAreaNonInclusive()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    this.expected.expect(RangeCheckException.class);
    JCGLTextureUpdates.newUpdateReplacingAreaCube(
      ta,
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(0L, 512L),
        new UnsignedRangeInclusiveL(0L, 512L)));
  }

  @Test public final void testTextureCubeUpdateAllIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLTextureCubeUpdateType up =
      JCGLTextureUpdates.newUpdateReplacingAllCube(ta);

    Assert.assertEquals(ta, up.getTexture());

    final AreaInclusiveUnsignedL expected_area =
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(0L, 127L),
        new UnsignedRangeInclusiveL(0L, 127L));
    Assert.assertEquals(expected_area, up.getArea());

    final ByteBuffer data = up.getData();
    final long expected_size = 128L * 128L * 4L;
    Assert.assertEquals(expected_size, (long) data.capacity());
    final UnsignedRangeInclusiveL expected_range =
      new UnsignedRangeInclusiveL(0L, expected_size - 1L);
    Assert.assertEquals(expected_range, up.getDataUpdateRange());
  }

  @Test public final void testTextureCubeUpdateAreaIdentities()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final AreaInclusiveUnsignedL expected_area =
      AreaInclusiveUnsignedL.of(
        new UnsignedRangeInclusiveL(128L, 255L),
        new UnsignedRangeInclusiveL(128L, 255L));

    final JCGLTextureCubeUpdateType up =
      JCGLTextureUpdates.newUpdateReplacingAreaCube(ta, expected_area);

    Assert.assertEquals(ta, up.getTexture());
    Assert.assertEquals(expected_area, up.getArea());

    final ByteBuffer data = up.getData();
    final long expected_size = 128L * 128L;
    Assert.assertEquals(expected_size, (long) data.capacity());
    final UnsignedRangeInclusiveL expected_range =
      new UnsignedRangeInclusiveL(0L, expected_size - 1L);
    Assert.assertEquals(expected_range, up.getDataUpdateRange());
  }

  @Test public final void testTextureCubeUpdateAllGet1BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0xff);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAllGet4BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0xff);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAllGet3BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0xff);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAllGet2BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0xff);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          Assert.assertEquals(0xffL, Byte.toUnsignedLong(data.get(index)));
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAreaGet1BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
          new UnsignedRangeInclusiveL(128L, 255L),
          new UnsignedRangeInclusiveL(128L, 255L));

        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAreaCube(ta, area);
        final ByteBuffer data = up.getData();
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0xff);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L, (long) data.capacity());

        for (int y = 0; y < 512; ++y) {
          for (int x = 0; x < 512; ++x) {
            final int index = (y * 512) + x;
            final long val = Byte.toUnsignedLong(data.get(index));
            if (x >= 128 && x < 256 && y >= 128 && y < 256) {
              Assert.assertEquals(0xffL, val);
            } else {
              Assert.assertEquals(0L, val);
            }
          }
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAreaGet4BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
          new UnsignedRangeInclusiveL(128L, 255L),
          new UnsignedRangeInclusiveL(128L, 255L));

        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAreaCube(ta, area);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(128L * 128L * 4L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); index += 4) {
          data.put(index + 0, (byte) 0x01);
          data.put(index + 1, (byte) 0x02);
          data.put(index + 2, (byte) 0x03);
          data.put(index + 3, (byte) 0x04);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 4L, (long) data.capacity());
        final int row_bytes = 512 * 4;

        for (int y = 0; y < 512; ++y) {
          for (int x = 0; x < 512; ++x) {
            final int base = (y * row_bytes) + (x * 4);
            final int index0 = base + 0;
            final int index1 = base + 1;
            final int index2 = base + 2;
            final int index3 = base + 3;

            final long val0 = Byte.toUnsignedLong(data.get(index0));
            final long val1 = Byte.toUnsignedLong(data.get(index1));
            final long val2 = Byte.toUnsignedLong(data.get(index2));
            final long val3 = Byte.toUnsignedLong(data.get(index3));

            if (x >= 128 && x < 256 && y >= 128 && y < 256) {
              Assert.assertEquals(0x01L, val0);
              Assert.assertEquals(0x02L, val1);
              Assert.assertEquals(0x03L, val2);
              Assert.assertEquals(0x04L, val3);
            } else {
              Assert.assertEquals(0x0L, val0);
              Assert.assertEquals(0x0L, val1);
              Assert.assertEquals(0x0L, val2);
              Assert.assertEquals(0x0L, val3);
            }
          }
        }
      }
    }
  }

  @Test public final void testTextureCubeUpdateAreaGet3BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
          new UnsignedRangeInclusiveL(128L, 255L),
          new UnsignedRangeInclusiveL(128L, 255L));

        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAreaCube(ta, area);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(128L * 128L * 3L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); index += 3) {
          data.put(index + 0, (byte) 0x01);
          data.put(index + 1, (byte) 0x02);
          data.put(index + 2, (byte) 0x03);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 3L, (long) data.capacity());
        final int row_bytes = 512 * 3;

        for (int y = 0; y < 512; ++y) {
          for (int x = 0; x < 512; ++x) {
            final int base = (y * row_bytes) + (x * 3);
            final int index0 = base + 0;
            final int index1 = base + 1;
            final int index2 = base + 2;

            final long val0 = Byte.toUnsignedLong(data.get(index0));
            final long val1 = Byte.toUnsignedLong(data.get(index1));
            final long val2 = Byte.toUnsignedLong(data.get(index2));

            if (x >= 128 && x < 256 && y >= 128 && y < 256) {
              Assert.assertEquals(0x01L, val0);
              Assert.assertEquals(0x02L, val1);
              Assert.assertEquals(0x03L, val2);
            } else {
              Assert.assertEquals(0x0L, val0);
              Assert.assertEquals(0x0L, val1);
              Assert.assertEquals(0x0L, val2);
            }
          }
        }
      }
    }

  }

  @Test public final void testTextureCubeUpdateAreaGet2BPP()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u = us.get(0);
    final JCGLTextureCubeType ta =
      t.textureCubeAllocate(
        u,
        512L,
        JCGLTextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      {
        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAllCube(ta);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); ++index) {
          data.put(index, (byte) 0);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final AreaInclusiveUnsignedL area = AreaInclusiveUnsignedL.of(
          new UnsignedRangeInclusiveL(128L, 255L),
          new UnsignedRangeInclusiveL(128L, 255L));

        final JCGLTextureCubeUpdateType up =
          JCGLTextureUpdates.newUpdateReplacingAreaCube(ta, area);
        final ByteBuffer data = up.getData();
        Assert.assertEquals(128L * 128L * 2L, (long) data.capacity());
        for (int index = 0; index < data.capacity(); index += 2) {
          data.put(index + 0, (byte) 0x01);
          data.put(index + 1, (byte) 0x02);
        }
        t.textureCubeUpdateLH(u, v, up);
      }

      {
        final ByteBuffer data = t.textureCubeGetImageLH(u, v, ta);
        Assert.assertEquals(512L * 512L * 2L, (long) data.capacity());
        final int row_bytes = 512 * 2;

        for (int y = 0; y < 512; ++y) {
          for (int x = 0; x < 512; ++x) {
            final int base = (y * row_bytes) + (x * 2);
            final int index0 = base + 0;
            final int index1 = base + 1;

            final long val0 = Byte.toUnsignedLong(data.get(index0));
            final long val1 = Byte.toUnsignedLong(data.get(index1));

            if (x >= 128 && x < 256 && y >= 128 && y < 256) {
              Assert.assertEquals(0x01L, val0);
              Assert.assertEquals(0x02L, val1);
            } else {
              Assert.assertEquals(0x0L, val0);
              Assert.assertEquals(0x0L, val1);
            }
          }
        }
      }
    }
  }

  @Test public final void testTexture2DCubeBindExclusive()
  {
    final JCGLTexturesType t = this.getTextures("main");
    final List<JCGLTextureUnitType> us = t.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLTexture2DType ta =
      t.texture2DAllocate(
        u0,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLTextureCubeType tb =
      t.textureCubeAllocate(
        u0,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    t.textureUnitUnbind(u0);
    Assert.assertFalse(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, tb));
    Assert.assertFalse(t.textureCubeIsBoundAnywhere(tb));

    t.texture2DBind(u0, ta);
    Assert.assertTrue(t.textureUnitIsBound(u0));
    Assert.assertTrue(t.texture2DIsBound(u0, ta));
    Assert.assertTrue(t.texture2DIsBoundAnywhere(ta));
    Assert.assertFalse(t.textureCubeIsBound(u0, tb));
    Assert.assertFalse(t.textureCubeIsBoundAnywhere(tb));

    t.textureCubeBind(u0, tb);
    Assert.assertTrue(t.textureUnitIsBound(u0));
    Assert.assertFalse(t.texture2DIsBound(u0, ta));
    Assert.assertFalse(t.texture2DIsBoundAnywhere(ta));
    Assert.assertTrue(t.textureCubeIsBound(u0, tb));
    Assert.assertTrue(t.textureCubeIsBoundAnywhere(tb));
  }
}
