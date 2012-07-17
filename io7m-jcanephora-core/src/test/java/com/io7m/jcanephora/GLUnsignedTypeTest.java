package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class GLUnsignedTypeTest
{
  @SuppressWarnings("static-method") @Test public void testUnsignedSizes()
  {
    Assert.assertEquals(
      1,
      GLUnsignedTypeMeta.getSizeBytes(GLUnsignedType.TYPE_UNSIGNED_BYTE));
    Assert.assertEquals(
      2,
      GLUnsignedTypeMeta.getSizeBytes(GLUnsignedType.TYPE_UNSIGNED_SHORT));
    Assert.assertEquals(
      4,
      GLUnsignedTypeMeta.getSizeBytes(GLUnsignedType.TYPE_UNSIGNED_INT));
  }
}
