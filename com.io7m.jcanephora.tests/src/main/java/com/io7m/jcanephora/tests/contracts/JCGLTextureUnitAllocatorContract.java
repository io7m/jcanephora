/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.texture.unit_allocator.JCGLExceptionTextureUnitContextLimitReached;
import com.io7m.jcanephora.texture.unit_allocator.JCGLExceptionTextureUnitContextNotActive;
import com.io7m.jcanephora.texture.unit_allocator.JCGLExceptionTextureUnitExhausted;
import com.io7m.jcanephora.texture.unit_allocator.JCGLTextureUnitAllocatorType;
import com.io7m.jcanephora.texture.unit_allocator.JCGLTextureUnitContextParentType;
import com.io7m.jcanephora.texture.unit_allocator.JCGLTextureUnitContextType;
import com.io7m.jfunctional.Pair;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public abstract class JCGLTextureUnitAllocatorContract extends JCGLContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  private static JCGLTexture2DType newTexture(
    final JCGLTexturesType g_tx,
    final JCGLTextureUnitType u0)
  {
    return g_tx.texture2DAllocate(
      u0,
      2L,
      2L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);
  }

  private static JCGLTextureCubeType newTextureCube(
    final JCGLTexturesType g_tx,
    final JCGLTextureUnitType u0)
  {
    return g_tx.textureCubeAllocate(
      u0,
      2L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);
  }

  protected abstract JCGLTextureUnitAllocatorType newAllocator(
    final int max_depth,
    final List<JCGLTextureUnitType> u);

  protected abstract JCGLContextType newGL33Context(
    String name,
    int depth_bits,
    int stencil_bits);

  @Test
  public final void testUsage2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTexture2D(g_tx, t0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.texture2DIsBound(us.get(index), t0));
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testUsageCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTextureCube(g_tx, t0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureCubeIsBound(us.get(index), t0));
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testUsageReserved2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNewWithReserved(16);

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTexture2D(g_tx, t0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.texture2DIsBound(us.get(index), t0));
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testUsageReservedCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNewWithReserved(16);

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTextureCube(g_tx, t0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureCubeIsBound(us.get(index), t0));
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testOutOfUnits2D_0()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTexture2DType rt0 = t0;

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTexture2D(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    this.expected.expect(JCGLExceptionTextureUnitExhausted.class);
    c_0.unitContextNewWithReserved(1);
  }

  @Test
  public final void testOutOfUnits2D_1()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTexture2DType rt0 = t0;

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTexture2D(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    this.expected.expect(JCGLExceptionTextureUnitExhausted.class);
    c_0.unitContextBindTexture2D(g_tx, rt0);
  }

  @Test
  public final void testOutOfUnitsCube_0()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTextureCubeType rt0 = t0;

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTextureCube(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    this.expected.expect(JCGLExceptionTextureUnitExhausted.class);
    c_0.unitContextNewWithReserved(1);
  }

  @Test
  public final void testOutOfUnitsCube_1()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTextureCubeType rt0 = t0;

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < us.size(); ++index) {
      final JCGLTextureUnitType ru =
        c_0.unitContextBindTextureCube(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    this.expected.expect(JCGLExceptionTextureUnitExhausted.class);
    c_0.unitContextBindTextureCube(g_tx, rt0);
  }

  @Test
  public final void testUsageDeleted2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    final JCGLTexture2DType t1 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTexture2DType rt0 = t0;
    final JCGLTexture2DType rt1 = t1;

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(3, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < 4; ++index) {
      final JCGLTextureUnitType ru = c_0.unitContextBindTexture2D(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();
    for (int index = 4; index < 8; ++index) {
      final JCGLTextureUnitType ru = c_1.unitContextBindTexture2D(g_tx, rt1);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    g_tx.texture2DDelete(t0);
    c_1.unitContextFinish(g_tx);

    for (int index = 0; index < 4; ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < 8; ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);
  }

  @Test
  public final void testUsageDeletedCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    final JCGLTextureCubeType t1 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    final JCGLTextureCubeType rt0 = t0;
    final JCGLTextureCubeType rt1 = t1;

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(3, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    for (int index = 0; index < 4; ++index) {
      final JCGLTextureUnitType ru = c_0.unitContextBindTextureCube(g_tx, rt0);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();
    for (int index = 4; index < 8; ++index) {
      final JCGLTextureUnitType ru = c_1.unitContextBindTextureCube(g_tx, rt1);
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    g_tx.textureCubeDelete(t0);
    c_1.unitContextFinish(g_tx);

    for (int index = 0; index < 4; ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < 8; ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_0.unitContextFinish(g_tx);
  }

  @Test
  public final void testUsageExtension2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    final JCGLTexture2DType t1 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(3, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final JCGLTexture2DType rt0 = t0;
    final JCGLTexture2DType rt1 = t1;

    c_0.unitContextBindTexture2D(g_tx, rt0);
    c_0.unitContextBindTexture2D(g_tx, rt0);
    c_0.unitContextBindTexture2D(g_tx, rt0);
    c_0.unitContextBindTexture2D(g_tx, rt0);

    for (int index = 0; index < 4; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();

    c_1.unitContextBindTexture2D(g_tx, rt1);
    c_1.unitContextBindTexture2D(g_tx, rt1);
    c_1.unitContextBindTexture2D(g_tx, rt1);
    c_1.unitContextBindTexture2D(g_tx, rt1);

    for (int index = 0; index < 8; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_1.unitContextFinish(g_tx);

    for (int index = 0; index < 4; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testUsageExtensionCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    final JCGLTextureCubeType t1 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    for (int index = 0; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(3, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final JCGLTextureCubeType rt0 = t0;
    final JCGLTextureCubeType rt1 = t1;

    c_0.unitContextBindTextureCube(g_tx, rt0);
    c_0.unitContextBindTextureCube(g_tx, rt0);
    c_0.unitContextBindTextureCube(g_tx, rt0);
    c_0.unitContextBindTextureCube(g_tx, rt0);

    for (int index = 0; index < 4; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }

    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();

    c_1.unitContextBindTextureCube(g_tx, rt1);
    c_1.unitContextBindTextureCube(g_tx, rt1);
    c_1.unitContextBindTextureCube(g_tx, rt1);
    c_1.unitContextBindTextureCube(g_tx, rt1);

    for (int index = 0; index < 8; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    c_1.unitContextFinish(g_tx);

    for (int index = 0; index < 4; ++index) {
      Assert.assertTrue(g_tx.textureUnitIsBound(us.get(index)));
    }

    for (int index = 4; index < us.size(); ++index) {
      Assert.assertFalse(g_tx.textureUnitIsBound(us.get(index)));
    }
  }

  @Test
  public final void testUsageBigStack()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(10, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();
    final JCGLTextureUnitContextType c_2 = c_1.unitContextNew();
    final JCGLTextureUnitContextType c_3 = c_2.unitContextNew();
    final JCGLTextureUnitContextType c_4 = c_3.unitContextNew();
    final JCGLTextureUnitContextType c_5 = c_4.unitContextNew();
    final JCGLTextureUnitContextType c_6 = c_5.unitContextNew();
    final JCGLTextureUnitContextType c_7 = c_6.unitContextNew();
    final JCGLTextureUnitContextType c_8 = c_7.unitContextNew();

    c_8.unitContextFinish(g_tx);
    c_7.unitContextFinish(g_tx);
    c_6.unitContextFinish(g_tx);
    c_5.unitContextFinish(g_tx);
    c_4.unitContextFinish(g_tx);
    c_3.unitContextFinish(g_tx);
    c_2.unitContextFinish(g_tx);
    c_1.unitContextFinish(g_tx);
    c_0.unitContextFinish(g_tx);
  }

  @Test
  public final void testUsageBigStackExceeded()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(4, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();
    final JCGLTextureUnitContextType c_2 = c_1.unitContextNew();

    this.expected.expect(JCGLExceptionTextureUnitContextLimitReached.class);
    c_2.unitContextNew();
  }

  @Test
  public final void testBindError2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t0 =
      newTexture(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    c_0.unitContextFinish(g_tx);
    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextBindTexture2D(g_tx, t0);
  }

  @Test
  public final void testBindErrorCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTextureCubeType t0 =
      newTextureCube(g_tx, u0);
    g_tx.textureUnitUnbind(u0);

    c_0.unitContextFinish(g_tx);
    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextBindTextureCube(g_tx, t0);
  }

  @Test
  public final void testNewError0()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_root.unitContextNew();
  }

  @Test
  public final void testNewError1()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(3, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();

    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextNew();
  }

  @Test
  public final void testFinishError0()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    c_0.unitContextFinish(g_tx);

    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextFinish(g_tx);
  }

  @Test
  public final void testNew2D()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final Pair<JCGLTextureUnitType, JCGLTexture2DType> p =
      c_0.unitContextAllocateTexture2D(
        g_tx,
        128L,
        256L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLTexture2DType t = p.getRight();
    Assert.assertEquals(
      128L, t.rangeX().getInterval());
    Assert.assertEquals(
      256L, t.rangeY().getInterval());
    Assert.assertEquals(
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP, t.format());
    Assert.assertEquals(
      JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.wrapS());
    Assert.assertEquals(
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT, t.wrapT());
    Assert.assertEquals(
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      t.minificationFilter());
    Assert.assertEquals(
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      t.magnificationFilter());
  }

  @Test
  public final void testNewCube()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(2, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();

    final Pair<JCGLTextureUnitType, JCGLTextureCubeType> p =
      c_0.unitContextAllocateTextureCube(
        g_tx,
        128L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLTextureCubeType t = p.getRight();
    Assert.assertEquals(
      128L, t.rangeX().getInterval());
    Assert.assertEquals(
      128L, t.rangeY().getInterval());
    Assert.assertEquals(
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP, t.format());
    Assert.assertEquals(
      JCGLTextureWrapR.TEXTURE_WRAP_REPEAT, t.wrapR());
    Assert.assertEquals(
      JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE, t.wrapS());
    Assert.assertEquals(
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT, t.wrapT());
    Assert.assertEquals(
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      t.minificationFilter());
    Assert.assertEquals(
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      t.magnificationFilter());
  }

  @Test
  public final void testNew2DNotActive()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(4, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();

    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextAllocateTexture2D(
      g_tx,
      128L,
      256L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }

  @Test
  public final void testNewCubeNotActive()
  {
    final JCGLContextType gc = this.newGL33Context("main", 24, 8);
    final JCGLInterfaceGL33Type g33 = gc.contextGetGL33();
    final JCGLTexturesType g_tx = g33.textures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();

    final JCGLTextureUnitAllocatorType alloc = this.newAllocator(4, us);
    final JCGLTextureUnitContextParentType c_root = alloc.rootContext();
    final JCGLTextureUnitContextType c_0 = c_root.unitContextNew();
    final JCGLTextureUnitContextType c_1 = c_0.unitContextNew();

    this.expected.expect(JCGLExceptionTextureUnitContextNotActive.class);
    c_0.unitContextAllocateTextureCube(
      g_tx,
      128L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      JCGLTextureWrapR.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
  }
}
