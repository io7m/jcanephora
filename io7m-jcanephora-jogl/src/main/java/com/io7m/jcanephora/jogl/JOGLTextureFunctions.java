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

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.junreachable.UnreachableCodeException;

final class JOGLTextureFunctions
{
  static List<TextureUnitType> textureGetUnitsActual(
    final GL gl,
    final JOGLIntegerCacheType icache,
    final JOGLLogMessageCacheType tcache,
    final LogUsableType log,
    final JCGLSoftRestrictionsType restrictions)
    throws JCGLExceptionRuntime
  {
    final StringBuilder text = tcache.getTextCache();
    final int max = icache.getInteger(gl, GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);

    if (log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("implementation supports ");
      text.append(max);
      text.append(" texture units");
      final String r = text.toString();
      assert r != null;
      log.debug(r);
    }

    final int count =
      Math.max(1, Math.min(restrictions.restrictTextureUnitCount(max), max));
    if (count < max) {
      text.setLength(0);
      text.append("implementation exposes ");
      text.append(count);
      text.append(" texture units after soft restrictions");
      final String r = text.toString();
      assert r != null;
      log.debug(r);
    }

    final GLContext ctx = gl.getContext();
    assert ctx != null;

    final List<TextureUnitType> u = new ArrayList<TextureUnitType>();
    for (int index = 0; index < count; ++index) {
      u.add(new JOGLTextureUnit(ctx, index));
    }

    return u;
  }

  private JOGLTextureFunctions()
  {
    throw new UnreachableCodeException();
  }
}
