package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class PolygonModeLWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final PolygonMode p : PolygonMode.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .polygonModeFromGL(GLInterfaceLWJGL30.polygonModeToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.polygonModeFromGL(-1);
  }
}
