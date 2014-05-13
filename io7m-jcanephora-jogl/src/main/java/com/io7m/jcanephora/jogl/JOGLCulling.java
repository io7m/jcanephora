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

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLCullType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLCulling implements JCGLCullType
{
  private final GL      gl;
  private final LogType log;

  JOGLCulling(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("culling");
  }

  @Override public void cullingDisable()
    throws JCGLExceptionRuntime
  {
    this.gl.glDisable(GL.GL_CULL_FACE);
    JOGLErrors.check(this.gl);
  }

  @Override public void cullingEnable(
    final FaceSelection faces,
    final FaceWindingOrder order)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(order, "Face winding order");

    final int fi = JOGLTypeConversions.faceSelectionToGL(faces);
    final int oi = JOGLTypeConversions.faceWindingOrderToGL(order);

    this.gl.glEnable(GL.GL_CULL_FACE);
    this.gl.glCullFace(fi);
    this.gl.glFrontFace(oi);
    JOGLErrors.check(this.gl);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLExceptionRuntime
  {
    final boolean e = this.gl.glIsEnabled(GL.GL_CULL_FACE);
    JOGLErrors.check(this.gl);
    return e;
  }
}
