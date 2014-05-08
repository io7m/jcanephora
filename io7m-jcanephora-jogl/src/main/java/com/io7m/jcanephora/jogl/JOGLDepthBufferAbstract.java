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
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

abstract class JOGLDepthBufferAbstract implements JCGLDepthBufferType
{
  private final ByteBuffer depth_buffer_mask_cache;
  private final GL         gl;
  private final LogType    log;

  JOGLDepthBufferAbstract(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("depth-buffer");

    final ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
    bb.order(ByteOrder.nativeOrder());
    this.depth_buffer_mask_cache = bb;
  }

  protected final void checkDepthBuffer()
    throws JCGLException
  {
    final int bits = this.depthBufferGetBits();
    if (bits == 0) {
      throw new JCGLExceptionNoDepthBuffer("No depth buffer available");
    }
  }

  @Override public final void depthBufferClear(
    final float depth)
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glClearDepth(depth);
    this.gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    JOGLErrors.check(this.gl);
  }

  @Override public final void depthBufferTestDisable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDisable(GL.GL_DEPTH_TEST);
    JOGLErrors.check(this.gl);
  }

  @Override public final void depthBufferTestEnable(
    final DepthFunction function)
    throws JCGLException
  {
    NullCheck.notNull(function, "Depth function");
    this.checkDepthBuffer();

    final int d = JOGL_GLTypeConversions.depthFunctionToGL(function);
    this.gl.glEnable(GL.GL_DEPTH_TEST);
    this.gl.glDepthFunc(d);
    JOGLErrors.check(this.gl);
  }

  @Override public final boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    this.checkDepthBuffer();
    final boolean e = this.gl.glIsEnabled(GL.GL_DEPTH_TEST);
    JOGLErrors.check(this.gl);
    return e;
  }

  @Override public final void depthBufferWriteDisable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDepthMask(false);
    JOGLErrors.check(this.gl);
  }

  @Override public final void depthBufferWriteEnable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDepthMask(true);
    JOGLErrors.check(this.gl);
  }

  @Override public final boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl
      .glGetBooleanv(GL.GL_DEPTH_WRITEMASK, this.depth_buffer_mask_cache);
    JOGLErrors.check(this.gl);

    final IntBuffer bi = this.depth_buffer_mask_cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  protected final GL getGL()
  {
    return this.gl;
  }

  protected final LogType getLog()
  {
    return this.log;
  }
}
