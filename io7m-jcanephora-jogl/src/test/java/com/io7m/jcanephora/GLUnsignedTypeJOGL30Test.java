package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class GLUnsignedTypeJOGL30Test
{
  @Test public void testUnsignedBijection()
  {
    for (final GLUnsignedType u : GLUnsignedType.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .unsignedTypeFromGL(GLInterfaceJOGL30.unsignedTypeToGL(u)), u);
    }
  }

  @Test(expected = AssertionError.class) public void testUnsignedFailure()
  {
    GLInterfaceJOGL30.unsignedTypeFromGL(-1);
  }

  @Test public void testUnsignedSizes()
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
