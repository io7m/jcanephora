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

package com.io7m.jcanephora.fake;

import java.util.ArrayList;
import java.util.List;

import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * An allocated framebuffer.
 */

public final class FakeFramebuffer extends FakeObjectUnshared implements
  FramebufferType
{
  private final List<FakeFramebufferAttachableType> color_attachments;
  private @Nullable FakeFramebufferAttachableType   depth_attachment;
  private @Nullable FakeFramebufferAttachableType   stencil_attachment;

  FakeFramebuffer(
    final FakeContext in_context,
    final FakeDrawBuffers in_draw_buffers,
    final int in_id)
    throws JCGLExceptionRuntime
  {
    super(in_context, in_id);
    this.color_attachments = new ArrayList<FakeFramebufferAttachableType>();

    final List<FramebufferDrawBufferType> buffers =
      in_draw_buffers.getDrawBuffers();
    for (int index = 0; index < buffers.size(); ++index) {
      this.color_attachments.add(null);
    }

    this.depth_attachment = null;
    this.stencil_attachment = null;
  }

  /**
   * @return The number of bits in the depth attachment (if any).
   */

  @Override @SuppressWarnings("boxing") public int framebufferGetDepthBits()
  {
    try {
      if (this.depth_attachment != null) {
        return this.depth_attachment
          .attachableAccept(
            new FakeFramebufferAttachableVisitorType<Integer, UnreachableCodeException>() {
              @Override public
                <K extends RenderbufferKind>
                Integer
                renderbuffer(
                  final FakeRenderbuffer<K> r)
              {
                switch (r.renderbufferGetFormat()) {
                  case RENDERBUFFER_COLOR_RGBA_4444:
                  case RENDERBUFFER_COLOR_RGBA_5551:
                  case RENDERBUFFER_COLOR_RGBA_8888:
                  case RENDERBUFFER_COLOR_RGB_565:
                  case RENDERBUFFER_COLOR_RGB_888:
                  {
                    throw new UnreachableCodeException();
                  }
                  case RENDERBUFFER_DEPTH_16:
                  {
                    return 16;
                  }
                  case RENDERBUFFER_DEPTH_24_STENCIL_8:
                  case RENDERBUFFER_DEPTH_24:
                  {
                    return 24;
                  }
                  case RENDERBUFFER_STENCIL_8:
                  {
                    throw new UnreachableCodeException();
                  }
                }

                throw new UnreachableCodeException();
              }

              @Override public Integer texture2D(
                final FakeTexture2DStatic t)
              {
                switch (t.textureGetFormat()) {
                  case TEXTURE_FORMAT_DEPTH_16_2BPP:
                  {
                    return 16;
                  }
                  case TEXTURE_FORMAT_DEPTH_24_4BPP:
                  case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
                  {
                    return 24;
                  }
                  case TEXTURE_FORMAT_DEPTH_32F_4BPP:
                  {
                    return 32;
                  }
                  // $CASES-OMITTED$
                  default:
                    throw new UnreachableCodeException();
                }
              }

              @Override public Integer textureCube(
                final FakeTextureCubeStatic t)
              {
                switch (t.textureGetFormat()) {
                  case TEXTURE_FORMAT_DEPTH_16_2BPP:
                  {
                    return 16;
                  }
                  case TEXTURE_FORMAT_DEPTH_24_4BPP:
                  case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
                  {
                    return 24;
                  }
                  case TEXTURE_FORMAT_DEPTH_32F_4BPP:
                  {
                    return 32;
                  }
                  // $CASES-OMITTED$
                  default:
                    throw new UnreachableCodeException();
                }
              }
            }).intValue();
      }

      return 0;
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  @Override public int framebufferGetStencilBits()
  {
    final FakeFramebufferAttachableType da = this.depth_attachment;
    if (da != null) {
      if (da.isStencilRenderable()) {
        return da.getStencilBits();
      }
    }
    if (this.stencil_attachment != null) {
      return this.stencil_attachment.getStencilBits();
    }

    return 0;
  }

  List<FakeFramebufferAttachableType> getColorAttachments()
  {
    return this.color_attachments;
  }

  @Nullable FakeFramebufferAttachableType getDepthAttachment()
  {
    return this.depth_attachment;
  }

  @Nullable FakeFramebufferAttachableType getStencilAttachment()
  {
    return this.stencil_attachment;
  }

  void setColorRenderbuffer(
    final int i,
    final FakeRenderbuffer<RenderableColorKind> renderbuffer)
  {
    this.color_attachments.set(i, renderbuffer);
  }

  void setColorTexture(
    final int i,
    final FakeTexture2DStatic t)
  {
    this.color_attachments.set(i, t);
  }

  void setDepthRenderbuffer(
    final FakeRenderbuffer<RenderableDepthKind> fake_r)
  {
    this.depth_attachment = fake_r;
  }

  void setDepthStencilRenderbuffer(
    final FakeRenderbuffer<RenderableDepthStencilKind> fake_r)
  {
    this.depth_attachment = fake_r;
    this.stencil_attachment = fake_r;
  }

  void setDepthStencilTexture2D(
    final FakeTexture2DStatic fake_t)
  {
    this.depth_attachment = fake_t;
    this.stencil_attachment = fake_t;
  }

  void setDepthTexture2D(
    final FakeTexture2DStatic fake_t)
  {
    this.depth_attachment = fake_t;
  }

  void setStencilRenderbuffer(
    final FakeRenderbuffer<RenderableStencilKind> fake_r)
  {
    this.stencil_attachment = fake_r;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeFramebuffer ");
    builder.append(this.getGLName());
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
