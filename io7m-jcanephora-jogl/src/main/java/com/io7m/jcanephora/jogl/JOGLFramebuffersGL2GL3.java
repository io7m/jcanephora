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
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES3;
import javax.media.opengl.GLContext;

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
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebuffersGL3Type;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;
import com.jogamp.common.nio.Buffers;

final class JOGLFramebuffersGL2GL3 extends JOGLFramebuffersAbstract implements
  JCGLFramebuffersGL3Type
{
  private final GLContext ctx;
  private final GL2ES3    g;

  JOGLFramebuffersGL2GL3(
    final GL2ES3 in_gl,
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

    final RangeInclusiveL s_range_x = source.getRangeX();
    final RangeInclusiveL s_range_y = source.getRangeY();
    final RangeInclusiveL d_range_x = target.getRangeX();
    final RangeInclusiveL d_range_y = target.getRangeY();

    final int src_x0 = (int) s_range_x.getLower();
    final int src_y0 = (int) s_range_y.getLower();
    final int src_x1 = (int) s_range_x.getUpper();
    final int src_y1 = (int) s_range_y.getUpper();

    final int dst_x0 = (int) d_range_x.getLower();
    final int dst_y0 = (int) d_range_y.getLower();
    final int dst_x1 = (int) d_range_x.getUpper();
    final int dst_y1 = (int) d_range_y.getUpper();

    final int mask =
      JOGL_GLTypeConversions.framebufferBlitBufferSetToMask(buffers);
    final int filteri =
      JOGL_GLTypeConversions.framebufferBlitFilterToGL(filter);

    this.g.glBlitFramebuffer(
      src_x0,
      src_y0,
      src_x1,
      src_y1,
      dst_x0,
      dst_y0,
      dst_x1,
      dst_y1,
      mask,
      filteri);
    JOGLErrors.check(this.g);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    final int bound = this.g.getBoundFramebuffer(GL2ES3.GL_DRAW_FRAMEBUFFER);
    final int default_fb = this.g.getDefaultDrawFramebuffer();
    return bound != default_fb;
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.ctx, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isColorRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a color-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.ctx, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isColorRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a color-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.colorAttachmentPointGetIndex(),
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLTextures2DStaticAbstract.checkTexture(this.ctx, texture);

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.getMeta().metaGetVersion(),
      this.getExtensions()) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLTextures2DStaticAbstract.checkTexture(this.ctx, texture);

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.getMeta().metaGetVersion(),
      this.getExtensions()) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.colorAttachmentPointGetIndex(),
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final FramebufferType framebuffer,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLTexturesCubeStaticAbstract.checkTexture(this.ctx, texture);
    NullCheck.notNull(face, "Cube face");

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.getMeta().metaGetVersion(),
      this.getExtensions()) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment 0");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);
    this.g.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLTexturesCubeStaticAbstract.checkTexture(this.ctx, texture);
    NullCheck.notNull(face, "Cube face");

    if (TextureFormatMeta.isColorRenderable2D(
      texture.textureGetFormat(),
      this.getMeta().metaGetVersion(),
      this.getExtensions()) == false) {
      final String s =
        String.format(
          "Texture %s is not of a color-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at color attachment ");
      text.append(point.colorAttachmentPointGetIndex());
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);
    this.g.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.colorAttachmentPointGetIndex(),
      gface,
      texture.getGLName(),
      0);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.ctx, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isDepthRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a depth-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    if (renderbuffer.renderbufferGetFormat().isStencilRenderable()) {
      final String s =
        String
          .format(
            "Renderbuffer %s is of a depth+stencil-renderable format, so must be attached as a depth+stencil attachment, not a depth attachment",
            renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.ctx, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isDepthRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a depth+stencil-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    if (renderbuffer.renderbufferGetFormat().isStencilRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a depth+stencil-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at depth+stencil attachment");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLTextures2DStaticAbstract.checkTexture(this.ctx, texture);

    if (TextureFormatMeta.isDepthRenderable2D(
      texture.textureGetFormat(),
      this.getExtensions()) == false) {
      final String s =
        String.format(
          "Texture %s is not of a depth-renderable format",
          texture);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(texture);
      text.append(" at depth attachment");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableStencilKind> renderbuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.ctx, renderbuffer);

    if (renderbuffer.renderbufferGetFormat().isStencilRenderable() == false) {
      final String s =
        String.format(
          "Renderbuffer %s is not of a stencil-renderable format",
          renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    if (renderbuffer.renderbufferGetFormat().isDepthRenderable() == false) {
      final String s =
        String
          .format(
            "Renderbuffer %s is of a depth+stencil-renderable format, so must be attached as a depth+stencil attachment, not a stencil attachment",
            renderbuffer);
      assert s != null;
      throw new JCGLExceptionFormatError(s);
    }

    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();
    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("attach ");
      text.append(framebuffer);
      text.append(" ");
      text.append(renderbuffer);
      text.append(" at stencil attachment");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    this.g.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    this.g.glBindFramebuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      framebuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    final int bound = this.g.getBoundFramebuffer(GL2ES3.GL_DRAW_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
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
      JOGLDrawBuffers.checkDrawBuffer(this.ctx, db);
      final FramebufferColorAttachmentPointType point = mappings.get(db);
      JOGLColorAttachmentPoints.checkColorAttachmentPoint(this.ctx, point);
    }

    final List<FramebufferDrawBufferType> buffers =
      this.getDrawBuffers().getDrawBuffers();
    final LogType logx = this.getLog();
    final StringBuilder text = this.getTcache().getTextCache();

    final IntBuffer out = Buffers.newDirectIntBuffer(buffers.size());

    for (int index = 0; index < buffers.size(); ++index) {
      final FramebufferDrawBufferType buffer = buffers.get(index);
      assert buffer != null;

      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPointType attach =
          mappings.get(buffer);

        out.put(
          index,
          GL.GL_COLOR_ATTACHMENT0 + attach.colorAttachmentPointGetIndex());

        if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to ");
          text.append(attach);
          final String r = text.toString();
          assert r != null;
          logx.debug(r);
        }
      } else {
        out.put(index, GL.GL_NONE);
        if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
          text.setLength(0);
          text.append("map ");
          text.append(buffer);
          text.append(" to none");
          final String r = text.toString();
          assert r != null;
          logx.debug(r);
        }
      }
    }

    out.rewind();

    this.g.glDrawBuffers(buffers.size(), out);
    JOGLErrors.check(this.g);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    this.g.glBindFramebuffer(GL2ES3.GL_DRAW_FRAMEBUFFER, 0);
    JOGLErrors.check(this.g);
  }

  @Override public FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    final int status =
      this.g.glCheckFramebufferStatus(GL2ES3.GL_DRAW_FRAMEBUFFER);
    JOGLErrors.check(this.g);
    return JOGL_GLTypeConversions.framebufferStatusFromGL(status);
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

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLExceptionRuntime
  {
    final int bound = this.g.getBoundFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER);
    final int default_fb = this.g.getDefaultReadFramebuffer();
    return bound != default_fb;
  }

  @Override public void framebufferReadBind(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    this.g.glBindFramebuffer(
      GL2ES3.GL_READ_FRAMEBUFFER,
      framebuffer.getGLName());
    JOGLErrors.check(this.g);
  }

  @Override public boolean framebufferReadIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.ctx, framebuffer);
    final int bound = this.g.getBoundFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
  }

  @Override public void framebufferReadUnbind()
    throws JCGLExceptionRuntime
  {
    this.g.glBindFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER, 0);
    JOGLErrors.check(this.g);
  }
}
