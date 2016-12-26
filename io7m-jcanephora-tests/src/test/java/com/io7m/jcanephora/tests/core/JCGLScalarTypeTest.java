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


package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import org.junit.Assert;
import org.junit.Test;

public final class JCGLScalarTypeTest
{
  @Test
  public void testFromUnsigned()
  {
    for (final JCGLUnsignedType v : JCGLUnsignedType.values()) {
      final JCGLScalarType t = JCGLScalarType.fromScalarUnsigned(v);
      final JCGLUnsignedType r = JCGLUnsignedType.fromScalar(t);
      Assert.assertEquals(v, r);
    }
  }

  @Test
  public void testFromScalarIntegral()
  {
    for (final JCGLScalarIntegralType v : JCGLScalarIntegralType.values()) {
      final JCGLScalarType t = JCGLScalarType.fromScalarIntegral(v);
      final JCGLScalarIntegralType r = JCGLScalarIntegralType.fromScalar(t);
      Assert.assertEquals(v, r);
    }
  }
}
