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

import javax.media.opengl.GL;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLScissorType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

final class JOGLScissor implements JCGLScissorType
{
  private final GL      gl;
  private final LogType log;

  JOGLScissor(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("scissor");
  }

  @Override public void scissorDisable()
    throws JCGLExceptionRuntime
  {
    this.gl.glDisable(GL.GL_SCISSOR_TEST);
    JOGLErrors.check(this.gl);
  }

  @Override public void scissorEnable(
    final AreaInclusive area)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(area, "Scissor area");

    final RangeInclusiveL range_x = area.getRangeX();
    final RangeInclusiveL range_y = area.getRangeY();

    this.gl.glEnable(GL.GL_SCISSOR_TEST);
    this.gl.glScissor(
      (int) range_x.getLower(),
      (int) range_y.getLower(),
      (int) range_x.getInterval(),
      (int) range_y.getInterval());
    JOGLErrors.check(this.gl);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLExceptionRuntime
  {
    final boolean e = this.gl.glIsEnabled(GL.GL_SCISSOR_TEST);
    JOGLErrors.check(this.gl);
    return e;
  }
}
