/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.lwjgl3;

import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.lwjgl3.LWJGL3TypeConversions;
import com.io7m.jcanephora.tests.contracts.JCGLTextureWrapTContract;

public final class LWJGL3TextureWrapTTest extends
  JCGLTextureWrapTContract
{
  @Override
  protected int toInt(final JCGLTextureWrapT c)
  {
    return LWJGL3TypeConversions.textureWrapTToGL(c);
  }

  @Override
  protected JCGLTextureWrapT fromInt(final int c)
  {
    return LWJGL3TypeConversions.textureWrapTFromGL(c);
  }
}