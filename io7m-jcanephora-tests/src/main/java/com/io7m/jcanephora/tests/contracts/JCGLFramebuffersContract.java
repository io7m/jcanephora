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

import com.io7m.jcanephora.core.JCGLExceptionFormatError;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferInvalid;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Framebuffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLFramebuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract Interfaces getInterfaces(String name);

  @Test public final void testFramebufferBuildWrongContext_0()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebuffersType g_fb_alt = ia.getFramebuffers();

    ia.context.contextMakeCurrent();
    final JCGLFramebufferBuilderType fbb = g_fb_alt.framebufferNewBuilder();
    ia.context.contextReleaseCurrent();

    im.context.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    g_fb_main.framebufferAllocate(fbb);
  }

  @Test public final void testFramebufferBuildWrongContext_1()
  {
    final Interfaces im = this.getInterfaces("main");
    final Interfaces ia = this.getInterfaces("alt");

    final JCGLFramebuffersType g_fb_main = im.getFramebuffers();
    final JCGLFramebufferBuilderType fbb = g_fb_main.framebufferNewBuilder();
    im.context.contextReleaseCurrent();

    final JCGLTexturesType g_tex = ia.getTextures();
    ia.context.contextMakeCurrent();
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

  @Test public final void testFramebufferValidateUnbound()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    this.expected.expect(JCGLExceptionFramebufferNotBound.class);
    g_fb.framebufferDrawValidate();
  }

  @Test public final void testFramebufferBuildNothing()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    this.expected.expect(JCGLExceptionFramebufferInvalid.class);
    g_fb.framebufferAllocate(fbb);
  }

  @Test public final void testFramebufferBuildDepthOnly()
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

  @Test public final void testFramebufferBindIdentities()
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

  @Test public final void testFramebufferBuildDepthNotStencil()
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

  @Test public final void testFramebufferBuildDepthStencilNotStencil()
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

  @Test public final void testFramebufferBuildDepthStencil()
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
  }

  @Test public final void testFramebufferBuildDetachDepth()
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
  }

  @Test public final void testFramebufferBuildDetachDepthStencil()
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
  }

  @Test public final void testFramebufferBuildColorAll()
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

    for (int index = 0; index < min; ++index) {
      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      final Optional<JCGLFramebufferColorAttachmentType> opt =
        fb.framebufferGetColorAttachment(point);
      Assert.assertTrue(opt.isPresent());
      final JCGLFramebufferColorAttachmentType attach = opt.get();
      Assert.assertEquals(attaches.get(index), attach);
    }
  }

  @Test public final void testFramebufferBuildDetachColorAll()
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
    fbb.attachDepthStencilTexture2D(td);

    final int min = Math.min(db.size(), ps.size());
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
      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      fbb.attachColorTexture2DAt(point, db.get(index), tc);
      fbb.detachColorAttachment(point);
    }

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(8L, (long) fb.framebufferGetStencilBits());
    Assert.assertEquals(24L, (long) fb.framebufferGetDepthBits());

    for (int index = 0; index < min; ++index) {
      final JCGLFramebufferColorAttachmentPointType point = ps.get(index);
      final Optional<JCGLFramebufferColorAttachmentType> opt =
        fb.framebufferGetColorAttachment(point);
      Assert.assertFalse(opt.isPresent());
    }
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
      this.context = NullCheck.notNull(in_context);
      this.framebuffers = NullCheck.notNull(in_framebuffers);
      this.textures = NullCheck.notNull(in_textures);
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
