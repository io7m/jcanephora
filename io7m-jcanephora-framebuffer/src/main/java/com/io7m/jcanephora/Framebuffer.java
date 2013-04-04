/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jcanephora;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilRenderbuffer;

/**
 * A created framebuffer, with all associated textures and/or renderbuffers.
 */

public final class Framebuffer extends GLResourceDeleteable implements
  FramebufferUsable
{
  private final @Nonnull FramebufferReference                                  framebuffer;
  private final int                                                            width;
  private final int                                                            height;

  private @Nonnull final Map<FramebufferColorAttachmentPoint, AttachmentColor> color_attachments;
  private @CheckForNull AttachmentDepth                                        depth_attachment;
  private @CheckForNull AttachmentStencil                                      stencil_attachment;

  Framebuffer(
    final @Nonnull FramebufferReference framebuffer,
    final int width,
    final int height)
  {
    this.framebuffer = framebuffer;
    this.width = width;
    this.height = height;
    this.color_attachments =
      new HashMap<FramebufferColorAttachmentPoint, AttachmentColor>();
    this.depth_attachment = null;
    this.stencil_attachment = null;
  }

  void configAddColorAttachment(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull AttachmentColor attachment)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(attachment, "Color attachment");

    Constraints.constrainArbitrary(
      this.color_attachments.containsKey(point) == false,
      "No color attachment at " + point);

    this.color_attachments.put(point, attachment);
  }

  void configSetDepthAttachment(
    final @Nonnull AttachmentDepth attachment)
    throws ConstraintError
  {
    Constraints.constrainNotNull(attachment, "Depth attachment");
    Constraints.constrainArbitrary(
      this.depth_attachment == null,
      "No depth attachment");

    this.depth_attachment = attachment;
  }

  void configSetStencilAttachment(
    final @Nonnull AttachmentStencil attachment)
    throws ConstraintError
  {
    Constraints.constrainNotNull(attachment, "Stencil attachment");
    Constraints.constrainArbitrary(
      this.stencil_attachment == null,
      "No stencil attachment");

    this.stencil_attachment = attachment;
  }

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Framebuffer other = (Framebuffer) obj;
    if (!this.framebuffer.equals(other.framebuffer)) {
      return false;
    }
    return true;
  }

  @Override public @Nonnull AttachmentColor getColorAttachment(
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasColorAttachment(point),
      "Has color attachment at " + point);

    return this.color_attachments.get(point);
  }

  @Override public @Nonnull AttachmentDepth getDepthAttachment()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasDepthAttachment(),
      "Has depth attachment");
    return this.depth_attachment;
  }

  public @Nonnull FramebufferReference getFramebuffer()
  {
    return this.framebuffer;
  }

  @Override public int getHeight()
  {
    return this.height;
  }

  @Override public @Nonnull AttachmentStencil getStencilAttachment()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasStencilAttachment(),
      "Has stencil attachment");
    return this.stencil_attachment;
  }

  @Override public int getWidth()
  {
    return this.width;
  }

  @Override public boolean hasColorAttachment(
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    return this.color_attachments.containsKey(point);
  }

  @Override public boolean hasDepthAttachment()
  {
    return this.depth_attachment != null;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.framebuffer.hashCode();
    return result;
  }

  @Override public boolean hasStencilAttachment()
  {
    return this.stencil_attachment != null;
  }

  public void delete(
    final @Nonnull GLRenderbuffersCommon rb,
    final @Nonnull GLTextures2DStaticCommon t2d,
    final @Nonnull GLTexturesCubeStaticCommon tc)
    throws ConstraintError,
      GLException
  {
    for (final AttachmentColor c : this.color_attachments.values()) {
      switch (c.type) {
        case ATTACHMENT_COLOR_RENDERBUFFER:
        {
          final AttachmentColorRenderbuffer ar =
            (AttachmentColorRenderbuffer) c;
          rb.renderbufferDelete(ar.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_2D:
        {
          final AttachmentColorTexture2DStatic at =
            (AttachmentColorTexture2DStatic) c;
          t2d.texture2DStaticDelete(at.getTextureWritable());
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        {
          final AttachmentColorTextureCubeStatic at =
            (AttachmentColorTextureCubeStatic) c;
          tc.textureCubeStaticDelete(at.getTextureWritable());
          break;
        }
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          break;
        }
      }
    }

    if (this.depth_attachment != null) {
      switch (this.depth_attachment.type) {
        case ATTACHMENT_DEPTH_RENDERBUFFER:
        {
          final AttachmentDepthRenderbuffer a =
            (AttachmentDepthRenderbuffer) this.depth_attachment;
          rb.renderbufferDelete(a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
        {
          final AttachmentDepthStencilRenderbuffer a =
            (AttachmentDepthStencilRenderbuffer) this.depth_attachment;
          rb.renderbufferDelete(a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
        case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
        {
          break;
        }
      }
    }

    if (this.stencil_attachment != null) {
      switch (this.stencil_attachment.type) {
        case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
        case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
        {
          break;
        }
        case ATTACHMENT_STENCIL_RENDERBUFFER:
        {
          final AttachmentStencilRenderbuffer a =
            (AttachmentStencilRenderbuffer) this.stencil_attachment;
          rb.renderbufferDelete(a.getRenderbufferWritable());
          break;
        }
      }
    }
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Framebuffer  ");
    builder.append(this.framebuffer);
    builder.append(" ");
    builder.append(this.width);
    builder.append(" x ");
    builder.append(this.height);
    builder.append("]");
    return builder.toString();
  }
}
