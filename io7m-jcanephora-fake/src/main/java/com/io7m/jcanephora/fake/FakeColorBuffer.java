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
 * ACTION OF CONTRACT, NEFakeContextIGENCE OR OTHER TORTIOUS ACTION, ARISING
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.api.JCGLColorBufferType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable4FType;

final class FakeColorBuffer implements JCGLColorBufferType
{
  private boolean           alpha;
  private boolean           blue;
  private final FakeContext context;
  private boolean           green;
  private final LogType     log;
  private boolean           red;

  FakeColorBuffer(
    final FakeContext in_gl,
    final LogUsableType in_log)
  {
    this.context = NullCheck.notNull(in_gl, "FakeContext");
    this.log = NullCheck.notNull(in_log, "Log").with("color-buffer");
    this.red = true;
    this.green = true;
    this.blue = true;
    this.alpha = true;
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
  {

  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
  {

  }

  @Override public void colorBufferClearV3f(
    final VectorReadable3FType color)
  {
    NullCheck.notNull(color, "Color vector");
  }

  @Override public void colorBufferClearV4f(
    final VectorReadable4FType color)
  {
    NullCheck.notNull(color, "Color vector");
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
  {
    this.red = r;
    this.green = g;
    this.blue = b;
    this.alpha = a;
  }

  @Override public boolean colorBufferMaskStatusAlpha()
  {
    return this.alpha;
  }

  @Override public boolean colorBufferMaskStatusBlue()
  {
    return this.blue;
  }

  @Override public boolean colorBufferMaskStatusGreen()
  {
    return this.green;
  }

  @Override public boolean colorBufferMaskStatusRed()
  {
    return this.red;
  }
}
