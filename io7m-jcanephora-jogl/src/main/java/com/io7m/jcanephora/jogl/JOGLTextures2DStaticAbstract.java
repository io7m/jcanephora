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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticCommonType;
import com.io7m.jcanephora.jogl.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

abstract class JOGLTextures2DStaticAbstract extends
  JOGLTextures2DStaticAllocateAbstract implements
  JCGLTextures2DStaticCommonType
{
  /**
   * Check that the given texture:
   * 
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  public static final void checkTexture(
    final GLContext ctx,
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(texture, "Texture");
    JOGLCompatibilityChecks.checkTexture(ctx, texture);
    ResourceCheck.notDeleted(texture);
  }

  public static final void setPackUnpackAlignment1(
    final GL gl)
    throws JCGLExceptionRuntime
  {
    gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
    JOGLErrors.check(gl);
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    JOGLErrors.check(gl);
  }

  public JOGLTextures2DStaticAbstract(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
  }

  @Override public final void texture2DStaticBind(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLTextures2DStaticAbstract.checkTexture(this.getContext(), texture);

    final GL g = this.getGL();
    g.glActiveTexture(GL.GL_TEXTURE0 + unit.unitGetIndex());
    g.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    JOGLErrors.check(g);
  }

  @Override public final void texture2DStaticDelete(
    final Texture2DStaticType texture)
    throws JCGLException
  {
    JOGLTextures2DStaticAbstract.checkTexture(this.getContext(), texture);

    final GL g = this.getGL();
    final LogUsableType log = this.getLog();

    final StringBuilder text = this.getTcache().getTextCache();
    if (log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(texture);
      final String s = text.toString();
      assert s != null;
      log.debug(s);
    }

    final IntBuffer cache = this.getIcache().getIntegerCache();
    cache.put(0, texture.getGLName());
    g.glDeleteTextures(1, cache);
    JOGLErrors.check(g);

    ((JOGLObjectDeletable) texture).resourceSetDeleted();
  }

  @Override public final boolean texture2DStaticIsBound(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLTextures2DStaticAbstract.checkTexture(this.getContext(), texture);
    JOGLTextureUnits.checkTextureUnit(this.getContext(), unit);

    final GL g = this.getGL();
    g.glActiveTexture(GL.GL_TEXTURE0 + unit.unitGetIndex());
    final IntBuffer cache = this.getIcache().getIntegerCache();
    g.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, cache);
    final int e = cache.get(0);
    JOGLErrors.check(g);
    return e == texture.getGLName();
  }

  @Override public final void texture2DStaticUnbind(
    final TextureUnitType unit)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext
  {
    JOGLTextureUnits.checkTextureUnit(this.getContext(), unit);

    final GL g = this.getGL();
    g.glActiveTexture(GL.GL_TEXTURE0 + unit.unitGetIndex());
    g.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGLErrors.check(g);
  }

  @Override public final void texture2DStaticUpdate(
    final Texture2DStaticUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Texture data");
    final Texture2DStaticType texture = data.getTexture();
    final GLContext context = this.getContext();
    JOGLTextures2DStaticAbstract.checkTexture(context, texture);

    final GL g = this.getGL();

    final AreaInclusive area = data.getTargetArea();
    final TextureFormat type = texture.textureGetFormat();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final TextureSpec spec = this.getTextureSpec(type);
    final ByteBuffer buffer = data.getTargetData();

    g.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    g.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.getFormat(),
      spec.getType(),
      buffer);
    g.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGLErrors.check(g);
  }
}
