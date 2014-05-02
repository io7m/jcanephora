package com.io7m.jcanephora.jogl;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.JCGLExtensionNames;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLStateCache;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.api.JCGLExtensionPackedDepthStencil;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
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
      final @Nonnull JCGLNamedExtensionsType extensions,
      final @Nonnull JCGLVersion version,
      final @Nonnull Log log)
      throws ConstraintError
  {
    final String names[] =
      {
        JCGLExtensionNames.GL_OES_PACKED_DEPTH_STENCIL,
        JCGLExtensionNames.GL_EXT_PACKED_DEPTH_STENCIL, };

    for (final String name : names) {
      if (extensions.extensionIsVisible(name)) {
        return new Option.Some<JCGLExtensionPackedDepthStencil>(
          new ExtPackedDepthStencil<G>(g, state, version, log));
      }
    }

    return new Option.None<JCGLExtensionPackedDepthStencil>();
  }

  private final @Nonnull JCGLStateCache cache;
  private final @Nonnull GL             gl;
  private final @Nonnull Log            log;
  private final @Nonnull JCGLVersion    version;

  private ExtPackedDepthStencil(
    final @Nonnull GL gl1,
    final @Nonnull JCGLStateCache cache1,
    final @Nonnull JCGLVersion version1,
    final @Nonnull Log log1)
  {
    this.gl = gl1;
    this.cache = cache1;
    this.log = log1;
    this.version = version1;
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (this.version.getAPI()) {
      case JCGL_ES:
      {
        if (this.version.getVersionMajor() == 2) {
          JOGL_GLES2_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
            this.gl.getGL2ES2(),
            this.cache,
            this.log,
            framebuffer,
            renderbuffer);
          return;
        }
        break;
      }
      case JCGL_FULL:
      {
        break;
      }
    }

    JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.gl,
      this.cache,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableDepthStencil>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLRuntimeException
  {
    return JOGLRenderbuffer.unsafeBrandDepthStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.gl,
        this.cache,
        this.log,
        RenderbufferFormat.RENDERBUFFER_DEPTH_24_STENCIL_8,
        width,
        height));
  }
}
