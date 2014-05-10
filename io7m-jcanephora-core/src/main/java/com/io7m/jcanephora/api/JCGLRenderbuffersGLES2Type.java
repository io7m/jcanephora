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

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferType;

/**
 * Type-safe interface to the renderbuffer API exposed by OpenGL ES2.
 */

public interface JCGLRenderbuffersGLES2Type extends
  JCGLRenderbuffersCommonType
{
  /**
   * Allocate a depth renderbuffer.
   * 
   * See {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_DEPTH_16}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  RenderbufferType<RenderableDepthKind> renderbufferAllocateDepth16(
    final int width,
    final int height)
    throws JCGLExceptionRuntime;

  /**
   * Allocate a color renderbuffer.
   * 
   * See
   * {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_COLOR_RGB_565}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  RenderbufferType<RenderableColorKind> renderbufferAllocateRGB565(
    final int width,
    final int height)
    throws JCGLExceptionRuntime;

  /**
   * Allocate a color renderbuffer.
   * 
   * See
   * {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_COLOR_RGBA_4444}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  RenderbufferType<RenderableColorKind> renderbufferAllocateRGBA4444(
    final int width,
    final int height)
    throws JCGLExceptionRuntime;

  /**
   * Allocate a color renderbuffer.
   * 
   * See
   * {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_COLOR_RGBA_5551}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  RenderbufferType<RenderableColorKind> renderbufferAllocateRGBA5551(
    final int width,
    final int height)
    throws JCGLExceptionRuntime;

  /**
   * Allocate a stencil renderbuffer.
   * 
   * See {@link com.io7m.jcanephora.RenderbufferFormat#RENDERBUFFER_STENCIL_8}
   * for the precise format.
   * 
   * @param width
   *          The width.
   * @param height
   *          The height.
   * @return A freshly allocated renderbuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  RenderbufferType<RenderableStencilKind> renderbufferAllocateStencil8(
    final int width,
    final int height)
    throws JCGLExceptionRuntime;
}
