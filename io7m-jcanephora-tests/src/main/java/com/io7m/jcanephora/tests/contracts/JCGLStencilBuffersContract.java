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

import com.io7m.jcanephora.core.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Stencil buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLStencilBuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  private static JCGLFramebufferType stencillessFramebuffer(
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

  @Test
  public final void testStencilTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertFalse(g_stencil.stencilBufferIsEnabled());
    g_stencil.stencilBufferEnable();
    Assert.assertTrue(g_stencil.stencilBufferIsEnabled());
    g_stencil.stencilBufferEnable();
    Assert.assertTrue(g_stencil.stencilBufferIsEnabled());
    g_stencil.stencilBufferDisable();
    Assert.assertFalse(g_stencil.stencilBufferIsEnabled());
    g_stencil.stencilBufferDisable();
    Assert.assertFalse(g_stencil.stencilBufferIsEnabled());
  }

  @Test
  public final void testStencilClear()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    g_stencil.stencilBufferClear(0xff);
  }

  @Test
  public final void testStencilMask()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetMaskFrontFaces()));
    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetMaskBackFaces()));

    g_stencil.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 23);
    g_stencil.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 31);
    Assert.assertEquals(23L, (long) g_stencil.stencilBufferGetMaskFrontFaces());
    Assert.assertEquals(31L, (long) g_stencil.stencilBufferGetMaskBackFaces());

    g_stencil.stencilBufferMask(JCGLFaceSelection.FACE_FRONT_AND_BACK, 7);
    Assert.assertEquals(7L, (long) g_stencil.stencilBufferGetMaskFrontFaces());
    Assert.assertEquals(7L, (long) g_stencil.stencilBufferGetMaskBackFaces());
  }

  @Test
  public final void testStencilOperationFront()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationDepthFailFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationPassFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationStencilFailFront());

    for (final JCGLStencilOperation op : JCGLStencilOperation.values()) {
      g_stencil.stencilBufferOperation(
        JCGLFaceSelection.FACE_FRONT, op, op, op);

      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationDepthFailFront());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationPassFront());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationStencilFailFront());

      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationDepthFailBack());
      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationPassBack());
      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationStencilFailBack());
    }
  }

  @Test
  public final void testStencilOperationBack()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationDepthFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationPassBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationStencilFailBack());

    for (final JCGLStencilOperation op : JCGLStencilOperation.values()) {
      g_stencil.stencilBufferOperation(
        JCGLFaceSelection.FACE_BACK, op, op, op);

      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationDepthFailBack());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationPassBack());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationStencilFailBack());

      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationDepthFailFront());
      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationPassFront());
      Assert.assertEquals(
        JCGLStencilOperation.STENCIL_OP_KEEP,
        g_stencil.stencilBufferGetOperationStencilFailFront());
    }
  }

  @Test
  public final void testStencilOperationBoth()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationDepthFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationPassBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationStencilFailBack());

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationDepthFailFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationPassFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_stencil.stencilBufferGetOperationStencilFailFront());

    for (final JCGLStencilOperation op : JCGLStencilOperation.values()) {
      g_stencil.stencilBufferOperation(
        JCGLFaceSelection.FACE_FRONT_AND_BACK, op, op, op);

      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationDepthFailBack());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationPassBack());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationStencilFailBack());

      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationDepthFailFront());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationPassFront());
      Assert.assertEquals(
        op, g_stencil.stencilBufferGetOperationStencilFailFront());
    }
  }

  @Test
  public final void testStencilFunctionBoth()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_stencil.stencilBufferGetFunctionFront());
    Assert.assertEquals(
      0L, (long) g_stencil.stencilBufferGetFunctionReferenceFront());
    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskFront()));

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_stencil.stencilBufferGetFunctionBack());
    Assert.assertEquals(
      0L, (long) g_stencil.stencilBufferGetFunctionReferenceBack());
    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskBack()));

    for (final JCGLStencilFunction f : JCGLStencilFunction.values()) {
      g_stencil.stencilBufferFunction(
        JCGLFaceSelection.FACE_FRONT_AND_BACK, f, 1, 2);

      Assert.assertEquals(
        f, g_stencil.stencilBufferGetFunctionFront());
      Assert.assertEquals(
        1L, (long) g_stencil.stencilBufferGetFunctionReferenceFront());
      Assert.assertEquals(
        2L, (long) g_stencil.stencilBufferGetFunctionMaskFront());

      Assert.assertEquals(
        f, g_stencil.stencilBufferGetFunctionBack());
      Assert.assertEquals(
        1L, (long) g_stencil.stencilBufferGetFunctionReferenceBack());
      Assert.assertEquals(
        2L, (long) g_stencil.stencilBufferGetFunctionMaskBack());
    }
  }

  @Test
  public final void testStencilFunctionFront()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_stencil.stencilBufferGetFunctionFront());
    Assert.assertEquals(
      0L, (long) g_stencil.stencilBufferGetFunctionReferenceFront());
    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskFront()));

    for (final JCGLStencilFunction f : JCGLStencilFunction.values()) {
      g_stencil.stencilBufferFunction(
        JCGLFaceSelection.FACE_FRONT, f, 1, 2);

      Assert.assertEquals(
        f, g_stencil.stencilBufferGetFunctionFront());
      Assert.assertEquals(
        1L, (long) g_stencil.stencilBufferGetFunctionReferenceFront());
      Assert.assertEquals(
        2L, (long) g_stencil.stencilBufferGetFunctionMaskFront());

      Assert.assertEquals(
        JCGLStencilFunction.STENCIL_ALWAYS,
        g_stencil.stencilBufferGetFunctionBack());
      Assert.assertEquals(
        0L,
        Integer.toUnsignedLong(
          g_stencil.stencilBufferGetFunctionReferenceBack()));
      Assert.assertEquals(
        0xffffffffL,
        Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskBack()));
    }
  }

  @Test
  public final void testStencilFunctionBack()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_stencil.stencilBufferGetFunctionFront());
    Assert.assertEquals(
      0L, (long) g_stencil.stencilBufferGetFunctionReferenceFront());
    Assert.assertEquals(
      0xffffffffL,
      Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskFront()));

    for (final JCGLStencilFunction f : JCGLStencilFunction.values()) {
      g_stencil.stencilBufferFunction(
        JCGLFaceSelection.FACE_BACK, f, 1, 2);

      Assert.assertEquals(
        f, g_stencil.stencilBufferGetFunctionBack());
      Assert.assertEquals(
        1L, (long) g_stencil.stencilBufferGetFunctionReferenceBack());
      Assert.assertEquals(
        2L, (long) g_stencil.stencilBufferGetFunctionMaskBack());

      Assert.assertEquals(
        JCGLStencilFunction.STENCIL_ALWAYS,
        g_stencil.stencilBufferGetFunctionFront());
      Assert.assertEquals(
        0L,
        Integer.toUnsignedLong(
          g_stencil.stencilBufferGetFunctionReferenceFront()));
      Assert.assertEquals(
        0xffffffffL,
        Integer.toUnsignedLong(g_stencil.stencilBufferGetFunctionMaskFront()));
    }
  }

  @Test
  public final void testStencilGetBitsFramebuffer0()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, i.getTextures());
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));
    Assert.assertEquals(0L, (long) g_stencil.stencilBufferGetBits());
  }

  @Test
  public final void testNoStencilNoFramebufferClear()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferClear(1);
  }

  @Test
  public final void testNoStencilNoFramebufferMask()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 23);
  }

  @Test
  public final void testNoStencilNoFramebufferFunction()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS, 0, 0);
  }

  @Test
  public final void testNoStencilNoFramebufferOperation()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
  }

  @Test
  public final void testNoStencilNoFramebufferTestDisable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferDisable();
  }

  @Test
  public final void testNoStencilNoFramebufferTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferEnable();
  }

  @Test
  public final void testNoStencilNoFramebufferTestIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 0, 0);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferIsEnabled();
  }

  @Test
  public final void testNoStencilFramebufferClear()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferClear(1);
  }

  @Test
  public final void testNoStencilFramebufferMask()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 23);
  }

  @Test
  public final void testNoStencilFramebufferFunction()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS,
      0,
      0);
  }

  @Test
  public final void testNoStencilFramebufferOperation()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
  }

  @Test
  public final void testNoStencilFramebufferTestDisable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferDisable();
  }

  @Test
  public final void testNoStencilFramebufferTestEnable()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferEnable();
  }

  @Test
  public final void testNoStencilFramebufferTestIsEnabled()
  {
    final Interfaces i = this.getInterfaces("main", 24, 8);
    final JCGLStencilBuffersType g_stencil = i.getStencilBuffers();
    final JCGLFramebuffersType g_fb = i.getFramebuffers();
    final JCGLTexturesType g_tex = i.getTextures();
    final JCGLFramebufferType fb =
      JCGLStencilBuffersContract.stencillessFramebuffer(g_fb, g_tex);
    Assert.assertTrue(g_fb.framebufferDrawIsBound(fb));

    this.expected.expect(JCGLExceptionNoStencilBuffer.class);
    g_stencil.stencilBufferIsEnabled();
  }

  protected static final class Interfaces
  {
    private final JCGLContextType context;
    private final JCGLFramebuffersType framebuffers;
    private final JCGLTexturesType textures;
    private final JCGLStencilBuffersType stencil_buffers;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLFramebuffersType in_framebuffers,
      final JCGLTexturesType in_textures,
      final JCGLStencilBuffersType in_stencil_buffers)
    {
      this.context = NullCheck.notNull(in_context);
      this.framebuffers = NullCheck.notNull(in_framebuffers);
      this.textures = NullCheck.notNull(in_textures);
      this.stencil_buffers = NullCheck.notNull(in_stencil_buffers);
    }

    public JCGLStencilBuffersType getStencilBuffers()
    {
      return this.stencil_buffers;
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
