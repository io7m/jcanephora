package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class LWJGL30TextureWrapTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.textureWrapFromGL(-1);
  }

  /**
   * ∀w. textureWrapFromGL(textureWrapToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final TextureWrap w : TextureWrap.values()) {
      Assert.assertEquals(w, GLInterfaceEmbedded_LWJGL_ES2_Actual
        .textureWrapFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .textureWrapToGL(w)));
    }
  }
}
