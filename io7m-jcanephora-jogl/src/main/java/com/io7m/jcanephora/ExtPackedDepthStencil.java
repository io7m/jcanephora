package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;

/**
 * The packed depth/stencil extension.
 */

class ExtPackedDepthStencil<G extends GL> implements
  JCGLExtensionPackedDepthStencil
{
  public static
    <G extends GL>
    Option<JCGLExtensionPackedDepthStencil>
    create(
      final @Nonnull G g,
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log)
  {
    final GLContext ctx = g.getContext();

    final String names[] =
      { "GL_OES_packed_depth_stencil", "GL_EXT_packed_depth_stencil", };

    for (final String name : names) {

      if (ctx.isExtensionAvailable(name)) {
        log.debug("Extension " + name + " is available");
        return new Option.Some<JCGLExtensionPackedDepthStencil>(
          new ExtPackedDepthStencil<G>(g, state, log));
      }
      log.debug("Extension " + name + " is not available");
    }

    return new Option.None<JCGLExtensionPackedDepthStencil>();
  }

  private final @Nonnull JCGLStateCache cache;
  private final @Nonnull GL             gl;
  private final @Nonnull Log            log;

  private ExtPackedDepthStencil(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache cache,
    final @Nonnull Log log)
  {
    this.gl = gl;
    this.cache = cache;
    this.log = log;
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.gl,
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
    return Renderbuffer.unsafeBrandDepthStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.gl,
        this.cache,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        width,
        height));
  }
}
