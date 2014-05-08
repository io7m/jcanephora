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

import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.api.JCGLRenderbuffersCommonType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

abstract class JOGLRenderbuffersAbstract implements
  JCGLRenderbuffersCommonType
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

  public static
    <K extends RenderbufferKind, R extends RenderbufferUsableType<K>>
    R
    checkRenderbuffer(
      final GLContext ctx,
      final R r)
      throws JCGLExceptionWrongContext,
        JCGLExceptionDeleted
  {
    NullCheck.notNull(r, "Renderbuffer");
    JOGLCompatibilityChecks.checkRenderbuffer(ctx, r);
    ResourceCheck.notDeleted(r);
    return r;
  }

  private final GLContext               context;
  private final GL                      gl;
  private final JOGLIntegerCacheType    icache;
  private final LogUsableType           log;
  private final JOGLLogMessageCacheType tcache;

  JOGLRenderbuffersAbstract(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("renderbuffers");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.context = NullCheck.notNull(this.gl.getContext());
  }

  protected final GLContext getContext()
  {
    return this.context;
  }

  protected final JOGLIntegerCacheType getIcache()
  {
    return this.icache;
  }

  protected final LogUsableType getLog()
  {
    return this.log;
  }

  protected final JOGLLogMessageCacheType getTcache()
  {
    return this.tcache;
  }

  protected final JOGLRenderbuffer<?> renderbufferAllocate(
    final RenderbufferFormat type,
    final int width,
    final int height)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(type, "Renderbuffer type");

    RangeCheck.checkIncludedIn(
      width,
      "Width",
      RangeCheck.POSITIVE_INTEGER,
      "Valid widths");
    RangeCheck.checkIncludedIn(
      height,
      "Height",
      RangeCheck.POSITIVE_INTEGER,
      "Valid heights");

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocate ");
      text.append(width);
      text.append("x");
      text.append(height);
      text.append(" ");
      text.append(type);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final IntBuffer cache = this.getIcache().getIntegerCache();
    this.gl.glGenRenderbuffers(1, cache);
    JOGLErrors.check(this.gl);
    final int id = cache.get(0);

    final int gtype = JOGL_GLTypeConversions.renderbufferTypeToGL(type);
    this.gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    JOGLErrors.check(this.gl);
    this.gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, gtype, width, height);
    JOGLErrors.check(this.gl);
    this.gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
    JOGLErrors.check(this.gl);

    /**
     * The phantom type is set to {@link RenderableColorKind} here and then
     * deliberately discarded. The caller will cast to the correct type.
     */

    final JOGLRenderbuffer<?> r =
      new JOGLRenderbuffer<RenderableColorKind>(
        this.getContext(),
        type,
        id,
        width,
        height);
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(r);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    return r;
  }

  @Override public void renderbufferDelete(
    final RenderbufferType<?> buffer)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLRenderbuffersAbstract.checkRenderbuffer(this.context, buffer);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(buffer);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final IntBuffer ix = this.icache.getIntegerCache();
    ix.put(0, buffer.getGLName());
    this.gl.glDeleteRenderbuffers(1, ix);
    ((JOGLObjectDeletable) buffer).resourceSetDeleted();
    JOGLErrors.check(this.gl);
  }
}
