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

import com.io7m.jcanephora.core.JCGLBlendEquation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Conversion contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLBlendEquationContract
{
  protected abstract int toInt(JCGLBlendEquation d);

  protected abstract JCGLBlendEquation fromInt(int c);

  @Test
  public final void testBijection()
  {
    final JCGLBlendEquation[] vs = JCGLBlendEquation.values();
    for (int index = 0; index < vs.length; ++index) {
      final JCGLBlendEquation d = vs[index];
      Assert.assertEquals(d, this.fromInt(this.toInt(d)));
    }
  }
}
