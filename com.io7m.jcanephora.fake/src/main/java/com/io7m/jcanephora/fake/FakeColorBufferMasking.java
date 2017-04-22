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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;

final class FakeColorBufferMasking implements JCGLColorBufferMaskingType
{
  private boolean current_r;
  private boolean current_g;
  private boolean current_b;
  private boolean current_a;

  FakeColorBufferMasking(final FakeContext c)
  {
    this.current_r = true;
    this.current_g = true;
    this.current_b = true;
    this.current_a = true;
  }

  @Override
  public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLException
  {
    this.current_r = r;
    this.current_g = g;
    this.current_b = b;
    this.current_a = a;
  }

  @Override
  public boolean colorBufferMaskStatusAlpha()
    throws JCGLException
  {
    return this.current_a;
  }

  @Override
  public boolean colorBufferMaskStatusBlue()
    throws JCGLException
  {
    return this.current_b;
  }

  @Override
  public boolean colorBufferMaskStatusGreen()
    throws JCGLException
  {
    return this.current_g;
  }

  @Override
  public boolean colorBufferMaskStatusRed()
    throws JCGLException
  {
    return this.current_r;
  }
}
