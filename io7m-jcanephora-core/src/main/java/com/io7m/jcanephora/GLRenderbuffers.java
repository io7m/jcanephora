/*
 * Copyright © 2012 http://io7m.com
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
 * Type-safe interface to the renderbuffer API exposed by OpenGL 3.0 and ES2.
 * 
 * Note that the framebuffer API exposed by 3.0 and ES2 are mutually
 * incompatible in various ways, so this interface
 */

public interface GLRenderbuffers extends GLRenderbuffersES2
{
  /**
   * Allocate a packed depth/stencil renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_DEPTH_24_STENCIL_8} for the
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
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer renderbufferAllocateDepth24Stencil8(
    final int width,
    final int height)
    throws ConstraintError,
      GLException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_COLOR_RGB_888} for the precise
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
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer renderbufferAllocateRGB888(
    final int width,
    final int height)
    throws ConstraintError,
      GLException;

  /**
   * Allocate a color renderbuffer.
   * 
   * See {@link RenderbufferType#RENDERBUFFER_COLOR_RGBA_8888} for the precise
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
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Renderbuffer renderbufferAllocateRGBA8888(
    final int width,
    final int height)
    throws ConstraintError,
      GLException;
}
