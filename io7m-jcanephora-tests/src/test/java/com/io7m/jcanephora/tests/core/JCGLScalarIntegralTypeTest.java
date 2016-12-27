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

public final class JCGLScalarIntegralTypeTest
{
  @Test
  public void testFromUnsigned()
  {
    for (final JCGLUnsignedType v : JCGLUnsignedType.values()) {
      final JCGLScalarIntegralType t =
        JCGLScalarIntegralType.fromScalarUnsigned(v);
      final JCGLUnsignedType r =
        JCGLUnsignedType.fromScalarIntegral(t);
      Assert.assertEquals(v, r);
    }
  }

  @Test
  public void testFromScalar()
  {
    for (final JCGLScalarType v : JCGLScalarType.values()) {
      switch (v) {
        case TYPE_HALF_FLOAT:
        case TYPE_FLOAT:
          continue;
        case TYPE_BYTE:
        case TYPE_INT:
        case TYPE_SHORT:
        case TYPE_UNSIGNED_BYTE:
        case TYPE_UNSIGNED_INT:
        case TYPE_UNSIGNED_SHORT:
          break;
      }

      final JCGLScalarIntegralType t =
        JCGLScalarIntegralType.fromScalar(v);
      final JCGLScalarType r =
        JCGLScalarType.fromScalarIntegral(t);
      Assert.assertEquals(v, r);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFloatingHalf()
  {
    JCGLScalarIntegralType.fromScalar(JCGLScalarType.TYPE_HALF_FLOAT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFloating()
  {
    JCGLScalarIntegralType.fromScalar(JCGLScalarType.TYPE_FLOAT);
  }
}
