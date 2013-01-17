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
 * A color attachment for a framebuffer.
 */

public abstract class AttachmentColor
{
  /**
   * ATTACHMENT_COLOR_RENDERBUFFER
   */

  public static final class AttachmentColorRenderbuffer extends
    AttachmentColor implements AttachmentColorRenderbufferUsable
  {
    private final @Nonnull Renderbuffer<RenderableColor> renderbuffer;

    public AttachmentColorRenderbuffer(
      final @Nonnull Renderbuffer<RenderableColor> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_COLOR_RENDERBUFFER);
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
      final AttachmentColorRenderbuffer other =
        (AttachmentColorRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableColor>
      getRenderbuffer()
    {
      return this.renderbuffer;
    }

    @Nonnull Renderbuffer<RenderableColor> getRenderbufferWritable()
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
      builder.append("[AttachmentColorRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  interface AttachmentColorRenderbufferUsable
  {
    public @Nonnull RenderbufferUsable<RenderableColor> getRenderbuffer();
  }

  /**
   * ATTACHMENT_COLOR_TEXTURE_2D
   */

  public static final class AttachmentColorTexture2DStatic extends
    AttachmentColor implements AttachmentColorTexture2DUsable
  {
    private final @Nonnull Texture2DStatic texture;

    public AttachmentColorTexture2DStatic(
      final @Nonnull Texture2DStatic texture)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_COLOR_TEXTURE_2D);
      this.texture = Constraints.constrainNotNull(texture, "Texture2DStatic");
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
      final AttachmentColorTexture2DStatic other =
        (AttachmentColorTexture2DStatic) obj;
      if (!this.texture.equals(other.texture)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull Texture2DStaticUsable getTexture2D()
    {
      return this.texture;
    }

    @Nonnull Texture2DStatic getTextureWritable()
    {
      return this.texture;
    }

    @Override public int hashCode()
    {
      return this.texture.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentColorTexture2DStatic ");
      builder.append(this.texture);
      builder.append("]");
      return builder.toString();
    }
  }

  interface AttachmentColorTexture2DUsable
  {
    public @Nonnull Texture2DStaticUsable getTexture2D();
  }

  /**
   * ATTACHMENT_COLOR_TEXTURE_CUBE
   */

  public static final class AttachmentColorTextureCubeStatic extends
    AttachmentColor implements AttachmentColorTextureCubeUsable
  {
    private final @Nonnull TextureCubeStatic texture;
    private final @Nonnull CubeMapFace       face;

    public AttachmentColorTextureCubeStatic(
      final @Nonnull TextureCubeStatic texture,
      final @Nonnull CubeMapFace face)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_COLOR_TEXTURE_CUBE);
      this.texture =
        Constraints.constrainNotNull(texture, "TextureCubeStatic");
      this.face = Constraints.constrainNotNull(face, "Face");
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
      final AttachmentColorTextureCubeStatic other =
        (AttachmentColorTextureCubeStatic) obj;
      if (!this.texture.equals(other.texture)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull CubeMapFace getFace()
    {
      return this.face;
    }

    @Override public @Nonnull TextureCubeStaticUsable getTextureCube()
    {
      return this.texture;
    }

    @Nonnull TextureCubeStatic getTextureWritable()
    {
      return this.texture;
    }

    @Override public int hashCode()
    {
      return this.texture.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentColorTextureCubeStatic ");
      builder.append(this.texture);
      builder.append(" ");
      builder.append(this.face);
      builder.append("]");
      return builder.toString();
    }
  }

  interface AttachmentColorTextureCubeUsable
  {
    public @Nonnull CubeMapFace getFace();

    public @Nonnull TextureCubeStaticUsable getTextureCube();
  }

  /**
   * ATTACHMENT_SHARED_COLOR_RENDERBUFFER
   */

  public static final class AttachmentSharedColorRenderbuffer extends
    AttachmentColor implements AttachmentColorRenderbufferUsable
  {
    private final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer;

    public AttachmentSharedColorRenderbuffer(
      final @Nonnull AttachmentSharedColorRenderbuffer a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_RENDERBUFFER);
      this.renderbuffer =
        Constraints.constrainNotNull(a.getRenderbuffer(), "Renderbuffer");
    }

    public AttachmentSharedColorRenderbuffer(
      final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_RENDERBUFFER);
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
      final AttachmentSharedColorRenderbuffer other =
        (AttachmentSharedColorRenderbuffer) obj;
      if (!this.renderbuffer.equals(other.renderbuffer)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull
      RenderbufferUsable<RenderableColor>
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
      builder.append("[AttachmentSharedColorRenderbuffer ");
      builder.append(this.renderbuffer);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * ATTACHMENT_SHARED_COLOR_TEXTURE_2D
   */

  public static final class AttachmentSharedColorTexture2DStatic extends
    AttachmentColor implements AttachmentColorTexture2DUsable
  {
    private final @Nonnull Texture2DStaticUsable texture;

    public AttachmentSharedColorTexture2DStatic(
      final @Nonnull AttachmentSharedColorTexture2DStatic a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_TEXTURE_2D);
      this.texture =
        Constraints.constrainNotNull(a.getTexture2D(), "Texture2DStatic");
    }

    public AttachmentSharedColorTexture2DStatic(
      final @Nonnull Texture2DStaticUsable texture)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_TEXTURE_2D);
      this.texture = Constraints.constrainNotNull(texture, "Texture2DStatic");
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
      final AttachmentSharedColorTexture2DStatic other =
        (AttachmentSharedColorTexture2DStatic) obj;
      if (!this.texture.equals(other.texture)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull Texture2DStaticUsable getTexture2D()
    {
      return this.texture;
    }

    @Override public int hashCode()
    {
      return this.texture.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentSharedColorTexture2DStatic ");
      builder.append(this.texture);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE
   */

  public static final class AttachmentSharedColorTextureCubeStatic extends
    AttachmentColor implements AttachmentColorTextureCubeUsable
  {
    private final @Nonnull TextureCubeStaticUsable texture;
    private final @Nonnull CubeMapFace             face;

    public AttachmentSharedColorTextureCubeStatic(
      final @Nonnull AttachmentSharedColorTextureCubeStatic a)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE);
      this.texture =
        Constraints.constrainNotNull(a.getTextureCube(), "TextureCubeStatic");
      this.face = Constraints.constrainNotNull(a.getFace(), "Face");
    }

    public AttachmentSharedColorTextureCubeStatic(
      final @Nonnull TextureCubeStaticUsable texture,
      final @Nonnull CubeMapFace face)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE);
      this.texture =
        Constraints.constrainNotNull(texture, "TextureCubeStatic");
      this.face = Constraints.constrainNotNull(face, "Face");
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
      final AttachmentSharedColorTextureCubeStatic other =
        (AttachmentSharedColorTextureCubeStatic) obj;
      if (!this.texture.equals(other.texture)) {
        return false;
      }
      return true;
    }

    @Override public @Nonnull CubeMapFace getFace()
    {
      return this.face;
    }

    @Override public @Nonnull TextureCubeStaticUsable getTextureCube()
    {
      return this.texture;
    }

    @Override public int hashCode()
    {
      return this.texture.hashCode();
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[AttachmentSharedColorTextureCubeStatic ");
      builder.append(this.texture);
      builder.append(" ");
      builder.append(this.face);
      builder.append("]");
      return builder.toString();
    }
  }

  public static enum Type
  {
    ATTACHMENT_COLOR_RENDERBUFFER,
    ATTACHMENT_COLOR_TEXTURE_2D,
    ATTACHMENT_COLOR_TEXTURE_CUBE,
    ATTACHMENT_SHARED_COLOR_RENDERBUFFER,
    ATTACHMENT_SHARED_COLOR_TEXTURE_2D,
    ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE
  }

  public final @Nonnull Type type;

  AttachmentColor(
    final @Nonnull Type type)
  {
    this.type = type;
  }
}
