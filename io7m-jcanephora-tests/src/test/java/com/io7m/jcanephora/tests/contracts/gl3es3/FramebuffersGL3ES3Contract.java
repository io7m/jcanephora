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

package com.io7m.jcanephora.tests.contracts.gl3es3;

import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferColorAttachmentType;
import com.io7m.jcanephora.FramebufferColorAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthAttachmentType;
import com.io7m.jcanephora.FramebufferDepthAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentVisitorType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLRenderbuffersGL3ES3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jfunctional.Some;
import com.io7m.jfunctional.Unit;
import com.io7m.junreachable.UnreachableCodeException;

public abstract class FramebuffersGL3ES3Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLFramebuffersGL3Type getFramebuffers(
    final TestContext tc);

  public abstract JCGLRenderbuffersGL3ES3Type getRenderbuffers(
    final TestContext tc);

  public abstract JCGLTextures2DStaticGL3ES3Type getTextures2D(
    TestContext tc);

  @Test public void testColorAttachmentsRenderbuffer_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLRenderbuffersGL3ES3Type glr = this.getRenderbuffers(tc);

    final RenderbufferType<RenderableColorKind> r0 =
      glr.renderbufferAllocateRGBA8888(128, 128);
    final RenderbufferType<RenderableColorKind> r1 =
      glr.renderbufferAllocateRGBA8888(128, 128);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

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

  @Test public void testColorAttachmentsRenderbuffer_1()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLRenderbuffersGL3ES3Type glr = this.getRenderbuffers(tc);

    final RenderbufferType<RenderableColorKind> r0 =
      glr.renderbufferAllocateRGBA8888(128, 128);
    final RenderbufferType<RenderableColorKind> r1 =
      glr.renderbufferAllocateRGBA8888(128, 128);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> cas =
      fbb.getColorAttachments();
    final FramebufferColorAttachmentPointType point_0 =
      glf.framebufferGetColorAttachmentPoints().get(0);
    assert point_0 != null;

    fbb.attachColorRenderbufferAt(point_0, r0);

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

    fbb.attachColorRenderbufferAt(point_0, r1);

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
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLTextures2DStaticGL3ES3Type glt = this.getTextures2D(tc);

    final Texture2DStaticType t0 =
      glt.texture2DStaticAllocateRGBA8(
        "t0",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);
    final Texture2DStaticType t1 =
      glt.texture2DStaticAllocateRGBA8(
        "t1",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

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

  @Test public void testColorAttachmentsTexture2D_1()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLTextures2DStaticGL3ES3Type glt = this.getTextures2D(tc);

    final Texture2DStaticType t0 =
      glt.texture2DStaticAllocateRGBA8(
        "t0",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);
    final Texture2DStaticType t1 =
      glt.texture2DStaticAllocateRGBA8(
        "t1",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

    final FramebufferColorAttachmentPointType point_0 =
      glf.framebufferGetColorAttachmentPoints().get(0);
    assert point_0 != null;

    fbb.attachColorTexture2DAt(point_0, t0);

    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> cas =
      fbb.getColorAttachments();

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

    fbb.attachColorTexture2DAt(point_0, t1);

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

  @Test public void testDepthAttachmentsRenderbuffers_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLRenderbuffersGL3ES3Type glr = this.getRenderbuffers(tc);

    final RenderbufferType<RenderableDepthKind> rd =
      glr.renderbufferAllocateDepth24(128, 128);
    final RenderbufferType<RenderableDepthStencilKind> rds =
      glr.renderbufferAllocateDepth24Stencil8(128, 128);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

    fbb.attachDepthRenderbuffer(rd);

    {
      final Some<FramebufferDepthAttachmentType> some_da =
        (Some<FramebufferDepthAttachmentType>) fbb.getDepthAttachment();
      some_da.get().depthAttachmentAccept(
        new FramebufferDepthAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableDepthKind> r)
          {
            Assert.assertEquals(rd, r);
            return Unit.unit();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            throw new UnreachableCodeException();
          }
        });
    }

    fbb.attachDepthStencilRenderbuffer(rds);

    Assert.assertTrue(fbb.getDepthAttachment().isNone());
    Assert.assertTrue(fbb.getStencilAttachment().isNone());

    {
      final Some<FramebufferDepthStencilAttachmentType> some_da =
        (Some<FramebufferDepthStencilAttachmentType>) fbb
          .getDepthStencilAttachment();
      some_da
        .get()
        .depthStencilAttachmentAccept(
          new FramebufferDepthStencilAttachmentVisitorType<Unit, JCGLException>() {
            @Override public Unit renderbuffer(
              final RenderbufferUsableType<RenderableDepthStencilKind> r)
            {
              Assert.assertEquals(rds, r);
              return Unit.unit();
            }

            @Override public Unit texture2DStatic(
              final Texture2DStaticUsableType t)
            {
              throw new UnreachableCodeException();
            }
          });
    }
  }

  @Test public void testDepthAttachmentsTextures_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLFramebuffersGL3Type glf = this.getFramebuffers(tc);
    final JCGLTextures2DStaticGL3ES3Type glt = this.getTextures2D(tc);

    final Texture2DStaticType t0 =
      glt.texture2DStaticAllocateDepth16(
        "t0",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final Texture2DStaticType t1 =
      glt.texture2DStaticAllocateDepth24Stencil8(
        "t1",
        128,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLFramebufferBuilderGL3ES3Type fbb =
      glf.framebufferNewBuilderGL3ES3();

    fbb.attachDepthTexture2D(t0);

    {
      final Some<FramebufferDepthAttachmentType> some_da =
        (Some<FramebufferDepthAttachmentType>) fbb.getDepthAttachment();
      some_da.get().depthAttachmentAccept(
        new FramebufferDepthAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableDepthKind> r)
          {
            throw new UnreachableCodeException();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            Assert.assertEquals(t0, t);
            return Unit.unit();
          }
        });
    }

    fbb.attachDepthStencilTexture2D(t1);

    Assert.assertTrue(fbb.getDepthAttachment().isNone());
    Assert.assertTrue(fbb.getStencilAttachment().isNone());

    {
      final Some<FramebufferDepthStencilAttachmentType> some_da =
        (Some<FramebufferDepthStencilAttachmentType>) fbb
          .getDepthStencilAttachment();
      some_da
        .get()
        .depthStencilAttachmentAccept(
          new FramebufferDepthStencilAttachmentVisitorType<Unit, JCGLException>() {
            @Override public Unit renderbuffer(
              final RenderbufferUsableType<RenderableDepthStencilKind> r)
            {
              throw new UnreachableCodeException();
            }

            @Override public Unit texture2DStatic(
              final Texture2DStaticUsableType t)
            {
              Assert.assertEquals(t1, t);
              return Unit.unit();
            }
          });
    }
  }
}
