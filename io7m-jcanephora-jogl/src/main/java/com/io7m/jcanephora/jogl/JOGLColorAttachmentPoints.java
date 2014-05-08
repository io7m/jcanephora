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

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

final class JOGLColorAttachmentPoints implements
  JOGLColorAttachmentPointsType
{
  /**
   * <p>
   * Check that the attachment point:
   * </p>
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (attachment points are not shared)</li>
   * </ul>
   */

  public static
    FramebufferColorAttachmentPointType
    checkColorAttachmentPoint(
      final GLContext ctx,
      final @Nullable FramebufferColorAttachmentPointType b)
      throws JCGLExceptionWrongContext
  {
    final FramebufferColorAttachmentPointType bb =
      NullCheck.notNull(b, "Color attachment point");
    JOGLCompatibilityChecks.checkColorAttachmentPoint(ctx, bb);
    return bb;
  }

  private static List<FramebufferColorAttachmentPointType> make(
    final GL gl,
    final JOGLIntegerCacheType icache,
    final JOGLLogMessageCacheType tcache,
    final LogUsableType log)
    throws JCGLExceptionRuntime
  {
    final int max = icache.getInteger(gl, GL2ES2.GL_MAX_COLOR_ATTACHMENTS);

    final StringBuilder text = tcache.getTextCache();
    if (log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("implementation supports ");
      text.append(max);
      text.append(" framebuffer color attachments");
      final String r = text.toString();
      assert r != null;
      log.debug(r);
    }

    final GLContext ctx = gl.getContext();
    assert ctx != null;

    final List<FramebufferColorAttachmentPointType> a =
      new ArrayList<FramebufferColorAttachmentPointType>();
    for (int index = 0; index < max; ++index) {
      a.add(new JOGLFramebufferColorAttachmentPoint(ctx, index));
    }

    return a;
  }

  private final List<FramebufferColorAttachmentPointType> points;

  public JOGLColorAttachmentPoints(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(in_gl, "GL");
    NullCheck.notNull(in_icache, "Integer cache");
    NullCheck.notNull(in_tcache, "Log message cache");
    NullCheck.notNull(in_log, "Log");

    this.points =
      JOGLColorAttachmentPoints.make(in_gl, in_icache, in_tcache, in_log);
  }

  @Override public
    List<FramebufferColorAttachmentPointType>
    getColorAttachmentPoints()
  {
    return this.points;
  }
}
