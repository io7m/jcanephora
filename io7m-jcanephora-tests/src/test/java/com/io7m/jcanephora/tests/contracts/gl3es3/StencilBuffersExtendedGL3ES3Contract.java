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

package com.io7m.jcanephora.tests.contracts.gl3es3;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;

public abstract class StencilBuffersExtendedGL3ES3Contract implements
  TestContract
{
  protected abstract JCGLFramebuffersGL3Type getFramebuffersGL3(
    final TestContext tc);

  protected abstract JCGLTextures2DStaticGL3ES3Type getTexturesGL3ES3(
    final TestContext tc);

  @Test public void testStencilBufferBits()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type gf = this.getFramebuffersGL3(tc);
    final JCGLTextures2DStaticGL3ES3Type gt = this.getTexturesGL3ES3(tc);

    final Texture2DStaticType ct =
      gt.texture2DStaticAllocateRGBA8(
        "color",
        256,
        256,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final Texture2DStaticType dt =
      gt.texture2DStaticAllocateDepth24Stencil8(
        "depth+stencil",
        256,
        256,
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      gf.framebufferNewBuilderGL3ES3();
    fbb.attachColorTexture2D(ct);
    fbb.attachDepthStencilTexture2D(dt);

    final FramebufferType fb = gf.framebufferAllocate(fbb);
    gf.framebufferDrawBind(fb);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    Assert.assertTrue(gf.framebufferDrawAnyIsBound());
    Assert.assertEquals(8, gc.stencilBufferGetBits());
  }
}
