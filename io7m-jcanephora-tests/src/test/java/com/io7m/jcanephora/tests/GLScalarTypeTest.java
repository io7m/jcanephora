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

package com.io7m.jcanephora.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLType;

@SuppressWarnings("null") public final class GLScalarTypeTest
{
  private static boolean convertibleLoudly(
    final JCGLScalarType st,
    final JCGLType t,
    final int count)
  {
    final StringBuilder b = new StringBuilder();
    b.append("Convertible: ");
    b.append(st);
    b.append(" ");
    b.append(t);
    b.append(" ");
    b.append(count);

    System.out.println(b.toString());
    return st.shaderTypeConvertible(count, t);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testScalarConvertible()
  {
    for (final JCGLScalarType st : JCGLScalarType.values()) {
      switch (st) {
        case TYPE_SHORT:
        case TYPE_BYTE:
        case TYPE_UNSIGNED_BYTE:
        case TYPE_UNSIGNED_INT:
        case TYPE_UNSIGNED_SHORT:
        {
          for (int count = 1; count <= 4; ++count) {
            for (final JCGLType t : JCGLType.values()) {
              Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                st,
                t,
                count));
            }
          }
          break;
        }
        case TYPE_FLOAT:
        {
          for (final JCGLType t : JCGLType.values()) {
            switch (t) {
              case TYPE_FLOAT:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 1) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_BOOLEAN:
              case TYPE_BOOLEAN_VECTOR_2:
              case TYPE_BOOLEAN_VECTOR_3:
              case TYPE_BOOLEAN_VECTOR_4:
              case TYPE_FLOAT_MATRIX_2:
              case TYPE_FLOAT_MATRIX_3:
              case TYPE_FLOAT_MATRIX_4:
              case TYPE_INTEGER:
              case TYPE_INTEGER_VECTOR_2:
              case TYPE_INTEGER_VECTOR_3:
              case TYPE_INTEGER_VECTOR_4:
              case TYPE_SAMPLER_2D:
              case TYPE_SAMPLER_2D_SHADOW:
              case TYPE_SAMPLER_3D:
              case TYPE_SAMPLER_CUBE:
              {
                for (int count = 1; count <= 4; ++count) {
                  Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                    st,
                    t,
                    count));
                }
                break;
              }
              case TYPE_FLOAT_VECTOR_2:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 2) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_FLOAT_VECTOR_3:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 3) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_FLOAT_VECTOR_4:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 4) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
            }
          }
          break;
        }
        case TYPE_INT:
        {
          for (final JCGLType t : JCGLType.values()) {
            switch (t) {
              case TYPE_INTEGER:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 1) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_BOOLEAN:
              case TYPE_BOOLEAN_VECTOR_2:
              case TYPE_BOOLEAN_VECTOR_3:
              case TYPE_BOOLEAN_VECTOR_4:
              case TYPE_FLOAT:
              case TYPE_FLOAT_MATRIX_2:
              case TYPE_FLOAT_MATRIX_3:
              case TYPE_FLOAT_MATRIX_4:
              case TYPE_FLOAT_VECTOR_2:
              case TYPE_FLOAT_VECTOR_3:
              case TYPE_FLOAT_VECTOR_4:
              case TYPE_SAMPLER_2D:
              case TYPE_SAMPLER_2D_SHADOW:
              case TYPE_SAMPLER_3D:
              case TYPE_SAMPLER_CUBE:
              {
                for (int count = 1; count <= 4; ++count) {
                  Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                    st,
                    t,
                    count));
                }
                break;
              }
              case TYPE_INTEGER_VECTOR_2:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 2) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_INTEGER_VECTOR_3:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 3) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
              case TYPE_INTEGER_VECTOR_4:
              {
                for (int count = 1; count <= 4; ++count) {
                  if (count == 4) {
                    Assert.assertTrue(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  } else {
                    Assert.assertFalse(GLScalarTypeTest.convertibleLoudly(
                      st,
                      t,
                      count));
                  }
                }
                break;
              }
            }
          }
          break;
        }
      }
    }
  }

  @SuppressWarnings("static-method") @Test public void testScalarSizes()
  {
    Assert.assertEquals(1, JCGLScalarType.TYPE_BYTE.getSizeBytes());
    Assert.assertEquals(1, JCGLScalarType.TYPE_UNSIGNED_BYTE.getSizeBytes());

    Assert.assertEquals(2, JCGLScalarType.TYPE_SHORT.getSizeBytes());
    Assert.assertEquals(2, JCGLScalarType.TYPE_UNSIGNED_SHORT.getSizeBytes());

    Assert.assertEquals(4, JCGLScalarType.TYPE_INT.getSizeBytes());
    Assert.assertEquals(4, JCGLScalarType.TYPE_UNSIGNED_INT.getSizeBytes());

    Assert.assertEquals(4, JCGLScalarType.TYPE_FLOAT.getSizeBytes());
  }
}
