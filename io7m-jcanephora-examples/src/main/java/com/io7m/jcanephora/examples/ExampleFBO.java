package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jtensors.VectorReadable2I;

public final class ExampleFBO implements Example
{
  private final GLInterfaceEmbedded gl;
  private final RenderbufferD24S8   depth_buffer;
  private final Texture2DRGBAStatic texture;
  private final Framebuffer         framebuffer;

  public ExampleFBO(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      GLException
  {
    this.gl = config.getGL();

    /**
     * Allocate a combined depth and stencil buffer, the same size as the
     * screen.
     */

    this.depth_buffer = this.gl.renderbufferD24S8Allocate(640, 480);

    /**
     * Allocate a texture to act as the color buffer for the framebuffer.
     */

    this.texture =
      this.gl.texture2DRGBAStaticAllocate(
        "color_buffer",
        640,
        480,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_LINEAR);

    /**
     * Allocate a framebuffer and attach the color buffer (texture) and
     * depth/stencil buffer.
     */

    this.framebuffer =
      this.gl.framebufferAllocate(new FramebufferAttachment[] {
        new ColorAttachment(this.texture, 0),
        new RenderbufferD24S8Attachment(this.depth_buffer) });

  }

  @Override public void display()
    throws GLException,
      GLCompileException,
      ConstraintError
  {
    this.gl.colorBufferClear3f(0.15f, 0.2f, 0.15f);
  }

  @Override public void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.gl.viewportSet(position, size);
  }

  @Override public void shutdown()
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.framebuffer.resourceDelete(this.gl);
    this.depth_buffer.resourceDelete(this.gl);
    this.texture.resourceDelete(this.gl);
  }
}
