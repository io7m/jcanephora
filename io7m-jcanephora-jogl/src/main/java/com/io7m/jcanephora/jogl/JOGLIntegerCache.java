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
import javax.media.opengl.GL2ES2;

import com.io7m.jcanephora.JCGLExceptionRuntime;

final class JOGLIntegerCache implements JOGLIntegerCacheType
{
  /**
   * The size of the integer cache, in bytes.
   */

  private static final int INTEGER_CACHE_SIZE = 16 * 4;

  public static JOGLIntegerCacheType newCache()
  {
    return new JOGLIntegerCache();
  }

  private final IntBuffer  integer_cache;
  private final ByteBuffer integer_cache_buffer;

  private JOGLIntegerCache()
  {
    final ByteBuffer bb =
      ByteBuffer.allocateDirect(JOGLIntegerCache.INTEGER_CACHE_SIZE);
    bb.order(ByteOrder.nativeOrder());
    this.integer_cache_buffer = bb;

    final IntBuffer ic = this.integer_cache_buffer.asIntBuffer();
    assert ic != null;
    this.integer_cache = ic;
  }

  @Override public int getInteger(
    final GL gl,
    final int name)
    throws JCGLExceptionRuntime
  {
    final IntBuffer cache = this.getIntegerCache();
    gl.glGetIntegerv(name, cache);
    return cache.get(0);
  }

  @Override public IntBuffer getIntegerCache()
  {
    this.integerCacheReset();
    return this.integer_cache;
  }

  @Override public int getProgramInteger(
    final GL2ES2 g,
    final int id,
    final int name)
    throws JCGLExceptionRuntime
  {
    final IntBuffer cache = this.getIntegerCache();
    g.glGetProgramiv(id, name, cache);
    return cache.get(0);
  }

  @Override public int getShaderInteger(
    final GL2ES2 g,
    final int id,
    final int name)
    throws JCGLExceptionRuntime
  {
    final IntBuffer cache = this.getIntegerCache();
    g.glGetShaderiv(id, name, cache);
    return cache.get(0);
  }

  private void integerCacheReset()
  {
    this.integer_cache.rewind();
    this.integer_cache_buffer.rewind();
  }
}
