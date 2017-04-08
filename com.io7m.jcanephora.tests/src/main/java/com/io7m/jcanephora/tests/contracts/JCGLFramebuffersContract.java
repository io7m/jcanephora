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

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionFeedback;
import com.io7m.jcanephora.core.JCGLExceptionFormatError;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferInvalid;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferReadDrawSame;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferWrongBlitFilter;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLFramebufferBlitBuffer;
import com.io7m.jcanephora.core.JCGLFramebufferBlitFilter;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferStatus;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureUpdates;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import com.io7m.jregions.core.unparameterized.areas.AreasL;
import com.io7m.jregions.core.unparameterized.sizes.AreaSizesL;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Framebuffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLFramebuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract Interfaces getInterfaces(String name);

  protected abstract boolean hasRealBlitting();

  @Test
  public final void testFramebufferBuildWrongContextAllocate()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebuffersType g_fb_alt = ia.getFramebuffers();

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    final JCGLFramebufferBuilderType fbb = g_fb_alt.framebufferNewBuilder();
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    g_fb_main.framebufferAllocate(fbb);
  }

  @Test
  public final void testFramebufferBuildWrongContextDepth()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebufferBuilderType fbb = g_fb_main.framebufferNewBuilder();

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    final JCGLTexturesType g_tex = ia.getTextures();
    final List<JCGLTextureUnitType> us = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType tc = g_tex.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    ia.context.contextReleaseCurrent();
    im.context.contextMakeCurrent();

    this.expected.expect(JCGLExceptionWrongContext.class);
    fbb.attachDepthTexture2D(tc);
  }

  @Test
  public final void testFramebufferBuildWrongContextDepthStencil()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebufferBuilderType fbb = g_fb_main.framebufferNewBuilder();

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    final JCGLTexturesType g_tex = ia.getTextures();
    final List<JCGLTextureUnitType> us = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType tc = g_tex.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    fbb.attachDepthStencilTexture2D(tc);
  }

  @Test
  public final void testFramebufferBuildWrongContextColorAttachmentPoint()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    ia.context.contextReleaseCurrent();
    im.context.contextMakeCurrent();
    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    im.context.contextReleaseCurrent();

    ia.context.contextMakeCurrent();
    final JCGLFramebuffersType g_fb_alt = ia.getFramebuffers();

    final List<JCGLFramebufferColorAttachmentPointType> points =
      g_fb_alt.framebufferGetColorAttachments();
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    final List<JCGLFramebufferDrawBufferType> draw_buffers =
      g_fb_main.framebufferGetDrawBuffers();
    final JCGLFramebufferBuilderType fbb =
      g_fb_main.framebufferNewBuilder();

    final JCGLTexturesType g_tex = im.getTextures();
    final List<JCGLTextureUnitType> us = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType tc = g_tex.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(JCGLExceptionWrongContext.class);
    fbb.attachColorTexture2DAt(points.get(0), draw_buffers.get(0), tc);
  }

  @Test
  public final void testFramebufferBuildWrongContextColorDrawBuffer()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebuffersType g_fb_alt = ia.getFramebuffers();

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    ia.context.contextReleaseCurrent();
    im.context.contextMakeCurrent();
    final List<JCGLFramebufferColorAttachmentPointType> points =
      g_fb_main.framebufferGetColorAttachments();
    im.context.contextReleaseCurrent();

    ia.context.contextMakeCurrent();
    final List<JCGLFramebufferDrawBufferType> draw_buffers =
      g_fb_alt.framebufferGetDrawBuffers();
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    final JCGLFramebufferBuilderType fbb = g_fb_main.framebufferNewBuilder();
    final JCGLTexturesType g_tex = im.getTextures();
    final List<JCGLTextureUnitType> us = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType tc = g_tex.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(JCGLExceptionWrongContext.class);
    fbb.attachColorTexture2DAt(points.get(0), draw_buffers.get(0), tc);
  }

  @Test
  public final void testFramebufferBuildWrongContextColorTexture()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    Assert.assertFalse(im.context.contextIsCurrent());
    Assert.assertTrue(ia.context.contextIsCurrent());

    ia.context.contextReleaseCurrent();
    im.context.contextMakeCurrent();
    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    im.context.contextReleaseCurrent();

    ia.context.contextMakeCurrent();
    final JCGLFramebuffersType g_fb_alt = ia.getFramebuffers();
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    final List<JCGLFramebufferColorAttachmentPointType> points =
      g_fb_main.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> draw_buffers =
      g_fb_main.framebufferGetDrawBuffers();

    final JCGLFramebufferBuilderType fbb = g_fb_main.framebufferNewBuilder();
    im.context.contextReleaseCurrent();

    ia.context.contextMakeCurrent();
    final JCGLTexturesType g_tex = ia.getTextures();
    final List<JCGLTextureUnitType> us = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType tc = g_tex.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tex.textureUnitUnbind(u0);
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    fbb.attachColorTexture2DAt(points.get(0), draw_buffers.get(0), tc);
  }

  @Test
  public final void testFramebufferValidateDrawUnbound()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    this.expected.expect(JCGLExceptionFramebufferNotBound.class);
    g_fb.framebufferDrawValidate();
  }

  @Test
  public final void testFramebufferValidateReadUnbound()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    this.expected.expect(JCGLExceptionFramebufferNotBound.class);
    g_fb.framebufferReadValidate();
  }

  @Test
  public final void testFramebufferBuildNothing()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    this.expected.expect(JCGLExceptionFramebufferInvalid.class);
    g_fb.framebufferAllocate(fbb);
  }

  @Test
  public final void testFramebufferBuildDepthOnly()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(16L, (long) fb.framebufferGetDepthBits());
  }

  @Test
  public final void testFramebufferBindIdentitiesDraw()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(Optional.of(fb), g_fb.framebufferDrawGetBound());
    Assert.assertTrue(g_fb.framebufferDrawAnyIsBound());
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    g_fb.framebufferDrawUnbind();
    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());
    Assert.assertFalse(g_fb.framebufferDrawIsBound(fb));

    g_fb.framebufferDrawBind(fb);
    Assert.assertEquals(Optional.of(fb), g_fb.framebufferDrawGetBound());
    Assert.assertTrue(g_fb.framebufferDrawAnyIsBound());
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));
  }

  @Test
  public final void testFramebufferDeleteIdentitiesDraw()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(Optional.of(fb), g_fb.framebufferDrawGetBound());
    Assert.assertTrue(g_fb.framebufferDrawAnyIsBound());
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    g_fb.framebufferDelete(fb);
    Assert.assertTrue(fb.isDeleted());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());
  }

  @Test
  public final void testFramebufferBindDrawDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDelete(fb);

    this.expected.expect(JCGLExceptionDeleted.class);
    g_fb.framebufferDrawBind(fb);
  }

  @Test
  public final void testFramebufferIsBoundReadDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDelete(fb);

    this.expected.expect(JCGLExceptionDeleted.class);
    g_fb.framebufferReadIsBound(fb);
  }

  @Test
  public final void testFramebufferIsBoundDrawDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDelete(fb);

    this.expected.expect(JCGLExceptionDeleted.class);
    g_fb.framebufferDrawIsBound(fb);
  }

  @Test
  public final void testFramebufferBindReadDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDelete(fb);

    this.expected.expect(JCGLExceptionDeleted.class);
    g_fb.framebufferReadBind(fb);
  }

  @Test
  public final void testFramebufferDeleteIdentitiesRead()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadBind(fb);

    Assert.assertEquals(Optional.of(fb), g_fb.framebufferReadGetBound());
    Assert.assertTrue(g_fb.framebufferReadAnyIsBound());
    Assert.assertTrue(g_fb.framebufferReadIsBound(fb));

    g_fb.framebufferDelete(fb);
    Assert.assertTrue(fb.isDeleted());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());
  }

  @Test
  public final void testFramebufferBindFeedback()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);
    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDrawUnbind();

    g_tx.texture2DBind(u0, t);
    this.expected.expect(JCGLExceptionFeedback.class);
    g_fb.framebufferDrawBind(fb);
  }

  @Test
  public final void testFramebufferBindFeedbackTexture()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);
    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDrawUnbind();

    g_fb.framebufferDrawBind(fb);
    this.expected.expect(JCGLExceptionFeedback.class);
    g_tx.texture2DBind(u0, t);
  }

  @Test
  public final void testFramebufferBuildDepthNotStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(JCGLExceptionFormatError.class);
    fbb.attachDepthTexture2D(t);
  }

  @Test
  public final void testFramebufferBuildDepthStencilNotStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    this.expected.expect(JCGLExceptionFormatError.class);
    fbb.attachDepthStencilTexture2D(t);
  }

  @Test
  public final void testFramebufferBuildDepthStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    fbb.attachDepthStencilTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(8L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(24L, (long) fb.framebufferGetDepthBits());

    final Set<JCGLReferableType> refs = fb.getReferences();
    Assert.assertEquals(1L, (long) refs.size());
    Assert.assertTrue(refs.contains(t));
  }

  @Test
  public final void testFramebufferBuildDetachDepth()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLTexture2DType tc = g_tx.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLTexture2DType td = g_tx.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLFramebufferColorAttachmentPointType p0 = ps.get(0);
    final JCGLFramebufferDrawBufferType db0 = db.get(0);
    fbb.attachColorTexture2DAt(p0, db0, tc);
    fbb.attachDepthTexture2D(td);
    fbb.detachDepth();

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(0L, (long) fb.framebufferGetDepthBits());

    final Set<JCGLReferableType> refs = fb.getReferences();
    Assert.assertEquals(1L, (long) refs.size());
    Assert.assertTrue(refs.contains(tc));
    Assert.assertFalse(refs.contains(td));
  }

  @Test
  public final void testFramebufferBuildDetachDepthStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLTexture2DType tc = g_tx.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLTexture2DType td = g_tx.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLFramebufferColorAttachmentPointType p0 = ps.get(0);
    final JCGLFramebufferDrawBufferType db0 = db.get(0);
    fbb.attachColorTexture2DAt(p0, db0, tc);
    fbb.attachDepthStencilTexture2D(td);
    fbb.detachDepth();

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(0L, (long) fb.framebufferGetDepthBits());

    final Set<JCGLReferableType> refs = fb.getReferences();
    Assert.assertEquals(1L, (long) refs.size());
    Assert.assertTrue(refs.contains(tc));
    Assert.assertFalse(refs.contains(td));
  }

  @Test
  public final void testFramebufferBuildColorAll()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final int min = Math.min(db.size(), ps.size());
    final List<JCGLTexture2DType> attaches = new ArrayList<>(min);
    for (int index = 0; index < min; ++index) {
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0,
        64L,
        64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      fbb.attachColorTexture2DAt(ps.get(index), db.get(index), tc);
      attaches.add(tc);
    }

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(0L, (long) fb.framebufferGetDepthBits());

    final Set<JCGLReferableType> refs = fb.getReferences();
    Assert.assertEquals((long) min, (long) refs.size());

    for (int index = 0; index < min; ++index) {
      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      final Optional<JCGLFramebufferColorAttachmentType> opt =
        fb.framebufferGetColorAttachment(point);
      Assert.assertTrue(opt.isPresent());
      final JCGLFramebufferColorAttachmentType attach = opt.get();
      Assert.assertEquals(attaches.get(index), attach);
      Assert.assertTrue(refs.contains(attach));
    }
  }

  @Test
  public final void testFramebufferBuildDetachColorAll()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLTexture2DType td = g_tx.texture2DAllocate(
      u0,
      64L,
      64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);
    fbb.attachDepthStencilTexture2D(td);

    final int min = Math.min(db.size(), ps.size());
    final List<JCGLFramebufferColorAttachmentType> colors =
      new ArrayList<>(min);
    for (int index = 0; index < min; ++index) {
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0,
        64L,
        64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      fbb.attachColorTexture2DAt(point, db.get(index), tc);
      fbb.detachColorAttachment(point);
      colors.add(tc);
    }

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(8L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(24L, (long) fb.framebufferGetDepthBits());

    final Set<JCGLReferableType> refs = fb.getReferences();
    Assert.assertTrue(refs.contains(td));
    Assert.assertEquals(1L, (long) refs.size());

    for (int index = 0; index < min; ++index) {
      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      final Optional<JCGLFramebufferColorAttachmentType> opt =
        fb.framebufferGetColorAttachment(point);
      Assert.assertFalse(opt.isPresent());
      final JCGLFramebufferColorAttachmentType tc = colors.get(index);
      Assert.assertFalse(refs.contains(tc));
    }
  }

  @Test
  public final void testFramebufferBlitWrongFilterDepth()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final List<JCGLFramebufferColorAttachmentPointType> points =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> buffers =
      g_fb.framebufferGetDrawBuffers();

    final JCGLFramebufferType fb_read;
    final JCGLFramebufferType fb_draw;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType td = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(td.textureGetSize());
      fbb.attachDepthTexture2D(td);
      fbb.attachColorTexture2DAt(points.get(0), buffers.get(0), tc);
      fb_draw = g_fb.framebufferAllocate(fbb);
    }

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType td = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      fbb.attachDepthTexture2D(td);
      fbb.attachColorTexture2DAt(points.get(0), buffers.get(0), tc);
      fb_read = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());

    Assert.assertEquals(Optional.empty(), g_fb.framebufferReadGetBound());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());

    g_fb.framebufferDrawBind(fb_draw);
    Assert.assertEquals(
      JCGLFramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE,
      g_fb.framebufferDrawValidate());

    g_fb.framebufferReadBind(fb_read);
    Assert.assertEquals(
      JCGLFramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE,
      g_fb.framebufferReadValidate());

    this.expected.expect(JCGLExceptionFramebufferWrongBlitFilter.class);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test
  public final void testFramebufferBlitSameFramebuffer()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLFramebufferType fb;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t.textureGetSize());
      fbb.attachDepthTexture2D(t);
      fb = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());

    Assert.assertEquals(Optional.empty(), g_fb.framebufferReadGetBound());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());

    g_fb.framebufferDrawBind(fb);
    g_fb.framebufferReadBind(fb);

    this.expected.expect(JCGLExceptionFramebufferReadDrawSame.class);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test
  public final void testFramebufferBlitSameFramebufferDefault()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();

    final AreaL area = AreasL.create(0L, 0L, 640L, 480L);

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    this.expected.expect(JCGLExceptionFramebufferReadDrawSame.class);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test
  public final void testFramebufferBlitOK()
  {
    Assume.assumeTrue("Real blitting is supported", this.hasRealBlitting());

    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final List<JCGLFramebufferColorAttachmentPointType> points = g_fb
      .framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> buffers = g_fb
      .framebufferGetDrawBuffers();

    final JCGLFramebufferType fb_read;
    final JCGLFramebufferType fb_draw;
    final AreaL area;
    final JCGLTexture2DType t_draw_color;
    final JCGLTexture2DType t_read_color;
    final JCGLTexture2DType t_draw_depth;
    final JCGLTexture2DType t_read_depth;
    final ByteBuffer expected_contents;

    /*
     * Allocate a draw framebuffer.
     */

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      t_draw_color = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      t_draw_depth = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t_draw_color.textureGetSize());
      fbb.attachColorTexture2DAt(points.get(0), buffers.get(0), t_draw_color);
      fbb.attachDepthTexture2D(t_draw_depth);
      fb_draw = g_fb.framebufferAllocate(fbb);
    }

    /*
     * Allocate a read framebuffer and fill it with random data.
     */

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      t_read_color = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      t_read_depth = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);

      final JCGLTexture2DUpdateType up =
        JCGLTextureUpdates.newUpdateReplacingAll2D(t_read_depth);
      expected_contents = up.getData();
      for (int index = 0; index < expected_contents.capacity(); ++index) {
        expected_contents.put(index, (byte) (Math.random() * 0xff));
      }
      g_tx.texture2DUpdate(u0, up);
      g_tx.textureUnitUnbind(u0);

      fbb.attachColorTexture2DAt(points.get(0), buffers.get(0), t_read_color);
      fbb.attachDepthTexture2D(t_read_depth);
      fb_read = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();
    g_fb.framebufferDrawBind(fb_draw);
    g_fb.framebufferReadBind(fb_read);

    /*
     * Blit the contents of the read framebuffer to the draw framebuffer.
     * The draw framebuffer should now contain the random data inserted into
     * the read framebuffer earlier.
     */

    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    /*
     * Compare the contents of each of the buffers.
     */

    final ByteBuffer image_d = g_tx.texture2DGetImage(u0, t_draw_depth);
    final ByteBuffer image_r = g_tx.texture2DGetImage(u0, t_read_depth);
    final byte[] image_db = new byte[image_d.capacity()];
    final byte[] image_rb = new byte[image_r.capacity()];
    final byte[] image_exp = new byte[expected_contents.capacity()];
    image_d.get(image_db);
    image_r.get(image_rb);
    expected_contents.get(image_exp);

    Assert.assertArrayEquals(image_exp, image_rb);
    Assert.assertArrayEquals(image_exp, image_db);
    Assert.assertArrayEquals(image_db, image_rb);
  }

  @Test
  public final void testFramebufferBlitWrongFilterStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLFramebufferType fb_read;
    final JCGLFramebufferType fb_draw;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t.textureGetSize());
      fbb.attachDepthStencilTexture2D(t);
      fb_draw = g_fb.framebufferAllocate(fbb);
    }

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      fbb.attachDepthStencilTexture2D(t);
      fb_read = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());

    Assert.assertEquals(Optional.empty(), g_fb.framebufferReadGetBound());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());

    g_fb.framebufferDrawBind(fb_draw);
    g_fb.framebufferReadBind(fb_read);

    this.expected.expect(JCGLExceptionFramebufferWrongBlitFilter.class);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test
  public final void testFramebufferBlitWrongFilterDepthStencil()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLFramebufferType fb_read;
    final JCGLFramebufferType fb_draw;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t.textureGetSize());
      fbb.attachDepthStencilTexture2D(t);
      fb_draw = g_fb.framebufferAllocate(fbb);
    }

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      fbb.attachDepthStencilTexture2D(t);
      fb_read = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());

    Assert.assertEquals(Optional.empty(), g_fb.framebufferReadGetBound());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());

    g_fb.framebufferDrawBind(fb_draw);
    g_fb.framebufferReadBind(fb_read);

    this.expected.expect(JCGLExceptionFramebufferWrongBlitFilter.class);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(
        JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH,
        JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_LINEAR);
  }

  @Test
  public final void testFramebufferBlitReadNotBound()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLFramebufferType fb_draw;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t.textureGetSize());
      fbb.attachDepthTexture2D(t);
      fb_draw = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();
    g_fb.framebufferDrawBind(fb_draw);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);
  }

  @Test
  public final void testFramebufferBlitDrawNotBound()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();
    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final JCGLFramebufferType fb_read;
    final AreaL area;

    {
      final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
      final JCGLTexture2DType t = g_tx.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      g_tx.textureUnitUnbind(u0);

      area = AreaSizesL.area(t.textureGetSize());
      fbb.attachDepthTexture2D(t);
      fb_read = g_fb.framebufferAllocate(fbb);
    }

    g_fb.framebufferDrawUnbind();
    g_fb.framebufferReadUnbind();
    g_fb.framebufferReadBind(fb_read);
    g_fb.framebufferBlit(
      area,
      area,
      EnumSet.of(JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH),
      JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);
  }

  @Test
  public final void testFramebufferBindIdentitiesRead()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tx = i.getTextures();

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);
    final JCGLTexture2DType t = g_tx.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tx.textureUnitUnbind(u0);

    fbb.attachDepthTexture2D(t);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    g_fb.framebufferDrawUnbind();

    g_fb.framebufferReadBind(fb);
    Assert.assertEquals(Optional.of(fb), g_fb.framebufferReadGetBound());
    Assert.assertTrue(g_fb.framebufferReadAnyIsBound());
    Assert.assertTrue(g_fb.framebufferReadIsBound(fb));

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());
    Assert.assertFalse(g_fb.framebufferDrawIsBound(fb));

    g_fb.framebufferReadUnbind();
    Assert.assertEquals(Optional.empty(), g_fb.framebufferReadGetBound());
    Assert.assertFalse(g_fb.framebufferReadAnyIsBound());
    Assert.assertFalse(g_fb.framebufferReadIsBound(fb));

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());
    Assert.assertFalse(g_fb.framebufferDrawIsBound(fb));

    g_fb.framebufferReadBind(fb);
    Assert.assertEquals(Optional.of(fb), g_fb.framebufferReadGetBound());
    Assert.assertTrue(g_fb.framebufferReadAnyIsBound());
    Assert.assertTrue(g_fb.framebufferReadIsBound(fb));

    Assert.assertEquals(Optional.empty(), g_fb.framebufferDrawGetBound());
    Assert.assertFalse(g_fb.framebufferDrawAnyIsBound());
    Assert.assertFalse(g_fb.framebufferDrawIsBound(fb));
  }

  protected static final class Interfaces
  {
    private final JCGLContextType context;
    private final JCGLFramebuffersType framebuffers;
    private final JCGLTexturesType textures;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLFramebuffersType in_framebuffers,
      final JCGLTexturesType in_textures)
    {
      this.context =
        NullCheck.notNull(in_context, "Context");
      this.framebuffers =
        NullCheck.notNull(in_framebuffers, "Framebuffers");
      this.textures =
        NullCheck.notNull(in_textures, "Textures");
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLFramebuffersType getFramebuffers()
    {
      return this.framebuffers;
    }

    public JCGLTexturesType getTextures()
    {
      return this.textures;
    }
  }

}
