package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class GLUnsignedTypeTest
{
  @SuppressWarnings("static-method") @Test public void testUnsignedSizes()
  {
    Assert.assertEquals(1, GLUnsignedType.TYPE_UNSIGNED_BYTE.getSizeBytes());
    Assert.assertEquals(2, GLUnsignedType.TYPE_UNSIGNED_SHORT.getSizeBytes());
    Assert.assertEquals(4, GLUnsignedType.TYPE_UNSIGNED_INT.getSizeBytes());
  }
}
