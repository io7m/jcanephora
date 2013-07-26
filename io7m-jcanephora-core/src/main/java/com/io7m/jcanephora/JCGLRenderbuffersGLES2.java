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
 * Type-safe interface to the renderbuffer API exposed by OpenGL ES2.
 */

public interface JCGLRenderbuffersGLES2 extends JCGLRenderbuffersCommon
{
  /**
   * Allocate a depth renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_DEPTH_16} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer<RenderableDepth> renderbufferAllocateDepth16(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_COLOR_RGB_565} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer<RenderableColor> renderbufferAllocateRGB565(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_COLOR_RGBA_4444} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer<RenderableColor> renderbufferAllocateRGBA4444(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_COLOR_RGBA_5551} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer<RenderableColor> renderbufferAllocateRGBA5551(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException;

  /**
   * Allocate a stencil renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_STENCIL_8} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer<RenderableStencil> renderbufferAllocateStencil8(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException;
}
