package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the subset of framebuffer functionality available
 * on OpenGL 3.0 implementations.
 */

public interface GLFramebuffers extends GLFramebuffersES2
{
  /**
   * Attach the given depth/stencil renderbuffer <code>renderbuffer</code> to
   * the framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing depth/stencil attachment.
   * 
   * @see RenderbufferType#isDepthRenderable()
   * @see RenderbufferType#isStencilRenderable()
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>renderbuffer == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a depth-renderable and
   *           stencil-renderable format</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferReadable renderbuffer)
    throws GLException,
      ConstraintError;
}
