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

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Depth buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLDepthBuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  private static JCGLFramebufferType depthlessFramebuffer(
    final JCGLFramebuffersType g_fb,
    final JCGLTexturesType g_tex)
  {
    final JCGLTextureUnitType u0 = g_tex.textureGetUnits().get(0);
    final JCGLTexture2DType t =
      g_tex.texture2DAllocate(
        u0, 64L, 64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tex.textureUnitUnbind(u0);

    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();
    fbb.attachColorTexture2DAt(
      g_fb.framebufferGetColorAttachments().get(0),
      g_fb.framebufferGetDrawBuffers().get(0),
      t);
    return g_fb.framebufferAllocate(fbb);
  }

  protected abstract Interfaces getInterfaces(
    String name,
    int depth,
    int stencil);

  @Test public final void testDepthTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    Assert.assertFalse(g_dep.depthBufferTestIsEnabled());
    g_dep.depthBufferTestEnable(JCGLDepthFunction.DEPTH_EQUAL);
    Assert.assertTrue(g_dep.depthBufferTestIsEnabled());
    g_dep.depthBufferTestDisable();
    Assert.assertFalse(g_dep.depthBufferTestIsEnabled());
  }

  @Test public final void testDepthWriteEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteEnable();
    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteDisable();
    Assert.assertFalse(g_dep.depthBufferWriteIsEnabled());
  }

  @Test public final void testDepthWriteEnableRedundant()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteEnable();
    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteEnable();
    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
  }

  @Test public final void testDepthWriteDisableRedundant()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    Assert.assertTrue(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteDisable();
    Assert.assertFalse(g_dep.depthBufferWriteIsEnabled());
    g_dep.depthBufferWriteDisable();
    Assert.assertFalse(g_dep.depthBufferWriteIsEnabled());
  }

  @Test public final void testDepthClear()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    g_dep.depthBufferClear(1.0f);
  }

  @Test public final void testDepthClamp()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    Assert.assertFalse(g_dep.depthClampingIsEnabled());
    g_dep.depthClampingEnable();
    Assert.assertTrue(g_dep.depthClampingIsEnabled());
    g_dep.depthClampingDisable();
    Assert.assertFalse(g_dep.depthClampingIsEnabled());
  }

  @Test public final void testDepthGetBitsNoFramebuffer0()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    Assert.assertEquals(0L, (long) g_dep.depthBufferGetBits());
  }

  @Test public final void testDepthGetBitsNoFramebuffer24_8()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    Assert.assertEquals(24L, (long) g_dep.depthBufferGetBits());
  }

  @Test public final void testDepthGetBitsFramebuffer0()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, i.getTextures());
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));
    Assert.assertEquals(0L, (long) g_dep.depthBufferGetBits());
  }

  @Test public final void testNoDepthNoFramebufferClampEnable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingEnable();
  }

  @Test public final void testNoDepthNoFramebufferClampDisable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingDisable();
  }

  @Test public final void testNoDepthNoFramebufferClampIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingIsEnabled();
  }

  @Test public final void testNoDepthNoFramebufferClear()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferClear(1.0f);
  }

  @Test public final void testNoDepthNoFramebufferTestDisable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestDisable();
  }

  @Test public final void testNoDepthNoFramebufferTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestEnable(JCGLDepthFunction.DEPTH_EQUAL);
  }

  @Test public final void testNoDepthNoFramebufferWriteDisable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteDisable();
  }

  @Test public final void testNoDepthNoFramebufferWriteEnable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteEnable();
  }

  @Test public final void testNoDepthNoFramebufferTestIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestIsEnabled();
  }

  @Test public final void testNoDepthNoFramebufferWriteIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteIsEnabled();
  }

  @Test public final void testNoDepthFramebufferClampEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingEnable();
  }

  @Test public final void testNoDepthFramebufferClampDisable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingDisable();
  }

  @Test public final void testNoDepthFramebufferClampIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthClampingIsEnabled();
  }

  @Test public final void testNoDepthFramebufferClear()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferClear(1.0f);
  }

  @Test public final void testNoDepthFramebufferTestDisable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestDisable();
  }

  @Test public final void testNoDepthFramebufferTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestEnable(JCGLDepthFunction.DEPTH_EQUAL);
  }

  @Test public final void testNoDepthFramebufferWriteDisable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteDisable();
  }

  @Test public final void testNoDepthFramebufferWriteEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteEnable();
  }

  @Test public final void testNoDepthFramebufferTestIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferTestIsEnabled();
  }

  @Test public final void testNoDepthFramebufferWriteIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLDepthBuffersType g_dep = i.getDepthBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLDepthBuffersContract.depthlessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    g_dep.depthBufferWriteIsEnabled();
  }

  protected static final class Interfaces
  {
    private final JCGLContextType context;
    private final JCGLFramebuffersType framebuffers;
    private final JCGLTexturesType textures;
    private final JCGLDepthBuffersType depth_buffers;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLFramebuffersType in_framebuffers,
      final JCGLTexturesType in_textures,
      final JCGLDepthBuffersType in_depth_buffers)
    {
      this.context = NullCheck.notNull(in_context);
      this.framebuffers = NullCheck.notNull(in_framebuffers);
      this.textures = NullCheck.notNull(in_textures);
      this.depth_buffers = NullCheck.notNull(in_depth_buffers);
    }

    public JCGLDepthBuffersType getDepthBuffers()
    {
      return this.depth_buffers;
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
