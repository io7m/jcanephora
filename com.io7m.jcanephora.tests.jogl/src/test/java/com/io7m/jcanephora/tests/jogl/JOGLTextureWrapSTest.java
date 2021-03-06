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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.jogl.JOGLTypeConversions;
import com.io7m.jcanephora.tests.contracts.JCGLTextureWrapSContract;

public final class JOGLTextureWrapSTest extends
  JCGLTextureWrapSContract
{
  @Override
  protected int toInt(final JCGLTextureWrapS c)
  {
    return JOGLTypeConversions.textureWrapSToGL(c);
  }

  @Override
  protected JCGLTextureWrapS fromInt(final int c)
  {
    return JOGLTypeConversions.textureWrapSFromGL(c);
  }
}
