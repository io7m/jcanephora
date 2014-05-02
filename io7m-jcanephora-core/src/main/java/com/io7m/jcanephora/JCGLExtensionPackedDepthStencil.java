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
import com.io7m.jcanephora.jogl.FramebufferReference;
import com.io7m.jcanephora.jogl.Renderbuffer;

/**
 * <p>
 * The interface to packed depth/stencil buffers. Provided by
 * <code>GL_OES_packed_depth_stencil</code> or
 * <code>GL_EXT_packed_depth_stencil</code>.
 * </p>
 * <p>
 * This is only useful for ES2 and 2.1, as the extension is included in 3.0.
 * </p>
 */

public interface JCGLExtensionPackedDepthStencil extends JCGLExtensionType
{
  /**
   * Attach the given depth/stencil renderbuffer <code>renderbuffer</code> to
   * the framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing depth/stencil attachment.
   * 
   * @see RenderbufferFormat#isDepthRenderable()
   * @see RenderbufferFormat#isStencilRenderable()
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
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthStencilRenderbuffer(
    final  FramebufferReference framebuffer,
    final  RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * Allocate a packed depth/stencil renderbuffer.
   * 
   * See {@link RenderbufferFormat#RENDERBUFFER_DEPTH_24_STENCIL_8} for the
   * precise format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           <li>
   *           <code>renderbufferSupportsDepth24Stencil8() == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  
    Renderbuffer<RenderableDepthStencilKind>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLRuntimeException;
}
