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

import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

final class JOGLDrawBuffers implements JOGLDrawBuffersType
{
  /**
   * <p>
   * Check that the draw buffer:
   * </p>
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (draw buffer indices are not shared)</li>
   * </ul>
   */

  public static FramebufferDrawBufferType checkDrawBuffer(
    final GLContext ctx,
    final @Nullable FramebufferDrawBufferType b)
    throws JCGLExceptionWrongContext
  {
    final FramebufferDrawBufferType bb = NullCheck.notNull(b, "Draw buffer");
    JOGLCompatibilityChecks.checkDrawBuffer(ctx, bb);
    return bb;
  }

  private final List<FramebufferDrawBufferType> buffers;
  private final GL                              gl;
  private final JOGLIntegerCacheType            icache;
  private final LogUsableType                   log;
  private final JOGLLogMessageCacheType         tcache;

  public JOGLDrawBuffers(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
    throws JCGLExceptionRuntime
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.log = NullCheck.notNull(in_log, "Log");
    this.buffers = this.make();
  }

  @Override public List<FramebufferDrawBufferType> getDrawBuffers()
    throws JCGLExceptionRuntime
  {
    return this.buffers;
  }

  private List<FramebufferDrawBufferType> make()
    throws JCGLExceptionRuntime
  {
    final int max =
      this.icache.getInteger(this.gl, GL2ES2.GL_MAX_DRAW_BUFFERS);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("implementation supports ");
      text.append(max);
      text.append(" framebuffer draw buffers");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final GLContext ctx = this.gl.getContext();
    assert ctx != null;

    final List<FramebufferDrawBufferType> b =
      new ArrayList<FramebufferDrawBufferType>();
    for (int index = 0; index < max; ++index) {

      b.add(new JOGLFramebufferDrawBuffer(ctx, index));
    }

    return b;
  }
}
