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

package com.io7m.jcanephora.api;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.jogl.Renderbuffer;

/**
 * Type-safe interface to the renderbuffer API exposed by both OpenGL 3.* and
 * ES 3.*.
 */

public interface JCGLRenderbuffersGL3ES3 extends JCGLRenderbuffersCommon
{
  /**
   * Allocate a depth renderbuffer.
   * 
   * See {@link RenderbufferFormat#RENDERBUFFER_DEPTH_16} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

   Renderbuffer<RenderableDepthKind> renderbufferAllocateDepth16(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Allocate a depth renderbuffer.
   * 
   * See {@link RenderbufferFormat#RENDERBUFFER_DEPTH_24} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

   Renderbuffer<RenderableDepthKind> renderbufferAllocateDepth24(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException;

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

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferFormat#RENDERBUFFER_COLOR_RGB_888} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>renderbufferSupportsRGB888() == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

   Renderbuffer<RenderableColorKind> renderbufferAllocateRGB888(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferFormat#RENDERBUFFER_COLOR_RGBA_8888} for the precise
   * format.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>renderbufferSupportsRGBA8888() == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

   Renderbuffer<RenderableColorKind> renderbufferAllocateRGBA8888(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException;
}
