package com.io7m.jcanephora;

import java.util.StringTokenizer;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;

/**
 * The packed depth/stencil extension.
 */

class ExtPackedDepthStencil implements JCGLExtensionPackedDepthStencil
{
  public static Option<JCGLExtensionPackedDepthStencil> create(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log)
  {
    final String names[] =
      { "GL_OES_packed_depth_stencil", "GL_EXT_packed_depth_stencil", };

    final String all = GL11.glGetString(GL11.GL_EXTENSIONS);
    final StringTokenizer tok = new StringTokenizer(all);

    while (tok.hasMoreTokens()) {
      final String extension = tok.nextToken();
      for (final String name : names) {
        if (extension.equals(name)) {
          return new Option.Some<JCGLExtensionPackedDepthStencil>(
            new ExtPackedDepthStencil(state, log));
        }
      }
    }

    return new Option.None<JCGLExtensionPackedDepthStencil>();
  }

  private final @Nonnull JCGLStateCache cache;
  private final @Nonnull Log            log;

  private ExtPackedDepthStencil(
    final @Nonnull JCGLStateCache cache,
    final @Nonnull Log log)
  {
    this.cache = cache;
    this.log = log;
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GL3Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.cache,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepthStencil>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandDepthStencil(LWJGL_GLES2Functions
      .renderbufferAllocate(
        this.cache,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        width,
        height));
  }
}
