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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionFormatError;
import com.io7m.jcanephora.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.JCGLExceptionParameterError;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnimplementedCodeException;

/**
 * The framebuffer API implementation.
 */

public final class FakeFramebuffers implements JCGLFramebuffersGL3Type
{
  /**
   * Check that the given framebuffer:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (framebuffers are not shared)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static void checkFramebuffer(
    final FakeContext ctx,
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(framebuffer, "Framebuffer");
    FakeCompatibilityChecks.checkFramebuffer(ctx, framebuffer);
    ResourceCheck.notDeleted(framebuffer);
  }

  private final FakeColorAttachmentPointsType color_points;
  private final FakeContext                   context;
  private @Nullable FakeFramebuffer           draw_bind;
  private final FakeDrawBuffers               draw_buffers;
  private final JCGLNamedExtensionsType       extensions;
  private final LogType                       log;
  private final FakeMeta                      meta;
  private int                                 pool;
  private @Nullable FakeFramebuffer           read_bind;
  private final FakeRenderbuffers             renderbuffers;
  private final FakeLogMessageCacheType       tcache;
  private final FakeTextures2DStatic          textures_2d;

  FakeFramebuffers(
    final FakeContext in_context,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache,
    final FakeColorAttachmentPointsType in_color_points,
    final FakeDrawBuffers in_draw_buffers,
    final FakeMeta in_meta,
    final FakeRenderbuffers in_renderbuffers,
    final JCGLNamedExtensionsType in_extensions,
    final FakeTextures2DStatic textures2d)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.log = NullCheck.notNull(in_log, "Log").with("index");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.pool = 1;
    this.draw_buffers = NullCheck.notNull(in_draw_buffers, "Draw buffers");
    this.renderbuffers = NullCheck.notNull(in_renderbuffers, "Renderbuffers");
    this.meta = NullCheck.notNull(in_meta, "Meta");
    this.extensions = NullCheck.notNull(in_extensions, "Extensions");
    this.textures_2d = NullCheck.notNull(textures2d, "Textures 2D");
    this.color_points =
      NullCheck.notNull(in_color_points, "Color attachment points");
  }

  /**
   * Check that the given framebuffer:
   * <ul>
   * <li>Satisfies {@link #checkFramebuffer(GLContext, FramebufferUsableType)}
   * </li>
   * <li>Is bound as the draw framebuffer</li>
   * </ul>
   */

  void checkFramebufferAndDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionFramebufferNotBound,
      JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);

    if (this.framebufferDrawIsBound(framebuffer) == false) {
      throw JCGLExceptionFramebufferNotBound.notBound(framebuffer);
    }
  }

  @Override public FramebufferType framebufferAllocate()
    throws JCGLException
  {
    final int id = this.pool;
    ++this.pool;
    return new FakeFramebuffer(this.context, this.draw_buffers, id);
  }

  @Override public void framebufferBlit(
    final AreaInclusive source,
    final AreaInclusive target,
    final Set<FramebufferBlitBuffer> buffers,
    final FramebufferBlitFilter filter)
    throws JCGLException
  {
    NullCheck.notNull(source, "Source area");
    NullCheck.notNull(target, "Target area");
    NullCheck.notNull(buffers, "Buffers");
    NullCheck.notNull(filter, "Filter");

    if (this.framebufferReadAnyIsBound() == false) {
      throw new JCGLExceptionFramebufferNotBound(
        "No read framebuffer is bound");
    }
    if (this.framebufferDrawAnyIsBound() == false) {
      throw new JCGLExceptionFramebufferNotBound(
        "No draw framebuffer is bound");
    }

    final boolean want_depth =
      buffers.contains(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH);
    final boolean want_stenc =
      buffers.contains(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL);
    if (want_depth || want_stenc) {
      if (filter != FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST) {
        final String s =
          String
            .format(
              "Blit specifies depth/stencil buffers, but the filter specified is not %s (is %s)",
              FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST,
              filter);
        assert s != null;
        throw new JCGLExceptionParameterError(s);
      }
    }
  }

  @Override public void framebufferDelete(
    final FramebufferType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(framebuffer);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    ((FakeObjectDeletable) framebuffer).resourceSetDeleted();
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return this.draw_bind != null;
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeRenderbuffers.checkRenderbuffer(this.context, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isColorRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a color-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeRenderbuffer<RenderableColorKind> fake_r =
      (FakeRenderbuffer<RenderableColorKind>) renderbuffer;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setColorRenderbuffer(0, fake_r);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeRenderbuffers.checkRenderbuffer(this.context, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isColorRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a color-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeRenderbuffer<RenderableColorKind> fake_r =
      (FakeRenderbuffer<RenderableColorKind>) renderbuffer;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setColorRenderbuffer(point.colorAttachmentPointGetIndex(), fake_r);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeTextures2DStatic.checkTexture(this.context, texture);

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.meta.metaGetVersion(),
      this.extensions) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) texture;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setColorTexture(0, fake_t);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeTextures2DStatic.checkTexture(this.context, texture);

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.meta.metaGetVersion(),
      this.extensions) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) texture;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setColorTexture(point.colorAttachmentPointGetIndex(), fake_t);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final FramebufferType framebuffer,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeRenderbuffers.checkRenderbuffer(this.context, renderbuffer);

    switch (renderbuffer.renderbufferGetFormat()) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_COLOR_RGB_888:
      case RENDERBUFFER_STENCIL_8:
      {
        final String s =
          String.format(
            "Renderbuffer %s is not of a depth-renderable format",
            renderbuffer);
        assert s != null;
        throw new JCGLExceptionFormatError(s);
      }
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
      {
        break;
      }
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeRenderbuffer<RenderableDepthKind> fake_r =
      (FakeRenderbuffer<RenderableDepthKind>) renderbuffer;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setDepthRenderbuffer(fake_r);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeRenderbuffers.checkRenderbuffer(this.context, renderbuffer);

    switch (renderbuffer.renderbufferGetFormat()) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_COLOR_RGB_888:
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
      case RENDERBUFFER_STENCIL_8:
      {
        final String s =
          String.format(
            "Renderbuffer %s is not of a depth+stencil-renderable format",
            renderbuffer);
        assert s != null;
        throw new JCGLExceptionFormatError(s);
      }
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
      {
        break;
      }
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at depth+stencil attachment");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeRenderbuffer<RenderableDepthStencilKind> fake_r =
      (FakeRenderbuffer<RenderableDepthStencilKind>) renderbuffer;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setDepthStencilRenderbuffer(fake_r);
  }

  @Override public void framebufferDrawAttachDepthStencilTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeTextures2DStatic.checkTexture(this.context, texture);

    final TextureFormat format = texture.textureGetFormat();
    if ((TextureFormatMeta.isDepthRenderable2D(format, this.extensions) && TextureFormatMeta
      .isStencilRenderable(format)) == false) {
      final String s =
        String.format(
          "Texture %s is not of a depth+stencil-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at depth+stencil attachment");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) texture;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setDepthStencilTexture2D(fake_t);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeTextures2DStatic.checkTexture(this.context, texture);

    final TextureFormat format = texture.textureGetFormat();
    if (TextureFormatMeta.isDepthRenderable2D(format, this.extensions) == false) {
      final String s =
        String.format(
          "Texture %s is not of a depth-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) texture;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setDepthTexture2D(fake_t);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableStencilKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    FakeRenderbuffers.checkRenderbuffer(this.context, renderbuffer);

    switch (renderbuffer.renderbufferGetFormat()) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_COLOR_RGB_888:
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
      {
        final String s =
          String.format(
            "Renderbuffer %s is not of a stencil-renderable format",
            renderbuffer);
        assert s != null;
        throw new JCGLExceptionFormatError(s);
      }
      case RENDERBUFFER_STENCIL_8:
      {
        break;
      }
    }

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at stencil attachment");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final FakeRenderbuffer<RenderableStencilKind> fake_r =
      (FakeRenderbuffer<RenderableStencilKind>) renderbuffer;
    final FakeFramebuffer fake = (FakeFramebuffer) framebuffer;
    fake.setStencilRenderbuffer(fake_r);
  }

  @Override public void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);
    this.draw_bind = (FakeFramebuffer) framebuffer;
  }

  @Override public boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);

    final FakeFramebuffer b = this.draw_bind;
    if (b == null) {
      return false;
    }
    return b.equals(framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      final FramebufferType framebuffer,
      final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> mappings)
      throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    NullCheck.notNull(mappings, "Draw buffer mappings");

    for (final FramebufferDrawBufferType db : mappings.keySet()) {
      FakeDrawBuffers.checkDrawBuffer(this.context, db);
      final FramebufferColorAttachmentPointType point = mappings.get(db);
      FakeColorAttachmentPoints
        .checkColorAttachmentPoint(this.context, point);
    }

    final List<FramebufferDrawBufferType> buffers =
      this.draw_buffers.getDrawBuffers();
    final StringBuilder text = this.tcache.getTextCache();

    for (int index = 0; index < buffers.size(); ++index) {
      final FramebufferDrawBufferType buffer = buffers.get(index);
      assert buffer != null;

      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPointType attach =
          mappings.get(buffer);

        if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to ");
          text.append(attach);
          final String r = text.toString();
          assert r != null;
          this.log.debug(r);
        }
      } else {
        if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to none");
          final String r = text.toString();
          assert r != null;
          this.log.debug(r);
        }
      }
    }
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    this.draw_bind = null;
  }

  @Override public FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);

    final FakeFramebuffer fb = (FakeFramebuffer) framebuffer;

    /**
     * If both depth and stencil attachments are specified, they must be the
     * same object.
     */

    final FakeFramebufferAttachableType sa = fb.getStencilAttachment();
    final FakeFramebufferAttachableType da = fb.getDepthAttachment();

    if ((sa != null) && (da != null)) {
      if (sa != da) {
        return FramebufferStatus.FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED;
      }
    }

    return FramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
  }

  @Override public
    List<FramebufferColorAttachmentPointType>
    framebufferGetColorAttachmentPoints()
      throws JCGLException
  {
    return this.color_points.getColorAttachmentPoints();
  }

  @Override public
    List<FramebufferDrawBufferType>
    framebufferGetDrawBuffers()
      throws JCGLException
  {
    return this.draw_buffers.getDrawBuffers();
  }

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLException
  {
    return this.read_bind != null;
  }

  @Override public void framebufferReadBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);
    this.read_bind = (FakeFramebuffer) framebuffer;
  }

  @Override public boolean framebufferReadIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    FakeFramebuffers.checkFramebuffer(this.context, framebuffer);

    final FakeFramebuffer b = this.read_bind;
    if (b == null) {
      return false;
    }
    return b.equals(framebuffer);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLException
  {
    this.read_bind = null;
  }

  int getBitsDepth()
  {
    if (this.draw_bind != null) {
      return this.draw_bind.getBitsDepth();
    }

    return this.context.getDefaultFramebuffer().getBitsDepth();
  }

  int getBitsStencil()
  {
    if (this.draw_bind != null) {
      return this.draw_bind.getBitsStencil();
    }

    return this.context.getDefaultFramebuffer().getBitsStencil();
  }
}
