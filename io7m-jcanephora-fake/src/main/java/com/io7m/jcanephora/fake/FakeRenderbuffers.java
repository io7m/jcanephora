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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.api.JCGLRenderbuffersGL2Type;
import com.io7m.jcanephora.api.JCGLRenderbuffersGL3Type;
import com.io7m.jcanephora.api.JCGLRenderbuffersGLES3Type;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeRenderbuffers implements
  JCGLRenderbuffersGL2Type,
  JCGLRenderbuffersGL3Type,
  JCGLRenderbuffersGLES3Type
{

  /**
   * Check that the given renderbuffer:
   *
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static
    <K extends RenderbufferKind, R extends RenderbufferUsableType<K>>
    R
    checkRenderbuffer(
      final FakeContext ctx,
      final R r)
      throws JCGLExceptionWrongContext,
        JCGLExceptionDeleted
  {
    NullCheck.notNull(r, "Renderbuffer");
    FakeCompatibilityChecks.checkRenderbuffer(ctx, r);
    ResourceCheck.notDeleted(r);
    return r;
  }

  private final FakeContext             context;
  private final LogType                 log;
  private int                           pool;
  private final FakeLogMessageCacheType tcache;

  FakeRenderbuffers(
    final FakeContext in_context,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.log = NullCheck.notNull(in_log, "Log").with("shaders");
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
    this.pool = 1;
  }

  private int freshID()
  {
    final int id = this.pool;
    ++this.pool;
    return id;
  }

  @Override public
    RenderbufferType<RenderableDepthKind>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableDepthKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_DEPTH_16,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableDepthKind>
    renderbufferAllocateDepth24(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableDepthKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_DEPTH_24,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableDepthStencilKind>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableDepthStencilKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_DEPTH_24_STENCIL_8,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGB565(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableColorKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_COLOR_RGB_565,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGB888(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableColorKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_COLOR_RGB_888,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA4444(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableColorKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_4444,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA5551(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableColorKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_5551,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA8888(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableColorKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_8888,
      this.freshID(),
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableStencilKind>
    renderbufferAllocateStencil8(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return new FakeRenderbuffer<RenderableStencilKind>(
      this.context,
      RenderbufferFormat.RENDERBUFFER_STENCIL_8,
      this.freshID(),
      width,
      height);
  }

  @Override public void renderbufferDelete(
    final RenderbufferType<?> buffer)
    throws JCGLException
  {
    FakeRenderbuffers.checkRenderbuffer(this.context, buffer);
    ((FakeObjectDeletable) buffer).resourceSetDeleted();
  }

}
