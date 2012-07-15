package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class GLUnsignedTypeLWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testUnsignedBijection()
  {
    for (final GLUnsignedType u : GLUnsignedType.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .unsignedTypeFromGL(GLInterfaceLWJGL30.unsignedTypeToGL(u)), u);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testUnsignedFailure()
  {
    GLInterfaceLWJGL30.unsignedTypeFromGL(-1);
  }

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
