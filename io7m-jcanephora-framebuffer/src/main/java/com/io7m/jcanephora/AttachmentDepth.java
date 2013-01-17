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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A depth attachment for a framebuffer.
 */

public abstract class AttachmentDepth
{
  /**
   * ATTACHMENT_DEPTH_RENDERBUFFER
   */

  public static final class AttachmentDepthRenderbuffer extends
    AttachmentDepth implements AttachmentDepthRenderbufferUsable
  {
    private final @Nonnull Renderbuffer<RenderableDepth> renderbuffer;

    public AttachmentDepthRenderbuffer(
      final @Nonnull Renderbuffer<RenderableDepth> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_DEPTH_RENDERBUFFER);
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
      final AttachmentDepthRenderbuffer other =
        (AttachmentDepthRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableDepth>
      getRenderbuffer()
    {
      return this.renderbuffer;
    }

    @Nonnull Renderbuffer<RenderableDepth> getRenderbufferWritable()
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
      builder.append("[AttachmentDepthRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  interface AttachmentDepthRenderbufferUsable
  {
    public @Nonnull RenderbufferUsable<?> getRenderbuffer();
  }

  /**
   * ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER
   */

  public static final class AttachmentDepthStencilRenderbuffer extends
    AttachmentDepth implements AttachmentDepthRenderbufferUsable
  {
    private final @Nonnull Renderbuffer<RenderableDepthStencil> renderbuffer;

    public AttachmentDepthStencilRenderbuffer(
      final @Nonnull Renderbuffer<RenderableDepthStencil> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER);
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
      final AttachmentDepthStencilRenderbuffer other =
        (AttachmentDepthStencilRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableDepthStencil>
      getRenderbuffer()
    {
      return this.renderbuffer;
    }

    @Nonnull Renderbuffer<RenderableDepthStencil> getRenderbufferWritable()
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
      builder.append("[AttachmentDepthStencilRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * ATTACHMENT_SHARED_DEPTH_RENDERBUFFER
   */

  public static final class AttachmentSharedDepthRenderbuffer extends
    AttachmentDepth implements AttachmentDepthRenderbufferUsable
  {
    private final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer;

    public AttachmentSharedDepthRenderbuffer(
      final AttachmentSharedDepthRenderbuffer a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_DEPTH_RENDERBUFFER);
      Constraints.constrainNotNull(a, "Attachment");
      this.renderbuffer = a.getRenderbuffer();
    }

    public AttachmentSharedDepthRenderbuffer(
      final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_DEPTH_RENDERBUFFER);
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
      final AttachmentSharedDepthRenderbuffer other =
        (AttachmentSharedDepthRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableDepth>
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
      builder.append("[AttachmentSharedDepthRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER
   */

  public static final class AttachmentSharedDepthStencilRenderbuffer extends
    AttachmentDepth implements AttachmentDepthRenderbufferUsable
  {
    private final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer;

    public AttachmentSharedDepthStencilRenderbuffer(
      final @Nonnull AttachmentSharedDepthStencilRenderbuffer a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_DEPTH_RENDERBUFFER);
      Constraints.constrainNotNull(a, "Attachment");
      this.renderbuffer = a.getRenderbuffer();
    }

    public AttachmentSharedDepthStencilRenderbuffer(
      final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER);
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
      final AttachmentSharedDepthStencilRenderbuffer other =
        (AttachmentSharedDepthStencilRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableDepthStencil>
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
      builder.append("[AttachmentSharedDepthStencilRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  public static enum Type
  {
    ATTACHMENT_DEPTH_RENDERBUFFER,
    ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER,
    ATTACHMENT_SHARED_DEPTH_RENDERBUFFER,
    ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER,
  }

  public final @Nonnull Type type;

  AttachmentDepth(
    final @Nonnull Type type)
  {
    this.type = type;
  }

}
