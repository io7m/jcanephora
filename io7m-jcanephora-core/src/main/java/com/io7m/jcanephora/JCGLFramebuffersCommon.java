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

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the subset of framebuffer functionality available
 * on all OpenGL implementations.
 */

public interface JCGLFramebuffersCommon
{
  /**
   * Allocate a new framebuffer.
   * 
   * @return A freshly allocated framebuffer.
   */

  @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLException,
      ConstraintError;

  /**
   * Delete the framebuffer <code>framebuffer</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Return <code>true</code> iff any application-created draw framebuffer is
   * currently bound.
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawAnyIsBound()
    throws JCGLException;

  /**
   * Attach the given color renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * 
   * @see RenderbufferType#isColorRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>renderbuffer == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Attach the given color texture <code>texture</code> to the framebuffer
   * <code>framebuffer</code>.
   * 
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * 
   * @see TextureType#isColorRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>texture.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>texture</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError;

  /**
   * Attach the face <code>face</code> of the given color cube-map texture
   * <code>texture</code> to the framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * 
   * @see TextureType#isColorRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>face == null</code></li>
   *           <li><code>texture.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>texture</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws JCGLException,
      ConstraintError;

  /**
   * Attach the given depth renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing depth attachment.
   * 
   * @see RenderbufferType#isDepthRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>renderbuffer == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a depth-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Attach the given depth texture <code>texture</code> to the framebuffer
   * <code>framebuffer</code>.
   * 
   * The function will replace any existing depth attachment.
   * 
   * @see TextureType#isDepthRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>texture == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>texture.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>texture</code> is not a depth-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError;

  /**
   * Attach the given stencil renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing stencil attachment.
   * 
   * @see RenderbufferType#isStencilRenderable()
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>renderbuffer == null</code></li>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a stencil-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Bind the given framebuffer <code>framebuffer</code> to the draw target.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Return <code>true</code> iff <code>framebuffer</code> is currently bound
   * to the draw target.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Unbind the current framebuffer from the draw target.
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawUnbind()
    throws JCGLException;

  /**
   * Determine the validity of the framebuffer <code>framebuffer</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError;

  /**
   * Retrieve the available set of color attachment points for framebuffers.
   * 
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows single color attachments for framebuffers).
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetColorAttachmentPoints()
      throws JCGLException,
        ConstraintError;

  /**
   * Retrieve the available set of draw buffers framebuffers.
   * 
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows a single color draw buffer).
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull FramebufferDrawBuffer[] framebufferGetDrawBuffers()
    throws JCGLException,
      ConstraintError;
}
