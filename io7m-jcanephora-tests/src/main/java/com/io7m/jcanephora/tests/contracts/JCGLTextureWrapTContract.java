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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLTextureWrapT;
import org.junit.Assert;
import org.junit.Test;

/**
 * Wrapping conversion contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTextureWrapTContract
{
  protected abstract int toInt(JCGLTextureWrapT d);

  protected abstract JCGLTextureWrapT fromInt(int c);

  @Test
  public final void testBijection()
  {
    final JCGLTextureWrapT[] vs = JCGLTextureWrapT.values();
    for (int index = 0; index < vs.length; ++index) {
      final JCGLTextureWrapT d = vs[index];
      Assert.assertEquals(d, this.fromInt(this.toInt(d)));
    }
  }
}
