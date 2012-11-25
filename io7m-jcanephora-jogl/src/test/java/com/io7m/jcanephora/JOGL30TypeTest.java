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
      Assert.assertEquals(
        GLInterfaceJOGL30.typeFromGL(GLInterfaceJOGL30.typeToGL(u)),
        u);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testTypeFailure()
  {
    GLInterfaceJOGL30.typeFromGL(-1);
  }
}
