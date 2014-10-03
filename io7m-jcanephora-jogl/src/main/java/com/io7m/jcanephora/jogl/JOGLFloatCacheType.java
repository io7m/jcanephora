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

import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorM4F;

interface JOGLFloatCacheType
{
  float getFloat(
    final GL gl,
    final int name)
    throws JCGLExceptionRuntime;

  void getFloat2f(
    final GL gl,
    final int name,
    final VectorM2F v)
    throws JCGLExceptionRuntime;

  void getFloat3f(
    final GL gl,
    final int name,
    final VectorM3F v)
    throws JCGLExceptionRuntime;

  void getFloat4f(
    final GL gl,
    final int name,
    final VectorM4F v)
    throws JCGLExceptionRuntime;

  FloatBuffer getFloatCache();
}
