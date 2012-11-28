package com.io7m.jcanephora.contracts_embedded;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;

public abstract class StencilBuffersContract implements
  GLEmbeddedTestContract
{
  private static Framebuffer makeFramebuffer(
    final GLInterfaceEmbedded g)
    throws GLException,
      ConstraintError
  {
    final RenderbufferD24S8 rb = g.renderbufferD24S8Allocate(128, 128);
    final Texture2DRGBAStatic cb =
      g.texture2DRGBAStaticAllocate(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g.framebufferAllocate(new FramebufferAttachment[] {
        new ColorAttachment(cb, 0),
        new RenderbufferD24S8Attachment(rb) });
    return fb;
  }

  private static Framebuffer makeFramebufferNoStencil(
    final GLInterfaceEmbedded g)
    throws GLException,
      ConstraintError
  {
    final Texture2DRGBAStatic cb =
      g.texture2DRGBAStaticAllocate(
        "framebuffer",
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final Framebuffer fb =
      g
        .framebufferAllocate(new FramebufferAttachment[] { new ColorAttachment(
          cb,
          0) });
    return fb;
  }

  @Test public void testStencilBufferWithoutBoundFramebufferWorks()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    Assert.assertTrue(gl.stencilBufferGetBits() >= 0);
  }

  @Test public void testStencilBufferWithoutStencilHasNoBits()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final Framebuffer fb =
      StencilBuffersContract.makeFramebufferNoStencil(gl);
    gl.framebufferBind(fb);
    Assert.assertEquals(0, gl.stencilBufferGetBits());
  }

  @Test public void testStencilBufferWithStencilHasBits()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final Framebuffer fb = StencilBuffersContract.makeFramebuffer(gl);
    gl.framebufferBind(fb);
    Assert.assertEquals(8, gl.stencilBufferGetBits());
  }
}
