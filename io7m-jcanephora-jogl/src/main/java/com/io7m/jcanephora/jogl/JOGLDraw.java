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

import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.api.JCGLDrawType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLDraw implements JCGLDrawType
{
  private final GL      gl;
  private final LogType log;

  JOGLDraw(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("draw");
  }

  @Override public void drawElements(
    final Primitives mode,
    final IndexBufferUsableType indices)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(mode, "Drawing mode");
    JOGLIndexBuffers.checkIndex(this.gl, indices);

    final int index_id = indices.getGLName();
    final int index_count = (int) indices.bufferGetRange().getInterval();
    final int mode_gl = JOGL_GLTypeConversions.primitiveToGL(mode);
    final int type =
      JOGL_GLTypeConversions.unsignedTypeToGL(indices.indexGetType());

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, index_id);
    try {
      this.gl.glDrawElements(mode_gl, index_count, type, 0L);
    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);
  }
}
