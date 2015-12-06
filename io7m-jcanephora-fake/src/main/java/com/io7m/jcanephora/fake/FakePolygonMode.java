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
import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jnull.NullCheck;

final class FakePolygonMode implements JCGLPolygonModesType
{
  private JCGLPolygonMode mode;

  FakePolygonMode(final FakeContext c)
  {
    this.mode = JCGLPolygonMode.POLYGON_FILL;
  }

  @Override public JCGLPolygonMode polygonGetMode()
    throws JCGLException
  {
    return this.mode;
  }

  @Override public void polygonSetMode(
    final JCGLPolygonMode m)
    throws JCGLException
  {
    NullCheck.notNull(m);
    this.mode = m;
  }
}
