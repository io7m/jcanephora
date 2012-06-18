package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class GLScalarTypeJOGL30Test
{
  @Test public void testScalarBijection()
  {
    for (final GLScalarType t : GLScalarType.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .scalarTypeFromGL(GLInterfaceJOGL30.scalarTypeToGL(t)), t);
    }
  }

  @Test(expected = AssertionError.class) public void testScalarFailure()
  {
    GLInterfaceJOGL30.scalarTypeFromGL(-1);
  }

  @Test public void testScalarSizes()
  {
    Assert.assertEquals(
      1,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_BYTE));
    Assert.assertEquals(
      1,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_UNSIGNED_BYTE));

    Assert.assertEquals(
      2,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_SHORT));
    Assert.assertEquals(
      2,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_UNSIGNED_SHORT));

    Assert.assertEquals(
      4,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_INT));
    Assert.assertEquals(
      4,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_UNSIGNED_INT));

    Assert.assertEquals(
      4,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_FLOAT));
    Assert.assertEquals(
      8,
      GLScalarTypeMeta.getSizeBytes(GLScalarType.TYPE_DOUBLE));
  }
}
