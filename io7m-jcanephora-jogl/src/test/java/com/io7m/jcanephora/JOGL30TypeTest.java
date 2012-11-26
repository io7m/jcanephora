package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLType.Type;

public final class JOGL30TypeTest
{
  /**
   * ∀t. typeFromGL(typeToGL(t)) = t.
   */

  @SuppressWarnings("static-method") @Test public void testTypeBijection()
  {
    for (final Type u : GLType.Type.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_JOGL_ES2_Actual
        .typeFromGL(GLInterfaceEmbedded_JOGL_ES2_Actual.typeToGL(u)), u);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testTypeFailure()
  {
    GLInterfaceEmbedded_JOGL_ES2_Actual.typeFromGL(-1);
  }
}
