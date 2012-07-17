package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class GLScalarTypeTest
{
  @SuppressWarnings("static-method") @Test public void testScalarSizes()
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
