/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class JOGLColorBufferMasking implements JCGLColorBufferMaskingType
{
  private final GL3        gl;
  private final ByteBuffer cache;

  JOGLColorBufferMasking(final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c);
    this.gl = context.getGL3();
    this.cache = ByteBuffer.allocateDirect(4 * 4);
    this.cache.order(ByteOrder.nativeOrder());
  }

  private static ByteBuffer colorBufferMaskStatus(
    final GL gl,
    final ByteBuffer cache)
  {
    gl.glGetBooleanv(GL.GL_COLOR_WRITEMASK, cache);
    return cache;
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLException
  {
    this.gl.glColorMask(r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLException
  {
    return JOGLColorBufferMasking.colorBufferMaskStatus(
      this.gl, this.cache).get(3) != 0;
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLException
  {
    return JOGLColorBufferMasking.colorBufferMaskStatus(
      this.gl, this.cache).get(2) != 0;
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLException
  {
    return JOGLColorBufferMasking.colorBufferMaskStatus(
      this.gl, this.cache).get(1) != 0;
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLException
  {
    return JOGLColorBufferMasking.colorBufferMaskStatus(
      this.gl, this.cache).get(0) != 0;
  }
}
