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
import javax.media.opengl.GL2GL3;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.api.JCGLLogicType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLLogic implements JCGLLogicType
{
  private final GL      gl;
  private final LogType log;

  JOGLLogic(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("logic");
  }

  @Override public void logicOperationsDisable()
    throws JCGLExceptionRuntime
  {
    this.gl.glDisable(GL.GL_COLOR_LOGIC_OP);
    JOGLErrors.check(this.gl);
  }

  @Override public void logicOperationsEnable(
    final LogicOperation operation)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(operation, "Logic operation");

    final GL2GL3 g3 = this.gl.getGL2GL3();
    g3.glEnable(GL.GL_COLOR_LOGIC_OP);
    g3.glLogicOp(JOGL_GLTypeConversions.logicOpToGL(operation));
    JOGLErrors.check(this.gl);
  }

  @Override public boolean logicOperationsEnabled()
    throws JCGLExceptionRuntime
  {
    final boolean e = this.gl.glIsEnabled(GL.GL_COLOR_LOGIC_OP);
    JOGLErrors.check(this.gl);
    return e;
  }
}
