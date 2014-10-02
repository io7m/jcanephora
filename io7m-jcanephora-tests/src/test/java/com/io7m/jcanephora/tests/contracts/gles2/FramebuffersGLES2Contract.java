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

package com.io7m.jcanephora.tests.contracts.gles2;

import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferColorAttachmentType;
import com.io7m.jcanephora.FramebufferColorAttachmentVisitorType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.api.JCGLFramebuffersGLES2Type;
import com.io7m.jcanephora.api.JCGLRenderbuffersGLES2Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGLES2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jfunctional.Unit;
import com.io7m.junreachable.UnreachableCodeException;

public abstract class FramebuffersGLES2Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLFramebuffersGLES2Type getFramebuffers(
    final TestContext tc);

  public abstract JCGLRenderbuffersGLES2Type getRenderbuffers(
    final TestContext tc);

  public abstract JCGLTextures2DStaticGLES2Type getTextures2D(
    TestContext tc);

  @Test public void testColorAttachmentsRenderbuffer_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGLES2Type glf = this.getFramebuffers(tc);
    final JCGLRenderbuffersGLES2Type glr = this.getRenderbuffers(tc);

    final RenderbufferType<RenderableColorKind> r0 =
      glr.renderbufferAllocateRGB565(128, 128);
    final RenderbufferType<RenderableColorKind> r1 =
      glr.renderbufferAllocateRGB565(128, 128);

    final JCGLFramebufferBuilderType fbb = glf.framebufferNewBuilder();

    fbb.attachColorRenderbuffer(r0);

    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> cas =
      fbb.getColorAttachments();
    final FramebufferColorAttachmentPointType point_0 =
      glf.framebufferGetColorAttachmentPoints().get(0);

    {
      final FramebufferColorAttachmentType ca_0 = cas.get(point_0);
      ca_0
        .colorAttachmentAccept(new FramebufferColorAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableColorKind> rr)
          {
            Assert.assertEquals(r0, rr);
            return Unit.unit();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            throw new UnreachableCodeException();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            throw new UnreachableCodeException();
          }
        });
    }

    fbb.attachColorRenderbuffer(r1);

    {
      final FramebufferColorAttachmentType ca_0 = cas.get(point_0);
      ca_0
        .colorAttachmentAccept(new FramebufferColorAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableColorKind> rr)
          {
            Assert.assertEquals(r1, rr);
            return Unit.unit();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            throw new UnreachableCodeException();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            throw new UnreachableCodeException();
          }
        });
    }

    glf.framebufferAllocate(fbb);
  }

  @Test public void testColorAttachmentsTexture2D_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGLES2Type glf = this.getFramebuffers(tc);
    final JCGLTextures2DStaticGLES2Type glt = this.getTextures2D(tc);

    final Texture2DStaticType t0 =
      glt.texture2DStaticAllocateRGB565(
        "t0",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);
    final Texture2DStaticType t1 =
      glt.texture2DStaticAllocateRGB565(
        "t1",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLFramebufferBuilderType fbb = glf.framebufferNewBuilder();

    fbb.attachColorTexture2D(t0);

    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> cas =
      fbb.getColorAttachments();
    final FramebufferColorAttachmentPointType point_0 =
      glf.framebufferGetColorAttachmentPoints().get(0);

    {
      final FramebufferColorAttachmentType ca_0 = cas.get(point_0);
      ca_0
        .colorAttachmentAccept(new FramebufferColorAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableColorKind> rr)
          {
            throw new UnreachableCodeException();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            Assert.assertEquals(t0, t);
            return Unit.unit();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            throw new UnreachableCodeException();
          }
        });
    }

    fbb.attachColorTexture2D(t1);

    {
      final FramebufferColorAttachmentType ca_0 = cas.get(point_0);
      ca_0
        .colorAttachmentAccept(new FramebufferColorAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableColorKind> rr)
          {
            throw new UnreachableCodeException();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            Assert.assertEquals(t1, t);
            return Unit.unit();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            throw new UnreachableCodeException();
          }
        });
    }

    glf.framebufferAllocate(fbb);
  }
}
