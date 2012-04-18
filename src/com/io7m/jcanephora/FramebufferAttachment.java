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

  public static final class FramebufferColorAttachment extends
    FramebufferAttachment
  {
    private final @Nonnull Texture2DRGBAStatic texture;
    private final int                          index;

    @SuppressWarnings("synthetic-access") public FramebufferColorAttachment(
      final @Nonnull Texture2DRGBAStatic texture,
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

    public int getIndex()
    {
      return this.index;
    }

    public @Nonnull Texture2DRGBAStatic getTexture()
    {
      return this.texture;
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
   * Framebuffer depth buffer attachment. When passed to the framebuffer API,
   * this attachment will attach <code>buffer</code> as the depth buffer.
   */

  public static final class FramebufferDepthAttachment extends
    FramebufferAttachment
  {
    private final @Nonnull RenderbufferDepth buffer;

    @SuppressWarnings("synthetic-access") public FramebufferDepthAttachment(
      final @Nonnull RenderbufferDepth buffer)
      throws ConstraintError
    {
      super(Type.ATTACHMENT_DEPTH);
      this.buffer = Constraints.constrainNotNull(buffer, "Renderbuffer");
    }

    public @Nonnull RenderbufferDepth getRenderbuffer()
    {
      return this.buffer;
    }

    @Override public String toString()
    {
      final StringBuilder builder = new StringBuilder();
      builder.append("[FramebufferDepthAttachment ");
      builder.append(this.buffer);
      builder.append("]");
      return builder.toString();
    }
  }

  static enum Type
  {
    ATTACHMENT_COLOR,
    ATTACHMENT_DEPTH
  }

  public final @Nonnull Type type;

  private FramebufferAttachment(
    final @Nonnull Type type)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Attachment type");
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FramebufferAttachment ");
    builder.append(this.type);
    builder.append("]");
    return builder.toString();
  }
}
