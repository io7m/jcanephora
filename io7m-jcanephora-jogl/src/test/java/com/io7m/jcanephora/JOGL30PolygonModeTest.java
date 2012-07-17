package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class JOGL30PolygonModeTest
{
  /**
   * ∀m. polygonModeFromGL(polygonModeToGL(m)) == m.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final PolygonMode p : PolygonMode.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .polygonModeFromGL(GLInterfaceJOGL30.polygonModeToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.polygonModeFromGL(-1);
  }
}
