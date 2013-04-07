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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A stencil attachment for a framebuffer.
 */

public abstract class AttachmentStencil
{
  /**
   * ATTACHMENT_SHARED_STENCIL_RENDERBUFFER
   */

  public static final class AttachmentSharedStencilRenderbuffer extends
    AttachmentStencil implements AttachmentStencilRenderbufferUsable
  {
    private final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer;

    public AttachmentSharedStencilRenderbuffer(
      final @Nonnull AttachmentSharedStencilRenderbuffer a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_STENCIL_RENDERBUFFER);
      Constraints.constrainNotNull(a, "Attachment");
      this.renderbuffer = a.getRenderbuffer();
    }

    public AttachmentSharedStencilRenderbuffer(
      final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_STENCIL_RENDERBUFFER);
      this.renderbuffer =
        Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
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
      final AttachmentSharedStencilRenderbuffer other =
        (AttachmentSharedStencilRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableStencil>
      getRenderbuffer()
    {
      return this.renderbuffer;
    }

    @Override public int hashCode()
    {
      return this.renderbuffer.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentSharedStencilRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * ATTACHMENT_STENCIL_AS_DEPTH_STENCIL
   */

  public static final class AttachmentStencilAsDepthStencil extends
    AttachmentStencil
  {
    public AttachmentStencilAsDepthStencil()
    {
      super(Type.ATTACHMENT_STENCIL_AS_DEPTH_STENCIL);
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
      return true;
    }

    @Override public int hashCode()
    {
      return 137;
    }

    @Override public String toString()
    {
      return "[AttachmentStencilAsDepthStencil]";
    }
  }

  /**
   * ATTACHMENT_STENCIL_RENDERBUFFER
   */

  public static final class AttachmentStencilRenderbuffer extends
    AttachmentStencil implements AttachmentStencilRenderbufferUsable
  {
    private final @Nonnull Renderbuffer<RenderableStencil> renderbuffer;

    public AttachmentStencilRenderbuffer(
      final @Nonnull Renderbuffer<RenderableStencil> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_STENCIL_RENDERBUFFER);
      this.renderbuffer =
        Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
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
      final AttachmentStencilRenderbuffer other =
        (AttachmentStencilRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableStencil>
      getRenderbuffer()
    {
      return this.renderbuffer;
    }

    @Nonnull Renderbuffer<RenderableStencil> getRenderbufferWritable()
    {
      return this.renderbuffer;
    }

    @Override public int hashCode()
    {
      return this.renderbuffer.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentStencilRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  interface AttachmentStencilRenderbufferUsable
  {
    public @Nonnull RenderbufferUsable<RenderableStencil> getRenderbuffer();
  }

  public static enum Type
  {
    ATTACHMENT_STENCIL_RENDERBUFFER,
    ATTACHMENT_SHARED_STENCIL_RENDERBUFFER,
    ATTACHMENT_STENCIL_AS_DEPTH_STENCIL
  }

  public final @Nonnull Type type;

  AttachmentStencil(
    final @Nonnull Type type)
  {
    this.type = type;
  }
}