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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jnull.NullCheck;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class LWJGL3ColorBufferMasking implements JCGLColorBufferMaskingType
{
  private final ByteBuffer cache;

  LWJGL3ColorBufferMasking(final LWJGL3Context c)
  {
    final LWJGL3Context context = NullCheck.notNull(c, "Context");
    this.cache = ByteBuffer.allocateDirect(4 * 4);
    this.cache.order(ByteOrder.nativeOrder());
  }

  private static ByteBuffer colorBufferMaskStatus(
    final ByteBuffer cache)
  {
    GL11.glGetBooleanv(GL11.GL_COLOR_WRITEMASK, cache);
    return cache;
  }

  @Override
  public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLException
  {
    GL11.glColorMask(r, g, b, a);
  }

  @Override
  public boolean colorBufferMaskStatusAlpha()
    throws JCGLException
  {
    return
      colorBufferMaskStatus(this.cache).get(3) != 0;
  }

  @Override
  public boolean colorBufferMaskStatusBlue()
    throws JCGLException
  {
    return
      colorBufferMaskStatus(this.cache).get(2) != 0;
  }

  @Override
  public boolean colorBufferMaskStatusGreen()
    throws JCGLException
  {
    return
      colorBufferMaskStatus(this.cache).get(1) != 0;
  }

  @Override
  public boolean colorBufferMaskStatusRed()
    throws JCGLException
  {
    return
      colorBufferMaskStatus(this.cache).get(0) != 0;
  }
}
