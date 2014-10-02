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

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

abstract class JOGLFramebuffersAbstract implements JCGLFramebuffersCommonType
{
  /**
   * Check that the given framebuffer:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (framebuffers are not shared)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  public static void checkFramebuffer(
    final GLContext ctx,
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(framebuffer, "Framebuffer");
    JOGLCompatibilityChecks.checkFramebuffer(ctx, framebuffer);
    ResourceCheck.notDeleted(framebuffer);
  }

  private final JOGLColorAttachmentPointsType color_points;
  private final GLContext                     context;
  private final JOGLDrawBuffersType           draw_buffers;
  private final JCGLNamedExtensionsType       extensions;
  private final GL                            gl;
  private final JOGLIntegerCacheType          icache;
  private final LogType                       log;
  private final JCGLMetaType                  meta;
  private final JOGLLogMessageCacheType       tcache;

  JOGLFramebuffersAbstract(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JOGLColorAttachmentPointsType in_color_points,
    final JOGLDrawBuffersType in_draw_buffers,
    final JCGLMetaType in_meta,
    final JCGLNamedExtensionsType in_extensions)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("framebuffers");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.context = NullCheck.notNull(this.gl.getContext());
    this.color_points = NullCheck.notNull(in_color_points, "Color points");
    this.draw_buffers = NullCheck.notNull(in_draw_buffers, "Draw buffers");
    this.meta = NullCheck.notNull(in_meta, "Meta");
    this.extensions = NullCheck.notNull(in_extensions, "Extensions");
  }

  /**
   * Check that the given framebuffer:
   * <ul>
   * <li>Satisfies {@link #checkFramebuffer(GLContext, FramebufferUsableType)}
   * </li>
   * <li>Is bound as the draw framebuffer</li>
   * </ul>
   */

  public final void checkFramebufferAndDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionFramebufferNotBound,
      JCGLException
  {
    JOGLFramebuffersAbstract.checkFramebuffer(this.context, framebuffer);

    if (this.framebufferDrawIsBound(framebuffer) == false) {
      throw JCGLExceptionFramebufferNotBound.notBound(framebuffer);
    }
  }

  @Override public void framebufferDelete(
    final FramebufferType framebuffer)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;

    JOGLFramebuffersAbstract.checkFramebuffer(ctx, framebuffer);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(framebuffer);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final IntBuffer ix = this.icache.getIntegerCache();
    ix.put(0, framebuffer.getGLName());
    this.gl.glDeleteFramebuffers(1, ix);
    ((JOGLObjectDeletable) framebuffer).resourceSetDeleted();
  }

  @Override public final JCGLFramebufferBuilderType framebufferNewBuilder()
  {
    return new JOGLFramebufferBuilder(
      this.framebufferGetColorAttachmentPoints(),
      this.framebufferGetDrawBuffers(),
      this.getExtensions(),
      this.meta.metaGetVersion());
  }

  protected final JOGLColorAttachmentPointsType getColorPoints()
  {
    return this.color_points;
  }

  protected final JOGLDrawBuffersType getDrawBuffers()
  {
    return this.draw_buffers;
  }

  protected final JCGLNamedExtensionsType getExtensions()
  {
    return this.extensions;
  }

  protected final JOGLIntegerCacheType getIcache()
  {
    return this.icache;
  }

  protected final LogType getLog()
  {
    return this.log;
  }

  protected final JCGLMetaType getMeta()
  {
    return this.meta;
  }

  protected final JOGLLogMessageCacheType getTcache()
  {
    return this.tcache;
  }
}
