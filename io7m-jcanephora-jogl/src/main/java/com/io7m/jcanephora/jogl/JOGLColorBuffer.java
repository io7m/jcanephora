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

import javax.media.opengl.GL;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLColorBufferType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable4FType;

final class JOGLColorBuffer implements JCGLColorBufferType
{
  private static ByteBuffer colorBufferMaskStatus(
    final GL gl,
    final ByteBuffer cache)
    throws JCGLExceptionRuntime
  {
    gl.glGetBooleanv(GL.GL_COLOR_WRITEMASK, cache);
    return cache;
  }

  private final ByteBuffer color_buffer_mask_cache;
  private final GL         gl;
  private final LogType    log;

  JOGLColorBuffer(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("color-buffer");

    final ByteBuffer cb = ByteBuffer.allocateDirect(4 * 4);
    cb.order(ByteOrder.nativeOrder());
    this.color_buffer_mask_cache = cb;
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws JCGLExceptionRuntime
  {
    this.gl.glClearColor(r, g, b, 1.0f);
    this.gl.glClear(GL.GL_COLOR_BUFFER_BIT);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws JCGLExceptionRuntime
  {
    this.gl.glClearColor(r, g, b, a);
    this.gl.glClear(GL.GL_COLOR_BUFFER_BIT);
  }

  @Override public void colorBufferClearV3f(
    final VectorReadable3FType color)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(color, "Color vector");
    this.colorBufferClear3f(color.getXF(), color.getYF(), color.getZF());
  }

  @Override public void colorBufferClearV4f(
    final VectorReadable4FType color)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(color, "Color vector");
    this.colorBufferClear4f(
      color.getXF(),
      color.getYF(),
      color.getZF(),
      color.getWF());
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLExceptionRuntime
  {
    this.gl.glColorMask(r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLExceptionRuntime
  {
    final int a =
      JOGLColorBuffer.colorBufferMaskStatus(
        this.gl,
        this.color_buffer_mask_cache).get(3);
    return a != 0;
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLExceptionRuntime
  {
    final int b =
      JOGLColorBuffer.colorBufferMaskStatus(
        this.gl,
        this.color_buffer_mask_cache).get(2);
    return b != 0;
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLExceptionRuntime
  {
    final int g =
      JOGLColorBuffer.colorBufferMaskStatus(
        this.gl,
        this.color_buffer_mask_cache).get(1);
    return g != 0;
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLExceptionRuntime
  {
    final int r =
      JOGLColorBuffer.colorBufferMaskStatus(
        this.gl,
        this.color_buffer_mask_cache).get(0);
    return r != 0;
  }
}
