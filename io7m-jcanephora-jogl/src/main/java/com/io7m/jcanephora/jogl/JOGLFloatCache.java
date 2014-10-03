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
import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM4F;

final class JOGLFloatCache implements JOGLFloatCacheType
{
  /**
   * The size of the float cache, in bytes.
   */

  private static final int FLOAT_CACHE_SIZE = 16 * 4;

  public static JOGLFloatCacheType newCache()
  {
    return new JOGLFloatCache();
  }

  private final FloatBuffer float_cache;
  private final ByteBuffer  float_cache_buffer;

  private JOGLFloatCache()
  {
    final ByteBuffer bb =
      ByteBuffer.allocateDirect(JOGLFloatCache.FLOAT_CACHE_SIZE);
    bb.order(ByteOrder.nativeOrder());
    this.float_cache_buffer = bb;

    final FloatBuffer ic = this.float_cache_buffer.asFloatBuffer();
    assert ic != null;
    this.float_cache = ic;
  }

  private void floatCacheReset()
  {
    this.float_cache.rewind();
    this.float_cache_buffer.rewind();
  }

  @Override public float getFloat(
    final GL gl,
    final int name)
    throws JCGLExceptionRuntime
  {
    final FloatBuffer cache = this.getFloatCache();
    gl.glGetFloatv(name, cache);
    return cache.get(0);
  }

  @Override public void getFloat2f(
    final GL gl,
    final int name,
    final VectorM2F v)
    throws JCGLExceptionRuntime
  {
    final FloatBuffer cache = this.getFloatCache();
    gl.glGetFloatv(name, cache);
    v.set2F(cache.get(0), cache.get(1));
  }

  @Override public void getFloat3f(
    final GL gl,
    final int name,
    final VectorM3F v)
    throws JCGLExceptionRuntime
  {
    final FloatBuffer cache = this.getFloatCache();
    gl.glGetFloatv(name, cache);
    v.set3F(cache.get(0), cache.get(1), cache.get(2));
  }

  @Override public void getFloat4f(
    final GL gl,
    final int name,
    final VectorM4F v)
    throws JCGLExceptionRuntime
  {
    final FloatBuffer cache = this.getFloatCache();
    gl.glGetFloatv(name, cache);
    v.set4F(cache.get(0), cache.get(1), cache.get(2), cache.get(3));
  }

  @Override public FloatBuffer getFloatCache()
  {
    this.floatCacheReset();
    return this.float_cache;
  }
}
