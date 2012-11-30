package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Specification of the attachment of buffer storage to the framebuffer.
 */

public class FramebufferAttachment
{
  /**
   * Framebuffer color buffer attachment. When passed to the framebuffer API,
   * this attachment will attach <code>texture</code> as the color buffer at
   * index <code>index</code>.
   */

  public static final class ColorAttachment extends FramebufferAttachment
  {
    private final @Nonnull Texture2DStatic texture;
    private final int                          index;

    @SuppressWarnings("synthetic-access") public ColorAttachment(
      final @Nonnull Texture2DStatic texture,
      final int index)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_COLOR);
      this.texture = Constraints.constrainNotNull(texture, "Texture");
      this.index =
        Constraints.constrainRange(
          index,
          0,
          Integer.MAX_VALUE,
          "Attachment index");
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
      final ColorAttachment other = (ColorAttachment) obj;
      if (this.index != other.index) {
        return false;
      }
      if (!this.texture.equals(other.texture)) {
        return false;
      }
      return true;
    }

    /**
     * Retrieve the index at which this attachment is attached to the
     * framebuffer.
     */

    public int getIndex()
    {
      return this.index;
    }

    /**
     * Retrieve the texture that backs the color buffer.
     */

    public @Nonnull Texture2DStatic getTexture()
    {
      return this.texture;
    }

    @Override public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + this.index;
      result = (prime * result) + this.texture.hashCode();
      return result;
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[FramebufferColorAttachment ");
      builder.append(this.texture);
      builder.append(" ");
      builder.append(this.index);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * Framebuffer depth and stencil buffer attachment. When passed to the
   * framebuffer API, this attachment will attach <code>buffer</code> as the
   * combined depth/stencil buffer.
   */

  public static final class RenderbufferD24S8Attachment extends
    FramebufferAttachment
  {
    private final @Nonnull RenderbufferD24S8 buffer;

    @SuppressWarnings("synthetic-access") public RenderbufferD24S8Attachment(
      final @Nonnull RenderbufferD24S8 buffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_D24S8);
      this.buffer = Constraints.constrainNotNull(buffer, "Renderbuffer");
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
      final RenderbufferD24S8Attachment other =
        (RenderbufferD24S8Attachment) obj;
      if (!this.buffer.equals(other.buffer)) {
        return false;
      }
      return true;
    }

    /**
     * Retrieve the renderbuffer for the attachment.
     */

    public @Nonnull RenderbufferD24S8 getRenderbuffer()
    {
      return this.buffer;
    }

    @Override public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + this.buffer.hashCode();
      return result;
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[RenderbufferD24S8Attachment ");
      builder.append(this.buffer);
      builder.append("]");
      return builder.toString();
    }
  }

  static enum Type
  {
    ATTACHMENT_COLOR,
    ATTACHMENT_D24S8
  }

  public final @Nonnull Type type;

  private FramebufferAttachment(
    final @Nonnull Type type)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Attachment type");
  }
}
