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

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLES2;

import com.io7m.jcanephora.CubeMapFaceLH;
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
import com.io7m.jcanephora.JCGLExceptionFramebufferInvalid;
import com.io7m.jcanephora.JCGLExceptionParameterError;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.api.JCGLFramebuffersGLES2Type;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jfunctional.FunctionType;
import com.io7m.jfunctional.Unit;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnimplementedCodeException;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * An implementation of the {@link JCGLFramebuffersGLES2Type} intended to run
 * on a real GLES2 context.
 */

@SuppressWarnings("synthetic-access") final class JOGLFramebuffersGLES2 extends
  JOGLFramebuffersAbstract implements JCGLFramebuffersGLES2Type
{
  private static void addColorAttachments(
    final JCGLFramebufferBuilderType b,
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg)
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
            JOGLFramebuffersGLES2.addColorRenderbuffer(
              framebuffer,
              text,
              glog,
              gg,
              point,
              r);
            return Unit.unit();
          }

          @Override public Unit texture2DStatic(
            final Texture2DStaticUsableType t)
          {
            JOGLFramebuffersGLES2.addColorTexture2D(
              framebuffer,
              text,
              glog,
              gg,
              point,
              t);
            return Unit.unit();
          }

          @Override public Unit textureCubeStatic(
            final TextureCubeStaticUsableType t,
            final CubeMapFaceLH face)
          {
            JOGLFramebuffersGLES2.addColorTextureCube(
              framebuffer,
              text,
              glog,
              gg,
              point,
              t,
              face);
            return Unit.unit();
          }
        });
    }
  }

  private static void addColorRenderbuffer(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> r)
  {
    RenderbufferFormat.checkColorRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(r);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    gg.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.colorAttachmentPointGetIndex(),
      GL.GL_RENDERBUFFER,
      r.getGLName());
  }

  private static void addColorTexture2D(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType t)
  {
    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(t);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    gg.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.colorAttachmentPointGetIndex(),
      GL.GL_TEXTURE_2D,
      t.getGLName(),
      0);
  }

  private static void addColorTextureCube(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType t,
    final CubeMapFaceLH face)
  {
    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(t);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    final int gface = JOGLTypeConversions.cubeFaceToGL(face);
    gg.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0
      + point.colorAttachmentPointGetIndex(), gface, t.getGLName(), 0);
  }

  private static void addDepthAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final JCGLNamedExtensionsType exts,
    final AtomicInteger depth_bits,
    final GLES2 gg)
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
                depth_bits.set(JOGLFramebuffersGLES2.addDepthRenderbuffer(
                  framebuffer,
                  text,
                  glog,
                  gg,
                  r));
                return Unit.unit();
              }

              @Override public Unit texture2DStatic(
                final Texture2DStaticUsableType t)
              {
                depth_bits.set(JOGLFramebuffersGLES2.addDepthTexture(
                  framebuffer,
                  text,
                  glog,
                  gg,
                  t,
                  exts));
                return Unit.unit();
              }
            });
        }
      });
  }

  private static int addDepthRenderbuffer(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final RenderbufferUsableType<RenderableDepthKind> r)
  {
    RenderbufferFormat.checkDepthOnlyRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(r);
      text.append(" at depth attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    gg.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      r.getGLName());

    return r.renderbufferGetFormat().getDepthBits();
  }

  private static void addDepthStencilAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final JCGLNamedExtensionsType exts,
    final AtomicInteger depth_bits,
    final AtomicInteger stencil_bits,
    final GLES2 gg)
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
                JOGLFramebuffersGLES2.addDepthStencilRenderbuffer(
                  framebuffer,
                  text,
                  glog,
                  gg,
                  r,
                  depth_bits,
                  stencil_bits);
                return Unit.unit();
              }

              @Override public Unit texture2DStatic(
                final Texture2DStaticUsableType t)
              {
                JOGLFramebuffersGLES2.addDepthStencilTexture2D(
                  framebuffer,
                  text,
                  glog,
                  gg,
                  t,
                  exts,
                  depth_bits,
                  stencil_bits);
                return Unit.unit();
              }
            });
        }
      });
  }

  private static void addDepthStencilRenderbuffer(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final RenderbufferUsableType<RenderableDepthStencilKind> r,
    final AtomicInteger depth_bits,
    final AtomicInteger stencil_bits)
  {
    RenderbufferFormat.checkDepthStencilRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(r);
      text.append(" at depth+stencil attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    gg.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      r.getGLName());
    gg.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      r.getGLName());

    final RenderbufferFormat format = r.renderbufferGetFormat();
    depth_bits.set(format.getDepthBits());
    stencil_bits.set(format.getStencilBits());
  }

  private static void addDepthStencilTexture2D(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions,
    final AtomicInteger depth_bits,
    final AtomicInteger stencil_bits)
  {
    TextureFormatMeta.checkDepthStencilRenderableTexture2D(t, extensions);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(t);
      text.append(" at depth+stencil attachment");
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    gg.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      t.getGLName(),
      0);
    gg.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      t.getGLName(),
      0);

    final TextureFormat format = t.textureGetFormat();
    depth_bits.set(TextureFormatMeta.getDepthBits(format));
    stencil_bits.set(TextureFormatMeta.getStencilBits(format));
  }

  private static int addDepthTexture(
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final GLES2 gg,
    final Texture2DStaticUsableType t,
    final JCGLNamedExtensionsType extensions)
  {
    TextureFormatMeta.checkDepthOnlyRenderableTexture2D(t, extensions);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(t);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    gg.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      t.getGLName(),
      0);

    return TextureFormatMeta.getDepthBits(t.textureGetFormat());
  }

  private static void addStencilAttachmentIfAny(
    final JCGLFramebufferBuilderType b,
    final int framebuffer,
    final StringBuilder text,
    final LogType glog,
    final AtomicInteger stencil_bits,
    final GLES2 gg)
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
                stencil_bits.set(JOGLFramebuffersGLES2
                  .addStencilRenderbuffer(framebuffer, text, glog, gg, r));
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

  private static int addStencilRenderbuffer(
    final int framebuffer,
    final StringBuilder text,
    final LogUsableType glog,
    final GLES2 gg,
    final RenderbufferUsableType<RenderableStencilKind> r)
  {
    RenderbufferFormat.checkStencilRenderableRenderbuffer(r);

    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(r);
      text.append(" at stencil attachment");
      final String rs = text.toString();
      assert rs != null;
      glog.debug(rs);
    }

    gg.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      r.getGLName());

    return r.renderbufferGetFormat().getStencilBits();
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

  private final GLContext ctx;
  private final GLES2     g;

  JOGLFramebuffersGLES2(
    final GLES2 in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JOGLDrawBuffersType in_draw_buffers,
    final JOGLColorAttachmentPointsType in_color_points,
    final JCGLMetaType in_meta,
    final JCGLNamedExtensionsType in_extensions)
  {
    super(
      in_gl,
      in_log,
      in_icache,
      in_tcache,
      in_color_points,
      in_draw_buffers,
      in_meta,
      in_extensions);
    this.g = in_gl;
    this.ctx = NullCheck.notNull(this.g.getContext());
  }

  @Override public FramebufferType framebufferAllocate(
    final JCGLFramebufferBuilderType b)
    throws JCGLException
  {
    JOGLFramebuffersGLES2.checkBuilder(b);

    final JCGLNamedExtensionsType exts = this.getExtensions();
    final AtomicInteger depth_bits = new AtomicInteger(0);
    final AtomicInteger stencil_bits = new AtomicInteger(0);
    final GLES2 gg = this.g;

    /**
     * Allocate framebuffer.
     */

    final IntBuffer ix = this.getIcache().getIntegerCache();
    this.g.glGenFramebuffers(1, ix);
    final int id = ix.get(0);

    final StringBuilder text = this.getTcache().getTextCache();
    final LogType glog = this.getLog();
    if (glog.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      glog.debug(r);
    }

    /**
     * Configure framebuffer.
     */

    gg.glBindFramebuffer(GL.GL_FRAMEBUFFER, id);

    try {

      JOGLFramebuffersGLES2.addDepthAttachmentIfAny(
        b,
        id,
        text,
        glog,
        exts,
        depth_bits,
        gg);
      JOGLFramebuffersGLES2.addStencilAttachmentIfAny(
        b,
        id,
        text,
        glog,
        stencil_bits,
        gg);
      JOGLFramebuffersGLES2.addDepthStencilAttachmentIfAny(
        b,
        id,
        text,
        glog,
        exts,
        depth_bits,
        stencil_bits,
        gg);
      JOGLFramebuffersGLES2.addColorAttachments(b, id, text, glog, gg);

      /**
       * Validate.
       */

      final int gs = this.g.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
      final FramebufferStatus status =
        JOGLTypeConversions.framebufferStatusFromGL(gs);
      switch (status) {
        case FRAMEBUFFER_STATUS_COMPLETE:
        {
          return new JOGLFramebuffer(
            this.ctx,
            id,
            depth_bits.get(),
            stencil_bits.get());
        }
        case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT:
        case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER:
        case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER:
        case FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT:
        case FRAMEBUFFER_STATUS_ERROR_UNKNOWN:
        case FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED:
        {
          throw new JCGLExceptionFramebufferInvalid(status);
        }
      }

      throw new UnreachableCodeException();

    } finally {
      gg.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    final int bound = this.g.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
    final int default_fb = this.g.getDefaultDrawFramebuffer();
    return bound != default_fb;
  }

  @Override public void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    this.g.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebuffer.getGLName());
  }

  @Override public boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    final int bound = this.g.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    this.g.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
  }

  @Override public FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    final int status = this.g.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    return JOGLTypeConversions.framebufferStatusFromGL(status);
  }

  @Override public
    List<FramebufferColorAttachmentPointType>
    framebufferGetColorAttachmentPoints()
      throws JCGLException
  {
    return this.getColorPoints().getColorAttachmentPoints();
  }

  @Override public
    List<FramebufferDrawBufferType>
    framebufferGetDrawBuffers()
      throws JCGLException
  {
    return this.getDrawBuffers().getDrawBuffers();
  }
}
