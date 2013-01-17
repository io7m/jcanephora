package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jcanephora.GLType.Type;

public final class GLScalarTypeTest
{
  private static boolean convertibleLoudly(
    final GLScalarType st,
    final Type t,
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
    for (final GLScalarType st : GLScalarType.values()) {
      switch (st) {
        case TYPE_SHORT:
        case TYPE_BYTE:
        case TYPE_UNSIGNED_BYTE:
        case TYPE_UNSIGNED_INT:
        case TYPE_UNSIGNED_SHORT:
        {
          for (int count = 1; count <= 4; ++count) {
            for (final Type t : GLType.Type.values()) {
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
          for (final Type t : GLType.Type.values()) {
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
          for (final Type t : GLType.Type.values()) {
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
    Assert.assertEquals(1, GLScalarType.TYPE_BYTE.getSizeBytes());
    Assert.assertEquals(1, GLScalarType.TYPE_UNSIGNED_BYTE.getSizeBytes());

    Assert.assertEquals(2, GLScalarType.TYPE_SHORT.getSizeBytes());
    Assert.assertEquals(2, GLScalarType.TYPE_UNSIGNED_SHORT.getSizeBytes());

    Assert.assertEquals(4, GLScalarType.TYPE_INT.getSizeBytes());
    Assert.assertEquals(4, GLScalarType.TYPE_UNSIGNED_INT.getSizeBytes());

    Assert.assertEquals(4, GLScalarType.TYPE_FLOAT.getSizeBytes());
  }
}