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
import com.io7m.jcanephora.FramebufferColorAttachmentType;
import com.io7m.jcanephora.FramebufferColorAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthAttachmentType;
import com.io7m.jcanephora.FramebufferDepthAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferStencilAttachmentType;
import com.io7m.jcanephora.FramebufferStencilAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.JCGLExceptionParameterError;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jfunctional.FunctionType;
import com.io7m.jfunctional.Unit;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnimplementedCodeException;

/**
 * The framebuffer API implementation.
 */

@SuppressWarnings("synthetic-access") public final class FakeFramebuffers implements
  JCGLFramebuffersGL3Type
{
  private static void addColorAttachments(
    final JCGLFramebufferBuilderType b,
    final FakeFramebuffer fb,
    final StringBuilder text,
    final LogType glog)
  {
    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> cas =
      b.getColorAttachments();

    for (final FramebufferColorAttachmentPointType point : cas.keySet()) {
      assert point != null;
      final FramebufferColorAttachmentType ca = cas.get(point);
      assert ca != null;

      ca
        .colorAttachmentAccept(new FramebufferColorAttachmentVisitorType<Unit, JCGLException>() {
          @Override public Unit renderbuffer(
            final RenderbufferUsableType<RenderableColorKind> r)
          {
            FakeFramebuffers.addColorRenderbuffer(fb, text, glog, point, r);
            return Unit.unit();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            FakeFramebuffers.addColorTexture2D(fb, text, glog, point, t);
            return Unit.unit();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            FakeFramebuffers.addColorTextureCube(
              fb,
              text,
              glog,
              point,
              t,
              face);
            return Unit.unit();
          }
        });
    }
  }

  private static void addColorRenderbuffer(
    final FakeFramebuffer fb,
    final StringBuilder text,
    final LogType glog,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> r)
  {
    RenderbufferFormat.checkColorRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fb);
      text.append(" ");
      text.append(r);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    final FakeRenderbuffer<RenderableColorKind> fake_r =
      (FakeRenderbuffer<RenderableColorKind>) r;
    fb.setColorRenderbuffer(point.colorAttachmentPointGetIndex(), fake_r);
  }

  private static void addColorTexture2D(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType t)
  {
    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(t);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) t;
    fake.setColorTexture(point.colorAttachmentPointGetIndex(), fake_t);
  }

  @SuppressWarnings("unused") private static void addColorTextureCube(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType t,
    final CubeMapFaceLH face)
  {
    throw new UnimplementedCodeException();
  }

  private static void addDepthAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final FakeFramebuffer framebuffer,
    final StringBuilder text,
    final LogType glog,
    final JCGLNamedExtensionsType exts)
  {
    b.getDepthAttachment().map(
      new FunctionType<FramebufferDepthAttachmentType, Unit>() {
        @Override public Unit call(
          final FramebufferDepthAttachmentType da)
        {
          return da
            .depthAttachmentAccept(new FramebufferDepthAttachmentVisitorType<Unit, JCGLException>() {
              @Override public Unit renderbuffer(
                final RenderbufferUsableType<RenderableDepthKind> r)
              {
                FakeFramebuffers.addDepthRenderbuffer(
                  framebuffer,
                  text,
                  glog,
                  r);
                return Unit.unit();
              }

              @Override public Unit texture2DStatic(
                final Texture2DStaticUsableType t)
              {
                FakeFramebuffers.addDepthTexture(
                  framebuffer,
                  text,
                  glog,
                  t,
                  exts);
                return Unit.unit();
              }
            });
        }
      });
  }

  private static void addDepthRenderbuffer(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final RenderbufferUsableType<RenderableDepthKind> r)
  {
    RenderbufferFormat.checkDepthOnlyRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(r);
      text.append(" at depth attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    final FakeRenderbuffer<RenderableDepthKind> fake_r =
      (FakeRenderbuffer<RenderableDepthKind>) r;
    fake.setDepthRenderbuffer(fake_r);
  }

  private static void addDepthStencilAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final FakeFramebuffer framebuffer,
    final StringBuilder text,
    final LogType glog,
    final JCGLNamedExtensionsType exts)
  {
    b.getDepthStencilAttachment().map(
      new FunctionType<FramebufferDepthStencilAttachmentType, Unit>() {
        @Override public Unit call(
          final FramebufferDepthStencilAttachmentType dsa)
        {
          return dsa
            .depthStencilAttachmentAccept(new FramebufferDepthStencilAttachmentVisitorType<Unit, JCGLException>() {
              @Override public Unit renderbuffer(
                final RenderbufferUsableType<RenderableDepthStencilKind> r)
              {
                FakeFramebuffers.addDepthStencilRenderbuffer(
                  framebuffer,
                  text,
                  glog,
                  r);
                return Unit.unit();
              }

              @Override public Unit texture2DStatic(
                final Texture2DStaticUsableType t)
              {
                FakeFramebuffers.addDepthStencilTexture2D(
                  framebuffer,
                  text,
                  glog,
                  t,
                  exts);
                return Unit.unit();
              }
            });
        }
      });
  }

  private static void addDepthStencilRenderbuffer(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final RenderbufferUsableType<RenderableDepthStencilKind> r)
  {
    RenderbufferFormat.checkDepthStencilRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(r);
      text.append(" at depth+stencil attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    final FakeRenderbuffer<RenderableDepthStencilKind> fake_r =
      (FakeRenderbuffer<RenderableDepthStencilKind>) r;
    fake.setDepthStencilRenderbuffer(fake_r);
  }

  private static void addDepthStencilTexture2D(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions)
  {
    TextureFormatMeta.checkDepthStencilRenderableTexture2D(t, extensions);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(t);
      text.append(" at depth+stencil attachment");
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) t;
    fake.setDepthStencilTexture2D(fake_t);
  }

  private static void addDepthTexture(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogType glog,
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions)
  {
    TextureFormatMeta.checkDepthOnlyRenderableTexture2D(t, extensions);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(t);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) t;
    fake.setDepthTexture2D(fake_t);
  }

  private static
    void
    addDrawBufferMappings(
      final FakeContext context,
      final StringBuilder text,
      final LogType glog,
      final List<FramebufferDrawBufferType> buffers,
      final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> mappings)
  {
    NullCheck.notNull(mappings, "Draw buffer mappings");

    for (final FramebufferDrawBufferType db : mappings.keySet()) {
      FakeDrawBuffers.checkDrawBuffer(context, db);
      final FramebufferColorAttachmentPointType point = mappings.get(db);
      FakeColorAttachmentPoints.checkColorAttachmentPoint(context, point);
    }

    for (int index = 0; index < buffers.size(); ++index) {
      final FramebufferDrawBufferType buffer = buffers.get(index);
      assert buffer != null;

      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPointType attach =
          mappings.get(buffer);

        if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to ");
          text.append(attach);
          final String r = text.toString();
          assert r != null;
          glog.debug(r);
        }
      } else {
        if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to none");
          final String r = text.toString();
          assert r != null;
          glog.debug(r);
        }
      }
    }
  }

  private static void addStencilAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final FakeFramebuffer framebuffer,
    final StringBuilder text,
    final LogType glog)
  {
    b.getStencilAttachment().map(
      new FunctionType<FramebufferStencilAttachmentType, Unit>() {
        @Override public Unit call(
          final FramebufferStencilAttachmentType sa)
        {
          return sa
            .stencilAttachmentAccept(new FramebufferStencilAttachmentVisitorType<Unit, JCGLException>() {
              @Override public Unit renderbuffer(
                final RenderbufferUsableType<RenderableStencilKind> r)
              {
                FakeFramebuffers.addStencilRenderbuffer(
                  framebuffer,
                  text,
                  glog,
                  r);
                return Unit.unit();
              }

              @Override public Unit texture2DStatic(
                final Texture2DStaticUsableType t)
              {
                throw new UnimplementedCodeException();
              }
            });
        }
      });
  }

  private static void addStencilRenderbuffer(
    final FakeFramebuffer fake,
    final StringBuilder text,
    final LogUsableType glog,
    final RenderbufferUsableType<RenderableStencilKind> r)
  {
    RenderbufferFormat.checkStencilRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(fake);
      text.append(" ");
      text.append(r);
      text.append(" at stencil attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    final FakeRenderbuffer<RenderableStencilKind> fake_r =
      (FakeRenderbuffer<RenderableStencilKind>) r;
    fake.setStencilRenderbuffer(fake_r);
  }

  private static void checkBuilder(
    final JCGLFramebufferBuilderType b)
  {
    NullCheck.notNull(b, "Builder");

    if (b.getDepthStencilAttachment().isSome()) {
      if (b.getDepthAttachment().isSome()) {
        throw new JCGLExceptionParameterError(
          "Depth attachment specified but depth+stencil already specified");
      }
      if (b.getStencilAttachment().isSome()) {
        throw new JCGLExceptionParameterError(
          "Stencil attachment specified but depth+stencil already specified");
      }
    }
  }

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
  private final FakeLogMessageCacheType       tcache;

  FakeFramebuffers(
    final FakeContext in_context,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache,
    final FakeColorAttachmentPointsType in_color_points,
    final FakeDrawBuffers in_draw_buffers,
    final FakeMeta in_meta,
    final JCGLNamedExtensionsType in_extensions)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.log = NullCheck.notNull(in_log, "Log").with("framebuffers");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.pool = 1;
    this.draw_buffers = NullCheck.notNull(in_draw_buffers, "Draw buffers");
    this.meta = NullCheck.notNull(in_meta, "Meta");
    this.extensions = NullCheck.notNull(in_extensions, "Extensions");
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

  @Override public FramebufferType framebufferAllocate(
    final JCGLFramebufferBuilderType b)
    throws JCGLException
  {
    FakeFramebuffers.checkBuilder(b);

    final int id = this.pool;
    ++this.pool;
    final FakeFramebuffer fb =
      new FakeFramebuffer(this.context, this.draw_buffers, id);

    final JCGLNamedExtensionsType exts = this.extensions;
    final StringBuilder text = this.tcache.getTextCache();

    FakeFramebuffers.addDepthAttachmentIfAny(b, fb, text, this.log, exts);
    FakeFramebuffers.addStencilAttachmentIfAny(b, fb, text, this.log);
    FakeFramebuffers.addDepthStencilAttachmentIfAny(
      b,
      fb,
      text,
      this.log,
      exts);
    FakeFramebuffers.addColorAttachments(b, fb, text, this.log);
    FakeFramebuffers.addDrawBufferMappings(
      this.context,
      text,
      this.log,
      this.draw_buffers.getDrawBuffers(),
      b.getDrawBufferMappings());

    return fb;
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

  @Override public JCGLFramebufferBuilderType framebufferNewBuilder()
  {
    return new FakeFramebufferBuilder(
      this.color_points.getColorAttachmentPoints(),
      this.draw_buffers.getDrawBuffers(),
      this.extensions,
      this.meta.metaGetVersion());
  }

  @Override public
    JCGLFramebufferBuilderGL3ES3Type
    framebufferNewBuilderGL3ES3()
  {
    return new FakeFramebufferBuilder(
      this.color_points.getColorAttachmentPoints(),
      this.draw_buffers.getDrawBuffers(),
      this.extensions,
      this.meta.metaGetVersion());
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
      return this.draw_bind.framebufferGetDepthBits();
    }

    return this.context.getDefaultFramebuffer().getBitsDepth();
  }

  int getBitsStencil()
  {
    if (this.draw_bind != null) {
      return this.draw_bind.framebufferGetStencilBits();
    }

    return this.context.getDefaultFramebuffer().getBitsStencil();
  }
}
