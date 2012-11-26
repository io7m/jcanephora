package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class JOGL30PolygonModeTest
{
  /**
   * ∀m. polygonModeFromGL(polygonModeToGL(m)) == m.
   */

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final PolygonMode p : PolygonMode.values()) {
      Assert.assertEquals(GLInterface_JOGL30
        .polygonModeFromGL(GLInterface_JOGL30.polygonModeToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterface_JOGL30.polygonModeFromGL(-1);
  }
}
