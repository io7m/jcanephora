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

package com.io7m.jcanephora.tests.jogl;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.jogl.JOGL_GLTypeConversions;
import com.io7m.junreachable.UnreachableCodeException;

@SuppressWarnings({ "null", "static-method" }) public final class JOGLUnsignedTypeTest
{
  /**
   * ∀t. unsignedTypeFromGL(unsignedTypeToGL(t)) = t.
   */

  @Test public void testUnsignedBijection()
  {
    for (final JCGLUnsignedType u : JCGLUnsignedType.values()) {
      Assert.assertEquals(JOGL_GLTypeConversions
        .unsignedTypeFromGL(JOGL_GLTypeConversions.unsignedTypeToGL(u)), u);
    }
  }

  @Test(expected = UnreachableCodeException.class) public
    void
    testUnsignedFailure()
  {
    JOGL_GLTypeConversions.unsignedTypeFromGL(-1);
  }
}
