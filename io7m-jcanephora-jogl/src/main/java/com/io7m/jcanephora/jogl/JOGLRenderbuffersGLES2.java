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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GL;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLRenderbuffersGLES2Type;
import com.io7m.jlog.LogUsableType;

final class JOGLRenderbuffersGLES2 extends JOGLRenderbuffersAbstract implements
  JCGLRenderbuffersGLES2Type
{
  JOGLRenderbuffersGLES2(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableDepthKind>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return (RenderbufferType<RenderableDepthKind>) this.renderbufferAllocate(
      RenderbufferFormat.RENDERBUFFER_DEPTH_16,
      width,
      height);
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGB565(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return (RenderbufferType<RenderableColorKind>) this.renderbufferAllocate(
      RenderbufferFormat.RENDERBUFFER_COLOR_RGB_565,
      width,
      height);
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA4444(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return (RenderbufferType<RenderableColorKind>) this.renderbufferAllocate(
      RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_4444,
      width,
      height);
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA5551(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return (RenderbufferType<RenderableColorKind>) this.renderbufferAllocate(
      RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_5551,
      width,
      height);
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableStencilKind>
    renderbufferAllocateStencil8(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return (RenderbufferType<RenderableStencilKind>) this
      .renderbufferAllocate(
        RenderbufferFormat.RENDERBUFFER_STENCIL_8,
        width,
        height);
  }
}
