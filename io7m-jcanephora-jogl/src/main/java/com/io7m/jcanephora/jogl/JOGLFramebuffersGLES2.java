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

import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLES2;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionFormatError;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebuffersGLES2Type;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

/**
 * An implementation of the {@link JCGLFramebuffersGLES2Type} intended to run
 * on a real GLES2 context.
 */

final class JOGLFramebuffersGLES2 extends JOGLFramebuffersAbstract implements
  JCGLFramebuffersGLES2Type
{
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

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    final int bound = this.g.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
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
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
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
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
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

    final int gface = JOGLTypeConversions.cubeFaceToGL(face);
    this.g.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
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

    if (renderbuffer.renderbufferGetFormat().isStencilRenderable() == false) {
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
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
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
      GL.GL_FRAMEBUFFER,
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
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    this.g.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
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
    this.g.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebuffer.getGLName());
    JOGLErrors.check(this.g);
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
    JOGLErrors.check(this.g);
  }

  @Override public FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.checkFramebufferAndDrawIsBound(framebuffer);
    final int status = this.g.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    JOGLErrors.check(this.g);
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
