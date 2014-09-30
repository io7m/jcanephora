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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLTextureUnits implements JCGLTextureUnitsType
{
  /**
   * Check that the given texture unit:
   *
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (texture units are not shared)</li>
   * </ul>
   */

  public static void checkTextureUnit(
    final GLContext ctx,
    final TextureUnitType unit)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(unit, "Texture unit");
    JOGLCompatibilityChecks.checkTextureUnit(ctx, unit);
  }

  private final List<TextureUnitType>    cached_units;
  private final GLContext                context;
  private final GL                       gl;
  private final JOGLIntegerCacheType     icache;
  private final LogUsableType            log;
  private final int                      max_size;
  private final JCGLSoftRestrictionsType restrictions;
  private final JOGLLogMessageCacheType  tcache;

  public JOGLTextureUnits(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JCGLSoftRestrictionsType in_restrictions)
    throws JCGLExceptionRuntime
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.log = NullCheck.notNull(in_log, "Log");
    this.restrictions = NullCheck.notNull(in_restrictions, "Restrictions");
    this.context = NullCheck.notNull(this.gl.getContext());
    this.cached_units = this.makeUnits();
    this.max_size = this.icache.getInteger(this.gl, GL.GL_MAX_TEXTURE_SIZE);
  }

  private List<TextureUnitType> makeUnits()
    throws JCGLExceptionRuntime
  {
    final int max =
      this.icache.getInteger(this.gl, GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("implementation supports ");
      text.append(max);
      text.append(" texture units");
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final int count =
      Math.max(
        1,
        Math.min(this.restrictions.restrictTextureUnitCount(max), max));
    if (count < max) {
      text.setLength(0);
      text.append("implementation exposes ");
      text.append(count);
      text.append(" texture units after soft restrictions");
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final List<TextureUnitType> u = new ArrayList<TextureUnitType>();
    for (int index = 0; index < count; ++index) {
      u.add(new JOGLTextureUnit(this.context, index));
    }

    final List<TextureUnitType> r = Collections.unmodifiableList(u);
    assert r != null;
    return r;
  }

  @Override public int textureGetMaximumSize()
    throws JCGLExceptionRuntime
  {
    return this.max_size;
  }

  @Override public List<TextureUnitType> textureGetUnits()
    throws JCGLExceptionRuntime
  {
    return this.cached_units;
  }

  @Override public boolean textureUnitIsBound(
    final TextureUnitType unit)
    throws JCGLException
  {
    JOGLTextureUnits.checkTextureUnit(this.context, unit);

    final GL g = this.context.getGL();
    g.glActiveTexture(GL.GL_TEXTURE0 + unit.unitGetIndex());
    final IntBuffer cache = this.icache.getIntegerCache();
    g.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, cache);
    final int e = cache.get(0);
    return e != 0;
  }
}
