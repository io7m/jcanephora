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

public final class JCGLUnsignedTest
{
  @Test
  public void testFromScalar()
  {
    for (final JCGLScalarType v : JCGLScalarType.values()) {
      switch (v) {
        case TYPE_HALF_FLOAT:
        case TYPE_FLOAT:
        case TYPE_BYTE:
        case TYPE_INT:
        case TYPE_SHORT:
          continue;
        case TYPE_UNSIGNED_BYTE:
        case TYPE_UNSIGNED_INT:
        case TYPE_UNSIGNED_SHORT:
          break;
      }

      final JCGLUnsignedType t =
        JCGLUnsignedType.fromScalar(v);
      final JCGLScalarType r =
        JCGLScalarType.fromScalarUnsigned(t);
      Assert.assertEquals(v, r);
    }
  }

  @Test
  public void testFromScalarIntegral()
  {
    for (final JCGLScalarIntegralType v : JCGLScalarIntegralType.values()) {
      switch (v) {
        case TYPE_BYTE:
        case TYPE_INT:
        case TYPE_SHORT:
          continue;
        case TYPE_UNSIGNED_BYTE:
        case TYPE_UNSIGNED_INT:
        case TYPE_UNSIGNED_SHORT:
          break;
      }

      final JCGLUnsignedType t =
        JCGLUnsignedType.fromScalarIntegral(v);
      final JCGLScalarIntegralType r =
        JCGLScalarIntegralType.fromScalarUnsigned(t);
      Assert.assertEquals(v, r);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFloatingHalf()
  {
    JCGLUnsignedType.fromScalar(JCGLScalarType.TYPE_HALF_FLOAT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFloating()
  {
    JCGLUnsignedType.fromScalar(JCGLScalarType.TYPE_FLOAT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testByte()
  {
    JCGLUnsignedType.fromScalar(JCGLScalarType.TYPE_BYTE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShort()
  {
    JCGLUnsignedType.fromScalar(JCGLScalarType.TYPE_SHORT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInt()
  {
    JCGLUnsignedType.fromScalar(JCGLScalarType.TYPE_INT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegralByte()
  {
    JCGLUnsignedType.fromScalarIntegral(JCGLScalarIntegralType.TYPE_BYTE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegralShort()
  {
    JCGLUnsignedType.fromScalarIntegral(JCGLScalarIntegralType.TYPE_SHORT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegralInt()
  {
    JCGLUnsignedType.fromScalarIntegral(JCGLScalarIntegralType.TYPE_INT);
  }
}
