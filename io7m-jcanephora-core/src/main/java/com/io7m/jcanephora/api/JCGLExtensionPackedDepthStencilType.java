/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;

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

public interface JCGLExtensionPackedDepthStencilType extends
  JCGLExtensionType
{
  /**
   * Attach the given depth/stencil renderbuffer <code>renderbuffer</code> to
   * the framebuffer <code>framebuffer</code>.
   * 
   * The function will replace any existing depth/stencil attachment.
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isDepthRenderable()
   * @see com.io7m.jcanephora.RenderbufferFormat#isStencilRenderable()
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLException;

  /**
   * Allocate a packed depth/stencil renderbuffer.
   * 
   * See
   * {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_DEPTH_24_STENCIL_8}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

    RenderbufferType<RenderableDepthStencilKind>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws JCGLException;
}
