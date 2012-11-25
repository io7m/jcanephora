package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30PolygonModeTest
{
  /**
   * ∀m. polygonModeFromGL(polygonModeToGL(m)) == m.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final PolygonMode p : PolygonMode.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .polygonModeFromGL(GLInterfaceLWJGL30.polygonModeToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.polygonModeFromGL(-1);
  }
}
